package sc.common.view.bean;

import java.sql.Date;

import java.text.SimpleDateFormat;

public class ExchangeRateBean {
    
    private String conversionType;
    private String fromCurrency;
    private String toCurrency;
    private String conversionDate;
    private String conversionRate;

    
    public ExchangeRateBean() {
        super();
    }
    
    public ExchangeRateBean(String conversionType,
                            String fromCurrency,
                            String toCurrency,
                            String conversionDate,
                            String conversionRate) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        this.conversionType = conversionType;
        this.fromCurrency = fromCurrency;
        this.toCurrency = toCurrency;
        if(conversionDate!=null){
            this.conversionDate = String.valueOf(new Date(sdf.parse(conversionDate).getTime()));
        }else{
            this.conversionDate = conversionDate;   
        }
        this.conversionRate = conversionRate;
     
    }
    
    public void setConversionType(String conversionType) {
        this.conversionType = conversionType;
    }

    public String getConversionType() {
        return conversionType;
    }

    public void setFromCurrency(String fromCurrency) {
        this.fromCurrency = fromCurrency;
    }

    public String getFromCurrency() {
        return fromCurrency;
    }

    public void setToCurrency(String toCurrency) {
        this.toCurrency = toCurrency;
    }

    public String getToCurrency() {
        return toCurrency;
    }


    public void setConversionDate(String conversionDate) {
        this.conversionDate = conversionDate;
    }

    public String getConversionDate() {
        return conversionDate;
    }

    public void setConversionRate(String conversionRate) {
        this.conversionRate = conversionRate;
    }

    public String getConversionRate() {
        return conversionRate;
    }
}
