$(function() {
    $("#noticeSend #inputSingleDate").daterangepicker({
        singleDatePicker:true,
        showDropdowns:true,
        minYear:2017,
        maxYear:parseInt(moment().format('YYYY'),10)
    });
    getNoticeSend();
});

//查询按钮
$("#noticeSend #noticeSend_btn_get").on('click',function(){
    getNoticeSend();
    // $('#noticeSend button[name="refresh"]').click();
});

//表格按钮
function noticeSend(value ,row ,index){
    return [
        '<button id=\"sendBtn\" class=\"btn btn-icon btn-blue btn-icon-style-1\"><i class=\"fa fa-rocket\"></i></button>',
    ].join('');
};
//表格按钮事件
window.noticeSendEvents = {
    'click #sendBtn': function (e, value, row, index) {
        if(row.payprocess==1){
            myAlert(false,"这笔付款还在处理中，无法重推汇款结果!");
            return false;
        }
        var mymessage = confirm("确认重推交易 "+row.ordernum+" 的汇款结果?");
        if (mymessage == true) {
            $.ajax({
                type:"put",
                url:"/noticeSend/"+row.ordernum,
                data:row.ordernum,
                success:function(result){
                    myAlert(result.status,result.errMsg);
                    $("#noticeSend_table").bootstrapTable('refresh');
                },error:function(xhr, ajaxOptions, errorThrown) {
                    alert(xhr.status+thrownError);
                }
            });
        }
    }
};

//构建表格查询参数
function getNoticeSendParams(params) {
    var ordernum=$('#noticeSend #inputOrdernum').val();
    var date=$('#noticeSend #inputSingleDate').val();
    var rule=$('#noticeSend #inputRule').val();
    if(rule=='0'){
        return { offset :params.offset + 0,limit : params.limit, ordernum:"", queryDate:date,};
    }else if(rule=='1'){
        return { offset :params.offset + 0,limit : params.limit, ordernum:ordernum, queryDate:"",};
    }else {
        return { offset :params.offset + 0,limit : params.limit, ordernum:ordernum, queryDate:date,};
    }
}

//构造表格
function getNoticeSend() {
    $("#noticeSend #noticeSend_table").bootstrapTable('destroy');//先销毁
    $("#noticeSend #noticeSend_table").bootstrapTable({
        url: '/noticeSend',
        method: 'post',
        /*toolbar: '#payInfor_toolbar',*/
        queryParams: getNoticeSendParams,
        queryParamsType: "limit", //参数格式,发送标准的RESTFul类型的参数请求
        sortable: true,                     //是否启用排序
        sortOrder: "asc",                   //排序方式
        clickEdit: true,
        showToggle: true,
        showRefresh: true,
        pageNumber : 1,
        pagination: true,
        sidePagination : 'server',          //服务器分页
        pageSize: 25,
        pageList: [25, 50, 100],
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
            title : '企业编号',
            align : 'center',
            width : 80
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
            field : 'trandate',
            title : '报文接收时间',
            align : 'center',
            width : 200
        },{
            field : 'payprocess',
            title : '在途状态',
            align : 'center',
            width : 100,
            formatter :function (value ,row ,index) {
                if (row['payprocess'] === '1') {
                    return '<h6 style="color: #ffc107">在途</h6>';
                }else{
                    var finalcode=row['bankfinalcode'];
                    if(!finalcode){
                        return '<h6 style="color: #f44336">付款失败</h6>';
                    }else if(finalcode.startsWith("M")){
                        return '<h6 style="color: #aed67e">付款成功</h6>';
                    }else{
                        return '<h6 style="color: #f44336">付款失败</h6>';
                    }
                }
            }
        },{
            field : 'txmoment',
            title : '操作',
            align : 'center',
            width : 100,
            events:noticeSendEvents,
            formatter :noticeSend
        } ]
    });
}