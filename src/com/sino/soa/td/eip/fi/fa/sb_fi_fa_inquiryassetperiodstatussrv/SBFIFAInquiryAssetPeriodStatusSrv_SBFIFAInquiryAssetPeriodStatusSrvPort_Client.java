
package com.sino.soa.td.eip.fi.fa.sb_fi_fa_inquiryassetperiodstatussrv;

/**
 * Please modify this class to meet your needs
 * This class is not complete
 */

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.namespace.QName;

/**
 * This class was generated by Apache CXF 2.1.4
 * Tue Sep 06 19:21:39 CST 2011
 * Generated source version: 2.1.4
 * date:2011-09-08
 * DSC:��ѯ�ʲ����״̬_TD(�ͻ���)
 */

public final class SBFIFAInquiryAssetPeriodStatusSrv_SBFIFAInquiryAssetPeriodStatusSrvPort_Client {

    private static final QName SERVICE_NAME = new QName("http://eip.zte.com/common/fa/SB_FI_FA_InquiryAssetPeriodStatusSrv", "SB_FI_FA_TDInquiryAssetPeriodStatusSrv");

    private SBFIFAInquiryAssetPeriodStatusSrv_SBFIFAInquiryAssetPeriodStatusSrvPort_Client() {
    }

    public static void main(String args[]) throws Exception {
        URL wsdlURL = SBFIFATDInquiryAssetPeriodStatusSrv.WSDL_LOCATION;
        if (args.length > 0) { 
            File wsdlFile = new File(args[0]);
            try {
                if (wsdlFile.exists()) {
                    wsdlURL = wsdlFile.toURI().toURL();
                } else {
                    wsdlURL = new URL(args[0]);
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
      
        SBFIFATDInquiryAssetPeriodStatusSrv ss = new SBFIFATDInquiryAssetPeriodStatusSrv(wsdlURL, SERVICE_NAME);
        SBFIFAInquiryAssetPeriodStatusSrv port = ss.getSBFIFAInquiryAssetPeriodStatusSrvPort();  
        
        {
        System.out.println("Invoking process...");
        com.sino.soa.td.eip.fi.fa.sb_fi_fa_inquiryassetperiodstatussrv.inquiryassetperiodstatussrv.InquiryAssetPeriodStatusSrvRequest _process_payload = null;
        com.sino.soa.td.eip.fi.fa.sb_fi_fa_inquiryassetperiodstatussrv.inquiryassetperiodstatussrv.InquiryAssetPeriodStatusSrvResponse _process__return = port.process(_process_payload);
        System.out.println("process.result=" + _process__return);


        }

        System.exit(0);
    }

}