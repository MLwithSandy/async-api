package com.example.demo.service;

import com.example.demo.dao.CustomerDao;
import com.example.demo.model.Customer;
import com.example.demo.util.CSVOperation;
import com.mysql.cj.conf.ConnectionUrlParser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    private CustomerDao customerDao;

    @Override
    @Async
    public CompletableFuture<List<Customer>> saveCustomers(MultipartFile file) {
        List<Customer> customers = null;
        Long start = System.currentTimeMillis();
        customers = customerDao.saveAllCustomers(CSVOperation.parseCSVFile(file));
        Long end = System.currentTimeMillis();
        log.info("Total time {}", (end - start));
        return CompletableFuture.completedFuture(customers);
    }

    @Override
    public List<Customer> getCustomers() {
        return customerDao.getCustomers();
    }

    @Override
    public CompletableFuture<List<Customer>> getCustomersUsingAsync() {
        List<Customer> customers = customerDao.getCustomers();
        try {
            Thread.sleep(10000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return CompletableFuture.completedFuture(customers);
    }

    @Override
    public CompletableFuture<Pair<UUID, List<Customer>>> getCustomersWithUUIDUsingAsync(UUID uuid) {
        // generate a random number between 0 and 1500 and use this to get a random sublist of customers
        var randomStart = (int) (Math.random() * 1500);

        // generate a random number between randomStart and 3000 and use this to get a random sublist of customers
        var randomEnd = (int) (Math.random() * 3000) + randomStart;

        List<Customer> customers = customerDao.getCustomers().subList(randomStart, randomEnd);
        try {
            Thread.sleep(30000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        var pair = CompletableFuture.completedFuture(Pair.of(uuid, customers));
        return pair;
    }
}
