$(function() {
    $('#planPule_table1').bootstrapTable({
        url: '/planRule',
        method: 'get',
        toolbar: '#planPule_toolbar',
        clickEdit: true,
        showToggle: true,
        pagination: true,
        pageSize: 10,
        pageList: [10, 25],
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
            field : 'jobName',
            title : '任务名称',
            align : 'center',
            width : 100
        }, {
            field : 'cronExpression',
            title : '规则',
            align : 'center',
            width : 100
        }, {
            field : 'beanName',
            title : '执行类',
            align : 'center',
            width : 100
        }, {
            field : 'methodName',
            title : '执行方法',
            align : 'center',
            width : 100
        },{
            field : 'status',
            title : '状态',
            align : 'center',
            formatter :function (value, row, index) {
                if (row['status'] === 1) {
                    return ['<i style="color:#20b426" class=\"fa fa-hourglass-end fa-spin fa-2x fa-fw\"></i>'].join();
                }
                if (row['status'] === 0) {
                    return ['<i style="color:#ff7700" class=\"fa fa-pause fa-2x\"></i>'].join();
                }
                return value;
            },
            width : 100
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
        }, {
            field : 'operation',
            title : '操作',
            align : 'center',
            width : 150,
            events: planRuleEvents,
            formatter :planRuleConfig
        }]
    });
});

function planRuleConfig(value ,row ,index){
    return [
        '<button id=\"line_btn_up\" class=\"btn btn-icon btn-blue btn-icon-style-1\"><i class=\"fa fa-cog\"></i></button>',
        '    <button id=\"line_btn_start\" class=\"btn btn-icon btn-green btn-icon-style-1\"><i class=\"fa fa-play\"></i></button>',
        '    <button id=\"line_btn_stop\" class=\"btn btn-icon btn-gold btn-icon-style-1\"><i class=\"fa fa-pause\"></i></button>',
        '    <button id=\"line_btn_del\" class=\"btn btn-icon btn-red btn-icon-style-1\"><i class=\"fa fa-trash\"></i></button>',
    ].join('');
};
//表格按钮事件
window.planRuleEvents = {
    'click #line_btn_up': function (e, value, row, index) {
        $("#planPule_upModal #id_up").val(row.id);
        $("#planPule_upModal #jobName_up").val(row.jobName);
        $("#planPule_upModal #cronExpression_up").val(row.cronExpression);
        $("#planPule_upModal  #beanName_up").val(row.beanName);
        $("#planPule_upModal  #methodName_up").val(row.methodName);
        $("#planPule_upModal").modal("show");
    },'click #line_btn_del': function (e, value, row, index) {
        var mymessage = confirm("确认删除"+row.jobName+"任务?");
        if (mymessage == true) {
            $.ajax({
                type:"delete",
                url:"/planRule/"+row.id,
                data:row.id,
                success:function(result){
                    myAlert(result.status,result.errMsg);
                    $("#planPule_table1").bootstrapTable('refresh');
                },error:function(xhr, ajaxOptions, errorThrown) {
                    alert(xhr.status+thrownError);
                }
            });
        }
    },'click #line_btn_stop': function (e, value, row, index) {
        var mymessage = confirm("确认暂停"+row.jobName+"任务?");
        if (mymessage == true) {
            $.ajax({
                type: "put",
                url: "/planRule/pause/" + row.id,
                data: row.id,
                success: function (result) {
                    myAlert(result.status,result.errMsg);
                    $("#planPule_table1").bootstrapTable('refresh');
                }, error: function (xhr, ajaxOptions, errorThrown) {
                    alert(xhr.status + thrownError);
                }
            });
        }
    },'click #line_btn_start': function (e, value, row, index) {
        var mymessage = confirm("确认开启"+row.jobName+"任务?");
        if(mymessage == true){
            $.ajax({
                type:"put",
                url:"/planRule/state/"+row.id,
                data:row.id,
                success:function(result){
                    myAlert(result.status,result.errMsg);
                    $("#planPule_table1").bootstrapTable('refresh');
                },error:function(xhr, ajaxOptions, errorThrown) {
                    alert(xhr.status+thrownError);
                }
            });
        }
    }
};

