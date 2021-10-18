package com.elearning.learning.controller;

import com.elearning.learning.model.Order;
import com.elearning.learning.service.PaypalService;
import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@AllArgsConstructor
public class PaypalController {

    private final PaypalService service;
    private static Order order;

    @PostMapping(value = "/pay")
    public String payment(@RequestBody Order order) {
        try {
            this.order = order;
            Payment payment = service.createPayment(order.getPrice(), "INR", "paypal",
                    "sale", order.getPlanType() + " plan for " + order.getCourseId() + " to " + order.getUserName(), "http://localhost:8080/" + "pay/cancel",
                    "http://localhost:8080/" + "pay/success");
            for(Links link:payment.getLinks()) {
                if(link.getRel().equals("approval_url")) {
                    return "redirect:"+link.getHref();
                }
            }

        } catch (PayPalRESTException e) {

            e.printStackTrace();
        }
        return "success";
    }

    @GetMapping(value = "pay/cancel")
    public String cancelPay() {
        this.order = new Order();
        return "cancel";
    }

    @GetMapping(value = "pay/success")
    public String successPay(@RequestParam("paymentId") String paymentId, @RequestParam("PayerID") String payerId) {
        try {
            Payment payment = service.executePayment(paymentId, payerId);
            System.out.println(payment.toJSON());
            if (payment.getState().equals("approved")) {
                service.updateCourseForUser(this.order);
                return "success";
            }
        } catch (PayPalRESTException e) {
            System.out.println(e.getMessage());
        }
        return "successed";
    }

}
