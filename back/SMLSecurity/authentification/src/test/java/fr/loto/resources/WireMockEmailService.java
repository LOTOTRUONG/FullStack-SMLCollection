package fr.loto.resources;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import io.quarkus.test.common.QuarkusTestResourceLifecycleManager;

import java.util.Collections;
import java.util.Map;

public class WireMockEmailService implements QuarkusTestResourceLifecycleManager {
    WireMockServer wireMockServer;

    @Override
    public Map<String, String> start() {
        wireMockServer = new WireMockServer(8088);
        wireMockServer.start();

        // create some stubs
        wireMockServer.stubFor(
                WireMock.post(WireMock.urlEqualTo("/email"))
                        .withHeader("ApiKey", WireMock.equalTo("pIPgm7NoG2BeD27x"))
                       .willReturn(WireMock.aResponse()
                               .withStatus(200)
                              )
                );

        return Collections.singletonMap("%test.quarkus.rest-client.mail-service.url", wireMockServer.baseUrl());

    }

    @Override
    public synchronized void stop() {
        if (wireMockServer != null) {
            wireMockServer.stop();
            wireMockServer = null;
        }
    }
}
