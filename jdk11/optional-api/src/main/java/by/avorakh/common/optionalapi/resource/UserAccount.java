package by.avorakh.common.optionalapi.resource;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserAccount {

    @NotNull String id;
    @NotNull String username;
    @NonFinal
    @Nullable PersonaData userProfile;
    @NonFinal
    @Nullable SubscriptionData subscription;
    @NotNull Long created;
    @NonFinal
    @Nullable Long modified;
}
