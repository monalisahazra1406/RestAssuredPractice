package tests;

import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;
import specs.RequestSpecFactory;
import specs.ResponseSpecFactory;

import static io.restassured.RestAssured.given;

public class CreatePetTest {

    @Test
    public void createPet(){

        long petId = 2026100500044L;
        String requestBody = """
                {
                 "id": %d,
                                  "name": "RestAssuredDog",
                                  "photoUrls": [
                                    "https://example.com/restassured-dog.jpg"
                                  ],
                                  "status": "available"
                }
                """.formatted(petId);
        Response response = given()
                .spec(RequestSpecFactory.getJsonRequestSpec())
                .body(requestBody)
                .when()
                .post("/pet");

        response.then()
                .spec(ResponseSpecFactory.getSuccessResponseSpec());

        Assert.assertEquals(response.jsonPath().getLong("id"),
                petId,"Returned PetId is incorrect");

        Assert.assertEquals(response.jsonPath().getString("status"),
                "available","Returned Pet Status is incorrect");

    }
}
