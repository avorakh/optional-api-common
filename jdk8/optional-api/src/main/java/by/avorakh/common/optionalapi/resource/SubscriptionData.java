package by.avorakh.common.optionalapi.resource;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.jetbrains.annotations.Nullable;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SubscriptionData {

    @Nullable String id;
    @Nullable SubscriptionType type;
    @Nullable String code;
    @Nullable Long created;
    @Nullable Long modified;
}
