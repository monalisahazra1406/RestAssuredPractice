package services;

import io.restassured.response.Response;
import specs.RequestSpecFactory;

import static io.restassured.RestAssured.given;

public class PetService {

    public Response findPetByStatus(String status){

        return given()
                .spec(RequestSpecFactory.getDefaultRequestSpec())
                .queryParam("status",status)
                .when()
                .get("/pet/findByStatus");

    }

    public Response findPetById(long petId){

        return given()
                .spec(RequestSpecFactory.getDefaultRequestSpec())
                .pathParam("petId",petId)
                .when()
                .get("/pet/{petId}");
    }
}
