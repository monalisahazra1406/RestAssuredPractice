package tests;

import io.restassured.response.Response;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.testng.Assert.assertNotNull;

public class DynamicTokenChainingTest {

    private static final String BASE_URL = "https://httpbin.org";

    @Test
    public void generateAndReuseDynamicToken(){

        // STEP 1 - Get a dynamically generated value
        Response tokenResponse =
                given()
                        .baseUri(BASE_URL)
                        .when()
                        .get("/uuid")
                        .then()
                        .statusCode(200)
                        .extract().response();

        // STEP 2 - Extract it

        String token = tokenResponse.jsonPath().getString("uuid");

        assertNotNull(token);

        System.out.println("Generated token: " + token);

        // STEP 3 - Reuse it as Bearer token
        given()
                .baseUri(BASE_URL)
                .auth()
                .oauth2(token)
                .when()
                .get("/bearer")
                .then()
                .statusCode(200)
                .body("authenticated", equalTo(true))
                .body("token", equalTo(token));


    }
}
