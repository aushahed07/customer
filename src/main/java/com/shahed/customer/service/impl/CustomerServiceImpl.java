package com.shahed.customer.service.impl;

import com.shahed.customer.mapper.CustomerMapper;
import com.shahed.customer.model.CustomerDTO;
import com.shahed.customer.model.CustomerServiceResponse;
import com.shahed.customer.repository.CustomerRepository;
import com.shahed.customer.service.CustomerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class CustomerServiceImpl implements CustomerService {

    Logger logger = LoggerFactory.getLogger(CustomerServiceImpl.class);

    private String invalidDataFileName = "invalid-data.txt";
    private String timeInfoFileName = "execution-time-report.txt";
    private String duplicateDataFileName = "duplicate-data.txt";
    private String validationRegex = "^.*,.*,.*,.*,.*,(\\(\\+?1\\)\\s?|\\+?1\\s?)?((\\([0-9]{3}\\))|[0-9]{3})[\\s\\-]?[0-9]{3}[\\s\\-]?[0-9]{4},[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4},.*$";
    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;

    public CustomerServiceImpl(CustomerRepository customerRepository, CustomerMapper customerMapper) {
        this.customerRepository = customerRepository;
        this.customerMapper = customerMapper;
    }

    @Override
    @Async
    public CompletableFuture<CustomerServiceResponse> saveCustomerInfo(MultipartFile file) {
        StringBuilder message = new StringBuilder("");
        CustomerServiceResponse serviceResponse = new CustomerServiceResponse();

        try {
            serviceResponse = processFile(file);
        } catch (Exception e) {
            e.printStackTrace();
            message = message.append("Failed to save customer information.");
            logger.error(message.toString());
            return CompletableFuture.completedFuture(new CustomerServiceResponse(message.toString(), message.toString()));
        }

        return CompletableFuture.completedFuture(serviceResponse);
    }

    private CustomerServiceResponse processFile(MultipartFile file) {
        logger.info("Saving file: {} , Thread: {}", file.getOriginalFilename(), Thread.currentThread().getName());

        long startTime = System.currentTimeMillis();
        StringBuilder message = new StringBuilder("");
        CustomerServiceResponse serviceResponse = new CustomerServiceResponse();
        List<CustomerDTO> customerDTOList = new ArrayList<>();

        try {
            InputStream inputStream = new ByteArrayInputStream(file.getBytes());
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

            List<String> strings = bufferedReader.lines().collect(Collectors.toList());

            Set<String> stringSet = new HashSet<>();

            List<String> duplicates = strings.stream()
                    .filter(s -> !stringSet.add(s))
                    .collect(Collectors.toList());

            try {
                for (String line: duplicates) {
                    writeLineInFile(duplicateDataFileName, line);
                }
            } catch (Exception e) {
                e.printStackTrace();
                logger.error("Failed to write duplicate data.");
            }

            for(String line: stringSet) {
                CustomerDTO customerDTO = processLine(line);
                if(customerDTO != null) {
                    customerDTOList.add(customerDTO);
                }
            }

            long queryStartTime = System.currentTimeMillis();

            customerRepository.saveAll(customerDTOList.stream()
                    .map(customerDTO -> customerMapper
                            .domainToEntity(customerDTO))
                    .collect(Collectors.toList())
            );

            long queryStoreTime = System.currentTimeMillis() - queryStartTime;
            String info = "File '"
                            + file.getOriginalFilename()
                            + "' storing time: "
                            + TimeUnit.MILLISECONDS.toMinutes(queryStoreTime)
                            + " minutes and "
                            + (TimeUnit.MILLISECONDS.toSeconds(queryStoreTime) % 60)
                            + " seconds";

            logger.info(info);
            try {
                writeLineInFile(timeInfoFileName,info);
            } catch (Exception e) {
                e.printStackTrace();
                logger.error("Failed to write '{}' storing time.", file.getOriginalFilename());
            }

            serviceResponse.setHasError(false);
            serviceResponse.setDecentMessage("Customer information saved successfully.");
        } catch (Exception e) {
            e.printStackTrace();
            message = message.append("Failed to process file: ").append(file.getOriginalFilename());
            logger.error(message.toString());
            return new CustomerServiceResponse(message.toString(), message.toString());
        }

        long processingTime = System.currentTimeMillis() - startTime ;
        String info = "File '"
                        + file.getOriginalFilename()
                        + "' processing time: "
                        + TimeUnit.MILLISECONDS.toMinutes(processingTime)
                        + " minutes and "
                        + (TimeUnit.MILLISECONDS.toSeconds(processingTime) % 60)
                        + " seconds" ;

        logger.info(info);
        try {
            writeLineInFile(timeInfoFileName,info);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("Failed to write '{}' processing time.", file.getOriginalFilename());
        }

        return serviceResponse;
    }

    private CustomerDTO processLine(String line) throws IOException {

        CustomerDTO customerDTO = new CustomerDTO();
        if(line.endsWith(",")) {
            line = line.concat(" ");
        }
        String [] items = line.split(",");

        if (line.chars().filter(ch -> ch == ',').count() != 7
                || !line.matches(validationRegex)) {
            writeLineInFile(invalidDataFileName,line);
            return null;
        }

        customerDTO.setFirstName(items[0].trim().isEmpty() ? null : items[0].trim());
        customerDTO.setLastName(items[1].trim().isEmpty() ? null : items[1].trim());
        customerDTO.setCity(items[2].trim().isEmpty() ? null : items[2].trim());
        customerDTO.setState(items[3].trim().isEmpty() ? null : items[3].trim());
        customerDTO.setZipCode(items[4].trim().isEmpty() ? null : items[4].trim());
        customerDTO.setMobileNumber(items[5].trim().isEmpty() ? null : items[5].trim());
        customerDTO.setEmailAddress(items[6].trim().isEmpty() ? null : items[6].trim());
        customerDTO.setIpAddress(items[7].trim().isEmpty() ? null : items[7].trim());
        return customerDTO;
    }

    @Override
    public void writeLineInFile(String fileName, String line) throws IOException {
        FileWriter fw = new FileWriter(fileName, true);
        BufferedWriter bw = new BufferedWriter(fw);
        try {
            bw.write(line);
            bw.newLine();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            bw.close();
            fw.close();
        }
    }
}
