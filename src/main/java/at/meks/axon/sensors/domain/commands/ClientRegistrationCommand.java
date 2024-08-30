package at.meks.axon.sensors.domain.commands;

import at.meks.axon.sensors.domain.model.ClientId;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Value;
import lombok.experimental.Accessors;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

@Value
@Accessors(fluent = true)
@NoArgsConstructor(force = true, access = AccessLevel.PACKAGE)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class ClientRegistrationCommand {

    @NonNull
    @TargetAggregateIdentifier
    ClientId clientId;

}
