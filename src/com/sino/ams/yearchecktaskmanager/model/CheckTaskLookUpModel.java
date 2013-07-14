package com.sino.ams.yearchecktaskmanager.model;

import java.util.ArrayList;
import java.util.List;

import com.sino.ams.system.user.dto.SfUserDTO;
import com.sino.ams.yearchecktaskmanager.dto.AssetsYearCheckTaskLineDTO;
import com.sino.ams.yearchecktaskmanager.dto.AssetsYeartParentOrderDTO;
import com.sino.ams.yearchecktaskmanager.util.AssetsCheckTaskConstant;
import com.sino.base.db.sql.model.SQLModel;
import com.sino.base.dto.DTO;
import com.sino.base.lookup.LookUpModel;
import com.sino.base.lookup.LookUpProp;
import com.sino.framework.dto.BaseUserDTO;
import com.sino.framework.security.dto.ServletConfigDTO;

public class CheckTaskLookUpModel extends LookUpModel {

	private SfUserDTO user = null;
	private String proCode = "";

	public CheckTaskLookUpModel(BaseUserDTO userAccount, DTO dtoParameter,
			LookUpProp lookProp) {
		super(userAccount, dtoParameter, lookProp);
		this.user = (SfUserDTO) userAccount;

	}

	public void setServletConfig(ServletConfigDTO servletConfig) {
		super.setServletConfig(servletConfig);
		this.proCode = servletConfig.getProvinceCode();
	}

