$(function () {
    get();
})

//获取数据
function get() {
    $.ajax({
        type:"get",
        dataType:"JSON",
        url:"/ecosphere",
        contentType: "application/json",
        success:function(result){
            /*getMessage(result.nodes);*/
            getRadarMap(result.nodes,result.links);
            ecosphereTable(result.nodes);
        }
    });
}

/*function getMessage(nodes) {
    var message="";
    for (let i = 0; i < nodes.length; i++) {
        if(nodes[i].alarm !=undefined){
            message='<p3 style=\'color: red\'>'+message+nodes[i].name+" : "+nodes[i].alarm+'</p3>\n';
            /!*message=message+nodes[i].name+":"+nodes[i].alarm+"\n";*!/
        }
        $("#message").val('<p3 style=\'color: red\'>'+nodes[i].name+" : "+nodes[i].alarm+'</p3>\n');
    }
}*/

//构建雷达预警图
function getRadarMap(nodes,links) {
    var dom = document.getElementById("RadarMap");

    var resizeMainContainer = function () {
        dom.style.width = window.innerWidth*0.45+'px';
        dom.style.height = window.innerHeight*0.655+'px';
    };
    //设置div容器高宽
    resizeMainContainer();
    // 初始化图表
    var myChart = echarts.init(dom);
    $(window).on('resize',function(){//
        //屏幕大小自适应，重置容器高宽
        resizeMainContainer();
        myChart.resize();
    });

    var myChart = echarts.init(dom);

    for (let i = 0; i < nodes.length; i++) {
        var angle= (360 / nodes.length * i);
        if(angle==0){
            nodes[i].angle = 360;
        }else{
            nodes[i].angle = angle;
        }
    }

    nodes[nodes.length] = {
        name: '注册中心',
        img: 'Compass',
        value: [0, 0]
    }


    var charts = {
        nodes: [],
        links: [],
        linesData: []
    }

    var dataMap = new Map();
    for (var j = 0; j < nodes.length; j++) {
        var node = {
            name: nodes[j].name,
            value: nodes[j].value || [1, nodes[j].angle],
            symbolSize: 30,
            alarm: nodes[j].alarm,
            // symbol: 'image:/static/' + nodes[j].img,
            symbol: 'image://../img/Monitor/'+nodes[j].img+'.jpg',
            //+ nodes[j].img +'.jpg',
            itemStyle: {
                normal: {
                    color: '#191aff',
                }
            }
        }
        dataMap.set(nodes[j].name, node.value)
        charts.nodes.push(node)
    }
    for (var i = 0; i < links.length; i++) {
        var link = {
            source: links[i].source,
            target: links[i].target,
            label: {
                normal: {
                    show: true,
                    formatter: links[i].name
                }
            },
            lineStyle: {
                normal: {
                    color: '#ffffff'
                }
            }
        }
        charts.links.push(link)

        // 组装动态移动的效果数据
        var lines = {
            coords: [
                dataMap.get(links[i].source),
                dataMap.get(links[i].target)
            ]
        }

        charts.linesData.push(lines)
    }
    option = {
        title: {
            text: ''
        },
        backgroundColor: "#ffffff",
        polar: {},
        radiusAxis: {
            show: false
        },
        angleAxis: {
            type: 'value',
            show: false
        },
        series: [{
            type: 'graph',
            layout: 'none',
            coordinateSystem: 'polar',
            symbolSize: 30,
            label: {
                normal: {
                    show: true,//先不展示名称
                    position: 'bottom',
                    color: '#12b5d0'
                }
            },
            lineStyle: {
                normal: {
                    width: 2,
                    shadowColor: 'red'
                }
            },
            edgeSymbolSize: 9,
            data: charts.nodes,
            links: charts.links,
            nodes: charts.nodes,
            itemStyle: {
                normal: {
                    label: {
                        show: true,
                        formatter: function(item) {
                            return item.data.name
                        }
                    }
                }
            }
        }, {
            name: 'A',
            type: 'lines',
            coordinateSystem: 'polar',
            lineStyle: {
                color: '#a6c84c',
                width: 1,
                opacity: 0.6,
                curveness: 0.5
            },
            effect: {
                show: true,
                trailLength: 0,
                symbol: 'triangle',
                color: '#12b5d0',
                symbolSize: 9
            },
            data: charts.linesData
        }]
    };
    // 警报效果
    setInterval(() => {
        var dataI = []
        for (var n = 0; n < nodes.length; n++) {
            var alarm = nodes[n].alarm
            if (alarm !== null && alarm !== '' && alarm !== undefined) {
                option.series[0].data[n].symbolSize = 60
                option.series[0].data[n].label = {
                    normal: {
                        color: '#DC143C'
                    }
                }
                option.series[0].itemStyle.normal.label.formatter = function(item) {
                    if (item.data.alarm !== undefined) {
                        /*return item.data.name + '\n[ ' + item.data.alarm + ' ]'*/
                        return '[失踪' + item.data.alarm + '分钟]\n'+item.data.name
                    } else {
                        return item.data.name
                    }
                }
                dataI.push(n)
            }
        }
        myChart.setOption(option)
        setTimeout(() => {
            for (var m = 0; m < dataI.length; m++) {
                option.series[0].data[dataI[m]].symbolSize = 30
                option.series[0].data[dataI[m]].label = {
                    normal: {
                        color: '#12b5d0'
                    }
                }
                option.series[0].itemStyle.normal.label.formatter = function(item) {
                    return item.data.name
                }
            }
            myChart.setOption(option)
        }, 500)
    }, 1000);
}

