//显示交易总量
function versionNumCharShow(data) {
    var dom = document.getElementById("payInforChart");
    //用于使chart自适应高度和宽度,通过窗体高宽计算容器高宽
    var myChart = resizeContainer(dom, 0.65 ,0.75);
    var colors = ['#d14a61'];
    let option = {
        title: {
            text: '汇款总量',
            //subtext: '',
            left: 'center'
        },
        tooltip: {
            trigger: 'axis',
            axisPointer: {
                type: 'cross',
                crossStyle: {
                    color: '#999'
                }
            }
        },
        legend: {
            data: ['交易量'],
            left: 20
        },
        toolbox: {
            feature: {
                magicType: {show: true, type: ['line', 'bar']},
                restore: {show: true},
                saveAsImage: {show: true}
            }
        },
        xAxis: {
            type: 'category',
            axisPointer: {
                type: 'shadow'
            },
            data: data.name,
            axisLabel: {
                interval: 0,
                formatter:function(value)
                {
                    var ret = "";//拼接加\n返回的类目项
                    var maxLength = 2;//每项显示文字个数
                    var valLength = value.length;//X轴类目项的文字个数
                    var rowN = Math.ceil(valLength / maxLength); //类目项需要换行的行数
                    if (rowN > 1)//如果类目项的文字大于3,
                    {
                        for (var i = 0; i < rowN; i++) {
                            var temp = "";//每次截取的字符串
                            var start = i * maxLength;//开始截取的位置
                            var end = start + maxLength;//结束截取的位置
                            //这里也可以加一个是否是最后一行的判断，但是不加也没有影响，那就不加吧
                            temp = value.substring(start, end) + "\n";
                            ret += temp; //凭借最终的字符串
                        }
                        return ret;
                    }
                    else {
                        return value;
                    }
                }
            }
        },
        yAxis: [{
            name: '交易量',
            type: 'value',
            splitLine:{
                show:false
            },
            boundaryGap: [0, '10%'],
            axisLabel: {
                formatter: '{value} 笔'
            },
        }],
        series: [{
            name: '交易量',
            type: 'bar',
            data: data.num,
        }]
    };
    myChart.setOption(option);
}
//显示交易总量和付款总额
function versionCharShow(data) {
    var dom = document.getElementById("payInforChart");
    //用于使chart自适应高度和宽度,通过窗体高宽计算容器高宽
    var myChart = resizeContainer(dom, 0.65 ,0.75);
    var colors = ['#5793f3', '#d14a61'];
    let option = {
        title: {
            text: '汇款总额与汇款总量',
            //subtext: '',
            left: 'center'
        },
        tooltip: {
            trigger: 'axis',
            axisPointer: {
                type: 'cross',
                crossStyle: {
                    color: '#999'
                }
            }
        },
        legend: {
            data: ['总金额', '交易量'],
            left: 20
        },
        toolbox: {
            feature: {
                magicType: {show: true, type: ['line', 'bar']},
                restore: {show: true},
                saveAsImage: {show: true}
            }
        },
        /*grid: {
             x: 40
        },*/
        xAxis: {
            type: 'category',
            axisPointer: {
                type: 'shadow'
            },
            data: data.name,
            axisLabel: {
                interval: 0,
                formatter:function(value)
                {
                    var ret = "";//拼接加\n返回的类目项
                    var maxLength = 2;//每项显示文字个数
                    var valLength = value.length;//X轴类目项的文字个数
                    var rowN = Math.ceil(valLength / maxLength); //类目项需要换行的行数
                    if (rowN > 1)//如果类目项的文字大于3,
                    {
                        for (var i = 0; i < rowN; i++) {
                            var temp = "";//每次截取的字符串
                            var start = i * maxLength;//开始截取的位置
                            var end = start + maxLength;//结束截取的位置
                            //这里也可以加一个是否是最后一行的判断，但是不加也没有影响，那就不加吧
                            temp = value.substring(start, end) + "\n";
                            ret += temp; //凭借最终的字符串
                        }
                        return ret;
                    }
                    else {
                        return value;
                    }
                }
            }
        },
        yAxis: [{
            name: '总金额',
            type: 'value',
            /*splitLine:{
                show:false
            },*/
            boundaryGap: [0, '10%'],
            axisLine: {
                lineStyle: {
                    color: colors[1]
                }
            },
            axisLabel: {
                formatter: '{value} 万元'
            },
        }, {
            name: '交易量',
            type: 'value',
            splitLine:{
                show:false
            },
            boundaryGap: [0, '10%'],
            axisLine: {
                lineStyle: {
                    color: colors[0]
                }},
            axisLabel: {
                formatter: '{value} 笔'
            },
        }],
        series: [{
            name: '总金额',
            type: 'bar',
            data: data.total,
            itemStyle:{
                normal:{ color:'#d14a61'}
            }
        }, {
            name: '交易量',
            type: 'bar',
            yAxisIndex: 1,
            data: data.num,
            itemStyle:{
                normal:{ color:'#5793f3'}
            }
        }]
    };
    myChart.setOption(option);
}

