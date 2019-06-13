var ProjectIP=userserviceIp;
$(function(){
	initUserTable();
})

function initUserTable(){
	$('#userTable').bootstrapTable({
        method:'get',
        dataType:'json',
        sidePagination: "server",//分页方式：client客户端分页，server服务端分页（*）
        url:ProjectIP + "secUser/findAllByPage",
        sortable: true,//是否启用排序
        sortOrder: "left center desc",//排序方式
        showColumns:true,
        pagination:true,//是否显示分页
        paginationPreText: '上一页',
        paginationNextText: '下一页',
        queryParams : function(params) {
        	return {
    			"pageSize" : params.limit, //页面大小
				"pageNum" : params.offset/params.limit, //页码
        	}
        },//传递参数（*）
        responseHandler:function(res){
        	return {
        		"rows":res.data.content,
        		"total":res.data.totalElements
        	};
        },//加载服务器数据之前的处理程序，可以用来格式化数据。 参数：res为从服务器请求到的数据。
        pageNumber:1,//初始化加载第一页，默认第一页
        pageSize:10 ,//每页的记录行数（*）
        pageList: [0],
        showColumns: false,//是否显示所有的列
//        clickToSelect: true,//是否启用点击选中行
        uniqueId: "userId",//每一行的唯一标识，一般为主键列
//        showToggle:true,
        columns: [
            { 
                align:"center",
                title: '用户名',
                field: '',
                formatter : function(value,row, index) {
                    return row.userLoginName;
                }
            },
            { 
                align:"center",
                title: '是否是超级管理员',
                field: '',
                formatter : function(value,row, index) {
                	if(row.userIsSuperAdmin=="0"){
                		return "否";
                	}
                	
                    return "是";
                }
            },
            { 
                align:"center",
                title: '用户创建时间',
                field: '',
                formatter : function(value,row, index) {
                    return row.userCreateTime;
                }
            },
            { 
                align:"center",
                title: '操作',
                field: '',
                formatter : function(value,row, index) {
                	var result = '<a href="updateUser.html?userId='+row.userId+'"> <img title="修改" src="../img/edit.png"></a>';
                	if(row.userUsesMark=="1"){
                		result += ' <a href="javascript:;" class="usesMark'+row.userId+'"> <img onclick="updateUsesMark(0,\''+row.userId+'\')" title="禁用" src="../img/disabled.png"></a>';
                	}else{
                		result += ' <a href="javascript:;" class="usesMark'+row.userId+'"> <img onclick="updateUsesMark(1,\''+row.userId+'\')" title="启用" src="../img/start.png"></a>';
                	}
                	result += ' <a href="javascript:;" onclick="delUserById(\''+row.userId+'\')"> <img title="删除" src="../img/delete.png"></a> ';
                    return result;
                }
            }
        ],
        onPageChange:function(number, size){
        	$('#userTable').bootstrapTable('refresh', {query: {}});
        	return false;
        },
        onLoadSuccess: function (data) {
        	$("#searchBtn").unbind("click",findUserBykey);
        	$("#searchBtn").bind("click",findUserBykey);
            return false;
        }
    });
}
/**
 * 通过关键字查找用户
 */
function findUserBykey(){
	var keyword = $("#queryKeywordIpt").val();
	$('#userTable').bootstrapTable('refresh', {query: {"userName":keyword}});
}

/**
 * 通过id删除用户
 */
function delUserById(id){
	$.ligerDialog.confirm("是否确定删除当前用户！",function(yes){
		if(yes){
			$.ajax({
	    		type:"post",
	    		url: ProjectIP + "secUser/deleteUser",
	    		data: {"userId":id},
	    		dataType:"json",
	    		success:function(json){
	    			if(json.resultInfo.code=="0"){
	    				$('#userTable').bootstrapTable('refresh', {});
	    			}else{
	    				$.ligerDialog.error("删除失败！");
	    			}
	    		}
	    	});
		}
	});
}

/**
 * 
 * @Title: updateUsesMark   
 * @Description: 修改用户的启用状态   
 * @param: @param usesMark      
 * @return: void      
 * @throws
 */
function updateUsesMark(usesMark,userId){
	var $usesMark = $(".usesMark"+userId);
	$.ajax({
		type:"post",
		url: ProjectIP + "secUser/updateUsesMark",
		data: {"userId":userId,"usesMark":usesMark},
		dataType:"json",
		success:function(json){
			if(json.resultInfo.code=="0"){
				if(usesMark==0){
					$usesMark.html(' <img onclick="updateUsesMark(1,\''+userId+'\')" title="启用" src="../img/start.png">');
				}else{
					$usesMark.html(' <img onclick="updateUsesMark(0,\''+userId+'\')" title="禁用" src="../img/disabled.png">');
				}
			}else{
				$.ligerDialog.error("修改失败！");
			}
		}
	});
}
