package at.meks.axon.sensors.domain.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Value;
import lombok.experimental.Accessors;
import org.axonframework.modelling.command.EntityId;

@Value
@Getter(onMethod = @__({@JsonProperty}))
@Accessors(fluent = true)
@NoArgsConstructor(force = true, access = AccessLevel.PUBLIC)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class Measurement {

    @EntityId
    MeasurementId measurementId;
    Unit unit;
    MeasuredValue value;

}
