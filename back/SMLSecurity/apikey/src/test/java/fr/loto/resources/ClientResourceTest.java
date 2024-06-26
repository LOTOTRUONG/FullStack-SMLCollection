package fr.loto.resources;
import fr.loto.dto.NewClientDto;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.parsing.Parser;

import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.*;



import static org.hamcrest.Matchers.*;
import static io.restassured.RestAssured.given;


@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@QuarkusTest
@QuarkusTestResource(WireMockEmailService.class)
class ClientResourceTest {
    private static final String permanentSuperAdminToken = "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJpc3MiOiJqd3QtdG9rZW4iLCJzdWIiOiJhZG1pbiIsImdyb3VwcyI6WyJzdXBlcmFkbWluIl0sImlhdCI6MTcxMjA0OTk2MiwiZXhwIjozMzI0ODA0OTk2MiwianRpIjoiZjVlYWNmYzUtMmRkZC00OGFlLWEwOWYtNjM3ZjliOTQ4NGVhIn0.KKD6sWVA-e7t39bQYbNFKPP8QnCCGZ8UPwkFjkjO-IbHor7oTHSBKEAoj31OFnS27VagR_-dHSgCegThsJgsTMmylGW7dbzHsH4r7v47UdVwDGXk2khLrKfgAyO0wxB5SB2pkzo-_bXvHqcmzWxY08NVRghGxayDxcH3LpBzAszMylRssb7YoWThdgYdDlNY2voGFBfycA_wQDXmHIfXgUvimn2-zL5A5m5vPwyi2hiEgC-pc3Iz72u6A6J586XknD5w1A_KMtxirhrZK9UZ2wukw7kUuXEdf0K2kfJtPTC6hAPX_8cgkHG2Ovff89rSoZhhUsnyZbw1u0Buks2Bfg";
    private static final String permanentUserToken = "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJpc3MiOiJqd3QtdG9rZW4iLCJzdWIiOiJ1c2VyMDEiLCJncm91cHMiOlsidXNlciJdLCJpYXQiOjE3MTIxMjkzMzIsImV4cCI6MTc0ODAxMjkzMzIsImp0aSI6ImQ1MWI3MTczLWMyYTAtNGQzZi1hMzM4LTM4ZDk5MzJiM2IyZiJ9.C89ER3nqUWB812UTBCuHiw7ItVPO5lQ6E3dd5hrMGufz8fCmWvgS7220cIDjz4Zgsdd74FO71UGNpaKzQWoc4ah5dXVveo3u1zk-4uvpSDKhMjG-TmqsgcAKq4hfN1VuUlqdcMXbxDoOxtrDAkzQmkbCK3eB2rJ8xPfeREN3H_snS5Dt2kPqkfy36xkMP6G4whvdwT2JcHr_qAzoOy-A1K6b0V-PBoVdVPy1C-x_EkTs598X5-B8_sVGeNhPKP6-Nyo1KeXVrmpYrCsfGRjokXsu-VzAh1q49BWDDKl2iAxVnC1i_hoAlBB_1tolVjbmpf15B3WuceyXjFaNCigc9A";

