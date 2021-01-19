$(function () {
    var dom = document.getElementById("memoryUtilizationChart");
    //用于使chart自适应高度和宽度,通过窗体高宽计算容器高宽
    var myChart = resizeContainer(dom, 0.45, 0.45);
    var data = [];
    /*var initData = initMemory();//向后台获取初始化数据*/
    let startTime = initSysMemoryData.startTime; //开始时间
    let endTime = initSysMemoryData.endTime; //结束时间
    data = initSysMemoryData.utilizationsData; //数据

    let option = {
        visualMap: {
            show: false,
            type: 'continuous',
            seriesIndex: 0,
            min: 0,
            max: 100
        },
        title: {
            text: '内存使用率（%）',
            textStyle: {
                color: '#fbfff9'
            }
        },
        tooltip: {
            trigger: 'axis',
            formatter: function (params) {
                params = params[0];
                return dateModified(params.value[0]) + "\n" + params.value[1]+"%";
            },
            axisPointer: {
                animation: true
            }
        },
        grid: {
            x: 40,
            x2: 40,
            y: 65,
        },
        xAxis: {
            type: 'time',
            //interval: 1000 * 60 * 5, // 固定x轴时间间隔
            min: startTime,   // 开始时间
            max: endTime,   // 结束时间
            /*splitLine: {
                show: false
            },*/
            axisLabel: {
                show: true,
                showMinLabel: true,
                showMaxLabel: true,
                textStyle: {
                    color: '#fbfff9',  //更改坐标轴文字颜色
                    fontSize: 11      //更改坐标轴文字大小
                },
                formatter: function (value, index) {
                    return dateModified(value);
                }
            },
            axisLine: {
                lineStyle: {
                    color: '#fbfff9' //更改坐标轴颜色
                }
            }
        },
        yAxis: {
            type: 'value',
            max: 100,
            boundaryGap: [0, '100%'],
            splitLine: {
                show: true
            },
            axisLabel: {
                /*show: true,*/
                formatter: '{value}'+'%',
                textStyle: {
                    color: '#fbfff9',  //更改坐标轴文字颜色
                    fontSize: 11      //更改坐标轴文字大小
                }
            },
            axisLine: {
                lineStyle: {
                    color: '#fbfff9' //更改坐标轴颜色
                }
            }
        },
        series: [{
            name: '后端数据',
            type: 'line',
            showSymbol: false,
            hoverAnimation: false,
            data: data
        }]
    };
    myChart.setOption(option);
    var arr = [];
    var mytimer;
    function timer() {
        startTime = startTime + 1000 * 60;
        endTime = endTime + 1000 * 60;
        $.ajax({
            url:'/sysMemory/utilization/'+endTime,
            method: 'get',
            async:false,
            dataType : "json",
            success:function(result){
                arr=result.data;
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
        mytimer = setTimeout(timer,60*1000);
    };
    timer();
})