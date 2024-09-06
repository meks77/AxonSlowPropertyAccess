package at.meks.axon.sensors.infrastructure.axon;

import at.meks.axon.sensors.domain.model.Client;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.module.afterburner.AfterburnerModule;
import io.quarkus.runtime.ShutdownEvent;
import io.quarkus.runtime.Startup;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.enterprise.inject.Produces;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.config.Configuration;
import org.axonframework.config.Configurer;
import org.axonframework.config.DefaultConfigurer;
import org.axonframework.serialization.json.JacksonSerializer;

@Startup
public class AxonProvider {

    private Configuration configuration;

    public AxonProvider() {
        init();
    }

    protected void onStop(@Observes ShutdownEvent shutdownEvent) {
        configuration.shutdown();
    }

    void init() {
        ObjectMapper mapper = JsonMapper.builder()
                                        .addModule(new AfterburnerModule())
                                        .build();
//        mapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.NONE);
//        mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        var serializer = JacksonSerializer.builder().objectMapper(mapper).build();
        Configurer configurer = DefaultConfigurer.defaultConfiguration()
                                                 .configureAggregate(Client.class)
                .configureSerializer(conf -> serializer)
                .configureEventSerializer(conf -> serializer)
                .configureMessageSerializer(conf -> serializer);
        configuration = configurer.start();
    }

    @Produces
    @ApplicationScoped
    CommandGateway commandGateway() {
        return configuration.commandGateway();
    }


}
