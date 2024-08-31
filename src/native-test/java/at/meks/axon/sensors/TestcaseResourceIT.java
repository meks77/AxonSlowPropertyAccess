package at.meks.axon.sensors;

import at.meks.axon.sensors.adapters.rest.TestcaseResource;
import io.quarkus.test.junit.QuarkusIntegrationTest;

@QuarkusIntegrationTest
class TestcaseResourceIT extends TestcaseResource {
    // Execute the same tests but in packaged mode.
}
