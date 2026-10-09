package specs;

import configs.ConfigManager;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

public class RequestSpecFactory {

    public static RequestSpecification getDefaultRequestSpec(){

        return new RequestSpecBuilder()
                .setBaseUri(ConfigManager.getBaseUrl())
                .setAccept(ContentType.JSON)
                .build();

    }

    public static RequestSpecification getJsonRequestSpec(){

        return new RequestSpecBuilder()
                .setBaseUri(ConfigManager.getBaseUrl())
                .setAccept(ContentType.JSON)
                .setContentType(ContentType.JSON)
                .build();
    }

}

