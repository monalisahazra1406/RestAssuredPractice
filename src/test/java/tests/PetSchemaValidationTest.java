package tests;

import models.Category;
import models.Pet;
import models.Tag;
import org.testng.annotations.Test;
import specs.RequestSpecFactory;
import specs.ResponseSpecFactory;

import java.util.List;

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

public class PetSchemaValidationTest {

    @Test
    public void validatePetResponseSchema(){

        long petId = System.currentTimeMillis();

        Category category = new Category(1,"Dogs");
        Tag tag = new Tag(105,"schema-Test");

        Pet pet = new Pet();
        pet.setId(petId);
        pet.setCategory(category);
        pet.setName("SchemaDog");

        pet.setPhotoUrls(List.of("https://example.com/schema-dog.jpg"));
        pet.setTags(List.of(tag));
        pet.setStatus("available");

        // =========================
        // CREATE PET
        // =========================

        given()
                .spec(RequestSpecFactory.getJsonRequestSpec())
                .body(pet)
                .when()
                .post("/pet")
                .then()
                .spec(ResponseSpecFactory.getSuccessResponseSpec());

        // =========================
        // GET PET + SCHEMA VALIDATION
        // =========================

        given()
                .spec(RequestSpecFactory.getDefaultRequestSpec())
                .pathParam("petId",petId)
                .when()
                .get("/pet/{petId}")
                .then()
                .log().all()
                .spec(ResponseSpecFactory.getSuccessResponseSpec())
                .body(matchesJsonSchemaInClasspath("schemas/pet-schema.json"));

    }
}
