--------------------------------------------------------
--  DDL for Package XX_SECURITY_PKG
--------------------------------------------------------

  CREATE OR REPLACE EDITIONABLE PACKAGE "XX_SECURITY_PKG" 
IS
   PROCEDURE GET_SETUP_DETAILS (
                                SETUP_TYPE_PARAM       IN     VARCHAR2,
                                SETUP_SUB_TYPE_PARAM   IN     VARCHAR2,
                                MODULE_PARAM           IN     VARCHAR2,
                                HOST_PARAM                OUT VARCHAR2,
                                PORT_PARAM                OUT VARCHAR2,
                                USER_NAME_PARAM           OUT VARCHAR2,
                                PASSWORD_PARAM            OUT VARCHAR2,
                                OPERATION_PARAM           OUT VARCHAR2,
                                SFTP_PATH_PARAM           OUT VARCHAR2,
                                EMAIL_ADDRESS_PARAM           OUT VARCHAR2);
END;
