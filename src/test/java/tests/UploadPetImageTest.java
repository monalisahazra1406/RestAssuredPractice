package tests;

import base.BaseTest;
import io.restassured.common.mapper.TypeRef;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import models.Pet;
import org.testng.Assert;
import org.testng.annotations.Test;
import specs.RequestSpecFactory;
import specs.ResponseSpecFactory;
import testdata.PetTestDataBuilder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static io.restassured.RestAssured.given;

public class UploadPetImageTest extends BaseTest {

    @Test
    public void uploadPetImage() throws IOException {

        long petId = System.currentTimeMillis();
        String originalPhotoUrl = "https://example.com/original-dog.jpg";

        // =========================
        // STEP 1 - CREATE PET
        // =========================

        Pet requestPet = new PetTestDataBuilder()
                .withId(petId)
                .withName("PetBuilder")
                .withStatus("available")
                .build();

        given()
                .spec(RequestSpecFactory.getJsonRequestSpec())
                .body(requestPet)
                .when()
                .post("/pet")
                .then()
                .spec(ResponseSpecFactory.getSuccessResponseSpec());


        // =========================
        // STEP 2 - READ IMAGE FILE
        // =========================

        Path imagePath = Path.of("src","test","resources","testData","pet-test.png");
        byte[] imageBytes = Files.readAllBytes(imagePath);

        // =========================
        // STEP 3 - UPLOAD IMAGE
        // =========================

        Response uploadResponse =
               given()
                       .spec(RequestSpecFactory.getBinaryRequestSpec())
                       .body(imageBytes)
                       .pathParam("petId", petId)
                       .when()
                       .post("/pet/{petId}/uploadImage")
                       .then()
                       .spec(ResponseSpecFactory.getSuccessResponseSpec())
                       .extract().response();

        // =========================
        // STEP 4 - DESERIALIZE RESPONSE
        // =========================

        Pet uploadedPet = uploadResponse.as(Pet.class);

        // =========================
        // STEP 5 - VALIDATE RESPONSE
        // =========================

        Assert.assertEquals(uploadedPet.getId(),petId,
                "Pet ID is incorrect");
        Assert.assertEquals(uploadedPet.getName(),"PetBuilder",
                "Name is incorrect");
        Assert.assertTrue(uploadedPet.getPhotoUrls().contains(originalPhotoUrl),"Photo URL is missing");
        Assert.assertTrue(uploadedPet.getPhotoUrls().size()>1,
                "Uploaded image path was not added");




    }
}
