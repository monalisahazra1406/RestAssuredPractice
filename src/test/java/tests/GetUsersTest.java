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


        int userId = 2;

        given()
                .baseUri("https://reqres.in")
                .pathParam("id", userId)

                .when()
                .get("/api/users/{id}")

                .then()
                .statusCode(200);

    }

    @Test
    public void getNonExistingUser() {

        given()
                .baseUri("https://reqres.in")
                .pathParam("id", 999999)

                .when()
                .get("/api/users/{id}")

                .then()
                .statusCode(404);
    }
}
