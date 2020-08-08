//构造表格
$(function() {
    let $table = $('#alertRule_table');
    $table.bootstrapTable({
        url: '/alertRule',
        method: 'get',
        toolbar: '#alertRule_toolbar',
        clickEdit: true,
        showToggle: true,
        pagination: true,
        pageSize: 10,
        pageList: [10, 25],
        showColumns: true,
        showPaginationSwitch: false,
        showRefresh: true,
        clickToSelect: true,  //点击row选中radio或CheckBox
        checkbox: true,
        singleSelect: true,
        showToggle: true,
        columns: [{
            checkbox: true
        },{
            field : 'id',
            visible: false
        },{
            field: 'type',
            title: '系统类型',
            align : 'center',
            width: 100
        }, {
            field: 'typeName',
            title: '类型名称',
            align : 'center',
            width: 150
        }, {
            field: 'rule',
            title: '临界点',
            align : 'center',
            width: 50
        }, {
            field: 'operate',
            title: '预警操作',
            align : 'center',
            width: 100,
            formatter :function (value ,row ,index) {
                if (value == 1) {
                    return '短信提醒';
                }else if(value == 2){
                    return '微信提醒';
                }else {
                    return '不提醒';
                }
            }
        },{
            field : 'createdTime',
            align : 'center',
            title : '创建时间',
            width : 150
        },{
            field : 'updatedTime',
            align : 'center',
            title : '更新时间',
            width : 150
        },{
            field : 'typeStart',
            title : '系统状态',
            align : 'center',
            width : 100,
            formatter :function (value ,row ,index) {
                if(value=='1'){
                    return ['<h4><i style="color:#20b426" class=\"fa fa-play\"></i></h4>'].join();
                }else{
                    return ['<h4><i style="color:gold" class=\"fa fa-pause\"></i></h4>'].join();
                }
            }
        }, {
            field : 'operation',
            title : '操作',
            align : 'center',
            width : 200,
            events: alertConfigEvents,
            formatter :alertConfig
        } ]
    });
});

function alertConfig(value ,row ,index){
    return [
        '<button id=\"updateBtn\" class=\"btn btn-icon btn-blue btn-icon-style-1\"><i class=\"fa fa-wrench\"></i></button>',
        '    <button id=\"startBtn\" class=\"btn btn-icon btn-green btn-icon-style-1\"><i class=\"fa fa-play\"></i></button>',
        '    <button id=\"stopBtn\" class=\"btn btn-icon btn-gold btn-icon-style-1\"><i class=\"fa fa-pause\"></i></button>',
        '    <button id=\"deleteBtn\" class=\"btn btn-icon btn-red btn-icon-style-1\"><i class=\"fa fa-trash\"></i></button>',
    ].join('');
};
//表格按钮事件
window.alertConfigEvents = {
    'click #updateBtn': function (e, value, row, index) {
        $("#alertRule_upModal #id_up").val(row.id);
        $("#alertRule_upModal #type_up").val(row.type);
        $("#alertRule_upModal #typeName_up").val(row.typeName);
        $("#alertRule_upModal #rule_up").val(row.rule);
        $("#alertRule_upModal #operate_up").val(row.operate);
        $("#alertRule_upModal").modal("show");
    },'click #deleteBtn': function (e, value, row, index) {
        var mymessage = confirm("确认删除 "+row.type+" 系统类型?");
        if (mymessage == true) {
            $.ajax({
                type:"delete",
                url:"/alertRule/"+row.id,
                data:row.id,
                success:function(result){
                    myAlert(result.status,result.errMsg);
                    $("#alertRule_table").bootstrapTable('refresh');
                },error:function(xhr, ajaxOptions, errorThrown) {
                    alert(xhr.status+thrownError);
                }
            });
        }
    },'click #startBtn': function (e, value, row, index) {
        var mymessage = confirm("确认启用"+row.type+"系统类型?");
        if(mymessage == true){
            $.ajax({
                type:"put",
                url:"/alertRule/state/"+row.id,
                data:row.id,
                success:function(result){
                    myAlert(result.status,result.errMsg);
                    $("#alertRule_table").bootstrapTable('refresh');
                },error:function(xhr, ajaxOptions, errorThrown) {
                    alert(xhr.status+thrownError);
                }
            });
        }
    },'click #stopBtn': function (e, value, row, index) {
        var mymessage = confirm("确认停用"+row.type+"系统类型?");
        if (mymessage == true) {
            $.ajax({
                type: "put",
                url: "/alertRule/pause/" + row.id,
                data: row.id,
                success: function (result) {
                    myAlert(result.status,result.errMsg);
                    $("#alertRule_table").bootstrapTable('refresh');
                }, error: function (xhr, ajaxOptions, errorThrown) {
                    alert(xhr.status + thrownError);
                }
            });
        }
    }
};

//修改任务提交按钮
$("#alertRule_upModal #updateModal-footer-up").on('click',function(){
    var id_up=$("#alertRule_upModal #id_up").val();
    var type_up=$("#alertRule_upModal #type_up").val();
    var typeName_up=$("#alertRule_upModal #typeName_up").val();
    var rule_up=$("#alertRule_upModal #rule_up").val();
    var operate_up=$("#alertRule_upModal #operate_up").val();
    var postData={id:id_up,type:type_up,typeName:typeName_up,rule:rule_up,operate:operate_up};
    $.ajax({
        type:"put",
        dataType:"JSON",
        url:"/alertRule",
        contentType: "application/json",
        data:JSON.stringify(postData),
        success:function(result){
            myAlert(result.status,result.errMsg);
            $("#alertRule_upModal").modal('hide');//模态框隐藏
            $('#alertRule button[name="refresh"]').click();
        }
    });
});

$('#alertRule #btn_add').on('click',function(){
    $("#alertRule_addModal").modal("show");
});

//新增任务
$("#alertRule_addModal #addModal-footer-add").on('click',function(){
    debugger;
    var type_add = $("#alertRule_addModal #type_add").val();
    if (!type_add) {
        alert("系统类型不能为空");
        return false;
    }
    var typeName_add = $("#alertRule_addModal #typeName_add").val();
    if (!typeName_add) {
        alert("类型名称为空");
        return false;
    }
    var rule_add = $("#alertRule_addModal #rule_add").val();
    if (!rule_add) {
        alert("规则不能为空");
        return false;
    }
    var operate_add = $("#alertRule_addModal #operate_add").val();
    if (!operate_add) {
        alert("预警提醒不能为空");
        return false;
    }
    $.ajax({
        type : "POST",
        dataType : "json",
        url : "/alertRule",
        contentType : "application/json",
        data : JSON.stringify({
            type : type_add,
            typeName : typeName_add,
            rule : rule_add,
            operate : operate_add
        }),
        success : function(result) {
            myAlert(result.status,result.errMsg);
            $("#alertRule_addModal").modal('hide');
            $('#alertRule button[name="refresh"]').click();
        }
    });
});
