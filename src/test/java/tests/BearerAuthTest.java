package tests;

import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class BearerAuthTest {

    private static final String BASE_URL = "https://httpbin.org";
    private static final String TOKEN = "my-test-token";

    @Test
    public void validBearerAuthentication(){

        given()
                .baseUri(BASE_URL)
                .auth()
                .oauth2(TOKEN)
                .when()
                .get("/bearer")
                .then()
                .statusCode(200)
                .body("authenticated",equalTo(true))
                .body("token",equalTo(TOKEN));

    }

    @Test
    public void missingBearerAuthentication(){

        given()
                .baseUri(BASE_URL)
                .when()
                .get("/bearer")
                .then()
                .statusCode(401);
    }

}
