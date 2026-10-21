package tests;

import io.restassured.response.Response;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.testng.Assert.assertTrue;

public class ApiResponseTimeTest {

    @Test
    public void validateApiResponseTime(){

        Response response =
                given()
                        .baseUri("https://httpbin.org")
                        .when()
                        .get("/get");

        long responseTime = response.getTime();

        System.out.println("Response Time : "+responseTime+" ms");

        assertTrue(responseTime<5000,"API response exceeded expected threshold");
    }
}
