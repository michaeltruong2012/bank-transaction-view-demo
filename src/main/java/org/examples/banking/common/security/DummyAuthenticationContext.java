package org.examples.banking.common.security;

import jakarta.annotation.Nonnull;
import org.springframework.stereotype.Component;

import java.time.ZoneId;

@Component
public class DummyAuthenticationContext implements AuthenticationContext {

    @Override
    @Nonnull
    public ApplicationUser getLoggedInUser() {
        return new ApplicationUser("michael.truong", ZoneId.systemDefault().getId());
    }
}
