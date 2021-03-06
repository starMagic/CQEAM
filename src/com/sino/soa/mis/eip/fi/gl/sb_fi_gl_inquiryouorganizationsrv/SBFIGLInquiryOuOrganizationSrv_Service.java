
/*
 * 
 */

package com.sino.soa.mis.eip.fi.gl.sb_fi_gl_inquiryouorganizationsrv;

import com.sino.soa.common.SOAConstant;

import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.namespace.QName;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceFeature;
import javax.xml.ws.Service;

/**
 * This class was generated by Apache CXF 2.1.4
 * Thu Sep 08 15:32:12 CST 2011
 * Generated source version: 2.1.4
 * 
 */


@WebServiceClient(name = "SB_FI_GL_InquiryOuOrganizationSrv", 
                  wsdlLocation = "http://"+ SOAConstant.SERVER_NAME+":"+SOAConstant.SERVER_PORT+"/orabpel/fi/SB_FI_GL_InquiryOuOrganizationSrv/3.0/SB_FI_GL_InquiryOuOrganizationSrv?wsdl",
                  targetNamespace = "http://eip.zte.com/common/gl/SB_FI_GL_InquiryOuOrganizationSrv") 
public class SBFIGLInquiryOuOrganizationSrv_Service extends Service {

    public final static URL WSDL_LOCATION;
    public final static QName SERVICE = new QName("http://eip.zte.com/common/gl/SB_FI_GL_InquiryOuOrganizationSrv", "SB_FI_GL_InquiryOuOrganizationSrv");
    public final static QName SBFIGLInquiryOuOrganizationSrvPort = new QName("http://eip.zte.com/common/gl/SB_FI_GL_InquiryOuOrganizationSrv", "SB_FI_GL_InquiryOuOrganizationSrvPort");
    static {
        URL url = null;
        try {
            url = new URL("http://"+ SOAConstant.SERVER_NAME+":"+SOAConstant.SERVER_PORT+"/orabpel/fi/SB_FI_GL_InquiryOuOrganizationSrv/3.0/SB_FI_GL_InquiryOuOrganizationSrv?wsdl");
        } catch (MalformedURLException e) {
            System.err.println("Can not initialize the default wsdl from http://"+ SOAConstant.SERVER_NAME+":"+SOAConstant.SERVER_PORT+"/orabpel/fi/SB_FI_GL_InquiryOuOrganizationSrv/3.0/SB_FI_GL_InquiryOuOrganizationSrv?wsdl");
            // e.printStackTrace();
        }
        WSDL_LOCATION = url;
    }

    public SBFIGLInquiryOuOrganizationSrv_Service(URL wsdlLocation) {
        super(wsdlLocation, SERVICE);
    }

    public SBFIGLInquiryOuOrganizationSrv_Service(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    public SBFIGLInquiryOuOrganizationSrv_Service() {
        super(WSDL_LOCATION, SERVICE);
    }

    /**
     * 
     * @return
     *     returns SBFIGLInquiryOuOrganizationSrv
     */
    @WebEndpoint(name = "SB_FI_GL_InquiryOuOrganizationSrvPort")
    public SBFIGLInquiryOuOrganizationSrv getSBFIGLInquiryOuOrganizationSrvPort() {
        return super.getPort(SBFIGLInquiryOuOrganizationSrvPort, SBFIGLInquiryOuOrganizationSrv.class);
    }

    /**
     * 
     * @param features
     *     A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns SBFIGLInquiryOuOrganizationSrv
     */
    @WebEndpoint(name = "SB_FI_GL_InquiryOuOrganizationSrvPort")
    public SBFIGLInquiryOuOrganizationSrv getSBFIGLInquiryOuOrganizationSrvPort(WebServiceFeature... features) {
        return super.getPort(SBFIGLInquiryOuOrganizationSrvPort, SBFIGLInquiryOuOrganizationSrv.class, features);
    }

}
