package tests;

import io.restassured.config.RedirectConfig;
import io.restassured.config.RestAssuredConfig;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;

public class RedirectConfigTest {

    private static final String BASE_URL = "https://httpbin.org";

    @Test
    public void followRedirectByDefault() {

        given()
                .baseUri(BASE_URL)
                .queryParam("url","/get")
                .when()
                .get("/redirect-to")
                .then()
                .statusCode(200)
                .body("url",containsString("/get"));
    }

    @Test
    public void disableRedirectFollowing(){

        RestAssuredConfig noRedirectConfig =
                RestAssuredConfig
                        .config()
                        .redirect(RedirectConfig
                                .redirectConfig()
                                .followRedirects(false));

        given()
                .baseUri(BASE_URL)
                .config(noRedirectConfig)
                .queryParam("url","/get")
                .when()
                .get("/redirect-to")
                .then()
                .statusCode(302)
                .header("Location", containsString("/get"));
     }
}
