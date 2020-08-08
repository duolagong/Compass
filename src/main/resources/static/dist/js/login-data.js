/*Login Init*/
 
"use strict"; 
$('#owl_demo_1').owlCarousel({
	items: 1,
	animateOut: 'fadeOut',
	loop: true,
	margin: 10,
	autoplay: true,
	mouseDrag: false

});

$('#login-button').click(function (event) {
	debugger;
	var userid=document.getElementById("userid").value;
	var password=document.getElementById("password").value;
	if(!userid || !password){
		alert("请输入用户名和密码！");
	}
	else{
		window.parent.location.href="/SysUser/login?userid="+userid+"&password="+password;
	}
});
