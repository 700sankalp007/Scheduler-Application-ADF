--------------------------------------------------------
--  File created - Wednesday-September-18-2019   
--------------------------------------------------------
--------------------------------------------------------
--  DDL for Type EI_INTERFACE_SETUP_TYPE
--------------------------------------------------------

--  CREATE OR REPLACE EDITIONABLE TYPE "EI_INTERFACE_SETUP_TYPE" AS OBJECT
--                  (SETUP_ID NUMBER,
--                   HOST VARCHAR2 (1000),
--                   PORT VARCHAR2 (10),
--                   USER_NAME VARCHAR2 (100),
--                   PASSWORD VARCHAR2 (1000),
--                   SETUP_TYPE VARCHAR2 (10),
--                   SUB_TYPE VARCHAR2 (10),
--                   OPERATION VARCHAR2 (100),
--                   SFTP_PATH VARCHAR2 (1000))
--
--
--
--/
----------------------------------------------------------
----  DDL for Type EI_INTERFACE_SETUP_TYPE_VAR
----------------------------------------------------------
--
--  CREATE OR REPLACE EDITIONABLE TYPE "EI_INTERFACE_SETUP_TYPE_VAR" as table of EI_INTERFACE_SETUP_TYPE
--
--/
--------------------------------------------------------
--  DDL for Sequence SC_LOOKUP_STRINGS_SEQ
--------------------------------------------------------

   CREATE SEQUENCE  "SC_LOOKUP_STRINGS_SEQ"  MINVALUE 1 MAXVALUE 9999999999999999999999999999 INCREMENT BY 1 START WITH 1 NOCACHE  NOORDER  NOCYCLE   ;
--------------------------------------------------------
--  DDL for Sequence SC_REPORT_COL_MAPPING_SEQ
--------------------------------------------------------

   CREATE SEQUENCE  "SC_REPORT_COL_MAPPING_SEQ"  MINVALUE 1 MAXVALUE 999999999999999999999999999 INCREMENT BY 1 START WITH 1 NOCACHE  NOORDER  NOCYCLE   ;
--------------------------------------------------------
--  DDL for Sequence SC_REPORT_HEADER_SEQ
--------------------------------------------------------

   CREATE SEQUENCE  "SC_REPORT_HEADER_SEQ"  MINVALUE 1 MAXVALUE 999999999999999999999999999 INCREMENT BY 1 START WITH 1 NOCACHE  NOORDER  NOCYCLE   ;
--------------------------------------------------------
--  DDL for Sequence SC_REPORT_RUN_HIST_SEQ
--------------------------------------------------------

   CREATE SEQUENCE  "SC_REPORT_RUN_HIST_SEQ"  MINVALUE 1 MAXVALUE 9999999999999999999999999999 INCREMENT BY 1 START WITH 1 NOCACHE  NOORDER  NOCYCLE   ;
--------------------------------------------------------
--  DDL for Sequence SC_REPORT_SETUP_DTL_SEQ
--------------------------------------------------------

   CREATE SEQUENCE  "SC_REPORT_SETUP_DTL_SEQ"  MINVALUE 1 MAXVALUE 99999999999999999999999 INCREMENT BY 1 START WITH 1 NOCACHE  NOORDER  NOCYCLE   ;
--------------------------------------------------------
--  DDL for Sequence SC_SCHEDULER_SETUP_SEQ
--------------------------------------------------------

   CREATE SEQUENCE  "SC_SCHEDULER_SETUP_SEQ"  MINVALUE 1 MAXVALUE 99999999999999999999999999 INCREMENT BY 1 START WITH 1 NOCACHE  NOORDER  NOCYCLE   ;
   
   
   
 -------------------------------------------------
 ------ DDL For SEQUENCE  sc_report_exception_log_seq
 ------------------------------------------------
 create sequence  sc_report_exception_log_seq  minvalue 1 maxvalue 9999999999999999999999999999 increment by 1 start with 1 nocache  noorder  nocycle   ;
--------------------------------------------------------
--  DDL for Table SC_LOOKUP_STRINGS
--------------------------------------------------------

  CREATE TABLE "SC_LOOKUP_STRINGS" 
   (	"GEN_ID" NUMBER, 
	"TYPE" VARCHAR2(100), 
	"CODE" VARCHAR2(100), 
	"VALUE" VARCHAR2(100), 
	"ENT_DATE" DATE, 
	"ENT_BY" VARCHAR2(100), 
	"MODIFY_BY" VARCHAR2(100), 
	"MODIFY_DATE" DATE, 
	"ENABLE_FLAG" VARCHAR2(1)
   ) ;
--------------------------------------------------------
--  DDL for Table SC_REPORT_DATA_MAPPING
--------------------------------------------------------

  CREATE TABLE "SC_REPORT_DATA_MAPPING" 
   (	"MAP_ID" NUMBER(19,0), 
	"REPORT_COL_NAME" VARCHAR2(200) DEFAULT NULL, 
	"REPORT_SEQ_NAME" NUMBER(10,0) DEFAULT NULL, 
	"TABLE_COL_NAME" VARCHAR2(200) DEFAULT NULL, 
	"DEFAULT_VALUE" VARCHAR2(200) DEFAULT NULL, 
	"REPORT_HEADER_ID" NUMBER(19,0) DEFAULT NULL, 
	"PRIMARY_FLAG" VARCHAR2(10) DEFAULT NULL, 
	"DATE_FORMAT" VARCHAR2(50 CHAR), 
	"IS_SEQ" VARCHAR2(1), 
	"COLUMN_DATA_TYPE" VARCHAR2(100 CHAR)
   ) ;
--------------------------------------------------------
--  DDL for Table SC_REPORT_RUN_HISTORY
--------------------------------------------------------

  CREATE TABLE "SC_REPORT_RUN_HISTORY" 
   (	"ID" NUMBER, 
	"SERVICE_TYPE" VARCHAR2(100 CHAR), 
	"STATUS" VARCHAR2(50 CHAR), 
	"RECORD_PROCESSED" VARCHAR2(100 CHAR), 
	"START_TIME" TIMESTAMP (6), 
	"END_TME" TIMESTAMP (6), 
	"SCHEDULER_ID" NUMBER, 
	"RUN_TYPE" VARCHAR2(50 CHAR)
   ) ;
