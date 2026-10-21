package tests;

import io.restassured.response.Response;
import org.testng.annotations.Test;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class XmlPathTest {

    private static final String BASE_URL =
            "https://httpbin.org";

    @Test
    public void validateXMLResponseUsingXpath(){

        Response response =
                given()
                        .baseUri(BASE_URL)
                        .when()
                        .get("/xml")
                        .then()
                        .statusCode(200)
                        .extract()
                        .response();

        //Root element attribute
        String slideShowTitle = response.xmlPath().getString("slideshow@title");

        // First slide title
        String firstSlideTitle = response.xmlPath().getString("slideshow.slide[0].title");

        // Extract all slide titles
        List<String> slideTitles = response.xmlPath().getList("slideshow.slide.title");

        assertEquals(slideShowTitle,"Sample Slide Show");
        assertEquals(firstSlideTitle,"Wake up to WonderWidgets!");
        assertTrue(slideTitles.contains("Overview"));
    }
}
