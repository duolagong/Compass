/**
 * 修改Echart容器大小
 * @param dom
 * @param x
 * @param y
 * @returns {*}
 */
function resizeContainer(dom, x, y) {
    dom.style.width = window.innerWidth * x + 'px';
    dom.style.height = window.innerHeight * y + 'px';
    var myChart = echarts.init(dom);
    myChart.clear();
    $(window).on('resize', function () {
        dom.style.width = window.innerWidth * x + 'px';
        dom.style.height = window.innerHeight * y + 'px';
        myChart.resize();
    });
    var myChart = echarts.init(dom);
    return myChart;
};

/**
 * 设置时间时补零
 * @param str
 * @returns {string}
 */
function timeAdd0(str) {
    if (str.length <= 1) {
        str = '0' + str;
    }
    return str
}

/**
 * 定义横坐标为mm：dd格式
 * @param value
 * @returns {string}
 */
function dateModified(value) {
    let date = new Date(value);
    let nowHours = timeAdd0(date.getHours().toString());
    let nowMin = timeAdd0(date.getMinutes().toString());
    let nowSeconds = timeAdd0(date.getSeconds().toString());
    var texts = [nowHours, nowMin, nowSeconds];
    return texts.join(':');
}

/**
 * 获取指定名称的Cookie
 * @param name
 * @returns {string|null}
 */
function getCookie(name){
    var arr,reg = new RegExp("(^| )" + name + "=([^;]*)(;|$)");
    if (arr = document.cookie.match(reg)) {
        return unescape(arr[2]);
    } else {
        return null;
    }
}

/**
 * 消息提醒
 * @param starts
 * @param alertMsg
 * @returns {boolean}
 */
function myAlert(starts , alertMsg) {
    $.toast().reset('all');
    $("body").removeAttr('class');
    if(starts){
        $.toast({
            heading: '执行成功',
            text: '<i class="jq-toast-icon fa fa-check fa-2x"></i><h2>'+alertMsg+'</h2>',
            position: 'top-right',
            loaderBg:'#7a5449',
            class: 'jq-has-icon jq-toast-success',
            hideAfter: 3000,
            stack: 12,
            showHideTransition: 'fade'
        });
    }else {
        alert(alertMsg);
        // $.toast({
        //     heading: '发生异常',
        //     text: '<i class="jq-toast-icon fa fa-check fa-2x"></i><h2>'+alertMsg+'</h2>',
        //     position: 'top-right',
        //     loaderBg:'#7a5449',
        //     class: 'jq-has-icon jq-toast-error',
        //     hideAfter: 7000,
        //     stack: 12,
        //     showHideTransition: 'fade'
        // });
    }
    return false;
}

/**
 * 校验是否为数字
 * @param value
 * @returns {boolean|boolean}
 */
function isNumber(val){

    var regPos = /^\d+(\.\d+)?$/; //非负浮点数
    //var regNeg = /^(-(([0-9]+\.[0-9]*[1-9][0-9]*)|([0-9]*[1-9][0-9]*\.[0-9]+)|([0-9]*[1-9][0-9]*)))$/; //负浮点数
    if(regPos.test(val)){
        return true;
    }else{
        return false;
    }

}