package combooking.support;

import io.restassured.RestAssured;

import io.restassured.http.Cookie;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lombok.Getter;
import lombok.Setter;

public class BookingUtility {

    public Response response;

    private static String CONTENT_TYPE;

    public BookingUtility() {
        PropertiesFile property = new PropertiesFile();
        CONTENT_TYPE = property.getProperty("content.type");
    }

    @Getter
    @Setter
    private static Cookie token;

    @Getter
    @Setter
    private static String endPoint;

    @Getter
    @Setter
    private static int bookingId;

    @Getter
    @Setter
    private static int roomId;

    public RequestSpecification requestSetup() {

        RestAssured.baseURI = PropertiesFile.getProperty("baseURL");
        return RestAssured.given()
                .contentType(CONTENT_TYPE)
                .accept(CONTENT_TYPE);
    }

}
