package home1;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class HomeReqresTests {

    @BeforeAll
    static void setUp() {
        RestAssured.baseURI = "https://reqres.in/";
    }

    @Test
    void checkSingleUserId() {
        Integer response =
                get("/api/users/2")
                        .then()
                        .statusCode(200)
                        .extract().path("data.id");

        assertEquals(2, response);
    }

    @Test
    void checkSingleUserEmail() {
        String response =
                get("/api/users/2")
                        .then()
                        .statusCode(200)
                        .extract().path("data.email");

        assertEquals("janet.weaver@reqres.in", response);
    }

    @Test
    void checkSuccessfulRegister() {
        String data = "{ \"email\": \"eve.holt@reqres.in\", \"password\": \"pistol\" }";
        given()
                .contentType(JSON)
                .body(data)
                .when()
                .post("/api/register")
                .then()
                .statusCode(200)
                .body("id", is(4),"token", is("QpwL5tke4Pnpja7X4"));
    }

    @Test
    void checkNegativeRegister() {
        String data = "{ \"email\": \"sydney@fife\" }";
        given()
                .contentType(JSON)
                .body(data)
                .when()
                .post("/api/register")
                .then()
                .statusCode(400)
                .body("error", is("Missing password"));
    }

    @Test
    void createUserSuccess() {
        String data = "{ \"name\": \"morpheus\", \"job\": \"leader\" }";
        given()
                .contentType(JSON)
                .body(data)
                .when()
                .post("/api/users")
                .then()
                .statusCode(201)
                .body("name", is("morpheus"),
                        "job",is("leader"),
                        "id", notNullValue(),
                        "createdAt", notNullValue());
    }

    @Test
    void updateUserSuccess() {
        String data = "{ \"name\": \"morpheus\", \"job\": \"zion resident\" }";
        given()
                .contentType(JSON)
                .body(data)
                .when()
                .put("/api/users/2")
                .then()
                .statusCode(200)
                .body("name", is("morpheus"),
                        "job",is("zion resident"),
                        "updatedAt", notNullValue());
    }
}
