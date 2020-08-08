$(function () {
    var dom = document.getElementById("WeeklyTrading");
    var myChart = echarts.init(dom);
    var app = {};
    var series = [];
    var legend = [];
    $.ajax({
        url: '/Home/weeklyTrad',
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
                        areaStyle: {},
                        data: result[key]
                    });
                }
            }
        }
    });

    option = {
        tooltip: {
            trigger: 'axis',
            axisPointer: {
                type: 'cross',
                label: {
                    backgroundColor: '#6a7985'
                }
            }
        },
        legend: {
            data: legend
        },
        toolbox: {
            feature: {
                saveAsImage: {}
            }
        },
        grid: {
            left: '4%',
            right: '6%',
            bottom: '3%',
            containLabel: true
        },
        xAxis: [
            {
                type: 'category',
                boundaryGap: false,
                data: [getBeforeDate(6), getBeforeDate(5), getBeforeDate(4), getBeforeDate(3), getBeforeDate(2), getBeforeDate(1), getBeforeDate(0)]
            }
        ],
        yAxis: [
            {
                type: 'value'
            }
        ],
        series: series
    };
    ;
    if (option && typeof option === "object") {
        myChart.setOption(option, true);
    }
})

function getBeforeDate(n) {
    var n = n;
    var d = new Date();
    var year = d.getFullYear();
    var mon = d.getMonth() + 1;
    var day = d.getDate();
    if (day <= n) {
        if (mon > 1) {
            mon = mon - 1;
        } else {
            year = year - 1;
            mon = 12;
        }
    }
    d.setDate(d.getDate() - n);
    year = d.getFullYear();
    mon = d.getMonth() + 1;
    day = d.getDate();
    s = (mon < 10 ? ('0' + mon) : mon) + "-" + (day < 10 ? ('0' + day) : day);
    return s;
}
