package tests;

import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;
import specs.RequestSpecFactory;
import specs.ResponseSpecFactory;

import static io.restassured.RestAssured.given;

public class PetApiChainingTest {

    @Test
    public void createPetAndGetPetId() {

        long petId = System.currentTimeMillis();

        String requestBody = """
                {
                "id": %d,
                                    "name": "ChainedRestAssuredDog",
                                    "photoUrls": [
                                        "https://example.com/dog.jpg"
                                    ],
                                    "status": "available"
                }
                """.formatted(petId);

        // =========================
        // STEP 1 - CREATE PET
        // =========================

        Response createResponse =
                given()
                        .spec(RequestSpecFactory.getJsonRequestSpec())
                        .body(requestBody)
                        .when()
                        .post("/pet")
                        .then()
                        .log().all()
                        .spec(ResponseSpecFactory.getSuccessResponseSpec())
                        .extract()
                        .response();

        // Extract ID returned by POST API

        long createdPetId = createResponse.jsonPath().getLong("id");

        System.out.println("Created pet id: "+ createdPetId);

        Assert.assertEquals(createdPetId, petId,"Created pet ID is incorrect");

        Assert.assertEquals(createResponse.jsonPath().getString("name"),
                "ChainedRestAssuredDog", "Name is incorrect");

        Assert.assertEquals(createResponse.jsonPath().getString("status"),
                "available", "Status is incorrect");

        // =========================
        // STEP 2 - UPDATE CREATED PET
        // =========================

        String updateRequestBody = """
                {
                 "id": %d,
                                    "name": "UpdatedChainedDog",
                                    "photoUrls": [
                                        "https://example.com/dog.jpg"
                                    ],
                                    "status": "sold"
                }
                """.formatted(createdPetId);

        Response updateResponse =
                given()
                        .spec(RequestSpecFactory.getJsonRequestSpec())
                        .body(updateRequestBody)
                        .when()
                        .put("/pet")
                        .then()
                        .log().all()
                        .spec(ResponseSpecFactory.getSuccessResponseSpec())
                        .extract().response();

        Assert.assertEquals(updateResponse.jsonPath().getLong("id"),
                createdPetId, "Pet ID is incorrect");

        Assert.assertEquals(updateResponse.jsonPath().getString("name"),
                "UpdatedChainedDog", "Name is incorrect");

        Assert.assertEquals(updateResponse.jsonPath().getString("status"),
                "sold", "Status is incorrect");

        // =========================
        // STEP 3 - GET UPDATED PET
        // =========================

        Response getResponse =
                given()
                        .spec(RequestSpecFactory.getDefaultRequestSpec())
                        .when()
                        .get("/pet/{petId}", createdPetId)
                        .then()
                        .log().all()
                        .spec(ResponseSpecFactory.getSuccessResponseSpec())
                        .extract()
                        .response();

        Assert.assertEquals(getResponse.jsonPath().getLong("id"),
                createdPetId, "Pet ID is incorrect");

        Assert.assertEquals(
                getResponse.jsonPath().getString("name"),
                "UpdatedChainedDog", "Name is incorrect");

        Assert.assertEquals(
                getResponse.jsonPath().getString("status"),
                "sold", "Status is incorrect");

        // =========================
        // STEP 4 - DELETE PET
        // =========================

        Response deleteResponse =
                given()
                        .spec(RequestSpecFactory.getDefaultRequestSpec())
                        .log().all()
                        .when()
                        .delete("/pet/{petId}",createdPetId)
                        .then()
                        .log().all()
                        .statusCode(200)
                        .extract().response();

        // =========================
// STEP 5 - VERIFY PET IS DELETED
// =========================

        Response getDeletedPetResponse =
                given()
                        .spec(RequestSpecFactory.getDefaultRequestSpec())
                        .log().all()
                        .when()
                        .get("/pet/{petId}",createdPetId)
                        .then()
                        .log().all()
                        .statusCode(404)
                        .extract().response();






    }
}
