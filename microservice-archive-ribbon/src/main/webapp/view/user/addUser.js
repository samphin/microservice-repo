var ProjectIP = userserviceIp;

$(function(){
	initPage();
	validateAddUser();
	
	$('#iptBirthday').datepicker({
		inline : true,
		dateFormat : "yy-mm-dd 00:00:00"
	});

	$('#userEnabledTime').datepicker({
		inline : true,
		dateFormat : "yy-mm-dd 00:00:00"
	});
	$('#userInvalidTime').datepicker({
		inline : true,
		dateFormat : "yy-mm-dd 00:00:00"
	});
	
	$("#btnSaveFunctionPower").on("click", function(){
		$("#btn_submit").click();
	});
})

function initPage() {
	var orgID = $("#orgId").val();
	getOrgData(orgID, function(zNodes) {
		initTree(zNodes);
	});
}

function getOrgData(orgID, callback, failFn) {
	$.post(ProjectIP+"secOrg/getAllOrg", {
		"organization.orgID" : orgID
	}, function(result) {
		var data = result.data
		callback(data);
	});
}

function initTree(zNodes) {
	var setting = {
		check : {
			enable : true,
			chkStyle : "radio",
			radioType : "all"
		},
		view : {
			dblClickExpand : false
		},
		data : {
			simpleData : {
				enable : true
			},
			key: {
				name: "orgName"
			}
		},
		callback : {
			onClick : onClick,
			onCheck : onCheck
		}
	};
	$.fn.zTree.init($("#treeDemo"), setting, zNodes);
}

function onClick(e, treeId, treeNode) {
	var zTree = $.fn.zTree.getZTreeObj("treeDemo");
	zTree.checkNode(treeNode, !treeNode.checked, null, true);
	return false;
}

function onCheck(e, treeId, treeNode) {
	var zTree = $.fn.zTree.getZTreeObj("treeDemo"), nodes = zTree.getCheckedNodes(true), v = "";
	// 传递过来的orgID
	f = "";
	for (var i = 0, l = nodes.length; i < l; i++) {
		v += nodes[i].orgName + ",";
		f += nodes[i].orgId + ",";
		// alert("id:"+nodes[i].id);
	}
	if (v.length > 0)
		v = v.substring(0, v.length - 1);
	var cityObj = $("#citySel");
	cityObj.val(v);

	// 设置传递给后台的orgID
	if (f.length > 0)
		f = f.substring(0, f.length - 1);
	var cityObj = $("#orgId");
	cityObj.val(f);
	hideMenu();
}

function showMenu() {
	var cityObj = $("#citySel");
	var cityOffset = $("#citySel").offset();
	$("#menuContent").css({
		left : cityOffset.left + "px",
		top : cityOffset.top + cityObj.outerHeight() + "px"
	}).slideDown("fast");
	$("body").bind("mouseover", onBodyOver);
}
function hideMenu() {
	$("#menuContent").fadeOut("fast");
	$("body").unbind("mouseover", onBodyOver);
}
function onBodyOver(event) {
	if (!(event.target.id == "citySel" || event.target.id.indexOf("treeDemo") > -1)) {
		hideMenu();
	}
}


/**
 * 
 * @Title: validateAddUser   
 * @Description: 验证添加用户的数据合法性  
 * @param:       
 * @return: void      
 * @throws
 */
function validateAddUser(){
	$('.form-horizontal').validate({
        errorElement : 'span',
        errorClass : 'help-block',
        focusInvalid : true,
        rules : {
            'userLoginName' : {
                required : true,
                isExist : {"url":ProjectIP+"secUser/findByUserLoginName"}
            },
            'userPassword':{
            	required : true,
            	onlyLetterNumber:true
            },
            'userUsesMark':{
            	required : true
            },
            'userEmail':{
            	required:true,
            	email:true
            },
            'userEnabledTime':{
            	required:true
            },
            'userInvalidTime':{
            	required:true
            },
        },
        highlight : function(element) {
            $(element).closest('.form-group').addClass('has-error');
        },
        success : function(label) {
            label.closest('.form-group').removeClass('has-error');
            label.remove();
        },
        errorPlacement : function(error, element) {
            element.parent('div').append(error);
        },
        submitHandler : function(form) {
        	addFunctionPower();
        }
    });
}


function addFunctionPower() {
	if($('.form-horizontal').find('.form-group').hasClass('has-error')){
		return;
	}
	var data = $('.form-horizontal').serialize();
	if(!$("#userPassword").val()){
		$.ligerDialog.error("密码不能为空！");
		return;
	}
	var userPassWord = $.md5($("#userPassword").val());
	data = data + "&userPassWord="+userPassWord;
	$.ajax({
		url : ProjectIP+"secUser/saveUser",
		type : "post",
		data : data,
		dataType : "JSON",
		success : function(data) {
			if (data.resultInfo.code=="0") {
				window.location.href="userList.html";
			} else {
				$.ligerDialog.error("添加用户失败！");
			}
		}
	})
}