package fr.loto.resources;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.parsing.Parser;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.*;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;


@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@QuarkusTest
@QuarkusTestResource(WireMockEmailService.class)
class UserResourceTest {
    private static final String permanentAdminToken = "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJpc3MiOiJqd3QtdG9rZW4iLCJzdWIiOiJhZG1pbiIsImdyb3VwcyI6WyJzdXBlcmFkbWluIl0sImlhdCI6MTcxMjA0OTk2MiwiZXhwIjozMzI0ODA0OTk2MiwianRpIjoiZjVlYWNmYzUtMmRkZC00OGFlLWEwOWYtNjM3ZjliOTQ4NGVhIn0.KKD6sWVA-e7t39bQYbNFKPP8QnCCGZ8UPwkFjkjO-IbHor7oTHSBKEAoj31OFnS27VagR_-dHSgCegThsJgsTMmylGW7dbzHsH4r7v47UdVwDGXk2khLrKfgAyO0wxB5SB2pkzo-_bXvHqcmzWxY08NVRghGxayDxcH3LpBzAszMylRssb7YoWThdgYdDlNY2voGFBfycA_wQDXmHIfXgUvimn2-zL5A5m5vPwyi2hiEgC-pc3Iz72u6A6J586XknD5w1A_KMtxirhrZK9UZ2wukw7kUuXEdf0K2kfJtPTC6hAPX_8cgkHG2Ovff89rSoZhhUsnyZbw1u0Buks2Bfg";

    @BeforeEach
    public void setUp(){
        RestAssured.defaultParser = Parser.JSON;
    }


    @Test
    @Order(1)
    void testGetUserByLogin_UserFound() {
        String login = "admin";
        given().contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + permanentAdminToken)
                .when()
                .get("/users/{login}", login)
                .then()
                .statusCode(Response.Status.OK.getStatusCode())
                .body("login", equalTo("admin"))
                .body("email", equalTo("test1@gmail.com"))
                .body("role", equalTo("superadmin"));

    }
    @Test
    @Order(2)
    void testGetUserByLogin_UserNotFound() {
        String login = "testNotFound";
        given().contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + permanentAdminToken)
                .when()
                .get("/users/{login}", login)
                .then()
                .statusCode(Response.Status.NOT_FOUND.getStatusCode())
                .body(equalTo("User not found"));
    }

    @Test
    @Order(3)
    void testGetUserByLogin_RolesNotAllowed() {
        String login = "admin";
        given().contentType(ContentType.JSON)
                .when()
                .get("/users/{login}", login)
                .then()
                .statusCode(Response.Status.UNAUTHORIZED.getStatusCode());
    }
    @Test
    @Order(4)
    void testRequestPasswordReset_Success() {
        String login = "test2";
        String newPassword = "UPDATEDPASSWORD";
        given().header("Authorization", "Bearer " + permanentAdminToken)
                .formParam("NewPassword", newPassword)
                .when()
                .post("/users/{login}/request_password", login)
                .then()
                .statusCode(Response.Status.CREATED.getStatusCode());
    }
    @Test
    @Order(5)
    void testRequestPasswordReset_UserNotFound() {
        String login = "test7";
        String newPassword = "UPDATEDPASSWORD";
        given().header("Authorization", "Bearer " + permanentAdminToken)
                .formParam("NewPassword", newPassword)
                .when()
                .post("/users/{login}/request_password", login)
                .then()
                .statusCode(Response.Status.NOT_FOUND.getStatusCode());
    }

    @Test
    @Order(6)
    void testConfirmResetRequest_InvalidPassword() {
        String login = "test2";
        String newPassword = "1234";
        given().header("Authorization", "Bearer " + permanentAdminToken)
                .formParam("NewPassword", newPassword)
                .when()
                .post("/users/{login}/request_password", login)
                .then()
                .statusCode(Response.Status.BAD_REQUEST.getStatusCode());
    }

    @Test
    @Order(7)
    void testRequestLoginRecovery_EmailFound() {
        String email = "test2@gmail.com";
        given().header("Authorization", "Bearer " + permanentAdminToken)
                .when()
                .post("/users/{email}/request_login/", email)
                .then()
                .statusCode(Response.Status.CREATED.getStatusCode());
    }
    @Test
    @Order(8)
    void testRequestLoginRecovery_EmailNotFound() {
        String email = "testEmailNotFound@gmail.com";
        given().header("Authorization", "Bearer " + permanentAdminToken)
                .when()
                .post("/users/{email}/request_login/", email)
                .then()
                .statusCode(Response.Status.NOT_FOUND.getStatusCode());
    }

    @Test
    @Order(9)
    void testRequestEmailReset_Success() {
        String login = "test2";
        String newEmail = "newEmail@gmail.com";
        given().header("Authorization", "Bearer " + permanentAdminToken)
                .formParam("NewEmail", newEmail)
                .when()
                .post("/users/{login}/request_email", login)
                .then()
                .statusCode(Response.Status.CREATED.getStatusCode());
    }
    @Test
    @Order(10)
    void testRequestEmailReset_LoginNotFound() {
        String login = "loginNotFound";
        String newEmail = "newEmail@gmail.com";
        given().header("Authorization", "Bearer " + permanentAdminToken)
                .formParam("NewEmail", newEmail)
                .when()
                .post("/users/{login}/request_email", login)
                .then()
                .statusCode(Response.Status.NOT_FOUND.getStatusCode());
    }

    @Test
    @Order(11)
    void testRequestEmailReset_InvalidEmail() {
        String login = "test2";
        String newEmail = "newEmail@gmail";
        given().header("Authorization", "Bearer " + permanentAdminToken)
                .formParam("NewEmail", newEmail)
                .when()
                .post("/users/{login}/request_email", login)
                .then()
                .statusCode(Response.Status.BAD_REQUEST.getStatusCode());
    }
}