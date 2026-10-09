package tests;

import io.restassured.http.ContentType;
import models.Pet;
import org.testng.annotations.Test;
import specs.RequestSpecFactory;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class PetResponseValidationTest {

    @Test
    public void validatePetResponseUsingHamcrest() {

        long petId = System.currentTimeMillis();
        Pet pet = new Pet();
        pet.setId(petId);
        pet.setName("HamcrestDog");
        pet.setPhotoUrls(List.of("https://example.com/hamcrest-dog.jpg"));
        pet.setStatus("available");
        pet.setTags(List.of());

        //CREATE PET

        given()
                .spec(RequestSpecFactory.getJsonRequestSpec())
                .body(pet)
                .when()
                .post("/pet")
                .then()
                .statusCode(200);

        //VALIDATE GET RESPONSE USING HAMCREST

        given()
                .spec(RequestSpecFactory.getDefaultRequestSpec())
                .pathParam("petId",petId)
                .when()
                .get("/pet/{petId}")
                .then()
                .log().all()

        //STATUS VALIDATION

                .statusCode(200)

        //CONTENT TYPE VALIDATION

                .contentType(ContentType.JSON)

        //BODY VALIDATIONS

                .body("id",equalTo(petId))
                .body("name", equalTo("HamcrestDog"))
                .body("status", equalTo("available"))

        //COLLECTION VALIDATION

                .body("photoUrls", hasSize(1))
                .body("photoUrls", hasItem("https://example.com/hamcrest-dog.jpg"));
    }
}
