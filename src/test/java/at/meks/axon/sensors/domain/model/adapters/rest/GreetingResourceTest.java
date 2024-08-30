package at.meks.axon.sensors.domain.model.adapters.rest;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
class GreetingResourceTest {
    @Test
    void testHelloEndpoint() {
        given()
          .when().post("/startTestCase")
          .then()
             .statusCode(Response.Status.NO_CONTENT.getStatusCode());
    }

}