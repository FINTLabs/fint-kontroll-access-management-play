package no.fintlabs;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;

@AutoConfigureWebTestClient
public class TestControllerTest {

    private WebTestClient client =
            WebTestClient.bindToController(new TestController()).build();

    @MockBean
    private TestController testController;

    @Test
    public void test() throws Exception {
        client.get().uri("/api/v1/test")
                .exchange()
                .expectStatus()
                .isOk();
    }
}
