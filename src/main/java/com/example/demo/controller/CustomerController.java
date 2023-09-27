package com.example.demo.controller;

import com.example.demo.model.Customer;
import com.example.demo.service.CustomerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Slf4j
@RestController
@RequestMapping(value = "/customers")
public class CustomerController
{
    @Autowired
    private CustomerService customerService;


    @PostMapping()
    public ResponseEntity<Object> saveCustomers(@RequestParam("file") MultipartFile files)
    {
        List<Customer> customers = customerService.getCustomers();		// NORMAL METHOD CALL TO GET ALL CUSTOMERS

        log.debug("Customers : ",customers);

        customerService.saveCustomers(files);		 // READ 3000 RECORDS FROM CSV AND SAVE INTO DB -> THROUGH ASYNC METHOD

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }


    @GetMapping()
    public CompletableFuture<Object> getCustomers()
    {
        return customerService.getCustomersUsingAsync().thenApply(ResponseEntity::ok);
    }


    @GetMapping("/multiple")
    public ResponseEntity<Object> getMultipleCustomers()
    {
        CompletableFuture<List<Customer>> customerList1 = customerService.getCustomersUsingAsync();
        CompletableFuture<List<Customer>> customerList2 = customerService.getCustomersUsingAsync();
        CompletableFuture<List<Customer>> customerList3 = customerService.getCustomersUsingAsync();

        CompletableFuture.allOf(customerList1, customerList2, customerList3).join();

        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
