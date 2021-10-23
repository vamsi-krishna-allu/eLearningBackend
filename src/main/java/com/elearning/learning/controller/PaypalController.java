package com.elearning.learning.controller;

import com.elearning.learning.constants.MockTests;
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
    private final String SUCCESS = "success";
    private final String FAILED = "failed";
    private final String urlPrefix = "http://localhost:8080/";

    @PostMapping(value = "/pay")
    public String payment(@RequestBody Order order) {
        try {
            this.order = order;
            Double amount = MockTests.getAmountFromPlanAndType(order.getType(), order.getPlanType());
            Payment payment = service.createPayment(amount, "INR", "paypal",
                    "sale", order.getPlanType() + " plan for " + order.getCourseId() + " to " + order.getUserName(), urlPrefix + "pay/cancel",
                    urlPrefix + "pay/success");
            for(Links link:payment.getLinks()) {
                if(link.getRel().equals("approval_url")) {
                    return "redirect:"+link.getHref();
                }
            }

        } catch (PayPalRESTException e) {

            e.printStackTrace();
        }
        return SUCCESS;
    }

    @GetMapping(value = "pay/cancel")
    public String cancelPay() {
        this.order = new Order();
        return FAILED;
    }

    @GetMapping(value = "pay/success")
    public String successPay(@RequestParam("paymentId") String paymentId, @RequestParam("PayerID") String payerId) {
        try {
            Payment payment = service.executePayment(paymentId, payerId);
            System.out.println(payment.toJSON());
            if (payment.getState().equals("approved")) {
                service.updateCourseForUser(this.order);
                return SUCCESS;
            }
        } catch (PayPalRESTException e) {
            System.out.println(e.getMessage());
        }
        return SUCCESS;
    }

}
