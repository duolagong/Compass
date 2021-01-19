$(function () {
    var dom = document.getElementById("trafficMonitorChart");
    //用于使chart自适应高度和宽度,通过窗体高宽计算容器高宽
    var myChart = resizeContainer(dom, 0.92 ,0.72);
    var data = [];
    let startTime;
    let endTime;

    function initTrafficMonitor() {
        $.ajax({
            url: '/trafficMonitor/init',
            method: 'get',
            async: false,
            dataType: "json",
            success: function (result) {
                if (result.status) {
                    data = result.data;
                    startTime = data.startTime;
                    endTime = data.endTime;
                } else {
                    myAlert(result.status, result.errMsg);
                }
            }
        })
    }

    initTrafficMonitor();

    let option = {
        visualMap: {
            show: false,
            type: 'continuous',
            seriesIndex: 0,
            min: 0,
            max: 100
        },
        title: {
            text: '支付通实时访问量（24h）',
            //subtext: '',
            left: 'center'
        },
        tooltip: {
            trigger: 'axis',
            axisPointer: {
                animation: false
            }
        },
        legend: {
            data: ['查询访问量', '接口访问量'],
            left: 20
        },
        toolbox: {
            feature: {
                dataZoom: {
                    yAxisIndex: 'none'
                },
                restore: {},
                saveAsImage: {}
            }
        },
        axisPointer: {
            link: {xAxisIndex: 'all'}
        },
        tooltip: {
            trigger: 'axis',
            formatter: function (params) {
                var queryParams = params[0];
                var interfaceParams = params[1];
                return dateModified(queryParams.value[0]) + "<br />查询访问量:&nbsp;" + queryParams.value[1] + "次"
                    + "<br />接口访问量:&nbsp;" + interfaceParams.value[1] + "次";
            },
            axisPointer: {
                type: 'cross',
                animation: false,
                label: {
                    backgroundColor: '#505765'
                },
            }
        },
        grid: {
            x: 40,
            x2: 40,
            y: 65,
        },
        xAxis: {
            type: 'time',
            axisLine: {onZero: true},
            //interval: 1000 * 60 * 10, // 固定x轴时间间隔
            min: startTime,   // 开始时间
            max: endTime,   // 结束时间
            splitLine: {
                show: false
            },
            axisLabel: {
                show: true,
                showMinLabel: true,
                showMaxLabel: true,
                formatter: function (value, index) {
                    // 格式化成时分秒
                    let date = new Date(value);
                    let nowHours = timeAdd0(date.getHours().toString());
                    let nowMin = timeAdd0(date.getMinutes().toString());
                    let nowSeconds = timeAdd0(date.getSeconds().toString());
                    var texts = [nowHours, nowMin, nowSeconds];
                    return texts.join(':');
                }
            }
        },
        dataZoom: [{
            type: 'slider',
            show: true,
            realtime: true,
            xAxisIndex: [0],
            bottom: -5,
            start: 95,
            end: 100 //初始化滚动条
        }, {
            type: 'inside',
            realtime: true,
            start: 95,
            end: 100,
            xAxisIndex: [0]
        }],
        yAxis: {
            name: '业务数量',
            type: 'value',
            boundaryGap: [0, '100%'],
            splitLine: {
                show: true,
                lineStyle: {
                    type: 'dashed'
                }
            },
            axisLabel: {
                /*show: true,*/
                formatter: '{value}'
            }
        },
        series: [{
            name: '查询访问量',
            type: 'line',
            //symbolSize: 4, //原点大小
            showSymbol: false,
            smooth: true,//圆滑
            hoverAnimation: false,
            data: data.initData
        }, {
            name: '接口访问量',
            type: 'line',
            showSymbol: false,
            smooth: true,//圆滑
            hoverAnimation: false,
            data: data.initData2
        }]
    };
    myChart.setOption(option);
    var arr = [];
    var mytimer;

    function timer() {
        startTime = startTime + 1000 * 60;
        endTime = endTime + 1000 * 60;
        $.ajax({
            url: '/sysMemory/utilization/' + endTime,
            method: 'get',
            async: false,
            dataType: "json",
            success: function (result) {
                arr = result.data;
            }
        })
        data.push(arr);
        myChart.setOption({
            series: [{
                data: data
            }],
            xAxis: [{
                min: startTime,
                max: endTime,
            }]
        });
        mytimer = setTimeout(timer, 60 * 1000);
    };
    /*timer();*/
})