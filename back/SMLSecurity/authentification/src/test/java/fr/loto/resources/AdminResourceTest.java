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
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItem;
import static org.junit.jupiter.api.Assertions.*;
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@QuarkusTest
@QuarkusTestResource(WireMockEmailService.class)
class AdminResourceTest {
    private static final String permanentAdminToken = "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJpc3MiOiJqd3QtdG9rZW4iLCJzdWIiOiJhZG1pbiIsImdyb3VwcyI6WyJzdXBlcmFkbWluIl0sImlhdCI6MTcxMjA0OTk2MiwiZXhwIjozMzI0ODA0OTk2MiwianRpIjoiZjVlYWNmYzUtMmRkZC00OGFlLWEwOWYtNjM3ZjliOTQ4NGVhIn0.KKD6sWVA-e7t39bQYbNFKPP8QnCCGZ8UPwkFjkjO-IbHor7oTHSBKEAoj31OFnS27VagR_-dHSgCegThsJgsTMmylGW7dbzHsH4r7v47UdVwDGXk2khLrKfgAyO0wxB5SB2pkzo-_bXvHqcmzWxY08NVRghGxayDxcH3LpBzAszMylRssb7YoWThdgYdDlNY2voGFBfycA_wQDXmHIfXgUvimn2-zL5A5m5vPwyi2hiEgC-pc3Iz72u6A6J586XknD5w1A_KMtxirhrZK9UZ2wukw7kUuXEdf0K2kfJtPTC6hAPX_8cgkHG2Ovff89rSoZhhUsnyZbw1u0Buks2Bfg";
    private static final String permanentUserToken = "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJpc3MiOiJqd3QtdG9rZW4iLCJzdWIiOiJ1c2VyMDEiLCJncm91cHMiOlsidXNlciJdLCJpYXQiOjE3MTIxMjkzMzIsImV4cCI6MTc0ODAxMjkzMzIsImp0aSI6ImQ1MWI3MTczLWMyYTAtNGQzZi1hMzM4LTM4ZDk5MzJiM2IyZiJ9.C89ER3nqUWB812UTBCuHiw7ItVPO5lQ6E3dd5hrMGufz8fCmWvgS7220cIDjz4Zgsdd74FO71UGNpaKzQWoc4ah5dXVveo3u1zk-4uvpSDKhMjG-TmqsgcAKq4hfN1VuUlqdcMXbxDoOxtrDAkzQmkbCK3eB2rJ8xPfeREN3H_snS5Dt2kPqkfy36xkMP6G4whvdwT2JcHr_qAzoOy-A1K6b0V-PBoVdVPy1C-x_EkTs598X5-B8_sVGeNhPKP6-Nyo1KeXVrmpYrCsfGRjokXsu-VzAh1q49BWDDKl2iAxVnC1i_hoAlBB_1tolVjbmpf15B3WuceyXjFaNCigc9A";

    @BeforeEach
    void setUp() {
        RestAssured.defaultParser = Parser.JSON;
    }

    @Test
    @Order(1)
    void testGetAllUsers_Success() {
        given().contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + permanentAdminToken)
                .when()
                .get("/admin/allUsers")
                .then()
                .statusCode(Response.Status.OK.getStatusCode())
                .body("size()", equalTo(6))
                .body("login", hasItem("admin"))
                .body("email[0]", equalTo("test1@gmail.com"))
                .body("role[0]", equalTo("superadmin"));
    }
    @Test
    @Order(2)
    void testGetAllUsers_RoleNotAllowed() {
        given().contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + permanentUserToken)
                .when()
                .get("/admin/allUsers")
                .then()
                .statusCode(Response.Status.FORBIDDEN.getStatusCode());
    }
    @Test
    @Order(3)
    void testGetAllUsers_NoToken() {
        given().contentType(ContentType.JSON)
                .when()
                .get("/admin/allUsers")
                .then()
                .statusCode(Response.Status.UNAUTHORIZED.getStatusCode());
    }

    @Test
    @Order(4)
    void testChangeRole_UserFound() {
        // Define the login and new role
        String login = "test3";
        String newRole = "admin";
        given()
                .header("Authorization", "Bearer " + permanentAdminToken)
                .formParam("role", newRole)
                .when()
                .put("admin/{login}/updateRole", login)
                .then()
                .statusCode(Response.Status.OK.getStatusCode());
    }

    @Test
    @Order(5)
    void testChangeRole_UserFound_RoleNotAllowed() {
        // Define the login and new role
        String login = "test3";
        String newRole = "admin";
        given()
                .header("Authorization", "Bearer " + permanentUserToken)
                .formParam("role", newRole)
                .when()
                .put("admin/{login}/updateRole", login)
                .then()
                .statusCode(Response.Status.FORBIDDEN.getStatusCode());
    }

    @Test
    @Order(6)
    void testChangeRole_UserNotFound() {
        // Define the login and new role
        String login = "userNotFound";
        String newRole = "admin";
        given()
                .header("Authorization", "Bearer " + permanentAdminToken)
                .formParam("role", newRole)
                .when()
                .put("admin/{login}/updateRole", login)
                .then()
                .statusCode(Response.Status.NOT_FOUND.getStatusCode());
    }
    @Test
    @Order(7)
    void testChangeRole_InvalidRole() {
        // Define the login and new role
        String login = "test2";
        String newRole = "manager";
        given().contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + permanentAdminToken)
                .formParam("role", newRole)
                .when()
                .put("admin/{login}/updateRole", login)
                .then()
                .statusCode(Response.Status.BAD_REQUEST.getStatusCode());
    }

    @Test
    @Order(8)
    void testDeactivateAccount_Success() {
        // Define the login
        String login = "test3";
        given().contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + permanentAdminToken)
                .when()
                .put("admin/{login}/deactivate", login)
                .then()
                .statusCode(Response.Status.OK.getStatusCode())
                .body(equalTo("Account deactivated successfully"));
    }
    @Test
    @Order(9)
    void testDeactivateAccount_UserNotFound() {
        // Define the login
        String login = "userNotFound";
        given().contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + permanentAdminToken)
                .when()
                .put("admin/{login}/deactivate", login)
                .then()
                .statusCode(Response.Status.NOT_FOUND.getStatusCode());
    }
    @Test
    @Order(10)
    void testDeactivateAccount_RoleNotAllowed() {
        // Define the login
        String login = "userNotFound";
        given().contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + permanentUserToken)
                .when()
                .put("admin/{login}/deactivate", login)
                .then()
                .statusCode(Response.Status.FORBIDDEN.getStatusCode());
    }
    @Test
    @Order(11)
    void testReactivateAccount_RoleNotAllowed() {
        // Define the login
        String login = "testDeactive";
        given().contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + permanentUserToken)
                .when()
                .put("admin/{login}/reactivate", login)
                .then()
                .statusCode(Response.Status.FORBIDDEN.getStatusCode());
    }

    @Test
    @Order(13)
    void testReactivateAccount_Success() {
        // Define the login
        String login = "testDeactive";
        given().contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + permanentAdminToken)
                .when()
                .put("admin/{login}/reactivate", login)
                .then()
                .statusCode(Response.Status.OK.getStatusCode());
    }
    @Test
    @Order(12)
    void testReactivateAccount_UserNotFound() {
        // Define the login
        String login = "UserNotFound";
        given().contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + permanentAdminToken)
                .when()
                .put("admin/{login}/reactivate", login)
                .then()
                .statusCode(Response.Status.NOT_FOUND.getStatusCode());
    }

}