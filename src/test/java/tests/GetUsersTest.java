package tests;

import io.restassured.response.Response;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class GetUsersTest {

    @Test
    public void getUser(){
        given()
                .when()
                .get("https://reqres.in/api/users/2")
                .then()
                .statusCode(200)
                .body("data.id",equalTo(2));
    }

    @Test
    public void getUsers(){

        Response response =
                given()
                        .queryParam("page",2)
                        .when()
                        .get("https://reqres.in/api/users");

        response.then()
                .statusCode(200);

        System.out.println("Response: "+ response.asPrettyString());

        int totalUsers = response.jsonPath().getInt("total");
        System.out.println("Total Users "+ totalUsers);
    }

    @Test
    public void createUser(){

        String requestBody = """
                {
                "name": "Shawn",
                "job": "teacher"
                }
                """;

        Response response =
                given()
                        .contentType("application/json")
                        .body(requestBody)
                        .when()
                        .post("https://reqres.in/api/users");

        response.then()
                .statusCode(201);

        int userId = response.jsonPath().getInt("id");

        System.out.println(response.asPrettyString());

        System.out.println("created user id: "+ userId);
    }

    @Test
    public void getUserUsingPathParameter() {

        String requestBody = """
                {
                "name": "Eve",
                "job": "trainer"
                }
                """;

        Response createResponse =
                given().
                        contentType("application/json")
                        .body(requestBody)
                        .when()
                        .post("https://reqres.in/api/users");

        System.out.println(createResponse.asPrettyString());

        int userId = createResponse.jsonPath().getInt("id");

        System.out.println(userId);

        given()
                .pathParam("id", userId)
                .when()
                .get("https://reqres.in/api/users/{id}")
                .then()
                .statusCode(200);
    }
}
