<%--
  Created by HERRY.
  Date: 2007-11-19
  Time: 11:30:11
--%>
<%@ page contentType="text/html;charset=GBK" language="java" %>
<%@ page import="com.sino.base.util.StrUtil" %>
<%@ page import="com.sino.ams.constant.WebAttrConstant" %>
<%@ page import="com.sino.base.data.RowSet" %>
<%@ page import="com.sino.base.constant.db.QueryConstant" %>
<%@ page import="com.sino.base.web.request.upload.RequestParser" %>
<%@ page import="com.sino.ams.constant.DictConstant" %>
<%@ page import="com.sino.base.constant.web.WebActionConstant" %>
<%@ page import="com.sino.base.constant.web.WebConstant" %>
<%@ page import="com.sino.base.data.Row" %>
<html>
<%
    String transType = StrUtil.nullToString(request.getParameter("transType"));
    RequestParser parser = new RequestParser();
    parser.transData(request);
    RowSet rows = (RowSet) request.getAttribute(QueryConstant.SPLIT_DATA_VIEW);
    String orderTypeName = "";
    String url = "/servlet/com.sino.nm.spare2.servlet.AmsItemTransHServlet";
    if(transType.equals(DictConstant.BJCK)){
        orderTypeName = "出库";
        url = "/servlet/com.sino.nm.spare2.servlet.BjckServlet";
    }else if(transType.equals(DictConstant.BJFK)){
        orderTypeName = "返库";
        url = "/servlet/com.sino.nm.spare2.servlet.BjfkServlet";
    }else if(transType.equals(DictConstant.BJRK)){
        orderTypeName = "新购入库";
        url = "/servlet/com.sino.nm.spare2.servlet.AmsItemTransHServlet";
    }else if(transType.equals(DictConstant.BJBF)){
        orderTypeName = "报废";
        url = "/servlet/com.sino.nm.spare2.reject.servlet.AmsBjRejectServlet";
    }else if(transType.equals(DictConstant.BJSX)){
        orderTypeName = "送修";
        url = "/servlet/com.sino.nm.spare2.repair.servlet.BjSendRepairServlet";
    }else if(transType.equals(DictConstant.BJFH)){
        orderTypeName = "送修返还";
        url = "/servlet/com.sino.nm.spare2.returnBj.servlet.BjReturnRepairServlet";
    }else if(transType.equals(DictConstant.ZKZY)){
        orderTypeName = "子库转移";
        url = "/servlet/com.sino.nm.spare2.servlet.BjInvTransServlet";
    }
%>
  <head><title>备件<%=orderTypeName%></title>
      <link rel="stylesheet" type="text/css" href="/WebLibary/css/main.css">
    <script language="javascript" src="/WebLibary/js/Constant.js"></script>
    <script language="javascript" src="/WebLibary/js/calendar.js"></script>
    <script language="javascript" src="/WebLibary/js/CommonUtil.js"></script>
    <script language="javascript" src="/WebLibary/js/FormProcess.js"></script>
    <script language="javascript" src="/WebLibary/js/SinoToolBar.js"></script>
    <script language="javascript" src="/WebLibary/js/SinoToolBarConst.js"></script>
    <script language="javascript" src="/WebLibary/js/TableProcess.js"></script>
  </head>
  <body>
  <jsp:include page="/servlet/com.sino.framework.servlet.MessageProcessServlet" flush="true"/>
  <form name="mainFrm"  method="POST" action="/servlet/com.sino.nm.spare2.servlet.AmsItemTransHServlet">
<script type="text/javascript">
    printTitleBar("备件<%=orderTypeName%>单");
</script>
    <input type="hidden" name="flexValueId" value="">
	<input type="hidden" name="transType" value="<%=transType%>">
	<input type="hidden" name="transId" value="">
	<input type="hidden" name="act" value="">
