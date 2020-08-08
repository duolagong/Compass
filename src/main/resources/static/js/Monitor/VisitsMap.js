$(function () {
    var dom = document.getElementById("visitsMap");
    var myChart = echarts.init(dom);
    var app = {};
    option = null;
    option = {
        tooltip: {
            trigger: 'axis',
            axisPointer: {
                type: 'cross',
                crossStyle: {
                    color: '#999'
                }
            }
        },
        toolbox: {
            feature: {
                dataView: {show: true, readOnly: false},
                magicType: {show: true, type: ['line', 'bar']},
                restore: {show: true},
                saveAsImage: {show: true}
            }
        },
        legend: {
            data: [ '交易量', '访问量']
        },
        xAxis: [
            {
                type: 'category',
                data: ['1月', '2月', '3月', '4月', '5月', '6月', '7月', '8月', '9月', '10月', '11月', '12月'],
                axisPointer: {
                    type: 'shadow'
                }
            }
        ],
        yAxis: [
            {
                type: 'value',
                name: '交易量',
                min: 0,
                max: 250,
                interval: 50,
                axisLabel: {
                    formatter: '{value} 次'
                }
            },
            {
                type: 'value',
                name: '访问量',
                min: 0,
                max: 25,
                interval: 5,
                axisLabel: {
                    formatter: '{value} 次'
                }
            }
        ],
        series: [
            {
                name: '降水量',
                type: 'bar',
                data: [2.6, 5.9, 9.0, 26.4, 28.7, 70.7, 175.6, 182.2, 48.7, 18.8, 6.0, 2.3]
            },
            {
                name: '平均温度',
                type: 'line',
                yAxisIndex: 1,
                data: [2.0, 2.2, 3.3, 4.5, 6.3, 10.2, 20.3, 23.4, 23.0, 16.5, 12.0, 6.2]
            }
        ]
    };
    ;
    if (option && typeof option === "object") {
        myChart.setOption(option, true);
    }
})