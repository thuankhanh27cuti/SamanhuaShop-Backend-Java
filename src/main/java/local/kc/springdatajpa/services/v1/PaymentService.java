package local.kc.springdatajpa.services.v1;

import local.kc.springdatajpa.config.VNPayConfig;
import local.kc.springdatajpa.models.Order;
import local.kc.springdatajpa.models.OrderLog;
import local.kc.springdatajpa.models.OrderStatus;
import local.kc.springdatajpa.repositories.v1.OrderLogRepository;
import local.kc.springdatajpa.repositories.v2.OrderV2Repository;
import local.kc.springdatajpa.utils.payment.PaymentRequest;
import local.kc.springdatajpa.utils.payment.PaymentResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.view.RedirectView;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class PaymentService {
    private final OrderV2Repository orderRepository;
    private final OrderLogRepository orderLogRepository;

    @Autowired
    public PaymentService(OrderV2Repository orderRepository, OrderLogRepository orderLogRepository) {
        this.orderRepository = orderRepository;
        this.orderLogRepository = orderLogRepository;
    }

    public ResponseEntity<?> createPayment(PaymentRequest paymentRequest) {
        int orderId = paymentRequest.getOrderId();
        Order order = orderRepository.findById(orderId).orElse(null);
        if (order == null) {
            return ResponseEntity.notFound().build();
        }
        else if (order.getOrderStatus() != OrderStatus.WAIT_FOR_PAY) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        long amount = orderRepository.findTotalPriceById(orderId) * 100;
        String vnp_IpAddr = paymentRequest.getIdAddress();
        String vnp_TmnCode = VNPayConfig.vnp_TmnCode;

        Map<String, String> vnp_Params = new HashMap<>();
        vnp_Params.put("vnp_Version", VNPayConfig.vnp_Version);
        vnp_Params.put("vnp_Command", VNPayConfig.vnp_Command);
        vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
        vnp_Params.put("vnp_Amount", String.valueOf(amount));
        vnp_Params.put("vnp_CurrCode", "VND");

        vnp_Params.put("vnp_TxnRef", String.valueOf(orderId));
        vnp_Params.put("vnp_OrderInfo", "Thanh toan don hang:" + orderId);
        vnp_Params.put("vnp_OrderType", VNPayConfig.orderType);

        vnp_Params.put("vnp_Locale", "vn");
        vnp_Params.put("vnp_ReturnUrl", VNPayConfig.vnp_ReturnUrl);
        vnp_Params.put("vnp_IpAddr", vnp_IpAddr);

        Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String vnp_CreateDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_CreateDate", vnp_CreateDate);

        cld.add(Calendar.MINUTE, 15);
        String vnp_ExpireDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_ExpireDate", vnp_ExpireDate);

        List<String> fieldNames = new ArrayList<>(vnp_Params.keySet());
        Collections.sort(fieldNames);
        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();
        Iterator<String> itr = fieldNames.iterator();
        while (itr.hasNext()) {
            String fieldName = itr.next();
            String fieldValue = vnp_Params.get(fieldName);
            if ((fieldValue != null) && (!fieldValue.isEmpty())) {
                hashData.append(fieldName);
                hashData.append('=');
                hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII));

                query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII));
                query.append('=');
                query.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII));
                if (itr.hasNext()) {
                    query.append('&');
                    hashData.append('&');
                }
            }
        }
        String queryUrl = query.toString();
        String vnp_SecureHash = VNPayConfig.hmacSHA512(VNPayConfig.secretKey, hashData.toString());
        queryUrl += "&vnp_SecureHash=" + vnp_SecureHash;
        String paymentUrl = VNPayConfig.vnp_PayUrl + "?" + queryUrl;

        return ResponseEntity.ok(PaymentResponse.builder()
                .code("00")
                .message("Successfully")
                .data(paymentUrl)
                .build()
        );
    }

    public RedirectView updateOrderStatus(int orderId, String responseCode) {
        if (responseCode.equals("00")) {
            orderRepository.findById(orderId).ifPresent(order -> {
                order.setOrderStatus(OrderStatus.PENDING);
                orderRepository.save(order);

                OrderLog orderLog = OrderLog.builder()
                        .time(new Date())
                        .order(new Order(orderId))
                        .description("Đặt hàng thành công")
                        .build();

                orderLogRepository.save(orderLog);
            });
        }
        return new RedirectView("http://localhost:4200/orders");
    }
}
