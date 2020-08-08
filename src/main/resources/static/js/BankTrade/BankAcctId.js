$(function() {
    bankAcctIdTable();
});

$("#bankAcctId #bankAcctId_btn_get").on('click',function(){
    /*$('#mxContent button[name="refresh"]').click();*/
    bankAcctIdTable();
});

//获取后台数据
function getBankAcctIdParams(params){
    debugger;
    var agentid=$('#bankAcctId #input_agentid').val();
    var acctid=$('#bankAcctId #input_acctid').val();
    var postData = {
        offset :params.offset + 0,// SQL语句起始索引
        limit : params.limit,  // 每页显示数量
        agentid: agentid,
        acctid: acctid,
    };
    return postData;
}

window.bankAcctIdEvents = {
    'click #line_btn_up': function (e, value, row, index) {
        $("#acctPower_updateModal #version_up").val(row.version);
        $("#acctPower_updateModal #agentid_up").val(row.agentid);
        $("#acctPower_updateModal #acctid_up").val(row.acctid);
        $("#acctPower_updateModal #querypower_up").val(row.querypower);
        $("#acctPower_updateModal #paypower_up").val(row.paypower);
        $("#acctPower_updateModal #billpower_up").val(row.billpower);
        $("#acctPower_updateModal").modal("show");
    }
};


function bankAcctIdAnzeige(value,row,index){
    if(value=='1'){
        return ['<h3><i style="color:#20b426" class=\"fa fa-check\"></i></h3>'].join();
    }else{
        /* return ['<i style="color: red" class=\"fa fa-close\"></i>'].join();*/
        return ['<h3><i style="color: red" class=\"fa fa-close\"></i></h3>'].join();
    }
}
function bankAcctIdTable(){
    $("#bankAcctId #bankAcctId_table").bootstrapTable('destroy');//先销毁
    $('#bankAcctId #bankAcctId_table').bootstrapTable({
        url: '/bankAcctId',
        method: 'post',
        toolbar: '#bankAcctId_toolbar',
        queryParams: getBankAcctIdParams,
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
        columns: [{
            checkbox: true
        }, {
            field: 'agentid',
            title: '银行编号',
            align : 'center',
            width: 50
        }, {
            field: 'acctid',
            title: '账户号',
            align : 'center',
            width: 150
        },{
            field: 'acctidname',
            title: '账户名称',
            align : 'center',
            width: 200
        },{
            field: 'branchid',
            title: '联行号',
            align : 'center',
            width: 100,
            formatter :function (value ,row ,index) {
                if (!value) {
                    return '未维护';
                } else{
                    return value;
                }
            }
        },{
            field: 'curcode',
            title: '币种',
            align : 'center',
            width: 50
        },{
            field: 'level',
            title: '账户类型',
            align : 'center',
            width: 100,
            formatter:function (value ,row ,index) {
                if (!value) {
                    return '未知';
                } else if(value=='1'){
                    return '一级户';
                } else if(value=='2'){
                    return '二级/一般户';
                } else if(value=='3'){
                    return '非归集户';
                } else if (value=='4'){
                    return '代理户';
                } else{
                    return value;
                }
            }
        },{
            field: 'detailed',
            title: '当日明细',
            align : 'center',
            width: 50,
            formatter: bankAcctIdAnzeige
        },{
            field: 'l_detailed',
            title: '历史明细',
            align : 'center',
            width: 50,
            formatter: bankAcctIdAnzeige
        }, {
            field: 'l_balance',
            title: '历史余额',
            align : 'center',
            width: 50,
            formatter: bankAcctIdAnzeige
        }, {
            field: 'statement',
            title: '对账单',
            align : 'center',
            width: 50,
            formatter: bankAcctIdAnzeige
        }]
    });
}
//修改任务提交按钮
$("#updateModal_btn_up").on('click',function() {
    debugger;
    var from=$('#acctPower_updateModal_from').serialize();
    console.log(from);
    var version_up = $("#acctPower_updateModal #version_up").val();
    var agentid_up = $("#acctPower_updateModal #agentid_up").val();
    var acctid_up = $("#acctPower_updateModal #acctid_up").val();
    var querypower_up = $("#acctPower_updateModal #querypower_up").val();
    var paypower_up = $("#acctPower_updateModal #paypower_up").val();
    var billpower_up = $("#acctPower_updateModal #billpower_up").val();
    var postData = {
        version: version_up,
        agentid: agentid_up,
        acctid: acctid_up,
        querypower: querypower_up,
        paypower: paypower_up,
        billpower: billpower_up
    };
    $.ajax({
        type: "PUT",
        dataType: "JSON",
        url: "/cctPower",
        contentType: "application/json",
        data: JSON.stringify(postData),
        success: function (result) {
            var textStatus = result.textStatus;
            var errorThrown = result.errorThrown;
            alert(textStatus + ":" + errorThrown);
            $("#acctPower_updateModal").modal('hide');//模态框隐藏
            //加个刷新
            /*$("#acctPower_table1").bootstrapTable('refresh');*/
            $('#acctPower button[name="refresh"]').click();
        }
    });
});