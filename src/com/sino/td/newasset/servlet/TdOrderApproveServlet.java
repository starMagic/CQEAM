package com.sino.td.newasset.servlet;

import java.io.IOException;
import java.sql.Connection;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sino.ams.newasset.bean.AssetsOptProducer;
import com.sino.ams.newasset.constant.AssetsActionConstant;
import com.sino.ams.newasset.constant.AssetsDictConstant;
import com.sino.ams.newasset.constant.AssetsMessageKeys;
import com.sino.ams.newasset.constant.AssetsURLList;
import com.sino.ams.newasset.constant.AssetsWebAttributes;
import com.sino.ams.system.user.dto.SfUserDTO;
import com.sino.base.constant.message.MessageConstant;
import com.sino.base.constant.message.MsgKeyConstant;
import com.sino.base.dto.DTOSet;
import com.sino.base.dto.Request2DTO;
import com.sino.base.exception.DTOException;
import com.sino.base.exception.PoolPassivateException;
import com.sino.base.exception.QueryException;
import com.sino.base.exception.UploadException;
import com.sino.base.message.Message;
import com.sino.base.util.StrUtil;
import com.sino.base.web.ServletForwarder;
import com.sino.base.web.request.upload.RequestParser;
import com.sino.flow.bean.FlowAction;
import com.sino.flow.dto.FlowDTO;
import com.sino.framework.security.bean.SessionUtil;
import com.sino.framework.security.dto.ServletConfigDTO;
import com.sino.framework.servlet.BaseServlet;
import com.sino.td.commom.TdURLDefineList;
import com.sino.td.newasset.dao.TdAssetsTransHeaderDAO;
import com.sino.td.newasset.dao.TdAssetsTransLineDAO;
import com.sino.td.newasset.dao.TdOrderApproveDAO;
import com.sino.td.newasset.dto.TdAssetsTransHeaderDTO;
import com.sino.td.newasset.dto.TdAssetsTransLineDTO;


/**
 * <p>Title: OrderApproveServlet</p>
 * <p>Description:�����Զ����ɷ������OrderApproveServlet�����������Ҫ�����޸�</p>
 * <p>Copyright: Copyright (c) 2007</p>
 * <p>Company: ����˼ŵ����Ϣ�������޹�˾</p>
 * @author ����ʤ
 * @version 1.0
 */

public class TdOrderApproveServlet extends BaseServlet {

