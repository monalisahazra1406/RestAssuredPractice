package tests;

import base.BaseTest;
import configs.ConfigManager;
import io.restassured.response.Response;
import models.Pet;
import org.testng.Assert;
import org.testng.annotations.Test;
import specs.RequestSpecFactory;
import specs.ResponseSpecFactory;
import testdata.PetTestDataBuilder;

import static io.restassured.RestAssured.given;

public class GetPetByIdTest extends BaseTest {

    @Test
    public  void getPetById() {

        long petId= System.currentTimeMillis();

        Pet pet = new PetTestDataBuilder()
                .withId(petId)
                .withName("GetByIdDog")
                .build();

        //Create pet

        given()
                .spec(RequestSpecFactory.getJsonRequestSpec())
                .body(pet)
                .when()
                .post("/pet")
                .then()
                .spec(ResponseSpecFactory.getSuccessResponseSpec());


        Response response =
                given()
                        .spec(RequestSpecFactory.getDefaultRequestSpec())
                        .pathParam("petId",petId)
                        .when()
                        .get("/pet/{petId}");

        response.then()
                .spec(ResponseSpecFactory.getSuccessResponseSpec());

        Assert.assertEquals(response.jsonPath().getString("name"),
                "GetByIdDog", "Pet name mismatch");

        Assert.assertEquals(response.jsonPath().getString("status"),
                "available", "Pet status mismatch");

    }
}
