
/*
 * 
 */

package com.sino.soa.mis.eip.fi.fa.sb_fi_fa_pageinquiryassetlocationsrv;

import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.namespace.QName;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceFeature;
import javax.xml.ws.Service;

import com.sino.soa.common.SOAConstant;

/**
 * This class was generated by Apache CXF 2.1.4
 * Wed Sep 07 16:57:28 CST 2011
 * Generated source version: 2.1.4
 * date:2011-09-08
 * DES: ��ѯ�ʲ��ص�
 */


@WebServiceClient(name = "SB_FI_FA_PageInquiryAssetLocationSrv", 
                  wsdlLocation = "http://"+ SOAConstant.SERVER_NAME+":"+SOAConstant.SERVER_PORT+"/orabpel/fi/SB_FI_FA_PageInquiryAssetLocationSrv/1.0/SB_FI_FA_PageInquiryAssetLocationSrv?wsdl",
                  targetNamespace = "http://eip.zte.com/fi/SB_FI_FA_PageInquiryAssetLocationSrv") 
public class SBFIFAPageInquiryAssetLocationSrv_Service extends Service {

    public final static URL WSDL_LOCATION;
    public final static QName SERVICE = new QName("http://eip.zte.com/fi/SB_FI_FA_PageInquiryAssetLocationSrv", "SB_FI_FA_PageInquiryAssetLocationSrv");
    public final static QName SBFIFAPageInquiryAssetLocationSrvPort = new QName("http://eip.zte.com/fi/SB_FI_FA_PageInquiryAssetLocationSrv", "SB_FI_FA_PageInquiryAssetLocationSrvPort");
    static {
        URL url = null;
        try {
            url = new URL("http://"+ SOAConstant.SERVER_NAME+":"+SOAConstant.SERVER_PORT+"/orabpel/fi/SB_FI_FA_PageInquiryAssetLocationSrv/1.0/SB_FI_FA_PageInquiryAssetLocationSrv?wsdl");
        } catch (MalformedURLException e) {
            System.err.println("Can not initialize the default wsdl from "+"http://"+ SOAConstant.SERVER_NAME+":"+SOAConstant.SERVER_PORT+"/orabpel/fi/SB_FI_FA_PageInquiryAssetLocationSrv/1.0/SB_FI_FA_PageInquiryAssetLocationSrv?wsdl");
            // e.printStackTrace();
        }
        WSDL_LOCATION = url;
    }

    public SBFIFAPageInquiryAssetLocationSrv_Service(URL wsdlLocation) {
        super(wsdlLocation, SERVICE);
    }

    public SBFIFAPageInquiryAssetLocationSrv_Service(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    public SBFIFAPageInquiryAssetLocationSrv_Service() {
        super(WSDL_LOCATION, SERVICE);
    }

    /**
     * 
     * @return
     *     returns SBFIFAPageInquiryAssetLocationSrv
     */
    @WebEndpoint(name = "SB_FI_FA_PageInquiryAssetLocationSrvPort")
    public SBFIFAPageInquiryAssetLocationSrv getSBFIFAPageInquiryAssetLocationSrvPort() {
        return super.getPort(SBFIFAPageInquiryAssetLocationSrvPort, SBFIFAPageInquiryAssetLocationSrv.class);
    }

    /**
     * 
     * @param features
     *     A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns SBFIFAPageInquiryAssetLocationSrv
     */
    @WebEndpoint(name = "SB_FI_FA_PageInquiryAssetLocationSrvPort")
    public SBFIFAPageInquiryAssetLocationSrv getSBFIFAPageInquiryAssetLocationSrvPort(WebServiceFeature... features) {
        return super.getPort(SBFIFAPageInquiryAssetLocationSrvPort, SBFIFAPageInquiryAssetLocationSrv.class, features);
    }

}