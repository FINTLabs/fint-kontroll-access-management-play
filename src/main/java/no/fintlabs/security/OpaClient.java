package no.fintlabs.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Slf4j
@Component
public class OpaClient {
    //TODO: Move to configuration
    private String opaUrl = "http://localhost:8181/v1/data/accessmanagement/allow";
    private RestTemplate restTemplate = new RestTemplate();
    public OpaClient() {
    }

    public boolean isAuthorized(String user, String operation) {
        OpaResponse userResponse = hasUserAuthorization(user, operation);
        log.info("User {} got authorization response for operation {}: {}", user, operation, userResponse.result());

        return authorized(userResponse);
    }

    private OpaResponse hasUserAuthorization(String user, String operation) {
        return restTemplate.postForObject(opaUrl, createOpaRequestData(user, operation), OpaResponse.class);
    }

    private static boolean authorized(OpaResponse jsonNode) {
        return jsonNode.result() != null && jsonNode.result().equals("true");
    }

    private static Map<String, Object> createOpaRequestData(String user, String operation) {
        Map<String, Object> input = Map.of("input", new OpaRequest(user, operation));
        return input;
    }

    private record OpaRequest(String user, String operation) {
    }

    protected record OpaResponse(String result) {
    }

    protected OpaClient setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
        return this;
    }
}
