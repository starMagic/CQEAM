package com.sino.soa.mis.eip.fi.gl.sb_fi_gl_inquiryouorganizationsrv;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.xml.bind.annotation.XmlSeeAlso;

/**
 * This class was generated by Apache CXF 2.1.4
 * Thu Sep 08 15:32:12 CST 2011
 * Generated source version: 2.1.4
 * 
 */
 
@WebService(targetNamespace = "http://eip.zte.com/common/gl/SB_FI_GL_InquiryOuOrganizationSrv", name = "SB_FI_GL_InquiryOuOrganizationSrv")
@XmlSeeAlso({com.sino.soa.mis.eip.fi.gl.sb_fi_gl_inquiryouorganizationsrv.inquiryouorganizationsrv.ObjectFactory.class, com.sino.soa.mis.eip.fi.gl.sb_fi_gl_inquiryouorganizationsrv.msgheader.ObjectFactory.class})
@SOAPBinding(parameterStyle = SOAPBinding.ParameterStyle.BARE)
public interface SBFIGLInquiryOuOrganizationSrv {

    @WebResult(name = "InquiryOuOrganizationSrvResponse", targetNamespace = "http://eip.zte.com/common/gl/InquiryOuOrganizationSrv", partName = "payload")
    @WebMethod(action = "process")
    public com.sino.soa.mis.eip.fi.gl.sb_fi_gl_inquiryouorganizationsrv.inquiryouorganizationsrv.InquiryOuOrganizationSrvResponse process(
        @WebParam(partName = "payload", name = "InquiryOuOrganizationSrvRequest", targetNamespace = "http://eip.zte.com/common/gl/InquiryOuOrganizationSrv")
        com.sino.soa.mis.eip.fi.gl.sb_fi_gl_inquiryouorganizationsrv.inquiryouorganizationsrv.InquiryOuOrganizationSrvRequest payload
    );
}