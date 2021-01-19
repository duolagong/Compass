$(function () {
    var dom = document.getElementById("tradeAnalysisChat");
    var resizeMainContainer = function () {
        dom.style.width = window.innerWidth+'px';
        dom.style.height = window.innerHeight*0.7+'px';
    };
    resizeMainContainer();
    $("#tradeAnalysis #inputSingleDate").daterangepicker({
        singleDatePicker:true,
        showDropdowns:true,
        minYear:2017,
        maxYear:parseInt(moment().format('YYYY'),10)
    });
    /*var queryDate=$('#tradeAnalysis #inputSingleDate').val();
    tradeAnalysis(queryDate);*/
});

$("#tradeAnalysis #inputSingleDate").on('change',function(){
    $("#loading").fadeIn(500);
    $("#loading").fadeOut(3000);
    $("#tradeAnalysisHide").click();
    var queryDate=$('#tradeAnalysis #inputSingleDate').val();
    tradeAnalysis(queryDate);
});

/*$("#tradeAnalysis #tradeAnalysis_btn_get").on('click',function(){
    $("#loading").fadeIn(500);
    $("#loading").fadeOut(3000);
    $("#tradeAnalysisHide").click();
    var queryDate=$('#tradeAnalysis #inputSingleDate').val();
    tradeAnalysis(queryDate);
});*/

function tradeAnalysis(queryDate){
    //定义容器大小
    var myChart = echarts.init(document.getElementById("tradeAnalysisChat"));

    myChart.showLoading({
        text: '数据正在努力加载...',
        textStyle: { fontSize : 30 , color: '#444' },
        effectOption: {backgroundColor: 'rgba(0, 0, 0, 0)'}
    });

    /*$(window).on('resize',function(){
        resizeMainContainer();
        myChart.resize();
    });*/
    //获取数据
    option = null;
    var bank= [];
    var legend = [];
    var payTime = [];
    $.ajax({
        url: '/TradeAnalysis/'+queryDate,
        method: 'get',
        async: false,
        data:queryDate,
        success: function (result) {
            for (var key in result) {
                if (key == "bankId") {
                    bank = result.bankId;
                } else {
                    payTime = result.payTimeData;
                }
            }
            myChart.hideLoading();
            /*$("#loading").fadeOut(3000);*/
        }
    });
    var hours = ['01:00', '02:00', '03:00', '04:00', '05:00', '06:00',
        '07:00', '08:00', '09:00','10:00','11:00', '12:00', '13:00',
        '14:00', '15:00', '16:00', '17:00', '18:00', '19:00', '20:00',
        '21:00', '22:00', '23:00' ,'24:00'];

    //数据拼装
    option = {
        tooltip: {
            trigger: 'item',
            formatter: function (params) {
                debugger;
                var color = params.color;//图例颜色
                var htmlStr ='<div>';
                htmlStr += '明细:' + '<br/>';//x轴的名称
                htmlStr += '<span ></span>';
                //添加一个汉字，这里你可以格式你的数字或者自定义文本内容
                var value = params.value;
                htmlStr += hours[value[0]-1]+'-'+hours[value[0]]+'<br/>';
                htmlStr += bank[value[1]] + '共发生'+value[2]+ '笔付款</div>';
                return htmlStr;
            }
        },
        legend: {
            orient: 'vertical',
            left: '',
            /*data: ['1-工行', '2-农行', '3-中行', '4-建行', '5-交行', '6-中信', '7-兴业']*/
            data: legend
        },
        visualMap: {
            max: 60,
            inRange: {
                color: ['#313695', '#4575b4', '#74add1', '#abd9e9', '#e0f3f8', '#ffffbf', '#fee090', '#fdae61', '#f46d43', '#d73027', '#a50026']
            }
        },
        xAxis3D: {
            name:'时间',
            type: 'category',
            data: hours,
            nameGap : 20
        },
        yAxis3D: {
            name:'',
            type: 'category',
            data: bank
        },
        grid3D: {
            boxWidth: 200,
            boxDepth: 100,
            bottom : 10,
            axisTick:{
                show : true
            },
            postEffect:{
                colorCorrection:{
                    enable:true
                }
            },
            axisPointer:{
                show : true
            },
            axisLine:{
                interval:'auto'
            },
            axisLabel:{
                show:true
            },
            splitArea:{
                show:true
                /* areaStyle:color = ['rgba(250,250,250,0.3)','rgba(200,200,200,0.3)']*/
            },
            viewControl: {
                rotateSensitivity: [1, 0],
                 distance:600,
                orthographicSize:175,
                projection: 'orthographic',
                autoRotate:true,//自旋
                autoRotateDirection : 'cw',
                autoRotateSpeed : 10
            },
            light: {
                main: {
                    intensity: 1.0,
                    alpha:45,
                    beta:45,
                    shadow: false/* 影子 */
                },
                ambient: {
                    color : '#fff',
                    intensity: 0.4
                }
            }
        },
        zAxis3D: {
            name:'交易量',
            type: 'value'
        },
        series: [{
            type: 'bar3D',
            data: payTime.map(function (item) {
                return {
                    value: [item[1], item[0], item[2]],
                }
            }),
            shading: 'lambert',

            label: {
                textStyle: {
                    fontSize: 16,
                    borderWidth: 1
                }
            },

            emphasis: {
                label: {
                    textStyle: {
                        fontSize: 24,
                        color: '#900'
                    }
                },
                itemStyle: {
                    color: '#900'
                }
            }
        }]
    };;
    if (option && typeof option === "object") {
        myChart.setOption(option, true);
    }
};