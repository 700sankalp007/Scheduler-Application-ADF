package sc.common.view.util;

import java.net.URL;

import javax.xml.namespace.QName;

import sc.common.biReport.PublicReportService;
import sc.common.biReport.PublicReportServiceService;
import sc.common.essJob.ErpIntegrationService;
import sc.common.essJob.ErpIntegrationService_Service;

import weblogic.wsee.jws.jaxws.owsm.SecurityPolicyFeature;

public class ServiceBean {
    private final static QName PUBLICREPORTSERVICE_QNAME= new QName("http://xmlns.oracle.com/oxp/service/PublicReportService", "PublicReportServiceService");
    private static PublicReportServiceService publicReportServiceService;
    private static PublicReportService publicReportServicePort;
    private static ErpIntegrationService erpIntegrationPort;
    private static ErpIntegrationService_Service erpIntegrationService;
    
    public ServiceBean() {
        super();
    }
    
    
    /**To get the port for the Public report service to call the BI publisher report
     * @return      Public Report Service Port
     * @throws Exception
     */
    public static PublicReportService getPublicReportServicePort(String instanceURL) throws Exception {
        try {
            if(publicReportServicePort  != null){
                return publicReportServicePort;
            } else {
                URL url = new URL(instanceURL+Constants.BI_WSDL);
                publicReportServiceService = new PublicReportServiceService(url,PUBLICREPORTSERVICE_QNAME);       
                publicReportServicePort = publicReportServiceService.getPublicReportService();
                return publicReportServicePort;
            }            
        } catch(Exception e){
            e.printStackTrace();
            throw e;
        }
    }
    
    public static ErpIntegrationService getIntegrationServicePort(String finUrl) throws Exception {
            try{
                if(erpIntegrationPort  != null){
                    return erpIntegrationPort;
                } else{
                   SecurityPolicyFeature[] m_securityFeature = new SecurityPolicyFeature[] { new SecurityPolicyFeature("oracle/wss_username_token_over_ssl_client_policy") };
//                   URL url = new URL(finUrl + Constants.ERP_INTEGRATION_WSDL);
                   URL url = new java.io.File(finUrl).toURI().toURL();
                   erpIntegrationService = new ErpIntegrationService_Service(url,new QName("http://xmlns.oracle.com/apps/financials/commonModules/shared/model/erpIntegrationService/", "ErpIntegrationService"));       
                   erpIntegrationPort = erpIntegrationService.getErpIntegrationServiceSoapHttpPort(m_securityFeature);
                   return erpIntegrationPort;
               }
            } catch(Exception e){
                e.printStackTrace();
                throw e;
            }
        }
}
