package com.example.protien;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.phonepe.sdk.pg.common.models.response.RefundResponse;
import com.phonepe.sdk.pg.common.models.response.RefundStatusResponse;

@RestController
@RequestMapping("/api/payment")
public class RefundController {

    @Autowired
    private PhonePePaymentService phonePePaymentService;

    @PostMapping("/initiate-refund")
    public UserController.ApiResponse initiateRefund(@RequestBody RefundRequestDTO request) {
        try {
            RefundResponse response = phonePePaymentService.initiateRefund(
                request.getMerchantRefundId(),
                request.getOriginalMerchantOrderId(),
                request.getAmount()
            );
            return new UserController.ApiResponse(true, "Refund initiated", response);
        } catch (Exception e) {
            return new UserController.ApiResponse(false, "Refund initiation failed: " + e.getMessage());
        }
    }

    @PostMapping("/refund-status")
    public UserController.ApiResponse getRefundStatus(@RequestBody RefundStatusRequestDTO request) {
        try {
            RefundStatusResponse response = phonePePaymentService.getRefundStatus(request.getRefundId());
            return new UserController.ApiResponse(true, "Refund status fetched", response);
        } catch (Exception e) {
            return new UserController.ApiResponse(false, "Refund status fetch failed: " + e.getMessage());
        }
    }
}
