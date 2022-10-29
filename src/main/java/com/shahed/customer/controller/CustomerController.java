package com.shahed.customer.controller;

import com.shahed.customer.model.CustomerServiceResponse;
import com.shahed.customer.service.CustomerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * @author shahed
 */

@RestController
@RequestMapping(value = "/customer")
public class CustomerController {

    Logger logger = LoggerFactory.getLogger(CustomerController.class);
    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public CompletableFuture<CustomerServiceResponse> saveCustomerInfoFromTextFile(@RequestPart("files") MultipartFile[] files) throws ExecutionException, InterruptedException {
        long startTime = System.currentTimeMillis();
        logger.info("Request for customer information store.");
        CompletableFuture<CustomerServiceResponse> serviceResponse = CompletableFuture.completedFuture(new CustomerServiceResponse());

        try {
            for(MultipartFile file: files) {
                serviceResponse = customerService.saveCustomerInfo(file);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        serviceResponse = CompletableFuture.completedFuture(serviceResponse.get());

        long endTime = System.currentTimeMillis();
        long totalTime = endTime-startTime;
        String info = "Total processing time: "
                        + TimeUnit.MILLISECONDS.toMinutes(totalTime)
                        + " minutes and "
                        + (TimeUnit.MILLISECONDS.toSeconds(totalTime) % 60)
                        + " seconds" ;

        logger.info(info);

        try {
            customerService.writeLineInFile("execution-time-report.txt", info);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("Failed to write total execution time.");
        }

        return serviceResponse;
    }
}