<table border="0" width="100%" id="table1" cellspacing="0" cellpadding="0" class="queryHeadBg">
	<tr>
		<td width="10%" height="22" align="right">单据编号：</td>
		<td width="17%" height="22"><input type="text" name="transNo" style="width:100%" value="<%=parser.getParameter("transNo")%>"></td>
		<td width="10%" height="22" align="right">单据状态：</td>
		<td width="17%" height="22"><select name="transStatus" style="width:100%"><%=request.getAttribute(WebAttrConstant.TRANS_STATUS)%></select></td>
		<td width="10%" height="22" align="right"></td>
		<td width="17%" height="22"><%--<select name="fromObjectNo" style="width:100%"><%=request.getAttribute(WebAttrConstant.INV_OPTION)%></select>--%></td>
	</tr>
	<tr>
		<td height="22" align="right">创建日期：</td>
		<td height="22"><input type="text" name="fromDate" value="<%=parser.getParameter("fromDate")%>" style="width:80%" title="点击选择开始日期" readonly class="readonlyInput" onclick="gfPop.fStartPop(fromDate, toDate)">
            <img src="/images/calendar.gif" alt="点击选择开始日期" onclick="gfPop.fStartPop(fromDate, toDate)">
        </td>
		<td height="22" align="right">到</td>
		<td height="22"><input type="text" name="toDate" value="<%=parser.getParameter("toDate")%>" style="width:80%" title="点击选择截止日期" readonly class="readonlyInput" onclick="gfPop.fEndPop(fromDate, toDate)">
            <img src="/images/calendar.gif" alt="点击选择截止日期" onclick="gfPop.fEndPop(fromDate, toDate)">
        </td>
		<td height="22" colspan="2" align="right"><img src="/images/button/query.gif" onclick="do_Search();" alt="查询">&nbsp;&nbsp;<img src="/images/button/add.gif" onclick="do_Create();"></td>
	</tr>
</table>

<script type="text/javascript">
    var columnArr = new Array("单据编号","创建人","创建日期","单据状态");
    var widthArr = new Array("20%","10%","10%","10%");
    printTableHead(columnArr,widthArr);

</script>
<div style="overflow-y:scroll;left:1px;width:100%;height:300px">
    <table width="100%" border="1" bordercolor="#666666">
<%
    if (rows != null && rows.getSize() > 0) {
        Row row = null;
        for (int i = 0; i < rows.getSize(); i++) {
            row = rows.getRow(i);
%>
        <tr height="22" style="cursor:'hand'" onMouseMove="style.backgroundColor='#EFEFEF'" onMouseOut="style.backgroundColor='#ffffff'"
                onclick="do_ShowDetail('<%=row.getValue("TRANS_ID")%>')">
            <td width="20%" align="center"><%=row.getValue("TRANS_NO")%></td>
            <%--<td width="30%"><%=row.getValue("WORKORDER_OBJECT_NAME")%></td>--%>
            <td width="10%" align="left" ><%=row.getValue("CREATED_USER")%></td>
            <td width="10%" align="center" ><%=String.valueOf(row.getValue("CREATION_DATE"))%></td>
            <td width="10%" align="center"><%=row.getValue("ORDER_STATUS_NAME")%></td>
        </tr>
<%
	    }
    }
%>
    </table>
</div>
</form>
 <%if (rows != null && !rows.isEmpty()) {%>	
<div style="position:absolute;top:92%;left:0; right:20"><%=request.getAttribute(QueryConstant.SPLIT_PAGE_HTML)%></div>
<% }%>
</body>
<%=WebConstant.WAIT_TIP_MSG%>
</html>
<iframe width=174 height=189 name="gToday:normal:calendar.js" id="gToday:normal:calendar.js" src="/WebLibary/js/DateHTML.htm" scrolling="no" frameborder="0" style="visibility:visible; z-index:999; position:absolute; left:-500px; top:0;">
</iframe>

<script language="javascript">

function do_Search(){
    document.getElementById("$$$waitTipMsg$$$").style.visibility = "visible";
    document.mainFrm.act.value="<%=WebActionConstant.QUERY_ACTION%>";
	document.mainFrm.submit();
}

function do_Create(){
    var transType = document.mainFrm.transType.value;
    var url = "<%=url%>";
    url += "?act=<%=WebActionConstant.NEW_ACTION%>";
    var popscript = "width=1020,height=700,top=0,left=0,toolbar=no,menubar=no,scrollbars=no, resizable=yes,location=no, status=yes";
    window.open(url,"xgrkOrder",popscript);
}

function do_ShowDetail(primaryKey){
//	document.mainFrm.flexValueId.value = primaryKey;
	<%--document.mainFrm.act.value = "<%=WebActionConstant.DETAIL_ACTION%>";--%>
    var transType = document.mainFrm.transType.value;
    var url = "<%=url%>";
    url += "?act=<%=WebActionConstant.DETAIL_ACTION%>&transId="+primaryKey;
    var popscript = "width=1020,height=700,top=0,left=0,toolbar=no,menubar=no,scrollbars=no, resizable=yes,location=no, status=yes";
    window.open(url,"bjOrder",popscript);
}
  </script>