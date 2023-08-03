--------------------------------------------------------
--  DDL for Procedure ZATCA_ERROER
--------------------------------------------------------
set define off;

  CREATE OR REPLACE EDITIONABLE PROCEDURE "ZATCA_ERROER" 
(   p_CUSTOMERTRXID IN VARCHAR2,
    p_INVOICENUMBER in VARCHAR2,
   p_ERRORMESSAGE in VARCHAR2,
   
   p_ERROR_TYPE in varchar2,
   p_error_code in varchar2,
   p_error_source in varchar2,
   p_flag in number
   
)
is  
l_count number;
l_success_count number;
l_count_error number:=0;
begin    

    SELECT count(*) into l_success_count from ZATCA_RESPONSE_SUCCESS_T
    WHERE CUSTOMERTRXID=p_CUSTOMERTRXID;

    if l_success_count!=0 AND p_ERROR_TYPE!='WARNING' AND p_ERROR_TYPE!='INFO'  then
        DELETE FROM ZATCA_RESPONSE_SUCCESS_T
        WHERE CUSTOMERTRXID=p_CUSTOMERTRXID;
    end if;

    select count(*) into l_count from ZATCA_RESPONSE_ERROR_T
    WHERE CUSTOMERTRXID=p_CUSTOMERTRXID;

    if l_count!=0 and p_flag=1 then

        delete from ZATCA_RESPONSE_ERROR_T
        Where CUSTOMERTRXID=p_CUSTOMERTRXID;

        INSERT INTO ZATCA_RESPONSE_ERROR_T(
        CUSTOMERTRXID,
        INVOICENUMBER,
        ERRORMESSAGE,
        ERRORCODE,
        ERRORSOURCE,
        ERRORTYPE
        )values(
        p_CUSTOMERTRXID,
       p_INVOICENUMBER,
        p_ERRORMESSAGE,
        p_error_code,
        p_error_source,
        p_ERROR_TYPE
        );

    else
        INSERT INTO ZATCA_RESPONSE_ERROR_T(
        CUSTOMERTRXID,
        INVOICENUMBER,
        ERRORMESSAGE,
        ERRORCODE,
        ERRORSOURCE,
        ERRORTYPE
        )values(
        p_CUSTOMERTRXID,
       p_INVOICENUMBER,
        p_ERRORMESSAGE,
        p_error_code,
        p_error_source,
        p_ERROR_TYPE


        );
    end if;

    SELECT count(*) into l_count_error FROM ZATCA_RESPONSE_ERROR_T
    WHERE CUSTOMERTRXID=p_CUSTOMERTRXID AND ERRORTYPE='ERROR';

    IF l_count_error>0 THEN
        UPDATE ZATCA_EINVOICE_HEADER_T
        SET INVOICE_STATUS='Error'
        WHERE CUSTOMER_TRX_ID=p_CUSTOMERTRXID;
    ELSE
        UPDATE ZATCA_EINVOICE_HEADER_T
        SET INVOICE_STATUS='Warning'
        WHERE CUSTOMER_TRX_ID=p_CUSTOMERTRXID;
    END IF;

    commit;
end ZATCA_ERROER;
--------------------------------------------------------
--  DDL for Procedure ZATCA_SUCCESS
--------------------------------------------------------
set define off;

  CREATE OR REPLACE EDITIONABLE PROCEDURE "ZATCA_SUCCESS" 
(   p_CUSTOMERTRXID IN VARCHAR2,
    p_INVOICENUMBER in VARCHAR2,
    p_QRCODE in blob,
   p_UUID in VARCHAR2,
   p_INVOICEHASH in VARCHAR2,
   p_MESSAGE in VARCHAR2,
   p_INVOICEXML in blob,
   
   p_INVOICESTATUS in VARCHAR2,
    p_VALIDATIONSSUCCESS in VARCHAR2,
    p_INVOICE_FILE in BLOB,
    p_FINAL_OUTPUT_FILE in blob,
    p_pdf in blob
)
is  
l_count number;
l_count_error number;
begin    

    select count(*) into l_count from ZATCA_RESPONSE_SUCCESS_T
    WHERE CUSTOMERTRXID=p_CUSTOMERTRXID;

        select count(*) into l_count_error from ZATCA_RESPONSE_ERROR_T
    WHERE CUSTOMERTRXID=p_CUSTOMERTRXID;

    if l_count_error!=0 THEN
        DELETE FROM ZATCA_RESPONSE_ERROR_T
        WHERE CUSTOMERTRXID =p_CUSTOMERTRXID;

        DELETE FROM XXZATCA_INVOICE_FILES
        WHERE TRANSACTION_ID =p_INVOICENUMBER;

    END if;

    if l_count!=0 then

        update ZATCA_RESPONSE_SUCCESS_T
        set CUSTOMERTRXID=p_CUSTOMERTRXID,
        INVOICENUMBER=p_INVOICENUMBER,
        QRCODE=p_QRCODE,
        UUID=p_UUID,
        INVOICEHASH=p_INVOICEHASH,
        MESSAGE=p_MESSAGE,
        INVOICEXML=p_INVOICEXML,
        ISSUEDATE=SYSDATE,
        INVOICESTATUS=p_INVOICESTATUS,
        VALIDATIONSSUCCESS=p_VALIDATIONSSUCCESS,
        INVOICE_FILE=p_INVOICE_FILE,
        FINAL_OUTPUT_FILE=p_FINAL_OUTPUT_FILE
        Where CUSTOMERTRXID=p_CUSTOMERTRXID;

        UPDATE XXZATCA_INVOICE_FILES SET
        INVOICE_FILE=p_pdf
        WHERE TRANSACTION_ID=p_INVOICENUMBER;

    else
        INSERT INTO ZATCA_RESPONSE_SUCCESS_T(
        CUSTOMERTRXID,
        INVOICENUMBER,
        QRCODE,
        UUID,
        INVOICEHASH,
        MESSAGE,
        INVOICEXML,
        ISSUEDATE,
        INVOICESTATUS,
        VALIDATIONSSUCCESS,
        INVOICE_FILE,
        FINAL_OUTPUT_FILE
        )values(
        p_CUSTOMERTRXID,
       p_INVOICENUMBER,
        p_QRCODE,
        p_UUID,
        p_INVOICEHASH,
        p_MESSAGE,
        p_INVOICEXML,
        SYSDATE,
        p_INVOICESTATUS,
        p_VALIDATIONSSUCCESS,
        p_INVOICE_FILE,
        p_FINAL_OUTPUT_FILE
        );

            insert into XXZATCA_INVOICE_FILES(
            TRANSACTION_ID,
            INVOICE_FILE
            )
            values(
            p_CUSTOMERTRXID,
            p_pdf
            );
    end if;

  UPDATE ZATCA_EINVOICE_HEADER_T SET 
      INVOICE_STATUS =  CASE 
                WHEN INVOICESUBTYPECODE like '01%' THEN
                    'Cleared'
                WHEN INVOICESUBTYPECODE like '02%' THEN
                    'Reported'
                ELSE
                    'other'
                END,
                PROCESSED_FLAG='P'
        WHERE CUSTOMER_TRX_ID= p_CUSTOMERTRXID;

  commit;

end ZATCA_SUCCESS;
