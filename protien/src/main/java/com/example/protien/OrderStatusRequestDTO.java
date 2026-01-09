package com.example.protien;

public class OrderStatusRequestDTO {
    private String merchantOrderId;
    private Boolean details;

    public String getMerchantOrderId() {
        return merchantOrderId;
    }
    public void setMerchantOrderId(String merchantOrderId) {
        this.merchantOrderId = merchantOrderId;
    }
    public Boolean getDetails() {
        return details;
    }
    public void setDetails(Boolean details) {
        this.details = details;
    }
}
