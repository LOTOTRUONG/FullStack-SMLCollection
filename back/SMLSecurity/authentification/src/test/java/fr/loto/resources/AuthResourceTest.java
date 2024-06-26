package fr.loto.resources;
import fr.loto.dto.LoginDto;
import fr.loto.dto.NewUserDto;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.parsing.Parser;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.*;
import static org.hamcrest.Matchers.*;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@QuarkusTest
@QuarkusTestResource(WireMockEmailService.class)

class AuthResourceTest {

    @BeforeEach
    public void setUp(){
        RestAssured.defaultParser = Parser.JSON;
    }

    @Test
    @Order(1)
    void testRegister_Sucess() {
        String login = "test60";
        String password = "test60password";
        String email = "test60@gmail.com";
        NewUserDto newTestUser = new NewUserDto();
        newTestUser.setEmail(email);
        newTestUser.setLogin(login);
        newTestUser.setPassword(password);
        given().contentType(ContentType.JSON)
                .body(newTestUser)
                .when()
                .post("auth/register")
                .then()
                .statusCode(Response.Status.CREATED.getStatusCode());
    }

    @Test
    @Order(2)
    void testConfirmRegistration_WrongCode() {
        String testCode = "amJuNXFTYzhwUVlGYVZOWEk1RzU1WnFOQ3pvaGtuRzVJanUyU3RPbHJZd1VFcWlTZ0NsQVlmZk8wUVEtX0ZBSktrRkZtSVI4Q3pGZ05Fa191Z1pkTng3S2NBWk1kbEtoUWZVN0JJek9MOXdvVmhNdTgyWlhUUUdxY19Za2FkNWZPLW1SN3JTTkNZZHZ5RkNQb1c4TUc4Vko0M1FLaENJaVpmb2RLZi02OHRKZFluU2FjSlNxbVVXSFJndng5RU5vS0pFV05aNkRZcl9SWjhYT2tmWFlpdXJabXdwYUxQZjZwY2d3X3BLWlNnUXJiQWExSURCM25sTVNteHMzMi1GRmtHVW1FYTZEVWF0WnRwX2tOYkdCYURPdDlCMmREandndWs3M3o3ZlhmbVU9"; //code genereated from test 1
        given().contentType(ContentType.JSON)
                .queryParam("code",testCode)
                .when()
                .get("auth/register/confirm")
                .then()
                .statusCode(Response.Status.BAD_REQUEST.getStatusCode());
    }

    @Test
    @Order(3)
    void testRegister_DuplicatedLogin()  {
        String login = "test5";
        String password ="test5password";
        String email = "test5@gmail.com";
        NewUserDto newTestUser = new NewUserDto();
        newTestUser.setEmail(email);
        newTestUser.setLogin(login);
        newTestUser.setPassword(password);
        given().contentType(ContentType.JSON)
                .body(newTestUser)
                .when()
                .post("auth/register")
                .then()
                .statusCode(Response.Status.BAD_REQUEST.getStatusCode())
                .body(equalTo("Login already taken"));
    }
    @Test
    @Order(4)
    void testRegister_InvalidPassword() {
        String login = "test7";
        String password = "1234";
        String email = "test7@gmail.com";
        NewUserDto newTestUser = new NewUserDto();
        newTestUser.setEmail(email);
        newTestUser.setLogin(login);
        newTestUser.setPassword(password);
        given().contentType(ContentType.JSON)
                .body(newTestUser)
                .when()
                .post("auth/register")
                .then()
                .statusCode(Response.Status.BAD_REQUEST.getStatusCode())
                .body(equalTo("Invalid password"));
    }
    @Test
    @Order(5)
    void testRegister_InvalidEmail() {
        String login = "test8";
        String password = "12345678";
        String email = "test8@gmail";
        NewUserDto newTestUser = new NewUserDto();
        newTestUser.setEmail(email);
        newTestUser.setLogin(login);
        newTestUser.setPassword(password);
        given().contentType(ContentType.JSON)
                .body(newTestUser)
                .when()
                .post("auth/register")
                .then()
                .statusCode(Response.Status.BAD_REQUEST.getStatusCode())
                .body(equalTo("Invalid email"));
    }

