package at.meks.axon.sensors.domain.model.adapters.rest;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.*;

import static io.restassured.RestAssured.given;

@QuarkusTest
class TestcaseResourceTest {
    @Test
    void testHelloEndpoint() {
        given()
          .when().post("/startTestCase")
          .then()
             .statusCode(Response.Status.NO_CONTENT.getStatusCode());
    }

}