//构建表格
function ecosphereTable(nodes) {
    debugger;
    $("#ecosphere_table").bootstrapTable('destroy');//先销毁
    $("#ecosphere_table").bootstrapTable({
        data : nodes,
        /*theadClasses: "thead-light",
        striped: true, //是否显示行间隔色*/
        classes: "table table-bordered table-sm table-success",
        cache: false, //是否使用缓存，默认为true，
        columns : [
            {
                field : 'name',
                title : '系统',
                align : 'center',
                width : 80
            },{
                field : 'name',
                title : '系统名称',
                align : 'center',
                width : 200,
                formatter:function (value, row, index) {
                    var systemData=[{id:'atom',text:'核心系统'}, {id:'ebank1',text:'网银资管系统1'}, {id:'ebank2',text:'网银资管系统2'},
                        {id:'atomosb',text:'核心OSB'}, {id:'nbkosb',text:'网银OSB'}, {id:'icbc',text:'中国工商银行前置'},
                        {id:'abc',text:'中国农业银行前置'},{id:'boc',text:'中国银行前置'},{id:'ccb',text:'中国建设银行'},
                        {id:'comm',text:'中国交通银行前置'},{id:'ecds',text:'电票财司端'},{id:'compass',text:'Compass'}]
                    for (var i = 0, l = systemData.length; i < l; i++) {
                        var g = systemData[i];
                        if (g.id == value) value= g.text;
                    }
                    return value;
                }
            },{
                field : 'address',
                title : '系统地址',
                align : 'center',
                width : 200
            },{
                field : 'alarm',
                title : '在线情况',
                align : 'center',
                width : 150,
                formatter: function (value, row, index) {
                    var time;
                    if (value>=5){
                        return "<span class=\"text-danger\"><i class=\"ion ion-ios-trending-down\" aria-hidden=\"true\"></i>"+value+"分钟</span>";
                    }else if (5>value>0){
                        return "<span class=\"text-warning\"><i class=\"ion ion-ios-trending-down\" aria-hidden=\"true\"></i>"+value+"分钟</span>";
                    }else{
                        return "<span class=\"text-success\"><i class=\"ion ion-ios-trending-up\" aria-hidden=\"true\"></i>正常</span>";
                    }}
            }]
    });
}