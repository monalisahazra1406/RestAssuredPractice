package tests;

import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;

public class BookingSmokeTest {

    @Test
    public void getALLBookingIds(){

        Response response = given()
                .baseUri("https://restful-booker.herokuapp.com")
                .when()
                .get("/booking");

        //1. Validate status code
        Assert.assertEquals(response.getStatusCode(),200,
                "Unexpected status code");

        //2. Validate content-type
        Assert.assertTrue(response.contentType().startsWith("application/json"),
                "Unexpected content type: "+ response.contentType());

        //3. Extract root JSON array
        List<Map<String,Object>> bookings= response.jsonPath().getList("$");

        //4. Validate array is not empty
        Assert.assertFalse(bookings.isEmpty(),
                "Booking list should not be empty");

        //5. Validate each  object contains booking id
        for(Map<String,Object> booking: bookings){
            Assert.assertTrue(booking.containsKey("bookingid"),
                    "Booking id is missing");

            Assert.assertNotNull(booking.get("bookingid"),
                    "booking Id should not be null");

            Assert.assertTrue(booking.get("bookingid")instanceof Number,
                    "Booking id should be numeric");

        }


    }
}