--------------------------------------------------------
--  DDL for Table SC_REPORT_SETUP_DTL
--------------------------------------------------------

  CREATE TABLE "SC_REPORT_SETUP_DTL" 
   (	"EI_REP_DTL_ID" NUMBER, 
	"EI_REP_HEADER_ID" NUMBER, 
	"PARAM_NAME" VARCHAR2(50), 
	"PARAM_SQL_TYPE" VARCHAR2(50), 
	"DEFUALT_VAL" VARCHAR2(50), 
	"LAST_VAL_LOOKUP_TYPE" VARCHAR2(50), 
	"CREATED_BY" VARCHAR2(100), 
	"CREATED_DATE" DATE, 
	"UPDATED_BY" VARCHAR2(100), 
	"UPDATED_DATE" DATE, 
	"SQL_STATEMENT" VARCHAR2(200)
   ) ;
--------------------------------------------------------
--  DDL for Table SC_REPORT_SETUP_HEADER
--------------------------------------------------------

  CREATE TABLE "SC_REPORT_SETUP_HEADER" 
   (	"EI_REP_HEADER_ID" NUMBER, 
	"REPORT_PATH" VARCHAR2(400), 
	"REPORT_DATA_TABLE" VARCHAR2(50), 
	"REPORT_DATA_FORMAT" VARCHAR2(10), 
	"REPORT_DATA_LOCALE" VARCHAR2(10), 
	"CREATED_BY" VARCHAR2(100), 
	"CREATED_DATE" DATE, 
	"UPDATED_BY" VARCHAR2(100), 
	"UPDATED_DATE" DATE, 
	"SERVICE_TYPE" VARCHAR2(100), 
	"IS_REFRESH" VARCHAR2(1), 
	"DELIMITER" VARCHAR2(1), 
	"ROOT_NODE" VARCHAR2(100 CHAR), 
	"SCHEDULER_ID" NUMBER
   ) ;
--------------------------------------------------------
--  DDL for Table SC_SCHEDULER_DATE
--------------------------------------------------------

  CREATE TABLE "SC_SCHEDULER_DATE" 
   (	"SCHEDULER_DATE_ID" NUMBER, 
	"SERVICE_TYPE" VARCHAR2(100), 
	"SCHEDULER_FLAG" VARCHAR2(1), 
	"SCHEDULER_DATE" TIMESTAMP (6), 
	"CREATED_BY" VARCHAR2(255), 
	"UPDATED_BY" VARCHAR2(255), 
	"UPDATION_DATE" DATE, 
	"CREATION_DATE" DATE, 
	"SCHEDULER_ID" NUMBER
   ) ;
--------------------------------------------------------
--  DDL for Table SC_SCHEDULER_LOGS
--------------------------------------------------------

  CREATE TABLE "SC_SCHEDULER_LOGS" 
   (	"LOG_ID" NUMBER, 
	"LOG_MESSAGE" VARCHAR2(2000), 
	"LOG_TRACE" VARCHAR2(4000), 
	"LOG_DATE" TIMESTAMP (6), 
	"LOG_TYPE" VARCHAR2(100)
   ) ;
--------------------------------------------------------
--  DDL for Table SC_SCHEDULER_SETUP
--------------------------------------------------------

  CREATE TABLE "SC_SCHEDULER_SETUP" 
   (	"SCHEDULER_ID" NUMBER, 
	"SERVICE_NAME" VARCHAR2(50), 
	"SCHEDULE_FLAG" VARCHAR2(1), 
	"SCHEDULER_TYPE" VARCHAR2(1), 
	"HOUR" NUMBER, 
	"MINUTE" NUMBER, 
	"SECOND" NUMBER, 
	"SERVICE_TYPE" VARCHAR2(100), 
	"CREATED_BY" VARCHAR2(255), 
	"UPDATED_BY" VARCHAR2(255), 
	"UPDATION_DATE" DATE, 
	"CREATION_DATE" DATE, 
	"STATUS" VARCHAR2(100), 
	"IS_PARAMETERIZED" VARCHAR2(1), 
	"CALL_TYPE" VARCHAR2(5)
   ) ;
--------------------------------------------------------
--  DDL for Table SC_SCHEDULER_SETUP_LOGS
--------------------------------------------------------

  CREATE TABLE "SC_SCHEDULER_SETUP_LOGS" 
   (	"LOG_ID" NUMBER, 
	"SERVICE_TYPE" VARCHAR2(100), 
	"RUN_START_TIME" DATE, 
	"RUN_COMPLETE_TIME" DATE, 
	"PARAMS" VARCHAR2(1000)
   ) ;
--------------------------------------------------------
--  DDL for Index EI_LOOKUP_STRINGS_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "EI_LOOKUP_STRINGS_PK" ON "SC_LOOKUP_STRINGS" ("GEN_ID") 
  ;
--------------------------------------------------------
--  DDL for Index EI_REPORT_DATA_MAPPING_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "EI_REPORT_DATA_MAPPING_PK" ON "SC_REPORT_DATA_MAPPING" ("MAP_ID") 
  ;
--------------------------------------------------------
--  DDL for Index EI_REPORT_RUN_HISTORY_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "EI_REPORT_RUN_HISTORY_PK" ON "SC_REPORT_RUN_HISTORY" ("ID") 
  ;
--------------------------------------------------------
--  DDL for Index EI_REPORT_SETUP_DTL_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "EI_REPORT_SETUP_DTL_PK" ON "SC_REPORT_SETUP_DTL" ("EI_REP_DTL_ID") 
  ;
--------------------------------------------------------
--  DDL for Index EI_REPORT_SETUP_HEADER_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "EI_REPORT_SETUP_HEADER_PK" ON "SC_REPORT_SETUP_HEADER" ("EI_REP_HEADER_ID") 
  ;
