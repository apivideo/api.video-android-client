package video.api.client.api.auth;

import video.api.client.api.clients.AdvancedAuthenticationApi;
import video.api.client.api.ApiClient;
import video.api.client.api.ApiException;
import video.api.client.api.models.AccessToken;
import video.api.client.api.models.AuthenticatePayload;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

public class ApiVideoAuthInterceptor implements Interceptor {

    private final String apiKey;
    private final AdvancedAuthenticationApi authenticationApi;
    private String cachedAccessToken;
    private Long tokenExpirationMs;

    public ApiVideoAuthInterceptor(ApiClient client, String apiKey) {
        this.apiKey = apiKey;
        this.authenticationApi = new AdvancedAuthenticationApi(client);
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();

        if (request.url().toString().equals(authenticationApi.getApiClient().getBasePath() + "/auth/api-key")) {
            return chain.proceed(request);
        }

        try {
            return chain.proceed(request.newBuilder().addHeader("authorization", "Bearer " + getAccessToken()).build());
        } catch (ApiException e) {
            throw new IOException(e);
        }
    }

    public String getAccessToken() throws ApiException {
        if (!isTokenValid()) {
            retrieveAccessToken();
        }
        return this.cachedAccessToken;
    }

    private void retrieveAccessToken() throws ApiException {
        AccessToken accessToken = authenticationApi.authenticate(new AuthenticatePayload().apiKey(apiKey));

        Integer expires = accessToken.getExpiresIn();
        this.tokenExpirationMs = System.currentTimeMillis() + (expires - 60) * 1000;
        this.cachedAccessToken = accessToken.getAccessToken();

    }

    private boolean isTokenValid() {
        return tokenExpirationMs != null && tokenExpirationMs > System.currentTimeMillis();
    }
}
