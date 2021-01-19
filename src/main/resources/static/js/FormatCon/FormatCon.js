$(function () {
    $('#formatCon_assembly').hide();
    $('#formatCon_requiredEntry').hide();
    $("#formatCon #inputSingleDate").daterangepicker({
        singleDatePicker: true,
        showDropdowns: true,
        minYear: 2017,
        maxYear: parseInt(moment().format('YYYY'), 10)
    });
})
//关于类型切换的事件监听
$('#formatCon_inputType').on('change', function () {
    var inputTypr = $("#formatCon_inputType").val();
    if (inputTypr == 3) {
        $('#formatCon_assembly').show();
    } else {
        $('#formatCon_assembly').hide();
        $('#formatCon_requiredEntry').hide();
        $('#formatCon #leftArea').val("");
    }
})
//关于TxCode切换的事件监听
$('#formatCon_inputTxCode').on('change', function () {
    var inputTxCode = $("#formatCon_inputTxCode").val();
    if (!inputTxCode) {
        $('#formatCon_requiredEntry').hide();
    } else {
        $('#formatCon_requiredEntry').show();
    }
})
//关于agentid切换的事件监听
$('#formatCon #input_agentid').change(function () {
    $('#formatCon #input_select_acctid').selectpicker('destroy');
    var agentid = $('#formatCon #input_agentid').val();
    if (!agentid) return;
    $.ajax({
        type: "get",
        url: "/bankAcctid/" + agentid,
        async: true,
        success: function (data) {
            var str = "";
            for (var i = 0; i < data.length; i++) {
                str += '<option>' + data[i].acctid + '</option>'
            }
            $("#formatCon #input_select_acctid").html(str);

            $("#formatCon #input_select_acctid").selectpicker('refresh');
        }
    });
})

$('#formatCon #button_change').on('click', function () {
    var inputTypr = $("#formatCon_inputType").val();
    if (inputTypr == 3) {
        var txCode = $('#formatCon #formatCon_inputTxCode').val();
        var agentid = $('#formatCon #input_agentid').val();
        var acctId = $('#formatCon #input_select_acctid').val();
        var curcode = $('#formatCon #input_curcode').val();
        var txdate = $('#formatCon #inputSingleDate').val();
        $.ajax({
            type:"POST",
            dataType: "json",
            url:"/assemblyMessage",
            contentType: "application/json",
            data: JSON.stringify({txCode: txCode,agentId: agentid,acctId: acctId,curCode: curcode,txDate: txdate.replaceAll('-','')}),
            success : function(result) {
                myAlert(result.status,result.errMsg);
                $('#rightArea').val(result.data);
            }
        });
    } else {
        var leftArea = $("#leftArea").val();
        if (leftArea == null || leftArea == "") {
            alert("请填写转换内容！")
            return false;
        }
        var inputTypr = $("#formatCon_inputType").val();
        $.ajax({
            type: "POST",
            dataType: "json",
            url: "/formatCon",
            contentType: "application/json",
            data: JSON.stringify({inputTypr: inputTypr, leftArea: leftArea}),
            success: function (result) {
                $('#rightArea').val(result.Msg);
            }
        });
    }
})