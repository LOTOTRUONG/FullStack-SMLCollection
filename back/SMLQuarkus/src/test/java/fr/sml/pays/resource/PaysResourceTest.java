package fr.sml.pays.resource;

import fr.sml.WireMockEmailService;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.parsing.Parser;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.*;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItem;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@QuarkusTest
@QuarkusTestResource(WireMockEmailService.class)

class PaysResourceTest {
    private static final String permanentAdminToken = "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJpc3MiOiJqd3QtdG9rZW4iLCJzdWIiOiJhZG1pbiIsImdyb3VwcyI6WyJzdXBlcmFkbWluIl0sImlhdCI6MTcxMjA0OTk2MiwiZXhwIjozMzI0ODA0OTk2MiwianRpIjoiZjVlYWNmYzUtMmRkZC00OGFlLWEwOWYtNjM3ZjliOTQ4NGVhIn0.KKD6sWVA-e7t39bQYbNFKPP8QnCCGZ8UPwkFjkjO-IbHor7oTHSBKEAoj31OFnS27VagR_-dHSgCegThsJgsTMmylGW7dbzHsH4r7v47UdVwDGXk2khLrKfgAyO0wxB5SB2pkzo-_bXvHqcmzWxY08NVRghGxayDxcH3LpBzAszMylRssb7YoWThdgYdDlNY2voGFBfycA_wQDXmHIfXgUvimn2-zL5A5m5vPwyi2hiEgC-pc3Iz72u6A6J586XknD5w1A_KMtxirhrZK9UZ2wukw7kUuXEdf0K2kfJtPTC6hAPX_8cgkHG2Ovff89rSoZhhUsnyZbw1u0Buks2Bfg";
    private static final String permanentUserToken = "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJpc3MiOiJqd3QtdG9rZW4iLCJzdWIiOiJ1c2VyMDEiLCJncm91cHMiOlsidXNlciJdLCJpYXQiOjE3MTIxMjkzMzIsImV4cCI6MTc0ODAxMjkzMzIsImp0aSI6ImQ1MWI3MTczLWMyYTAtNGQzZi1hMzM4LTM4ZDk5MzJiM2IyZiJ9.C89ER3nqUWB812UTBCuHiw7ItVPO5lQ6E3dd5hrMGufz8fCmWvgS7220cIDjz4Zgsdd74FO71UGNpaKzQWoc4ah5dXVveo3u1zk-4uvpSDKhMjG-TmqsgcAKq4hfN1VuUlqdcMXbxDoOxtrDAkzQmkbCK3eB2rJ8xPfeREN3H_snS5Dt2kPqkfy36xkMP6G4whvdwT2JcHr_qAzoOy-A1K6b0V-PBoVdVPy1C-x_EkTs598X5-B8_sVGeNhPKP6-Nyo1KeXVrmpYrCsfGRjokXsu-VzAh1q49BWDDKl2iAxVnC1i_hoAlBB_1tolVjbmpf15B3WuceyXjFaNCigc9A";

    @BeforeEach
    void setUp() {
        RestAssured.defaultParser = Parser.JSON;
    }

    @Test
    @Order(1)
    void testGetAllCountries() {
        given().contentType(ContentType.JSON)
                .when()
                .get("/countries")
                .then()
                .statusCode(Response.Status.OK.getStatusCode())
                .body("size()", equalTo(3))
                .body("idPays", hasItem(1))
                .body("nomPays[0]", equalTo("TESTPAY1")); // It is nomPays instead of libellePays because we show paysDTO in the response
    }

    @Test
    @Order(2)
    void testGetCountryById_CountryFOUND() {
        Integer testId = 1;
        given().contentType(ContentType.JSON)
                .when()
                .get("/countries/{id}", testId)
                .then()
                .statusCode(Response.Status.OK.getStatusCode())
                .body("nomPays", equalTo("TESTPAY1"));
    }

