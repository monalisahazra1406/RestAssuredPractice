package tests;

import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class BasicAuthTest {

    private static final String BASE_URL = "https://httpbin.org";

    @Test
    public void validBasicAuthentication(){

            given()
                    .baseUri(BASE_URL)
                    .auth()
                    .preemptive()
                    .basic("user","passwd")
                    .when()
                    .get("/basic-auth/user/passwd")
                    .then()
                    .statusCode(200)
                    .body("authenticated",equalTo(true))
                    .body("user",equalTo("user"));
    }

    @Test
    public void invalidBasicAuthentication(){

        given()
                .baseUri(BASE_URL)
                .auth()
                .preemptive()
                .basic("user","wrongPassword")
                .when()
                .get("/basic-auth/user/passwd")
                .then()
                .statusCode(401);
    }

    @Test
    public void missingBasicAuthentication(){

        given()
                .baseUri(BASE_URL)
                .when()
                .get("/basic-auth/user/passwd")
                .then()
                .statusCode(401);

    }
}
