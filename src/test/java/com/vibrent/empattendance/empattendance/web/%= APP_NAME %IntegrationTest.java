package com.vibrent.empattendance.empattendance.web;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.net.URL;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;

import org.springframework.kafka.test.context.EmbeddedKafka;

import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext

@EmbeddedKafka(topics = { "MYTOPIC1","MYTOPIC2" }) // Required for allowing Kafka initialization for test
@TestPropertySource(properties = { "spring.kafka.bootstrap-servers=${spring.embedded.kafka.brokers}" })

public class EmpattendanceIntegrationTest {

  @LocalServerPort
  private int port;

  private URL base;

  @Autowired
  private TestRestTemplate template;

  @Before
  public void setUp() throws Exception {
      this.base = new URL("http://localhost:" + port + "/");
  }

  @Test
  public void getHello() throws Exception {
      ResponseEntity<String> response = template.getForEntity(base.toString(),
              String.class);
      assertThat(response.getBody(), equalTo("Greetings from Spring Boot!"));
  }

}
