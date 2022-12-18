# Optional-api-common

## Description

The project covers the basic concept of Java programming language to use the Optional API.
This is based on the '12 recipes for using the Optional class as it’s meant to be used' article.

## Requirements

- JDK 8 + JDK 11
- Gradle 7.4.2

## Why am I getting null even when I use Optional?

### Recipe 1: Never assign null to an optional variable

**BAD:**
```java
public class DefaultUserAccountService implements UserAccountService {

    public @NotNull Optional<UserAccount> getUserAccount(@NotNull String id) {

        Optional<UserAccount> userAccount = null;
        return userAccount;
    }
}
```

**GOOD:**
```java
public class DefaultUserAccountService implements UserAccountService {

    public @NotNull Optional<UserAccount> getUserAccount(@NotNull String id) {

        Optional<UserAccount> userAccount = Optional.empty();
        return userAccount;
    }
}
```

### Recipe 2: Don’t call get() directly

**BAD:**
```java
public class DefaultUserAccountService implements UserAccountService {

    public @NotNull Optional<UserAccount> getUserAccount(@NotNull String id) {

        Optional<UserAccount> userAccount = userAccountDao.find(id);
        UserAccount foundUserAccount = userAccount.get();
        
        return userAccount;
    }
}
```
**GOOD:**
```java
public class DefaultUserAccountService implements UserAccountService {

    public @NotNull Optional<UserAccount> getUserAccount(@NotNull String id) {
        Optional<UserAccount> userAccount = userAccountDao.find(id);
        if (userAccount.isPresent()) {
            UserAccount foundUser = userAccount.get();
            // logic to set subscription details
        } else {
            // logic with logging if the user account was not found
        }
        
        return userAccount;
    }
}
```
### Recipe 3: Don’t use null directly to get a null reference when you have an Optional.
**BAD:** 
```java
public class DefaultUserAccountService implements UserAccountService {

    public @NotNull Optional<UserAccount> getUserAccount(@NotNull String id) {

        Optional<UserAccount> userAccount = userAccountDao.find(id);
        if (userAccount.isPresent()) {
            UserAccount foundUser = userAccount.get();
            var foundSubscriptionType = subscriptionService.findSubscriptionType(foundUser);
            // Some logic to set subscription
        } else {
            var foundSubscriptionType = subscriptionService.findSubscriptionType(null);
            // another logic
            System.out.println("Account not found");
        }
        
        return userAccount;
    }
}
```
**GOOD:** 
```java
public class DefaultUserAccountService implements UserAccountService {

    public @NotNull Optional<UserAccount> getUserAccount(@NotNull String id) {
        Optional<UserAccount> userAccount = userAccountDao.find(id);

        Optional.of(subscriptionService.findSubscriptionType(userAccount.orElse(null)))
            .filter(foundSubscriptionType -> EnumSet.of(GOLD, SILVER).contains(foundSubscriptionType))
            .ifPresent(foundSubscriptionType -> userAccount.ifPresent(foundUser -> {
                SubscriptionData subscription = new SubscriptionData();
                subscription.setType(foundSubscriptionType);
                foundUser.setSubscription(subscription);
            }));

        return userAccount;
    }
}
```


## What should I return or set when no value is present?
### Recipe 4: Avoid using an isPresent() and get() pair for setting and returning a value. 
**BAD:**
```java
public class SafeSubscriptionService implements SubscriptionService {
    ...
    @Override
    public  @NotNull SubscriptionType findSubscriptionType(@Nullable UserAccount account) {
        
        Optional<SubscriptionType> extractedSubscriptionType = SubscriptionUtil.extractSubscriptionType(account);
        if (extractedSubscriptionType.isPresent()){
            return extractedSubscriptionType.get();
        } else {
            return defaultType;
        }
    }
    ...
}
```
**GOOD:** 
```java
// JDK 8
public class SafeSubscriptionService implements SubscriptionService {
    ...
    @Override
    public  @NotNull SubscriptionType findSubscriptionType(@Nullable UserAccount account) {
        
        return SubscriptionUtil.extractSubscriptionType(account)
            .orElse(defaultType);
    }
    ...
}
```

