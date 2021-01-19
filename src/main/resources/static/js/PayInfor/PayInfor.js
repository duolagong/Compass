//初始加载
$(function () {
    $("#payInfor #inputTrandate").daterangepicker({
        format: 'yyyy-MM-dd - yyyy-MM-dd'
    });
    $('.selectpicker').selectpicker({
        noneSelectedText: '未选择'
    });
    getPayInfor();
});
//查询按钮
$("#payInfor #payInfor_btn_get").on('click', function () {
    getPayInfor();
    // $('#payInfor button[name="refresh"]').click();
});

$("#payInfor #btn-versionChart").on('click', function () {
    ChartShow('versionChart');
});
$("#payInfor #btn-versionNumChart").on('click', function () {
    ChartShow('versionNumChart');
});
$("#payInfor #btn-personflagChart").on('click', function () {
    ChartShow('personflagChart');
});
$("#payInfor #btn-payDateTrendChart").on('click', function () {
    ChartShow('payDateTrendChart');
});
$("#payInfor #btn-numTrendChart").on('click', function () {
    ChartShow('numTrendChart');
});
$("#payInfor #btn-proportionChart").on('click', function () {
    myAlert(false, "敬请期待下次更新！")
});

function ChartShow(typeChart) {
    var date = $('#payInfor #inputTrandate').val().split(" - ");
    var amountMin = $('#payInfor #inputAmountMin').val();
    var amountMax = $('#payInfor #inputAmountMax').val();
    if (!!amountMax) {
        if (!isNumber(amountMin)) {
            myAlert(false, '请输入正确的最小金额！');
            return;
        } else if (!isNumber(amountMax)) {
            myAlert(false, '请输入正确的最大金额！');
            return;
        } else if (amountMin > amountMax) {
            myAlert(false, '最小金额不能大于最大金额');
            return;
        }
    }
    $.ajax({
        type: "POST",
        dataType: "JSON",
        url: "/payInfor/" + typeChart,
        contentType: "application/json",
        data: JSON.stringify({
            versions: $('#payInfor #inputVersion').val(),
            payprocess: $('#payInfor #inputPayprocess').val(),
            agentids: $('#payInfor #inputAgentid').val(),
            personflag: $('#payInfor #inputPersonflag').val(),
            amountMin: amountMin,
            amountMax: amountMax,
            dateFrom: date[0],
            dateTo: date[1],
        }),
        success: function (result) {
            if (result.status) {
                if (typeChart == 'versionChart') {
                    versionCharShow(result.data);
                } else if (typeChart == 'versionNumChart') {
                    versionNumCharShow(result.data);
                } else if (typeChart == 'personflagChart') {
                    personFlagCharShow(result.data);
                } else if (typeChart == 'payDateTrendChart') {
                    payDateTrendChartShow(result.data);
                } else if (typeChart == 'numTrendChart') {
                    numlTrendChartShow(result.data);
                } else {
                    myAlert(false, "没有这种类型");
                }
                $("#payInfor_ChartModal").modal("show");
            }
            myAlert(result.status, result.errMsg);
        }
    });
}

//表格按钮
function payInforFormatter(value, row, index) {
    return [
        '<button id=\"line_btn_xml\" class=\"btn btn-icon btn-blue btn-icon-style-1\"><i class=\"fa fa-commenting-o\"></i></button>',
        '   <button id=\"line_btn_infor\" class=\"btn btn-icon btn-blue btn-icon-style-1\"><i class=\"fa fa-search\"></i></button>',].join('');
};
//表格按钮事件
window.payInforEvent = {
    "click #line_btn_infor": function (e, value, row, index) {
        var url = "/payInfor/" + row.ordernum; //获取url的值
        $('#payInfor_inforModal #payInforDetails').load(url);//从url加载数据到模态框//OrderNumInfor
        $("#payInfor_inforModal").modal("show");
    }, "click #line_btn_xml": function (e, value, row, index) {
        var url = "/payInfor/TranXml/" + row.ordernum; //获取url的值
        $('#payInfor_xmlModal #payInforTranXml').load(url);//从url加载数据到模态框//OrderNumInfor
        $("#payInfor_xmlModal").modal("show");
    }
}

