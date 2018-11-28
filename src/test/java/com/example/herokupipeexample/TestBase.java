package com.example.herokupipeexample;

import com.jayway.restassured.RestAssured;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class TestBase {

    @Before
    public void setUp() throws Exception {
        System.out.println("hello");

    }
    @Test
    public void test(){
        assertThat(1,equalTo(1));
    }

}
