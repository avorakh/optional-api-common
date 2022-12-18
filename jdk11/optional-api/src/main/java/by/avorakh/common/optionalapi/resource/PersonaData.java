package by.avorakh.common.optionalapi.resource;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.jetbrains.annotations.Nullable;

import java.util.Date;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PersonaData {

    @Nullable String firstName;
    @Nullable String lastName;
    @Nullable String countryCode;
    @Nullable Date birthDate;
}
