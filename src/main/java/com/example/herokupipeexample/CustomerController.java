package com.example.herokupipeexample;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import com.codahale.metrics.Counter;
import com.codahale.metrics.Histogram;
import com.codahale.metrics.MetricRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.codahale.metrics.MetricRegistry.name;

@RestController
public class CustomerController {

    private CustomerRepository customerRepository;

    @Autowired
    public CustomerController(CustomerRepository customerRepository ,MetricRegistry registry) {
      this.customerRepository = customerRepository;
        this.registry = registry;
    }
    private final MetricRegistry registry;
    @RequestMapping("/")
    public String welcome() {
        registry.meter("Root entrypoint").mark();

        return "Welcome to this small REST service. It will accept a GET on /list with a request parameter lastName, and a POST to / with a JSON payload with firstName and lastName as values.";
    }

    @RequestMapping("/list")
    public List<Customer> find(@RequestParam(value="lastName") String lastName) {
        registry.meter("List entrypoint").mark();
        List<Customer> results = customerRepository.findByLastName(lastName);
        final Histogram resultCounts = registry.histogram(name(Customer.class, "result-counts"));
        resultCounts.update(results.size());
        return  results;
    }

    @PostMapping("/")
    	Customer newCustomer(@RequestBody Customer customer) {
        System.out.println(customer);
        registry.meter("Create user entrypoint").mark();

        return customerRepository.save(customer);
    	}

}
