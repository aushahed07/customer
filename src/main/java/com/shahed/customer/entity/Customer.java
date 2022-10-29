package com.shahed.customer.entity;

import javax.persistence.*;

@Entity
@Table(name = "customer")
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CUSTOMER_ID_GEN")
    @SequenceGenerator(name = "CUSTOMER_ID_GEN", sequenceName = "S_CUSTOMER_ID", initialValue = 1)
    private long id;
    private String firstName;
    private String lastName;
    private String city;
    private String state;
    private String zipCode;
    private String emailAddress;
    private String mobileNumber;
    private String ipAddress;

    public Customer() {
    }

    public Customer(long id, String firstName, String lastName, String city, String state, String zipCode, String emailAddress, String mobileNumber, String ipAddress) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.city = city;
        this.state = state;
        this.zipCode = zipCode;
        this.emailAddress = emailAddress;
        this.mobileNumber = mobileNumber;
        this.ipAddress = ipAddress;
    }

    public Customer(String firstName, String lastName, String city, String state, String zipCode, String emailAddress, String mobileNumber, String ipAddress) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.city = city;
        this.state = state;
        this.zipCode = zipCode;
        this.emailAddress = emailAddress;
        this.mobileNumber = mobileNumber;
        this.ipAddress = ipAddress;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

}

