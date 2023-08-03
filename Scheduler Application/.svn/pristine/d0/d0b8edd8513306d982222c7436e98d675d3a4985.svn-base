package sc.common.model.util;

import java.math.BigDecimal;

import oracle.jbo.domain.Number;


public class ObjectConversion {
    public ObjectConversion() {
        super();
    }
    
    /**To convert String to Integer
         * @param obj
         * @return int
         */
        public static int stringToInteger(String obj) throws Exception{
            try{
                Integer res = Integer.parseInt(obj);
                return res.intValue();
            }
            catch(Exception e){
                e.printStackTrace();
                throw e;
            }
        }
        
        public static Number stringToNumber(String obj) throws Exception{
            try{
                Number n=  new Number(obj);
                return n;
            }
            catch(Exception e){
                e.printStackTrace();
                throw e;
            }
        }



        /**To Convert String to Double
         * @param obj
         * @return double
         */
        public static double stringToDouble(String obj) throws Exception{
            try{
                Double res = Double.parseDouble(obj);
                return res.doubleValue();
            }
            catch(Exception e){
                e.printStackTrace();
                throw e;
            }
        }

        /**To Convert String to Float
         * @param obj
         * @return float
         */
        public static float stringToFloat(String obj) throws Exception{
            try{
                Float res = Float.parseFloat(obj);
                return res.floatValue();
            }
            catch(Exception e){
                e.printStackTrace();
                throw e;
            }
        }

        /**To Convert String to BigDecimal
         * @param obj
         * @return BigDecimal
         */
        public static BigDecimal stringToBigDecimal(String obj) throws Exception{
            try{
                BigDecimal res = new BigDecimal(obj);
                return res;
            }
            catch(Exception e){
                e.printStackTrace();
                throw e;
            }
        }
        

        /**To convert from any int to String
         * @param obj
         * @return String
         */
        public static String intToString(int obj) throws Exception{
            try{
                String res = String.valueOf(obj);
                return res;
            }
            catch(Exception e){
                e.printStackTrace();
                throw e;
            }
        }

        /**To convert from any double to String
         * @param obj
         * @return String
         */
        public static String doubleToString(double obj) throws Exception{
            try{
                String res = String.valueOf(obj);
                return res;
            }
            catch(Exception e){
                e.printStackTrace();
                throw e;
            }
        }

        /**To convert from any int to Float
         * @param obj
         * @return String
         */
        public static String floatToString(float obj) throws Exception{
            try{
                String res = String.valueOf(obj);
                return res;
            }
            catch(Exception e){
                e.printStackTrace();
                throw e;
            }
        }

        /**To convert from any BigDecimal to String
         * @param obj
         * @return String
         */
        public static String bigDecimalToString(BigDecimal obj) throws Exception{
            try{
                String res = String.valueOf(obj);
                return res;
            }
            catch(Exception e){
                e.printStackTrace();
                throw e;
            }
        }

        /**To convert Number to BigDecimal
         * @param Number
         * @return BigDecimal
         */
        public static BigDecimal convertNumberToBigDecimal(Number val) throws Exception{
            try{
                BigDecimal bd = new BigDecimal(val.intValue());
                return bd;
            }
            catch(Exception e){
                e.printStackTrace();
                throw e;
            }
        }
        
        public static long convertStringToLong(String val) throws Exception{
            try{
                Long res = Long.parseLong(val);
                return res.longValue();
            }
            catch(Exception e){
                e.printStackTrace();
                throw e;
            }
        }
}
