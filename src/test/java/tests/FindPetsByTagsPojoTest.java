package tests;

import base.BaseTest;
import io.restassured.common.mapper.TypeRef;
import io.restassured.response.Response;
import models.Category;
import models.Pet;
import models.Tag;
import org.testng.Assert;
import org.testng.annotations.Test;
import specs.RequestSpecFactory;
import specs.ResponseSpecFactory;
import java.util.List;

import static io.restassured.RestAssured.given;

public class FindPetsByTagsPojoTest extends BaseTest {

    @Test
    public void createPetAndFindByTag() {

        // ---------------------------------
        // STEP 1 - CREATE UNIQUE TEST DATA
        // ---------------------------------

        long petId = System.currentTimeMillis();
        String uniqueTag = "restAssured"+petId;

        Category category = new Category(1,"Dogs");

        Tag tag = new Tag(105,uniqueTag);

        Pet requestPet = new Pet();
        requestPet.setId(petId);
        requestPet.setCategory(category);
        requestPet.setTags(List.of(tag));
        requestPet.setName("PojoTagDog");
        requestPet.setPhotoUrls(List.of("https://example.com/pojo-tag-dog.jpg",
                "https://example.com/pojo-tag-dog1.jpg"));
        requestPet.setStatus("available");

        // ---------------------------------
        // STEP 2 - CREATE PET
        // SERIALIZATION
        // ---------------------------------

        Response createResponse =
                given()
                        .spec(RequestSpecFactory.getJsonRequestSpec())
                        .body(requestPet)
                        .when()
                        .post("/pet")
                        .then()
                        .spec(ResponseSpecFactory.getSuccessResponseSpec())
                        .extract().response();

        // ---------------------------------
        // STEP 3 - DESERIALIZE POST RESPONSE
        // ---------------------------------

        Pet createdPet = createResponse.as(Pet.class);

        Assert.assertEquals(createdPet.getId(), petId);
        Assert.assertEquals(createdPet.getName(),
                "PojoTagDog","Name is incorrect");
        Assert.assertEquals(createdPet.getStatus(),
                "available","Status is not correct");

        // ---------------------------------
        // STEP 4 - FIND PET BY TAG
        // ---------------------------------

        Response findByTagResponse =
                given()
                        .spec(RequestSpecFactory.getDefaultRequestSpec())
                        .queryParam("tags",uniqueTag)
                        .when()
                        .get("/pet/findByTags")
                        .then()
                        .spec(ResponseSpecFactory.getSuccessResponseSpec())
                        .extract().response();

        // ---------------------------------
        // STEP 5 - DESERIALIZE ARRAY RESPONSE
        // ---------------------------------

        List<Pet> pets = findByTagResponse.as(new TypeRef<List<Pet>>() {});

        // ---------------------------------
        // STEP 6 - VERIFY CREATED PET EXISTS
        // ---------------------------------

        boolean petFound = pets.stream().anyMatch(pet -> pet.getId() == petId);

        Assert.assertTrue(petFound,"Created pet not found");

    }
}
