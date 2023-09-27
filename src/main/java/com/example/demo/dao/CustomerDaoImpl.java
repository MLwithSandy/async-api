package com.example.demo.dao;

import com.example.demo.model.Customer;
import com.example.demo.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerDaoImpl implements CustomerDao {

    @Autowired
    private CustomerRepository customerRepository;

    @Override
    public List<Customer> saveAllCustomers(List<Customer> customers) {
        return (List<Customer>) customerRepository.saveAll(customers);
    }

    @Override
    public List<Customer> getCustomers() {

        return (List<Customer>) customerRepository.findAll();
    }
}
