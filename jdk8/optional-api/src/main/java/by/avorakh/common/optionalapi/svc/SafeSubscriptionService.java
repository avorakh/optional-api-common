package by.avorakh.common.optionalapi.svc;

import by.avorakh.common.optionalapi.dao.SubscriptionDataDao;
import by.avorakh.common.optionalapi.resource.SubscriptionData;
import by.avorakh.common.optionalapi.resource.SubscriptionType;
import by.avorakh.common.optionalapi.resource.UserAccount;
import by.avorakh.common.optionalapi.utl.SubscriptionUtil;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SafeSubscriptionService implements SubscriptionService {

    @NotNull SubscriptionDataDao subscriptionDataDao;
    @NotNull SubscriptionType defaultType;

    public SafeSubscriptionService(@NotNull SubscriptionDataDao subscriptionDataDao) {

        this.subscriptionDataDao = subscriptionDataDao;
        this.defaultType = SubscriptionType.FREE;
    }

    @Override
    public  @NotNull SubscriptionType findSubscriptionType(@Nullable UserAccount account) {

        /* Vanilla code
        SubscriptionType type = defaultType;

        if (account != null) {
            SubscriptionData subscription = account.getSubscription();
            if (subscription != null) {
                type = subscription.getType();
            }
        }
        
        return type != null ? type : defaultType;
         */


        /* Recipe 4: Avoid using an isPresent() and get() pair for setting and returning a value
        // Inefficient code
        Optional<SubscriptionType> extractedSubscriptionType = SubscriptionUtil.extractSubscriptionType(account);
        if (extractedSubscriptionType.isPresent()){
            return extractedSubscriptionType.get();
        } else {
            return defaultType;
        }
         */
        
        return SubscriptionUtil.extractSubscriptionType(account)
            .orElse(defaultType);
    }

    @Override
    public @NotNull SubscriptionData findSubscriptionData(@NotNull String id) {
        SubscriptionData subscriptionData = subscriptionDataDao.find(id);
        /* Recipe 12: Don’t overuse Optional.
        // Inefficient code
        return Optional.ofNullable(subscriptionData).orElseGet(SubscriptionData::new);
         */

        return subscriptionData != null ? subscriptionData : new SubscriptionData();
    }
}
