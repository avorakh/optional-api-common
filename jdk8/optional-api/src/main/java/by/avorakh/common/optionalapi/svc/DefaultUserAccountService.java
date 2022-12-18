package by.avorakh.common.optionalapi.svc;

import by.avorakh.common.optionalapi.resource.SubscriptionData;
import by.avorakh.common.optionalapi.resource.UserAccount;
import by.avorakh.common.optionalapi.dao.UserAccountDao;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.jetbrains.annotations.NotNull;

import java.util.EnumSet;
import java.util.Optional;

import static by.avorakh.common.optionalapi.resource.SubscriptionType.GOLD;
import static by.avorakh.common.optionalapi.resource.SubscriptionType.SILVER;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class DefaultUserAccountService implements UserAccountService {

    @NotNull UserAccountDao userAccountDao;
    @NotNull SubscriptionService subscriptionService;

    @Override
    public @NotNull Optional<UserAccount> getUserAccount(@NotNull String id) {
        
        /* Recipe 1: Never assign null to an optional variable.
        // Wrong code
        Optional<UserAccount> userAccount = null;
        // Good code
        Optional<UserAccount> userAccount = Optional.empty();
         */
        Optional<UserAccount> userAccount = userAccountDao.find(id);
      
        /*  Recipe 2: Don’t call get() directly.
        // Wrong code
        UserAccount foundUserAccount = userAccount.get();
        // Good code
        if (userAccount.isPresent()) {
            UserAccount foundUser = userAccount.get();
            ...
        } 
         */

        /* Recipe 3: Don’t use null directly to get a null reference when you have an Optional
        // Wrong code
        if (userAccount.isPresent()) {
            UserAccount foundUser = userAccount.get();
            SubscriptionType foundSubscriptionType = subscriptionService.findSubscriptionType(foundUser);
            ...
        } else {
            SubscriptionType foundSubscriptionType = subscriptionService.findSubscriptionType(null);
            ...
        }

        // Good code
        Optional.of(subscriptionService.findSubscriptionType(userAccount.orElse(null)))
            .filter(foundSubscriptionType -> EnumSet.of(GOLD, SILVER).contains(foundSubscriptionType))
            .ifPresent(foundSubscriptionType -> userAccount.ifPresent(foundUser -> {
                ...
            }));
         */
        
        /* Recipe 8: Don’t use isPresent()-get() if you want to perform an action only when an Optional value is present.
        // Inefficient code
        if (userAccount.isPresent()) {
            UserAccount foundUser = userAccount.get();
            SubscriptionType foundSubscriptionType = subscriptionService.findSubscriptionType(foundUser);
            switch (foundSubscriptionType) {
                case GOLD:
                case SILVER: {
                    SubscriptionData subscription = new SubscriptionData();
                    subscription.setType(foundSubscriptionType);
                    foundUser.setSubscription(subscription);
                }
            }
        } else {
            System.out.println("Account not found");
        }
         */
        // Good code
        userAccount.ifPresent(foundUser -> Optional.of(subscriptionService.findSubscriptionType(foundUser))
            .filter(foundSubscriptionType -> EnumSet.of(GOLD, SILVER).contains(foundSubscriptionType))
            .ifPresent(foundSubscriptionType -> {
                SubscriptionData subscription = new SubscriptionData();
                subscription.setType(foundSubscriptionType);
                foundUser.setSubscription(subscription);
            }));
        
        if (!userAccount.isPresent()) {
            System.out.println("Account not found");
        }
      
        return userAccount;
    }
}
