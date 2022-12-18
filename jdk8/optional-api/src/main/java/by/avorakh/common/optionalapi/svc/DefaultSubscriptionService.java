package by.avorakh.common.optionalapi.svc;

import by.avorakh.common.optionalapi.resource.SubscriptionData;
import by.avorakh.common.optionalapi.resource.SubscriptionType;
import by.avorakh.common.optionalapi.resource.UserAccount;
import by.avorakh.common.optionalapi.dao.SubscriptionDataDao;
import by.avorakh.common.optionalapi.utl.SubscriptionUtil;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.NoSuchElementException;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class DefaultSubscriptionService implements SubscriptionService {
    
    @NotNull SubscriptionDataDao subscriptionDataDao;

    @Override
    public @NotNull SubscriptionType findSubscriptionType(@Nullable UserAccount account) {
        
        /* Vanilla code
        if (account != null) {
            SubscriptionData subscription = account.getSubscription();
            if (subscription != null) {
                SubscriptionType type = subscription.getType();
                if (type != null) {
                    return subscription.getType();
                }
            }
        }

        throw new IllegalStateException("user account without subscription type");
         */
        
        /* Recipe 6: Throw an exception in the absence of a value.
           Recipe 7: How can I throw explicit exceptions when no value is present? 
        //Inefficient code
        Optional<SubscriptionType> extractedSubscriptionType = SubscriptionUtil.extractSubscriptionType(account);
        if (extractedSubscriptionType.isPresent()){
            return extractedSubscriptionType.get();
        } else {
            throw new NoSuchElementException();
        }
         */
        // Well code
        return SubscriptionUtil.extractSubscriptionType(account)
            .orElseThrow(NoSuchElementException::new);
    }

    @Override
    public @NotNull SubscriptionData findSubscriptionData(@NotNull String id) {


        /* Recipe 12: Don’t overuse Optional.
        // Inefficient code
        return Optional.ofNullable(subscriptionDataDao.find(id))
            .orElseThrow(NoSuchElementException::new);
        */

        SubscriptionData subscriptionData = subscriptionDataDao.find(id);
        if (subscriptionData == null){
            throw  new NoSuchElementException();
        }
        return subscriptionData;
    }
}
