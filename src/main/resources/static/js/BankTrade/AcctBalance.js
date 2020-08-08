$(function(){
	var dom = document.getElementById("AcctBalance");
	var myChart = echarts.init(dom,'macarons');
	var app = {};
	option = null;
	option = {
	    title: {
	        text: '一天账户余额分布',
	        subtext: '数据为虚构内容，仅供本论文使用'
	    },
	    tooltip: {
	        trigger: 'axis',
	        axisPointer: {
	            type: 'cross'
	        }
	    },
	    toolbox: {
	        show: true,
	        feature: {
	            saveAsImage: {}
	        }
	    },
	    xAxis: {
	        type: 'category',
	        boundaryGap: false,
	        data: ['00:00', '01:15', '02:30', '03:45', '05:00', '06:15', '07:30', '08:45', '10:00', '11:15', '12:30', '13:45', '15:00', '16:15', '17:30', '18:45', '20:00', '21:15', '22:30', '23:45']
	    },
	    yAxis: {
	        type: 'value',
	        axisLabel: {
	            formatter: '{value} 万'
	        },
	        axisPointer: {
	            snap: true
	        }
	    },
	    visualMap: {
	        show: false,
	        dimension: 0,
	        pieces: [{
	            lte: 1,
	            color: 'green'
	        }, {
	            gt: 1,
	            lte: 2,
	            color: 'red'
	        }, {
	            gt: 2,
	            lte: 14,
	            color: 'green'
	        }, {
	            gt: 14,
	            lte: 16,
	            color: 'red'
	        }, {
	            gt: 16,
	            color: 'green'
	        }]
	    },
	    series: [
	        {
	            name: '余额',
	            type: 'line',
	            smooth: true,
	            data: [600, 280, 250, 650, 500, 300, 550, 500, 700, 750, 620, 490, 500, 640, 800, 850, 950, 600, 550, 550],
	            markArea: {
	                data: [ [{
	                    name: 'MIN警报',
	                    xAxis: '01:15'
	                }, {
	                    xAxis: '02:30'
	                }], [{
	                    name: 'MAX警报',
	                    xAxis: '17:30'
	                }, {
	                    xAxis: '20:00'
	                }] ]
	            }
	        }
	    ]
	};;
	if (option && typeof option === "object") {
	    myChart.setOption(option, true);
	}
})