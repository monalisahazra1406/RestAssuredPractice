package base;

import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import org.testng.annotations.BeforeSuite;

public class BaseTest {

    @BeforeSuite(alwaysRun = true)
    public void configureRestAssured(){

        //Default behaviour
        //print request + response only if validation fails
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();

        //optional verbose logging
        boolean logAll = Boolean.parseBoolean(System.getProperty("loggAll","false"));

        if(logAll){
            RestAssured.filters(new AllureRestAssured(),
                    new RequestLoggingFilter(),
                    new ResponseLoggingFilter());
        } else {
            RestAssured.filters(new AllureRestAssured());
        }
    }

}
