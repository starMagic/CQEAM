package com.sino.ams.workorder.dao;

import com.sino.ams.system.user.dto.SfUserDTO;
import com.sino.ams.workorder.dto.EtsWorkorderDTO;
import com.sino.ams.workorder.model.TrunListMisQueryModel;
import com.sino.base.constant.WorldConstant;
import com.sino.base.db.datatrans.*;
import com.sino.base.db.sql.model.SQLModel;
import com.sino.base.dto.DTO;
import com.sino.base.exception.DataTransException;
import com.sino.base.exception.SQLModelException;
import com.sino.framework.dao.BaseDAO;
import com.sino.framework.dto.BaseUserDTO;

import java.io.File;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 12-1-18
 * Time: ����11:08
 * To change this template use File | Settings | File Templates.
 */
public class TrunListMisQueryDAO extends BaseDAO {
    public TrunListMisQueryDAO(SfUserDTO userAccount, EtsWorkorderDTO dtoParameter, Connection conn) {
        super(userAccount, dtoParameter, conn);
        user = userAccount;
        initSQLProducer(userAccount, dtoParameter);
    }

    protected void initSQLProducer(BaseUserDTO userAccount, DTO dtoParameter) {
    	EtsWorkorderDTO ffDTO=(EtsWorkorderDTO) dtoParameter;
        super.sqlProducer = new TrunListMisQueryModel((SfUserDTO)userAccount, ffDTO);
    }

    private SfUserDTO user = null;

    /**
	 * ���ܣ���ȡת���嵥��ѯExcel�ļ�
	 * @return File
	 * @throws com.sino.base.exception.DataTransException
	 */
	public File getExportFile(EtsWorkorderDTO dto) throws DataTransException {
		File file = null;
		try {
			SQLModel sqlModel = sqlProducer.getPageQueryModel();
			String reportTitle = reportTitle = "ת���嵥��ѯ";
			String fileName = reportTitle + ".xls";
			String filePath = WorldConstant.USER_HOME;
			filePath += WorldConstant.FILE_SEPARATOR;
			filePath += fileName;
			TransRule rule = new TransRule();
			rule.setDataSource(sqlModel);
			rule.setSourceConn(conn);
			rule.setTarFile(filePath);
			DataRange range = new DataRange();
			rule.setDataRange(range);
			rule.setFieldMap(getFieldMap(dto));
			CustomTransData custData = new CustomTransData();
			custData.setReportTitle(reportTitle);
			custData.setReportPerson(user.getUsername());
			custData.setNeedReportDate(true);
			rule.setCustData(custData);
			rule.setCalPattern(LINE_PATTERN);
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

    private Map getFieldMap(EtsWorkorderDTO dto){
    	Map fieldMap = new HashMap();
        fieldMap.put("ASSETS_LOCATION_CODE", "�ص���");
        fieldMap.put("ASSETS_LOCATION", "�ص���");
		fieldMap.put("SEGMENT1", "��Ŀ���");
        fieldMap.put("NAME", "��Ŀ����");
        fieldMap.put("ORG_NAME", "��˾");

        fieldMap.put("BARCODE", "��ǩ��");
        fieldMap.put("ITEM_NAME", "�ʲ�����");
        fieldMap.put("ITEM_SPEC", "����ͺ�");
        fieldMap.put("ITEM_QTY", "����");
        fieldMap.put("ITEM_CATEGORY_DESC", "�ʲ����");

        fieldMap.put("CONTENT_CODE", "�ʲ�Ŀ¼����");
        fieldMap.put("CONTENT_NAME", "�ʲ�Ŀ¼����");

        fieldMap.put("DEPT_NAME", "���β���");
        fieldMap.put("USER_NAME", "������");

        fieldMap.put("MAINTAIN_DEPT", "ʹ�ò���");
        fieldMap.put("MAINTAIN_USER", "ʹ����");

        fieldMap.put("CREATION_DATE", "��������");
        fieldMap.put("LAST_UPDATE_DATE", "����޸�����");


        return fieldMap;
	}
}
