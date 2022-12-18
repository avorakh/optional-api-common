package by.avorakh.common.optionalapi.svc;

import by.avorakh.common.optionalapi.resource.SubscriptionData;
import by.avorakh.common.optionalapi.resource.SubscriptionType;
import by.avorakh.common.optionalapi.resource.UserAccount;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface SubscriptionService  {

    @NotNull SubscriptionType findSubscriptionType(@Nullable UserAccount account);
    
    @NotNull SubscriptionData findSubscriptionData(@NotNull String id);
}
