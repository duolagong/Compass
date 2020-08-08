$(function() {
    $('#sysMessage_table').bootstrapTable({
        url: '/sysMessage',
        method: 'get',
        toolbar: '#sysMessage_toolbar',
        clickEdit: true,
        showToggle: true,
        pagination: true,
        pageSize: 10,
        pageList: [10, 20, 25],
        showColumns: true,
        showPaginationSwitch: false,
        showRefresh: true,
        clickToSelect: true,  //点击row选中radio或CheckBox
        checkbox:true,
        singleSelect: true,
        showToggle: true,
        columns : [ {
            checkbox : true
        },{
            field : 'id',
            visible: false
        },{
            field : 'type',
            title : '模板类型',
            align : 'center',
            width : 80
        },{
            field : 'typeSource',
            title : '类型来源',
            align : 'center',
            width : 80
        }, {
            field : 'typeName',
            title : '类型名称',
            align : 'center',
            width : 200
        },{
            field : 'message',
            visible: false
        },{
            field : 'messageType',
            title : '报文格式',
            align : 'center',
            width : 80
        },{
            field : 'createdTime',
            title : '创建时间',
            align : 'center',
            width : 150
        },{
            field : 'updatedTime',
            title : '更新时间',
            align : 'center',
            width : 150
        },{
            field : 'operation',
            title : '操作',
            align : 'center',
            width : 150,
            events: sysMessageEvents,
            formatter :sysMessageFormatter
        } ]
    });
});

//表格按钮
function sysMessageFormatter(value ,row ,index){
    return [
        '<button id=\"line_btn_up\" class=\"btn btn-icon btn-blue btn-icon-style-1\"><i class=\"fa fa-cog\"></i></button>',
        '   <button id=\"line_btn_del\" class=\"btn btn-icon btn-red btn-icon-style-1\"><i class=\"fa fa-close\"></i></button>',
        '   <button id=\"line_btn_get\" class=\"btn btn-icon btn-green btn-icon-style-1\"><i class=\"fa fa-exclamation-circle\"></i></button>',
    ].join('');
};

//表格按钮事件
window.sysMessageEvents = {
    'click #line_btn_up': function (e, value, row, index) {
        $("#sysMessage_updateModal #id_up").val(row.id);
        $("#sysMessage_updateModal #type_up").val(row.type);
        $("#sysMessage_updateModal #typeSource_up").val(row.typeSource);
        $("#sysMessage_updateModal #typeName_up").val(row.typeName);
        $("#sysMessage_updateModal #message_up").val(row.message);
        $("#sysMessage_updateModal #messageType_up").val(row.messageType);
        $("#sysMessage_updateModal").modal("show");
    },'click #line_btn_del': function (e, value, row, index) {
        var mymessage = confirm("确认删除"+row.type+"模板?");
        if (mymessage == true) {
            $.ajax({
                type:"delete",
                url:"/sysMessage/"+row.id,
                data:row.id,
                success:function(result){
                    myAlert(result.status,result.errMsg);
                    $("#sysMessage_table").bootstrapTable('refresh');
                    /*$('#sysMessage button[name="refresh"]').click();*/
                },error:function(xhr, ajaxOptions, errorThrown) {
                    alert(xhr.textStatus+":"+errorThrown);
                }
            });
        } else if (mymessage == false) {

        }
    },'click #line_btn_get': function (e, value, row, index) {
        $("#sysMessage_getModal #message_get").val(row.message);
        $("#sysMessage_getModal").modal("show");
    }
};

$('#sysMessage #btn_add').on('click',function(){
    $("#sysMessage_addModal").modal("show");
});