//显示对私对公情况
function personFlagCharShow(data) {
    var dom = document.getElementById("payInforChart");
    //用于使chart自适应高度和宽度,通过窗体高宽计算容器高宽
    var myChart = resizeContainer(dom, 0.62 ,0.75);
    var colors = ['#5793f3', '#d14a61'];
    let option = {
        title: {
            text: '公私付款情况',
            //subtext: '',
            left: 'center'
        },
        tooltip: {
            trigger: 'axis',
            axisPointer: {
                type: 'cross',
                crossStyle: {
                    color: '#999'
                }
            }
        },
        legend: {
            data: ['对公付款', '对私付款'],
            left: 20
        },
        toolbox: {
            feature: {
                magicType: {show: true, type: ['line', 'bar']},
                restore: {show: true},
                saveAsImage: {show: true}
            }
        },
        /*grid: {
             x: 40
        },*/
        xAxis: {
            type: 'category',
            axisPointer: {
                type: 'shadow'
            },
            data: data.name,
            axisLabel: {
                interval: 0,
                formatter:function(value)
                {
                    var ret = "";//拼接加\n返回的类目项
                    var maxLength = 2;//每项显示文字个数
                    var valLength = value.length;//X轴类目项的文字个数
                    var rowN = Math.ceil(valLength / maxLength); //类目项需要换行的行数
                    if (rowN > 1)//如果类目项的文字大于3,
                    {
                        for (var i = 0; i < rowN; i++) {
                            var temp = "";//每次截取的字符串
                            var start = i * maxLength;//开始截取的位置
                            var end = start + maxLength;//结束截取的位置
                            //这里也可以加一个是否是最后一行的判断，但是不加也没有影响，那就不加吧
                            temp = value.substring(start, end) + "\n";
                            ret += temp; //凭借最终的字符串
                        }
                        return ret;
                    }
                    else {
                        return value;
                    }
                }
            }
        },
        yAxis: [{
            name: '对公交易量',
            type: 'value',
            /*splitLine:{
                show:false
            },*/
            boundaryGap: [0, '10%'],
            axisLine: {
                lineStyle: {
                    color: colors[1]
                }
            },
            axisLabel: {
                formatter: '{value} 笔'
            },
        },{
            name: '对私交易量',
            type: 'value',
            splitLine:{
                show:false
            },
            boundaryGap: [0, '10%'],
            axisLine: {
                lineStyle: {
                    color: colors[0]
                }},
            axisLabel: {
                formatter: '{value} 笔'
            },
        }],
        series: [{
            name: '对公付款',
            type: 'bar',
            data: data.noperson,
            itemStyle:{
                normal:{ color:'#d14a61'}
            }
        }, {
            name: '对私付款',
            type: 'bar',
            yAxisIndex: 1,
            data: data.person,
            itemStyle:{
                normal:{ color:'#5793f3'}
            }
        }]
    };
    myChart.setOption(option);
}

