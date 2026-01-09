package com.example.protien;

public class CreateSdkOrderResponseDTO {
    private String orderId;
    private String state;
    private Long expireAt;
    private String token;

    public CreateSdkOrderResponseDTO(String orderId, String state, Long expireAt, String token) {
        this.orderId = orderId;
        this.state = state;
        this.expireAt = expireAt;
        this.token = token;
    }

    public String getOrderId() { return orderId; }
    public void setOrderId(String orderId) { this.orderId = orderId; }
    public String getState() { return state; }
    public void setState(String state) { this.state = state; }
    public Long getExpireAt() { return expireAt; }
    public void setExpireAt(Long expireAt) { this.expireAt = expireAt; }
    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }
}
