package tests;

import io.restassured.config.RestAssuredConfig;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;

public class TimeOutConfigTest {

    private static final String BASE_URL =
            "https://httpbin.org";

    @Test
    public void configureRequestTimeouts(){

        RestAssuredConfig timeoutConfig =
                RestAssuredConfig
                        .config()
                        .httpClient(
                                RestAssuredConfig.config().getHttpClientConfig()
                                        .setParam("http.connection.timeout",
                                                5000)
                                        .setParam("http.socket.timeout",
                                                5000));

        given()
                .baseUri(BASE_URL)
                .config(timeoutConfig)
                .when()
                .get("/get")
                .then()
                .statusCode(200);

    }
}
