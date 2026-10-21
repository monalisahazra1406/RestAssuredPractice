package tests;

import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;

public class SSLConfigTest {

    private static final String BASE_URL =
            "https://self-signed.badssl.com";

    @Test
    public void accessSSLUsingRelaxedHttpsValidation(){

        given()
                .relaxedHTTPSValidation()
                .when()
                .get(BASE_URL)
                .then()
                .statusCode(200);
    }
}
