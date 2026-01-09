package com.example.protien;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.phonepe.sdk.pg.common.models.response.OrderStatusResponse;

@RestController
@RequestMapping("/api/payment")
public class OrderStatusController {

    @Autowired
    private PhonePePaymentService phonePePaymentService;

    @PostMapping("/order-status")
    public UserController.ApiResponse getOrderStatus(@RequestBody OrderStatusRequestDTO request) {
        try {
            OrderStatusResponse response = phonePePaymentService.getOrderStatus(request.getMerchantOrderId());
            return new UserController.ApiResponse(true, "Order status fetched", response);
        } catch (Exception e) {
            return new UserController.ApiResponse(false, "Order status fetch failed: " + e.getMessage());
        }
    }
}
