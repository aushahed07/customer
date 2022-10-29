package com.shahed.customer.service;

import com.shahed.customer.model.CustomerServiceResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

@Service
public interface CustomerService {

    CompletableFuture<CustomerServiceResponse> saveCustomerInfo(MultipartFile file);

    void writeLineInFile(String fileName, String line) throws IOException;

}
