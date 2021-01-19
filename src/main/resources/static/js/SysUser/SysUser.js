//表格按钮
function sysUser(value ,row ,index){
    return [
        '<button id=\"updateBtn\" class=\"btn btn-icon btn-blue btn-icon-style-1\"><i class=\"fa fa-wrench\"></i></button>',
        '    <button id=\"deleteBtn\" class=\"btn btn-icon btn-red btn-icon-style-1\"><i class=\"fa fa-trash\"></i></button>',
    ].join('');
};
//表格按钮事件
window.sysUserEvents = {
    'click #updateBtn': function (e, value, row, index) {
        $("#sysUser_upModal #id_up").val(row.id);
        $("#sysUser_upModal #userId_up").val(row.userId);
        $("#sysUser_upModal #userName_up").val(row.userName);
        $("#sysUser_upModal #phone_up").val(row.phone);
        $("#sysUser_upModal  #wechat_up").val(row.wechat);
        $("#sysUser_upModal  #userType_up").val(row.userType);
        $("#sysUser_upModal").modal("show");
    },'click #deleteBtn': function (e, value, row, index) {
        if(row.userType == '1'){
            alert("不允许删除管理员用户");
            return false;
        }
        var mymessage = confirm("确认删除 "+row.userName+" 用户?");
        if (mymessage == true) {
            $.ajax({
                type:"delete",
                url:"/sysUser/"+row.id,
                data:row.id,
                success:function(result){
                    myAlert(result.status,result.errMsg);
                    $("#sysUser_table").bootstrapTable('refresh');
                },error:function(xhr, ajaxOptions, errorThrown) {
                    alert(xhr.status+thrownError);
                }
            });
        }
    }
};

$('#sysUser #btn_add').on('click',function () {
    $("#sysUser_addModal").modal("show");
});

//修改用户提交按钮
$("#sysUser_upModal #updateModal-footer-up").on('click',function(){
    var id_up=$("#sysUser_upModal #id_up").val();
    var userId_up=$("#sysUser_upModal #userId_up").val();
    var userName_up=$("#sysUser_upModal #userName_up").val();
    var phone_up=$("#sysUser_upModal #phone_up").val();
    var wechat_up=$("#sysUser_upModal #wechat_up").val();
    var userType_up=$("#sysUser_upModal #userType_up").val();
    if (!userType_up) {
        userType_up='3';
    }
    var password_up=$("#sysUser_upModal #password_up").val();
    var postData={id:id_up,userId:userId_up,userName:userName_up,phone:phone_up,wechat:wechat_up,userType:userType_up,password:password_up};
    $.ajax({
        type:"put",
        dataType:"JSON",
        url:"/sysUser",
        contentType: "application/json",
        data:JSON.stringify(postData),
        success:function(result){
            myAlert(result.status,result.errMsg);
            $("#sysUser_upModal").modal('hide');//模态框隐藏
            $('#sysUser button[name="refresh"]').click();
        }
    });
});
//新增用户
$("#sysUser_addModal #addModal-footer-add").on('click',function(){
    var userId_add = $("#sysUser_addModal #userId_add").val();
    if (!userId_add) {
        alert("用户ID不能为空");
        return false;
    }
    var userName_add = $("#sysUser_addModal #userName_add").val();
    if (!userName_add) {
        alert("用户名称为空");
        return false;
    }
    var phone_add = $("#sysUser_addModal #phone_add").val();
    if (!phone_add) {
        alert("手机号不能为空");
        return false;
    }
    var wechat_add = $("#sysUser_addModal #wechat_add").val();
    var userType_add = $("#sysUser_addModal #userType_add").val();
    if (!userType_add) {
        userType_add='3';
    }
    var password_add = $("#sysUser_addModal #password_add").val();
    if (!password_add) {
        alert("用户密码不能为空");
        return false;
    }
    $.ajax({
        type : "POST",
        dataType : "json",
        url : "/sysUser",
        contentType : "application/json",
        data : JSON.stringify({
            userId : userId_add,
            userName : userName_add,
            phone : phone_add,
            wechat : wechat_add,
            userType : userType_add,
            password : password_add
        }),
        success : function(result) {
            myAlert(result.status,result.errMsg);
            $("#sysUser_addModal").modal('hide');
            $('#sysUser button[name="refresh"]').click();
        }
    });
});

//构造表格
$(function() {
    /*var sysUserName = $("#sysUserName").text();*/
    let $table = $('#sysUser #sysUser_table');
    $table.bootstrapTable({
        url: '/sysUser',
        method: 'get',
        toolbar: '#sysUser_toolbar',
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
            field: 'userId',
            title: '用户ID',
            align : 'center',
            width: 100
        }, {
            field: 'userName',
            title: '用户名称',
            align : 'center',
            width: 150
        }, {
            field: 'phone',
            title: '手机号',
            align : 'center',
            width: 100
        },
        /*    {
            field: 'wechat',
            title: '微信编号',
            align : 'center',
            width: 100
        },*/
            {
            field: 'userType',
            title: '用户类型',
            align : 'center',
            width: 100 ,
            formatter: function (value, row, index) {
                if (value == '3') {
                    return ['<h3><i style="color:rgba(53,162,255,0.98)" class=\"fa fa-group\"></i></h3>'].join();
                } else if(value == '2'){
                    return ['<h3><i style="color: #f4231b" class=\"fa fa-user\"></i></h3>'].join();
                }else {
                    return ['<h3><i style="color: #000000" class=\"fa fa-user-secret\"></i></h3>'].join();
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
        }, {
            field : 'operation',
            title : '操作',
            align : 'center',
            width : 100,
            events: sysUserEvents,
            formatter :sysUser
        } ]
    });
});