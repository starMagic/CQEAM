<%@ page contentType="text/html;charset=GBK" language="java" %>
<%@ page import = "com.sino.ams.match.amselementmatch.dto.AmsElementMatchDTO"%>
<%@ page import="com.sino.ams.constant.WebAttrConstant" %>
<%@ page import="com.sino.base.constant.db.QueryConstant" %>
<%@ page import="com.sino.base.constant.web.WebActionConstant" %>
<%@ page import="com.sino.base.constant.web.WebConstant" %>
<%@ page import="com.sino.base.data.Row" %>
<%@ page import="com.sino.base.data.RowSet" %>
<%@ page import="com.sino.base.util.StrUtil" %>
<%@ page import="com.sino.framework.security.bean.SessionUtil" %>
<%@ page import="com.sino.ams.system.user.dto.SfUserDTO" %>
<%@ page import="com.sino.base.web.request.upload.RequestParser" %>
<%@ page import="com.sino.base.web.CheckBoxProp" %>
<html>
<head>
    <title>投资分类对应关系维护</title>
    <link rel="stylesheet" type="text/css" href="/WebLibary/css/main.css">
    <link rel="stylesheet" type="text/css" href="/WebLibary/css/eam.css">  
    <script language="javascript" src="/WebLibary/js/Constant.js"></script>
    <script language="javascript" src="/WebLibary/js/CommonUtil.js"></script>
    <script type="text/javascript" src="/WebLibary/js/SinoToolBar.js"></script>
    <script type="text/javascript" src="/WebLibary/js/SinoToolBarConst.js"></script>
    <script type="text/javascript" src="/WebLibary/js/SinoToolBarScroll.js"></script>
    <script language="javascript" src="/WebLibary/js/FormProcess.js"></script>
    <script language="javascript" src="/WebLibary/js/ajax.js"></script>
    <script language="javascript" src="/WebLibary/js/CheckboxProcess.js"></script>
    <script language="javascript" src="/WebLibary/js/AssetsLookUp.js"></script>
    <script language="javascript" src="/WebLibary/js/jslib.js"></script>
	<script language="javascript" src="/WebLibary/js/TableProcess.js"></script>
</head>
<iframe width=174 height=189 name="gToday:normal:calendar.js" id="gToday:normal:calendar.js"
        src="/WebLibary/js/DateHTML.htm" scrolling="no" frameborder="0"
        style="visibility:visible; z-index:999; position:absolute; left:-500px; top:0;">
</iframe>
<body leftmargin="1" topmargin="0"  onload="initPage();">
<%
    RequestParser reqParser = new RequestParser();
    CheckBoxProp checkProp = new CheckBoxProp("subCheck");
    reqParser.setCheckBoxProp(checkProp);
    reqParser.transData(request);
    String[] subChecks = reqParser.getParameterValues("subCheck");
    if (subChecks != null) {
        for (int i = 0; i < subChecks.length; i++) {
            System.out.print(subChecks[i] + ";");
        }
    }
    SfUserDTO user = (SfUserDTO) SessionUtil.getUserAccount(request);
    AmsElementMatchDTO aemDTO = (AmsElementMatchDTO)request.getAttribute(WebAttrConstant.AMS_ELEMENT_MATCH_DTO);
    String action = StrUtil.nullToString(request.getParameter("act"));
%>

<form name="mainFrm" method="post" action="/servlet/com.sino.ams.match.amselementmatch.servlet.AmsElementMatchServlet">
    <script language="javascript">
        printTitleBar("投资分类与资产目录对应关系维护");
    </script>
    <input type="hidden" name="act" value="<%=action%>">
    <jsp:include page="/message/MessageProcess"/>
    <table border="0" width="100%"  id="table1">
    	<input type = "hidden" name = "accessType" value = "cex">
    <tr>
	    <td width="8%" align="right">资产目录代码：</td>
		<td width="10%"><input name="contentCode"  value = "<%=aemDTO.getContentCode() %>" class="input_style1" style="width:100%"></td>
		<td width="8%" align="right">资产目录描述：</td>
		<td width="10%"><input name="contentName"  value = "<%=aemDTO.getContentName() %>" class="input_style1" style="width:100%"></td>
		<td width="8%" align="right">网元编码：</td>
        <td width="10%"><input type="text" name="netUnitCode" value = "<%=aemDTO.getNetUnitCode() %>" class="input_style1" style="width:100%"></td>
        <td width = "12%" >
        	<img src="/images/eam_images/search.jpg" id="queryImg" style="cursor:'hand'" onclick="do_Search();" alt="查询">
        	<img src="/images/eam_images/new.jpg"    id="newImg"  style="cursor:'hand'" onclick="do_Create();" alt="新增">
        </td>
    </tr>
    <tr>
    	<td width="8%" align="right">投资分类名称：</td>
        <td width="10%">
        	<input type="text" name="investCatName" class="input_style1" style="width:100%" value="<%=aemDTO.getInvestCatName()%>">
        </td>  
    	<td width="8%" align="right">投资大类：</td>
        <td width="10%"><input type="text"  class="input_style1" style="width:100%" name="investCategory1" value="<%=aemDTO.getInvestCategory1() %>"></td>
        <td width="8%" align="right">投资中类：</td>
        <td width="10%"><input type="text"  class="input_style1" style="width:100%" name="investCategory2" value="<%=aemDTO.getInvestCategory2() %>"></td>
        <td width = "12%" >
 			<img src="/images/eam_images/export.jpg" id="queryImg" style="cursor:'hand'" onclick="do_Export();" alt="导出到Excel"> 			
 			<img src="/images/eam_images/delete.jpg" id="deleteImg" style="cursor:'hand'" onclick="do_Delete();" alt="删除">
        </td>   
   
    </tr>    
    
