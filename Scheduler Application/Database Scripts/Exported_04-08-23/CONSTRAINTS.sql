--------------------------------------------------------
--  Constraints for Table XX_SCH_EXCEPTION_LOG
--------------------------------------------------------

  ALTER TABLE "XX_SCH_EXCEPTION_LOG" ADD PRIMARY KEY ("EXCEPTION_ID")
  USING INDEX  ENABLE
--------------------------------------------------------
--  Constraints for Table XX_SCH_REPORT_DATA_MAPPING
--------------------------------------------------------

  ALTER TABLE "XX_SCH_REPORT_DATA_MAPPING" MODIFY ("MAP_ID" NOT NULL ENABLE)
--------------------------------------------------------
--  Constraints for Table XX_SCH_REPORT_RUN_HIS
--------------------------------------------------------

  ALTER TABLE "XX_SCH_REPORT_RUN_HIS" ADD CONSTRAINT "XX_SCH_REPORT_RUN_HIS_PK" PRIMARY KEY ("ID")
  USING INDEX  ENABLE
--------------------------------------------------------
--  Constraints for Table XX_SCH_REPORT_SETUP_DTL
--------------------------------------------------------

  ALTER TABLE "XX_SCH_REPORT_SETUP_DTL" MODIFY ("EI_REP_DTL_ID" NOT NULL ENABLE)
--------------------------------------------------------
--  Constraints for Table XX_SCH_REPORT_SETUP_HDR
--------------------------------------------------------

  ALTER TABLE "XX_SCH_REPORT_SETUP_HDR" ADD CONSTRAINT "XX_SCH_REPORT_SETUP_HDR_PK" PRIMARY KEY ("EI_REP_HEADER_ID")
  USING INDEX  ENABLE
--------------------------------------------------------
--  Constraints for Table XX_SCH_SCHEDULER_DATE
--------------------------------------------------------

  ALTER TABLE "XX_SCH_SCHEDULER_DATE" MODIFY ("SCHEDULER_DATE_ID" NOT NULL ENABLE)
--------------------------------------------------------
--  Constraints for Table XX_SCH_SCHEDULER_SETUP
--------------------------------------------------------

  ALTER TABLE "XX_SCH_SCHEDULER_SETUP" MODIFY ("SCHEDULER_ID" NOT NULL ENABLE)
