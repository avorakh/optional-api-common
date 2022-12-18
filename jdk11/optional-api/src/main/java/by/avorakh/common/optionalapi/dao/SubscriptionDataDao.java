package by.avorakh.common.optionalapi.dao;

import by.avorakh.common.optionalapi.resource.SubscriptionData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface SubscriptionDataDao {

    @Nullable SubscriptionData find(@NotNull String id);
}
