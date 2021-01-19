//初始加载
$(function() {
    $("#bankPayment #inputTrandate").daterangepicker();
    getbankPayment();
});
//查询按钮
$("#bankPayment #bankPayment_btn_get").on('click',function(){
    getbankPayment();
    // $('#bankPayment button[name="refresh"]').click();
});
//表格按钮
function bankPaymentFormatter(value ,row ,index){
    return [
        '<button id=\"line_btn_xml\" class=\"btn btn-icon btn-blue btn-icon-style-1\"><i class=\"fa fa-comments-o\"></i></button>',
        '   <button id=\"line_btn_bankxml\" class=\"btn btn-icon btn-blue btn-icon-style-1\"><i class=\"fa fa-university\"></i></button>',
        '   <button id=\"line_btn_infor\" class=\"btn btn-icon btn-blue btn-icon-style-1\"><i class=\"fa fa-search\"></i></button>',].join('');
};
//表格按钮事件
window.bankPaymentEvent = {
    "click #line_btn_infor": function (e, value, row, index) {
        var url = "/bankPayment/" + row.ordernum +"/" +row.payresultiswait;
        $('#bankPayment_inforModal #bankPaymentDetails').load(url);
        $("#bankPayment_inforModal").modal("show");
    },"click #line_btn_bankxml": function (e, value, row, index) {
        $("#bankPayment_QueryModal #queryOrdernum").val(row.ordernum);
        $("#bankPayment_QueryModal #queryTransnoId").val(row.transnoid);
        $("#bankPayment_QueryModal #queryAgentId").val(row.agentid);
        $("#bankPayment_QueryModal #querySubBranchId").val(row.subbranchid);
        $("#bankPayment_QueryModal #resultXml").val("本功能仅作为查询付款结果使用，不会修改付款结果!");
        $("#bankPayment_QueryModal").modal("show");
    },"click #line_btn_xml": function (e, value, row, index) {
        var url = "/bankPayment/TranXml/" + row.ordernum +"/" +row.payresultiswait;
        $('#bankPayment_xmlModal #bankPaymentTranXml').load(url);
        $("#bankPayment_xmlModal").modal("show");
    }
}
//构建表格查询参数
function getBankPaymentParams(params) {
    var ordernum=$('#bankPayment #inputOrdernum').val();
    var payresultiswait =$('#bankPayment #inputResult').val();
    var agentid =$('#bankPayment #inputAgentid').val();
    var  bankaccounttype =$('#bankPayment #inputType').val();
    var txamount=$('#bankPayment #inputTxamount').val();
    var date=$('#bankPayment #inputTrandate').val().split(" - ");
    var temp = {
        offset :params.offset + 0,// SQL语句起始索引
        limit : params.limit,  // 每页显示数量
        ordernum:ordernum,
        dateFrom:date[0],
        dateTo:date[1],
        bankaccounttype:bankaccounttype,
        agentid:agentid,
        payresultiswait:payresultiswait,
        txamount:txamount
    };
    return temp;
}
//查询1030
$('#bankPayment #bankPayment_btn_1030').on('click',function () {
    getBankPaymentResult("1030");
});

//查询1031
$('#bankPayment #bankPayment_btn_1031').on('click',function () {
    getBankPaymentResult("1031_000");
});

function getBankPaymentResult(txcode) {
    $.ajax({
        type : "POST",
        dataType : "JSON",
        url : "/bankPayment/Result",
        contentType : "application/json",
        data : JSON.stringify({
            ordernum : $('#bankPayment_QueryModal #queryOrdernum').val(),
            agentid : $('#bankPayment_QueryModal #queryAgentId').val(),
            txcode : txcode,
            subbranchid : $('#bankPayment_QueryModal #querySubBranchId').val(),
        }),
        success : function(result) {
            $("#bankPayment_QueryModal #resultXml").val(result.resultXml);
        }
    });
}

//构建表格
function getbankPayment(){
    $("#bankPayment_table").bootstrapTable('destroy');//先销毁
    $("#bankPayment_table").bootstrapTable({
        url: '/bankPayment',
        method: 'post',
        toolbar: '#bankPayment_toolbar',
        queryParams: getBankPaymentParams,
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
            field : 'transnoid',
            title : '银行交易号',
            align : 'center',
            width : 100
        },{
            field : 'agentid',
            title : '银行编号',
            align : 'center',
            width : 150
        },{
            field : 'subbranchid',
            title : '企业编号',
            align : 'center',
            width : 100
        },{
            field : 'bankaccounttype',
            title : '付款类型',
            align : 'center',
            width : 100,
            formatter :function (value ,row ,index) {
                if (value === 'AA') {
                    return '代理支付';
                } else if(value === 'SA'){
                    return '对公支付';
                } else{
                    return '对私支付';
                }
            }
        },{
            field : 'totalamount',
            title : '付款金额',
            align : 'center',
            width : 100
        },{
            field : 'inputdate',
            title : '付款接收时间',
            align : 'center',
            width : 150
        },{
            field : 'payresultiswait',
            title : '在途状态',
            align : 'center',
            width : 100,
            formatter :function (value ,row ,index) {
                if(value=="0"){
                    return '<h6 style="color: #aed67e">交易已完成</h6>';
                }else if(value=="1"){
                    return '<h6 style="color: #ffb809">交易待查询</h6>';
                }else if(value=="2"){
                    return '<h6 style="color: #35a2ff">交易同步中</h6>';
                }else {
                    return '<h6 style="color: #f44336">交易异常</h6>';
                }
            }
        },{
            field : 'operation',
            title : '交易过程明细',
            align : 'center',
            width : 150,
            events:bankPaymentEvent,
            formatter :bankPaymentFormatter
        } ]
    });
}