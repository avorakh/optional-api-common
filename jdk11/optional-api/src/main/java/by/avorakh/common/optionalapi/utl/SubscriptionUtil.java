package by.avorakh.common.optionalapi.utl;

import by.avorakh.common.optionalapi.resource.SubscriptionData;
import by.avorakh.common.optionalapi.resource.SubscriptionType;
import by.avorakh.common.optionalapi.resource.UserAccount;
import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

@UtilityClass
public class SubscriptionUtil {

    public @NotNull Optional<SubscriptionType> extractSubscriptionType(@Nullable UserAccount account) {

        return Optional.ofNullable(account)
            .map(UserAccount::getSubscription)
            .map(SubscriptionData::getType);
    }

    public @NotNull Optional<SubscriptionType> extractSubscriptionType(@Nullable UserAccount account, @NotNull SubscriptionType defaultVaue) {
        
        // Recipe 10: Return another Optional when no value is present.
        return extractSubscriptionType(account)
            .or(() -> Optional.of(defaultVaue));
    }

    public boolean isEmptySubscriptionType(@Nullable UserAccount account) {

        // Recipe 11: Get an Optional’s status regardless of whether it is empty.
        return extractSubscriptionType(account).isEmpty();
    }
}
