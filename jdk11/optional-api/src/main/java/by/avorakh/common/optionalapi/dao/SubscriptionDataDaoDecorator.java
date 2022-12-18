package by.avorakh.common.optionalapi.dao;

import by.avorakh.common.optionalapi.resource.SubscriptionData;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.NoSuchElementException;
import java.util.Optional;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SubscriptionDataDaoDecorator implements SubscriptionDataDao {

    @NotNull SubscriptionDataDao jdbcSubscriptionDataDao;
    @NotNull SubscriptionDataDao cachedSubscriptionDataDao;

    @Override
    public @Nullable SubscriptionData find(@NotNull String id) {
        /* Recipe 5: Don’t use orElse() for returning a computed value
        // Worst code
        return getFromCache(id)
            .orElse(getFromDB(id)
                .orElseThrow(() -> new NoSuchElementException("SubscriptionData not found with id" + id)));
         */

        // Good code
        return getFromCache(id)
            .orElseGet(() -> getFromDB(id)
                .orElseThrow(() -> new NoSuchElementException("SubscriptionData not found with id" + id)));
    }

    @NotNull Optional<SubscriptionData> getFromCache(@NotNull String id) {

        return Optional.ofNullable(cachedSubscriptionDataDao.find(id));
    }

    @NotNull Optional<SubscriptionData> getFromDB(@NotNull String id) {

        return Optional.ofNullable(jdbcSubscriptionDataDao.find(id));
    }
}
