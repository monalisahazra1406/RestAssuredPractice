package tests;

import configs.ConfigManager;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;
import specs.RequestSpecFactory;
import specs.ResponseSpecFactory;

import static io.restassured.RestAssured.given;

public class GetPetByIdTest {

    @Test
    public  void getPetById() {

        long petId= 2026100500044L;

        Response response =
                given()
                        .spec(RequestSpecFactory.getDefaultRequestSpec())
                        .pathParam("petId",petId)
                        .when()
                        .get("/pet/{petId}");

        response.then()
                .spec(ResponseSpecFactory.getSuccessResponseSpec());

        Assert.assertEquals(response.jsonPath().getString("name"),
                "RestAssuredDog", "Pet name mismatch");

        Assert.assertEquals(response.jsonPath().getString("status"),
                "available", "Pet status mismatch");

    }
}
