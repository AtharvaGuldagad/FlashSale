
//DUMMY CONTROLLER FOR PAYMENT SERVICE, NOT USED IN ACTUAL IMPLEMENTATION

package com.atharva.flashsale.payment_service.controller;

import com.atharva.flashsale.payment_service.model.Payment;
import com.atharva.flashsale.payment_service.repository.PaymentRepository;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private final PaymentRepository paymentRepository;

    public PaymentController(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    @PostMapping
    public Payment processDummyPayment(@RequestBody Payment payment) {
        payment.setStatus("SUCCESS");
        return paymentRepository.save(payment);
    }
}