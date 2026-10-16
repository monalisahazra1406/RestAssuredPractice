package tests;

import base.BaseTest;
import io.restassured.response.Response;
import models.Pet;
import org.testng.Assert;
import org.testng.annotations.Test;
import specs.RequestSpecFactory;
import specs.ResponseSpecFactory;

import java.util.List;

import static io.restassured.RestAssured.given;

public class CreatePetTest extends BaseTest {

    @Test
    public void createPet(){

        long petId = 2026100500044L;
         /*String requestBody = """
                {
                 "id": %d,
                                  "name": "RestAssuredDog",
                                  "photoUrls": [
                                    "https://example.com/restassured-dog.jpg"
                                  ],
                                  "status": "available"
                }
                """.formatted(petId);*/

        Pet pet = new Pet();
        pet.setId(petId);
        pet.setName("PojoRestAssuredDog");
        pet.setPhotoUrls(List.of("https://example.com/dog.jpg"));
        pet.setTags(List.of());
        pet.setStatus("available");

        Response response = given()
                .spec(RequestSpecFactory.getJsonRequestSpec())
                .body(pet)
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
