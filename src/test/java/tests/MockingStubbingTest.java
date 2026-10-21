package tests;

import com.github.tomakehurst.wiremock.WireMockServer;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class MockingStubbingTest {

    private WireMockServer wireMockServer;

    @BeforeClass
    public void startWireMockServer(){

        wireMockServer = new WireMockServer(options().dynamicPort());
        wireMockServer.start();
    }

    @AfterClass(alwaysRun = true)
    public void stopWireMockServer(){

        if(wireMockServer != null){
            wireMockServer.stop();
        }

    }

    @Test
    public void getUsersFromStubbedApi(){

        // STEP 1 - Configure stub response
        wireMockServer.stubFor(
                get(urlEqualTo("/users/101"))
                        .willReturn(
                                aResponse()
                                .withStatus(200)
                                .withHeader( "Content-Type", "application/json")
                        .withBody( """
                            {
                              "id": 101,
                              "name": "Monalisa",
                              "role": "QA Lead"
                            }
                            """)));

        // STEP 2 - Call mock API using REST Assured
        given()
                .baseUri("http://localhost")
                .port(wireMockServer.port())
                .when()
                .get("/users/101")
                .then()
                .statusCode(200)
                .body("id",equalTo(101))
                .body("name",equalTo("Monalisa"))
                .body("role",equalTo("QA Lead"));

        // STEP 3 - Verify interaction

        wireMockServer.verify(1,getRequestedFor(urlEqualTo("/users/101")));

    }

    @Test
    public void serviceUnavailableFromStubbedApi(){

        wireMockServer.stubFor(
                get(
                        urlEqualTo("/users/500"))
                        .willReturn(
                                aResponse()
                                        .withStatus(503)
                                        .withHeader("Content-Type", "application/json")
                                        .withBody(  """
                        {
                          "error": "Service Unavailable",
                          "message": "Downstream service is temporarily unavailable"
                        }
                        """)));

        given()
                .baseUri("http://localhost")
                .port(wireMockServer.port())
                .when()
                .get("/users/500")
                .then()
                .statusCode(503)
                .body("error",equalTo("Service Unavailable"))
                .body("message", equalTo("Downstream service is temporarily unavailable"));

        wireMockServer.verify(1, getRequestedFor(urlEqualTo("/users/500")));
    }
}
