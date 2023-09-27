package com.example.demo.service;

import com.example.demo.model.Customer;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface CustomerService {
    public CompletableFuture<List<Customer>> saveCustomers(MultipartFile file);
    public List<Customer> getCustomers();
    public CompletableFuture<List<Customer>> getCustomersUsingAsync();
}
