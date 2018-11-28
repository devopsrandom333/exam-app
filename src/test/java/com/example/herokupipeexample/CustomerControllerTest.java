package com.example.herokupipeexample;

import com.jayway.restassured.RestAssured;
import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;


public class CustomerControllerTest {

    @Test
    public void welcome() {
       assertThat(1,equalTo(1));
    }

    @Test
    public void find() {
    }

    @Test
    public void newCustomer() {
    }
}