</table>   
</form>
		
<input type="hidden" name="act">    
<div id="headDiv" style="overflow:hidden;position:absolute;top:73px;left:1px;width:838px">
	<table class="headerTable" border="1" width="100%">
	    <tr height=20px onClick="executeClick(this)" style="cursor:hand" title="点击全选或取消全选">
            <td width="2%" align="center"><input type="checkbox" name="subCheck" class="headCheckbox" id="controlBox" onclick="checkAll('subCheck','contentCode')"></td>
	        <td align=center width="10%">资产目录代码</td>
	        <td align=center width="12%">资产目录描述</td>
	        <td align=center width="8%">投资分类代码</td>
	        <td align=center width="10%">投资分类名称</td>
	        <td align=center width="10%">投资大类</td>
	        <td align=center width="10%">投资种类</td>
		</tr>
    </table>
</div>

<div id="dataDiv" style="overflow:scroll;height:75%;width:847px;position:absolute;top:95px;left:1px" align="left" onscroll="document.getElementById('headDiv').scrollLeft = this.scrollLeft;">
  <form  name="mainFrm1" method="post" action="/servlet/com.sino.ams.match.amselementmatch.servlet.AmsElementMatchServlet">
    <table id="dataTable" width="100%" border="1"  style="TABLE-LAYOUT:fixed;word-break:break-all">
	    <input type="hidden" name="act">
  		<input type = "hidden" name = "accessType" value = "cex">
	<%
		RowSet rows = (RowSet) request.getAttribute(QueryConstant.SPLIT_DATA_VIEW);
		boolean hasData = (rows != null && !rows.isEmpty());
		if (hasData) {
				Row row = null;
				for (int i = 0; i < rows.getSize(); i++) {
					row = rows.getRow(i);
	%>
		<tr height="22" style="cursor:'hand'" onMouseMove="style.backgroundColor='#EFEFEF'" onMouseOut="style.backgroundColor='#ffffff'">
            <td width="2%" align="center"><input type="checkbox" name="contentCode" value="<%=row.getValue("CONTENT_CODE")%>"></td>
			<td  align=center width="10%"><input type="text" style="width:100%; border: 1px solid #FFFFFF;text-align: center" readonly value="<%=row.getValue("CONTENT_CODE")%>"></td>
			<td align=center width="12%"><input type="text" style="width:100%; border: 1px solid #FFFFFF;text-align: left" readonly value="<%=row.getValue("CONTENT_NAME")%>"></td>
			<td align=center width="8%"><input type="text" style="width:100%; border: 1px solid #FFFFFF;text-align: center" readonly value="<%=row.getValue("INVEST_CAT_CODE")%>"></td>
			<td align=center width="10%"><input type="text" style="width:100%; border: 1px solid #FFFFFF;text-align: center" readonly value="<%=row.getValue("INVEST_CAT_NAME")%>"></td>
			<td align=center width="10%"><input type="text" style="width:100%; border: 1px solid #FFFFFF;text-align: center" readonly value="<%=row.getValue("INVEST_CATEGORY1")%>"></td>
			<td align=center width="10%"><input type="text" style="width:100%; border: 1px solid #FFFFFF;text-align: center" readonly value="<%=row.getValue("INVEST_CATEGORY2")%>"></td>
		</tr>
	<%
			    }
		}
	%>
    </table>
  </form>
</div>
<%
if(hasData){
%>
<div style="position:absolute;top:456px;left:0; right:20"><%=request.getAttribute(QueryConstant.SPLIT_PAGE_HTML)%></div>
<%
	}
%>
<%=WebConstant.WAIT_TIP_MSG%>

</body>
</html>

<script type="text/javascript">
//调整页面加载时和数据的table框进行调节
function initPage(){
	do_SetPageWidth();
}

function do_Search() {
    mainFrm.act.value = "<%=WebActionConstant.QUERY_ACTION%>";
    mainFrm.submit();
    document.getElementById("$$$waitTipMsg$$$").style.visibility = "visible";
}

function do_Export(){                  //导出execl
    mainFrm.act.value = "<%=WebActionConstant.EXPORT_ACTION%>";
    mainFrm.submit();
}

function do_Create() {
    mainFrm.act.value = "NEW_ACTION";
    mainFrm.submit();
}

function do_Delete() {
    var checkedCount = getCheckedBoxCount("contentCode");
    if (checkedCount < 1) {
        alert("请至少选择一行数据！");
        return;
    } else {
    	if(confirm("是否确定删除资产目录与投资分类对应关系？继续请点击“确定”，否则请点击“取消”按钮")){
    		 mainFrm1.act.value = "<%=WebActionConstant.DELETE_ACTION %>";
    		 mainFrm1.submit();
    	}       
    }
}
</script>
