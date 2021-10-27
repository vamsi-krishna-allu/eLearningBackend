package com.elearning.learning.service;

import com.elearning.learning.entities.UserCourseDetails;
import com.elearning.learning.model.Order;
import com.elearning.learning.repository.UserCourseRepository;
import com.paypal.api.payments.*;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class PaypalService {

    @Autowired
    private APIContext apiContext;

    private final UserCourseRepository userCourseRepository;

    public Payment createPayment(
            Double total,
            String currency,
            String method,
            String intent,
            String description,
            String cancelUrl,
            String successUrl) throws PayPalRESTException {
        Amount amount = new Amount();
        amount.setCurrency(currency);
        total = new BigDecimal(total).setScale(2, RoundingMode.HALF_UP).doubleValue();
        amount.setTotal(String.format("%.2f", total));

        Transaction transaction = new Transaction();
        transaction.setDescription(description);
        transaction.setAmount(amount);

        List<Transaction> transactions = new ArrayList<>();
        transactions.add(transaction);

        Payer payer = new Payer();
        payer.setPaymentMethod(method.toString());

        Payment payment = new Payment();
        payment.setIntent(intent.toString());
        payment.setPayer(payer);
        payment.setTransactions(transactions);
        RedirectUrls redirectUrls = new RedirectUrls();
        redirectUrls.setCancelUrl(cancelUrl);
        redirectUrls.setReturnUrl(successUrl);
        payment.setRedirectUrls(redirectUrls);

        return payment.create(apiContext);
    }

    public Payment executePayment(String paymentId, String payerId) throws PayPalRESTException{
        Payment payment = new Payment();
        payment.setId(paymentId);
        PaymentExecution paymentExecute = new PaymentExecution();
        paymentExecute.setPayerId(payerId);
        return payment.execute(apiContext, paymentExecute);
    }

    public String updateCourseForUser(com.elearning.learning.model.Order order) {
        UserCourseDetails userCourseDetails = userCourseRepository.findByUsername(order.getUserName());
        if(ObjectUtils.isEmpty(userCourseDetails)) {
            return insertCourseDetails(order);
        }
        String availableCourses = userCourseDetails.getAllowedCourses();
        String availableTests = userCourseDetails.getAllowedMockTests();
        if("COURSE".equals(order.getType())) {
            if("BASIC".equals(order.getPlanType())) {
                availableCourses = availableCourses.concat(",").concat(order.getCourseId());
                availableTests = availableTests.concat(",").concat(order.getCourseId()+"-test-1");
            } else if("STANDARD".equals(order.getPlanType())) {
                availableCourses = availableCourses.concat(",").concat(order.getCourseId());
                availableTests = availableTests.concat(",").concat(order.getCourseId()+"-test-1")
                        .concat(",").concat(order.getCourseId()+"-test-2");
            } else if("PLATINUM".equals(order.getPlanType())) {
                availableCourses = availableCourses.concat(",").concat(order.getCourseId());
                availableTests = availableTests.concat(",").concat(order.getCourseId()+"-test-1")
                        .concat(",").concat(order.getCourseId()+"-test-2").concat(",")
                        .concat(order.getCourseId()+"-test-3").concat(",")
                        .concat(order.getCourseId()+"-test-4");
            }
            try {
                userCourseRepository.updateAllowedCourseAndTests(order.getUserName(), availableCourses, availableTests);
                return "UPDATE SUCCESSFUL";
            }catch(Exception e) {
                return "UPDATE FAILED";
            }
        } else if("TEST".equals(order.getType())) {
            if("BASIC".equals(order.getPlanType())) {
                availableTests = availableTests.concat(",").concat(order.getCourseId()+"-test-1");
            } else if("STANDARD".equals(order.getPlanType())) {
                availableTests = availableTests.concat(",").concat(order.getCourseId()+"-test-1")
                        .concat(",").concat(order.getCourseId()+"-test-2");
            } else if("PLATINUM".equals(order.getPlanType())) {
                availableTests = availableTests.concat(",").concat(order.getCourseId()+"-test-1")
                        .concat(",").concat(order.getCourseId()+"-test-2").concat(",")
                        .concat(order.getCourseId()+"-test-3").concat(",")
                        .concat(order.getCourseId()+"-test-4");
            }
            try {
                userCourseRepository.updateAllowedTest(order.getUserName(), availableTests);
                return "UPDATE SUCCESSFUL";
            }catch(Exception e) {
                return "UPDATE FAILED";
            }

        }
        return "UPDATE SUCCESSFUL";
    }

    private String insertCourseDetails(com.elearning.learning.model.Order order) {
        String availableCourses;
        String availableTests = null;
        if("COURSE".equals(order.getType())) {
            availableCourses = order.getCourseId();
            if("BASIC".equals(order.getPlanType())) {
                availableTests = order.getCourseId()+"-test-1";
            } else if("STANDARD".equals(order.getPlanType())) {
                availableTests = (order.getCourseId()+"-test-1")
                        .concat(",").concat(order.getCourseId()+"-test-2");
            } else if("PLATINUM".equals(order.getPlanType())) {
                availableTests = (order.getCourseId()+"-test-1")
                        .concat(",").concat(order.getCourseId()+"-test-2").concat(",")
                        .concat(order.getCourseId()+"-test-3").concat(",")
                        .concat(order.getCourseId()+"-test-4");
            }
            try {
                UserCourseDetails userCourseDetails = new UserCourseDetails();
                userCourseDetails.setUsername(order.getUserName());
                userCourseDetails.setAllowedCourses(availableCourses);
                userCourseDetails.setAllowedMockTests(availableTests);
                userCourseDetails.setSubmittedMockTests(null);
                userCourseRepository.save(userCourseDetails);
                return "INSERT SUCCESSFUL";
            }catch(Exception e) {
                return "INSERT FAILED";
            }
        } else if("TEST".equals(order.getType())) {
            availableTests = order.getCourseId()+"-test-1";
            if("BASIC".equals(order.getPlanType())) {
                availableTests = availableTests;
            } else if("STANDARD".equals(order.getPlanType())) {
                availableTests = (availableTests)
                        .concat(",").concat(order.getCourseId()+"-test-2");
            } else if("PLATINUM".equals(order.getPlanType())) {
                availableTests = (availableTests)
                        .concat(",").concat(order.getCourseId()+"-test-2").concat(",")
                        .concat(order.getCourseId()+"-test-3").concat(",")
                        .concat(order.getCourseId()+"-test-4");
            }
            try {
                UserCourseDetails userCourseDetails = new UserCourseDetails();
                userCourseDetails.setUsername(order.getUserName());
                userCourseDetails.setAllowedCourses(null);
                userCourseDetails.setAllowedMockTests(availableTests);
                userCourseDetails.setSubmittedMockTests(null);
                userCourseRepository.save(userCourseDetails);
                userCourseRepository.save(userCourseDetails);
                return "INSERT SUCCESSFUL";
            }catch(Exception e) {
                return "INSERT FAILED";
            }

        }
        return "INSERT FAILED";
    }
}
