package fr.sml.objet.resource;

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
class ObjetResourceTest {
    private static final String permanentAdminToken = "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJpc3MiOiJqd3QtdG9rZW4iLCJzdWIiOiJhZG1pbiIsImdyb3VwcyI6WyJzdXBlcmFkbWluIl0sImlhdCI6MTcxMjA0OTk2MiwiZXhwIjozMzI0ODA0OTk2MiwianRpIjoiZjVlYWNmYzUtMmRkZC00OGFlLWEwOWYtNjM3ZjliOTQ4NGVhIn0.KKD6sWVA-e7t39bQYbNFKPP8QnCCGZ8UPwkFjkjO-IbHor7oTHSBKEAoj31OFnS27VagR_-dHSgCegThsJgsTMmylGW7dbzHsH4r7v47UdVwDGXk2khLrKfgAyO0wxB5SB2pkzo-_bXvHqcmzWxY08NVRghGxayDxcH3LpBzAszMylRssb7YoWThdgYdDlNY2voGFBfycA_wQDXmHIfXgUvimn2-zL5A5m5vPwyi2hiEgC-pc3Iz72u6A6J586XknD5w1A_KMtxirhrZK9UZ2wukw7kUuXEdf0K2kfJtPTC6hAPX_8cgkHG2Ovff89rSoZhhUsnyZbw1u0Buks2Bfg";
    private static final String permanentUserToken = "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJpc3MiOiJqd3QtdG9rZW4iLCJzdWIiOiJ1c2VyMDEiLCJncm91cHMiOlsidXNlciJdLCJpYXQiOjE3MTIxMjkzMzIsImV4cCI6MTc0ODAxMjkzMzIsImp0aSI6ImQ1MWI3MTczLWMyYTAtNGQzZi1hMzM4LTM4ZDk5MzJiM2IyZiJ9.C89ER3nqUWB812UTBCuHiw7ItVPO5lQ6E3dd5hrMGufz8fCmWvgS7220cIDjz4Zgsdd74FO71UGNpaKzQWoc4ah5dXVveo3u1zk-4uvpSDKhMjG-TmqsgcAKq4hfN1VuUlqdcMXbxDoOxtrDAkzQmkbCK3eB2rJ8xPfeREN3H_snS5Dt2kPqkfy36xkMP6G4whvdwT2JcHr_qAzoOy-A1K6b0V-PBoVdVPy1C-x_EkTs598X5-B8_sVGeNhPKP6-Nyo1KeXVrmpYrCsfGRjokXsu-VzAh1q49BWDDKl2iAxVnC1i_hoAlBB_1tolVjbmpf15B3WuceyXjFaNCigc9A";

    @BeforeEach
    void setUp() {
        RestAssured.defaultParser = Parser.JSON;
    }

    @Test

