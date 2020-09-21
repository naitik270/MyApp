package com.demo.nspl.restaurantlite.SMS;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ClsCheckCustCreditSMSParams {


    String CustomerCode = "";

    public String getCustomerCode() {
        return CustomerCode;
    }

    public void setCustomerCode(String customerCode) {
        CustomerCode = customerCode;
    }

    @SerializedName("Success")
    @Expose
    private String success;
    @SerializedName("Message")
    @Expose
    private String message;
    @SerializedName("Transactional")
    @Expose
    private Integer transactional;
    @SerializedName("Promotional")
    @Expose
    private Integer promotional;
    @SerializedName("Total")
    @Expose
    private Integer total;

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getTransactional() {
        return transactional;
    }

    public void setTransactional(Integer transactional) {
        this.transactional = transactional;
    }

    public Integer getPromotional() {
        return promotional;
    }

    public void setPromotional(Integer promotional) {
        this.promotional = promotional;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

}
