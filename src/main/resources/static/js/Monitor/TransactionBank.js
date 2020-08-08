$(function(){
	var dom = document.getElementById("transactionBank");
	var myChart = echarts.init(dom);
	var BnakData;
	var app = {};
	option = null;
	$.ajax({
		url:'SysRule!transactionBankShow.do',
		method:'get',
		async:false,
		success:function(result){
			BnakData=result;
		}
	})
	setTimeout(function() {
		option = {
			legend : {},
			tooltip : {
				trigger : 'axis',
				showContent : false
			},
			dataset : {
				source : BnakData
			/*	source : [[ 'product', '2012', '2013', '2014', '2015', '2016', '2017' ],
							  [ '工商银行', 41.1, 30.4, 65.1, 53.3, 83.8, 98.7 ],
							  [ '农业银行', 86.5, 92.1, 85.7, 83.1, 73.4, 55.1 ],
							  [ '中国银行', 24.1, 67.2, 79.5, 86.4, 65.2, 82.5 ],
							  [ '建设银行', 55.2, 67.1, 69.2, 72.4, 53.9, 39.1 ],
							  [ '交通银行', 55.2, 67.1, 69.2, 72.4, 53.9, 39.1 ]] */
			},
			xAxis : {
				type : 'category'
			},
			yAxis : {
				gridIndex : 0
			},
			grid : {
				top : '55%'
			},
			series : [ {
				type : 'line',
				smooth : true,
				seriesLayoutBy : 'row'
			}, {
				type : 'line',
				smooth : true,
				seriesLayoutBy : 'row'
			}, {
				type : 'line',
				smooth : true,
				seriesLayoutBy : 'row'
			}, {
				type : 'line',
				smooth : true,
				seriesLayoutBy : 'row'
			},{
				type : 'line',
				smooth : true,
				seriesLayoutBy : 'row'
			},{
				type : 'pie',
				id : 'pie',
				radius : '30%',
				center : [ '50%', '25%' ],
				label : {
					formatter : '{b}: {@2012} ({d}%)'
				},
				encode : {
					itemName : 'product',
					value : '2012',
					tooltip : '2012'
				}
			} ]
		};

		myChart.on('updateAxisPointer', function(event) {
			var xAxisInfo = event.axesInfo[0];
			if (xAxisInfo) {
				var dimension = xAxisInfo.value + 1;
				myChart.setOption({
					series : {
						id : 'pie',
						label : {
							formatter : '{b}: {@[' + dimension + ']} ({d}%)'
						},
						encode : {
							value : dimension,
							tooltip : dimension
						}
					}
				});
			}
		});
		myChart.setOption(option);
	});
	if (option && typeof option === "object") {
		myChart.setOption(option, true);
	}
})