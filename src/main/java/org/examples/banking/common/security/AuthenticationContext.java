package org.examples.banking.common.security;


import jakarta.annotation.Nonnull;

public interface AuthenticationContext {

    @Nonnull
    ApplicationUser getLoggedInUser();
}