    @Test
    @Order(3)
    void testGetCountryById_CountryNotFound() {
        Integer testId = 8;
        given().contentType(ContentType.JSON)
                .when()
                .get("/countries/{id}", testId)
                .then()
                .statusCode(Response.Status.NOT_FOUND.getStatusCode())
                .body(equalTo("ID is not existed"));
    }

    @Test
    @Order(4)
    void testCreateNewCountry_Success() {
        String testNameCountry = "TESTPAY4";
        given().contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + permanentAdminToken)
                .queryParam("Name_Country", testNameCountry)
                .when()
                .post(("/countries"))
                .then()
                .statusCode(Response.Status.CREATED.getStatusCode())
                .body(equalTo("Insert successfully"));
    }
    @Test
    @Order(5)
    void testCreateNewCountry_RoleNotAllow() {
        String testNameCountry = "TESTPAY5";
        given().contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + permanentUserToken)
                .queryParam("Name_Country", testNameCountry)
                .when()
                .post(("/countries"))
                .then()
                .statusCode(Response.Status.FORBIDDEN.getStatusCode());
    }

    @Test
    @Order(6)
    void testCreateNewCountry_NoRole() {
        String testNameCountry = "TESTPAY6";
        given().contentType(ContentType.JSON)
                .queryParam("Name_Country", testNameCountry)
                .when()
                .post(("/countries"))
                .then()
                .statusCode(Response.Status.UNAUTHORIZED.getStatusCode());
    }


    @Test
    @Order(7)
    void testCreateNewCountry_DuplicatedName() {
        String testNameCountry = "TESTPAY1";
        given().contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + permanentAdminToken)
                .queryParam("Name_Country", testNameCountry)
                .when()
                .post("/countries")
                .then()
                .statusCode(Response.Status.CONFLICT.getStatusCode())
                .body(equalTo("This name is duplicated"));
    }

    @Test
    @Order(8)
    void testUpdateNameCountry_Success() {
        Integer id = 2;
        String testNameCountry = "UPDATEDTESTPAY2";
        given().contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + permanentAdminToken)
                .queryParam("Name_Country", testNameCountry)
                .when()
                .put("/countries/{id}", id)
                .then()
                .statusCode(Response.Status.OK.getStatusCode())
                .body("idPays",equalTo(2))
                .body("libellePays", equalTo("UPDATEDTESTPAY2"));
    }

    @Test
    @Order(9)
    void testUpdateNameCountry_CountryNotFound() {
        Integer id = 8;
        String testNameCountry = "UPDATEDTESTPAY";
        given().contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + permanentAdminToken)
                .queryParam("Name_Country", testNameCountry)
                .when()
                .put("/countries/{id}", id)
                .then()
                .statusCode(Response.Status.NOT_FOUND.getStatusCode())
                .body(equalTo("ID is not found"));
    }
    @Test
    @Order(10)
    void testUpdateNameCountry_DuplicateName() {
        Integer id = 3;
        String testNameCountry = "TESTPAY1";
        given().contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + permanentAdminToken)
                .queryParam("Name_Country", testNameCountry)
                .when()
                .put("/countries/{id}", id)
                .then()
                .statusCode(Response.Status.CONFLICT.getStatusCode())
                .body(equalTo("This name is duplicated"));
    }

    @Test
    @Order(11)
    void testDeleteCountry_Success() {
        Integer id = 3;
        given().contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + permanentAdminToken)
                .when()
                .delete("/countries/{id}", id)
                .then()
                .statusCode(Response.Status.OK.getStatusCode());
    }

    @Test
    @Order(12)
    void testDeleteCountry_CountryNotFound() {
        Integer id = 8;
        given().contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + permanentAdminToken)
                .when()
                .delete("/countries/{id}", id)
                .then()
                .statusCode(Response.Status.NOT_FOUND.getStatusCode());
    }

    @Test
    @Order(13)
    void testDeleteCountry_RoleNotAllow() {
        Integer id = 2;
        given().contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + permanentUserToken)
                .when()
                .delete("/countries/{id}", id)
                .then()
                .statusCode(Response.Status.FORBIDDEN.getStatusCode());
    }
}