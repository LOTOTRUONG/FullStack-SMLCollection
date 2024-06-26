package fr.loto.resources;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.parsing.Parser;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.*;
import static io.restassured.RestAssured.given;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@QuarkusTest
@QuarkusTestResource(WireMockEmailService.class)
class ApiKeyResourceTest {
    @BeforeAll
    static void setUp() {
        RestAssured.defaultParser = Parser.JSON;
    }

    @Test
    @Order(1)
    void testGetClientByApiKey() {
        String key = "TESTAPIKEY";
        given().when()
                .get("/apikeys/{keys}", key)
                .then()
                .log()
                .all()
                .statusCode(Response.Status.OK.getStatusCode());
    }
    @Test
    @Order(2)
    void testGetClientByApiKey_NOTFOUND() {
        String key = "TESTAPIKEYNOTFOUND";
        given().when()
                .get("/apikeys/{keys}", key)
                .then()
                .log()
                .all()
                .statusCode(Response.Status.NOT_FOUND.getStatusCode());
    }

    @Test
    @Order(3)
    void testGetCountEmail_Success() {
        String key = "TESTAPIKEY";
        given().when()
                .get("/apikeys/{keys}/countEmail", key)
                .then()
                .log()
                .all()
                .statusCode(Response.Status.OK.getStatusCode());
    }


    @Test
    @Order(4)
    void testSaveMail_Success() {
        // Create a JSON object with email record data
        JsonObject emailRecord = Json.createObjectBuilder()
                .add("sendTo", "pelovejeily@gmail.com")
                .add("subject", "TestSaveEmail")
                .build();
        String key = "TESTAPIKEY";

        // Send POST request with the email record data
        given()
                .contentType(ContentType.JSON)
                .body(emailRecord.toString())
                .when()
                .post("/apikeys/{keys}/email", key)
                .then()
                .log()
                .all()
                .statusCode(Response.Status.OK.getStatusCode());
    }


}