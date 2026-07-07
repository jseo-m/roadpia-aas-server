package org.payment.client;

import com.fasterxml.jackson.databind.JsonNode;
import org.payment.exception.BasyxClientException;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Component
public class BasyxClient {

    private final WebClient basyxWebClient;

    public BasyxClient(WebClient basyxWebClient) {
        this.basyxWebClient = basyxWebClient;
    }

    public JsonNode getShells() {
        return get("/shells");
    }

    public JsonNode getShell(String aasId) {
        return get("/shells/" + encodeIdentifier(aasId));
    }

    public JsonNode getSubmodels() {
        return get("/submodels");
    }

    public JsonNode getSubmodel(String submodelId) {
        return get("/submodels/" + encodeIdentifier(submodelId));
    }

    private JsonNode get(String uri) {
        try {
            return basyxWebClient.get()
                    .uri(uri)
                    .retrieve()
                    .bodyToMono(JsonNode.class)
                    .block();
        } catch (WebClientResponseException ex) {
            throw new BasyxClientException(
                    "BaSyx 요청 실패: " + uri,
                    ex.getStatusCode().value(),
                    ex.getResponseBodyAsString(),
                    ex
            );
        } catch (RuntimeException ex) {
            throw new BasyxClientException("BaSyx 요청 실패: " + uri, 500, ex.getMessage(), ex);
        }
    }

    private String encodeIdentifier(String identifier) {
        return Base64.getUrlEncoder()
                .withoutPadding()
                .encodeToString(identifier.getBytes(StandardCharsets.UTF_8));
    }
}
