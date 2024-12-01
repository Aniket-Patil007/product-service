package com.techie.microservices.product;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Import;
import org.testcontainers.containers.MongoDBContainer;

import io.restassured.RestAssured;

@Import(TestcontainersConfiguration.class)
@SpringBootTest(webEnvironment=SpringBootTest.WebEnvironment.RANDOM_PORT)
class ProductServiceApplicationTests {

	@ServiceConnection
	static MongoDBContainer mongoDBContainer= new MongoDBContainer("mongo");
	
	@LocalServerPort
	private Integer port;
	
	@BeforeEach
	void voidSetup() {
		RestAssured.baseURI="http://localhost";
		RestAssured.port=port;
	}
	
	static {
		mongoDBContainer.start();
	}
	
	@Test
	void shouldCreateProduct() {
		
		String resquestBody="""
					 {
					   "name": "Nokia",
					   "description": "nokia 1100 256Gb smartphone from nokia",
					   "price": 100000
					 }
			   """;
			RestAssured.given()
			.contentType("application/json")
			.body(resquestBody)
			.when()
			.post("/api/product/insert")
			.then()
			.statusCode(200)
			.body("id", Matchers.notNullValue())
			.body("name", Matchers.equalTo("Nokia"))
			.body("description", Matchers.equalTo("nokia 1100 256Gb smartphone from nokia"))
			.body("price", Matchers.equalTo(100000));
	}

}
