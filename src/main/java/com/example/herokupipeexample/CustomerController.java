package com.example.herokupipeexample;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import com.codahale.metrics.Counter;
import com.codahale.metrics.Histogram;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Timer;
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
    private final Counter postReqs;

    @Autowired
    public CustomerController(CustomerRepository customerRepository, MetricRegistry registry) {
        this.customerRepository = customerRepository;
        this.registry = registry;
        postReqs =  registry.counter(name(Customer.class, "postReqs"));
    }

    private final MetricRegistry registry;

    @RequestMapping("/")
    public String welcome() {
        registry.meter("Root entrypoint").mark();

        return "Welcome to this small REST service. It will accept a GET on /list with a request parameter lastName, and a POST to / with a JSON payload with firstName and lastName as values.";
    }

    @RequestMapping("/list")
    public List<Customer> find(@RequestParam(value = "lastName") String lastName) {
        Timer responses = registry.timer(name(Customer.class, "responses"));

        final Timer.Context context = responses.time();
        registry.meter("List entrypoint").mark();
        //doing this just to show you can get the size, easily
        customerRepository.save(new Customer("test", "test"));
        List<Customer> results = customerRepository.findByLastName(lastName);
        final Histogram resultCounts = registry.histogram(name(Customer.class, "result-counts"));
        resultCounts.update(results.size());
        //deleting it again to make  not spam the db
        customerRepository.delete(customerRepository.findByLastName("test").get(0));
        try {
            return results;
        } finally {
            context.stop();
        }
    }

    @PostMapping("/")
    Customer newCustomer(@RequestBody Customer customer) {
        postReqs.inc();
        registry.meter("Create user entrypoint").mark();

        return customerRepository.save(customer);
    }

}