$('#planPule #btn_add').on('click',function () {
    $("#planPule_addModal").modal("show");
});
//转换时间，有问题需要修改
function changeDateFormat(cellval) {
    if (cellval != null) {
        var date = new Date(parseInt(cellval.replace("/Date(", "").replace(")/", ""), 10));
        var month = date.getMonth() + 1 < 10 ? "0" + (date.getMonth() + 1) : date.getMonth() + 1;
        var currentDate = date.getDate() < 10 ? "0" + date.getDate() : date.getDate();
        return date.getFullYear() + "-" + month + "-" + currentDate;
    }
}
//修改任务提交按钮
$("#planPule_upModal #updateModal-footer-up").on('click',function(){
    var id_up=$("#planPule_upModal #id_up").val();
    var jobName_up=$("#planPule_upModal #jobName_up").val();
    var cronExpression_up=$("#planPule_upModal #cronExpression_up").val();
    var beanName_up=$("#planPule_upModal #beanName_up").val();
    var methodName_up=$("#planPule_upModal #methodName_up").val();
    var postData={id:id_up,jobName:jobName_up,cronExpression:cronExpression_up,beanName:beanName_up,methodName:methodName_up};
    $.ajax({
        type:"put",
        dataType:"JSON",
        url:"/planRule",
        contentType: "application/json",
        data:JSON.stringify(postData),
        success:function(result){
            myAlert(result.status,result.errMsg);
            $("#planPule_upModal").modal('hide');//模态框隐藏
            $('#planPule button[name="refresh"]').click();
        }
    });
});
//新增任务
$("#planPule_addModal #addModal-footer-add").on('click',function(){
    var jobName_add = $("#jobName_add").val();
    if (jobName_add == null || jobName_add == "") {
        alert("任务名不能为空");
        return false;
    }
    var cronExpression_add = $("#cronExpression_add").val();
    if (cronExpression_add == null || cronExpression_add == "") {
        alert("规则不能为空");
        return false;
    }
    var beanName_add = $("#beanName_add").val();
    if (beanName_add == null || beanName_add == "") {
        alert("执行类不能为空");
        return false;
    }
    var methodName_add = $("#methodName_add").val();
    if (methodName_add == null || methodName_add == "") {
        alert("执行方法不能为空");
        return false;
    }
    $.ajax({
        type : "POST",
        dataType : "json",
        url : "/planRule",
        contentType : "application/json",
        data : JSON.stringify({
            jobName : jobName_add,
            cronExpression : cronExpression_add,
            beanName : beanName_add,
            methodName : methodName_add
        }),
        success : function(result) {
            myAlert(result.status,result.errMsg);
            $("#planPule_addModal").modal('hide');
            $('#planPule button[name="refresh"]').click();
        },error:function(XMLHttpRequest, textStatus, errorThrown) {
            /* alert("错误信息：" + XMLHttpRequest.responseText); */
            alert(textStatus+"："+errorThrown);
            $("#planPule_addModal").modal('hide');
            $('#planPule button[name="refresh"]').click();
        }
    });
});
//暂停所有任务
$("#planPule #btn_pauseAll").on('click',function () {
    var msg = "确定暂停所有自动任务？";
    if (confirm(msg)==true){
        $.ajax({
            type:"get",
            url:"/planRule/pause/",
            success:function(result){
                myAlert(result.status,result.errMsg);
                $('#planPule button[name="refresh"]').click();
            },error:function(xhr, ajaxOptions, errorThrown) {
                alert(xhr.status+thrownError);
            }
        });
    }
})
//启动所有任务
$("#planPule #btn_startAll").on('click',function () {
    var msg = "确定启动所有自动任务？";
    if (confirm(msg)==true){
        $.ajax({
            type:"get",
            url:"/planRule/start/",
            success:function(result){
                myAlert(result.status,result.errMsg);
                $('#planPule button[name="refresh"]').click();
            },error:function(xhr, ajaxOptions, errorThrown) {
                alert(xhr.status+thrownError);
            }
        });
    }
})


//修改任务
// $("#btn_update").on('click',function(){
//     var rows = $("#table1").bootstrapTable('getSelections');
//     if(rows.length!=1){
//         alert("请选择一行数据");
//     }else{
//         $("#id_up").val(rows[0].id);
//         $("#jobName_up").val(rows[0].jobName);
//         $("#cronExpression_up").val(rows[0].cronExpression);
//         $("#beanName_up").val(rows[0].beanName);
//         $("#methodName_up").val(rows[0].methodName);
//         $("#updateModal").modal("show");
//     }
// });
//删除任务
// $("#btn_delete").on('click',function(){
//     var rows = $("#table1").bootstrapTable('getSelections');
//     if(rows.length!=1){
//         alert("请选择一行数据");
//     }else{
//         var id=rows[0].id;
//         $.ajax({
//             type:"delete",
//             url:"/planRule/"+id,
//             data:id,
//             success:function(result){
//                 var textStatus = result.textStatus;
//                 var errorThrown = result.errorThrown;
//                 alert(textStatus + ":" + errorThrown);
//                 $("#table1").bootstrapTable('refresh');
//             },error:function(xhr, ajaxOptions, errorThrown) {
//                 alert(xhr.status+thrownError);
//             }
//         });
//     }
// });
//启动任务
// $("#btn_start").on('click',function(){
//     debugger;
//     var rows = $("#table1").bootstrapTable('getSelections');
//     if(rows.length!=1){
//         alert("请选择一行数据");
//     }else{
//         var id=rows[0].id;
//         $.ajax({
//             type:"put",
//             url:"/planRule/state/"+id,
//             data:id,
//             success:function(result){
//                 console.log(result);
//                 var textStatus = result.textStatus;
//                 console.log(textStatus);
//                 var errorThrown = result.errorThrown;
//                 alert(textStatus + ":" + errorThrown);
//                 $("#table1").bootstrapTable('refresh');
//             },error:function(xhr, ajaxOptions, errorThrown) {
//                 alert(xhr.status+thrownError);
//             }
//         });
//     }
// });
//暂停任务
// $("#btn_pause").on('click',function(){
//     debugger;
//     var rows = $("#table1").bootstrapTable('getSelections');
//     if(rows.length!=1){
//         alert("请选择一行数据");
//     }else{
//         var id=rows[0].id;
//         $.ajax({
//             type:"put",
//             url:"/planRule/pause/"+id,
//             data:id,
//             success:function(result){
//                 var textStatus = result.textStatus;
//                 var errorThrown = result.errorThrown;
//                 alert(textStatus + ":" + errorThrown);
//                 $("#table1").bootstrapTable('refresh');
//             },error:function(xhr, ajaxOptions, errorThrown) {
//                 alert(xhr.status+thrownError);
//             }
//         });
//     }
// });