
package sc.common.essJob;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each
 * Java content interface and Java element interface
 * generated in the sc.common.essJob package.
 * <p>An ObjectFactory allows you to programatically
 * construct new instances of the Java representation
 * for XML content. The Java representation of XML
 * content can consist of schema derived interfaces
 * and classes representing the binding of schema
 * type definitions, element declarations and model
 * groups.  Factory methods for each of these are
 * provided in this class.
 *
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _ExportBulkDataCallbackURL_QNAME = new QName("http://xmlns.oracle.com/apps/financials/commonModules/shared/model/erpIntegrationService/types/", "callbackURL");
    private final static QName _ExportBulkDataNotificationCode_QNAME = new QName("http://xmlns.oracle.com/apps/financials/commonModules/shared/model/erpIntegrationService/types/", "notificationCode");
    private final static QName _ExportBulkDataJobOptions_QNAME = new QName("http://xmlns.oracle.com/apps/financials/commonModules/shared/model/erpIntegrationService/types/", "jobOptions");
    private final static QName _ExportBulkDataParameterList_QNAME = new QName("http://xmlns.oracle.com/apps/financials/commonModules/shared/model/erpIntegrationService/types/", "parameterList");
    private final static QName _EssJob_QNAME = new QName("http://xmlns.oracle.com/apps/financials/commonModules/shared/model/erpIntegrationService/", "essJob");
    private final static QName _DocumentDetail_QNAME = new QName("http://xmlns.oracle.com/apps/financials/commonModules/shared/model/erpIntegrationService/", "documentDetail");
    private final static QName _Fault_QNAME = new QName("http://xmlns.oracle.com/oracleas/schema/oracle-fault-11_0", "Fault");
    private final static QName _DocumentDetails_QNAME = new QName("http://xmlns.oracle.com/apps/financials/commonModules/shared/model/erpIntegrationService/", "documentDetails");
    private final static QName _FindCriteria_QNAME = new QName("http://xmlns.oracle.com/adf/svc/types/", "findCriteria");
    private final static QName _Types_QNAME = new QName("commonj.sdo", "types");
    private final static QName _Type_QNAME = new QName("commonj.sdo", "type");
    private final static QName _FindControl_QNAME = new QName("http://xmlns.oracle.com/adf/svc/types/", "findControl");
    private final static QName _DataObject_QNAME = new QName("commonj.sdo", "dataObject");
    private final static QName _ProcessControl_QNAME = new QName("http://xmlns.oracle.com/adf/svc/types/", "processControl");
    private final static QName _ServiceErrorMessage_QNAME = new QName("http://xmlns.oracle.com/adf/svc/errors/", "ServiceErrorMessage");
    private final static QName _Datagraph_QNAME = new QName("commonj.sdo", "datagraph");
    private final static QName _DocumentDetailDocumentAccount_QNAME = new QName("http://xmlns.oracle.com/apps/financials/commonModules/shared/model/erpIntegrationService/", "DocumentAccount");
    private final static QName _DocumentDetailContentType_QNAME = new QName("http://xmlns.oracle.com/apps/financials/commonModules/shared/model/erpIntegrationService/", "ContentType");
    private final static QName _DocumentDetailContent_QNAME = new QName("http://xmlns.oracle.com/apps/financials/commonModules/shared/model/erpIntegrationService/", "Content");
    private final static QName _DocumentDetailDocumentId_QNAME = new QName("http://xmlns.oracle.com/apps/financials/commonModules/shared/model/erpIntegrationService/", "DocumentId");
    private final static QName _DocumentDetailDocumentName_QNAME = new QName("http://xmlns.oracle.com/apps/financials/commonModules/shared/model/erpIntegrationService/", "DocumentName");
    private final static QName _DocumentDetailDocumentAuthor_QNAME = new QName("http://xmlns.oracle.com/apps/financials/commonModules/shared/model/erpIntegrationService/", "DocumentAuthor");
    private final static QName _DocumentDetailDocumentSecurityGroup_QNAME = new QName("http://xmlns.oracle.com/apps/financials/commonModules/shared/model/erpIntegrationService/", "DocumentSecurityGroup");
    private final static QName _DocumentDetailDocumentTitle_QNAME = new QName("http://xmlns.oracle.com/apps/financials/commonModules/shared/model/erpIntegrationService/", "DocumentTitle");
    private final static QName _DocumentDetailFileName_QNAME = new QName("http://xmlns.oracle.com/apps/financials/commonModules/shared/model/erpIntegrationService/", "FileName");
    private final static QName _LoadAndImportDataAsyncInterfaceDetails_QNAME = new QName("http://xmlns.oracle.com/apps/financials/commonModules/shared/model/erpIntegrationService/types/", "interfaceDetails");
    private final static QName _EssJobParameterList_QNAME = new QName("http://xmlns.oracle.com/apps/financials/commonModules/shared/model/erpIntegrationService/", "ParameterList");
    private final static QName _EssJobJobName_QNAME = new QName("http://xmlns.oracle.com/apps/financials/commonModules/shared/model/erpIntegrationService/", "JobName");
    private final static QName _EssJobJobRequestId_QNAME = new QName("http://xmlns.oracle.com/apps/financials/commonModules/shared/model/erpIntegrationService/", "JobRequestId");
    private final static QName _ImportBulkDataDocument_QNAME = new QName("http://xmlns.oracle.com/apps/financials/commonModules/shared/model/erpIntegrationService/types/", "document");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: sc.common.essJob
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link ServiceErrorMessage}
     *
     */
    public ServiceErrorMessage createServiceErrorMessage() {
        return new ServiceErrorMessage();
    }

    /**
     * Create an instance of {@link ServiceMessage}
     *
     */
    public ServiceMessage createServiceMessage() {
        return new ServiceMessage();
    }

    /**
     * Create an instance of {@link ServiceDMLErrorMessage}
     *
     */
    public ServiceDMLErrorMessage createServiceDMLErrorMessage() {
        return new ServiceDMLErrorMessage();
    }

    /**
     * Create an instance of {@link ServiceRowValErrorMessage}
     *
     */
    public ServiceRowValErrorMessage createServiceRowValErrorMessage() {
        return new ServiceRowValErrorMessage();
    }

    /**
     * Create an instance of {@link ServiceAttrValErrorMessage}
     *
     */
    public ServiceAttrValErrorMessage createServiceAttrValErrorMessage() {
        return new ServiceAttrValErrorMessage();
    }

    /**
     * Create an instance of {@link JavaInfo}
     *
     */
    public JavaInfo createJavaInfo() {
        return new JavaInfo();
    }

    /**
     * Create an instance of {@link DataGraphType}
     *
     */
    public DataGraphType createDataGraphType() {
        return new DataGraphType();
    }

    /**
     * Create an instance of {@link Type}
     *
     */
    public Type createType() {
        return new Type();
    }

    /**
     * Create an instance of {@link Types}
     *
     */
    public Types createTypes() {
        return new Types();
    }

    /**
     * Create an instance of {@link ModelsType}
     *
     */
    public ModelsType createModelsType() {
        return new ModelsType();
    }

    /**
     * Create an instance of {@link Property}
     *
     */
    public Property createProperty() {
        return new Property();
    }

    /**
     * Create an instance of {@link XSDType}
     *
     */
    public XSDType createXSDType() {
        return new XSDType();
    }

    /**
     * Create an instance of {@link ChangeSummaryType}
     *
     */
    public ChangeSummaryType createChangeSummaryType() {
        return new ChangeSummaryType();
    }

    /**
     * Create an instance of {@link FaultType}
     *
     */
    public FaultType createFaultType() {
        return new FaultType();
    }

    /**
     * Create an instance of {@link Detail}
     *
     */
    public Detail createDetail() {
        return new Detail();
    }

    /**
     * Create an instance of {@link GetDocumentForDocumentIdAsync}
     *
     */
    public GetDocumentForDocumentIdAsync createGetDocumentForDocumentIdAsync() {
        return new GetDocumentForDocumentIdAsync();
    }

    /**
     * Create an instance of {@link DownloadExportOutputResponse}
     *
     */
    public DownloadExportOutputResponse createDownloadExportOutputResponse() {
        return new DownloadExportOutputResponse();
    }

    /**
     * Create an instance of {@link DocumentDetails}
     *
     */
    public DocumentDetails createDocumentDetails() {
        return new DocumentDetails();
    }

    /**
     * Create an instance of {@link SubmitESSJobRequest}
     *
     */
    public SubmitESSJobRequest createSubmitESSJobRequest() {
        return new SubmitESSJobRequest();
    }

    /**
     * Create an instance of {@link ImportBulkDataResponse}
     *
     */
    public ImportBulkDataResponse createImportBulkDataResponse() {
        return new ImportBulkDataResponse();
    }

    /**
     * Create an instance of {@link SubmitESSJobRequestAsync}
     *
     */
    public SubmitESSJobRequestAsync createSubmitESSJobRequestAsync() {
        return new SubmitESSJobRequestAsync();
    }

    /**
     * Create an instance of {@link GetESSJobStatusResponse}
     *
     */
    public GetESSJobStatusResponse createGetESSJobStatusResponse() {
        return new GetESSJobStatusResponse();
    }

    /**
     * Create an instance of {@link SubmitESSJobRequestResponse}
     *
     */
    public SubmitESSJobRequestResponse createSubmitESSJobRequestResponse() {
        return new SubmitESSJobRequestResponse();
    }

    /**
     * Create an instance of {@link GetESSJobStatusAsyncResponse}
     *
     */
    public GetESSJobStatusAsyncResponse createGetESSJobStatusAsyncResponse() {
        return new GetESSJobStatusAsyncResponse();
    }

    /**
     * Create an instance of {@link DownloadESSJobExecutionDetailsAsyncResponse}
     *
     */
    public DownloadESSJobExecutionDetailsAsyncResponse createDownloadESSJobExecutionDetailsAsyncResponse() {
        return new DownloadESSJobExecutionDetailsAsyncResponse();
    }

    /**
     * Create an instance of {@link AppendFileCommentResponse}
     *
     */
    public AppendFileCommentResponse createAppendFileCommentResponse() {
        return new AppendFileCommentResponse();
    }

    /**
     * Create an instance of {@link GetESSJobStatusAsync}
     *
     */
    public GetESSJobStatusAsync createGetESSJobStatusAsync() {
        return new GetESSJobStatusAsync();
    }

    /**
     * Create an instance of {@link DownloadESSJobExecutionDetailsResponse}
     *
     */
    public DownloadESSJobExecutionDetailsResponse createDownloadESSJobExecutionDetailsResponse() {
        return new DownloadESSJobExecutionDetailsResponse();
    }

    /**
     * Create an instance of {@link GetDocumentsForFilePrefixAsyncResponse}
     *
     */
    public GetDocumentsForFilePrefixAsyncResponse createGetDocumentsForFilePrefixAsyncResponse() {
        return new GetDocumentsForFilePrefixAsyncResponse();
    }

    /**
     * Create an instance of {@link GetEntityListAsync}
     *
     */
    public GetEntityListAsync createGetEntityListAsync() {
        return new GetEntityListAsync();
    }

    /**
     * Create an instance of {@link UploadFileToUcmAsync}
     *
     */
    public UploadFileToUcmAsync createUploadFileToUcmAsync() {
        return new UploadFileToUcmAsync();
    }

    /**
     * Create an instance of {@link LoadAndImportData}
     *
     */
    public LoadAndImportData createLoadAndImportData() {
        return new LoadAndImportData();
    }

    /**
     * Create an instance of {@link EssJob}
     *
     */
    public EssJob createEssJob() {
        return new EssJob();
    }

    /**
     * Create an instance of {@link AppendFileCommentAsyncResponse}
     *
     */
    public AppendFileCommentAsyncResponse createAppendFileCommentAsyncResponse() {
        return new AppendFileCommentAsyncResponse();
    }

    /**
     * Create an instance of {@link GetESSJobStatus}
     *
     */
    public GetESSJobStatus createGetESSJobStatus() {
        return new GetESSJobStatus();
    }

    /**
     * Create an instance of {@link GetDfltObjAttrHintsAsync}
     *
     */
    public GetDfltObjAttrHintsAsync createGetDfltObjAttrHintsAsync() {
        return new GetDfltObjAttrHintsAsync();
    }

    /**
     * Create an instance of {@link ImportBulkData}
     *
     */
    public ImportBulkData createImportBulkData() {
        return new ImportBulkData();
    }

    /**
     * Create an instance of {@link DocumentDetail}
     *
     */
    public DocumentDetail createDocumentDetail() {
        return new DocumentDetail();
    }

    /**
     * Create an instance of {@link DownloadESSJobExecutionDetailsAsync}
     *
     */
    public DownloadESSJobExecutionDetailsAsync createDownloadESSJobExecutionDetailsAsync() {
        return new DownloadESSJobExecutionDetailsAsync();
    }

    /**
     * Create an instance of {@link SubmitJobWithOutputAsync}
     *
     */
    public SubmitJobWithOutputAsync createSubmitJobWithOutputAsync() {
        return new SubmitJobWithOutputAsync();
    }

    /**
     * Create an instance of {@link UploadFileToUcmResponse}
     *
     */
    public UploadFileToUcmResponse createUploadFileToUcmResponse() {
        return new UploadFileToUcmResponse();
    }

    /**
     * Create an instance of {@link GetEntityListResponse}
     *
     */
    public GetEntityListResponse createGetEntityListResponse() {
        return new GetEntityListResponse();
    }

    /**
     * Create an instance of {@link ServiceViewInfo}
     *
     */
    public ServiceViewInfo createServiceViewInfo() {
        return new ServiceViewInfo();
    }

    /**
     * Create an instance of {@link LoadAndImportDataAsyncResponse}
     *
     */
    public LoadAndImportDataAsyncResponse createLoadAndImportDataAsyncResponse() {
        return new LoadAndImportDataAsyncResponse();
    }

    /**
     * Create an instance of {@link GetServiceLastUpdateTimeAsyncResponse}
     *
     */
    public GetServiceLastUpdateTimeAsyncResponse createGetServiceLastUpdateTimeAsyncResponse() {
        return new GetServiceLastUpdateTimeAsyncResponse();
    }

    /**
     * Create an instance of {@link AppendFileComment}
     *
     */
    public AppendFileComment createAppendFileComment() {
        return new AppendFileComment();
    }

    /**
     * Create an instance of {@link GetDfltObjAttrHintsResponse}
     *
     */
    public GetDfltObjAttrHintsResponse createGetDfltObjAttrHintsResponse() {
        return new GetDfltObjAttrHintsResponse();
    }

    /**
     * Create an instance of {@link ObjAttrHints}
     *
     */
    public ObjAttrHints createObjAttrHints() {
        return new ObjAttrHints();
    }

    /**
     * Create an instance of {@link ExportBulkData}
     *
     */
    public ExportBulkData createExportBulkData() {
        return new ExportBulkData();
    }

    /**
     * Create an instance of {@link GetDocumentForDocumentIdResponse}
     *
     */
    public GetDocumentForDocumentIdResponse createGetDocumentForDocumentIdResponse() {
        return new GetDocumentForDocumentIdResponse();
    }

    /**
     * Create an instance of {@link SubmitESSJobRequestAsyncResponse}
     *
     */
    public SubmitESSJobRequestAsyncResponse createSubmitESSJobRequestAsyncResponse() {
        return new SubmitESSJobRequestAsyncResponse();
    }

    /**
     * Create an instance of {@link GetServiceLastUpdateTime}
     *
     */
    public GetServiceLastUpdateTime createGetServiceLastUpdateTime() {
        return new GetServiceLastUpdateTime();
    }

    /**
     * Create an instance of {@link SubmitJobWithOutputResponse}
     *
     */
    public SubmitJobWithOutputResponse createSubmitJobWithOutputResponse() {
        return new SubmitJobWithOutputResponse();
    }

    /**
     * Create an instance of {@link ExportBulkDataAsyncResponse}
     *
     */
    public ExportBulkDataAsyncResponse createExportBulkDataAsyncResponse() {
        return new ExportBulkDataAsyncResponse();
    }

    /**
     * Create an instance of {@link UploadFileToUcm}
     *
     */
    public UploadFileToUcm createUploadFileToUcm() {
        return new UploadFileToUcm();
    }

    /**
     * Create an instance of {@link GetServiceLastUpdateTimeAsync}
     *
     */
    public GetServiceLastUpdateTimeAsync createGetServiceLastUpdateTimeAsync() {
        return new GetServiceLastUpdateTimeAsync();
    }

    /**
     * Create an instance of {@link GetDocumentsForFilePrefixAsync}
     *
     */
    public GetDocumentsForFilePrefixAsync createGetDocumentsForFilePrefixAsync() {
        return new GetDocumentsForFilePrefixAsync();
    }

    /**
     * Create an instance of {@link ExtractAndPurge}
     *
     */
    public ExtractAndPurge createExtractAndPurge() {
        return new ExtractAndPurge();
    }

    /**
     * Create an instance of {@link GetDocumentsForFilePrefixResponse}
     *
     */
    public GetDocumentsForFilePrefixResponse createGetDocumentsForFilePrefixResponse() {
        return new GetDocumentsForFilePrefixResponse();
    }

    /**
     * Create an instance of {@link GetDocumentsForFilePrefix}
     *
     */
    public GetDocumentsForFilePrefix createGetDocumentsForFilePrefix() {
        return new GetDocumentsForFilePrefix();
    }

    /**
     * Create an instance of {@link GetEntityListAsyncResponse}
     *
     */
    public GetEntityListAsyncResponse createGetEntityListAsyncResponse() {
        return new GetEntityListAsyncResponse();
    }

    /**
     * Create an instance of {@link LoadAndImportDataAsync}
     *
     */
    public LoadAndImportDataAsync createLoadAndImportDataAsync() {
        return new LoadAndImportDataAsync();
    }

    /**
     * Create an instance of {@link AppendFileCommentAsync}
     *
     */
    public AppendFileCommentAsync createAppendFileCommentAsync() {
        return new AppendFileCommentAsync();
    }

    /**
     * Create an instance of {@link SubmitJobWithOutput}
     *
     */
    public SubmitJobWithOutput createSubmitJobWithOutput() {
        return new SubmitJobWithOutput();
    }

    /**
     * Create an instance of {@link GetDocumentForDocumentIdAsyncResponse}
     *
     */
    public GetDocumentForDocumentIdAsyncResponse createGetDocumentForDocumentIdAsyncResponse() {
        return new GetDocumentForDocumentIdAsyncResponse();
    }

    /**
     * Create an instance of {@link ImportBulkDataAsyncResponse}
     *
     */
    public ImportBulkDataAsyncResponse createImportBulkDataAsyncResponse() {
        return new ImportBulkDataAsyncResponse();
    }

    /**
     * Create an instance of {@link GetDfltObjAttrHints}
     *
     */
    public GetDfltObjAttrHints createGetDfltObjAttrHints() {
        return new GetDfltObjAttrHints();
    }

    /**
     * Create an instance of {@link GetDfltObjAttrHintsAsyncResponse}
     *
     */
    public GetDfltObjAttrHintsAsyncResponse createGetDfltObjAttrHintsAsyncResponse() {
        return new GetDfltObjAttrHintsAsyncResponse();
    }

    /**
     * Create an instance of {@link GetDocumentForDocumentId}
     *
     */
    public GetDocumentForDocumentId createGetDocumentForDocumentId() {
        return new GetDocumentForDocumentId();
    }

    /**
     * Create an instance of {@link GetDocumentIdsForFilePrefixAsyncResponse}
     *
     */
    public GetDocumentIdsForFilePrefixAsyncResponse createGetDocumentIdsForFilePrefixAsyncResponse() {
        return new GetDocumentIdsForFilePrefixAsyncResponse();
    }

    /**
     * Create an instance of {@link GetDocumentIdsForFilePrefixAsync}
     *
     */
    public GetDocumentIdsForFilePrefixAsync createGetDocumentIdsForFilePrefixAsync() {
        return new GetDocumentIdsForFilePrefixAsync();
    }

    /**
     * Create an instance of {@link SubmitJobWithOutputAsyncResponse}
     *
     */
    public SubmitJobWithOutputAsyncResponse createSubmitJobWithOutputAsyncResponse() {
        return new SubmitJobWithOutputAsyncResponse();
    }

    /**
     * Create an instance of {@link LoadAndImportDataResponse}
     *
     */
    public LoadAndImportDataResponse createLoadAndImportDataResponse() {
        return new LoadAndImportDataResponse();
    }

    /**
     * Create an instance of {@link DownloadExportOutputAsyncResponse}
     *
     */
    public DownloadExportOutputAsyncResponse createDownloadExportOutputAsyncResponse() {
        return new DownloadExportOutputAsyncResponse();
    }

    /**
     * Create an instance of {@link UploadFileToUcmAsyncResponse}
     *
     */
    public UploadFileToUcmAsyncResponse createUploadFileToUcmAsyncResponse() {
        return new UploadFileToUcmAsyncResponse();
    }

    /**
     * Create an instance of {@link ExtractAndPurgeAsyncResponse}
     *
     */
    public ExtractAndPurgeAsyncResponse createExtractAndPurgeAsyncResponse() {
        return new ExtractAndPurgeAsyncResponse();
    }

    /**
     * Create an instance of {@link ExtractAndPurgeResponse}
     *
     */
    public ExtractAndPurgeResponse createExtractAndPurgeResponse() {
        return new ExtractAndPurgeResponse();
    }

    /**
     * Create an instance of {@link GetEntityList}
     *
     */
    public GetEntityList createGetEntityList() {
        return new GetEntityList();
    }

    /**
     * Create an instance of {@link ExtractAndPurgeAsync}
     *
     */
    public ExtractAndPurgeAsync createExtractAndPurgeAsync() {
        return new ExtractAndPurgeAsync();
    }

    /**
     * Create an instance of {@link GetDocumentIdsForFilePrefix}
     *
     */
    public GetDocumentIdsForFilePrefix createGetDocumentIdsForFilePrefix() {
        return new GetDocumentIdsForFilePrefix();
    }

    /**
     * Create an instance of {@link ExportBulkDataResponse}
     *
     */
    public ExportBulkDataResponse createExportBulkDataResponse() {
        return new ExportBulkDataResponse();
    }

    /**
     * Create an instance of {@link DownloadExportOutputAsync}
     *
     */
    public DownloadExportOutputAsync createDownloadExportOutputAsync() {
        return new DownloadExportOutputAsync();
    }

    /**
     * Create an instance of {@link DownloadExportOutput}
     *
     */
    public DownloadExportOutput createDownloadExportOutput() {
        return new DownloadExportOutput();
    }

    /**
     * Create an instance of {@link GetDocumentIdsForFilePrefixResponse}
     *
     */
    public GetDocumentIdsForFilePrefixResponse createGetDocumentIdsForFilePrefixResponse() {
        return new GetDocumentIdsForFilePrefixResponse();
    }

    /**
     * Create an instance of {@link ImportBulkDataAsync}
     *
     */
    public ImportBulkDataAsync createImportBulkDataAsync() {
        return new ImportBulkDataAsync();
    }

    /**
     * Create an instance of {@link ExportBulkDataAsync}
     *
     */
    public ExportBulkDataAsync createExportBulkDataAsync() {
        return new ExportBulkDataAsync();
    }

    /**
     * Create an instance of {@link GetServiceLastUpdateTimeResponse}
     *
     */
    public GetServiceLastUpdateTimeResponse createGetServiceLastUpdateTimeResponse() {
        return new GetServiceLastUpdateTimeResponse();
    }

    /**
     * Create an instance of {@link DownloadESSJobExecutionDetails}
     *
     */
    public DownloadESSJobExecutionDetails createDownloadESSJobExecutionDetails() {
        return new DownloadESSJobExecutionDetails();
    }

    /**
     * Create an instance of {@link FindControl}
     *
     */
    public FindControl createFindControl() {
        return new FindControl();
    }

    /**
     * Create an instance of {@link ProcessControl}
     *
     */
    public ProcessControl createProcessControl() {
        return new ProcessControl();
    }

    /**
     * Create an instance of {@link FindCriteria}
     *
     */
    public FindCriteria createFindCriteria() {
        return new FindCriteria();
    }

    /**
     * Create an instance of {@link ChildFindCriteria}
     *
     */
    public ChildFindCriteria createChildFindCriteria() {
        return new ChildFindCriteria();
    }

    /**
     * Create an instance of {@link BigDecimalResult}
     *
     */
    public BigDecimalResult createBigDecimalResult() {
        return new BigDecimalResult();
    }

    /**
     * Create an instance of {@link SortOrder}
     *
     */
    public SortOrder createSortOrder() {
        return new SortOrder();
    }

    /**
     * Create an instance of {@link SortAttribute}
     *
     */
    public SortAttribute createSortAttribute() {
        return new SortAttribute();
    }

    /**
     * Create an instance of {@link AmountType}
     *
     */
    public AmountType createAmountType() {
        return new AmountType();
    }

    /**
     * Create an instance of {@link DataObjectResult}
     *
     */
    public DataObjectResult createDataObjectResult() {
        return new DataObjectResult();
    }

    /**
     * Create an instance of {@link BooleanResult}
     *
     */
    public BooleanResult createBooleanResult() {
        return new BooleanResult();
    }

    /**
     * Create an instance of {@link MeasureType}
     *
     */
    public MeasureType createMeasureType() {
        return new MeasureType();
    }

    /**
     * Create an instance of {@link MethodResult}
     *
     */
    public MethodResult createMethodResult() {
        return new MethodResult();
    }

    /**
     * Create an instance of {@link TimeResult}
     *
     */
    public TimeResult createTimeResult() {
        return new TimeResult();
    }

    /**
     * Create an instance of {@link DateResult}
     *
     */
    public DateResult createDateResult() {
        return new DateResult();
    }

    /**
     * Create an instance of {@link ViewCriteriaRow}
     *
     */
    public ViewCriteriaRow createViewCriteriaRow() {
        return new ViewCriteriaRow();
    }

    /**
     * Create an instance of {@link BytesResult}
     *
     */
    public BytesResult createBytesResult() {
        return new BytesResult();
    }

    /**
     * Create an instance of {@link CtrlHint}
     *
     */
    public CtrlHint createCtrlHint() {
        return new CtrlHint();
    }

    /**
     * Create an instance of {@link DoubleResult}
     *
     */
    public DoubleResult createDoubleResult() {
        return new DoubleResult();
    }

    /**
     * Create an instance of {@link ViewCriteriaItem}
     *
     */
    public ViewCriteriaItem createViewCriteriaItem() {
        return new ViewCriteriaItem();
    }

    /**
     * Create an instance of {@link ShortResult}
     *
     */
    public ShortResult createShortResult() {
        return new ShortResult();
    }

    /**
     * Create an instance of {@link BigIntegerResult}
     *
     */
    public BigIntegerResult createBigIntegerResult() {
        return new BigIntegerResult();
    }

    /**
     * Create an instance of {@link DataHandlerResult}
     *
     */
    public DataHandlerResult createDataHandlerResult() {
        return new DataHandlerResult();
    }

    /**
     * Create an instance of {@link IntegerResult}
     *
     */
    public IntegerResult createIntegerResult() {
        return new IntegerResult();
    }

    /**
     * Create an instance of {@link FloatResult}
     *
     */
    public FloatResult createFloatResult() {
        return new FloatResult();
    }

    /**
     * Create an instance of {@link StringResult}
     *
     */
    public StringResult createStringResult() {
        return new StringResult();
    }

    /**
     * Create an instance of {@link ByteResult}
     *
     */
    public ByteResult createByteResult() {
        return new ByteResult();
    }

    /**
     * Create an instance of {@link LongResult}
     *
     */
    public LongResult createLongResult() {
        return new LongResult();
    }

    /**
     * Create an instance of {@link AttrCtrlHints}
     *
     */
    public AttrCtrlHints createAttrCtrlHints() {
        return new AttrCtrlHints();
    }

    /**
     * Create an instance of {@link ViewCriteria}
     *
     */
    public ViewCriteria createViewCriteria() {
        return new ViewCriteria();
    }

    /**
     * Create an instance of {@link TimestampResult}
     *
     */
    public TimestampResult createTimestampResult() {
        return new TimestampResult();
    }

    /**
     * Create an instance of {@link XMLInfo}
     *
     */
    public XMLInfo createXMLInfo() {
        return new XMLInfo();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://xmlns.oracle.com/apps/financials/commonModules/shared/model/erpIntegrationService/types/", name = "callbackURL", scope = ExportBulkData.class)
    public JAXBElement<String> createExportBulkDataCallbackURL(String value) {
        return new JAXBElement<String>(_ExportBulkDataCallbackURL_QNAME, String.class, ExportBulkData.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://xmlns.oracle.com/apps/financials/commonModules/shared/model/erpIntegrationService/types/", name = "notificationCode", scope = ExportBulkData.class)
    public JAXBElement<String> createExportBulkDataNotificationCode(String value) {
        return new JAXBElement<String>(_ExportBulkDataNotificationCode_QNAME, String.class, ExportBulkData.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://xmlns.oracle.com/apps/financials/commonModules/shared/model/erpIntegrationService/types/", name = "jobOptions", scope = ExportBulkData.class)
    public JAXBElement<String> createExportBulkDataJobOptions(String value) {
        return new JAXBElement<String>(_ExportBulkDataJobOptions_QNAME, String.class, ExportBulkData.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://xmlns.oracle.com/apps/financials/commonModules/shared/model/erpIntegrationService/types/", name = "parameterList", scope = ExportBulkData.class)
    public JAXBElement<String> createExportBulkDataParameterList(String value) {
        return new JAXBElement<String>(_ExportBulkDataParameterList_QNAME, String.class, ExportBulkData.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement} {@code <} {@link EssJob} {@code >} }
     *
     */
    @XmlElementDecl(namespace = "http://xmlns.oracle.com/apps/financials/commonModules/shared/model/erpIntegrationService/", name = "essJob")
    public JAXBElement<EssJob> createEssJob(EssJob value) {
        return new JAXBElement<EssJob>(_EssJob_QNAME, EssJob.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement} {@code <} {@link DocumentDetail} {@code >} }
     *
     */
    @XmlElementDecl(namespace = "http://xmlns.oracle.com/apps/financials/commonModules/shared/model/erpIntegrationService/", name = "documentDetail")
    public JAXBElement<DocumentDetail> createDocumentDetail(DocumentDetail value) {
        return new JAXBElement<DocumentDetail>(_DocumentDetail_QNAME, DocumentDetail.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement} {@code <} {@link FaultType} {@code >} }
     *
     */
    @XmlElementDecl(namespace = "http://xmlns.oracle.com/oracleas/schema/oracle-fault-11_0", name = "Fault")
    public JAXBElement<FaultType> createFault(FaultType value) {
        return new JAXBElement<FaultType>(_Fault_QNAME, FaultType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement} {@code <} {@link DocumentDetails} {@code >} }
     *
     */
    @XmlElementDecl(namespace = "http://xmlns.oracle.com/apps/financials/commonModules/shared/model/erpIntegrationService/", name = "documentDetails")
    public JAXBElement<DocumentDetails> createDocumentDetails(DocumentDetails value) {
        return new JAXBElement<DocumentDetails>(_DocumentDetails_QNAME, DocumentDetails.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement} {@code <} {@link FindCriteria} {@code >} }
     *
     */
    @XmlElementDecl(namespace = "http://xmlns.oracle.com/adf/svc/types/", name = "findCriteria")
    public JAXBElement<FindCriteria> createFindCriteria(FindCriteria value) {
        return new JAXBElement<FindCriteria>(_FindCriteria_QNAME, FindCriteria.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement} {@code <} {@link Types} {@code >} }
     *
     */
    @XmlElementDecl(namespace = "commonj.sdo", name = "types")
    public JAXBElement<Types> createTypes(Types value) {
        return new JAXBElement<Types>(_Types_QNAME, Types.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement} {@code <} {@link Type} {@code >} }
     *
     */
    @XmlElementDecl(namespace = "commonj.sdo", name = "type")
    public JAXBElement<Type> createType(Type value) {
        return new JAXBElement<Type>(_Type_QNAME, Type.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement} {@code <} {@link FindControl} {@code >} }
     *
     */
    @XmlElementDecl(namespace = "http://xmlns.oracle.com/adf/svc/types/", name = "findControl")
    public JAXBElement<FindControl> createFindControl(FindControl value) {
        return new JAXBElement<FindControl>(_FindControl_QNAME, FindControl.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Object }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "commonj.sdo", name = "dataObject")
    public JAXBElement<Object> createDataObject(Object value) {
        return new JAXBElement<Object>(_DataObject_QNAME, Object.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement} {@code <} {@link ProcessControl} {@code >} }
     *
     */
    @XmlElementDecl(namespace = "http://xmlns.oracle.com/adf/svc/types/", name = "processControl")
    public JAXBElement<ProcessControl> createProcessControl(ProcessControl value) {
        return new JAXBElement<ProcessControl>(_ProcessControl_QNAME, ProcessControl.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement} {@code <} {@link ServiceErrorMessage} {@code >} }
     *
     */
    @XmlElementDecl(namespace = "http://xmlns.oracle.com/adf/svc/errors/", name = "ServiceErrorMessage")
    public JAXBElement<ServiceErrorMessage> createServiceErrorMessage(ServiceErrorMessage value) {
        return new JAXBElement<ServiceErrorMessage>(_ServiceErrorMessage_QNAME, ServiceErrorMessage.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement} {@code <} {@link DataGraphType} {@code >} }
     *
     */
    @XmlElementDecl(namespace = "commonj.sdo", name = "datagraph")
    public JAXBElement<DataGraphType> createDatagraph(DataGraphType value) {
        return new JAXBElement<DataGraphType>(_Datagraph_QNAME, DataGraphType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://xmlns.oracle.com/apps/financials/commonModules/shared/model/erpIntegrationService/", name = "DocumentAccount", scope = DocumentDetail.class)
    public JAXBElement<String> createDocumentDetailDocumentAccount(String value) {
        return new JAXBElement<String>(_DocumentDetailDocumentAccount_QNAME, String.class, DocumentDetail.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://xmlns.oracle.com/apps/financials/commonModules/shared/model/erpIntegrationService/", name = "ContentType", scope = DocumentDetail.class)
    public JAXBElement<String> createDocumentDetailContentType(String value) {
        return new JAXBElement<String>(_DocumentDetailContentType_QNAME, String.class, DocumentDetail.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link byte[]}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://xmlns.oracle.com/apps/financials/commonModules/shared/model/erpIntegrationService/", name = "Content", scope = DocumentDetail.class)
    public JAXBElement<byte[]> createDocumentDetailContent(byte[] value) {
        return new JAXBElement<byte[]>(_DocumentDetailContent_QNAME, byte[].class, DocumentDetail.class, ((byte[]) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://xmlns.oracle.com/apps/financials/commonModules/shared/model/erpIntegrationService/", name = "DocumentId", scope = DocumentDetail.class)
    public JAXBElement<String> createDocumentDetailDocumentId(String value) {
        return new JAXBElement<String>(_DocumentDetailDocumentId_QNAME, String.class, DocumentDetail.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://xmlns.oracle.com/apps/financials/commonModules/shared/model/erpIntegrationService/", name = "DocumentName", scope = DocumentDetail.class)
    public JAXBElement<String> createDocumentDetailDocumentName(String value) {
        return new JAXBElement<String>(_DocumentDetailDocumentName_QNAME, String.class, DocumentDetail.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://xmlns.oracle.com/apps/financials/commonModules/shared/model/erpIntegrationService/", name = "DocumentAuthor", scope = DocumentDetail.class)
    public JAXBElement<String> createDocumentDetailDocumentAuthor(String value) {
        return new JAXBElement<String>(_DocumentDetailDocumentAuthor_QNAME, String.class, DocumentDetail.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://xmlns.oracle.com/apps/financials/commonModules/shared/model/erpIntegrationService/", name = "DocumentSecurityGroup", scope = DocumentDetail.class)
    public JAXBElement<String> createDocumentDetailDocumentSecurityGroup(String value) {
        return new JAXBElement<String>(_DocumentDetailDocumentSecurityGroup_QNAME, String.class, DocumentDetail.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://xmlns.oracle.com/apps/financials/commonModules/shared/model/erpIntegrationService/", name = "DocumentTitle", scope = DocumentDetail.class)
    public JAXBElement<String> createDocumentDetailDocumentTitle(String value) {
        return new JAXBElement<String>(_DocumentDetailDocumentTitle_QNAME, String.class, DocumentDetail.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://xmlns.oracle.com/apps/financials/commonModules/shared/model/erpIntegrationService/", name = "FileName", scope = DocumentDetail.class)
    public JAXBElement<String> createDocumentDetailFileName(String value) {
        return new JAXBElement<String>(_DocumentDetailFileName_QNAME, String.class, DocumentDetail.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://xmlns.oracle.com/apps/financials/commonModules/shared/model/erpIntegrationService/types/", name = "callbackURL", scope = ExportBulkDataAsync.class)
    public JAXBElement<String> createExportBulkDataAsyncCallbackURL(String value) {
        return new JAXBElement<String>(_ExportBulkDataCallbackURL_QNAME, String.class, ExportBulkDataAsync.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://xmlns.oracle.com/apps/financials/commonModules/shared/model/erpIntegrationService/types/", name = "notificationCode", scope = ExportBulkDataAsync.class)
    public JAXBElement<String> createExportBulkDataAsyncNotificationCode(String value) {
        return new JAXBElement<String>(_ExportBulkDataNotificationCode_QNAME, String.class, ExportBulkDataAsync.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://xmlns.oracle.com/apps/financials/commonModules/shared/model/erpIntegrationService/types/", name = "jobOptions", scope = ExportBulkDataAsync.class)
    public JAXBElement<String> createExportBulkDataAsyncJobOptions(String value) {
        return new JAXBElement<String>(_ExportBulkDataJobOptions_QNAME, String.class, ExportBulkDataAsync.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://xmlns.oracle.com/apps/financials/commonModules/shared/model/erpIntegrationService/types/", name = "parameterList", scope = ExportBulkDataAsync.class)
    public JAXBElement<String> createExportBulkDataAsyncParameterList(String value) {
        return new JAXBElement<String>(_ExportBulkDataParameterList_QNAME, String.class, ExportBulkDataAsync.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://xmlns.oracle.com/apps/financials/commonModules/shared/model/erpIntegrationService/types/", name = "callbackURL", scope = ExtractAndPurge.class)
    public JAXBElement<String> createExtractAndPurgeCallbackURL(String value) {
        return new JAXBElement<String>(_ExportBulkDataCallbackURL_QNAME, String.class, ExtractAndPurge.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://xmlns.oracle.com/apps/financials/commonModules/shared/model/erpIntegrationService/types/", name = "jobOptions", scope = ExtractAndPurge.class)
    public JAXBElement<String> createExtractAndPurgeJobOptions(String value) {
        return new JAXBElement<String>(_ExportBulkDataJobOptions_QNAME, String.class, ExtractAndPurge.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://xmlns.oracle.com/apps/financials/commonModules/shared/model/erpIntegrationService/types/", name = "notificationCode", scope = ExtractAndPurge.class)
    public JAXBElement<String> createExtractAndPurgeNotificationCode(String value) {
        return new JAXBElement<String>(_ExportBulkDataNotificationCode_QNAME, String.class, ExtractAndPurge.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://xmlns.oracle.com/apps/financials/commonModules/shared/model/erpIntegrationService/types/", name = "callbackURL", scope = LoadAndImportDataAsync.class)
    public JAXBElement<String> createLoadAndImportDataAsyncCallbackURL(String value) {
        return new JAXBElement<String>(_ExportBulkDataCallbackURL_QNAME, String.class, LoadAndImportDataAsync.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://xmlns.oracle.com/apps/financials/commonModules/shared/model/erpIntegrationService/types/", name = "interfaceDetails", scope = LoadAndImportDataAsync.class)
    public JAXBElement<String> createLoadAndImportDataAsyncInterfaceDetails(String value) {
        return new JAXBElement<String>(_LoadAndImportDataAsyncInterfaceDetails_QNAME, String.class, LoadAndImportDataAsync.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://xmlns.oracle.com/apps/financials/commonModules/shared/model/erpIntegrationService/types/", name = "notificationCode", scope = LoadAndImportDataAsync.class)
    public JAXBElement<String> createLoadAndImportDataAsyncNotificationCode(String value) {
        return new JAXBElement<String>(_ExportBulkDataNotificationCode_QNAME, String.class, LoadAndImportDataAsync.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://xmlns.oracle.com/apps/financials/commonModules/shared/model/erpIntegrationService/", name = "DocumentAccount", scope = DocumentDetails.class)
    public JAXBElement<String> createDocumentDetailsDocumentAccount(String value) {
        return new JAXBElement<String>(_DocumentDetailDocumentAccount_QNAME, String.class, DocumentDetails.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://xmlns.oracle.com/apps/financials/commonModules/shared/model/erpIntegrationService/", name = "ContentType", scope = DocumentDetails.class)
    public JAXBElement<String> createDocumentDetailsContentType(String value) {
        return new JAXBElement<String>(_DocumentDetailContentType_QNAME, String.class, DocumentDetails.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://xmlns.oracle.com/apps/financials/commonModules/shared/model/erpIntegrationService/", name = "DocumentId", scope = DocumentDetails.class)
    public JAXBElement<String> createDocumentDetailsDocumentId(String value) {
        return new JAXBElement<String>(_DocumentDetailDocumentId_QNAME, String.class, DocumentDetails.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://xmlns.oracle.com/apps/financials/commonModules/shared/model/erpIntegrationService/", name = "DocumentName", scope = DocumentDetails.class)
    public JAXBElement<String> createDocumentDetailsDocumentName(String value) {
        return new JAXBElement<String>(_DocumentDetailDocumentName_QNAME, String.class, DocumentDetails.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://xmlns.oracle.com/apps/financials/commonModules/shared/model/erpIntegrationService/", name = "DocumentAuthor", scope = DocumentDetails.class)
    public JAXBElement<String> createDocumentDetailsDocumentAuthor(String value) {
        return new JAXBElement<String>(_DocumentDetailDocumentAuthor_QNAME, String.class, DocumentDetails.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://xmlns.oracle.com/apps/financials/commonModules/shared/model/erpIntegrationService/", name = "DocumentSecurityGroup", scope = DocumentDetails.class)
    public JAXBElement<String> createDocumentDetailsDocumentSecurityGroup(String value) {
        return new JAXBElement<String>(_DocumentDetailDocumentSecurityGroup_QNAME, String.class, DocumentDetails.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://xmlns.oracle.com/apps/financials/commonModules/shared/model/erpIntegrationService/", name = "DocumentTitle", scope = DocumentDetails.class)
    public JAXBElement<String> createDocumentDetailsDocumentTitle(String value) {
        return new JAXBElement<String>(_DocumentDetailDocumentTitle_QNAME, String.class, DocumentDetails.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://xmlns.oracle.com/apps/financials/commonModules/shared/model/erpIntegrationService/types/", name = "callbackURL", scope = ExtractAndPurgeAsync.class)
    public JAXBElement<String> createExtractAndPurgeAsyncCallbackURL(String value) {
        return new JAXBElement<String>(_ExportBulkDataCallbackURL_QNAME, String.class, ExtractAndPurgeAsync.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://xmlns.oracle.com/apps/financials/commonModules/shared/model/erpIntegrationService/types/", name = "jobOptions", scope = ExtractAndPurgeAsync.class)
    public JAXBElement<String> createExtractAndPurgeAsyncJobOptions(String value) {
        return new JAXBElement<String>(_ExportBulkDataJobOptions_QNAME, String.class, ExtractAndPurgeAsync.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://xmlns.oracle.com/apps/financials/commonModules/shared/model/erpIntegrationService/types/", name = "notificationCode", scope = ExtractAndPurgeAsync.class)
    public JAXBElement<String> createExtractAndPurgeAsyncNotificationCode(String value) {
        return new JAXBElement<String>(_ExportBulkDataNotificationCode_QNAME, String.class, ExtractAndPurgeAsync.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://xmlns.oracle.com/apps/financials/commonModules/shared/model/erpIntegrationService/", name = "ParameterList", scope = EssJob.class)
    public JAXBElement<String> createEssJobParameterList(String value) {
        return new JAXBElement<String>(_EssJobParameterList_QNAME, String.class, EssJob.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://xmlns.oracle.com/apps/financials/commonModules/shared/model/erpIntegrationService/", name = "JobName", scope = EssJob.class)
    public JAXBElement<String> createEssJobJobName(String value) {
        return new JAXBElement<String>(_EssJobJobName_QNAME, String.class, EssJob.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://xmlns.oracle.com/apps/financials/commonModules/shared/model/erpIntegrationService/", name = "JobRequestId", scope = EssJob.class)
    public JAXBElement<String> createEssJobJobRequestId(String value) {
        return new JAXBElement<String>(_EssJobJobRequestId_QNAME, String.class, EssJob.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://xmlns.oracle.com/apps/financials/commonModules/shared/model/erpIntegrationService/types/", name = "callbackURL", scope = ImportBulkData.class)
    public JAXBElement<String> createImportBulkDataCallbackURL(String value) {
        return new JAXBElement<String>(_ExportBulkDataCallbackURL_QNAME, String.class, ImportBulkData.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://xmlns.oracle.com/apps/financials/commonModules/shared/model/erpIntegrationService/types/", name = "jobOptions", scope = ImportBulkData.class)
    public JAXBElement<String> createImportBulkDataJobOptions(String value) {
        return new JAXBElement<String>(_ExportBulkDataJobOptions_QNAME, String.class, ImportBulkData.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://xmlns.oracle.com/apps/financials/commonModules/shared/model/erpIntegrationService/types/", name = "notificationCode", scope = ImportBulkData.class)
    public JAXBElement<String> createImportBulkDataNotificationCode(String value) {
        return new JAXBElement<String>(_ExportBulkDataNotificationCode_QNAME, String.class, ImportBulkData.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement} {@code <} {@link DocumentDetail} {@code >} }
     *
     */
    @XmlElementDecl(namespace = "http://xmlns.oracle.com/apps/financials/commonModules/shared/model/erpIntegrationService/types/", name = "document", scope = ImportBulkData.class)
    public JAXBElement<DocumentDetail> createImportBulkDataDocument(DocumentDetail value) {
        return new JAXBElement<DocumentDetail>(_ImportBulkDataDocument_QNAME, DocumentDetail.class, ImportBulkData.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://xmlns.oracle.com/apps/financials/commonModules/shared/model/erpIntegrationService/types/", name = "callbackURL", scope = ImportBulkDataAsync.class)
    public JAXBElement<String> createImportBulkDataAsyncCallbackURL(String value) {
        return new JAXBElement<String>(_ExportBulkDataCallbackURL_QNAME, String.class, ImportBulkDataAsync.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://xmlns.oracle.com/apps/financials/commonModules/shared/model/erpIntegrationService/types/", name = "jobOptions", scope = ImportBulkDataAsync.class)
    public JAXBElement<String> createImportBulkDataAsyncJobOptions(String value) {
        return new JAXBElement<String>(_ExportBulkDataJobOptions_QNAME, String.class, ImportBulkDataAsync.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://xmlns.oracle.com/apps/financials/commonModules/shared/model/erpIntegrationService/types/", name = "notificationCode", scope = ImportBulkDataAsync.class)
    public JAXBElement<String> createImportBulkDataAsyncNotificationCode(String value) {
        return new JAXBElement<String>(_ExportBulkDataNotificationCode_QNAME, String.class, ImportBulkDataAsync.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement} {@code <} {@link DocumentDetail} {@code >} }
     *
     */
    @XmlElementDecl(namespace = "http://xmlns.oracle.com/apps/financials/commonModules/shared/model/erpIntegrationService/types/", name = "document", scope = ImportBulkDataAsync.class)
    public JAXBElement<DocumentDetail> createImportBulkDataAsyncDocument(DocumentDetail value) {
        return new JAXBElement<DocumentDetail>(_ImportBulkDataDocument_QNAME, DocumentDetail.class, ImportBulkDataAsync.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://xmlns.oracle.com/apps/financials/commonModules/shared/model/erpIntegrationService/types/", name = "callbackURL", scope = LoadAndImportData.class)
    public JAXBElement<String> createLoadAndImportDataCallbackURL(String value) {
        return new JAXBElement<String>(_ExportBulkDataCallbackURL_QNAME, String.class, LoadAndImportData.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://xmlns.oracle.com/apps/financials/commonModules/shared/model/erpIntegrationService/types/", name = "interfaceDetails", scope = LoadAndImportData.class)
    public JAXBElement<String> createLoadAndImportDataInterfaceDetails(String value) {
        return new JAXBElement<String>(_LoadAndImportDataAsyncInterfaceDetails_QNAME, String.class, LoadAndImportData.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://xmlns.oracle.com/apps/financials/commonModules/shared/model/erpIntegrationService/types/", name = "notificationCode", scope = LoadAndImportData.class)
    public JAXBElement<String> createLoadAndImportDataNotificationCode(String value) {
        return new JAXBElement<String>(_ExportBulkDataNotificationCode_QNAME, String.class, LoadAndImportData.class, value);
    }

}
