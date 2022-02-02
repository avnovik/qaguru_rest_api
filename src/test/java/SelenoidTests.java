import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.hamcrest.Matchers.hasKey;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class SelenoidTests {

    //{"total":20,"used":0,"queued":0,"pending":0,
    // "browsers":{"android":{"8.1":{}},"chrome":{"90.0":{},"91.0":{}},"firefox":{"88.0":{},"89.0":{}},"opera":{"76.0":{},"77.0":{}}}}

    @Test
    void checkTotal20() {
        given()
                .when()
                .get("https://selenoid.autotests.cloud/status")
                .then()
                .statusCode(200)
                .body("total", is(20));
    }

    @Test
    void checkTotal20WithoutGiven() {
        get("https://selenoid.autotests.cloud/status")
                .then()
                .statusCode(200)
                .body("total", is(20));
    }

    @Test
    void checkChrome() {
        given()
                .when()
                .get("https://selenoid.autotests.cloud/status")
                .then()
                .statusCode(200)
                .body("browsers.chrome", hasKey("91.0"));
    }

    @Test
    void checkTotal20BadPractice() {
        String response =
                get("https://selenoid.autotests.cloud/status")
                        .then()
                        .extract().response().asString();

        System.out.println(response);

        // DONT DO THAT, BAD PRACTICE
        assertEquals("{\"total\":20,\"used\":0,\"queued\":0,\"pending\":0,\"browsers\":" +
                        "{\"android\":{\"8.1\":{}},\"chrome\":{\"90.0\":{},\"91.0\":{}}," +
                        "\"firefox\":{\"88.0\":{},\"89.0\":{}},\"opera\":{\"76.0\":{},\"77.0\":{}}}}\n",
                response);
    }

    @Test
    void checkTotal20GoodPractice() {
        Integer response =
                get("https://selenoid.autotests.cloud/status")
                        .then()
                        .extract().path("total");

        System.out.println(response);

        assertEquals(20, response);
    }

    @Test
    void checkTotal20WithTalkAboutResponse() {
        Response response =
                get("https://selenoid.autotests.cloud/status")
                        .then()
                        .extract().response();

        System.out.println(response);
        System.out.println(response.toString());
        System.out.println(response.asString());
        System.out.println(response.path("total").toString());
        System.out.println(response.path("browsers.chrome").toString());
    }

    @Test
    void checkTotal20WithAssertJ() {
        Integer response =
                get("https://selenoid.autotests.cloud/status")
                        .then()
                        .extract().path("total");

        System.out.println(response);

        assertThat(response).isEqualTo(20);
    }

    @Test
    void checkWdHubStatus401() {
        get("https://selenoid.autotests.cloud/wd/hub/status")
                .then()
                .statusCode(401);
    }
}
