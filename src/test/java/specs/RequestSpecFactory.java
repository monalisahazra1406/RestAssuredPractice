package specs;

import configs.ConfigManager;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.config.EncoderConfig;
import io.restassured.config.RestAssuredConfig;
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
                .addRequestSpecification(getDefaultRequestSpec())
                .setContentType(ContentType.JSON)
                .build();
    }

    public static RequestSpecification getBinaryRequestSpec(){

        RestAssuredConfig config =
                RestAssuredConfig.config()
                        .encoderConfig(EncoderConfig.encoderConfig()
                                .appendDefaultContentCharsetToContentTypeIfUndefined
                                        (false));

        return new RequestSpecBuilder().addRequestSpecification(getDefaultRequestSpec())
                .setConfig(config)
                .setAccept("application/octet-stream")
                .setAccept(ContentType.JSON)
                .build();

    }

}

