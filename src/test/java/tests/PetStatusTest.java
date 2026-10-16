package tests;

import base.BaseTest;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import specs.RequestSpecFactory;
import specs.ResponseSpecFactory;

import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;

public class PetStatusTest extends BaseTest {

    @DataProvider(name="petStatuses")
    public Object[][] petStatuses(){
        return new Object[][]{
                {"available"},
                {"pending"},
                {"sold"} };
    }

    @Test(dataProvider = "petStatuses")
    public void getPetsByStatus(String status){

        Response response =
                given()
                        .spec(RequestSpecFactory.getDefaultRequestSpec())
                        .queryParam("status",status)
                        .when()
                        .get("/pet/findByStatus");

        response.then()
                        .spec(ResponseSpecFactory.getSuccessResponseSpec());

        List<Map<String, Object>> pets = response.jsonPath().getList("$");

        Assert.assertNotNull(pets, "Response should be a JSON array");

        //Assert.assertFalse(pets.isEmpty(),"Available pet list should not be empty");

        for(Map<String,Object> pet: pets){

            Assert.assertEquals(pet.get("status"),
                    status, "Returned pet has incorrect status");

        }
    }

    @Test
    public void getPetsWithInvalidStatus(){

        Response response =
                given()
                        .spec(RequestSpecFactory.getDefaultRequestSpec())
                        .queryParam("status","invalid")
                        .when()
                        .get("/pet/findByStatus");

        response.then()
                        .spec(ResponseSpecFactory.getBadRequestResponseSpec());

        Assert.assertEquals(response.jsonPath().getInt("code"),
                400,"Unexpected error code");

        String message = response.jsonPath().getString("message");

        Assert.assertTrue(message.contains("not in the allowable values"),
                "Unexpected error message: "+message);
    }

    @Test
    public void getPetsWithoutStatusParameter(){

        Response response =
                given()
                        .spec(RequestSpecFactory.getDefaultRequestSpec()
                        )
                        .when()
                        .get("/pet/findByStatus");

        response.then()
                        .spec(ResponseSpecFactory.getBadRequestResponseSpec());

        Assert.assertEquals(response.jsonPath().getInt("code"), 400,
                "Unexpected error code");

        String message = response.jsonPath().getString("message");

        Assert.assertTrue(message.contains("missing required query parameter `status`"),
                "Unexpected error message: "+message);
    }
}
