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

        Optional<SubscriptionType> extractedSubscriptionType = extractSubscriptionType(account);
        // Recipe 10: Return another Optional when no value is present. since Java 9.
        return extractedSubscriptionType.isPresent()
            ? extractedSubscriptionType
            : Optional.of(defaultVaue);
    }

    public boolean isEmptySubscriptionType(@Nullable UserAccount account){
        
        // Recipe 11: Get an Optional’s status regardless of whether it is empty. since Java 11.
        return !extractSubscriptionType(account).isPresent();
    }
}
