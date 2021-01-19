$(function () {
    var dom = document.getElementById("LineChart");
    var myChart = echarts.init(dom);
    var series = [];
    var legend = [];

    $.ajax({
        url: '/Home/interFaceflow',
        method: 'get',
        async: false,
        success: function (result) {
            for (var key in result) {
                if (key == "legend") {
                    legend = result.legend;
                } else {
                    series.push({
                        name: key,
                        type: 'line',
                        stack: '总量',
                        smooth: true,
                        data: result[key]
                    });
                }
            }
        }
    });

    option = {
        title: {
            text: ''
        },
        tooltip: {
            trigger: 'axis'
        },
        legend: {
            data: legend
        },
        grid: {
            left: '3%',
            right: '6%',
            bottom: '3%',
            containLabel: true
        },
        toolbox: {
            feature: {
                saveAsImage: {}
            }
        },
        xAxis: {
            type: 'category',
            boundaryGap: false,
            data: [getBeforeDate(6), getBeforeDate(5), getBeforeDate(4), getBeforeDate(3), getBeforeDate(2), getBeforeDate(1), getBeforeDate(0)]
        },
        yAxis: {
            type: 'value'
        },
        series: series
    };
    if (option && typeof option === "object") {
        myChart.setOption(option, true);
    }
})