//总笔数的趋势
function numlTrendChartShow(data) {
    var dom = document.getElementById("payInforChart");
    //用于使chart自适应高度和宽度,通过窗体高宽计算容器高宽
    var myChart = resizeContainer(dom, 0.62 ,0.75);
    let option = {
        visualMap: {
            show: false,
            type: 'continuous',
            seriesIndex: 0,
            min: 0,
            max: 100
        },
        title: {
            text: '支付总量',
            //subtext: '',
            left: 'center'
        },
        tooltip: {
            trigger: 'axis',
            axisPointer: {
                animation: false,
                crossStyle: {
                    color: '#999'
                }
            }
        },
        legend: {
            data: ['交易量'],
            left: 20
        },
        toolbox: {
            feature: {
                dataZoom: {
                    yAxisIndex: 'none'
                },
                restore: {},
                saveAsImage: {},
                y: 100
            }
        },
        axisPointer: {
            link: {xAxisIndex: 'all'}
        },
        xAxis: {
            type: 'category',
            axisLine: {onZero: true},
            splitLine: {
                show: false
            },
            boundaryGap: false,
            data: data.paydate,
        },
        dataZoom: [{
            type: 'slider',
            show: true,
            realtime: true,
            xAxisIndex: [0],
            bottom: -5,
            start: 60,
            end: 100 //初始化滚动条
        }, {
            type: 'inside',
            realtime: true,
            start: 80,
            end: 100,
            xAxisIndex: [0]
        }],
        yAxis: [{
            name: '交易量',
            type: 'value',
            boundaryGap: [0, '10%'],
            axisLabel: {
                formatter: '{value} 笔'
            },
        }],
        series: [{
            name: '交易量',
            type: 'line',
            smooth: true,//圆滑
            data: data.num,
        }]
    };
    myChart.setOption(option);
}

//显示交易总金额和总笔数的趋势
function payDateTrendChartShow(data) {
    var dom = document.getElementById("payInforChart");
    //用于使chart自适应高度和宽度,通过窗体高宽计算容器高宽
    var myChart = resizeContainer(dom, 0.62 ,0.75);
    var colors = ['#5793f3', '#d14a61'];
    let option = {
        /*visualMap: {
            show: false,
            type: 'continuous',
            seriesIndex: 0,
            min: 0,
            max: 100
        },*/
        title: {
            text: '支付总额与支付总量',
            //subtext: '',
            left: 'center'
        },
        tooltip: {
            trigger: 'axis',
            axisPointer: {
                animation: false,
                crossStyle: {
                    color: '#999'
                }
            }
        },
        legend: {
            data: ['总金额', '交易量'],
            left: 20
        },
        toolbox: {
            feature: {
                dataZoom: {
                    yAxisIndex: 'none'
                },
                restore: {},
                saveAsImage: {},
                y: 100
            }
        },
        axisPointer: {
            link: {xAxisIndex: 'all'}
        },
        xAxis: {
            type: 'category',
            axisLine: {onZero: true},
            splitLine: {
                show: false
            },
            boundaryGap: false,
            data: data.paydate,
            /*axisLabel: {
                show: true,
                showMinLabel: true,
                showMaxLabel: true
            }*/
        },
        dataZoom: [{
            type: 'slider',
            show: true,
            realtime: true,
            xAxisIndex: [0],
            bottom: -5,
            start: 60,
            end: 100 //初始化滚动条
        }, {
            type: 'inside',
            realtime: true,
            start: 80,
            end: 100,
            xAxisIndex: [0]
        }],
        yAxis: [{
            name: '总金额',
            type: 'value',
            boundaryGap: [0, '10%'],
            splitLine: {
                show: true,
                lineStyle: {
                    type: 'dashed'
                }
            },
            axisLine: {
                lineStyle: {
                    color: colors[1]
                }
            },
            axisLabel: {
                formatter: '{value} 万元'
            },
        }, {
            name: '交易量',
            type: 'value',
            boundaryGap: [0, '10%'],
            /*splitLine: {
                show: true,
                lineStyle: {
                    type: 'dashed'
                }
            },*/
            axisLine: {
                lineStyle: {
                    color: colors[0]
                }},
            axisLabel: {
                formatter: '{value} 笔'
            },
        }],
        series: [{
            name: '总金额',
            type: 'line',
            data: data.total,
            showSymbol: false,
            smooth: true,//圆滑
            hoverAnimation: false,
            itemStyle:{
                normal:{ color:'#d14a61'}
            }
        }, {
            name: '交易量',
            type: 'line',
            showSymbol: false,
            yAxisIndex: 1,
            smooth: true,//圆滑
            hoverAnimation: false,
            data: data.num,
            itemStyle:{
                normal:{ color:'#5793f3'}
            }
        }]
    };
    myChart.setOption(option);
}