    @Test
    @Order(6)
    void testLogin_Success() {
        String login = "test2";
        String password = "12345678";
        LoginDto newTestLogin = new LoginDto();
        newTestLogin.setLogin(login);
        newTestLogin.setPassword(password);
        given().contentType(ContentType.JSON)
                .body(newTestLogin)
                .when()
                .post("auth/login")
                .then()
                .statusCode(Response.Status.OK.getStatusCode());
    }

    @Test
    @Order(7)
    void testLogin_WrongLogin() {
        String login = "test1";
        String password = "12345678";
        LoginDto newTestLogin = new LoginDto();
        newTestLogin.setLogin(login);
        newTestLogin.setPassword(password);
        given().contentType(ContentType.JSON)
                .body(newTestLogin)
                .when()
                .post("auth/login")
                .then()
                .statusCode(Response.Status.NOT_FOUND.getStatusCode());
    }
    @Test
    @Order(8)
    void testLogin_WrongPassword() {
        String login = "test2";
        String password = "87654321";
        LoginDto newTestLogin = new LoginDto();
        newTestLogin.setLogin(login);
        newTestLogin.setPassword(password);
        given().contentType(ContentType.JSON)
                .body(newTestLogin)
                .when()
                .post("auth/login")
                .then()
                .statusCode(Response.Status.UNAUTHORIZED.getStatusCode());
    }

    @Test
    @Order(9)
    void testLogin_maximumAttempts() {
        String login = "test3";
        String password = "87654321";
        LoginDto newTestLogin = new LoginDto();
        newTestLogin.setLogin(login);
        newTestLogin.setPassword(password);
        given().contentType(ContentType.JSON)
                .body(newTestLogin)
                .when()
                .post("auth/login")
                .then()
                .statusCode(Response.Status.FORBIDDEN.getStatusCode());
    }
    @Test
    @Order(10)
    void testLogin_DeactivatedAccount() {
        String login = "test4";
        String password = "12345678";
        LoginDto newTestLogin = new LoginDto();
        newTestLogin.setLogin(login);
        newTestLogin.setPassword(password);
        given().contentType(ContentType.JSON)
                .body(newTestLogin)
                .when()
                .post("auth/login")
                .then()
                .statusCode(Response.Status.FORBIDDEN.getStatusCode())
                .body(equalTo("Your account has been deactivated"));
    }

    @Test
    @Order(11)
    void testLogin_requiredParameter() {
        String login = "";
        String password = "";
        LoginDto newTestLogin = new LoginDto();
        newTestLogin.setLogin(login);
        newTestLogin.setPassword(password);
        given().contentType(ContentType.JSON)
                .body(newTestLogin)
                .when()
                .post("auth/login")
                .then()
                .statusCode(Response.Status.BAD_REQUEST.getStatusCode())
                .body(equalTo("Please enter login and password"));
    }

    @Test
    @Order(12)
    void testValidateCodepin() {
        String login = "test5";
        String codePin = "6789";
        given().contentType(ContentType.JSON)
                .queryParam("Login", login)
                .queryParam("CodePin", codePin)
                .when()
                .post("auth/{login}/codepin", login)
                .then()
                .statusCode(Response.Status.OK.getStatusCode());

    }

    @Test
    @Order(13)
    void testValidateCodepin_wrongCodePin() {
        String login = "test5";
        String codePin = "9876";
        given().contentType(ContentType.JSON)
                .queryParam("Login", login)
                .queryParam("CodePin", codePin)
                .when()
                .post("auth/{login}/codepin", login)
                .then()
                .statusCode(Response.Status.UNAUTHORIZED.getStatusCode());
    }
}