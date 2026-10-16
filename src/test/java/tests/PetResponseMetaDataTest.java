package tests;

import base.BaseTest;
import models.Pet;
import org.testng.annotations.Test;
import specs.RequestSpecFactory;
import specs.ResponseSpecFactory;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class PetResponseMetaDataTest extends BaseTest {

    @Test
    public void validatePetResponseMetaData(){

        given()
                .spec(RequestSpecFactory.getDefaultRequestSpec())
                .queryParam("status","available")
                .when()
                .get("/pet/findByStatus")
                .then()

                //Content type validation
                .header("Content-Type", containsString("application/json"))

                //check that date header exists
                .header("Date",notNullValue())

                //Lightweight response time check
                .time(lessThan(5000L));



    }
}
