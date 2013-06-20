package com.sino.soa.td.srv.assetretire.dao;

import com.sino.ams.appbase.dao.AMSBaseDAO;
import com.sino.base.constant.WorldConstant;
import com.sino.base.data.RowSet;
import com.sino.base.db.datatrans.CustomTransData;
import com.sino.base.db.datatrans.DataRange;
import com.sino.base.db.datatrans.DataTransfer;
import com.sino.base.db.datatrans.TransRule;
import com.sino.base.db.datatrans.TransferFactory;
import com.sino.base.db.query.SimpleQuery;
import com.sino.base.db.sql.model.SQLModel;
import com.sino.base.db.util.DBOperator;
import com.sino.base.dto.DTO;
import com.sino.base.exception.DataHandleException;
import com.sino.base.exception.DataTransException;
import com.sino.base.exception.QueryException;
import com.sino.base.exception.SQLModelException;
import com.sino.base.util.ArrUtil;
import com.sino.framework.dto.BaseUserDTO;
import com.sino.soa.mis.srv.assetretire.model.AssetRetirementModel;
import com.sino.soa.td.srv.assetretire.dto.TDAssetRetirementDTO;
import com.sino.soa.td.srv.assetretire.model.TDAssetRetirementModel;
import com.sino.soa.td.eip.fi.fa.sb_fi_fa_importassetretirmentsrv.ErrorItem;

import java.io.File;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User: wangzhipeng
 * Date: 2011-09-08
 * Function:�ʲ�����ͬ��_TD
 */
public class TDAssetRetirementDAO extends AMSBaseDAO {
    private List<ErrorItem> errorItemList;

    private TDAssetRetirementDTO dto = null;

    /**
     * ���ܣ����캯����
     * @param userAccount  UserAccountDTO �û���Ϣ
     * @param dtoParameter DTO ���������ݿ⽻��ʱ��Ҫ�Ĳ�����
     * @param conn         Connection ���ݿ�����
     */
    public TDAssetRetirementDAO(BaseUserDTO userAccount, TDAssetRetirementDTO dtoParameter, Connection conn) {
        super(userAccount, dtoParameter, conn);
        dto = dtoParameter;
    }

    protected void initSQLProducer(BaseUserDTO userAccount, DTO dtoParameter) {
    }

    public RowSet getRetireAssets() throws SQLModelException, QueryException {
        RowSet rs = null;
        TDAssetRetirementModel model = new TDAssetRetirementModel(userAccount, dto);
        SQLModel sqlModel = model.getPageQueryModel();
        SimpleQuery sq = new SimpleQuery(sqlModel, conn);
        sq.executeQuery();
        if (sq.hasResult()) {
            rs = sq.getSearchResult();

        }
        return rs;
    }

    public RowSet getRetirementAssets(String systemId[]) throws QueryException {
        RowSet rs = null;
        TDAssetRetirementModel model = new TDAssetRetirementModel(userAccount, dto);
        String systemIds = ArrUtil.arrToSqlStr(systemId);
        SQLModel sqlModel = model.getRetireAssetsModel(systemIds);
        SimpleQuery sq = new SimpleQuery(sqlModel, conn);
        sq.executeQuery();
        if (sq.hasResult()) {
            rs = sq.getSearchResult();
        }
        return rs;
    }

    /**
     * д����ͬ����־ ETS_MISFA_UPDATE_LOG  Ĭ��:2
     * ״̬����ʧ�ܣ�TRANS_STATUS:2  (�ɹ�) TRANS_STATUS:1
     * @param systemIds �ʲ�IDS
     * @param batchId   ͬ����
     */
    public void logRetireAssets(String systemId[], String batchId) throws DataHandleException {
        TDAssetRetirementModel model = new TDAssetRetirementModel(userAccount, dto);
        String systemIds = ArrUtil.arrToSqlStr(systemId);
        SQLModel sqlModel = model.getLogAssetsModel(systemIds, batchId);
        DBOperator.updateRecord(sqlModel, conn);
    }

    /**
     * ����ͬ����ϸ��־ (����)
     * ETS_MISFA_UPDATE_LOG TRANS_STATUS:2
     * 
     */
    public void updateErrorLog(List<ErrorItem> errorItemList, String batchId) throws DataHandleException {
        SQLModel sqlModel = null;
        List sqlModelList = new ArrayList();
        TDAssetRetirementModel model = new TDAssetRetirementModel(userAccount, dto);
        for (int i = 0; i < errorItemList.size(); i++) {
            ErrorItem errorItem = errorItemList.get(i);
            sqlModel = model.getUpdateLogModel(batchId, errorItem.getRECORDNUMBER(), 2, errorItem.getERRORMESSAGE());
            sqlModelList.add(sqlModel);
        }
        if (sqlModelList.size() > 0) {
            DBOperator.updateBatchRecords(sqlModelList, conn);
        }
    }
    
    /**
     * ����ͬ����ϸ��־(�ɹ�)
     * ��־��: ETS_MISFA_UPDATE_LOG  �ɹ�: TRANS_STATUS��1
     * @param batchId       ͬ����
     * @throws DataHandleException
     */
    public void updateResponseLog(String batchId) throws DataHandleException {
        TDAssetRetirementModel model = new TDAssetRetirementModel(userAccount, dto);
        SQLModel sqlModel = model.getUpdateLogCompleteModel(batchId);
        DBOperator.updateRecord(sqlModel, conn);
    }


    /**
     * �ʲ�����Excel����
     */
    public File getExportFile() throws DataTransException, SQLModelException {
        TDAssetRetirementModel modelProducer = new TDAssetRetirementModel(userAccount, dto);
        SQLModel sqlModel = modelProducer.getPageQueryModel();
        String reportTitle = "TDϵͳ�ʲ�����";
        String fileName = reportTitle + ".xls";
        TransRule rule = new TransRule();
        rule.setDataSource(sqlModel);
        rule.setSourceConn(conn);
        String filePath = WorldConstant.USER_HOME;
        filePath += WorldConstant.FILE_SEPARATOR;
        filePath += fileName;
        rule.setTarFile(filePath);
        DataRange range = new DataRange();
        rule.setDataRange(range);
        rule.setFieldMap(getFieldMap());
        CustomTransData custData = new CustomTransData();
        custData.setReportTitle(reportTitle);
        custData.setReportPerson(userAccount.getUsername());
        custData.setNeedReportDate(true);
        rule.setCustData(custData);
        rule.setCalPattern(CAL_PATT_50);
        TransferFactory factory = new TransferFactory();
        DataTransfer transfer = factory.getTransfer(rule);
        transfer.transData();
        return (File) transfer.getTransResult();
    }

    private Map getFieldMap() {
        Map fieldMap = new HashMap();
        fieldMap.put("COMPANY", "��˾����");
        fieldMap.put("BOOK_TYPE_CODE", "�˲�");
        fieldMap.put("BARCODE", "��ǩ��");
        fieldMap.put("ITEM_NAME", "�ʲ�����");
        fieldMap.put("COST", "�ɱ�");
        fieldMap.put("RETIREMENT_COST", "���ϳɱ�");
        fieldMap.put("DATE_RRETIRED", "��������");
        return fieldMap;
    }

}