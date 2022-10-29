package com.shahed.customer.mapper;

import com.shahed.customer.entity.Customer;
import com.shahed.customer.model.CustomerDTO;
import org.springframework.stereotype.Component;

@Component
public class CustomerMapper {

    public Customer domainToEntity(CustomerDTO domain) {

        Customer entity = new Customer();
        entity.setId(domain.getId());
        entity.setFirstName(domain.getFirstName());
        entity.setLastName(domain.getLastName());
        entity.setCity(domain.getCity());
        entity.setState(domain.getState());
        entity.setZipCode(domain.getZipCode());
        entity.setMobileNumber(domain.getMobileNumber());
        entity.setEmailAddress(domain.getEmailAddress());
        entity.setIpAddress(domain.getIpAddress());
        return entity;
    }
}