--------------------------------------------------------
--  DDL for Index EI_SCHEDULER_DATE_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "EI_SCHEDULER_DATE_PK" ON "SC_SCHEDULER_DATE" ("SCHEDULER_DATE_ID") 
  ;
--------------------------------------------------------
--  DDL for Index EI_SCHEDULER_SETUP_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "EI_SCHEDULER_SETUP_PK" ON "SC_SCHEDULER_SETUP" ("SCHEDULER_ID") 
  ;
--------------------------------------------------------
--  DDL for Procedure ADD_UPDATE_MAPPING_TYPE
--------------------------------------------------------

--------------------------------------------------------
--  Constraints for Table SC_LOOKUP_STRINGS
--------------------------------------------------------

  ALTER TABLE "SC_LOOKUP_STRINGS" ADD CONSTRAINT "SC_LOOKUP_STRINGS_PK" PRIMARY KEY ("GEN_ID")
  USING INDEX (CREATE UNIQUE INDEX "EI_LOOKUP_STRINGS_PK" ON "SC_LOOKUP_STRINGS" ("GEN_ID") 
  )  ENABLE;
--------------------------------------------------------
--  Constraints for Table SC_REPORT_DATA_MAPPING
--------------------------------------------------------

  ALTER TABLE "SC_REPORT_DATA_MAPPING" MODIFY ("MAP_ID" NOT NULL ENABLE);
  ALTER TABLE "SC_REPORT_DATA_MAPPING" ADD CONSTRAINT "SC_REPORT_DATA_MAPPING_PK" PRIMARY KEY ("MAP_ID")
  USING INDEX (CREATE UNIQUE INDEX "EI_REPORT_DATA_MAPPING_PK" ON "SC_REPORT_DATA_MAPPING" ("MAP_ID") 
  )  ENABLE;
--------------------------------------------------------
--  Constraints for Table SC_REPORT_RUN_HISTORY
--------------------------------------------------------

  ALTER TABLE "SC_REPORT_RUN_HISTORY" ADD CONSTRAINT "SC_REPORT_RUN_HISTORY_PK" PRIMARY KEY ("ID")
  USING INDEX (CREATE UNIQUE INDEX "EI_REPORT_RUN_HISTORY_PK" ON "SC_REPORT_RUN_HISTORY" ("ID") 
  )  ENABLE;
--------------------------------------------------------
--  Constraints for Table SC_REPORT_SETUP_DTL
--------------------------------------------------------

  ALTER TABLE "SC_REPORT_SETUP_DTL" ADD CONSTRAINT "SC_REPORT_SETUP_DTL_PK" PRIMARY KEY ("EI_REP_DTL_ID")
  USING INDEX (CREATE UNIQUE INDEX "EI_REPORT_SETUP_DTL_PK" ON "SC_REPORT_SETUP_DTL" ("EI_REP_DTL_ID") 
  )  ENABLE;
--------------------------------------------------------
--  Constraints for Table SC_REPORT_SETUP_HEADER
--------------------------------------------------------

  ALTER TABLE "SC_REPORT_SETUP_HEADER" ADD CONSTRAINT "SC_REPORT_SETUP_HEADER_PK" PRIMARY KEY ("EI_REP_HEADER_ID")
  USING INDEX (CREATE UNIQUE INDEX "EI_REPORT_SETUP_HEADER_PK" ON "SC_REPORT_SETUP_HEADER" ("EI_REP_HEADER_ID") 
  )  ENABLE;
--------------------------------------------------------
--  Constraints for Table SC_SCHEDULER_DATE
--------------------------------------------------------

  ALTER TABLE "SC_SCHEDULER_DATE" ADD CONSTRAINT "SC_SCHEDULER_DATE_PK" PRIMARY KEY ("SCHEDULER_DATE_ID")
  USING INDEX (CREATE UNIQUE INDEX "EI_SCHEDULER_DATE_PK" ON "SC_SCHEDULER_DATE" ("SCHEDULER_DATE_ID") 
  )  ENABLE;
--------------------------------------------------------
--  Constraints for Table SC_SCHEDULER_SETUP
--------------------------------------------------------

  ALTER TABLE "SC_SCHEDULER_SETUP" ADD CONSTRAINT "SC_SCHEDULER_SETUP_PK" PRIMARY KEY ("SCHEDULER_ID")
  USING INDEX (CREATE UNIQUE INDEX "EI_SCHEDULER_SETUP_PK" ON "SC_SCHEDULER_SETUP" ("SCHEDULER_ID") 
  )  ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table SC_REPORT_DATA_MAPPING
--------------------------------------------------------

  ALTER TABLE "SC_REPORT_DATA_MAPPING" ADD CONSTRAINT "SC_REPORT_DATA_MAPPING_R01" FOREIGN KEY ("REPORT_HEADER_ID")
	  REFERENCES "SC_REPORT_SETUP_HEADER" ("EI_REP_HEADER_ID") ON DELETE CASCADE ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table SC_REPORT_RUN_HISTORY
--------------------------------------------------------

  ALTER TABLE "SC_REPORT_RUN_HISTORY" ADD CONSTRAINT "SC_REPORT_RUN_HISTORY_R01" FOREIGN KEY ("SCHEDULER_ID")
	  REFERENCES "SC_SCHEDULER_SETUP" ("SCHEDULER_ID") ON DELETE CASCADE ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table SC_REPORT_SETUP_DTL
--------------------------------------------------------

  ALTER TABLE "SC_REPORT_SETUP_DTL" ADD CONSTRAINT "SC_REPORT_SETUP_DTL_R01" FOREIGN KEY ("EI_REP_HEADER_ID")
	  REFERENCES "SC_REPORT_SETUP_HEADER" ("EI_REP_HEADER_ID") ON DELETE CASCADE ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table SC_REPORT_SETUP_HEADER
