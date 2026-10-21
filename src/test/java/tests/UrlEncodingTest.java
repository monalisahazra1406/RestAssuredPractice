package tests;

import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class UrlEncodingTest {

    private static final String BASE_URL =
            "https://httpbin.org";

    @Test
    public void restAssuredAutomaticallyEncodesQueryParameters(){

        given()
                .baseUri(BASE_URL)
                .queryParam("search","hello world")
                .when()
                .get("/get")
                .then()
                .statusCode(200)
                .body("args.search",equalTo("hello world"));
    }
}
