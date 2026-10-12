package tests;

import io.restassured.response.Response;
import org.testng.annotations.Test;
import services.PetService;
import specs.ResponseSpecFactory;

import java.util.List;

import static org.testng.Assert.assertEquals;

public class PetServiceLayerTest {

    private final PetService petService = new PetService();

    @Test
    public void findAvailablePetsUsingServiceLayer(){

        Response response = petService.findPetByStatus("available");

        assertEquals(response.getStatusCode(), 200);

        List<String> statuses = response.jsonPath().getList("status");

        for(String status:statuses){
            assertEquals(status,"available");
        }

    }


}
