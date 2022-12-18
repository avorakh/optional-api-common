package by.avorakh.common.optionalapi.svc;

import by.avorakh.common.optionalapi.resource.PersonaData;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public interface PersonaDataService {

    @NotNull Optional<PersonaData> getPersonaData(@NotNull String id);

}
