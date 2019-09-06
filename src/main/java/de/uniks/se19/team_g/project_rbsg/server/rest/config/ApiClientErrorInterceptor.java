package de.uniks.se19.team_g.project_rbsg.server.rest.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.uniks.se19.team_g.project_rbsg.server.rest.RBSGSResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;

import javax.annotation.Nonnull;
import java.io.IOException;

@Component
public class ApiClientErrorInterceptor implements ClientHttpRequestInterceptor {

    private final ObjectMapper mapper;
    private final Logger logger = LoggerFactory.getLogger(getClass());

    public ApiClientErrorInterceptor(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    @Nonnull
    public ClientHttpResponse intercept(
            @Nonnull HttpRequest request,
            @Nonnull byte[] body,
            @Nonnull ClientHttpRequestExecution execution
    ) throws IOException {

        try {
            return execution.execute(request, body);
        } catch (HttpClientErrorException httpClientError) {
            final RBSGSResponse response;
            try {
                response = mapper.readValue(httpClientError.getResponseBodyAsString(), RBSGSResponse.class);
            } catch (Exception ex) {
                logger.error("failed to inspect handle http client error", ex);
                throw httpClientError;
            }
            String message = response.message.isEmpty() ? httpClientError.getStatusText() : response.message;
            // throw new RBSGClientError(message, httpClientError);
            final var domainError = new HttpClientErrorException(httpClientError.getStatusCode(), message, httpClientError.getResponseHeaders(), httpClientError.getResponseBodyAsByteArray(), null);
            domainError.addSuppressed(httpClientError);
            throw domainError;
        }
    }
}
