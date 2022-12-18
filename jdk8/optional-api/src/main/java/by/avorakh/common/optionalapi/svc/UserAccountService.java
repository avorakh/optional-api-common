package by.avorakh.common.optionalapi.svc;

import by.avorakh.common.optionalapi.resource.UserAccount;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public interface UserAccountService {

    @NotNull Optional<UserAccount> getUserAccount(@NotNull String id);
}
