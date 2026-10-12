package tests;

import models.Pet;
import org.testng.annotations.Test;
import specs.RequestSpecFactory;
import specs.ResponseSpecFactory;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class PetResponseMetaDataTest {

    @Test
    public void validatePetResponseMetaData(){

        given()
                .spec(RequestSpecFactory.getDefaultRequestSpec())
                .queryParam("status","available")
                .log().all()
                .when()
                .get("/pet/findByStatus")
                .then()
                .log().all()

                //Content type validation
                .header("Content-Type", containsString("application/json"))

                //check that date header exists
                .header("Date",notNullValue())

                //Lightweight response time check
                .time(lessThan(5000L));



    }
}
