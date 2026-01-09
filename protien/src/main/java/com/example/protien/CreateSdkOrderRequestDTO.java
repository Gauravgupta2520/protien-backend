package com.example.protien;

public class CreateSdkOrderRequestDTO {
    private Long amount;
    private String redirectUrl;
    private Boolean disablePaymentRetry;

    public Long getAmount() {
        return amount;
    }
    public void setAmount(Long amount) {
        this.amount = amount;
    }
    public String getRedirectUrl() {
        return redirectUrl;
    }
    public void setRedirectUrl(String redirectUrl) {
        this.redirectUrl = redirectUrl;
    }
    public Boolean getDisablePaymentRetry() {
        return disablePaymentRetry;
    }
    public void setDisablePaymentRetry(Boolean disablePaymentRetry) {
        this.disablePaymentRetry = disablePaymentRetry;
    }
}
