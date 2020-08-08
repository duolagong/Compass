$(function() {
    var myChart = echarts.init(document.getElementById("PieChart"));
    var app = {};

    var arr=[];
    $.ajax({
        url:'/Home/personFlag',
        method: 'get',
        async:false,
        dataType : "json",
        success:function(result){
            arr=result;
        }
    })
    option = null;
    option = {
        title: {
            text:getBeforeDate(0),
            subtext: '',
            left: 'right'
        },
        tooltip: {
            trigger: 'item',
            formatter: '{a} <br/>{b} : {c} ({d}%)'
        },
        legend: {
            orient: 'vertical',
            left: '',
            data: ['银行对公', '银行对私', '内部对私','内部对公']
        },
        series: [
            {
                name: '交易来源',
                type: 'pie',
                radius: '55%',
                center: ['50%', '60%'],
                data :arr,
                // data: [
                //     {value: 678, name: '内部对私'},
                //     {value: 1154, name: '内部对公'},
                //     {value: 646, name: '银行对公'},
                //     {value: 1255, name: '银行对私'}
                // ],
                emphasis: {
                    itemStyle: {
                        shadowBlur: 10,
                        shadowOffsetX: 0,
                        shadowColor: 'rgba(0, 0, 0, 0.5)'
                    }
                }
            }
        ]
    };
    ;
    if (option && typeof option === "object") {
        myChart.setOption(option, true);
    }
})