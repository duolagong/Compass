$(function () {
    var dom = document.getElementById("versionProportion");
    var myChart = echarts.init(dom);
    var app = {};
    option = null;
    option = {
        title: {
            /*text: 'Version付款占比',*/
            left: '10%'
        },
        tooltip: {
            trigger: 'item',
            formatter: '{a} <br/>{b} : {c} ({d}%)'
        },
        // legend: {
        //     left: 'center',
        //     top: '90%',
        //     data: ['二院', '三院', '四院', '航天晨光', '航天信息', '集团', '七院', '六院']
        // },
        toolbox: {
            show: true,
            feature: {
                mark: {show: true},
                dataView: {show: true, readOnly: false},
                magicType: {
                    show: true,
                    type: ['pie', 'funnel']
                },
                restore: {show: true},
                saveAsImage: {show: true}
            }
        },
        series: [{
            name: '当日交易量',
            type: 'pie',
            radius: [35, 120],
            center: ['50%', '60%'],
            roseType: 'area',
            data: [
                {value: 10, name: '二院'},
                {value: 5, name: '三院'},
                {value: 15, name: '四院'},
                {value: 25, name: '航天晨光'},
                {value: 20, name: '航天信息'},
                {value: 35, name: '集团'},
                {value: 30, name: '七院'},
                {value: 40, name: '六院'},
                {value: 15, name: '五院'},
                {value: 25, name: '九院'},
                {value: 20, name: '十院'},
                {value: 35, name: '八院'},
                {value: 30, name: '航天云网'},
                {value: 40, name: '宏华'}
            ]
        }
        ]
    };
    ;
    if (option && typeof option === "object") {
        myChart.setOption(option, true);
    }
})