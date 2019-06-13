var ProjectIP = userserviceIp;

$(function(){
	initUserInfo();
})

/**
 * 初始化用户信息
 */
function initUserInfo(){
	var userId = $.getParamVal("userId");
	getUserById(userId,function(data){
		//通用的属性赋值
		$("#userForm input[type!=radio]").each(function(i,e){
			$(e).val(data.user[$(e).attr("name")]);
		})
		
		//特殊属性赋值
		if(data.user.userIsSuperAdmin=="1"){
			$("input[name='userIsSuperAdmin']").each(function(i,e){
				if($(e).val()=="1"){
					$(e).attr("checked","checked");
				}else{
					$(e).removeAttr("checked");
				}
			})
		}
		
		$("#userForm input[name='userEmail']").val(data.userInfo.userEmail);
		$("#userPassword").val(data.user.userPassWord);
		$("#curName").val(data.user.userLoginName);
		
		var v='',f='';
		for (var i = 0, l = data.orgs.length; i < l; i++) {
			v += data.orgs[i].orgName + ",";
			f += data.orgs[i].orgId + ",";
			// alert("id:"+nodes[i].id);
		}
		if (v.length > 0)
			v = v.substring(0, v.length - 1);
		if (f.length > 0)
			f = f.substring(0, f.length - 1);
		$("#orgId").val(f);
		$("#citySel").val(v);
	});
}

/**
 * 
 * @Title: getUserById   
 * @Description: 根据id获取用户信息   
 * @param:       
 * @return: void      
 * @throws
 */
function getUserById(userId,callback){
	$.ajax({
		url : ProjectIP+"secUser/getUesrDetail",
		type : "post",
		data : {"userId":userId},
		dataType : "JSON",
		success : function(data) {
			if (data.resultInfo.code=="0") {
				callback(data.data);
			} else {
				$.ligerDialog.error("查询用户信息失败！");
			}
		}
	})
}