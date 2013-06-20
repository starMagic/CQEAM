package com.sino.soa.mis.srv.AssetPeriodStatus.model;

import java.util.ArrayList;
import java.util.List;

import com.sino.base.db.sql.model.SQLModel;
import com.sino.base.exception.CalendarException;
import com.sino.base.exception.SQLModelException;
import com.sino.ams.system.user.dto.SfUserDTO;
import com.sino.framework.sql.BaseSQLProducer;
import com.sino.soa.mis.srv.AssetPeriodStatus.dto.SrvAssetPeriodStatusDTO;

/**
 * <p>Title: SrvAssetBookModel</p>
 * <p>Description:�����Զ�����SQL��������SrvAssetBookModel�����������Ҫ�����޸�</p>
 * <p>Copyright: Copyright (c) 2009</p>
 * <p>Company: ����˼ŵ����Ϣ�������޹�˾</p>
 * @author zhoujs
 * @version 1.0
 */


public class SrvAssetPeriodModel extends BaseSQLProducer {

	private SfUserDTO sfUser = null;

	/**
	 * ���ܣ��ʲ��˲����� SRV_ASSET_BOOK ���ݿ�SQL����㹹�캯��
	 * @param userAccount SfUserDTO ������ϵͳ�����ղ����û�����
	 * @param dtoParameter SrvAssetBookDTO ���β���������
	 */
	public SrvAssetPeriodModel(SfUserDTO userAccount, SrvAssetPeriodStatusDTO dtoParameter) {
		super(userAccount, dtoParameter);
		sfUser = userAccount;
	}

	/**
	 * ���ܣ�����Զ������ʲ��˲����� SRV_ASSET_BOOK���ݲ���SQLModel�������ʵ����Ҫ�޸ġ�
	 * @return SQLModel �������ݲ�����SQLModel
	 * @throws SQLModelException ���������쳣ʱת��Ϊ���쳣�׳�
	 */
	public SQLModel getDataCreateModel() {
		SQLModel sqlModel = new SQLModel();
		List sqlArgs = new ArrayList();
		SrvAssetPeriodStatusDTO periodStatusDTO = (SrvAssetPeriodStatusDTO)dtoParameter;
		String sqlStr = "INSERT INTO "
			+ " SRV_ASSET_PERIOD_STATUS("
			+"  BOOK_TYPE_CODE,"
			+ " PERIOD_NAME,"
			+ " MIS_PERIOD_NAME,"
			+ " START_DATE,"
			+ " END_DATE,"
			+ " PERIOD_OPEN_DATE,"
			+ " PERIOD_CLOSE_DATE,"
			+ " GL_TRANSFER_FLAG,"
			+ "	PERIOD_STATUS,"
			+ "	ORGANIZATION_ID"
			+ ") VALUES ("
			+ " ?, ?, ?, ?, ?, ?, ?,?, ?, ?)";
		sqlArgs.add(periodStatusDTO.getBookTypeCode());
		sqlArgs.add(periodStatusDTO.getPeriodName());
		sqlArgs.add(periodStatusDTO.getMisPeriodName());
		try {
			sqlArgs.add(periodStatusDTO.getStartDate());
			sqlArgs.add(periodStatusDTO.getEndDate());
			sqlArgs.add(periodStatusDTO.getPeriodOpenDate());
            if(periodStatusDTO.getPeriodStatus().equals("CLOSE")){
               sqlArgs.add(periodStatusDTO.getPeriodCloseDate());

            }else{
                 sqlArgs.add(null);
            }

			sqlArgs.add(periodStatusDTO.getGlTransferFlag());
			sqlArgs.add(periodStatusDTO.getPeriodStatus());
		} catch (CalendarException e) {
			e.printLog();
		}
		sqlArgs.add(periodStatusDTO.getOrganizationId());
		sqlModel.setSqlStr(sqlStr);
		sqlModel.setArgs(sqlArgs);
		return sqlModel;
	}

	/**
	 * ���ܣ�����Զ�����OU��֯��Ϣ���� SRV_OU_ORGANIZATION���ݸ���SQLModel�������ʵ����Ҫ�޸ġ�
	 * @return SQLModel �������ݸ�����SQLModel
	 */
	public SQLModel getDataUpdateModel() {
		SQLModel sqlModel = new SQLModel();
		List sqlArgs = new ArrayList();
		SrvAssetPeriodStatusDTO periodStatusDTO = (SrvAssetPeriodStatusDTO)dtoParameter;
		String sqlStr = "UPDATE "
			+ " SRV_ASSET_PERIOD_STATUS SET"
//			+"  BOOK_TYPE_CODE=?,"
//			+ " PERIOD_NAME=?,"
//			+ " MIS_PERIOD_NAME=?,"
			+ " START_DATE=?,"
			+ " END_DATE=?,"
			+ " PERIOD_OPEN_DATE=?,"
			+ " PERIOD_CLOSE_DATE=?,"
			+ " GL_TRANSFER_FLAG=?,"
//			+ " ORGANIZATION_ID=?,"
			+ "	PERIOD_STATUS=?"
			+ " WHERE BOOK_TYPE_CODE=? AND MIS_PERIOD_NAME=?"
			;
//		sqlArgs.add(periodStatusDTO.getBookTypeCode());
//		sqlArgs.add(periodStatusDTO.getPeriodName());
//		sqlArgs.add(periodStatusDTO.getMisPeriodName());
		try {
			sqlArgs.add(periodStatusDTO.getStartDate());
			sqlArgs.add(periodStatusDTO.getEndDate());
			sqlArgs.add(periodStatusDTO.getPeriodOpenDate());
			 if(periodStatusDTO.getPeriodStatus().equals("CLOSE")){
               sqlArgs.add(periodStatusDTO.getPeriodCloseDate());

            }else{
                 sqlArgs.add(null);
            }
		} catch (CalendarException e) {
			e.printLog();
		}
		sqlArgs.add(periodStatusDTO.getGlTransferFlag());
//        sqlArgs.add(periodStatusDTO.getOrganizationId());
        sqlArgs.add(periodStatusDTO.getPeriodStatus());
        sqlArgs.add(periodStatusDTO.getBookTypeCode());
		sqlArgs.add(periodStatusDTO.getMisPeriodName());
		sqlModel.setSqlStr(sqlStr);
		sqlModel.setArgs(sqlArgs);
		return sqlModel;
	}
	public SQLModel getEcouInforModel() {
		SQLModel sqlModel = new SQLModel();
			String sqlStr = "SELECT" 
			+"	ECOM.BOOK_TYPE_CODE,"
			+"	ECOM.MIS_PERIOD_NAME,"
			+"	ECOM.PERIOD_NAME"
			+"	FROM SRV_ASSET_PERIOD_STATUS ECOM";
		sqlModel.setSqlStr(sqlStr);
		return sqlModel;
	}
}