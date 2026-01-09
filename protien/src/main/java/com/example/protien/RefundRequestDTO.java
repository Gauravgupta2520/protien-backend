package com.example.protien;

public class RefundRequestDTO {
    private String merchantRefundId;
    private String originalMerchantOrderId;
    private Long amount;

    public String getMerchantRefundId() {
        return merchantRefundId;
    }
    public void setMerchantRefundId(String merchantRefundId) {
        this.merchantRefundId = merchantRefundId;
    }
    public String getOriginalMerchantOrderId() {
        return originalMerchantOrderId;
    }
    public void setOriginalMerchantOrderId(String originalMerchantOrderId) {
        this.originalMerchantOrderId = originalMerchantOrderId;
    }
    public Long getAmount() {
        return amount;
    }
    public void setAmount(Long amount) {
        this.amount = amount;
    }
}