### Recipe 5: Don’t use orElse() for returning a computed value.
**BAD:** 
```java
public class SubscriptionDataDaoDecorator implements SubscriptionDataDao {
    ...
    @Override
    public @Nullable SubscriptionData find(@NotNull String id) {

        return getFromCache(id)
            .orElse(getFromDB(id)
                .orElseThrow(() -> new IllegalStateException("SubscriptionData not found with id" + id)));
    }
    ...
}
```
**GOOD:** 
```java
// JDK 8
public class SubscriptionDataDaoDecorator implements SubscriptionDataDao {
    ...
    @Override
    public @Nullable SubscriptionData find(@NotNull String id) {
        return getFromCache(id)
            .orElseGet(() -> getFromDB(id)
                .orElseThrow(() -> new NoSuchElementException("SubscriptionData not found with id" + id)));
    }
    ...
}
```

### Recipe 6: Throw an exception in the absence of a value. </br> Recipe 7: How can I throw explicit exceptions when no value is present?
**INEFFICIENT:**
```java
public class DefaultSubscriptionService implements SubscriptionService {
    ...
    @Override
    public @NotNull SubscriptionType findSubscriptionType(@Nullable UserAccount account) {

        Optional<SubscriptionType> extractedSubscriptionType = SubscriptionUtil.extractSubscriptionType(account);
        if (extractedSubscriptionType.isPresent()){
            return extractedSubscriptionType.get();
        } else {
            throw new NoSuchElementException();
        }
    }
    ...
}
```
**GOOD:** 
```java
// JDK 8
public class DefaultSubscriptionService implements SubscriptionService {
    ...
    @Override
    public @NotNull SubscriptionType findSubscriptionType(@Nullable UserAccount account) {
        return SubscriptionUtil.extractSubscriptionType(account)
            .orElseThrow(NoSuchElementException::new);
    }
    ...
}
// JDK 10
public class DefaultSubscriptionService implements SubscriptionService {
    ...
    @Override
    public @NotNull SubscriptionType findSubscriptionType(@Nullable UserAccount account) {
        return SubscriptionUtil.extractSubscriptionType(account)
            .orElseThrow();
    }
    ...
}

public class SubscriptionDataDaoDecorator implements SubscriptionDataDao {
    ...
    @Override
    public @Nullable SubscriptionData find(@NotNull String id) {

        return getFromCache(id)
            .orElseGet(() -> getFromDB(id)
                .orElseThrow(() -> new NotFoundException("SubscriptionData not found with id" + id)));
    }
    ...
}
```
## How do I consume Optional values effectively?
### Recipe 8:  Don’t use isPresent()-get() if you want to perform an action only when an Optional value is present.
**INEFFICIENT:** 
```java
public class DefaultUserAccountService implements UserAccountService {
    @Override
    public @NotNull Optional<UserAccount> getUserAccount(@NotNull String id) {
        
        Optional<UserAccount> userAccount = userAccountDao.find(id);
        
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

        return userAccount;
    }
}
```
**WELL:** 
```java
// JDK 8
public class DefaultUserAccountService implements UserAccountService {
    ...
    @Override
    public @NotNull Optional<UserAccount> getUserAccount(@NotNull String id) {
        
        Optional<UserAccount> userAccount = userAccountDao.find(id);
        
        userAccount.ifPresent(foundUser -> Optional.of(subscriptionService.findSubscriptionType(foundUser))
            .filter(foundSubscriptionType -> EnumSet.of(GOLD, SILVER).contains(foundSubscriptionType))
            .ifPresent(foundSubscriptionType -> {
                SubscriptionData subscription = new SubscriptionData();
                subscription.setType(foundSubscriptionType);
                foundUser.setSubscription(subscription);
            }));
        
        return userAccount;
    }
}
```

