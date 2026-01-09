package com.example.protien;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.phonepe.sdk.pg.common.models.MetaInfo;
import com.phonepe.sdk.pg.common.models.request.paymentmodeconstraints.CardPaymentModeConstraint;
import com.phonepe.sdk.pg.common.models.request.paymentmodeconstraints.CardType;
import com.phonepe.sdk.pg.common.models.request.paymentmodeconstraints.NetBankingPaymentModeConstraint;
import com.phonepe.sdk.pg.common.models.request.paymentmodeconstraints.PaymentModeConstraint;
import com.phonepe.sdk.pg.common.models.request.paymentmodeconstraints.UpiQrPaymentModeConstraint;
import com.phonepe.sdk.pg.payments.v2.StandardCheckoutClient;
import com.phonepe.sdk.pg.payments.v2.models.request.PaymentModeConfig;
import com.phonepe.sdk.pg.payments.v2.models.request.StandardCheckoutPayRequest;
import com.phonepe.sdk.pg.payments.v2.models.response.StandardCheckoutPayResponse;

@Service
public class PhonePePaymentService {
                public com.phonepe.sdk.pg.common.models.response.RefundResponse initiateRefund(String merchantRefundId, String originalMerchantOrderId, Long amount) {
                        com.phonepe.sdk.pg.common.models.request.RefundRequest refundRequest = com.phonepe.sdk.pg.common.models.request.RefundRequest.builder()
                                .merchantRefundId(merchantRefundId)
                                .originalMerchantOrderId(originalMerchantOrderId)
                                .amount(amount)
                                .build();
                        return phonePeClient.refund(refundRequest);
                }

                public com.phonepe.sdk.pg.common.models.response.RefundStatusResponse getRefundStatus(String refundId) {
                        return phonePeClient.getRefundStatus(refundId);
                }
        public com.phonepe.sdk.pg.common.models.response.OrderStatusResponse getOrderStatus(String merchantOrderId) {
                return phonePeClient.getOrderStatus(merchantOrderId);
        }

    @Autowired
    private StandardCheckoutClient phonePeClient;

    public String initiatePayment(Long amount, String redirectUrl, String message) {
        String merchantOrderId = UUID.randomUUID().toString();
        MetaInfo metaInfo = MetaInfo.builder()
                .udf1("udf1")
                .udf2("udf2")
                .udf3("udf3")
                .udf4("udf4")
                .udf5("udf5")
                .build();

        Set<CardType> allowedCardTypes = new HashSet<>();
        allowedCardTypes.add(CardType.DEBIT_CARD);
        allowedCardTypes.add(CardType.CREDIT_CARD);
        PaymentModeConstraint cardPaymentModeConstraint = CardPaymentModeConstraint.builder()
                .cardTypes(allowedCardTypes)
                .build();
        PaymentModeConstraint netbanking = NetBankingPaymentModeConstraint.builder().build();
        PaymentModeConstraint upiQr = UpiQrPaymentModeConstraint.builder().build();
        PaymentModeConfig paymentModeConfigEnabled = PaymentModeConfig.builder()
                .enabledPaymentModes(Arrays.asList(cardPaymentModeConstraint, netbanking, upiQr))
                .build();

        StandardCheckoutPayRequest payRequest = StandardCheckoutPayRequest.builder()
                .merchantOrderId(merchantOrderId)
                .amount(amount)
                .redirectUrl(redirectUrl)
                .metaInfo(metaInfo)
                .paymentModeConfig(paymentModeConfigEnabled)
                .message(message)
                .expireAfter(3600L)
                .build();

        StandardCheckoutPayResponse payResponse = phonePeClient.pay(payRequest);
        return payResponse.getRedirectUrl();
    }
}