/*$('#sysMessage #btn_get').on('click',function(){
    var rows = $('#sysMessage_table').bootstrapTable('getSelections');
    if(rows.length!=1){
        alert("请选择一行数据");
    }else{
        $("#message_get").val(rows[0].message);
        $("#getModal").modal("show");
    }
});
$('#sysMessage #btn_update').on('click',function(){
    var rows = $("#sysMessage_table").bootstrapTable('getSelections');
    if(rows.length!=1){
        alert("请选择一行数据");
    }else{
        $("#id_up").val(rows[0].id);
        $("#type_up").val(rows[0].type);
        $("#typeSource_up").val(rows[0].typeSource);
        $("#typeName_up").val(rows[0].typeName);
        $("#message_up").val(rows[0].message);
        $("#messageType_up").val(rows[0].messageType);
        $("#updateModal").modal("show");
    }
});*/
$("#sysMessage  #updateModal-footer-up").on('click',function(){
    var id_up=$("#sysMessage #id_up").val();
    var type_up=$("#sysMessage #type_up").val();
    var typeSource_up=$("#sysMessage #typeSource_up").val();
    var typeName_up=$("#sysMessage #typeName_up").val();
    var message_up=$("#sysMessage #message_up").val();
    var messageType_up=$("#sysMessage #messageType_up").val();
    var postData={id:id_up,type:type_up,typeSource:typeSource_up,typeName:typeName_up,message:message_up,messageType:messageType_up};
    $.ajax({
        type:"PUT",
        dataType:"JSON",
        url:"/sysMessage",
        contentType: "application/json",
        data:JSON.stringify(postData),
        success:function(result){
            myAlert(result.status,result.errMsg);
            $("#sysMessage_updateModal").modal('hide');//模态框隐藏
            $('#sysMessage button[name="refresh"]').click();
        }
    });
});

/*$("#sysMessage  #btn_delete").on('click',function(){
    var rows = $("#table1").bootstrapTable('getSelections');
    if(rows.length!=1){
        alert("请选择一行数据");
    }else{
        $.ajax({
            type:"delete",
            url:"/sysMessage/"+rows[0].id,
            data:rows[0].id,
            success:function(result){
                console.log(result);
                var textStatus=result.textStatus;
                var errorThrown=result.errorThrown;
                alert(textStatus+":"+errorThrown);
                $("#table1").bootstrapTable('refresh');
            },error:function(xhr, ajaxOptions, errorThrown) {
                alert(xhr.textStatus+":"+errorThrown);
            }
        });
    }
});*/
$("#sysMessage  #addModal-footer-add").on('click',function() {
    debugger;
    var type_add = $("#sysMessage_addModal #type_add").val();
    if (!type_add) {
        alert("模板类型不能为空");
        return false;
    }
    var typeSource_add = $("#sysMessage_addModal #typeSource_add").val();
    if (!typeSource_add) {
        alert("类型来源不能为空");
        return false;
    }
    var typeName_add = $("#sysMessage_addModal #typeName_add").val();
    if (!typeName_add) {
        alert("类型名称不能为空");
        return false;
    }
    var message_add = $("#sysMessage_addModal #message_add").val();
    if (!message_add) {
        alert("报文模板不能为空");
        return false;
    }
    var messageType_add = $("#sysMessage_addModal #messageType_add").val();
    if (!messageType_add) {
        alert("模板格式不能为空");
        return false;
    }
    $.ajax({
        type : "POST",
        dataType : "json",
        url : "/sysMessage",
        contentType : "application/json",
        data : JSON.stringify({type : type_add,typeSource : typeSource_add,typeName : typeName_add,message : message_add,messageType : messageType_add}),
        success : function(result) {
            myAlert(result.status,result.errMsg);
            $("#sysMessage_addModal").modal('hide');
            $("#sysMessage_table").bootstrapTable('refresh');
            // $('#sysMessage button[name="refresh"]').click();
        },error:function(XMLHttpRequest, textStatus, errorThrown) {
            alert(textStatus+"："+errorThrown);
            $("#sysMessage_addModal").modal('hide');
            $("#sysMessage_table").bootstrapTable('refresh');
            // $('#sysMessage button[name="refresh"]').click();
        }
    });
});
/*
function paramsMatter(value,row,index) {
    var span=document.createElement('span');
    span.setAttribute('title',value);
    span.innerHTML = '<h3><i class="fa fa-commenting"></i></h3>';
    return span.outerHTML;
}
function formatTableUnit(value, row, index) {
    return { css: { "background-color":"#D5D5D5" } }
}*/
