package com.sino.soa.mis.eip.fi.fa.sb_fi_fa_transfaassetinfosrv;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.xml.bind.annotation.XmlSeeAlso;

/**
 * This class was generated by Apache CXF 2.2.6
 * Thu Oct 20 09:47:45 CST 2011
 * Generated source version: 2.2.6
 * 
 */
 
@WebService(targetNamespace = "http://eip.zte.com/fi/SB_FI_FA_TransFaAssetInfoSrv", name = "SB_FI_FA_TransFaAssetInfoSrv")
@XmlSeeAlso({ObjectFactory.class})
@SOAPBinding(parameterStyle = SOAPBinding.ParameterStyle.BARE)
public interface SBFIFATransFaAssetInfoSrv {

    @WebResult(name = "SB_FI_FA_TransFaAssetInfoSrvProcessResponse", targetNamespace = "http://eip.zte.com/fi/SB_FI_FA_TransFaAssetInfoSrv", partName = "payload")
    @WebMethod(action = "process")
    public SBFIFATransFaAssetInfoSrvProcessResponse process(
        @WebParam(partName = "payload", name = "SB_FI_FA_TransFaAssetInfoSrvProcessRequest", targetNamespace = "http://eip.zte.com/fi/SB_FI_FA_TransFaAssetInfoSrv")
        SBFIFATransFaAssetInfoSrvProcessRequest payload
    );
}