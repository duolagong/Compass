/**
 * 初始化化数据
 * @returns {*}
 */
function initMemory(){
    var data;
    $.ajax({
        url:'/sysMemory/init',
        method: 'get',
        async:false,
        dataType : "json",
        success:function(result){
            if(result.status){
                data = result.data;
            }else{
                myAlert(result.status,result.errMsg);
            }
        }
    })
    return data;
}
var initSysMemoryData;