--------------------------------------------------------

  ALTER TABLE "SC_REPORT_SETUP_HEADER" ADD CONSTRAINT "SC_REPORT_SETUP_HEADER_R01" FOREIGN KEY ("SCHEDULER_ID")
	  REFERENCES "SC_SCHEDULER_SETUP" ("SCHEDULER_ID") ON DELETE CASCADE ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table SC_SCHEDULER_DATE
--------------------------------------------------------

  ALTER TABLE "SC_SCHEDULER_DATE" ADD CONSTRAINT "SC_SCHEDULER_DATE_R01" FOREIGN KEY ("SCHEDULER_ID")
	  REFERENCES "SC_SCHEDULER_SETUP" ("SCHEDULER_ID") ENABLE;
	 
	 
------ Add Data to Lookup table -------



Insert into SC_LOOKUP_STRINGS (GEN_ID,TYPE,CODE,VALUE,ENT_DATE,ENT_BY,MODIFY_BY,MODIFY_DATE,ENABLE_FLAG) values (1,'SCHEDULER_TYPE','F','Frequency',null,null,null,null,'Y');
Insert into SC_LOOKUP_STRINGS (GEN_ID,TYPE,CODE,VALUE,ENT_DATE,ENT_BY,MODIFY_BY,MODIFY_DATE,ENABLE_FLAG) values (2,'SCHEDULER_TYPE','T','Time',null,null,null,null,'Y');
Insert into SC_LOOKUP_STRINGS (GEN_ID,TYPE,CODE,VALUE,ENT_DATE,ENT_BY,MODIFY_BY,MODIFY_DATE,ENABLE_FLAG) values (3,'STATUS','R','Running',null,null,null,null,'Y');
Insert into SC_LOOKUP_STRINGS (GEN_ID,TYPE,CODE,VALUE,ENT_DATE,ENT_BY,MODIFY_BY,MODIFY_DATE,ENABLE_FLAG) values (4,'STATUS','E','Error',null,null,null,null,'Y');
Insert into SC_LOOKUP_STRINGS (GEN_ID,TYPE,CODE,VALUE,ENT_DATE,ENT_BY,MODIFY_BY,MODIFY_DATE,ENABLE_FLAG) values (5,'STATUS','C','Completed',null,null,null,null,'Y');
Insert into SC_LOOKUP_STRINGS (GEN_ID,TYPE,CODE,VALUE,ENT_DATE,ENT_BY,MODIFY_BY,MODIFY_DATE,ENABLE_FLAG) values (6,'STATUS','S','Stoped',null,null,null,null,'Y');
Insert into SC_LOOKUP_STRINGS (GEN_ID,TYPE,CODE,VALUE,ENT_DATE,ENT_BY,MODIFY_BY,MODIFY_DATE,ENABLE_FLAG) values (7,'Y_N','Y','Yes',null,null,null,null,'Y');
Insert into SC_LOOKUP_STRINGS (GEN_ID,TYPE,CODE,VALUE,ENT_DATE,ENT_BY,MODIFY_BY,MODIFY_DATE,ENABLE_FLAG) values (8,'Y_N','N','No',null,null,null,null,'Y');
Insert into SC_LOOKUP_STRINGS (GEN_ID,TYPE,CODE,VALUE,ENT_DATE,ENT_BY,MODIFY_BY,MODIFY_DATE,ENABLE_FLAG) values (9,'HOUR','0','0',null,null,null,null,'Y');
Insert into SC_LOOKUP_STRINGS (GEN_ID,TYPE,CODE,VALUE,ENT_DATE,ENT_BY,MODIFY_BY,MODIFY_DATE,ENABLE_FLAG) values (10,'HOUR','1','1',null,null,null,null,'Y');
Insert into SC_LOOKUP_STRINGS (GEN_ID,TYPE,CODE,VALUE,ENT_DATE,ENT_BY,MODIFY_BY,MODIFY_DATE,ENABLE_FLAG) values (11,'HOUR','2','2',null,null,null,null,'Y');
Insert into SC_LOOKUP_STRINGS (GEN_ID,TYPE,CODE,VALUE,ENT_DATE,ENT_BY,MODIFY_BY,MODIFY_DATE,ENABLE_FLAG) values (12,'HOUR','3','3',null,null,null,null,'Y');
Insert into SC_LOOKUP_STRINGS (GEN_ID,TYPE,CODE,VALUE,ENT_DATE,ENT_BY,MODIFY_BY,MODIFY_DATE,ENABLE_FLAG) values (13,'HOUR','4','4',null,null,null,null,'Y');
Insert into SC_LOOKUP_STRINGS (GEN_ID,TYPE,CODE,VALUE,ENT_DATE,ENT_BY,MODIFY_BY,MODIFY_DATE,ENABLE_FLAG) values (14,'HOUR','5','5',null,null,null,null,'Y');
Insert into SC_LOOKUP_STRINGS (GEN_ID,TYPE,CODE,VALUE,ENT_DATE,ENT_BY,MODIFY_BY,MODIFY_DATE,ENABLE_FLAG) values (15,'HOUR','6','6',null,null,null,null,'Y');
Insert into SC_LOOKUP_STRINGS (GEN_ID,TYPE,CODE,VALUE,ENT_DATE,ENT_BY,MODIFY_BY,MODIFY_DATE,ENABLE_FLAG) values (16,'HOUR','7','7',null,null,null,null,'Y');
Insert into SC_LOOKUP_STRINGS (GEN_ID,TYPE,CODE,VALUE,ENT_DATE,ENT_BY,MODIFY_BY,MODIFY_DATE,ENABLE_FLAG) values (17,'HOUR','8','8',null,null,null,null,'Y');
Insert into SC_LOOKUP_STRINGS (GEN_ID,TYPE,CODE,VALUE,ENT_DATE,ENT_BY,MODIFY_BY,MODIFY_DATE,ENABLE_FLAG) values (18,'HOUR','9','9',null,null,null,null,'Y');
Insert into SC_LOOKUP_STRINGS (GEN_ID,TYPE,CODE,VALUE,ENT_DATE,ENT_BY,MODIFY_BY,MODIFY_DATE,ENABLE_FLAG) values (19,'HOUR','10','10',null,null,null,null,'Y');
Insert into SC_LOOKUP_STRINGS (GEN_ID,TYPE,CODE,VALUE,ENT_DATE,ENT_BY,MODIFY_BY,MODIFY_DATE,ENABLE_FLAG) values (20,'HOUR','11','11',null,null,null,null,'Y');
Insert into SC_LOOKUP_STRINGS (GEN_ID,TYPE,CODE,VALUE,ENT_DATE,ENT_BY,MODIFY_BY,MODIFY_DATE,ENABLE_FLAG) values (21,'HOUR','12','12',null,null,null,null,'Y');
Insert into SC_LOOKUP_STRINGS (GEN_ID,TYPE,CODE,VALUE,ENT_DATE,ENT_BY,MODIFY_BY,MODIFY_DATE,ENABLE_FLAG) values (22,'HOUR','13','13',null,null,null,null,'Y');
Insert into SC_LOOKUP_STRINGS (GEN_ID,TYPE,CODE,VALUE,ENT_DATE,ENT_BY,MODIFY_BY,MODIFY_DATE,ENABLE_FLAG) values (23,'HOUR','14','14',null,null,null,null,'Y');
Insert into SC_LOOKUP_STRINGS (GEN_ID,TYPE,CODE,VALUE,ENT_DATE,ENT_BY,MODIFY_BY,MODIFY_DATE,ENABLE_FLAG) values (24,'HOUR','15','15',null,null,null,null,'Y');
Insert into SC_LOOKUP_STRINGS (GEN_ID,TYPE,CODE,VALUE,ENT_DATE,ENT_BY,MODIFY_BY,MODIFY_DATE,ENABLE_FLAG) values (25,'HOUR','16','16',null,null,null,null,'Y');
Insert into SC_LOOKUP_STRINGS (GEN_ID,TYPE,CODE,VALUE,ENT_DATE,ENT_BY,MODIFY_BY,MODIFY_DATE,ENABLE_FLAG) values (26,'HOUR','17','17',null,null,null,null,'Y');
Insert into SC_LOOKUP_STRINGS (GEN_ID,TYPE,CODE,VALUE,ENT_DATE,ENT_BY,MODIFY_BY,MODIFY_DATE,ENABLE_FLAG) values (27,'HOUR','18','18',null,null,null,null,'Y');
Insert into SC_LOOKUP_STRINGS (GEN_ID,TYPE,CODE,VALUE,ENT_DATE,ENT_BY,MODIFY_BY,MODIFY_DATE,ENABLE_FLAG) values (28,'HOUR','19','19',null,null,null,null,'Y');
Insert into SC_LOOKUP_STRINGS (GEN_ID,TYPE,CODE,VALUE,ENT_DATE,ENT_BY,MODIFY_BY,MODIFY_DATE,ENABLE_FLAG) values (29,'HOUR','20','20',null,null,null,null,'Y');
Insert into SC_LOOKUP_STRINGS (GEN_ID,TYPE,CODE,VALUE,ENT_DATE,ENT_BY,MODIFY_BY,MODIFY_DATE,ENABLE_FLAG) values (30,'HOUR','21','21',null,null,null,null,'Y');
Insert into SC_LOOKUP_STRINGS (GEN_ID,TYPE,CODE,VALUE,ENT_DATE,ENT_BY,MODIFY_BY,MODIFY_DATE,ENABLE_FLAG) values (31,'HOUR','22','22',null,null,null,null,'Y');
Insert into SC_LOOKUP_STRINGS (GEN_ID,TYPE,CODE,VALUE,ENT_DATE,ENT_BY,MODIFY_BY,MODIFY_DATE,ENABLE_FLAG) values (32,'HOUR','23','23',null,null,null,null,'Y');
Insert into SC_LOOKUP_STRINGS (GEN_ID,TYPE,CODE,VALUE,ENT_DATE,ENT_BY,MODIFY_BY,MODIFY_DATE,ENABLE_FLAG) values (33,'HOUR','24','24',null,null,null,null,'Y');
Insert into SC_LOOKUP_STRINGS (GEN_ID,TYPE,CODE,VALUE,ENT_DATE,ENT_BY,MODIFY_BY,MODIFY_DATE,ENABLE_FLAG) values (34,'MIN_SEC','0','0',null,null,null,null,'Y');
Insert into SC_LOOKUP_STRINGS (GEN_ID,TYPE,CODE,VALUE,ENT_DATE,ENT_BY,MODIFY_BY,MODIFY_DATE,ENABLE_FLAG) values (35,'MIN_SEC','1','1',null,null,null,null,'Y');
Insert into SC_LOOKUP_STRINGS (GEN_ID,TYPE,CODE,VALUE,ENT_DATE,ENT_BY,MODIFY_BY,MODIFY_DATE,ENABLE_FLAG) values (36,'MIN_SEC','2','2',null,null,null,null,'Y');
Insert into SC_LOOKUP_STRINGS (GEN_ID,TYPE,CODE,VALUE,ENT_DATE,ENT_BY,MODIFY_BY,MODIFY_DATE,ENABLE_FLAG) values (37,'MIN_SEC','3','3',null,null,null,null,'Y');
Insert into SC_LOOKUP_STRINGS (GEN_ID,TYPE,CODE,VALUE,ENT_DATE,ENT_BY,MODIFY_BY,MODIFY_DATE,ENABLE_FLAG) values (38,'MIN_SEC','4','4',null,null,null,null,'Y');
Insert into SC_LOOKUP_STRINGS (GEN_ID,TYPE,CODE,VALUE,ENT_DATE,ENT_BY,MODIFY_BY,MODIFY_DATE,ENABLE_FLAG) values (39,'MIN_SEC','5','5',null,null,null,null,'Y');
Insert into SC_LOOKUP_STRINGS (GEN_ID,TYPE,CODE,VALUE,ENT_DATE,ENT_BY,MODIFY_BY,MODIFY_DATE,ENABLE_FLAG) values (40,'MIN_SEC','6','6',null,null,null,null,'Y');
Insert into SC_LOOKUP_STRINGS (GEN_ID,TYPE,CODE,VALUE,ENT_DATE,ENT_BY,MODIFY_BY,MODIFY_DATE,ENABLE_FLAG) values (41,'MIN_SEC','7','7',null,null,null,null,'Y');
Insert into SC_LOOKUP_STRINGS (GEN_ID,TYPE,CODE,VALUE,ENT_DATE,ENT_BY,MODIFY_BY,MODIFY_DATE,ENABLE_FLAG) values (42,'MIN_SEC','8','8',null,null,null,null,'Y');
Insert into SC_LOOKUP_STRINGS (GEN_ID,TYPE,CODE,VALUE,ENT_DATE,ENT_BY,MODIFY_BY,MODIFY_DATE,ENABLE_FLAG) values (43,'MIN_SEC','9','9',null,null,null,null,'Y');
Insert into SC_LOOKUP_STRINGS (GEN_ID,TYPE,CODE,VALUE,ENT_DATE,ENT_BY,MODIFY_BY,MODIFY_DATE,ENABLE_FLAG) values (44,'MIN_SEC','10','10',null,null,null,null,'Y');
Insert into SC_LOOKUP_STRINGS (GEN_ID,TYPE,CODE,VALUE,ENT_DATE,ENT_BY,MODIFY_BY,MODIFY_DATE,ENABLE_FLAG) values (45,'MIN_SEC','11','11',null,null,null,null,'Y');
Insert into SC_LOOKUP_STRINGS (GEN_ID,TYPE,CODE,VALUE,ENT_DATE,ENT_BY,MODIFY_BY,MODIFY_DATE,ENABLE_FLAG) values (46,'MIN_SEC','12','12',null,null,null,null,'Y');
Insert into SC_LOOKUP_STRINGS (GEN_ID,TYPE,CODE,VALUE,ENT_DATE,ENT_BY,MODIFY_BY,MODIFY_DATE,ENABLE_FLAG) values (47,'MIN_SEC','13','13',null,null,null,null,'Y');
Insert into SC_LOOKUP_STRINGS (GEN_ID,TYPE,CODE,VALUE,ENT_DATE,ENT_BY,MODIFY_BY,MODIFY_DATE,ENABLE_FLAG) values (48,'MIN_SEC','14','14',null,null,null,null,'Y');
Insert into SC_LOOKUP_STRINGS (GEN_ID,TYPE,CODE,VALUE,ENT_DATE,ENT_BY,MODIFY_BY,MODIFY_DATE,ENABLE_FLAG) values (49,'MIN_SEC','15','15',null,null,null,null,'Y');
Insert into SC_LOOKUP_STRINGS (GEN_ID,TYPE,CODE,VALUE,ENT_DATE,ENT_BY,MODIFY_BY,MODIFY_DATE,ENABLE_FLAG) values (50,'MIN_SEC','16','16',null,null,null,null,'Y');
Insert into SC_LOOKUP_STRINGS (GEN_ID,TYPE,CODE,VALUE,ENT_DATE,ENT_BY,MODIFY_BY,MODIFY_DATE,ENABLE_FLAG) values (51,'MIN_SEC','17','17',null,null,null,null,'Y');
Insert into SC_LOOKUP_STRINGS (GEN_ID,TYPE,CODE,VALUE,ENT_DATE,ENT_BY,MODIFY_BY,MODIFY_DATE,ENABLE_FLAG) values (52,'MIN_SEC','18','18',null,null,null,null,'Y');
Insert into SC_LOOKUP_STRINGS (GEN_ID,TYPE,CODE,VALUE,ENT_DATE,ENT_BY,MODIFY_BY,MODIFY_DATE,ENABLE_FLAG) values (53,'MIN_SEC','19','19',null,null,null,null,'Y');
Insert into SC_LOOKUP_STRINGS (GEN_ID,TYPE,CODE,VALUE,ENT_DATE,ENT_BY,MODIFY_BY,MODIFY_DATE,ENABLE_FLAG) values (54,'MIN_SEC','20','20',null,null,null,null,'Y');
Insert into SC_LOOKUP_STRINGS (GEN_ID,TYPE,CODE,VALUE,ENT_DATE,ENT_BY,MODIFY_BY,MODIFY_DATE,ENABLE_FLAG) values (55,'MIN_SEC','21','21',null,null,null,null,'Y');
Insert into SC_LOOKUP_STRINGS (GEN_ID,TYPE,CODE,VALUE,ENT_DATE,ENT_BY,MODIFY_BY,MODIFY_DATE,ENABLE_FLAG) values (56,'MIN_SEC','22','22',null,null,null,null,'Y');
Insert into SC_LOOKUP_STRINGS (GEN_ID,TYPE,CODE,VALUE,ENT_DATE,ENT_BY,MODIFY_BY,MODIFY_DATE,ENABLE_FLAG) values (57,'MIN_SEC','23','23',null,null,null,null,'Y');
Insert into SC_LOOKUP_STRINGS (GEN_ID,TYPE,CODE,VALUE,ENT_DATE,ENT_BY,MODIFY_BY,MODIFY_DATE,ENABLE_FLAG) values (58,'MIN_SEC','24','24',null,null,null,null,'Y');
Insert into SC_LOOKUP_STRINGS (GEN_ID,TYPE,CODE,VALUE,ENT_DATE,ENT_BY,MODIFY_BY,MODIFY_DATE,ENABLE_FLAG) values (59,'MIN_SEC','25','25',null,null,null,null,'Y');
Insert into SC_LOOKUP_STRINGS (GEN_ID,TYPE,CODE,VALUE,ENT_DATE,ENT_BY,MODIFY_BY,MODIFY_DATE,ENABLE_FLAG) values (60,'MIN_SEC','26','26',null,null,null,null,'Y');
Insert into SC_LOOKUP_STRINGS (GEN_ID,TYPE,CODE,VALUE,ENT_DATE,ENT_BY,MODIFY_BY,MODIFY_DATE,ENABLE_FLAG) values (61,'MIN_SEC','27','27',null,null,null,null,'Y');
Insert into SC_LOOKUP_STRINGS (GEN_ID,TYPE,CODE,VALUE,ENT_DATE,ENT_BY,MODIFY_BY,MODIFY_DATE,ENABLE_FLAG) values (62,'MIN_SEC','28','28',null,null,null,null,'Y');
Insert into SC_LOOKUP_STRINGS (GEN_ID,TYPE,CODE,VALUE,ENT_DATE,ENT_BY,MODIFY_BY,MODIFY_DATE,ENABLE_FLAG) values (63,'MIN_SEC','29','29',null,null,null,null,'Y');
Insert into SC_LOOKUP_STRINGS (GEN_ID,TYPE,CODE,VALUE,ENT_DATE,ENT_BY,MODIFY_BY,MODIFY_DATE,ENABLE_FLAG) values (64,'MIN_SEC','30','30',null,null,null,null,'Y');
Insert into SC_LOOKUP_STRINGS (GEN_ID,TYPE,CODE,VALUE,ENT_DATE,ENT_BY,MODIFY_BY,MODIFY_DATE,ENABLE_FLAG) values (65,'MIN_SEC','31','31',null,null,null,null,'Y');
Insert into SC_LOOKUP_STRINGS (GEN_ID,TYPE,CODE,VALUE,ENT_DATE,ENT_BY,MODIFY_BY,MODIFY_DATE,ENABLE_FLAG) values (66,'MIN_SEC','32','32',null,null,null,null,'Y');
Insert into SC_LOOKUP_STRINGS (GEN_ID,TYPE,CODE,VALUE,ENT_DATE,ENT_BY,MODIFY_BY,MODIFY_DATE,ENABLE_FLAG) values (67,'MIN_SEC','33','33',null,null,null,null,'Y');
Insert into SC_LOOKUP_STRINGS (GEN_ID,TYPE,CODE,VALUE,ENT_DATE,ENT_BY,MODIFY_BY,MODIFY_DATE,ENABLE_FLAG) values (68,'MIN_SEC','34','34',null,null,null,null,'Y');
Insert into SC_LOOKUP_STRINGS (GEN_ID,TYPE,CODE,VALUE,ENT_DATE,ENT_BY,MODIFY_BY,MODIFY_DATE,ENABLE_FLAG) values (69,'MIN_SEC','35','35',null,null,null,null,'Y');
Insert into SC_LOOKUP_STRINGS (GEN_ID,TYPE,CODE,VALUE,ENT_DATE,ENT_BY,MODIFY_BY,MODIFY_DATE,ENABLE_FLAG) values (70,'MIN_SEC','36','36',null,null,null,null,'Y');
Insert into SC_LOOKUP_STRINGS (GEN_ID,TYPE,CODE,VALUE,ENT_DATE,ENT_BY,MODIFY_BY,MODIFY_DATE,ENABLE_FLAG) values (71,'MIN_SEC','37','37',null,null,null,null,'Y');
Insert into SC_LOOKUP_STRINGS (GEN_ID,TYPE,CODE,VALUE,ENT_DATE,ENT_BY,MODIFY_BY,MODIFY_DATE,ENABLE_FLAG) values (72,'MIN_SEC','38','38',null,null,null,null,'Y');
Insert into SC_LOOKUP_STRINGS (GEN_ID,TYPE,CODE,VALUE,ENT_DATE,ENT_BY,MODIFY_BY,MODIFY_DATE,ENABLE_FLAG) values (73,'MIN_SEC','39','39',null,null,null,null,'Y');
Insert into SC_LOOKUP_STRINGS (GEN_ID,TYPE,CODE,VALUE,ENT_DATE,ENT_BY,MODIFY_BY,MODIFY_DATE,ENABLE_FLAG) values (74,'MIN_SEC','40','40',null,null,null,null,'Y');
Insert into SC_LOOKUP_STRINGS (GEN_ID,TYPE,CODE,VALUE,ENT_DATE,ENT_BY,MODIFY_BY,MODIFY_DATE,ENABLE_FLAG) values (75,'MIN_SEC','41','41',null,null,null,null,'Y');
Insert into SC_LOOKUP_STRINGS (GEN_ID,TYPE,CODE,VALUE,ENT_DATE,ENT_BY,MODIFY_BY,MODIFY_DATE,ENABLE_FLAG) values (76,'MIN_SEC','42','42',null,null,null,null,'Y');
Insert into SC_LOOKUP_STRINGS (GEN_ID,TYPE,CODE,VALUE,ENT_DATE,ENT_BY,MODIFY_BY,MODIFY_DATE,ENABLE_FLAG) values (77,'MIN_SEC','43','43',null,null,null,null,'Y');
Insert into SC_LOOKUP_STRINGS (GEN_ID,TYPE,CODE,VALUE,ENT_DATE,ENT_BY,MODIFY_BY,MODIFY_DATE,ENABLE_FLAG) values (78,'MIN_SEC','44','44',null,null,null,null,'Y');
Insert into SC_LOOKUP_STRINGS (GEN_ID,TYPE,CODE,VALUE,ENT_DATE,ENT_BY,MODIFY_BY,MODIFY_DATE,ENABLE_FLAG) values (79,'MIN_SEC','45','45',null,null,null,null,'Y');
Insert into SC_LOOKUP_STRINGS (GEN_ID,TYPE,CODE,VALUE,ENT_DATE,ENT_BY,MODIFY_BY,MODIFY_DATE,ENABLE_FLAG) values (80,'MIN_SEC','46','46',null,null,null,null,'Y');
Insert into SC_LOOKUP_STRINGS (GEN_ID,TYPE,CODE,VALUE,ENT_DATE,ENT_BY,MODIFY_BY,MODIFY_DATE,ENABLE_FLAG) values (81,'MIN_SEC','47','47',null,null,null,null,'Y');
Insert into SC_LOOKUP_STRINGS (GEN_ID,TYPE,CODE,VALUE,ENT_DATE,ENT_BY,MODIFY_BY,MODIFY_DATE,ENABLE_FLAG) values (82,'MIN_SEC','48','48',null,null,null,null,'Y');
Insert into SC_LOOKUP_STRINGS (GEN_ID,TYPE,CODE,VALUE,ENT_DATE,ENT_BY,MODIFY_BY,MODIFY_DATE,ENABLE_FLAG) values (83,'MIN_SEC','49','49',null,null,null,null,'Y');
Insert into SC_LOOKUP_STRINGS (GEN_ID,TYPE,CODE,VALUE,ENT_DATE,ENT_BY,MODIFY_BY,MODIFY_DATE,ENABLE_FLAG) values (84,'MIN_SEC','50','50',null,null,null,null,'Y');
Insert into SC_LOOKUP_STRINGS (GEN_ID,TYPE,CODE,VALUE,ENT_DATE,ENT_BY,MODIFY_BY,MODIFY_DATE,ENABLE_FLAG) values (85,'MIN_SEC','51','51',null,null,null,null,'Y');
Insert into SC_LOOKUP_STRINGS (GEN_ID,TYPE,CODE,VALUE,ENT_DATE,ENT_BY,MODIFY_BY,MODIFY_DATE,ENABLE_FLAG) values (86,'MIN_SEC','52','52',null,null,null,null,'Y');
Insert into SC_LOOKUP_STRINGS (GEN_ID,TYPE,CODE,VALUE,ENT_DATE,ENT_BY,MODIFY_BY,MODIFY_DATE,ENABLE_FLAG) values (87,'MIN_SEC','53','53',null,null,null,null,'Y');
Insert into SC_LOOKUP_STRINGS (GEN_ID,TYPE,CODE,VALUE,ENT_DATE,ENT_BY,MODIFY_BY,MODIFY_DATE,ENABLE_FLAG) values (88,'MIN_SEC','54','54',null,null,null,null,'Y');
Insert into SC_LOOKUP_STRINGS (GEN_ID,TYPE,CODE,VALUE,ENT_DATE,ENT_BY,MODIFY_BY,MODIFY_DATE,ENABLE_FLAG) values (89,'MIN_SEC','55','55',null,null,null,null,'Y');
Insert into SC_LOOKUP_STRINGS (GEN_ID,TYPE,CODE,VALUE,ENT_DATE,ENT_BY,MODIFY_BY,MODIFY_DATE,ENABLE_FLAG) values (90,'MIN_SEC','56','56',null,null,null,null,'Y');
Insert into SC_LOOKUP_STRINGS (GEN_ID,TYPE,CODE,VALUE,ENT_DATE,ENT_BY,MODIFY_BY,MODIFY_DATE,ENABLE_FLAG) values (91,'MIN_SEC','57','57',null,null,null,null,'Y');
Insert into SC_LOOKUP_STRINGS (GEN_ID,TYPE,CODE,VALUE,ENT_DATE,ENT_BY,MODIFY_BY,MODIFY_DATE,ENABLE_FLAG) values (92,'MIN_SEC','58','58',null,null,null,null,'Y');
Insert into SC_LOOKUP_STRINGS (GEN_ID,TYPE,CODE,VALUE,ENT_DATE,ENT_BY,MODIFY_BY,MODIFY_DATE,ENABLE_FLAG) values (93,'MIN_SEC','59','59',null,null,null,null,'Y');
Insert into SC_LOOKUP_STRINGS (GEN_ID,TYPE,CODE,VALUE,ENT_DATE,ENT_BY,MODIFY_BY,MODIFY_DATE,ENABLE_FLAG) values (94,'MIN_SEC','60','60',null,null,null,null,'Y');
Insert into SC_LOOKUP_STRINGS (GEN_ID,TYPE,CODE,VALUE,ENT_DATE,ENT_BY,MODIFY_BY,MODIFY_DATE,ENABLE_FLAG) values (95,'DATA_FORMAT','csv','csv',null,null,null,null,'Y');
Insert into SC_LOOKUP_STRINGS (GEN_ID,TYPE,CODE,VALUE,ENT_DATE,ENT_BY,MODIFY_BY,MODIFY_DATE,ENABLE_FLAG) values (96,'DATA_FORMAT','xml','XML',null,null,null,null,'Y');
Insert into SC_LOOKUP_STRINGS (GEN_ID,TYPE,CODE,VALUE,ENT_DATE,ENT_BY,MODIFY_BY,MODIFY_DATE,ENABLE_FLAG) values (97,'DATA_LOCALE','en-US','en-US',null,null,null,null,'Y');
Insert into SC_LOOKUP_STRINGS (GEN_ID,TYPE,CODE,VALUE,ENT_DATE,ENT_BY,MODIFY_BY,MODIFY_DATE,ENABLE_FLAG) values (98,'DELIMITER',',','COMMA (,)',null,null,null,null,'Y');
Insert into SC_LOOKUP_STRINGS (GEN_ID,TYPE,CODE,VALUE,ENT_DATE,ENT_BY,MODIFY_BY,MODIFY_DATE,ENABLE_FLAG) values (99,'DELIMITER','''','SINGLE QUOTE ('')',null,null,null,null,'Y');
Insert into SC_LOOKUP_STRINGS (GEN_ID,TYPE,CODE,VALUE,ENT_DATE,ENT_BY,MODIFY_BY,MODIFY_DATE,ENABLE_FLAG) values (100,'DELIMITER','"','DUBLE_QUOTE (")',null,null,null,null,'Y');
Insert into SC_LOOKUP_STRINGS (GEN_ID,TYPE,CODE,VALUE,ENT_DATE,ENT_BY,MODIFY_BY,MODIFY_DATE,ENABLE_FLAG) values (101,'DELIMITER','?','QUESTION_MARK (?)',null,null,null,null,'Y');

