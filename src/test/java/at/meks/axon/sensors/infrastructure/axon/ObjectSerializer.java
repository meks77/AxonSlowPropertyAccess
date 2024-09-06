package at.meks.axon.sensors.infrastructure.axon;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.module.blackbird.BlackbirdModule;
import com.thoughtworks.xstream.XStream;

public class ObjectSerializer {

    private final ObjectMapper mapper;

    public ObjectSerializer() {
        mapper = JsonMapper.builder()
//                                        .addModule(new AfterburnerModule())
                .addModule(new BlackbirdModule())
                                        .build();
                mapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.NONE);
        mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);

    }

    public String serializeToJson(Object object) {
        try {
            return mapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public String serializeToXml(Object object) {
        XStream xStream = new XStream();
        return xStream.toXML(object);
    }
}
