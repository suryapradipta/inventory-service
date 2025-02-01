package com.example.inventoryservice;

import io.restassured.RestAssured;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Import;
import org.testcontainers.containers.PostgreSQLContainer;

@Import(TestcontainersConfiguration.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class InventoryServiceApplicationTests {

    @ServiceConnection
    public static PostgreSQLContainer postgreSQLContainer = new PostgreSQLContainer("postgres:17.2");

    @LocalServerPort
    private Integer port;

    static {
        postgreSQLContainer.start();
    }

    @BeforeEach
    void setUp() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = port;
    }

    @Test
    void shouldCheckInventory() {
        String skuCode = "SKU12345";
        int validQuantity = 100;
        Boolean foundProduct = RestAssured.given()
                .when()
                .get("api/inventory/check" + "?skuCode=" + skuCode + "&quantity=" + validQuantity)
                .then()
                .log().all()
                .statusCode(200)
                .extract().body().as(Boolean.class);

        Assertions.assertTrue(foundProduct);

        int invalidQuantity = 101;
        Boolean notFoundProduct = RestAssured.given()
                .when()
                .get("api/inventory/check" + "?skuCode=" + skuCode + "&quantity=" + invalidQuantity)
                .then()
                .log().all()
                .statusCode(200)
                .extract().body().as(Boolean.class);

        Assertions.assertFalse(notFoundProduct);

    }

}
