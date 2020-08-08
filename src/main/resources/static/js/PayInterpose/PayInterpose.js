$(function() {
    $("#payInterpose #input_trandate").daterangepicker();
    getPayIntervene();
});

//表格按钮
function payInterposeFormatter(value ,row ,index){
    debugger;
    if (row.submitName =='System'){
        return [
            '<button id=\"line_btn_add\" class=\"btn btn-icon btn-blue btn-icon-style-1\"><i class=\"fa fa-mail-forward\"></i></button>',
        ].join('');
    }else {
        return [
            '<button id=\"line_btn_add\" class=\"btn btn-icon btn-blue btn-icon-style-1\"><i class=\"fa fa-mail-forward\"></i></button>',
            '&nbsp<button id=\"line_btn_up\" class=\"btn btn-icon btn-red btn-icon-style-1\"><i class=\"fa fa-pencil\"></i></button>',
        ].join('');
    }
};

//表格按钮事件
window.payInterposeEvents = {
    'click #line_btn_add': function (e, value, row, index) {
        $.ajax({
            type:"get",
            url:"/payInterpose/"+row.ordernum,
            data:row.ordernum,
            success:function(result){
                myAlert(result.status,result.errMsg);
                //加个刷新,由于不是自己改库，可能会慢
                $('#payInterpose button[name="refresh"]').click();
            }
        });
    }, 'click #line_btn_up': function (e, value, row, index) {
        $("#payInterpose #ordernum_up").val(row.ordernum);
        $("#payInterpose #version_up").val(row.version);
        $("#payInterpose #agentid_up").val(row.agentid);
        $("#payInterpose #subbranchid_up").val(row.subbranchid);
        $("#payInterpose #updateModal").modal("show");
    }
};

//修改交易状态
$('#payInterpose #modal_btn_up').on('click',function () {
    var ordernum=$("#payInterpose #ordernum_up").val();
    var bankCode=$("#payInterpose #bankCode_up").val();
    var bankMsg=$("#payInterpose #bankMsg_up").val();
    var postData={ordernum:ordernum,bankFinalCode:bankCode,bankFinalMsg:bankMsg};
    $.ajax({
        type:"put",
        dataType:"JSON",
        url:"/payInterpose",
        contentType: "application/json",
        data:JSON.stringify(postData),
        success:function(result){
            myAlert(result.status,result.errMsg);
            $("#updateModal").modal('hide');//模态框隐藏
            //加个刷新,由于不是自己改库，可能会慢
            $('#payInterpose button[name="refresh"]').click();
        }
    });
});

//查询按钮
$("#payInterpose_btn_get").on('click',function(){
    getPayIntervene();
    // $("#payInterpose_table").bootstrapTable('refresh');
    // $('#payInterpose button[name="refresh"]').click();
});

//构建表格查询参数
function getQueryParams(params) {
    var ordernum=$('#payInterpose #input_ordernum').val();
    var version =$('#payInterpose #input_version').val();
    var agentid =$('#payInterpose #input_agentid').val();
    var  txcode =$('#payInterpose #input_txCode').val();
    var payprocess=$('#payInterpose #input_payprocess').val();
    var txamount=$('#payInterpose#input_txamount').val();
    var personflag=$('#payInterpose #input_personflag').val();
    var date=$('#payInterpose #input_trandate').val().split(" - ");
    var temp = {
        offset :params.offset + 0,// SQL语句起始索引
        limit : params.limit,  // 每页显示数量
        ordernum:ordernum,
        dateFrom:date[0],
        dateTo:date[1],
        version:version,
        agentid:agentid
    };
    return temp;
}
//构建表格
function getPayIntervene(){
    debugger;
    $("#payInterpose_table").bootstrapTable('destroy');//先销毁
    $("#payInterpose_table").bootstrapTable({
        url: '/payInterpose/Table',
        method: 'post',
        toolbar: '#payInterpose_btn_get',
        queryParams: getQueryParams,
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
        pageList: [10, 25, 50],
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
            field : 'ordernum',
            title : '支付序号',
            align : 'center',
            width : 100
        },{
            field : 'version',
            title : '共享中心',
            align : 'center',
            width : 80
        },{
            field : 'agentid',
            title : '银行编号',
            align : 'center',
            width : 80
        },{
            field : 'subbranchid',
            title : '支付时间',
            align : 'center',
            width : 100
        },{
            field : 'txamount',
            title : '付款金额',
            align : 'center',
            width : 100
        },{
            field : 'txcode',
            title : '交易类型',
            align : 'center',
            width : 150,
            formatter :function (value ,row ,index) {
                if (row['txcode'] === '2000') {
                    return '资金监控付款';
                }else{
                    return '资金普通付款';
                }
            }
        },{
            field : 'createdTime',
            title : '接收时间',
            align : 'center',
            width : 150
        },{
            field : 'submitName',
            title : '提交人',
            align : 'center',
            width : 80,
            formatter :function (value ,row ,index) {
                if (value != "System") {
                    return '<h6 style="color: #ff0b37">'+value+'</h6>';
                }else{
                    return '<h6>'+value+'</h6>';
                }
            }
        },{
            field : 'operation',
            title : '校准',
            align : 'center',
            width : 100,
            events: payInterposeEvents,
            formatter :payInterposeFormatter
        } ]
    });
}