package combooking.step_definitions;

import combooking.support.BookingUtility;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.http.Cookie;
import io.restassured.http.Cookies;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;
import org.json.JSONObject;
import static org.junit.Assert.assertEquals;

import io.restassured.response.Response;
import org.junit.Assert;

import java.util.List;
import java.util.Map;
import java.util.Random;

public class Booking_CRUD {
    Response response;
    JSONObject requestBody;
    BookingUtility bookingUtility;

    @Given("user accesses endpoint {string}")
    public void user_accesses_endpoint(String endpoint) {
        bookingUtility = new BookingUtility();
        bookingUtility.setEndPoint(endpoint);
    }

    @When("user creates a auth token with login authentication as {string} and {string}")
    public void user_creates_a_auth_token_with_login_authentication_as_and(String userName, String password) {
        JSONObject loginAuth = new JSONObject();
        loginAuth.put("username", userName);
        loginAuth.put("password", password);
        bookingUtility.response =
                bookingUtility.
                        requestSetup().
                        body(loginAuth.toString())
                        .when().
                        post(bookingUtility.getEndPoint());

        Cookies allDetailedCookies = bookingUtility.response.detailedCookies();

        Cookie token = allDetailedCookies.get("token");
        bookingUtility.setToken(token);
    }

    @Then("user should get the response code {int}")
    public void user_should_get_the_response_code(Integer statusCode) {
           assertEquals(Long.valueOf(statusCode), Long.valueOf(bookingUtility.response.getStatusCode()));
    }

    @When("the user books a room with the mentioned booking details")
    public void the_user_books_a_room_with_the_mentioned_booking_details(final DataTable dataTable) {

        List<Map<String, String>> data = dataTable.asMaps(String.class, String.class);
        for (Map<String, String> row : data) {
            int roomid = Integer.parseInt(generateRandomRoomId());
            bookingUtility.setRoomId(roomid);
            requestBody = createBookingRequestBody(row, roomid);
            response = bookingUtility.requestSetup()
                        .body(requestBody.toString())
                        .when()
                        .post(bookingUtility.getEndPoint());

            validateBookingResponse(row.get("firstname"), row.get("lastname"), row.get("checkin"), row.get("checkout"));

            if (response.getStatusCode() == 201) {
                int bookingId = response.jsonPath().getInt("booking.bookingid");
                bookingUtility.setBookingId(bookingId);
            }
        }
    }

    @Then("the response status code should be {int}")
    public void the_response_status_code_should_be(int expectedStatusCode) {
        assertEquals(expectedStatusCode, response.getStatusCode());
    }

    @When("the user finds booking details with booking ID")
    public void theUserRetrievesBookingWithBookingID() {
        Integer fetchedBookingId = bookingUtility.getBookingId();
        if (fetchedBookingId != null) {
            response = bookingUtility
                        .requestSetup()
                        .cookie(bookingUtility.getToken())
                        .when()
                        .get(bookingUtility.getEndPoint()+ fetchedBookingId);
        } else {
            throw new RuntimeException("Booking ID not available for GET request.");
        }
    }

    @When("the user updates the booking with booking details")
    public void theUserUpdatesTheBookingWithFollowingDetails(final DataTable dataTable) {
        for (Map<String, String> data : dataTable.asMaps(String.class, String.class)) {
            requestBody = createBookingRequestBody(data, Integer.parseInt(generateRandomRoomId()));
            response = bookingUtility
                        .requestSetup()
                        .cookie(bookingUtility.getToken())
                        .body(requestBody.toString())
                        .when()
                        .put(bookingUtility.getEndPoint() + bookingUtility.getBookingId());

            String updatedLastname = response.jsonPath().getString("booking.lastname");
            if (response.statusCode() == 200) {
                assertEquals("Expected lastname to be " + data.get("lastname") + " but got " + updatedLastname,
                        data.get("lastname"), updatedLastname);
            }
        }
    }

    @When("the user deletes the booking with booking ID")
    public void theUserDeletesTheBookingWithBookingID() {
        Integer fetchedBookingId = bookingUtility.getBookingId();
        if (fetchedBookingId != null) {
            response = bookingUtility
                        .requestSetup()
                        .cookie(bookingUtility.getToken())
                        .when()
                        .delete(bookingUtility.getEndPoint() + fetchedBookingId);
        } else {
            throw new RuntimeException("Booking ID not available for Delete request.");
        }
    }

    @Then("validate the response with JSON schema {string}")
    public void validate_the_response_with_response_json_schema(String schemaFileName) {
        response.then().assertThat().body(JsonSchemaValidator.matchesJsonSchemaInClasspath("schemas/" + schemaFileName));
    }

    @When("the user searches booking details with invalid booking ID")
    public void the_user_searches_booking_details_with_invalid_booking_ID() {
        Integer dummyBookingId = Integer.parseInt(RandomStringUtils.randomNumeric(5));
        if (dummyBookingId != null) {
            response = bookingUtility.requestSetup().cookie(bookingUtility.getToken())
                    .when().get(bookingUtility.getEndPoint()+ dummyBookingId);
        } else {
            throw new RuntimeException("Booking ID not available for GET request.");
        }
    }

    @When("the user tries to books a room with invalid booking details")
    public void the_user_tries_to_books_a_room_with_invalid_booking_details(final DataTable dataTable) {
        List<Map<String, String>> data = dataTable.asMaps(String.class, String.class);
        for (Map<String, String> row : data) {
            int roomid = Integer.parseInt(generateRandomRoomId());
            bookingUtility.setRoomId(roomid);
            requestBody = createBookingRequestBody(row, roomid);
            response = bookingUtility.requestSetup().body(requestBody.toString()).when().post(bookingUtility.getEndPoint());
        }
    }

    @And("the user should see response with incorrect {string}")
    public void theUserShouldSeeTheResponseWithIncorrectField(final String errorMessage) {
        final String actualErrorMessage = response.jsonPath().getString("fieldErrors");
        assertEquals("Error message mismatch", errorMessage, actualErrorMessage);
    }

    private static String generateRandomRoomId() {

        final Random random = new Random();
        return String.valueOf(2000 + random.nextInt(900));

    }

    private JSONObject createBookingRequestBody(Map<String, String> row, int roomid) {

        return new JSONObject()
                .put("bookingid", 0)
                .put("roomid", roomid)
                .put("firstname", row.get("firstname"))
                .put("lastname", row.get("lastname"))
                .put("depositpaid", true)
                .put("email", row.get("email"))
                .put("phone", row.get("phone"))
                .put("bookingdates", new JSONObject()
                        .put("checkin", row.get("checkin"))
                        .put("checkout", row.get("checkout")));

    }

    private void validateBookingResponse(String firstname, String lastname, String checkin, String checkout) {

        Integer bookingId = response.jsonPath().getInt("bookingid");
        String responseFirstname = response.jsonPath().getString("booking.firstname");
        String responseLastname = response.jsonPath().getString("booking.lastname");
        String responseCheckin = response.jsonPath().getString("booking.bookingdates.checkin");
        String responseCheckout = response.jsonPath().getString("booking.bookingdates.checkout");

        Assert.assertNotNull("BookingID should not to be null",bookingId);
        assertEquals("First Name did not match", firstname, responseFirstname);
        assertEquals("Last Name did not match", lastname, responseLastname);
        assertEquals("Check in date did not match", checkin, responseCheckin);
        assertEquals("Checkout date did not match", checkout, responseCheckout);

    }
}