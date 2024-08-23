package com.elearning.learning.service;

import com.elearning.learning.entities.UserCourseDetails;
import com.elearning.learning.model.CourseMapper;
import com.elearning.learning.model.Order;
import com.elearning.learning.repository.UserCourseRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.paypal.api.payments.*;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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

    @Transactional
    public String updateCourseForUser(com.elearning.learning.model.Order order) throws JsonProcessingException {
        UserCourseDetails userCourseDetails = userCourseRepository.findByUsername(order.getUserName());
        if(ObjectUtils.isEmpty(userCourseDetails)) {
            return insertCourseDetails(order);
        }
        ObjectMapper mapper = new ObjectMapper();
        List<CourseMapper> coursesList = mapper.readValue(userCourseDetails.getAllowedCourses(), new TypeReference<List<CourseMapper>>(){});
        List<CourseMapper> testList = mapper.readValue(userCourseDetails.getAllowedMockTests(), new TypeReference<List<CourseMapper>>(){});

        if("COURSE".equals(order.getType())) {
            CourseMapper courseMapper = new CourseMapper();
            CourseMapper testMapper = new CourseMapper();

            Calendar c = Calendar.getInstance();
            c.setTime(new Date());
            courseMapper.setStartTime(c.getTimeInMillis());

            Calendar testCalendar = Calendar.getInstance();
            testCalendar.setTime(new Date());
            testMapper.setStartTime(testCalendar.getTimeInMillis());

            if("BASIC".equals(order.getPlanType())) {

                courseMapper.setId(order.getCourseId());
                c.add(Calendar.MONTH, 1);
                courseMapper.setEndTime(c.getTimeInMillis());

            } else if("STANDARD".equals(order.getPlanType())) {

                courseMapper.setId(order.getCourseId());
                c.add(Calendar.MONTH, 2);
                courseMapper.setEndTime(c.getTimeInMillis());

                List<String> testArray = new ArrayList<>();
                testArray.add(order.getCourseId()+"-test-1");
                testList = iterateCourseMappers(testList, testArray);
                testCalendar.add(Calendar.MONTH, 2);
                testMapper.setEndTime(testCalendar.getTimeInMillis());
                testMapper.setId(order.getCourseId()+"-test-1");
                testList.add(testMapper);
                testMapper.setId(order.getCourseId()+"-test-2");
                testList.add(testMapper);

            } else if("PLATINUM".equals(order.getPlanType())) {

                courseMapper.setId(order.getCourseId());
                c.add(Calendar.MONTH, 3);
                courseMapper.setEndTime(c.getTimeInMillis());

                testList = removeDuplicateMappers(order, testList);
                updatePlatinumData(order, testList, testMapper, testCalendar);

            }
            try {
                coursesList.add(courseMapper);
                Gson courseGson = new Gson();
                String testData = CollectionUtils.isEmpty(testList) ? "" : courseGson.toJson(testList);
                userCourseRepository.updateAllowedCourseAndTests(order.getUserName(),
                        courseGson.toJson(coursesList),
                        testData);
                return "UPDATE SUCCESSFUL";
            }catch(Exception e) {
                return "UPDATE FAILED";
            }
        } else if("TEST".equals(order.getType())) {
            CourseMapper testMapper = new CourseMapper();
            Calendar testCalendar = Calendar.getInstance();
            testCalendar.setTime(new Date());
            if("BASIC".equals(order.getPlanType())) {

                testCalendar.add(Calendar.MONTH, 1);
                testMapper.setEndTime(testCalendar.getTimeInMillis());
                testMapper.setId(order.getCourseId()+"-test-1");
                testList.add(testMapper);
                testMapper.setId(order.getCourseId()+"-test-2");
                testList.add(testMapper);

            } else if("PLATINUM".equals(order.getPlanType())) {

                testList = removeDuplicateMappers(order, testList);

                testCalendar.add(Calendar.MONTH, 2);
                testMapper.setEndTime(testCalendar.getTimeInMillis());
                updateTestMapper(order, testList, testMapper);

            }
            try {
                Gson testGson = new Gson();
                userCourseRepository.updateAllowedTest(order.getUserName(), testGson.toJson(testList));
                return "UPDATE SUCCESSFUL";
            }catch(Exception e) {
                return "UPDATE FAILED";
            }

        }
        return "UPDATE SUCCESSFUL";
    }

    private void updatePlatinumData(Order order, List<CourseMapper> testList, CourseMapper testMapper, Calendar testCalendar) {
        testCalendar.add(Calendar.MONTH, 3);
        testMapper.setEndTime(testCalendar.getTimeInMillis());
        updateTestMapper(order, testList, testMapper);
    }

    private void updateTestMapper(Order order, List<CourseMapper> testList, CourseMapper testMapper) {
        testMapper.setId(order.getCourseId()+"-test-1");
        testList.add(testMapper);
        testMapper.setId(order.getCourseId()+"-test-2");
        testList.add(testMapper);
        testMapper.setId(order.getCourseId()+"-test-3");
        testList.add(testMapper);
        testMapper.setId(order.getCourseId()+"-test-4");
        testList.add(testMapper);
    }

    private List<CourseMapper> removeDuplicateMappers(Order order, List<CourseMapper> testList) {
        List<String> testArray = new ArrayList<>();
        testArray.add(order.getCourseId()+"-test-1");
        testArray.add(order.getCourseId()+"-test-2");

        testList = iterateCourseMappers(testList, testArray);
        return testList;
    }

    private List<CourseMapper> iterateCourseMappers(List<CourseMapper> testList, List<String> testArray) {
        List<CourseMapper> localList = testList;
        int index = 0;
        for(CourseMapper test: testList) {
            if(testArray.contains(test.getId())) {
                localList.remove(index);
                continue;
            }
            index++;
        }
        testList = localList;
        return testList;
    }

    private String insertCourseDetails(com.elearning.learning.model.Order order) {

        List<CourseMapper> courseMapperList = new ArrayList<>();
        List<CourseMapper> testMapperList = new ArrayList<>();

        if("COURSE".equals(order.getType())) {
            Calendar c = Calendar.getInstance();
            CourseMapper courseMapper = new CourseMapper();
            courseMapper.setId(order.getCourseId());
            c.setTime(new Date());
            courseMapper.setStartTime(c.getTimeInMillis());

            CourseMapper testMapper = new CourseMapper();
            Calendar testCalendar = Calendar.getInstance();
            testCalendar.setTime(new Date());

            if("BASIC".equals(order.getPlanType())) {
                c.add(Calendar.MONTH, 1);
                courseMapper.setEndTime(c.getTimeInMillis());

            } else if("STANDARD".equals(order.getPlanType())) {
                c.add(Calendar.MONTH, 2);
                courseMapper.setEndTime(c.getTimeInMillis());

                testCalendar.add(Calendar.MONTH, 2);
                testMapper.setEndTime(testCalendar.getTimeInMillis());
                testMapper.setId(order.getCourseId()+"-test-1");
                testMapperList.add(testMapper);
                testMapper.setId(order.getCourseId()+"-test-2");
                testMapperList.add(testMapper);

            } else if("PLATINUM".equals(order.getPlanType())) {
                c.add(Calendar.MONTH, 3);
                courseMapper.setEndTime(c.getTimeInMillis());

                updatePlatinumData(order, testMapperList, testMapper, testCalendar);

            }
            try {
                courseMapperList.add(courseMapper);
                Gson courseGson = new Gson();
                String testData = CollectionUtils.isEmpty(testMapperList) ? "" : courseGson.toJson(testMapperList);
                UserCourseDetails userCourseDetails = new UserCourseDetails();
                userCourseDetails.setUsername(order.getUserName());
                userCourseDetails.setAllowedCourses(courseGson.toJson(courseMapperList));
                userCourseDetails.setAllowedMockTests(testData);
                userCourseDetails.setSubmittedMockTests(null);
                userCourseRepository.save(userCourseDetails);
                return "INSERT SUCCESSFUL";
            }catch(Exception e) {
                return "INSERT FAILED";
            }
        } else if("TEST".equals(order.getType())) {

            CourseMapper testMapper = new CourseMapper();
            Calendar testCalendar = Calendar.getInstance();
            testCalendar.setTime(new Date());

            if("BASIC".equals(order.getPlanType())) {

                testCalendar.add(Calendar.MONTH, 1);
                testMapper.setEndTime(testCalendar.getTimeInMillis());
                testMapper.setId(order.getCourseId()+"-test-1");
                testMapperList.add(testMapper);
                testMapper.setId(order.getCourseId()+"-test-2");
                testMapperList.add(testMapper);

            } else if("PLATINUM".equals(order.getPlanType())) {

                testCalendar.add(Calendar.MONTH, 2);
                testMapper.setEndTime(testCalendar.getTimeInMillis());
                updateTestMapper(order, testMapperList, testMapper);

            }
            try {
                Gson testGson = new Gson();
                UserCourseDetails userCourseDetails = new UserCourseDetails();
                userCourseDetails.setUsername(order.getUserName());
                userCourseDetails.setAllowedCourses(null);
                userCourseDetails.setAllowedMockTests(testGson.toJson(testMapperList));
                userCourseDetails.setSubmittedMockTests(null);
                userCourseRepository.save(userCourseDetails);
                return "INSERT SUCCESSFUL";
            }catch(Exception e) {
                return "INSERT FAILED";
            }

        }
        return "INSERT FAILED";
    }
}
