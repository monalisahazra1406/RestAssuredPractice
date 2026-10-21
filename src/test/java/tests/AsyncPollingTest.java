package tests;

import com.github.tomakehurst.wiremock.WireMockServer;
import io.restassured.response.Response;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.time.Duration;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;
import static com.github.tomakehurst.wiremock.stubbing.Scenario.STARTED;
import static io.restassured.RestAssured.given;
import static org.awaitility.Awaitility.await;
import static org.testng.Assert.assertEquals;

public class AsyncPollingTest {

    private WireMockServer wireMockServer;

    @BeforeClass
    public void startWireMockServer() {

        wireMockServer = new WireMockServer(options().dynamicPort());
        wireMockServer.start();
    }

    @AfterClass(alwaysRun = true)
    public void stopWireMockServer() {

        if(wireMockServer != null){
            wireMockServer.stop();
        }
    }

    @Test
    public void waitUntilJobIsCompleted(){

        // First call → PROCESSING

        wireMockServer.stubFor(
                get(urlEqualTo("/jobs/123"))
                        .inScenario("Job Processing")
                        .whenScenarioStateIs(STARTED)
                        .willReturn(
                                aResponse()
                                        .withStatus(200)
                                        .withHeader("Content-Type", "application/json")
                                        .withBody( """
                            {
                              "jobId": 123,
                              "status": "PROCESSING"
                            }
                            """))
                        .willSetStateTo("Completed"));

        // Following call → COMPLETED

        wireMockServer.stubFor(
                get(urlEqualTo("/jobs/123"))
                        .inScenario("Job Processing")
                        .whenScenarioStateIs("Completed")
                        .willReturn(
                                aResponse()
                                        .withStatus(200)
                                        .withHeader("Content-Type", "application/json")
                                        .withBody( """
                            {
                              "jobId": 123,
                              "status": "COMPLETED"
                            }
                            """) )  );

        // Poll until final status appears

        await()
                .atMost(Duration.ofSeconds(5))
                .pollInterval(Duration.ofMillis(500))
                .untilAsserted(()->{
                    Response response = given()
                            .baseUri("http://localhost")
                            .port(wireMockServer.port())
                            .when()
                            .get("/jobs/123");

                    assertEquals(response.statusCode(),200);
                    assertEquals(response.jsonPath().getString("status"),"COMPLETED");

                });
    }
}
