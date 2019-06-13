<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/sql" prefix="sql" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<c:set var="datePattern"><fmt:message key="date.format"/></c:set>
<script type="text/javascript">
function getContextPath(){
	var contextPath = '${pageContext.request.contextPath}';        	
	return contextPath;
}
function getBaseContextPath(){
	var baseContextPath='${pageContext.request.scheme}'+"://"+'${pageContext.request.serverName}'+":"+'${pageContext.request.serverPort}'+'${pageContext.request.contextPath}';       	
	return baseContextPath;
}

//获取系统用户编号
function getUserId(){
	var userId = '${sessionScope.DataServiceCenter2017_user.userID}';
	return userId;
}

//得到Resource文件服务器的访问地址
function getResourceIp(){
	return "http://192.168.100.162:9011/";
}
</script>