	@Override
	protected void produceSQLModel() {
		sqlModel = new SQLModel();
		List sqlArgs = new ArrayList();
		StringBuffer sqlStr = new StringBuffer();
		String lookUpName = lookProp.getLookUpName();
		if (lookUpName.equals(AssetsCheckTaskConstant.LOOK_UP_CITY_MANAGER)) {//省公司下发，查找地市公司资产管理员
			AssetsYearCheckTaskLineDTO dto = (AssetsYearCheckTaskLineDTO) dtoParameter;
			sqlStr.append(" SELECT '下发后自动生成' ORDER_NUMBER,EOCM.ORGANIZATION_ID IMPLEMENT_ORGANIZATION_ID,\n")
			      .append(" EOCM.COMPANY_CODE, \n")
			      .append(" EOCM.COMPANY, \n")
			      .append(" AMD.DEPT_CODE IMPLEMENT_DEPT_ID, \n")
			      .append(" AMD.DEPT_NAME IMPLEMENT_DEPT_NAME, \n")
			      .append(" SU.USERNAME IMPLEMENT_NAME, \n")
			      .append(" SU.USER_ID IMPLEMENT_BY \n")
				  .append(" FROM ETS_OU_CITY_MAP EOCM,SF_USER SU,SF_USER_AUTHORITY SUA,AMS_MIS_EMPLOYEE AME,AMS_MIS_DEPT AMD \n")
				  .append(" where EOCM.ORGANIZATION_ID = SU.ORGANIZATION_ID \n")
				  .append(" and EOCM.IS_TD='N' \n")
				  .append(" and SU.USER_ID = SUA.USER_ID \n")
				  .append(" and SUA.ROLE_NAME='"+AssetsCheckTaskConstant.ORDER_RECIVE_ROLE_TO_CITY+"' \n")
                  .append(" and SU.EMPLOYEE_NUMBER = AME.EMPLOYEE_NUMBER \n")
                  .append(" and AME.DEPT_CODE = AMD.DEPT_CODE ");
			if(dto.getCompanyCode()!=null && !dto.getCompanyCode().equals("")){
				sqlStr.append(" AND EOCM.COMPANY_CODE like ? ");
				sqlArgs.add("%"+dto.getCompanyCode().trim()+"%");
			}
			if(dto.getImplementName()!=null && !dto.getImplementName().equals("")){
				sqlStr.append(" AND SU.USERNAME like ? ");
				sqlArgs.add("%"+dto.getImplementName().trim()+"%");
			}
		}else if(lookUpName.equals("LOOK_UP_PARENT_ORDER_LEVE_2")
				||lookUpName.equals("LOOK_UP_PARENT_ORDER_LEVE_3")){//查找父级任务
			AssetsYeartParentOrderDTO dto = (AssetsYeartParentOrderDTO) dtoParameter;
			String orderLevel = "";
			if(lookUpName.equals("LOOK_UP_PARENT_ORDER_LEVE_2")){
				orderLevel = "2";
			}else if(lookUpName.equals("LOOK_UP_PARENT_ORDER_LEVE_3")){
				orderLevel = "3";
			}
			sqlStr.append(" SELECT ORDER_NAME PARENT_ORDER_NAME, ")
				  .append(" ORDER_NUMBER PARENT_ORDER_NUMBER,ORDER_TYPE PARENT_ORDER_TYPE, ")
				  .append(" case when ORDER_TYPE='"+AssetsCheckTaskConstant.ORDER_TYPE_ASS_CHK_TASK+"' then '年度盘点' " )
				  .append(" when ORDER_TYPE='"+AssetsCheckTaskConstant.ORDER_TYPE_NON_ADDRESS_SOFTWARE+"' then '非实地[软件类]' " )
				  .append(" when ORDER_TYPE='"+AssetsCheckTaskConstant.ORDER_TYPE_NON_ADDRESS_CLIENT+"' then '非实地[客户端类]' " )
				  .append(" when ORDER_TYPE='"+AssetsCheckTaskConstant.ORDER_TYPE_NON_ADDRESS_PIPELINE+"' then '非实地[光缆、杆路及管道类]' " )
				  .append(" when ORDER_TYPE='"+AssetsCheckTaskConstant.ORDER_TYPE_ADDRESS_WIRELESS+"' then ' 实地无线类' " )
				  .append(" when ORDER_TYPE='"+AssetsCheckTaskConstant.ORDER_TYPE_ADDRESS_NON_WIRELESS+"' then '实地非无线类' " )
				  .append(" else '年度盘点' end PARENT_ORDER_TYPE_NAME, ")
				  .append(" IMPLEMNET_DEPT_NAME PARENT_IMPLEMNET_DEPT_NAME ")
			      .append(" FROM AMS_ASSETS_YAER_CHECK_TASK_ORDER_LINE \n")
			      .append(" where ORDER_LEVEL = ?  AND IMPLEMENT_BY = ? ");
			sqlArgs.add(orderLevel);
			sqlArgs.add(user.getUserId());
			if(dto.getParetnOrderNumber()!=null && !dto.getParetnOrderNumber().equals("")){
				sqlStr.append(" AND ORDER_NUMBER like ? ");
				sqlStr.append("%"+dto.getParetnOrderNumber().trim()+"%");
			}
			
		}else if(lookUpName.equals("CITY_ADDRESS_FOR_CHECK_PERSON") || lookUpName.equals("CITY_ADDRESS_FOR_DEPT")
				||lookUpName.equals("CITY_NON_ADDRESS_SOFTEWARE_1") || lookUpName.equals("CITY_NON_ADDRESS_SOFTEWARE_2")
				||lookUpName.equals("CITY_NON_ADDRESS_SOFTEWARE_3") || lookUpName.equals("CITY_NON_ADDRESS_CLIENT_1")
				||lookUpName.equals("CITY_NON_ADDRESS_CLIENT_2") || lookUpName.equals("CITY_NON_ADDRESS_CLIENT_3")
				||lookUpName.equals("CITY_NON_ADDRESS_PIPELINE_1")||lookUpName.equals("CITY_NON_ADDRESS_PIPELINE_2")
				||lookUpName.equals("CITY_NON_ADDRESS_PIPELINE_3")){ //地市下发 ，第二级任务到第三级
			AssetsYearCheckTaskLineDTO dto = (AssetsYearCheckTaskLineDTO) dtoParameter;
			String orderName = "" ; //盘点任务工单名称
			String orderType = "" ; //盘点任务工单类型
			String orderTypeName = "" ; //盘点任务类型名称
			String implementsRoleName = "" ;//盘点任务执行人角色
			if(lookUpName.equals("CITY_ADDRESS_FOR_CHECK_PERSON")){//实地无线资产，查找盘点责任人
				orderName =AssetsCheckTaskConstant.ORDER_NAME_ADDRESS_WIRELESS;
				orderType = AssetsCheckTaskConstant.ORDER_TYPE_ADDRESS_WIRELESS;
				orderTypeName = "实地资产[无线类]";
				implementsRoleName= AssetsCheckTaskConstant.ORDER_RECIVE_ROLE_TO_DEPT_FOR_ADDRESS_WIRELESS;
			}else if(lookUpName.equals("CITY_ADDRESS_FOR_DEPT")){//实地非无线,查找部门管理员
				orderName =AssetsCheckTaskConstant.ORDER_NAME_ADDRESS_NON_WIRELESS;//实地非无线资产年度盘点任务
				orderType=AssetsCheckTaskConstant.ORDER_TYPE_ADDRESS_NON_WIRELESS;
				orderTypeName = "实地资产[非无线类]";
				implementsRoleName = AssetsCheckTaskConstant.ORDER_RECIVE_ROLE_TO_DEPT_FOR_ADDRESS_NON_WIRELESS;
			}else if(lookUpName.equals("CITY_NON_ADDRESS_SOFTEWARE_1") || lookUpName.equals("CITY_NON_ADDRESS_SOFTEWARE_2")
					||lookUpName.equals("CITY_NON_ADDRESS_SOFTEWARE_3")){
				orderName =AssetsCheckTaskConstant.ORDER_NAME_NON_ADDRESS; //非实地资产年度盘点任务
				orderType = AssetsCheckTaskConstant.ORDER_TYPE_NON_ADDRESS_SOFTWARE;
				orderTypeName = "非实地资产[软件类]";
				implementsRoleName = AssetsCheckTaskConstant.ORDER_RECIVE_ROLE_TO_DEPT_FOR_NON_ADDRESS;//非实地资产
			}else if(lookUpName.equals("CITY_NON_ADDRESS_CLIENT_1")||lookUpName.equals("CITY_NON_ADDRESS_CLIENT_2")
					||lookUpName.equals("CITY_NON_ADDRESS_CLIENT_3")){
				orderName = AssetsCheckTaskConstant.ORDER_NAME_NON_ADDRESS; //非实地资产年度盘点任务
				orderType = AssetsCheckTaskConstant.ORDER_TYPE_NON_ADDRESS_CLIENT;
				orderTypeName = "非实地资产[客户端类]";
				implementsRoleName = AssetsCheckTaskConstant.ORDER_RECIVE_ROLE_TO_DEPT_FOR_NON_ADDRESS;//非实地资产
			}else if(lookUpName.equals("CITY_NON_ADDRESS_PIPELINE_1")||lookUpName.equals("CITY_NON_ADDRESS_PIPELINE_2")
					||lookUpName.equals("CITY_NON_ADDRESS_PIPELINE_3")){
				orderName = AssetsCheckTaskConstant.ORDER_NAME_NON_ADDRESS; //非实地资产年度盘点任务
				orderType= AssetsCheckTaskConstant.ORDER_TYPE_NON_ADDRESS_PIPELINE;
				orderTypeName = "非实地资产[光缆、杆路及管道类]";
				implementsRoleName = AssetsCheckTaskConstant.ORDER_RECIVE_ROLE_TO_DEPT_FOR_NON_ADDRESS;//非实地资产
			}
			if(lookUpName.equals("CITY_ADDRESS_FOR_CHECK_PERSON") || lookUpName.equals("CITY_ADDRESS_FOR_DEPT")){//实地无线和非无线
				sqlStr.append(" SELECT '下发后自动生成' ORDER_NUMBER, \n")
			      .append(" '"+orderName+"' ORDER_NAME, \n")
			      .append(" '"+orderType+"' ORDER_TYPE, \n")
			      .append(" '"+orderTypeName+"' ORDER_TYPE_NAME, \n")
			      .append(" AMD.DEPT_CODE IMPLEMENT_DEPT_ID, \n")
			      .append(" AMD.DEPT_NAME IMPLEMENT_DEPT_NAME, \n")
			      .append(" SU.USERNAME IMPLEMENT_NAME, \n")
			      .append(" SU.ORGANIZATION_ID IMPLEMENT_ORGANIZATION_ID, \n")
			      .append(" SU.USER_ID IMPLEMENT_BY,AME.EMPLOYEE_NUMBER \n")
				  .append(" FROM SF_USER SU,SF_USER_AUTHORITY SUA,AMS_MIS_EMPLOYEE AME,AMS_MIS_DEPT AMD \n")
				  .append(" where SU.USER_ID = SUA.USER_ID \n")
				  .append(" and SU.EMPLOYEE_NUMBER = AME.EMPLOYEE_NUMBER \n")
				  .append(" and AME.DEPT_CODE = AMD.DEPT_CODE \n")
				  .append(" and SUA.ROLE_NAME ='"+implementsRoleName+"' \n")
                  .append(" and SU.ORGANIZATION_ID = ? \n");
				
			}else if(lookUpName.equals("CITY_NON_ADDRESS_SOFTEWARE_1") || lookUpName.equals("CITY_NON_ADDRESS_CLIENT_1")
					|| lookUpName.equals("CITY_NON_ADDRESS_PIPELINE_1") ){//地市
				sqlStr.append(" SELECT '下发后自动生成' ORDER_NUMBER, \n")
			      .append(" '"+orderName+"' ORDER_NAME, \n")
			      .append(" '"+orderType+"' ORDER_TYPE, \n")
			      .append(" '"+orderTypeName+"' ORDER_TYPE_NAME, \n")
			      .append(" AMD.DEPT_CODE IMPLEMENT_DEPT_ID, \n")
			      .append(" AMD.DEPT_NAME IMPLEMENT_DEPT_NAME, \n")
			      .append(" SU.USERNAME IMPLEMENT_NAME, \n")
			      .append(" SU.ORGANIZATION_ID IMPLEMENT_ORGANIZATION_ID, \n")
			      .append(" SU.USER_ID IMPLEMENT_BY,AME.EMPLOYEE_NUMBER \n")
				  .append(" FROM SF_USER SU,SF_USER_AUTHORITY SUA,AMS_MIS_EMPLOYEE AME,AMS_MIS_DEPT AMD \n")
				  .append(" where SU.USER_ID = SUA.USER_ID \n")
				  .append(" and SU.ENABLED = 'Y' ")
				  .append(" and SU.EMPLOYEE_NUMBER = AME.EMPLOYEE_NUMBER \n")
				  .append(" and AME.DEPT_CODE = AMD.DEPT_CODE \n")
				  .append(" and SUA.ROLE_NAME ='公司资产管理员' \n")
              .append(" and SU.ORGANIZATION_ID = ? \n");
			}else if(lookUpName.equals("CITY_NON_ADDRESS_SOFTEWARE_2") || lookUpName.equals("CITY_NON_ADDRESS_CLIENT_2")
					|| lookUpName.equals("CITY_NON_ADDRESS_PIPELINE_2") ){ //部门资产管理员
				sqlStr.append(" SELECT '下发后自动生成' ORDER_NUMBER, \n")
			      .append(" '"+orderName+"' ORDER_NAME, \n")
			      .append(" '"+orderType+"' ORDER_TYPE, \n")
			      .append(" '"+orderTypeName+"' ORDER_TYPE_NAME, \n")
			      .append(" AMD.DEPT_CODE IMPLEMENT_DEPT_ID, \n")
			      .append(" AMD.DEPT_NAME IMPLEMENT_DEPT_NAME, \n")
			      .append(" SU.USERNAME IMPLEMENT_NAME, \n")
			      .append(" SU.ORGANIZATION_ID IMPLEMENT_ORGANIZATION_ID, \n")
			      .append(" SU.USER_ID IMPLEMENT_BY,AME.EMPLOYEE_NUMBER \n")
				  .append(" FROM SF_USER SU,SF_USER_AUTHORITY SUA,AMS_MIS_EMPLOYEE AME,AMS_MIS_DEPT AMD \n")
				  .append(" where SU.USER_ID = SUA.USER_ID \n")
				  .append(" and SU.ENABLED = 'Y' ")
				  .append(" and SU.EMPLOYEE_NUMBER = AME.EMPLOYEE_NUMBER \n")
				  .append(" and AME.DEPT_CODE = AMD.DEPT_CODE \n")
				  .append(" and SUA.ROLE_NAME ='"+implementsRoleName+"' \n")
                .append(" and SU.ORGANIZATION_ID = ? \n");
			}else if(lookUpName.equals("CITY_NON_ADDRESS_SOFTEWARE_3")|| lookUpName.equals("CITY_NON_ADDRESS_CLIENT_3")
					|| lookUpName.equals("CITY_NON_ADDRESS_PIPELINE_3") ){//资产责任人
				sqlStr.append(" SELECT '下发后自动生成' ORDER_NUMBER, \n")
			      .append(" '"+orderName+"' ORDER_NAME, \n")
			      .append(" '"+orderType+"' ORDER_TYPE, \n")
			      .append(" '"+orderTypeName+"' ORDER_TYPE_NAME, \n")
			      .append(" AMD.DEPT_CODE IMPLEMENT_DEPT_ID, \n")
			      .append(" AMD.DEPT_NAME IMPLEMENT_DEPT_NAME, \n")
			      .append(" SU.USERNAME IMPLEMENT_NAME, \n")
			      .append(" SU.ORGANIZATION_ID IMPLEMENT_ORGANIZATION_ID, \n")
			      .append(" SU.USER_ID IMPLEMENT_BY,AME.EMPLOYEE_NUMBER \n")
				  .append(" FROM SF_USER SU,AMS_MIS_EMPLOYEE AME,AMS_MIS_DEPT AMD \n")
				  .append(" where  SU.ENABLED = 'Y' \n")
				  .append(" and SU.EMPLOYEE_NUMBER = AME.EMPLOYEE_NUMBER \n")
				  .append(" and AME.DEPT_CODE = AMD.DEPT_CODE \n")
                  .append(" and SU.ORGANIZATION_ID = ? \n");
			}
			sqlArgs.add(user.getOrganizationId());
			
			if(dto.getEmployeeNumber()!=null && !dto.getEmployeeNumber().equals("")){
				sqlStr.append(" AND SU.EMPLOYEE_NUMBER like ? ");
				sqlArgs.add("%"+dto.getEmployeeNumber().trim()+"%");
			}
			if(dto.getImplementDeptName()!=null && !dto.getImplementDeptName().equals("")){
				sqlStr.append(" AND AMD.DEPT_NAME like ? ");
				sqlArgs.add("%"+dto.getImplementDeptName().trim()+"%");
			}
		}else if(lookUpName.equals("DEPT_ADDRESS_FOR_CHECK_PERSON") || lookUpName.equals("DEPT_ADDRESS_FOR_DEPT")
				||lookUpName.equals("DEPT_NON_ADDRESS_SOFTEWARE") || lookUpName.equals("DEPT_NON_ADDRESS_CLIENT")
				|| lookUpName.equals("DEPT_NON_ADDRESS_PIPELINE")){ //部门下发 ，第三级任务到第四级,查找当前人部门下面的所有人员
			AssetsYearCheckTaskLineDTO dto = (AssetsYearCheckTaskLineDTO) dtoParameter;
			String orderName = "" ; //盘点任务工单名称
			String orderType = "" ; //盘点任务工单类型
			String orderTypeName = "" ; //盘点任务类型名称
			//String implementsRoleName = "" ;//盘点任务执行人角色
			if(lookUpName.equals("DEPT_ADDRESS_FOR_CHECK_PERSON")){//实地无线资产
				orderName =AssetsCheckTaskConstant.ORDER_NAME_ADDRESS_WIRELESS;
				orderType = AssetsCheckTaskConstant.ASSETS_TYPE_ADDRESS_WIRELESS;
				orderTypeName = "实地资产[无线类]";
				//implementsRoleName= AssetsCheckTaskConstant.ORDER_RECIVE_ROLE_TO_DEPT_FOR_ADDRESS_WIRELESS;
			}else if(lookUpName.equals("DEPT_ADDRESS_FOR_DEPT")){//实地非无线,
				orderName =AssetsCheckTaskConstant.ORDER_NAME_ADDRESS_NON_WIRELESS;//实地非无线资产年度盘点任务
				orderType=AssetsCheckTaskConstant.ORDER_TYPE_ADDRESS_NON_WIRELESS;
				orderTypeName = "实地资产[非无线类]";
				//implementsRoleName = AssetsCheckTaskConstant.ORDER_RECIVE_ROLE_TO_DEPT_FOR_ADDRESS_NON_WIRELESS;
			}else if(lookUpName.equals("DEPT_NON_ADDRESS_SOFTEWARE")){
				orderName =AssetsCheckTaskConstant.ORDER_NAME_NON_ADDRESS; //非实地资产年度盘点任务
				orderType = AssetsCheckTaskConstant.ORDER_TYPE_NON_ADDRESS_SOFTWARE;
				orderTypeName = "非实地资产[软件类]";
				//implementsRoleName = AssetsCheckTaskConstant.ORDER_RECIVE_ROLE_TO_DEPT_FOR_NON_ADDRESS;//非实地资产
			}else if(lookUpName.equals("DEPT_NON_ADDRESS_CLIENT")){
				orderName = AssetsCheckTaskConstant.ORDER_NAME_NON_ADDRESS; //非实地资产年度盘点任务
				orderType = AssetsCheckTaskConstant.ORDER_TYPE_NON_ADDRESS_CLIENT;
				orderTypeName = "非实地资产[客户端类]";
				//implementsRoleName = AssetsCheckTaskConstant.ORDER_RECIVE_ROLE_TO_DEPT_FOR_NON_ADDRESS;//非实地资产
			}else if(lookUpName.equals("DEPT_NON_ADDRESS_PIPELINE")){
				orderName = AssetsCheckTaskConstant.ORDER_NAME_NON_ADDRESS; //非实地资产年度盘点任务
				orderType= AssetsCheckTaskConstant.ORDER_TYPE_NON_ADDRESS_PIPELINE;
				orderTypeName = "非实地[光缆、杆路及管道类]";
				//implementsRoleName = AssetsCheckTaskConstant.ORDER_RECIVE_ROLE_TO_DEPT_FOR_NON_ADDRESS;//非实地资产
			}
			sqlStr.append(" SELECT '下发后自动生成' ORDER_NUMBER, \n")
			      .append(" '"+orderName+"' ORDER_NAME, \n")
			      .append(" '"+orderType+"' ORDER_TYPE, \n")
			      .append(" '"+orderTypeName+"' ORDER_TYPE_NAME, \n")
			      .append(" AMD.DEPT_CODE IMPLEMENT_DEPT_ID, \n")
			      .append(" AMD.DEPT_NAME IMPLEMENT_DEPT_NAME, \n")
			      .append(" SU.USERNAME IMPLEMENT_NAME, \n")
			      .append(" SU.ORGANIZATION_ID IMPLEMENT_ORGANIZATION_ID, \n")
			      .append(" SU.USER_ID IMPLEMENT_BY,AME.EMPLOYEE_NUMBER \n")
				  .append(" FROM SF_USER SU,AMS_MIS_EMPLOYEE AME,AMS_MIS_DEPT AMD \n")
				  .append(" where SU.EMPLOYEE_NUMBER = AME.EMPLOYEE_NUMBER \n")
				  .append(" and AME.DEPT_CODE = AMD.DEPT_CODE \n")
				  .append(" AND SU.ENABLED='Y' ")
				  .append(" and AMD.DEPT_CODE IN(select SGM.DEPT_ID from SF_GROUP SG,SINO_GROUP_MATCH SGM where SG.GROUP_ID = SGM.GROUP_ID and SGM.DEPT_ID = ? )")
				  .append(" and SU.ORGANIZATION_ID = ? \n");
			
			sqlArgs.add(user.getDeptCode());
			sqlArgs.add(user.getOrganizationId());
			
			if(dto.getEmployeeNumber()!=null && !dto.getEmployeeNumber().equals("")){
				sqlStr.append(" AND SU.EMPLOYEE_NUMBER like ? ");
				sqlArgs.add("%"+dto.getEmployeeNumber().trim()+"%");
			}
			if(dto.getImplementDeptName()!=null && !dto.getImplementDeptName().equals("")){
				sqlStr.append(" AND AMD.DEPT_NAME like ? ");
				sqlArgs.add("%"+dto.getImplementDeptName().trim()+"%");
			}
		}
		sqlModel.setArgs(sqlArgs);
		sqlModel.setSqlStr(sqlStr.toString());
	}
}
