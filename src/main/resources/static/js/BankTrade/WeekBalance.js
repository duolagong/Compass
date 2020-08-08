$(function(){
	$("#acctBalance #input_trandate").daterangepicker();
	$("#acctBalance #queryModal_date").daterangepicker({
		singleDatePicker:true,
		showDropdowns:true,
		minYear:2017,
		maxYear:parseInt(moment().format('YYYY'),10)
	});
	$("#acctBalance #input_select_acctid").selectpicker({
		noneSelectedText : '请选择所属银行'
	});
	// getAcctBalance();
})

//获取后台数据
function getAcctBalance(){
	var agentid=$('#acctBalance #input_agentid').val();
	var acctid=$('#acctBalance #input_select_acctid').val();
	var curcode=$('#acctBalance #input_curcode').val();
	var date=$('#acctBalance #input_trandate').val().split(" - ");
	var postData={agentid:agentid,acctid:acctid,curcode:curcode,dateFrom:date[0],dateTo:date[1]};
	$.ajax({
		type:"post",
		dataType:"JSON",
		url:"/acctBalance",
		contentType: "application/json",
		data:JSON.stringify(postData),
		success:function(result){
			myAlert(result.status,result.errMsg);
			if(result.status){
				lineChart(result.data);
				acctBalance(result.data);
			}
		}
	});
}

$('#acctBalance #input_agentid').change(function(){
	$('#acctBalance #input_select_acctid').selectpicker('destroy');
	var agentid=$('#acctBalance #input_agentid').val();
	if (!agentid) return;
	$.ajax({
		type:"get",
		url:"/bankAcctid/"+agentid,
		async:true,
		success:function (data) {
			var str = "";
			for(var i = 0;i<data.length;i++){
				str+='<option>'+data[i].acctid+'</option>'
			}
			$("#acctBalance #input_select_acctid").html(str);

			$("#acctBalance #input_select_acctid" ).selectpicker('refresh');
		}
	});
})

$('#acctBalance #acctBalance_btn_query').on('click',function () {
	debugger;
	var acctid = $('#acctBalance #input_select_acctid').val();
	if(!acctid){
		myAlert(false,"请选择银行账户!");
		return false;
	}
	$("#acctBalance_queryModal #queryModal_acctid").val(acctid);
	$("#acctBalance_queryModal #queryModal_agentid").val($('#acctBalance #input_agentid').val());
	$("#acctBalance_queryModal #queryModal_curcode").val($('#acctBalance #input_curcode').val());
	$("#acctBalance_queryModal #resultXml").val("本功能仅作为查询账户余额使用，不会影响核心现存数据!");
	$("#acctBalance_queryModal").modal("show");
})

$('#acctBalance_queryModal #acctBalance_btn_1032').on('click',function () {
	debugger;
	var agentid = $('#acctBalance_queryModal #queryModal_agentid').val();
	var acctid = $('#acctBalance_queryModal #queryModal_acctid').val();
	var curcode = $('#acctBalance_queryModal #queryModal_curcode').val();
	var txdate = $('#acctBalance_queryModal #queryModal_date').val();
	$.ajax({
		type:"get",
		url:"/acctBalance/message/"+agentid+"/"+acctid+"/"+curcode+"/"+txdate,
		async:true,
		success : function(result) {
			myAlert(result.status,result.errMsg);
			$("#acctBalance_queryModal #resultXml").val(result.data);
		}
	});
})

$('#acctBalance #acctBalance_btn_show').on('click',function () {
	getAcctBalance();
	/*$('#acctBalance button[name="refresh"]').bootstrapTable('refresh');*/
})

//页面图形绘制
function lineChart(resultBal) {
	var dom = document.getElementById("WeekBalanceChart");

	var resizeMainContainer = function () {
		dom.style.width = window.innerWidth*0.6+'px';
		dom.style.height = window.innerHeight*0.54+'px';
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
/*	var app = {};*/
	option = null;
	option = {
		title: {
			text: '账户余额变动',
			subtext: ''
		},
		tooltip: {
			trigger: 'axis'
		},
		legend: {
			data: ['账户余额', '可用余额']
		},
		toolbox: {
			show: true,
			feature: {
				dataZoom: {
					yAxisIndex: 'none'
				},
				dataView: {readOnly: false},
				magicType: {type: ['line', 'bar']},
				restore: {},
				saveAsImage: {}
			}
		},
		xAxis: {
			type: 'category',
			boundaryGap: false,
			data : resultBal.dateBal,
		},
		yAxis: {
			type: 'value',
			axisLabel: {
				formatter: '{value}'
			}
		},
		series: [
			{
				name: '账户余额',
				type: 'line',
				data : resultBal.acctBal,
				markPoint: {
					data: [
						{type: 'max', name: '最大值'}
						/*{type: 'min', name: '最大值'}*/
					]
				},
				markLine: {
					data: [
						{type: 'average', name: '平均值'}
					]
				}
			},
			{
				name: '可用余额',
				type: 'line',
				data : resultBal.avlBal,
				markPoint: {
					data: [
						{type: 'max', name: '最大值'}
					]
				},
				markLine: {
					data: [
						{type: 'average', name: '平均值'},
//	                    [{
//	                        symbol: 'none',
//	                        x: '90%',
//	                        yAxis: 'max'
//	                    }, {
//	                        symbol: 'circle',
//	                        label: {
//	                            position: 'start',
//	                            formatter: '最大值'
//	                        },
//	                        type: 'max',
//	                        name: '最高点'
//	                    }]
					]
				}
			}
		]
	};
	;
	if (option && typeof option === "object") {
		myChart.setOption(option);
		// window.onresize = myChart.resize;
	}
}

//构建表格
function acctBalance(resultBal){
	$("#acctBal_table").bootstrapTable('destroy');//先销毁
	$("#acctBal_table").bootstrapTable({
		data : resultBal.table,
		/*theadClasses: "thead-light",*/
		/*striped: true, //是否显示行间隔色*/
		/*showToggle: true,
		showRefresh: true,*/
		classes: "table table-bordered table-sm table-info",
		cache: false, //是否使用缓存，默认为true，
		columns : [
		   {
			field : 'ACCTID',
			title : '账户号',
			align : 'center',
			width : 100
		},{
			field : 'AGENTID',
			title : '银行编号',
			align : 'center',
			width : 80
		},{
			field : 'ACCTBAL',
			title : '账户余额',
			align : 'center',
			width : 80,
			formatter: function (value, row, index) {
				return (value || 0).toString().replace(/(\d)(?=(?:\d{3})+$)/g, '$1,')+"元";
			}
		},{
			field : 'AVLBAL',
			title : '可用余额',
			align : 'center',
			width : 100,
			formatter: function (value, row, index) {
				return (value || 0).toString().replace(/(\d)(?=(?:\d{3})+$)/g, '$1,')+"元";
			}
		},{
			field : 'CURCODE',
			title : '币种',
			align : 'center',
			width : 100
		},{
			field : 'TXDATE',
			title : '交易时间',
			align : 'center',
			width : 150,
			formatter : function (value ,row ,index) {
				return value.substring(0,4)+"/"+value.substring(4,6)+"/"+value.substring(6,8)
			}
		} ]
	});
}