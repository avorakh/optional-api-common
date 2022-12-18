package by.avorakh.common.optionalapi.dao;

import by.avorakh.common.optionalapi.resource.UserAccount;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public interface UserAccountDao {

    @NotNull Optional<UserAccount> find(@NotNull String id);
}
