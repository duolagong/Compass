$(function() {
	$("#mxContent #inputTrandate").daterangepicker({
		format: 'yyyy-MM-dd - yyyy-MM-dd'
	});
	getMxContent();
});

//获取后台数据
function getMxContent(){
	var acctId=$('#mxContent #input_acctid').val();
	var date=$('#mxContent #input_trandate').val().split(" - ");
	var postData={acctId:acctId,dateFrom:date[0],dateTo:date[1]};
	$.ajax({
		type:"post",
		dataType:"JSON",
		url:"/mxContent",
		contentType: "application/json",
		data:JSON.stringify(postData),
		success:function(result){
			lineChart(result);
			acctBalance(result);
		}
	});
}

//页面图形绘制
function mxContentChart(resultBal) {
	var dom = document.getElementById("acctMxContent");

	var resizeMainContainer = function () {
		dom.style.width = window.innerWidth * 0.79 + 'px';
		dom.style.height = window.innerHeight * 0.6 + 'px';
	};
	//设置div容器高宽
	resizeMainContainer();
	// 初始化图表
	var myChart = echarts.init(dom);
	$(window).on('resize',function(){//
		//屏幕大小自适应，重置容器高宽
		resizeMainContainer();
		myChart.resize();
	});

	var myChart = echarts.init(dom);
	var app = {};
	option = null;
	var dataCount=1440;
	var data = generateData(dataCount);
//	var timeData = [
//  	    '2020/3/30 1:00', '2020/3/30 2:00',
//			'2020/3/30 3:00', '2020/3/30 4:00', '2020/3/30 5:00',
//			'2020/3/30 6:00', '2020/3/30 7:00', '2020/3/30 8:00',
//			'2020/3/30 9:00', '2020/3/30 10:00', '2020/3/30 11:00',
//			'2020/3/30 12:00', '2020/3/30 13:00', '2020/3/30 14:00',
//			'2020/3/30 15:00', '2020/3/30 16:00', '2020/3/30 17:00',
//			'2020/3/30 18:00', '2020/3/30 19:00', '2020/3/30 20:00',
//			'2020/3/30 21:00', '2020/3/30 22:00', '2020/3/30 23:00',
//			'2020/3/30 24:00'
//	];

//	timeData = timeData.map(Function (str) {
//	    return str.replace('2020/', '');
//	});

	option = {
		title: {
			text: '',
			subtext: '',
			left: 'center'
		},
		tooltip: {
			trigger: 'axis',
			axisPointer: {
				animation: false
			}
		},
		legend: {
			data: ['付款', '收款'],
			left: 32
		},
		toolbox: {
			feature: {
				dataZoom: {
					yAxisIndex: 'none'
				},
				restore: {},
				saveAsImage: {}
			}
		},
		axisPointer: {
			link: {xAxisIndex: 'all'}
		},
		dataZoom: [
			{
				show: true,
				realtime: true,
				start: 30,
				end: 70,
				xAxisIndex: [0, 1]
			},
			{
				type: 'inside',
				realtime: true,
				start: 30,
				end: 70,
				xAxisIndex: [0, 1]
			}
		],
		grid: [{
			left: 50,
			right: 50,
			height: '35%'
		}, {
			left: 50,
			right: 50,
			top: '55%',
			height: '35%'
		}],
		xAxis: [
			{
				type: 'category',
				boundaryGap: false,
				axisLine: {onZero: true},
//	            data: timeData
				data: data.categoryData,
			},
			{
				gridIndex: 1,
				type: 'category',
				boundaryGap: false,
				axisLine: {onZero: true},
//	            data: timeData
				data: data.valueData1,
				position: 'top'
			}
		],
		yAxis: [
			{
				name: '付款',
				type: 'value',
			},
			{
				gridIndex: 1,
				name: '收款',
				type: 'value',
				inverse: true
			}
		],
		series: [{
			name: '付款',
			type: 'line',
			symbolSize: 8,
			hoverAnimation: false,
			data: data.valueData
		},{
			name: '收款',
			type: 'line',
			xAxisIndex: 1,
			yAxisIndex: 1,
			symbolSize: 8,
			hoverAnimation: false,
			data: data.valueData1
		}]
//	    series: [
//	        {
//	            name: '付款',
//	            type: 'line',
//	            symbolSize: 8,
//	            hoverAnimation: false,
//	            data: [
// 	            15000, 27000, 40000, 500000, 45000, 200000, 10000,
// 	            20000, 15000, 2000, 4000, 50000, 5000, 20000, 1000,
//				45000, 40000, 50000, 5000, 20000, 1000, 2000, 4000,
//				50000, 5000, 20000, 1000, 2000, 46000, 50000,
//				35000, 20000, 1000, 2000, 4000, 50000, 5000, 20000,
//				2000, 4000, 50000, 5000, 20000, 1000, 2000, 4000,
//				50000, 5000, 20000, 1000, 23000, 4000, 50000,
//				56000, 20000, 1000, 2000, 45000, 50000, 5000,
//				20000, 15000, 2000, 4000, 50000, 5000, 20000, 1000,
//				2000, 4000, 50000, 5000, 20000, 1000, 2000, 4000,
//				35000, 20000, 1000, 2000, 4000, 50000, 5000, 20000,
//				20000, 15000, 2000, 4000, 50000, 5000, 20000, 1000
//	            ]
//	        },
//	        {
//	            name: '收款',
//	            type: 'line',
//	            xAxisIndex: 1,
//	            yAxisIndex: 1,
//	            symbolSize: 8,
//	            hoverAnimation: false,
//	            data: [
// 	            50000, 5000, 20000, 1000, 23000, 4000, 50000,
//				56000, 20000, 1000, 2000, 45000, 50000, 5000,
//				20000, 15000, 2000, 4000, 50000, 5000, 20000, 1000,
//				2000, 4000, 50000, 5000, 20000, 1000, 2000, 4000,
//				35000, 20000, 1000, 2000, 4000, 50000, 5000, 20000,
//				20000, 15000, 2000, 4000, 50000, 5000, 20000, 1000,
//				50000, 5000, 20000, 1000, 23000, 4000, 50000,
//				56000, 20000, 1000, 2000, 45000, 50000, 5000,
//				20000, 15000, 2000, 4000, 50000, 5000, 20000, 1000,
//				2000, 4000, 50000, 5000, 20000, 1000, 2000, 4000,
//				35000, 20000, 1000, 2000, 4000, 50000, 5000, 20000,
//				20000, 15000, 2000, 4000, 50000, 5000, 20000, 1000
//	            ]
//	        }
//	    ]
	};

	if (option && typeof option === "object") {
		myChart.setOption(option, true);
	}
}

function generateData(count) {
	debugger;
    var baseValue = Math.random() * 1000;
    var time = +new Date(2020, 0, 1);
	var time1 = +new Date(2020, 0, 1);
    var smallBaseValue;

    function next(idx) {
        smallBaseValue = idx % 30 === 0
            ? Math.random() * 700
            : (smallBaseValue + Math.random() * 2000 - 250);
        baseValue += Math.random() * 20 - 10;
        return Math.max(
            0,
            Math.round(baseValue + smallBaseValue) + 3000
        );
    }

    var categoryData = [];
	var categoryData1 = [];
    var valueData = [];
    var valueData1 = [];

    for (var i = 0; i < count; i++) {
    	var num=Math.random()*10;
       if(num>7){
           valueData.push(next(i).toFixed(2));
           categoryData.push(echarts.format.formatTime('hh:mm:ss', time));
		   time +=60000;
       }
       if(3>num){
    	   valueData1.push(next(i).toFixed(2));
    	   categoryData1.push(echarts.format.formatTime('hh:mm:ss', time1));
		   time1 +=60000;
       }
    }

    return {
        categoryData: categoryData,
		categoryData1: categoryData1,
        valueData: valueData,
        valueData1: valueData1
    };
}