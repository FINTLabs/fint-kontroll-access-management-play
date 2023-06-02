package no.fintlabs.security;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * This test requires a running OPA server.
 */
@Disabled
public class OpaClientIntTest {
    @Test
    public void shouldBeAuthorized() {
        OpaClient opaClient = new OpaClient();
        boolean isAuthorized = opaClient.isAuthorized("ragnild.hansen@viken.no", "GET");
        assertThat(isAuthorized).isTrue();
    }

    @Test
    public void unknownUserShouldNotBeAuthorized() {
        OpaClient opaClient = new OpaClient();
        boolean isAuthorized = opaClient.isAuthorized("unknown@test.no", "GET");
        assertThat(isAuthorized).isFalse();
    }
}
