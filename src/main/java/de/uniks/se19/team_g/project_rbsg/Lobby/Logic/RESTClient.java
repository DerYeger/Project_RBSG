package de.uniks.se19.team_g.project_rbsg.Lobby.Logic;

import org.springframework.http.*;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

public class RESTClient
{
    private static final String baseURL = "https://rbsg.uniks.de/api";
    private final RestTemplate restTemplate;
    private HttpHeaders httpHeaders;
    private String uri;

    public RESTClient() {
        restTemplate = new RestTemplate();
    }

    public String get(final @NonNull String endpoint, final @Nullable MultiValueMap<String, String> headers, final @Nullable MultiValueMap<String, String> param) {
        return getString(endpoint, headers, param, HttpMethod.GET);
    }

    public String post(final @NonNull String endpoint ,final @Nullable MultiValueMap<String, String> headers, final @Nullable MultiValueMap<String, String> param, final @NonNull String body)  {
        prepareHeaderAndUri(endpoint, headers, param);

        HttpEntity<String> httpEntity = new HttpEntity<>(body, httpHeaders);
        ResponseEntity<String> responseEntity = restTemplate.exchange(uri, HttpMethod.POST, httpEntity, String.class);
        HttpStatus httpStatus = responseEntity.getStatusCode();
        if(httpStatus.is2xxSuccessful()) {
            return responseEntity.getBody();
        }

        return null;
    }

    //TODO: Delete
    public String delete(final @NonNull String endpoint ,final @Nullable MultiValueMap<String, String> headers, final @Nullable MultiValueMap<String, String> param)  {
        return getString(endpoint, headers, param, HttpMethod.DELETE);
    }

    private String getString(@NonNull String endpoint, @Nullable MultiValueMap<String, String> headers, @Nullable MultiValueMap<String, String> param, HttpMethod delete)
    {
        prepareHeaderAndUri(endpoint, headers, param);

        HttpEntity<String> httpEntity = new HttpEntity<>("", httpHeaders);
        ResponseEntity<String> responseEntity = restTemplate.exchange(uri, delete, httpEntity, String.class);
        HttpStatus httpStatus = responseEntity.getStatusCode();
        if(httpStatus.is2xxSuccessful()) {
            return responseEntity.getBody();
        }

        return null;
    }

    private void prepareHeaderAndUri(@NonNull String endpoint, @Nullable MultiValueMap<String, String> headers, @Nullable MultiValueMap<String, String> param)
    {
        httpHeaders = new HttpHeaders();
        if(headers != null) {
            httpHeaders.addAll(headers);
        }

        if(param != null) {
            uri = UriComponentsBuilder.fromHttpUrl(baseURL + endpoint).queryParams(param).toUriString();

        }
        else {
            uri = baseURL + endpoint;
        }

        System.out.println(uri);
    }

}