//构建表格查询参数
function getPayInforParams(params) {
    var ordernum = $('#payInfor #inputOrdernum').val();
    var version = $('#payInfor #inputVersion').val();
    var agentid = $('#payInfor #inputAgentid').val();
    var txcode = $('#payInfor #inputTxCode').val();
    var payprocess = $('#payInfor #inputPayprocess').val();
    var personflag = $('#payInfor #inputPersonflag').val();
    var date = $('#payInfor #inputTrandate').val().split(" - ");
    var subbranchid = $('#payInfor #inputSubbranchId').val();
    var amountMin = $('#payInfor #inputAmountMin').val();
    var amountMax = $('#payInfor #inputAmountMax').val();
    if (!!amountMax) {
        if (!isNumber(amountMin)) {
            myAlert(false, '请输入正确的最小金额！');
            return;
        } else if (!isNumber(amountMax)) {
            myAlert(false, '请输入正确的最大金额！');
            return;
        } else if (amountMin > amountMax) {
            myAlert(false, '最小金额不能大于最大金额');
            return;
        }
    }
    var temp = {
        offset: params.offset + 0,// SQL语句起始索引
        limit: params.limit,  // 每页显示数量
        ordernum: ordernum,
        dateFrom: date[0],
        dateTo: date[1],
        personflag: personflag,
        versions: version,
        agentids: agentid,
        txcode: txcode,
        payprocess: payprocess,
        subbranchids: subbranchid,
        amountMin: amountMin,
        amountMax: amountMax
    };
    return temp;
}

//在途校准
$('#payInfor #payInfor_btn_interpose').on('click', function () {
    /*var rows = $("#payInfor_table").bootstrapTable('getSelections');*/
    var payprocess = $('#payInfor_inforModal #payprocess').attr("value");
    if (payprocess == 0) {
        alert("交易已结束,无需处理!");
        return;
    }
    $.ajax({
        type: "POST",
        dataType: "JSON",
        url: "/payInterpose",
        contentType: "application/json",
        data: JSON.stringify({
            ordernum: $('#payInfor_inforModal #ordernum').text(),
            version: $('#payInfor_inforModal #version').text(),
            agentid: $('#payInfor_inforModal #agentid').text(),
            txcode: $('#payInfor_inforModal #txcode').text(),
            subbranchid: $('#payInfor_inforModal #subbranchid').text(),
            txamount: $('#payInfor_inforModal #txamount').text()
        }),
        success: function (result) {
            myAlert(result.status, result.errMsg);
        }
    });
})

//构建表格
function getPayInfor() {
    $("#payInfor_table").bootstrapTable('destroy');//先销毁
    $("#payInfor_table").bootstrapTable({
        url: '/payInfor',
        method: 'post',
        toolbar: '#payInfor_toolbar',
        queryParams: getPayInforParams,
        queryParamsType: "limit", //参数格式,发送标准的RESTFul类型的参数请求
        sortable: true,                     //是否启用排序
        sortOrder: "asc",                   //排序方式
        clickEdit: true,
        showToggle: true,
        showRefresh: true,
        pageNumber: 1,
        pagination: true,
        sidePagination: 'server',          //服务器分页
        pageSize: 10,
        pageList: [25, 50, 100],
        showColumns: true,
        showPaginationSwitch: false,
        /*showRefresh: true,*/
        clickToSelect: true,                //点击row选中radio或CheckBox
        checkbox: true,
        singleSelect: true,
        showToggle: true,
        columns: [{
            checkbox: true
        }, {
            field: 'ordernum',
            title: '支付序号',
            align: 'center',
            width: 100
        }, {
            field: 'version',
            title: '共享中心',
            align: 'center',
            width: 80
        }, {
            field: 'agentid',
            title: '银行编号',
            align: 'center',
            width: 80
        }, {
            field: 'subbranchid',
            title: '企业编号',
            align: 'center',
            width: 80
        }, {
            field: 'txamount',
            title: '付款金额',
            align: 'center',
            width: 100
        }, {
            field: 'txcode',
            title: '交易类型',
            align: 'center',
            width: 150,
            formatter: function (value, row, index) {
                if (row['txcode'] === '2000') {
                    return '资金监控付款';
                } else {
                    return '资金普通付款';
                }
            }
        }, {
            field: 'personflag',
            title: '公私标识',
            align: 'center',
            width: 80,
            formatter: function (value, row, index) {
                if (row['personflag'] === '1') {
                    return '公';
                } else {
                    return '私';
                }
            }
        }, {
            field: 'trandate',
            title: '报文接收时间',
            align: 'center',
            width: 200
        }, {
            field: 'payprocess',
            title: '在途状态',
            align: 'center',
            width: 100,
            formatter: function (value, row, index) {
                if (row['payprocess'] === '1') {
                    return '<h6 style="color: #ffc107">在途</h6>';
                } else {
                    var finalcode = row['bankfinalcode'];
                    if (finalcode == null || finalcode == "") {
                        return '<h6 style="color: #f44336">交易失败</h6>';
                    } else if (finalcode.startsWith("M")) {
                        return '<h6 style="color: #aed67e">交易成功</h6>';
                    } else {
                        return '<h6 style="color: #f44336">交易失败</h6>';
                    }
                }
            }
        }, {
            field: 'txmoment',
            title: '交易过程明细',
            align: 'center',
            width: 100,
            events: payInforEvent,
            formatter: payInforFormatter
        }]
    });
}