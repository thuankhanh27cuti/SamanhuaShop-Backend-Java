package local.kc.springdatajpa.controllers.v1;

import local.kc.springdatajpa.services.v1.PaymentService;
import local.kc.springdatajpa.utils.payment.PaymentRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

@RestController
@RequestMapping("/api/v1/payment")
public class PaymentController {
    private final PaymentService paymentService;

    @Autowired
    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping("/create-payment")
    public ResponseEntity<?> createPayment(@RequestBody PaymentRequest paymentRequest) {
        return paymentService.createPayment(paymentRequest);
    }

    @GetMapping("/payment-success")
    public RedirectView updateOrderStatus(@RequestParam(name = "vnp_TxnRef") int orderId, @RequestParam(name = "vnp_ResponseCode") String responseCode) {
        return paymentService.updateOrderStatus(orderId, responseCode);
    }
}
