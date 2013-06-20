package com.sino.ams.match.dao;

import java.io.File;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import com.sino.ams.match.dto.AmsNoMactingAssetDTO;
import com.sino.ams.match.model.AmsNoMactingAssetModel;
import com.sino.ams.system.user.dto.SfUserDTO;
import com.sino.base.constant.WorldConstant;
import com.sino.base.db.datatrans.CustomTransData;
import com.sino.base.db.datatrans.DataRange;
import com.sino.base.db.datatrans.DataTransfer;
import com.sino.base.db.datatrans.TransRule;
import com.sino.base.db.datatrans.TransferFactory;
import com.sino.base.db.sql.model.SQLModel;
import com.sino.base.dto.DTO;
import com.sino.base.exception.DataTransException;
import com.sino.base.exception.SQLModelException;
import com.sino.framework.dao.BaseDAO;
import com.sino.framework.dto.BaseUserDTO;

/**
 * Created by IntelliJ IDEA.
 * User: jiangtao
 * Date: 2007-11-21
 * Time: 11:54:53
 * To change this template use File | Settings | File Templates.
 */
public class AmsNoMactingAssetDAO extends BaseDAO  {
	private SfUserDTO sfUser;

	{
		sfUser = null;
	}

	/**
	 * ���ܣ����캯����
	 *
	 * @param userAccount  UserAccountDTO �û���Ϣ
	 * @param dtoParameter DTO ���������ݿ⽻��ʱ��Ҫ�Ĳ�����
	 * @param conn         Connection ���ݿ�����
	 */
	public AmsNoMactingAssetDAO(SfUserDTO userAccount, AmsNoMactingAssetDTO dtoParameter, Connection conn) {
		super(userAccount, dtoParameter, conn);
		 sfUser = userAccount;
	}
	/**
	* ���ܣ�SQL������BaseSQLProducer�ĳ�ʼ����
	*
	* @param userAccount  BaseUserDTO ��ϵͳ���ղ����û���
	* @param dtoParameter DTO ���β���������
	*/

	 protected void initSQLProducer(BaseUserDTO userAccount, DTO dtoParameter) {
	   AmsNoMactingAssetDTO dtoPara = (AmsNoMactingAssetDTO) dtoParameter;
	   super.sqlProducer = new AmsNoMactingAssetModel((SfUserDTO) userAccount, dtoPara);



	}


	/**
	 * ���ܣ�����Excel�ļ���
	 * @return File
	 * @throws DataTransException
	 */
	public File exportFile() throws DataTransException {
		File file = null;
		try {
			SQLModel sqlModel = sqlProducer.getPageQueryModel();
			TransRule rule = new TransRule();
			rule.setDataSource(sqlModel);
			rule.setSourceConn(conn);
			String fileName = "δƥ���ʲ��嵥.xls";
			String filePath = WorldConstant.USER_HOME;
			filePath += WorldConstant.FILE_SEPARATOR;
			filePath += fileName;
			rule.setTarFile(filePath);
			DataRange range = new DataRange();
			rule.setDataRange(range);

			Map fieldMap = new HashMap();
			fieldMap.put("TAG_NUMBER", "�ʲ���ǩ");
			fieldMap.put("ASSET_NUMBER", "�ʲ����");
			fieldMap.put("ASSETS_DESCRIPTION", "�ʲ�����");
			fieldMap.put("MODEL_NUMBER", "�ʲ��ͺ�");
			fieldMap.put("ASSETS_LOCATION", "�ʲ��ص�");
			fieldMap.put("ASSETS_LOCATION_CODE", "�ص����");
			fieldMap.put("DATE_PLACED_IN_SERVICE", "��������");
			fieldMap.put("LIFE_IN_YEARS", "ʹ������");
			fieldMap.put("COST", "�ʲ�ԭֵ");
			fieldMap.put("BOOK_TYPE_CODE", "�ʲ��˲�");
			fieldMap.put("COST_CENTER_NAME", "�ɱ�����");

			rule.setFieldMap(fieldMap);

			CustomTransData custData = new CustomTransData();
			custData.setReportTitle("δƥ���ʲ��嵥");
			custData.setReportPerson(sfUser.getUsername());
			custData.setNeedReportDate(true);
			rule.setCustData(custData);
			TransferFactory factory = new TransferFactory();
			DataTransfer transfer = factory.getTransfer(rule);
			transfer.transData();
			file = (File) transfer.getTransResult();
		} catch (SQLModelException ex) {
			ex.printLog();
			throw new DataTransException(ex);
		}
		return file;
	}
}