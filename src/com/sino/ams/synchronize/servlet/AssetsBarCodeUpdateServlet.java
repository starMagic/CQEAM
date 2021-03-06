package com.sino.ams.synchronize.servlet;

import java.io.IOException;
import java.sql.Connection;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sino.ams.constant.URLDefineList;
import com.sino.ams.constant.WebAttrConstant;
import com.sino.ams.newasset.constant.AssetsMessageKeys;
import com.sino.ams.synchronize.dao.AssetsBarCodeUpdateDAO;
import com.sino.ams.synchronize.dto.EamSyschronizeDTO;
import com.sino.ams.synchronize.model.AssetsBarCodeUpdateModel;
import com.sino.ams.system.user.dto.SfUserDTO;
import com.sino.base.constant.message.MessageConstant;
import com.sino.base.constant.message.MsgKeyConstant;
import com.sino.base.constant.web.WebActionConstant;
import com.sino.base.db.conn.DBManager;
import com.sino.base.dto.Request2DTO;
import com.sino.base.exception.DTOException;
import com.sino.base.exception.PoolPassivateException;
import com.sino.base.exception.QueryException;
import com.sino.base.exception.StrException;
import com.sino.base.message.Message;
import com.sino.base.util.ArrUtil;
import com.sino.base.util.StrUtil;
import com.sino.base.web.CheckBoxProp;
import com.sino.base.web.ServletForwarder;
import com.sino.framework.dao.PageQueryDAO;
import com.sino.framework.security.bean.SessionUtil;
import com.sino.framework.security.dto.ServletConfigDTO;
import com.sino.framework.servlet.BaseServlet;
import com.sino.framework.sql.BaseSQLProducer;

/**
 * Created by sunny song.
 * User: sunny song
 * Date: 2008-3-13
 * Time: 2:11:00
 * To    资产条码变动同步
 */
public class AssetsBarCodeUpdateServlet extends BaseServlet {
    public void performTask(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
          String forwardURL = "";
          Message message = SessionUtil.getMessage(req);
          String action = req.getParameter("act");
          action = StrUtil.nullToString(action);
          Connection conn = null;
          try {
              SfUserDTO user = (SfUserDTO) SessionUtil.getUserAccount(req);
              EamSyschronizeDTO dtoParameter = null;  //声明DTO
              Request2DTO req2DTO = new Request2DTO();
              req2DTO.setDTOClassName(EamSyschronizeDTO.class.getName());
              dtoParameter = (EamSyschronizeDTO) req2DTO.getDTO(req);
              conn = getDBConnection(req);
              AssetsBarCodeUpdateDAO eamNewLocusDAO = new AssetsBarCodeUpdateDAO(user,dtoParameter,conn);

              ServletConfigDTO servletConfig = getServletConfig(req);
              if(action.equals("")) {
                  req.setAttribute(WebAttrConstant.SYSCHRONIZE_DTO, dtoParameter);
                  forwardURL = URLDefineList.ASSETS_BARCODE;
              } else if(action.equals(WebActionConstant.QUERY_ACTION)) {  //查询操作
                  BaseSQLProducer sqlProducer = new AssetsBarCodeUpdateModel(user, dtoParameter);
                  PageQueryDAO pageDAO = new PageQueryDAO(req, conn, sqlProducer);
                  pageDAO.setServletConfig(servletConfig);
                  CheckBoxProp checkProp = new CheckBoxProp("systemids");
                  checkProp.addDbField("systemids");
                  pageDAO.setWebCheckProp(checkProp);
                  pageDAO.setCalPattern(LINE_PATTERN);
                  pageDAO.produceWebData();
                  req.setAttribute(WebAttrConstant.SYSCHRONIZE_DTO, dtoParameter);
                  forwardURL = URLDefineList.ASSETS_BARCODE;
              }  else if (action.equals(WebActionConstant.SYSCHRONIZE_ACTION)) {  //同步操作
                 String[] systemIds = req.getParameterValues("systemids");
                 String systemId = ArrUtil.arrToSqlStr(systemIds);
                 eamNewLocusDAO.syschronizeLocus(systemId);
                 message = eamNewLocusDAO.getMessage();
                 forwardURL = "/servlet/com.sino.ams.synchronize.servlet.AssetsBarCodeUpdateServlet?act=QUERY_ACTION";

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
          }  catch (QueryException ex) {
              ex.printLog();
              message = getMessage(MsgKeyConstant.QUERY_ERROR);
              message.setIsError(true);
              forwardURL = MessageConstant.MSG_PRC_SERVLET;
          } catch (StrException ex) {
              ex.printLog();
              message = getMessage(AssetsMessageKeys.COMMON_ERROR);
              message.setIsError(true);
              forwardURL = MessageConstant.MSG_PRC_SERVLET;
          }  finally {
              DBManager.closeDBConnection(conn);
              setHandleMessage(req, message);
              ServletForwarder forwarder = new ServletForwarder(req, res);
              forwarder.forwardView(forwardURL);
              //根据实际情况修改页面跳转代码。
          }
    }

}
