package com.example.protien;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/payment")
@CrossOrigin(origins = {"https://rocketmeals.netlify.app", "http://localhost:5173", "http://localhost:3000"})
public class PaymentController {

    @Autowired
    private PhonePePaymentService phonePePaymentService;

    @PostMapping("/initiate")
    public UserController.ApiResponse initiatePayment(@RequestBody PaymentRequest paymentRequest) {
        try {
            String redirectUrl = phonePePaymentService.initiatePayment(
                    paymentRequest.getAmount(),
                    paymentRequest.getRedirectUrl(),
                    paymentRequest.getMessage()
            );
            return new UserController.ApiResponse(true, "Payment initiated", redirectUrl);
        } catch (Exception e) {
            return new UserController.ApiResponse(false, "Payment initiation failed: " + e.getMessage());
        }
    }

    @GetMapping("/test")
    public String test() {
        return "Backend is reachable!";
    }
}
