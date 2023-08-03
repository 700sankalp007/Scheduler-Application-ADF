package sc.common.view.backing;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import java.text.SimpleDateFormat;

import java.util.Arrays;
import java.util.Date;
import java.util.TimeZone;

import sc.common.view.util.ConnectionManager;
import sc.common.view.util.ExceptionLog;


public class PhysicalCountExecuterService implements Runnable{
	
    String runHisId = null;
    String responseString = null;
    Connection cn = null;
    public static final String TAG = "PhysicalCountExecutiveService";
    
    public PhysicalCountExecuterService(String responseString, String runHisId) {
        this.responseString = responseString;
        this.runHisId = runHisId;
    }

    @Override
    public void run() {
    try{
//            cn = ConnectionManager.P2TgetConnection();
        cn = ConnectionManager.getConnection();
        //get the Row data into column
        String[] data = getReportData(responseString);
        
        //Check for the Physicalcount Inventory exist in db
        int physicalInventoryId = checkForPhysicalCountInventoryIdExistInMaster(data[15], cn); //data[15] - Physical_Inventory_Id
        if(physicalInventoryId ==  0) {
            //Insert into PHY_TAG_LIST_MST table 
            insertIntoWH360_PHY_TAG_LIST_MST(data, cn);
        }
        
        //Check for the WH360_PHY_TAG_LIST table for the TAG_ID exist
        // If exist, update data else insert
        int tagId = checkForTagIdExistInPhysicalCountLine(data[16], cn);
//        System.out.println("TAG ID:" + tagId + "data[16]:"  + data[16]);
        if(tagId > 0) {
            //Update
            updatePhysicalCountData(data, cn);
        }else {
            //Insert
            insertPhysicalCountData(data, cn);
        }
        cn.commit();
    
    }catch (Exception e){
        e.printStackTrace();
        ExceptionLog.CreateExceptionLog(this.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName(), e, runHisId);
    }finally{
        if(cn!=null){
            try {
                cn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
   }

	private String[] getReportData(String responseString) {
            String[] data = null;
            try {
                String delimiter=",";
                String otherThanQuote = " [^\"] ";
                String quotedString = String.format(" \" %s* \" ", otherThanQuote);
                String regex = String.format("(?x)"+delimiter+"(?=(%s*%s)*%s*$)",otherThanQuote, quotedString, otherThanQuote);
                data=responseString.split(regex,-1);
            } catch (Exception e) {
                System.out.println(TAG + ": getReportData(): " + e.getLocalizedMessage());
                e.printStackTrace();
//                throw e;
            }
            return data;
	}
	
	/**
	 * Check for the PhysicalCount Inventory id exist into WH360_PHY_INV_TAG_LIST_MST table
	 * @param physical_inventory_id
	 * @return
	 * 		PC Inventory count
	 */
	private int checkForPhysicalCountInventoryIdExistInMaster(String physical_inventory_id, Connection cn) throws SQLException {
	     int physicalInventoryCount = 0;
	     String query = "SELECT count(*) AS rowCount FROM WH360_PHY_INV_TAG_LIST_MST where physical_inventory_id = ?";
             try(
	        PreparedStatement pstmt =  cn.prepareStatement(query);) {
                pstmt.setString(1, physical_inventory_id);
                try (ResultSet resultSet = pstmt.executeQuery()) {
                    if(resultSet != null) {
                        while(resultSet.next()) {
                            physicalInventoryCount = resultSet.getInt("rowCount");
                        }
                    }
                } catch (Exception e) {
                    System.out.println(TAG + ": checkForPhysicalCountInventoryIdExistInMaster(): " + e.getLocalizedMessage());
                    e.printStackTrace();
//                    throw e;
                }
            } catch (Exception e) {
                System.out.println(TAG + ": checkForPhysicalCountInventoryIdExistInMaster(): " + e.getLocalizedMessage());
                e.printStackTrace();
//                throw e;
            }
            return physicalInventoryCount;
	}

	
	/**
	 * Insert PC into Master table
	 * @param data
	 */
	private void insertIntoWH360_PHY_TAG_LIST_MST(String[] data, Connection cn) throws SQLException {
            try {
//                for (int i = 0; i < data.length; i++) {
//                    System.out.println(i+ " : " + data[i]);
//                }
            System.out.println(TAG + ": insertIntoWH360_PHY_TAG_LIST_MST(): ");
            String insMstSQL="INSERT INTO WH360_PHY_INV_TAG_LIST_MST\n" + 
                "(ORGANIZATION_CODE,\n" + 
                "ORGANIZATION_ID,\n" + 
                "PHYSICAL_INVENTORY_NAME,\n" + 
                "PHYSICAL_INVENTORY_ID,\n" + 
                "SYNC_STATUS,\n" + 
                "STATUS)VALUES(?,?,?,?,?,?)";
               
            try(PreparedStatement stmt =  cn.prepareStatement(insMstSQL);) {
                    stmt.setString(1, data[11]!=null?data[11]:null);
                    stmt.setString(2, data[12]!=null?data[12]:null);
                    stmt.setString(3, data[14]!=null?data[14].replaceAll("^\"|\"$", ""):null);
                    stmt.setString(4, data[15]!=null?data[15]:null);
                    stmt.setObject(5, null);
                    stmt.setObject(6, null);
                    stmt.executeUpdate();
                } catch (Exception e) {
                    System.out.println(TAG + ": insertIntoWH360_PHY_TAG_LIST_MST(): data[]" + Arrays.toString(data));
                    System.out.println(TAG + ": insertIntoWH360_PHY_TAG_LIST_MST(): " + e.getLocalizedMessage());
                    e.printStackTrace();
//                    throw e;
                }
            
            } catch (Exception e) {
                e.printStackTrace();
//                throw e;
            }
	}
	
	/**
	 * Check for the PC line level table to TAG exist or not
	 * @param tag_id
	 * 	    Tag Id
	 * @return
	 * 	TAG id count
	 */
	private int checkForTagIdExistInPhysicalCountLine(String tag_id, Connection cn) throws SQLException {
         int tagIdCount = 0;
         String query = "SELECT count(TAG_ID) AS tag_id FROM WH360_PHY_INV_TAG_LIST where tag_id = ?";
         try(PreparedStatement pstmt =  cn.prepareStatement(query);) {
                pstmt.setString(1, tag_id);
                try (ResultSet resultSet = pstmt.executeQuery()) {
                    if(resultSet != null) {
                        while(resultSet.next()) {
                            tagIdCount = resultSet.getInt("tag_id");
                        }
                    }
                } catch (Exception e) {
                    System.out.println(TAG + ": checkForTagIdExistInPhysicalCountLine(): " + e.getLocalizedMessage());
                    e.printStackTrace();
//                    throw  e;
                }
            } catch (Exception e) {
                System.out.println(TAG + ": checkForTagIdExistInPhysicalCountLine(): " + e.getLocalizedMessage());
                e.printStackTrace();
//                throw  e;
            }
            return tagIdCount;
	}
	
	/**
	 * Update PC into line level table
	 * @param data
	 */
	private void updatePhysicalCountData(String[] data, Connection cn) throws SQLException, Exception {
            try {
//                System.out.println(TAG + ": updatePhysicalCountData(): ");
                int userId= 0;
	        String updateSQL="UPDATE WH360_PHY_INV_TAG_LIST  \n" + 
                    "        SET TAG_NUMBER=?,  \n" + 
                    "        SUBINVENTORY=?,  \n" + 
                    "        ITEM_NUMBER=?,  \n" + 
                    "        LOCATOR_ID=?,  \n" + 
                    "        LOCATOR=?,  \n" + 
                    "        LOT_NUMBER=?,  \n" + 
                    "        QUANTITY=?,  \n" + 
                    "        ITEM_ID=?,  \n" + 
                    "        ITEM_UOM=?,  \n" + 
                    "        ORGANIZATION_CODE=?,  \n" + 
                    "        ORGANIZATION_ID=?,  \n" + 
                    "        ITEM_DESCRIPTION=?,  \n" + 
                    "        PHYSICAL_INVENTORY_NAME=?,  \n" + 
                    "        PHYSICAL_INVENTORY_ID=?,  \n" + 
                    "        TAG_ID=?,  \n" + 
                    "        ADJUSTMENT_ID=?,  \n" + 
                    "        TAG_UOM=?,  \n" + 
                    "        TAG_UOM_CODE=?,  \n" + 
                    "        TAG_TYPE_CODE=?,  \n" + 
                    "        KEY_VALUE=?,  \n" + 
                    "        UNIQUE_SEQUENCE=?,  \n" + 
                    "        CROSS_REFERENCE=?, \n" +
                    "        SERIAL_NUMBER=?, \n" + 
                    "        REVISION=?, \n" + 
                    "        ITEM_TYPE=?, \n" + 
                    "        UNIT_COST_FD=?, \n" + 
                    "        UNIT_COST_CURRENT=?, \n" + 
                    "        SNAPSHOT_QTY=? \n" + 
                    "        WHERE TAG_ID=?";
                
                String updateSQLDynamicTag="UPDATE WH360_PHY_INV_TAG_LIST  \n" + 
                    "        SET TAG_NUMBER=?,  \n" + 
                    "        SUBINVENTORY=?,  \n" + 
                    "        ITEM_NUMBER=?,  \n" + 
                    "        LOCATOR_ID=?,  \n" + 
                    "        LOCATOR=?,  \n" + 
                    "        LOT_NUMBER=?,  \n" + 
                    "        QUANTITY=?,  \n" + 
                    "        ITEM_ID=?,  \n" + 
                    "        ITEM_UOM=?,  \n" + 
                    "        ORGANIZATION_CODE=?,  \n" + 
                    "        ORGANIZATION_ID=?,  \n" + 
                    "        ITEM_DESCRIPTION=?,  \n" + 
                    "        PHYSICAL_INVENTORY_NAME=?,  \n" + 
                    "        PHYSICAL_INVENTORY_ID=?,  \n" + 
                    "        TAG_ID=?,  \n" + 
                    "        ADJUSTMENT_ID=?,  \n" + 
                    "        TAG_UOM=?,  \n" + 
                    "        TAG_UOM_CODE=?,  \n" + 
                    "        TAG_TYPE_CODE=?,  \n" + 
                    "        KEY_VALUE=?,  \n" + 
                    "        UNIQUE_SEQUENCE=?,  \n" + 
                    "        CROSS_REFERENCE=?, \n" +
                    "        SERIAL_NUMBER=?, \n" + 
                    "        REVISION=?, \n" + 
                    "        ITEM_TYPE=?, \n" + 
                    "        UNIT_COST_FD=?, \n" + 
                    "        UNIT_COST_CURRENT=?, \n" + 
                    "        SNAPSHOT_QTY=?, \n" + 
                    "        MOBILE_QUANTITY=?, \n" + 
                    "        PC_ASSIGNED_USER_ID=?, \n" + 
                    "        STATUS=?, \n" + 
                    "        PC_ASSIGNED_USER_NAME=?, \n" +  
                    "        LAST_UPDATE_DATE=? \n" + 
                    "        WHERE TAG_ID=?";
                String updateStmt = null;
                if(data[20] != null && data[20].equals("3")){ //TAG_TYPE_CODE
                    updateStmt = updateSQLDynamicTag;
                }else{
                    updateStmt = updateSQL;
                }
                
            try(PreparedStatement udPST =  cn.prepareStatement(updateStmt);) {
                udPST.setString(1, data[0]!=null?data[0]:null); // TAG_NUMBER
                udPST.setString(2, data[1]!=null?data[1]:null); //SUB_INV
                udPST.setString(3, data[2]!=null?data[2]:null); //ITEM_NUMBER
                udPST.setString(4, data[4]!=null?data[4]:null); //LOC_ID
                udPST.setString(5, data[5]!=null?data[5]:null); //LOCATOR
                udPST.setString(6, data[6]!=null?data[6]:null); //LOT_NUMBER
                udPST.setString(7, data[8]!=null?data[8]:null); //QUANTITY
                udPST.setString(8, data[9]!=null?data[9]:null); //ITEM_ID
                udPST.setString(9, data[10]!=null?data[10]:null); //ITEM_UOM
                udPST.setString(10, data[11]!=null?data[11]:null); //ORGANIZATION_CODE
                udPST.setString(11, data[12]!=null?data[12]:null); //ORGANIZATION_ID
                udPST.setString(12, data[13]!=null?data[13].replaceAll("^\"|\"$", ""):null); //ITEM_DESCRIPTION
                udPST.setString(13, data[14]!=null?data[14].replaceAll("^\"|\"$", ""):null); //PHYSICAL_INVENTORY_NAME
                udPST.setString(14, data[15]!=null?data[15]:null); //PHYSICAL_INVENTORY_ID
                udPST.setString(15, data[16]!=null?data[16]:null); //TAG_ID
                udPST.setString(16, data[17]!=null?data[17]:null); //ADJUSTMENT_ID
                udPST.setString(17, data[18]!=null?data[18]:null); //TAG_UOM
                udPST.setString(18, data[19]!=null?data[19]:null); //TAG_UOM_CODE
                udPST.setString(19, data[20]!=null?data[20]:null); //TAG_TYPE_CODE
                udPST.setString(20, data[22]!=null?data[22]:null); //KEY
                if(data.length>23&&data[23]!=null) //UNIQUE_SEQUENCE
                    udPST.setString(21, data[23]);
                else
                    udPST.setObject(21, null);
                udPST.setString(22, data[21]!=null?data[21]:null); //CROSS_REFERENCE
//                udPST.setString(34, data[16]!=null?data[16]:null); //TAG_ID unique KEY
                udPST.setString(23, data[7]!=null?data[7]:null); //SERIAL_NUMBER
                udPST.setString(24, data[3]!=null?data[3]:null); //REVISION
                udPST.setString(25, data[26]!=null?data[26]:null); //ITEM_TYPE
                udPST.setObject(26, data[27]!=null?data[27]:null); //UNIT_COST_FD
                udPST.setObject(27, data[28]!=null?data[28]:null); //UNIT_COST_CURRENT
                
                if(data[20] != null && data[20].equals("3")){ //TAG_TYPE_CODE
//                System.out.println(TAG + ": updatePhysicalCountData():1 TAG_TYPE_CODE" + data[20]);
                udPST.setObject(28, 0); //SNAPSHOT_QTY
                    if(data[25] != null){ //COUNTED_BY
                        String userName = data[25];
                        //Get User id from db
                        userId = getUserID(userName.toUpperCase(), cn);
                        udPST.setString(29, data[8]!=null?data[8]:null); //INSERT QUANTITY from REPORT into MOBILE_QUANTITY of Database field
                        if(userId == 0){
                            System.out.println(TAG + ": updatePhysicalCountData():2 TAG_TYPE_CODE" + data[20] + "UserID: " + userId);
                            udPST.setObject(30, null); //PC_ASSIGNED_USER_ID
                            udPST.setObject(32, null); //PC_ASSIGNED_USER_NAME
                        }else{
//                            System.out.println(TAG + ": updatePhysicalCountData():3 TAG_TYPE_CODE" + data[20] + "UserID: " + userId);
                            udPST.setObject(30, userId); //PC_ASSIGNED_USER_ID
                            udPST.setObject(32, userName.toUpperCase()); //PC_ASSIGNED_USER_NAME - COUNTED_BY
                        }
                    }else{
                        System.out.println(TAG + ": updatePhysicalCountData():4 TAG_TYPE_CODE" + data[20] + "PC_ASSIGNED_USER_ID NULL");
                        udPST.setString(29, data[8]!=null?data[8]:null); // MOBILE_QUANTITY - what about here if TAG_TYPE_CODE -3 and COUNTEDby is null?
                        udPST.setObject(30, null); //PC_ASSIGNED_USER_ID
                        udPST.setObject(32, null); //PC_ASSIGNED_USER_NAME
                    }
                    udPST.setString(31, "COUNTED"); // STATUS
                    Date lsatUpdateDate = getLastUpdateDateInUTC();
                    udPST.setTimestamp(33, new Timestamp(lsatUpdateDate.getTime())); //LAST_UPDATE_DATE
                    udPST.setString(34, data[16]!=null?data[16]:null); //TAG_ID unique KEY
                } 
                else{
//                    System.out.println(TAG + ": updatePhysicalCountData():5 TAG_TYPE_CODE" + data[20]);
                    udPST.setObject(28, data[29]!=null?data[29]:null); //SNAPSHOT_QTY
                    udPST.setString(29, data[16]!=null?data[16]:null); //TAG_ID unique KEY
//                    udPST.setString(29, null); //MOBILE_QUANTITY
//                    udPST.setObject(30, null); //PC_ASSIGNED_USER_ID
//                    udPST.setString(31, "PENDING"); // STATUS
//                    udPST.setObject(32, null); //PC_ASSIGNED_USER_NAME
//                    udPST.setTimestamp(33, null); //LAST_UPDATE_DATE
                }
                
                udPST.executeUpdate();
				
                } catch (Exception e) {
                    System.out.println(TAG + ": updatePhysicalCountData(): data[]: " + Arrays.toString(data));
                    e.printStackTrace();
//                    throw  e;
                }
            } catch (Exception e) {
                System.out.println(TAG + ": updatePhysicalCountData(): data[]: " + Arrays.toString(data));
                System.out.println(TAG + ": updatePhysicalCountData(): " + e.getLocalizedMessage());
                e.printStackTrace();
//                throw e;
            }
	}
	
	/**
	 * Insert PC data into line level tabled
	 * @param data
	 */
	private void insertPhysicalCountData(String[] data, Connection cn) throws SQLException, Exception {
		int userId = 0;
//	    System.out.println(TAG + ": insertPhysicalCountData():1 ");
	        String insertSQL="INSERT INTO WH360_PHY_INV_TAG_LIST  \n" + 
                    "        (TAG_NUMBER,  \n" + 
                    "        SUBINVENTORY,  \n" + 
                    "        ITEM_NUMBER,  \n" + 
                    "        LOCATOR_ID,  \n" + 
                    "        LOCATOR,  \n" + 
                    "        LOT_NUMBER,  \n" + 
                    "        QUANTITY,  \n" + 
                    "        ITEM_ID,  \n" + 
                    "        ITEM_UOM,  \n" + 
                    "        ORGANIZATION_CODE,  \n" + 
                    "        ORGANIZATION_ID,  \n" + 
                    "        ITEM_DESCRIPTION,  \n" + 
                    "        PHYSICAL_INVENTORY_NAME,  \n" + 
                    "        PHYSICAL_INVENTORY_ID,  \n" + 
                    "        TAG_ID,  \n" + 
                    "        ADJUSTMENT_ID,  \n" + 
                    "        TAG_UOM,  \n" + 
                    "        TAG_UOM_CODE,  \n" + 
                    "        TAG_TYPE_CODE,  \n" + 
                    "        KEY_VALUE,  \n" + 
                    "        UNIQUE_SEQUENCE,  \n" + 
                    "        CROSS_REFERENCE,STATUS, SERIAL_NUMBER, REVISION, ITEM_TYPE, UNIT_COST_FD, UNIT_COST_CURRENT, SNAPSHOT_QTY, MOBILE_QUANTITY, PC_ASSIGNED_USER_ID, PC_ASSIGNED_USER_NAME, LAST_UPDATE_DATE)\n" + 
                    "        VALUES  \n" + 
                    "        (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
	        
        try(PreparedStatement insPST =  cn.prepareStatement(insertSQL);) {
              insPST.setString(1, data[0]!=null?data[0]:null); // TAG_NUMBER
              insPST.setString(2, data[1]!=null?data[1]:null); //SUB_INV
              insPST.setString(3, data[2]!=null?data[2]:null); //ITEM_NUMBER
              insPST.setString(4, data[4]!=null?data[4]:null); //LOC_ID
              insPST.setString(5, data[5]!=null?data[5]:null); //LOCATOR
              insPST.setString(6, data[6]!=null?data[6]:null); //LOT_NUMBER
              insPST.setString(7, data[8]!=null?data[8]:null); //QUANTITY
              insPST.setString(8, data[9]!=null?data[9]:null); //ITEM_ID
              insPST.setString(9, data[10]!=null?data[10]:null); //ITEM_UOM
              insPST.setString(10, data[11]!=null?data[11]:null); //ORGANIZATION_CODE
              insPST.setString(11, data[12]!=null?data[12]:null); //ORGANIZATION_ID
              insPST.setString(12, data[13]!=null?data[13].replaceAll("^\"|\"$", ""):null); //ITEM_DESCRIPTION
              insPST.setString(13, data[14]!=null?data[14].replaceAll("^\"|\"$", ""):null); //PHYSICAL_INVENTORY_NAME
              insPST.setString(14, data[15]!=null?data[15]:null); //PHYSICAL_INVENTORY_ID
              insPST.setString(15, data[16]!=null?data[16]:null); //TAG_ID
              insPST.setString(16, data[17]!=null?data[17]:null); //ADJUSTMENT_ID
              insPST.setString(17, data[18]!=null?data[18]:null); //TAG_UOM
              insPST.setString(18, data[19]!=null?data[19]:null); //TAG_UOM_CODE
              insPST.setString(19, data[20]!=null?data[20]:null); //TAG_TYPE_CODE
              insPST.setString(20, data[22]!=null?data[22]:null); //KEY
              if(data.length>23&&data[23]!=null)
                  insPST.setString(21, data[23]); //UNIQUE_SEQUENCE
              else
                  insPST.setObject(21, null);
              insPST.setString(22, data[21]!=null?data[21]:null); //CROSS_REFERENCE
              
//              insPST.setString(23, "PENDING");
              insPST.setString(24, data[7]!=null?data[7]:null); // SERIAL_NUMBER
              insPST.setString(25, data[3]!=null?data[3]:null); // REVISION
              insPST.setString(26, data[26]!=null?data[26]:null); // ITEM_TYPE
              insPST.setString(27, data[27]!=null?data[27]:null); // UNIT_COST_FD 
              insPST.setString(28, data[28]!=null?data[28]:null); // UNIT_COST_CURRENT
              
              if(data[20] != null && data[20].equals("3")){ //TAG_TYPE_CODE
              insPST.setObject(29, 0); // SNAPSHOT_QTY
                  // update status as COUNTED
                  insPST.setString(23, "COUNTED"); // STATUS
                  if(data[25] != null){ //COUNTED_BY
                      String userName = data[25];
                      //Get User id from db
                      userId = getUserID(userName.toUpperCase(), cn);
                      insPST.setString(30, data[8]!=null?data[8]:null); //INSERT QUANTITY from REPORT into MOBILE_QUANTITY of Database field
                      if(userId == 0){
//                          System.out.println(TAG + ": insertPhysicalCountData():2 TAG_TYPE_CODE" + data[20]);
                          insPST.setObject(31, null); //PC_ASSIGNED_USER_ID
                          insPST.setObject(32, null); //PC_ASSIGNED_USER_NAME
                          //also update status
                      }else{
//                          System.out.println(TAG + ": insertPhysicalCountData():3 TAG_TYPE_CODE" + data[20]);
                          insPST.setObject(31, userId); //PC_ASSIGNED_USER_ID
                          insPST.setObject(32, userName.toUpperCase()); //PC_ASSIGNED_USER_NAME - COUNTED_BY
                      }
                  }else{
//                      System.out.println(TAG + ": insertPhysicalCountData():3 TAG_TYPE_CODE" + data[20]);
                      insPST.setString(30, data[8]!=null?data[8]:null); // MOBILE_QUANTITY - what about here if TAG_TYPE_CODE-3 and COUNTEDby is null?
                      insPST.setObject(31, null); //PC_ASSIGNED_USER_ID
                      insPST.setObject(32, null); //PC_ASSIGNED_USER_NAME
                  }
                  Date lsatUpdateDate = getLastUpdateDateInUTC();
                  insPST.setTimestamp(33, new Timestamp(lsatUpdateDate.getTime())); //LAST_UPDATE_DATE
              } else{
//                  System.out.println(TAG + ": insertPhysicalCountData():4 TAG_TYPE_CODE" + data[20]);
                  insPST.setString(23, "PENDING");
                  insPST.setString(29, data[29]!=null?data[29]:null); // SNAPSHOT_QTY
                  insPST.setString(30, null); //MOBILE_QUANTITY
                  insPST.setObject(31, null); //PC_ASSIGNED_USER_ID
                  insPST.setObject(32, null); //PC_ASSIGNED_USER_NAME
                  insPST.setTimestamp(33, null); //LAST_UPDATE_DATE
              }
              
              insPST.executeUpdate();
            } catch (Exception e) {
                System.out.println(TAG + ": insertPhysicalCountData(): data[]: " + Arrays.toString(data));
                System.out.println(TAG + ": insertPhysicalCountData(): " + e.getLocalizedMessage());
                e.printStackTrace();
//                throw e;
            }
	}
	
    // Get MobileUserID of Mobile UserName
    public int getUserID(String userName, Connection cn) throws SQLException {
        int mobileUserId = 0;
        String statement = "SELECT MOBILE_USER_ID FROM WH360_MOBILE_USER_MANAGEMENT WHERE UPPER(MOBILE_USER_NAME) = ?";
        try(PreparedStatement ps =  cn.prepareStatement(statement);) {
            
            ps.setString(1, userName);
            try( ResultSet rs = ps.executeQuery();){
                if (rs != null && rs.next()) {
                    mobileUserId = rs.getInt("MOBILE_USER_ID");
                }
            } catch (SQLException sqle) {
                System.out.println(TAG + ": getUserID(): " + sqle.getLocalizedMessage());
                sqle.printStackTrace();
//                throw sqle;
            } 
            
        } catch (SQLException sqle) {
            System.out.println(TAG + ": getUserID(): " + sqle.getLocalizedMessage());
            sqle.printStackTrace();
//            throw sqle;
        } catch (Exception e) {
            System.out.println(TAG + ": getUserID(): " + e.getLocalizedMessage());
            e.printStackTrace();
//            throw e;
        }
        return mobileUserId;
    }
    
    public static Date getLastUpdateDateInUTC() throws Exception{
        Date currentSystemDate = null;
        try {
            SimpleDateFormat formatter =new SimpleDateFormat("dd-MM-yyyy hh:mm:ss aa");
            SimpleDateFormat DateFormatUTC =new SimpleDateFormat("dd-MM-yyyy hh:mm:ss aa");
            DateFormatUTC.setTimeZone(TimeZone.getTimeZone("UTC"));
            currentSystemDate = formatter.parse(DateFormatUTC.format(new Date()));
            
        } catch (Exception e) {
            System.out.println(TAG + ": getLastUpdateDateInUTC(): " + e.getLocalizedMessage());
            e.printStackTrace();
//            throw e;
        }
        return currentSystemDate;
    }
}
