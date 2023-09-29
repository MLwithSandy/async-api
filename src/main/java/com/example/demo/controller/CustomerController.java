package com.example.demo.controller;

import com.example.demo.model.Customer;
import com.example.demo.service.CustomerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Slf4j
@RestController
@RequestMapping(value = "/customers")
public class CustomerController
{
    @Autowired
    private CustomerService customerService;

    private static Map<UUID, List<Customer>> customerMap = new HashMap<>();

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

    // Write a public method which returns a response entity with id of type UUID and HttpStatus of ACCEPTED
    @GetMapping("/all")
    public ResponseEntity<UUID> getAllCustomers() throws ExecutionException {
        UUID randomUUID = UUID.randomUUID();
        customerMap.put(randomUUID, null);

        CompletableFuture.runAsync(() -> {

            var result = customerService.getCustomersWithUUIDUsingAsync(randomUUID);
            if (result.isDone())
            {
                // print current timestamp in the console  using log.info
                log.info("response returned from async call at: {}", new SimpleDateFormat("yyyy-MM-dd-HH.mm.ss").format(new java.util.Date()));
                try {
                    var uuid = result.get().getFirst();
                    var customers = result.get().getSecond();
                    customerMap.replace(uuid, customers);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                } catch (ExecutionException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        log.info("response returned at: {}", new SimpleDateFormat("yyyy-MM-dd-HH.mm.ss").format(new java.util.Date()));
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(randomUUID);
    }

    // Write a rest get mapping method to take UUID as input and return a response entity with list of customers and HttpStatus of OK if uuid exist in the map
    @GetMapping("/all/{uuid}")
    public ResponseEntity<Object> getAllCustomers(@PathVariable UUID uuid) {
        log.info("requested uuid: {}", uuid);

        if (customerMap.containsKey(uuid) && customerMap.get(uuid) != null) {
            var customers = customerMap.get(uuid);
            customerMap.remove(uuid);
            if (customers.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            } else {
                return ResponseEntity.status(HttpStatus.OK).body(customers);
            }
        } else if (customerMap.containsKey(uuid) && customerMap.get(uuid) == null) {
            return ResponseEntity.status(HttpStatus.ACCEPTED).build();
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("UUID not found in the map");
        }
    }
}
