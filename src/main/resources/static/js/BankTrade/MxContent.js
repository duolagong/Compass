$(function() {
    $("#mxContent #input_date").daterangepicker({
        singleDatePicker:true,
        /*showDropdowns:true,*/
        minYear:2017,
        maxYear:parseInt(moment().format('YYYY'),10),
    });
    $("#mxContent #input_select_acctid").selectpicker({
        noneSelectedText : '请选择所属银行'
    });
    mxContentTable();
});

//查询按钮
$("#mxContent #mxContent_btn_get").on('click',function(){
    /*$('#mxContent button[name="refresh"]').click();*/
    mxContentTable();
});

$('#mxContent #input_agentid').change(function(){
    debugger;
    $('#mxContent #input_select_acctid').selectpicker('destroy');
    var agentid=$('#mxContent #input_agentid').val();
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
            $("#mxContent #input_select_acctid").html(str);

            $("#mxContent #input_select_acctid" ).selectpicker('refresh');
        }
    });
})

//获取后台数据
function getMxContentParams(params){
    debugger;
    var agentid=$('#mxContent #input_agentid').val();
    var acctid=$('#mxContent #input_select_acctid').val();
    var amount=$('#mxContent #input_amount').val();
    var date=$('#mxContent #input_date').val().replace(/-/g,'');
    var bankvoucherid=$('#mxContent #input_id').val();
    var dcFlag=$('#mxContent #input_dcFlag').val();
    var racctid=$('#mxContent #input_racctid').val();
    var postData = {
        offset :params.offset + 0,// SQL语句起始索引
        limit : params.limit,  // 每页显示数量
        agentid: agentid,
        acctid: acctid,
        txamount: amount,
        txdate: date,
        bankvoucherid: bankvoucherid,
        dcflag: dcFlag,
        recipaccno: racctid
    };
    return postData;
}

//表格按钮
function mxContentFormatter(value ,row ,index){
    return [
        '<button id=\"line_btn_xml\" class=\"btn btn-icon btn-blue btn-icon-style-1\"><i class=\"fa fa-comments-o\"></i></button>',].join('');
};
//表格按钮事件
window.mxContentEvent = {
    "click #line_btn_xml": function (e, value, row, index) {
       /* $.ajax({
            type : "POST",
            dataType : "json",
            url : "/formatCon",
            contentType: "application/json",
            data : JSON.stringify({inputTypr : '1',leftArea : row.tranxml}),
            success : function(result) {
                $('#mxContent_getModal #message_get').val(result.Msg);
            }
        });*/
        $('#mxContent_getModal #message_get').val(row.tranxml);
        $("#mxContent_getModal").modal("show");
    }
}

//构建表格
function mxContentTable(){
    $("#mxContent #mxContent_table").bootstrapTable('destroy');//先销毁
    $("#mxContent #mxContent_table").bootstrapTable({
        url: '/mxContent',
        method: 'post',
        toolbar: '#mxContent_toolbar',
        queryParams: getMxContentParams,
        queryParamsType: "limit", //参数格式,发送标准的RESTFul类型的参数请求
        sortable: true,                     //是否启用排序
        sortOrder: "asc",                   //排序方式
        clickEdit: true,
        showToggle: true,
        showRefresh: true,
        pageNumber : 1,
        pagination: true,
        sidePagination : 'server',          //服务器分页
        pageSize: 10,
        pageList: [10, 25],
        showColumns: true,
        showPaginationSwitch: false,
        /*showRefresh: true,*/
        clickToSelect: true,                //点击row选中radio或CheckBox
        checkbox:true,
        singleSelect: true,
        showToggle: true,
        columns : [ {
            checkbox : true
        },{
            field : 'bankvoucherid',
            title : '唯一标识',
            align : 'center',
            width : 100
        /*},{
            field : 'acctid',
            title : '本方账号',
            align : 'center',
            width : 150
        },{
            field : 'acctidname',
            title : '本方户名',
            align : 'center',
            width : 200*/
        },{
            field : 'txdate',
            title : '发生日期',
            align : 'center',
            width : 100
        },{
            field : 'txamount',
            title : '交易金额',
            align : 'center',
            width : 150
        },{
            field : 'dcflag',
            title : '借贷标识',
            align : 'center',
            width : 20,
            formatter :function (value ,row ,index) {
                if (value === 'D') {
                    return '<h6 style="color: #f44336">支出</h6>';
                } else{
                    return '<h6 style="color: #1f9c0e">收入</h6>';
                }
            }
        },{
            field : 'recipaccno',
            title : '对方账号',
            align : 'center',
            width : 150
        },{
            field : 'recipname',
            title : '对方户名',
            align : 'center',
            width : 200
        },{
            field : 'abstractstr',
            title : '附言',
            align : 'center',
            width : 150
        },{
            field : 'purpose',
            title : '摘要',
            align : 'center',
            width : 150
        },{
            field : 'tranxml',
            visible: false
        },{
            field : 'operation',
            title : '操作',
            align : 'center',
            width : 80,
            events:mxContentEvent,
            formatter :mxContentFormatter
        } ]
    });
}