### Recipe 9: Don’t use isPresent()-get() to execute an empty-based action if a value is not present
**INEFFICIENT:** 
```java
public class DefaultUserAccountService implements UserAccountService {
    ...
    @Override
    public @NotNull Optional<UserAccount> getUserAccount(@NotNull String id) {
        
        var userAccount = userAccountDao.find(id);
        
        if (userAccount.isPresent()) {
            var foundUser = userAccount.get();
            var foundSubscriptionType = subscriptionService.findSubscriptionType(foundUser);
            switch (foundSubscriptionType) {
                case GOLD:
                case SILVER: {
                    var subscription = new SubscriptionData();
                    subscription.setType(foundSubscriptionType);
                    foundUser.setSubscription(subscription);
                }
            }
        } else {
            System.out.println("Account not found");
        }
        
        return userAccount;
    }
}
```
**WELL:** 
```java
// JDK 11
public class DefaultUserAccountService implements UserAccountService {
    ...
    @Override
    public @NotNull Optional<UserAccount> getUserAccount(@NotNull String id) {
        
        var userAccount = userAccountDao.find(id);
        
        userAccount.ifPresentOrElse(
            foundUser -> Optional.of(subscriptionService.findSubscriptionType(foundUser))
                .filter(foundSubscriptionType -> EnumSet.of(GOLD, SILVER).contains(foundSubscriptionType))
                .ifPresent(foundSubscriptionType -> {
                    var subscription = new SubscriptionData();
                    subscription.setType(foundSubscriptionType);
                    foundUser.setSubscription(subscription);
                }),
            () -> System.out.println("Account not found")
        );

        return userAccount;
    }
}
```

### Recipe 10: Return another Optional when no value is present

**WELL:**
```java
@UtilityClass
public class SubscriptionUtil {
    ...
    //JDK 8
    public @NotNull Optional<SubscriptionType> extractSubscriptionType(@Nullable UserAccount account, @NotNull SubscriptionType defaultVaue) {

        Optional<SubscriptionType> extractedSubscriptionType = extractSubscriptionType(account);
        
        return extractedSubscriptionType.isPresent()
            ? extractedSubscriptionType
            : Optional.of(defaultVaue);
    }
    //JDK 9
    public @NotNull Optional<SubscriptionType> extractSubscriptionType(@Nullable UserAccount account, @NotNull SubscriptionType defaultVaue) {
        return extractSubscriptionType(account)
            .or(() -> Optional.of(defaultVaue));
    }
}
```


### Recipe 11: Get an Optional’s status regardless of whether it is empty.
**INEFFICIENT:** 
```java
// JDK 8
@UtilityClass
public class SubscriptionUtil {
    ...
    public boolean isEmptySubscriptionType(@Nullable UserAccount account){
        return !extractSubscriptionType(account).isPresent();
    }
}
```
**WELL:**
```java
// JDK 11
@UtilityClass
public class SubscriptionUtil {
    ...
    public boolean isEmptySubscriptionType(@Nullable UserAccount account) {
        return extractSubscriptionType(account).isEmpty();
    }
}
```

### Recipe 12: Don’t overuse Optional
**INEFFICIENT:** 
```java
public class SafeSubscriptionService implements SubscriptionService {
    ...
    @Override
    public @NotNull SubscriptionData findSubscriptionData(@NotNull String id) {
        SubscriptionData subscriptionData = subscriptionDataDao.find(id);
        
        return Optional.ofNullable(subscriptionData).orElseGet(SubscriptionData::new);
    }
}

public class DefaultSubscriptionService implements SubscriptionService {
    ...
    @Override
    public @NotNull SubscriptionData findSubscriptionData(@NotNull String id) {

        SubscriptionData subscriptionData = subscriptionDataDao.find(id);

        return Optional.ofNullable(subscriptionData).orElseThrow(NoSuchElementException::new);
    }
}

```
**WELL:** 
```java
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SafeSubscriptionService implements SubscriptionService {
    ...
    @Override
    public @NotNull SubscriptionData findSubscriptionData(@NotNull String id) {
        var subscriptionData = subscriptionDataDao.find(id);
        
        return subscriptionData != null ? subscriptionData : new SubscriptionData();
    }
}

public class DefaultSubscriptionService implements SubscriptionService {
    ...
    @Override
    public @NotNull SubscriptionData findSubscriptionData(@NotNull String id) {

        SubscriptionData subscriptionData = subscriptionDataDao.find(id);
        
        if (subscriptionData == null){
            throw new NoSuchElementException();
        }
        return subscriptionData;
    }
}
```