package com.sino.ams.match.model;

import java.util.ArrayList;
import java.util.List;

import com.sino.ams.appbase.model.AMSSQLProducer;
import com.sino.ams.bean.SyBaseSQLUtil;
import com.sino.ams.ct.dto.EtsItemInfoDTO;
import com.sino.base.db.sql.model.SQLModel;
import com.sino.base.dto.DTO;
import com.sino.framework.dto.BaseUserDTO;

/**
 * <p>Title: SinoEAM</p>
 * <p>Description: </p>
 * <p>Copyright: 北京思诺搏信息技术有限公司 Copyright (c) 2008</p>
 * <p>Company: 北京思诺搏信息技术有限公司</p>
 * @author 于士博
 * @version 0.1
 *          Date: 2008-12-08
 */
public class ManualCtMatchAMSModel extends AMSSQLProducer {
	
	public ManualCtMatchAMSModel(BaseUserDTO userAccount, DTO dtoParameter) {
		super(userAccount, dtoParameter);
	}

	public SQLModel getPageQueryModel() {
		SQLModel sqlModel = new SQLModel();
		List sqlArgs = new ArrayList();
		EtsItemInfoDTO dto = (EtsItemInfoDTO) dtoParameter;
		String sqlStr = "SELECT /*+first_rows*/\n" +
				"       EII.SYSTEMID,\n" +
				"       EII.MAINTAIN_USER,\n" +
				"       EII.BARCODE,\n" +
				" 		EII.RESPONSIBILITY_USER," +
				"       AMS_PUB_PKG.GET_FLEX_VALUE(ESI.ITEM_CATEGORY, 'ITEM_TYPE') ITEM_CATEGORY,\n" +
				"       ESI.ITEM_NAME,\n" +
				"       ESI.ITEM_SPEC,\n" +
				"       EO.WORKORDER_OBJECT_CODE,\n" +
				"       EO.WORKORDER_OBJECT_NAME,\n" +
				"       AME.USER_NAME,\n" +
				"       AMD.DEPT_NAME,\n" +
				"       EII.START_DATE\n" +
				"FROM   ETS_ITEM_INFO      EII,\n" +
				"       ETS_SYSTEM_ITEM    ESI,\n" +
				"       ETS_OBJECT         EO,\n" +
				"       AMS_OBJECT_ADDRESS AOA,\n" +
				"       AMS_MIS_EMPLOYEE   AME,\n" +
				"       AMS_MIS_DEPT       AMD\n" +
				"WHERE  EII.ITEM_CODE = ESI.ITEM_CODE\n" +
				"       AND EII.ADDRESS_ID = AOA.ADDRESS_ID\n" +
				"       AND AOA.OBJECT_NO = EO.WORKORDER_OBJECT_NO\n" +
				"       AND EII.ORGANIZATION_ID = EO.ORGANIZATION_ID\n" +
				"       AND EII.RESPONSIBILITY_USER *= AME.EMPLOYEE_ID\n" +
				"       AND EII.RESPONSIBILITY_DEPT *= AMD.DEPT_CODE\n" +
//				"       AND (EO.OBJECT_CATEGORY < = 70 OR EO.OBJECT_CATEGORY = 80)\n" +
				"       AND EO.OBJECT_CATEGORY < 80 \n" +
//				"       AND EII.FINANCE_PROP = 'UNKNOW'\n" +
				"       AND EII.FINANCE_PROP IN ('UNKNOW','OTHER')\n" +
				"       AND ESI.ITEM_CATEGORY = dbo.NVL(?, ESI.ITEM_CATEGORY)\n" +
				"       AND ESI.IS_FIXED_ASSETS = 'Y'\n" +
				"       AND EII.DISABLE_DATE  " + SyBaseSQLUtil.isNullNoParam() + "   " +
				"       AND NOT EXISTS\n" +
				" (SELECT 1 FROM ETS_ITEM_MATCH EIM WHERE EII.SYSTEMID = EIM.SYSTEMID)\n" +
				"       AND NOT EXISTS\n" +
				" (SELECT 1 FROM ETS_NO_MATCH ENM WHERE ENM.SYSTEMID = EII.SYSTEMID)\n" +  //在ETS_NO_MATCH有的资产不参加匹配
				"       AND EII.ORGANIZATION_ID = ?\n" +
				"       AND (( " + SyBaseSQLUtil.isNull() + "  OR EII.BARCODE LIKE ?) OR \n" +
				"       ( " + SyBaseSQLUtil.isNull() + "  OR ESI.ITEM_NAME LIKE ?) OR\n" +
				"       ( " + SyBaseSQLUtil.isNull() + "  OR ESI.ITEM_SPEC LIKE ?))" +
				"       AND (( " + SyBaseSQLUtil.isNull() + "  OR EO.WORKORDER_OBJECT_CODE LIKE ?)\n" +
				"       OR ( " + SyBaseSQLUtil.isNull() + "  OR EO.WORKORDER_OBJECT_NAME LIKE ?))\n" +
				"       AND (( " + SyBaseSQLUtil.isNull() + "  OR EII.RESPONSIBILITY_DEPT LIKE ?) OR ( " + SyBaseSQLUtil.isNull() + "  OR AMD.DEPT_NAME LIKE ?))"+
				"       AND ( " + SyBaseSQLUtil.isNull() + "  OR AME.USER_NAME LIKE ?)";

//		if (servletConfig.getProvinceCode().equals(DictConstant.PROVINCE_CODE_JIN)) {
//	    sqlStr +=   " AND EXISTS (\n" +
//					" SELECT 1\n" +
//					"  FROM (SELECT EWDD.BARCODE\n" +                         //巡检
//					"          FROM ETS_WORKORDER_DIFF_DTL EWDD, ETS_WORKORDER EW\n" +
//					"         WHERE EWDD.WORKORDER_NO = EW.WORKORDER_NO\n" +
//					"           AND EW.WORKORDER_FLAG = 14\n" +
//					"           AND EWDD.VERIFY_RESULT = '扫描结果为准'\n" +
//					"        UNION ALL\n" +
//					"        SELECT EWDD.BARCODE\n" +
//					"          FROM ETS_WORKORDER_DIFF_DTL EWDD, ETS_WORKORDER EW\n" +
//					"         WHERE EWDD.WORKORDER_NO = EW.WORKORDER_NO\n" +
//					"           AND EWDD.VERIFY_RESULT = '系统数据为准'\n" +
//					"           AND NOT EXISTS (SELECT 1\n" +
//					"                  FROM ETS_WORKORDER_DTL EWD\n" +
//					"                 WHERE EWDD.WORKORDER_NO = EWD.WORKORDER_NO\n" +
//					"                   AND EWDD.BARCODE = EWD.BARCODE)\n" +
//					"        UNION ALL\n" +
//					"        SELECT EWD.BARCODE\n" +
//					"          FROM ETS_WORKORDER_DTL EWD, ETS_WORKORDER EW\n" +
//					"         WHERE EWD.WORKORDER_NO = EWD.WORKORDER_NO\n" +
//					"           AND EWD.ITEM_STATUS < 6\n" +
//					"           AND NOT EXISTS (SELECT 1\n" +
//					"                  FROM ETS_WORKORDER_DIFF_DTL EWDD\n" +
//					"                 WHERE EWDD.WORKORDER_NO = EWD.WORKORDER_NO\n" +
//					"                   AND EWD.BARCODE = EWDD.BARCODE)\n" +
//					"         UNION ALL\n" +
//					"         SELECT AACL.BARCODE\n" +                          //盘点
//					"          FROM AMS_ASSETS_CHK_LOG AACL\n" +
//					"         WHERE AACL.IS_EXIST = 'Y') E\n" +
//					"   WHERE E.BARCODE = EII.BARCODE)\n" ;
//		}
		
		
		sqlStr += " ORDER BY EO.WORKORDER_OBJECT_CODE ,ESI.ITEM_NAME DESC";

		sqlArgs.add(dto.getItemCategory());
		sqlArgs.add(userAccount.getOrganizationId());
		sqlArgs.add(dto.getKey());
		sqlArgs.add(dto.getKey());
		sqlArgs.add(dto.getKey());
		sqlArgs.add(dto.getKey());
		sqlArgs.add(dto.getKey());
		sqlArgs.add(dto.getKey());
		sqlArgs.add(dto.getWorkorderObjectName());
		sqlArgs.add(dto.getWorkorderObjectName());
		sqlArgs.add(dto.getWorkorderObjectName());
		sqlArgs.add(dto.getWorkorderObjectName());
		sqlArgs.add(dto.getResponsibilityDept());
		sqlArgs.add(dto.getResponsibilityDept());
		sqlArgs.add(dto.getResponsibilityDept());
		sqlArgs.add(dto.getResponsibilityDept());

		sqlArgs.add(dto.getMaintainUser());
		sqlArgs.add(dto.getMaintainUser());
		
		
		
		
		
//		String sqlStr = "";
//			sqlStr = "SELECT"
//				+ " EII.SYSTEMID,"
//				+ " EII.BARCODE,"
//				+ " ESI.ITEM_NAME,"
//				+ " ESI.ITEM_SPEC,"
//				+ " EII.RESPONSIBILITY_USER,"
//				+ " AME.USER_NAME,"
//				+ " EO.WORKORDER_OBJECT_NAME"
//				+ " FROM"
//				+ " ETS_ITEM_INFO EII,"
//				+ " ETS_SYSTEM_ITEM ESI,"
//				+ " AMS_MIS_EMPLOYEE AME,"
//				+ " ETS_OBJECT EO,"
//				+ " AMS_OBJECT_ADDRESS AOA"
//				+ " WHERE"
//				+ " EII.ITEM_CODE = ESI.ITEM_CODE"
//				+ " AND EII.RESPONSIBILITY_USER = AME.EMPLOYEE_ID"
//				+ " AND EII.ADDRESS_ID = AOA.ADDRESS_ID"
//				+ " AND AOA.OBJECT_NO = EO.WORKORDER_OBJECT_NO"
//				+ " AND (EII.FINANCE_PROP = 'OTHER' OR EII.FINANCE_PROP = 'UNKNOW')";
//			if(!("".equals(dto.getKey()))) {
//				sqlStr += " AND"
//						  + "("
//						  + "( " + SyBaseSQLUtil.isNull() + "  OR EII.BARCODE LIKE ? )"
//						  + " OR"
//						  + " ( " + SyBaseSQLUtil.isNull() + "  OR EII.RESPONSIBILITY_USER LIKE ? )"
//						  + ")";
//				sqlArgs.add(dto.getKey());
//				sqlArgs.add(dto.getKey());
//				sqlArgs.add(dto.getKey());
//				sqlArgs.add(dto.getKey());
//			}
//			if(!("".equals(dto.getWorkorderObjectName()))) {
//				sqlStr += " AND ( " + SyBaseSQLUtil.isNull() + "  OR EO.WORKORDER_OBJECT_NAME = ? )";
//				sqlArgs.add(dto.getWorkorderObjectName());
//				sqlArgs.add(dto.getWorkorderObjectName());
//			}
//			if(!("".equals(dto.getItemName()))) {
//				sqlStr += " AND ( " + SyBaseSQLUtil.isNull() + "  OR ESI.ITEM_NAME = ? )";
//				sqlArgs.add(dto.getItemName());
//				sqlArgs.add(dto.getItemName());
//			}
//			if(!("".equals(dto.getItemSpec()))){
//				
//				sqlStr += " AND ( " + SyBaseSQLUtil.isNull() + "  OR ESI.ITEM_SPEC = ? )";
//				sqlArgs.add(dto.getItemSpec());
//				sqlArgs.add(dto.getItemSpec());
//			}
			
		
		
		sqlModel.setSqlStr(sqlStr);
		sqlModel.setArgs(sqlArgs);
		return sqlModel;
	}
}