    @BeforeAll
    static void setUp() {
        RestAssured.defaultParser = Parser.JSON;
    }
    @Test
    @Order(1)
    void testGetAllClients_Success() {
        given().contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + permanentSuperAdminToken)
                .when()
                .get("/clients")
                .then()
                .statusCode(Response.Status.OK.getStatusCode())
                .body("size()", equalTo(3))
                .body("idClient", hasItem(1))
                .body("emailClient[0]", equalTo("test@gmail.com"))
                .body("nomClient[0]", equalTo("testClient"))
                .body("apiKey[0]", equalTo("TESTAPIKEY"))
                .body("monthlyQuota[0]", equalTo(5));

    }
    @Test
    @Order(2)
    void testGetAllClients_RoleNotAllow() {
        given().contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + permanentUserToken)
                .when()
                .get("/clients")
                .then()
                .statusCode(Response.Status.FORBIDDEN.getStatusCode());

    }
    @Test
    @Order(3)
    void testGetAllClients_NoToken() {
        given().contentType(ContentType.JSON)
                .when()
                .get("/clients")
                .then()
                .statusCode(Response.Status.UNAUTHORIZED.getStatusCode());

    }

    @Test
    @Order(4)
    void testGetClientById_ClientFound() {
        Integer id = 1;
        given().contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + permanentSuperAdminToken)
                .when()
                .get("/clients/{id}", id)
                .then()
                .statusCode(Response.Status.OK.getStatusCode())
                .body("idClient", equalTo(1))
                .body("emailClient", equalTo("test@gmail.com"))
                .body("nomClient", equalTo("testClient"))
                .body("apiKey", equalTo("TESTAPIKEY"))
                .body("monthlyQuota", equalTo(5));
    }
    @Test
    @Order(5)
    void testGetClientById_ClientFound_RoleNotAllow() {
        Integer id = 1;
        given().contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + permanentUserToken)
                .when()
                .get("/clients/{id}", id)
                .then()
                .statusCode(Response.Status.FORBIDDEN.getStatusCode());
    }
    @Test
    @Order(6)
    void testGetClientById_ClientNOTFound() {
        Integer id = 0;
        given().contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + permanentSuperAdminToken)
                .when()
                .get("/clients/{id}", id)
                .then()
                .statusCode(Response.Status.NOT_FOUND.getStatusCode());
    }

    @Test
    @Order(7)
    void testCreateNewClient_Success() {
        String testEmail = "test50@gmail.com";
        String testNom = "testClient50";
        Integer testMonthlyQuota = 5;
        // Creating request body
        NewClientDto newClientDto = new NewClientDto();
        newClientDto.setEmailClient(testEmail);
        newClientDto.setNomClient(testNom);
        newClientDto.setMonthlyQuota(testMonthlyQuota);

        given().contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + permanentSuperAdminToken)
                .body(newClientDto) // Set the request body
                .when()
                .post("/clients")
                .then()
                .statusCode(Response.Status.CREATED.getStatusCode());

    }
    @Test
    @Order(8)
    void testCreateNewClient_RoleNotAllow() {
        String testEmail = "test5@gmail.com";
        String testNom = "testClient5";
        Integer testMonthlyQuota = 5;
        NewClientDto newClientDto = new NewClientDto();
        newClientDto.setEmailClient(testEmail);
        newClientDto.setNomClient(testNom);
        newClientDto.setMonthlyQuota(testMonthlyQuota);

        given().contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + permanentUserToken)
                .body(newClientDto)
                .when()
                .post("/clients")
                .then()
                .statusCode(Response.Status.FORBIDDEN.getStatusCode());

    }

    @Test
    @Order(9)
    void testDeleteClient_ClientFound() {
        Integer id = 3;
        given().contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + permanentSuperAdminToken)
                .when()
                .delete("/clients/{id}", id)
                .then()
                .statusCode(Response.Status.OK.getStatusCode())
                .body(equalTo("Success deleting the client ID " + id));
    }
    @Test
    @Order(10)
    void testDeleteClient_ClientNotFound() {
        Integer id = 8;
        given().contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + permanentSuperAdminToken)
                .when()
                .delete("/clients/{id}", id)
                .then()
                .statusCode(Response.Status.NOT_FOUND.getStatusCode());
    }

    @Test
    @Order(11)
    void testUpdateMonthlyQuota_Success() {
        Integer id = 2;
        Integer updateMonthlyQuota = 20;
        given().contentType(ContentType.JSON)
                .queryParam("quota", updateMonthlyQuota)
                .when()
                .put("/clients/{id}/quota", id)
                .then()
                .statusCode(Response.Status.OK.getStatusCode())
                .body(equalTo("Success updating the monthly quota for client ID: " + id + ". New quota is " + updateMonthlyQuota));
    }

    @Test
    @Order(12)
    void testResetApiKey_Success() {
        Integer id = 1;
        given().contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + permanentSuperAdminToken)
                .when()
                .put("/clients/{id}/newKey",id)
                .then()
                .statusCode(Response.Status.OK.getStatusCode())
                .body(equalTo("Success update APIKey for the client ID " + id));
    }

    @Test
    @Order(13)
    void testResetApiKey_ClientNotFound() {
        Integer id = 8;
        given().contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + permanentSuperAdminToken)
                .when()
                .put("/clients/{id}/newKey",id)
                .then()
                .statusCode(Response.Status.NOT_FOUND.getStatusCode())
                .body(equalTo("Client not found"));
    }
}