package com.example.demo.dao;

import com.example.demo.model.Customer;

import java.util.List;

public interface CustomerDao {
    public List<Customer> saveAllCustomers(List<Customer> customers);

    public List<Customer> getCustomers();
}