	/**
	 * @param req HttpServletRequest
	 * @param res HttpServletResponse
	 * @throws ServletException
	 * @throws IOException
	 */
	public void performTask(HttpServletRequest req, HttpServletResponse res) throws
			ServletException, IOException {
		String forwardURL = "";
		Message message = SessionUtil.getMessage(req);
		Connection conn = null;
		try {
			SfUserDTO user = (SfUserDTO) getUserAccount(req);
			Request2DTO req2DTO = new Request2DTO();
			req2DTO.setDTOClassName(TdAssetsTransHeaderDTO.class.getName());
			TdAssetsTransHeaderDTO dto = (TdAssetsTransHeaderDTO) req2DTO.getDTO(req);
			ServletConfigDTO servletConfig = getServletConfig(req);
			String provinceCode = servletConfig.getProvinceCode();
			dto.setServletConfig(servletConfig);
			FlowDTO flowDTO = FlowAction.getDTOFromReq(req);
			String action = dto.getAct();
			conn = getDBConnection(req);
            //���������ʲ�����,��ȡѡ�еı����ʲ�������ʼ��
            RequestParser par = new RequestParser();
            par.transData(req);
            String[] barcodess = par.getParameterValues("subCheck");
            dto.setBarcodess(barcodess);

            TdOrderApproveDAO approveDAO = new TdOrderApproveDAO(user, dto, conn);
			approveDAO.setServletConfig(servletConfig);
			String transType = dto.getTransType();
			String transferType = dto.getTransferType();
			if (action.equals(AssetsActionConstant.EDIT_ACTION)) { //��������ҳ��
				approveDAO.setDTOClassName(TdAssetsTransHeaderDTO.class.getName());
				TdAssetsTransHeaderDTO headerDTO = (TdAssetsTransHeaderDTO)approveDAO.getDataByPrimaryKey();
                String accessSheet = approveDAO.getAccessSheet();//��������
                headerDTO.setAccessSheet(accessSheet);
                if (headerDTO == null) {
					message = getMessage(MsgKeyConstant.DATA_NOT_EXIST);
					message.setIsError(true);
					forwardURL = MessageConstant.MSG_PRC_SERVLET;
				} else {
					TdAssetsTransHeaderDAO headerDAO = new TdAssetsTransHeaderDAO(user, dto, conn);
					boolean isGroupPID = headerDAO.isGroupFlowId();
		            req.setAttribute(AssetsWebAttributes.IS_GROUP_PID, isGroupPID+"");
					headerDTO.setAttribute1(dto.getAttribute1());
					headerDTO.setAttribute2(dto.getAttribute2());
					headerDTO.setAttribute3(dto.getAttribute3());
					headerDTO.setAttribute4(dto.getAttribute4());
					headerDTO.setAttribute5(dto.getAttribute5());
					headerDTO.setSectionRight(dto.getSectionRight());
					headerDTO.setHiddenRight(dto.getHiddenRight());

					headerDTO.setServletConfig(servletConfig);
					TdAssetsTransLineDTO lineDTO = new TdAssetsTransLineDTO();
					lineDTO.setTransId(headerDTO.getTransId());
					lineDTO.setTransType(headerDTO.getTransType());
					TdAssetsTransLineDAO lineDAO = new TdAssetsTransLineDAO(user, lineDTO, conn);
					lineDAO.setDTOClassName(TdAssetsTransLineDTO.class.getName());
					DTOSet ds = (DTOSet) lineDAO.getDataByForeignKey("transId");
					req.setAttribute(AssetsWebAttributes.ORDER_LINE_DATA, ds);
					headerDTO.setCalPattern(LINE_PATTERN);
					req.setAttribute(AssetsWebAttributes.ORDER_HEAD_DATA, headerDTO);
					if (provinceCode.equals(AssetsDictConstant.PROVINCE_CODE_NM)) { //�������⴦��
						if (headerDTO.getAttribute1().equals(AssetsDictConstant.TRANS_EDIT_YES)) { //��˾��������⴦��
							headerDTO = fillOptions(headerDTO, user, conn);
							forwardURL = AssetsURLList.TRANS_EDIT_PAGE_NM;
						} else {
							forwardURL = AssetsURLList.APPROVE_EDIT_NM;
						}
					} else {
						//forwardURL = AssetsURLList.APPROVE_EDIT_PAGE;
						forwardURL = TdURLDefineList.APPROVE_EDIT_PAGE_TD;
					}
				}
			} else if (action.equals(AssetsActionConstant.APPROVE_ACTION)) { //�������̣����Ӹ�����ͷ����˻�����(2008-12-01 17:34)
				DTOSet orderLines = new DTOSet();
				if(dto.getAttribute4().equals(AssetsDictConstant.EDIT_ACCOUNT)){//�ýڵ����۾ɷ����˻��ɱ༭�ڵ�
					req2DTO.setDTOClassName(TdAssetsTransLineDTO.class.getName());
					req2DTO.setIgnoreFields(TdAssetsTransHeaderDTO.class);
					orderLines = req2DTO.getDTOSet(req);
				}
				if(orderLines != null){
					int lineCount = orderLines.getSize();
					for(int i = 0; i < lineCount; i++){
						TdAssetsTransLineDTO lineDTO = (TdAssetsTransLineDTO)orderLines.getDTO(i);
						lineDTO.setTransId(dto.getTransId());
						orderLines.set(i, lineDTO);
					}
				}
                //���������ʲ�����
                RequestParser parser = new RequestParser();
                parser.transData(req);
                String[] barcodes = parser.getParameterValues("subCheck");
                boolean operateResult = approveDAO.approveOrder(flowDTO, orderLines, barcodes);
				dto = (TdAssetsTransHeaderDTO) approveDAO.getDTOParameter();
				message = approveDAO.getMessage();
				if (operateResult) {
					if (provinceCode.equals(AssetsDictConstant.PROVINCE_CODE_NM)) { //������������
						forwardURL = TdURLDefineList.ORDER_APPROVE_SERVLET_TD;
						forwardURL += "?act=" +AssetsActionConstant.DETAIL_ACTION;
					} else { //��������������
//						if (transType.equals(AssetsDictConstant.ASS_RED)) { //����
//							if (dto.isFlow2End() &&
//								!transferType.equals(AssetsDictConstant.TRANS_INN_DEPT)) { //��ֹ�ڵ�
//								forwardURL = AssetsURLList.ASSETS_RCV_SERVLRT;
//								forwardURL += "?act=" +AssetsActionConstant.DETAIL_ACTION;
//								forwardURL += "&transId=" + dto.getTransId();
//							} else { //�м�ڵ�
//								forwardURL = TdURLDefineList.ORDER_APPROVE_SERVLET_TD;
//								forwardURL += "?act=" + AssetsActionConstant.DETAIL_ACTION;
//							}
//						} else {
							forwardURL = TdURLDefineList.ORDER_APPROVE_SERVLET_TD;
							forwardURL += "?act=" +AssetsActionConstant.DETAIL_ACTION;
//						}
					}
				} else {
					forwardURL = TdURLDefineList.ORDER_APPROVE_SERVLET_TD;
					forwardURL += "?act=" + AssetsActionConstant.EDIT_ACTION;
				}
				forwardURL += "&transType=" + dto.getTransType();
				forwardURL += "&transId=" + dto.getTransId();
			} else if (action.equals(AssetsActionConstant.DETAIL_ACTION)) { //��������ҳ��
				approveDAO.setDTOClassName(TdAssetsTransHeaderDTO.class.getName());
				approveDAO.setCalPattern(LINE_PATTERN);
				TdAssetsTransHeaderDTO headerDTO = (TdAssetsTransHeaderDTO)approveDAO.getDataByPrimaryKey();
                String accessSheet = approveDAO.getAccessSheet();//��������
                headerDTO.setAccessSheet(accessSheet);
                if (headerDTO == null) {
					message = getMessage(MsgKeyConstant.DATA_NOT_EXIST);
					message.setIsError(true);
					forwardURL = MessageConstant.MSG_PRC_SERVLET;
				} else {
					headerDTO.setServletConfig(servletConfig);
					TdAssetsTransLineDTO lineDTO = new TdAssetsTransLineDTO();
					lineDTO.setTransId(headerDTO.getTransId());
					lineDTO.setTransType(headerDTO.getTransType());
					TdAssetsTransLineDAO lineDAO = new TdAssetsTransLineDAO(user, lineDTO, conn);
					lineDAO.setDTOClassName(TdAssetsTransLineDTO.class.getName());
					DTOSet ds = (DTOSet) lineDAO.getDataByForeignKey("transId");
					req.setAttribute(AssetsWebAttributes.ORDER_LINE_DATA, ds);
					headerDTO.setCalPattern(LINE_PATTERN);
					req.setAttribute(AssetsWebAttributes.ORDER_HEAD_DATA,headerDTO);
					if (provinceCode.equals(AssetsDictConstant.PROVINCE_CODE_NM)) { //������������
						forwardURL = AssetsURLList.APPROVE_DETL_NM;
					} else {
						forwardURL = TdURLDefineList.APPROVE_DETL_PAGE_TD;
					}
				}
			} else {
				message = getMessage(MsgKeyConstant.INVALID_REQ);
				message.setIsError(true);
				forwardURL = MessageConstant.MSG_PRC_SERVLET;
			}
		} catch (PoolPassivateException ex) {
			ex.printLog();
			message = getMessage(MsgKeyConstant.POOL_PASSIVATE_ERROR);
			message.setIsError(true);
			forwardURL = MessageConstant.MSG_PRC_SERVLET;
		} catch (DTOException ex) {
			ex.printLog();
			message = getMessage(MsgKeyConstant.DTO_ERROR);
			message.setIsError(true);
			forwardURL = MessageConstant.MSG_PRC_SERVLET;
		} catch (QueryException ex) {
			ex.printLog();
			message = getMessage(MsgKeyConstant.QUERY_ERROR);
			message.setIsError(true);
			forwardURL = MessageConstant.MSG_PRC_SERVLET;
		} catch (UploadException ex) {
			ex.printLog();
			message = getMessage(AssetsMessageKeys.COMMON_ERROR);
			message.setIsError(true);
			forwardURL = MessageConstant.MSG_PRC_SERVLET;
		} finally {
			closeDBConnection(conn);
			setHandleMessage(req, message);
			ServletForwarder forwarder = new ServletForwarder(req, res);
			forwarder.forwardView(forwardURL);
		}
	}

	private TdAssetsTransHeaderDTO fillOptions(TdAssetsTransHeaderDTO dto,
												SfUserDTO user, Connection conn) throws
			QueryException {
		AssetsOptProducer optProducer = new AssetsOptProducer(user, conn);
		String deptOptions = optProducer.getUserAsssetsDeptOption(dto.
				getFromDept());
		dto.setFromDeptOption(deptOptions);
		String opt = optProducer.getAllOrganization(StrUtil.strToInt(dto.getToOrganizationId()));
		dto.setToCompanyOption(opt);
		String fromOrgId = dto.getFromOrganizationId();
		opt = optProducer.getAllOrganization(StrUtil.strToInt(fromOrgId));
		dto.setFromCompanyOption(opt);
		opt = optProducer.getBookTypeOption2(fromOrgId);
		dto.setBookTypeOption(opt);
		String transOption = optProducer.getFAContentOption(dto.
				getFaContentCode());
		dto.setFaContentOption(transOption);
		return dto;
	}


}