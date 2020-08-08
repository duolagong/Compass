$(function () {
    var dom = document.getElementById("RadarMap");
    var myChart = echarts.init(dom);
    var app = {};
    option = null;
    var dataArr = [
        [
            [20, 0],
            [130, 0]
        ],
        [
            [30, 1],
            [130, 1]
        ],
        [
            [40, 1.5],
            [130, 2]
        ],
        [
            [10, 2.5],
            [130, 3]
        ],
        [
            [20, 3.5],
            [130, 4]
        ],
        [
            [20, 5.5],
            [130, 5]
        ],
        [
            [40, 5.5],
            [130, 6]
        ],
        [
            [20, 6.5],
            [130, 7]
        ],
        [
            [60, 8],
            [130, 8]
        ],
        [
            [20, 9],
            [130, 9]
        ]
    ]
    var series = []
    for (var i = 0; i < dataArr.length; i++) {
        series.push({
            coordinateSystem: 'polar',
            name: 'line',
            type: 'line',
            z: 20,
            lineStyle: {
                color: '#000000',
                width: 1
            },
            symbol: 'circle',
            symbolSize: 10,
            itemStyle: {
                color: '#00cc33'
            },
            data: dataArr[i]
        })
    }
    option = {
        backgroundColor: '#ffffff',
        grid: {
            left: '10%',
            right: '10%'
        },

        angleAxis: {
            interval: 1,
            type: 'category',
            data: ['正常\nICBC',
                '{a|530}\nBOC',
                '{a|180+万}\nABC',
                '{a|512+亿}\nCCB',
                '{a|12326}\nCOMM',
                '{a|1.65+亿}\nNBK1',
                '{a|4500}\nATOM',
                '{a|2500}\nA.OSB',
                '{a|1300}\nECDS',
                '{a|5323}\nNBK2'
            ],
            z: 10,
            axisLine: {
                show: false,
                lineStyle: {
                    color: "#6666ff",
                    width: 1,
                    type: "dashed",
                    opacity: 0.7
                }
            },
            axisLabel: {
                interval: 0,
                show: true,
                color: "#000000",
                margin: 2,
                fontSize: 15,
                lineHeight: 15,
                align: 'center',
                rich: {
                    a: {
                        align: 'center',
                    }
                }
            },
            axisTick: {
                show: true
            }
        },
        radiusAxis: {
            min: 0,
            max: 100,
            interval: 20,
            axisLine: {
                show: false,
                lineStyle: {
                    color: "#red",
                    width: 1,
                    type: "dashed"
                }
            },
            axisTick: {
                show: false
            },
            axisLabel: {
                show: false
            },
            splitLine: {
                lineStyle: {
                    color: "#1d51a3",
                    width: 1,
                    type: "dashed",
                    opacity: 0.7
                }
            },
            splitArea: {
                show: true,
                areaStyle: {
                    color: '#f5f5f5',
                    opacity: 0.5
                }
            }
        },
        polar: {},
        // geo: {
        //     map: 'china',
        //     left: '75%',
        //     right: '75%',
        //     z: 15,
        //     label: {
        //         emphasis: {
        //             show: false
        //         }
        //     },
        //     mapType: 'china',
        //     itemStyle: {
        //         normal: {
        //             areaColor: '#red',
        //             borderColor: '#3dacdd'
        //         },
        //         emphasis: {
        //             areaColor: '#3dacdd'
        //         }
        //     }
        // },
        series: series
    };;
    if (option && typeof option === "object") {
        myChart.setOption(option, true);
    }
})