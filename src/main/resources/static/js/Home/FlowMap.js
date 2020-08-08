$(function(){
	var dom = document.getElementById("FlowMap");

	/*//用于使chart自适应高度和宽度,通过窗体高宽计算容器高宽
	var resizeMainContainer = function () {
		/!*dom.style.width = window.innerWidth*0.5+'px';*!/
		dom.style.height = window.innerHeight*0.6+'px';
	};
	//设置div容器高宽
	resizeMainContainer();
	// 初始化图表
	var myChart = echarts.init(dom);
	$(window).on('resize',function(){//
		//屏幕大小自适应，重置容器高宽
		resizeMainContainer();
		myChart.resize();
	});*/

	var myChart = echarts.init(dom);
	var app = {};
	option = null;
	var BJsheng=[];
	var BJshi=[];
	var BJche=[];
	var geoCoordMap=[];
	$.ajax({
		url: '/Home/flowMap',
		method: 'get',
		async: false,
		success: function (result) {
			BJsheng = result.BJshi;
			BJshi = result.BJshi;
			BJche = result.BJche;
			geoCoordMap = result.Target;
			flowMap(result);
		}
	});
	//北京
	var BJobj = {};
	for (var i = 0; i < BJsheng.length; i++) {
		BJobj[BJshi[i]] = BJsheng[i];
	}
	var BJData = [];
	for (var i = 0; i < BJche.length; i++) {
		var arr = [];
		var obj1 = {};
		obj1.name = "北京市";
		var obj2 = {};
		obj2.name = BJshi[i];
		obj2.value = BJche[i];

		arr.push(obj1);
		arr.push(obj2);
		BJData.push(arr);
	}
	debugger;
	var carPath = 'path://M1705.06,1318.313v-89.254l-319.9-221.799l0.073-208.063c0.521-84.662-26.629-121.796-63.961-121.491c-37.332-0.305-64.482,36.829-63.961,121.491l0.073,208.063l-319.9,221.799v89.254l330.343-157.288l12.238,241.308l-134.449,92.931l0.531,42.034l175.125-42.917l175.125,42.917l0.531-42.034l-134.449-92.931l12.238-241.308L1705.06,1318.313z';


	var convertData = function (data) {
		// console.log(data);
		var res = [];
		for (var i = 0; i < data.length; i++) {
			var dataItem = data[i];
			var fromCoord = geoCoordMap[dataItem[0].name];
			var toCoord = geoCoordMap[dataItem[1].name];
			if (fromCoord && toCoord) {
				res.push({
					fromName: dataItem[0].name,
					toName: dataItem[1].name,
					coords: [fromCoord, toCoord],
					value: dataItem[1].value
				});
			}
		}
		return res;
	};
	debugger;
	var color = ['#ffa022'];
	var series = [];
	[['北京', BJData]].forEach(function (item, i) {
		series.push({
				name: item[0],
				type: 'lines',
				zlevel: 1,
				effect: {
					show: true,
					period: 6,
					trailLength: 0.7,
					color: '#fff',
					symbolSize: 3
				},
				lineStyle: {
					normal: {
						color: color[i],
						width: 0,
						curveness: 0.2
					}
				},
				data: convertData(item[1])
			},
			{
				name: item[0],
				type: 'lines',
				zlevel: 2,
				symbol: ['none', 'arrow'],
				symbolSize: 5,
				effect: {
					show: true,
					period: 6,
					trailLength: 0.02,//0
					symbol: 'arrow',//carPath
					symbolSize: 5//[10, 15]
				},
				lineStyle: {
					normal: {
						color: color[i],
						width: 1,
						opacity: 0.6,
						curveness: 0.2
					}
				},
				data: convertData(item[1])
			},
			{
				name: item[0],
				type: 'effectScatter',
				coordinateSystem: 'geo',
				zlevel: 2,
				rippleEffect: {
					brushType: 'stroke'
				},
				label: {
					normal: {
						show: true,
						position: 'right',
						formatter: '{b}'
					}
				},
				symbolSize: function (val) {
					return val[2] / 300;
				},
				itemStyle: {
					normal: {
						color: color[i]
					}
				},
				data: item[1].map(function (dataItem) {
					/*debugger
					console.log(i);
					console.log(dataItem)*/
					var name = "";
					if (dataItem[0].name == "北京市") {
						name = BJobj[dataItem[1].name];
					}

					return {
						name: name ? name : dataItem[1].name,
						value: geoCoordMap[dataItem[1].name].concat([dataItem[1].value])
					};
				})
			});
	});

	var option = {
		backgroundColor: '#fff',
		title: {
			text: '',
			subtext: '',
			left: 'center',
			textStyle: {
				color: '#fff'
			}
		},
		tooltip: {
			trigger: 'item',
			formatter: function (params, ticket, callback) {
				// console.log(params);
				if (params.seriesType == "effectScatter") {
					return "汇款流向:" + params.data.name + "" + params.data.value[2];
				} else if (params.seriesType == "lines") {
					return params.data.fromName + ">" + params.data.toName + "<br />" + params.data.value;
				} else {
					return params.name;
				}
			}
		},
		legend: {
			orient: 'vertical',
			top: 'bottom',
			left: 'right',
			data: ['北京市'],
			textStyle: {
				color: '#fff'
			},
			selectedMode: 'multiple',
		},
		geo: {
			map: 'china',
			label: {
				emphasis: {
					show: true,
					color: '#fff'
				}
			},
			roam: true,
			itemStyle: {
				normal: {
					areaColor: '#323c48',
					borderColor: '#404a59'
				},
				emphasis: {
					areaColor: '#CC3300'
				}
			}
		},
		series: series
	};
	;
	if (option && typeof option === "object") {
		myChart.setOption(option, true);
	}
})

//构建表格
function flowMap(resultFlow){
	debugger;
	console.log(resultFlow.table);
	$("#flowMap_table").bootstrapTable('destroy');//先销毁
	$("#flowMap_table").bootstrapTable({
		data : resultFlow.table,
		/*theadClasses: "thead-light",*/
		/*striped: true, //是否显示行间隔色*/
		/*showToggle: true,
		showRefresh: true,*/
		classes: "able table-bordered table-striped table-sm",
		cache: false, //是否使用缓存，默认为true，
		columns : [
			{
				field : 'city',
				title : '城市',
				align : 'center',
				width : 100
			},{
				field : 'payamount',
				title : '总金额',
				align : 'center',
				width : 100
			},{
				field : 'paynum',
				title : '总笔数',
				align : 'center',
				width : 100
			},{
				field : 'ratio',
				title : '占比',
				align : 'center',
				width : 100
			} ]
	});
}