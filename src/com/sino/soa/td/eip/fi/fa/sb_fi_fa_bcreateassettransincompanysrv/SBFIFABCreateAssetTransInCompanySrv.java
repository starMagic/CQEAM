package com.sino.soa.td.eip.fi.fa.sb_fi_fa_bcreateassettransincompanysrv;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.xml.bind.annotation.XmlSeeAlso;

/**
 * This class was generated by Apache CXF 2.1.4
 * Wed Sep 14 09:43:41 CST 2011
 * Generated source version: 2.1.4
 * 
 */
 
@WebService(targetNamespace = "http://eip.zte.com/SB_FI_FA_BCreateAssetTransInCompanySrv", name = "SB_FI_FA_BCreateAssetTransInCompanySrv")
@XmlSeeAlso({ObjectFactory.class})
@SOAPBinding(parameterStyle = SOAPBinding.ParameterStyle.BARE)
public interface SBFIFABCreateAssetTransInCompanySrv {

    @WebResult(name = "BCreateAssetTransInCompanySrvResponse", targetNamespace = "http://eip.zte.com/SB_FI_FA_BCreateAssetTransInCompanySrv", partName = "payload")
    @WebMethod(action = "process")
    public BCreateAssetTransInCompanySrvResponse process(
        @WebParam(partName = "payload", name = "BCreateAssetTransInCompanySrvRequest", targetNamespace = "http://eip.zte.com/SB_FI_FA_BCreateAssetTransInCompanySrv")
        BCreateAssetTransInCompanySrvRequest payload
    );
}