    @Order(1)
    void testGetAllObject_Success() {
        given().contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + permanentAdminToken)
                .when()
                .get("/objects")
                .then()
                .statusCode(Response.Status.OK.getStatusCode())
                .body("size()", equalTo(3))
                .body("idObjet", hasItem(1))
                .body("nomObjet[0]", equalTo("TestObjet1"))
                .body("login[0]", equalTo("TestLogin1"));
    }
    @Test
    @Order(2)
    void testGetAllObject_RoleNotAllow() {
        given().contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + permanentUserToken)
                .when()
                .get("/objects")
                .then()
                .statusCode(Response.Status.FORBIDDEN.getStatusCode());
    }

    @Test
    @Order(3)
    void testUpdateObjectByAdmin() {
        Integer id = 2;
        String updatedNameObject = "UpdatedTestObjet2";
        String updatedNameCountry = "UpdatedTESTPAY2";
        String updatedTypeObject = "UpdatedTestType2";
        given().contentType(ContentType.JSON)
                .queryParam("1-nomObjet", updatedNameObject)
                .queryParam("2-Pays", updatedNameCountry)
                .queryParam("3-Type", updatedTypeObject)
                .when()
                .put("/objects/{id}", id)
                .then()
                .statusCode(Response.Status.OK.getStatusCode());

    }
    @Test
    @Order(4)
    void testDeleteObjectByAdmin() {
        Integer id = 3;
        given().contentType(ContentType.JSON)
                .when()
                .delete("/objects/{id}", id)
                .then()
                .statusCode(Response.Status.OK.getStatusCode());

    }

    @Test
    @Order(5)
    void testGetObjectByLoginUser() {
        String testLogin = "TestLogin1";
        given().contentType(ContentType.JSON)
                .when()
                .get("/objects/{login}", testLogin)
                .then()
                .statusCode(Response.Status.OK.getStatusCode());

        String testLoginNotFound = "TestLoginNotFound";
        given().contentType(ContentType.JSON)
                .when()
                .get("/objects/{login}", testLoginNotFound)
                .then()
                .statusCode(Response.Status.NOT_FOUND.getStatusCode());
    }

    @Test
    @Order(6)
    void testCreateNewObject() {
        String login = "TestLogin2";
        String loginNotFound = "TestLoginNotFound";
        String newObject = "TestObjet4";
        String newExistedCountry = "TESTPAY1";
        String newCountry = "NonExistentCountry";
        String newExistedType = "TestType1";
        String newType = "NonExistentType";

        given().contentType(ContentType.JSON)
                .when()
                .post("/objects/{login}", loginNotFound)
                .then()
                .statusCode(Response.Status.NOT_FOUND.getStatusCode());

        given().contentType(ContentType.JSON)
                .queryParam("1-nomObjet", newObject)
                .queryParam("2-Pays", newExistedCountry)
                .queryParam("3-Type", newExistedType)
                .when()
                .post("/objects/{login}", login)
                .then()
                .statusCode(Response.Status.OK.getStatusCode())
                .body("libelleObjet", equalTo(newObject));

        given().contentType(ContentType.JSON)
                .queryParam("1-nomObjet", newObject)
                .queryParam("2-Pays", newCountry)
                .queryParam("3-Type", newExistedType)
                .when()
                .post("/objects/{login}", login)
                .then()
                .statusCode(Response.Status.NOT_FOUND.getStatusCode());


        given().contentType(ContentType.JSON)
                .queryParam("1-nomObjet", newObject)
                .queryParam("2-Pays", newExistedCountry)
                .queryParam("3-Type", newType)
                .when()
                .post("/objects/{login}", login)
                .then()
                .statusCode(Response.Status.NOT_FOUND.getStatusCode());


    }
    @Test
    @Order(6)
    void testCreateNewObject_PaysTypeNotInList() {
        String login = "TestLogin2";
        String newObject = "TestObjet4";
        String newCountry = "NonExistentCountry";
        String newType = "NonExistentType";

        given().contentType(ContentType.JSON)
                .queryParam("1-nomObjet", newObject)
                .queryParam("2-Pays", newCountry)
                .queryParam("3-Type", newType)
                .pathParam("login", login)
                .when()
                .post("/objects/{login}")
                .then()
                .statusCode(Response.Status.NOT_FOUND.getStatusCode());
    }

    @Test
    @Order(7)
    void testUpdateObject_Success() {
        Integer id = 1;
        Integer idObjectNotFound = 9;
        String login = "TestLogin1";
        String newObject = "TestObjet4";
        String newExistedCountry = "TESTPAY1";
        String newExistedType = "TestType1";

        given().contentType(ContentType.JSON)
                .when()
                .put("/objects/{login}/{id}", login, idObjectNotFound)
                .then()
                .statusCode(Response.Status.NOT_FOUND.getStatusCode())
                .body(equalTo("Object not found"));

        given().contentType(ContentType.JSON)
                .queryParam("1-nomObjet", newObject)
                .queryParam("2-Pays", newExistedCountry)
                .queryParam("3-Type", newExistedType)
                .when()
                .put("/objects/{login}/{id}", login, id)
                .then()
                .statusCode(Response.Status.OK.getStatusCode())
                .body("libelleObjet", equalTo(newObject));

    }

    @Test
    @Order(8)
    void testUpdateObject_PaysTypeNotInList() {
        Integer id = 1;
        String login = "TestLogin1";
        String updatedNameObject = "UpdatedTestObjet1";
        String updatedNameCountry = "NonExistentCountry";
        String updatedTypeObject = "NonExistentType";

        given().contentType(ContentType.JSON)
                .queryParam("1-nomObjet", updatedNameObject)
                .queryParam("2-Pays", updatedNameCountry)
                .queryParam("3-Type", updatedTypeObject)
                .pathParam("id", id)
                .pathParam("login", login)
                .when()
                .put("/objects/{login}/{id}")
                .then()
                .statusCode(Response.Status.NOT_FOUND.getStatusCode());
    }

    @Test
    @Order(9)
    void testDeleteObject() {
        String login = "TestLogin1";
        Integer id = 2;
        Integer idObjectNotFound = 9;

        given().contentType(ContentType.JSON)
                .pathParam("login", login)
                .pathParam("id", idObjectNotFound)
                .when()
                .delete("/objects/{login}/{id}")
                .then()
                .statusCode(Response.Status.NOT_FOUND.getStatusCode());

        //test delete successfully
        given().contentType(ContentType.JSON)
                .pathParam("login", login)
                .pathParam("id", id)
                .when()
                .delete("/objects/{login}/{id}")
                .then()
                .statusCode(Response.Status.OK.getStatusCode());
    }
}