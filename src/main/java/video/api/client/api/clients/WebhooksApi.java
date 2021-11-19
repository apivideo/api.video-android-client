/*
 * api.video Java API client
 * api.video is an API that encodes on the go to facilitate immediate playback, enhancing viewer streaming experiences across multiple devices and platforms. You can stream live or on-demand online videos within minutes.
 *
 * The version of the OpenAPI document: 1
 * Contact: ecosystem@api.video
 *
 * NOTE: This class is auto generated.
 * Do not edit the class manually.
 */

package video.api.client.api.clients;

import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.IOException;

import video.api.client.api.models.*;
import video.api.client.api.upload.*;
import video.api.client.api.*;

import java.lang.reflect.Type;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WebhooksApi {
    private ApiClient localVarApiClient;

    public WebhooksApi(ApiClient apiClient) {
        this.localVarApiClient = apiClient;
    }

    /**
     * Constructor for WebhooksApi production environment where API key is not required.
     */
    public WebhooksApi() {
        this.localVarApiClient = new ApiClient(Environment.PRODUCTION.basePath);
    }

    /**
     * Constructor for WebhooksApi with custom API base path where API key is not required.
     * 
     * @param basePath
     *            the api base path. Expected Environment.PRODUCTION.basePath (default) or Environment.SANDBOX.basePath.
     */
    public WebhooksApi(String basePath) {
        this.localVarApiClient = new ApiClient(basePath);
    }

    /**
     * Constructor for WebhooksApi with custom API base path
     * 
     * @param apiKey
     *            the api key to use to authenticate to the API
     * @param basePath
     *            the api base path. Expected Environment.PRODUCTION.basePath (default) or Environment.SANDBOX.basePath.
     */
    public WebhooksApi(String apiKey, String basePath) {
        this.localVarApiClient = new ApiClient(apiKey, basePath);
    }

    public ApiClient getApiClient() {
        return localVarApiClient;
    }

    public void setApiClient(ApiClient apiClient) {
        this.localVarApiClient = apiClient;
    }

    /**
     * Build call for delete
     * 
     * @param webhookId
     *            The webhook you wish to delete. (required)
     * @param _callback
     *            Callback for upload/download progress
     * 
     * @return Call to execute
     * 
     * @throws ApiException
     *             If fail to serialize the request body object
     * 
     * @http.response.details
     *                        <table summary="Response Details" border="1">
     *                        <tr>
     *                        <td>Status Code</td>
     *                        <td>Description</td>
     *                        <td>Response Headers</td>
     *                        </tr>
     *                        <tr>
     *                        <td>204</td>
     *                        <td>No Content</td>
     *                        <td>-</td>
     *                        </tr>
     *                        <tr>
     *                        <td>404</td>
     *                        <td>Not Found</td>
     *                        <td>-</td>
     *                        </tr>
     *                        </table>
     */
    private okhttp3.Call deleteCall(String webhookId, final ApiCallback _callback) throws ApiException {
        Object localVarPostBody = null;

        // create path and map variables
        String localVarPath = "/webhooks/{webhookId}".replaceAll("\\{" + "webhookId" + "\\}",
                localVarApiClient.escapeString(webhookId.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        Map<String, String> localVarHeaderParams = new HashMap<String, String>();
        Map<String, String> localVarCookieParams = new HashMap<String, String>();
        Map<String, Object> localVarFormParams = new HashMap<String, Object>();

        final String[] localVarAccepts = { "application/json" };
        final String localVarAccept = localVarApiClient.selectHeaderAccept(localVarAccepts);
        if (localVarAccept != null) {
            localVarHeaderParams.put("Accept", localVarAccept);
        }

        final String[] localVarContentTypes = {

        };
        final String localVarContentType = localVarApiClient.selectHeaderContentType(localVarContentTypes);
        localVarHeaderParams.put("Content-Type", localVarContentType);

        String[] localVarAuthNames = new String[] { "bearerAuth" };
        return localVarApiClient.buildCall(localVarPath, "DELETE", localVarQueryParams, localVarCollectionQueryParams,
                localVarPostBody, localVarHeaderParams, localVarCookieParams, localVarFormParams, localVarAuthNames,
                _callback);
    }

    @SuppressWarnings("rawtypes")
    private okhttp3.Call deleteValidateBeforeCall(String webhookId, final ApiCallback _callback) throws ApiException {

        // verify the required parameter 'webhookId' is set
        if (webhookId == null) {
            throw new ApiException("Missing the required parameter 'webhookId' when calling delete");
        }

        okhttp3.Call localVarCall = deleteCall(webhookId, _callback);
        return localVarCall;
    }

    /**
     * Delete a Webhook This endpoint will delete the indicated webhook.
     * 
     * @param webhookId
     *            The webhook you wish to delete. (required)
     * 
     * @throws ApiException
     *             If fail to call the API, e.g. server error or cannot deserialize the response body
     * 
     * @http.response.details
     *                        <table summary="Response Details" border="1">
     *                        <tr>
     *                        <td>Status Code</td>
     *                        <td>Description</td>
     *                        <td>Response Headers</td>
     *                        </tr>
     *                        <tr>
     *                        <td>204</td>
     *                        <td>No Content</td>
     *                        <td>-</td>
     *                        </tr>
     *                        <tr>
     *                        <td>404</td>
     *                        <td>Not Found</td>
     *                        <td>-</td>
     *                        </tr>
     *                        </table>
     */
    public void delete(String webhookId) throws ApiException {
        deleteWithHttpInfo(webhookId);
    }

    /**
     * Delete a Webhook This endpoint will delete the indicated webhook.
     * 
     * @param webhookId
     *            The webhook you wish to delete. (required)
     * 
     * @return ApiResponse&lt;Void&gt;
     * 
     * @throws ApiException
     *             If fail to call the API, e.g. server error or cannot deserialize the response body
     * 
     * @http.response.details
     *                        <table summary="Response Details" border="1">
     *                        <tr>
     *                        <td>Status Code</td>
     *                        <td>Description</td>
     *                        <td>Response Headers</td>
     *                        </tr>
     *                        <tr>
     *                        <td>204</td>
     *                        <td>No Content</td>
     *                        <td>-</td>
     *                        </tr>
     *                        <tr>
     *                        <td>404</td>
     *                        <td>Not Found</td>
     *                        <td>-</td>
     *                        </tr>
     *                        </table>
     */
    public ApiResponse<Void> deleteWithHttpInfo(String webhookId) throws ApiException {
        okhttp3.Call localVarCall = deleteValidateBeforeCall(webhookId, null);
        return localVarApiClient.execute(localVarCall);
    }

    /**
     * Build call for get
     * 
     * @param webhookId
     *            The unique webhook you wish to retreive details on. (required)
     * @param _callback
     *            Callback for upload/download progress
     * 
     * @return Call to execute
     * 
     * @throws ApiException
     *             If fail to serialize the request body object
     * 
     * @http.response.details
     *                        <table summary="Response Details" border="1">
     *                        <tr>
     *                        <td>Status Code</td>
     *                        <td>Description</td>
     *                        <td>Response Headers</td>
     *                        </tr>
     *                        <tr>
     *                        <td>200</td>
     *                        <td>Success</td>
     *                        <td>-</td>
     *                        </tr>
     *                        </table>
     */
    private okhttp3.Call getCall(String webhookId, final ApiCallback _callback) throws ApiException {
        Object localVarPostBody = null;

        // create path and map variables
        String localVarPath = "/webhooks/{webhookId}".replaceAll("\\{" + "webhookId" + "\\}",
                localVarApiClient.escapeString(webhookId.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        Map<String, String> localVarHeaderParams = new HashMap<String, String>();
        Map<String, String> localVarCookieParams = new HashMap<String, String>();
        Map<String, Object> localVarFormParams = new HashMap<String, Object>();

        final String[] localVarAccepts = { "application/json" };
        final String localVarAccept = localVarApiClient.selectHeaderAccept(localVarAccepts);
        if (localVarAccept != null) {
            localVarHeaderParams.put("Accept", localVarAccept);
        }

        final String[] localVarContentTypes = {

        };
        final String localVarContentType = localVarApiClient.selectHeaderContentType(localVarContentTypes);
        localVarHeaderParams.put("Content-Type", localVarContentType);

        String[] localVarAuthNames = new String[] { "bearerAuth" };
        return localVarApiClient.buildCall(localVarPath, "GET", localVarQueryParams, localVarCollectionQueryParams,
                localVarPostBody, localVarHeaderParams, localVarCookieParams, localVarFormParams, localVarAuthNames,
                _callback);
    }

    @SuppressWarnings("rawtypes")
    private okhttp3.Call getValidateBeforeCall(String webhookId, final ApiCallback _callback) throws ApiException {

        // verify the required parameter 'webhookId' is set
        if (webhookId == null) {
            throw new ApiException("Missing the required parameter 'webhookId' when calling get");
        }

        okhttp3.Call localVarCall = getCall(webhookId, _callback);
        return localVarCall;
    }

    /**
     * Show Webhook details This call provides the same JSON information provided on Webjhook creation.
     * 
     * @param webhookId
     *            The unique webhook you wish to retreive details on. (required)
     * 
     * @return Webhook
     * 
     * @throws ApiException
     *             If fail to call the API, e.g. server error or cannot deserialize the response body
     * 
     * @http.response.details
     *                        <table summary="Response Details" border="1">
     *                        <tr>
     *                        <td>Status Code</td>
     *                        <td>Description</td>
     *                        <td>Response Headers</td>
     *                        </tr>
     *                        <tr>
     *                        <td>200</td>
     *                        <td>Success</td>
     *                        <td>-</td>
     *                        </tr>
     *                        </table>
     */
    public Webhook get(String webhookId) throws ApiException {
        ApiResponse<Webhook> localVarResp = getWithHttpInfo(webhookId);
        return localVarResp.getData();
    }

    /**
     * Show Webhook details This call provides the same JSON information provided on Webjhook creation.
     * 
     * @param webhookId
     *            The unique webhook you wish to retreive details on. (required)
     * 
     * @return ApiResponse&lt;Webhook&gt;
     * 
     * @throws ApiException
     *             If fail to call the API, e.g. server error or cannot deserialize the response body
     * 
     * @http.response.details
     *                        <table summary="Response Details" border="1">
     *                        <tr>
     *                        <td>Status Code</td>
     *                        <td>Description</td>
     *                        <td>Response Headers</td>
     *                        </tr>
     *                        <tr>
     *                        <td>200</td>
     *                        <td>Success</td>
     *                        <td>-</td>
     *                        </tr>
     *                        </table>
     */
    public ApiResponse<Webhook> getWithHttpInfo(String webhookId) throws ApiException {
        okhttp3.Call localVarCall = getValidateBeforeCall(webhookId, null);
        Type localVarReturnType = new TypeToken<Webhook>() {
        }.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call listCall(String events, Integer currentPage, Integer pageSize, final ApiCallback _callback)
            throws ApiException {
        Object localVarPostBody = null;

        // create path and map variables
        String localVarPath = "/webhooks";

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        Map<String, String> localVarHeaderParams = new HashMap<String, String>();
        Map<String, String> localVarCookieParams = new HashMap<String, String>();
        Map<String, Object> localVarFormParams = new HashMap<String, Object>();

        if (events != null) {
            localVarQueryParams.addAll(localVarApiClient.parameterToPair("events", events));
        }

        if (currentPage != null) {
            localVarQueryParams.addAll(localVarApiClient.parameterToPair("currentPage", currentPage));
        }

        if (pageSize != null) {
            localVarQueryParams.addAll(localVarApiClient.parameterToPair("pageSize", pageSize));
        }

        final String[] localVarAccepts = { "application/json" };
        final String localVarAccept = localVarApiClient.selectHeaderAccept(localVarAccepts);
        if (localVarAccept != null) {
            localVarHeaderParams.put("Accept", localVarAccept);
        }

        final String[] localVarContentTypes = {

        };
        final String localVarContentType = localVarApiClient.selectHeaderContentType(localVarContentTypes);
        localVarHeaderParams.put("Content-Type", localVarContentType);

        String[] localVarAuthNames = new String[] { "bearerAuth" };
        return localVarApiClient.buildCall(localVarPath, "GET", localVarQueryParams, localVarCollectionQueryParams,
                localVarPostBody, localVarHeaderParams, localVarCookieParams, localVarFormParams, localVarAuthNames,
                _callback);
    }

    @SuppressWarnings("rawtypes")
    private okhttp3.Call listValidateBeforeCall(String events, Integer currentPage, Integer pageSize,
            final ApiCallback _callback) throws ApiException {

        okhttp3.Call localVarCall = listCall(events, currentPage, pageSize, _callback);
        return localVarCall;
    }

    private ApiResponse<WebhooksListResponse> listWithHttpInfo(String events, Integer currentPage, Integer pageSize)
            throws ApiException {
        okhttp3.Call localVarCall = listValidateBeforeCall(events, currentPage, pageSize, null);
        Type localVarReturnType = new TypeToken<WebhooksListResponse>() {
        }.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    public class APIlistRequest {
        private String events;
        private Integer currentPage;
        private Integer pageSize;

        private APIlistRequest() {
        }

        /**
         * Set events
         * 
         * @param events
         *            The webhook event that you wish to filter on. (optional)
         * 
         * @return APIlistRequest
         */
        public APIlistRequest events(String events) {
            this.events = events;
            return this;
        }

        /**
         * Set currentPage
         * 
         * @param currentPage
         *            Choose the number of search results to return per page. Minimum value: 1 (optional, default to 1)
         * 
         * @return APIlistRequest
         */
        public APIlistRequest currentPage(Integer currentPage) {
            this.currentPage = currentPage;
            return this;
        }

        /**
         * Set pageSize
         * 
         * @param pageSize
         *            Results per page. Allowed values 1-100, default is 25. (optional, default to 25)
         * 
         * @return APIlistRequest
         */
        public APIlistRequest pageSize(Integer pageSize) {
            this.pageSize = pageSize;
            return this;
        }

        /**
         * Build call for list
         * 
         * @param _callback
         *            ApiCallback API callback
         * 
         * @return Call to execute
         * 
         * @throws ApiException
         *             If fail to serialize the request body object
         * 
         * @http.response.details
         *                        <table summary="Response Details" border="1">
         *                        <tr>
         *                        <td>Status Code</td>
         *                        <td>Description</td>
         *                        <td>Response Headers</td>
         *                        </tr>
         *                        <tr>
         *                        <td>200</td>
         *                        <td>Success</td>
         *                        <td>-</td>
         *                        </tr>
         *                        </table>
         */
        public okhttp3.Call buildCall(final ApiCallback _callback) throws ApiException {
            return listCall(events, currentPage, pageSize, _callback);
        }

        /**
         * Execute list request
         * 
         * @return WebhooksListResponse
         * 
         * @throws ApiException
         *             If fail to call the API, e.g. server error or cannot deserialize the response body
         * 
         * @http.response.details
         *                        <table summary="Response Details" border="1">
         *                        <tr>
         *                        <td>Status Code</td>
         *                        <td>Description</td>
         *                        <td>Response Headers</td>
         *                        </tr>
         *                        <tr>
         *                        <td>200</td>
         *                        <td>Success</td>
         *                        <td>-</td>
         *                        </tr>
         *                        </table>
         */
        public Page<Webhook> execute() throws ApiException {
            ApiResponse<WebhooksListResponse> localVarResp = listWithHttpInfo(events, currentPage, pageSize);
            return new Page<>(localVarResp.getData().getData(), localVarResp.getData().getPagination(), () -> {
                try {
                    return copy().currentPage((currentPage == null ? 1 : currentPage) + 1).execute();
                } catch (ApiException e) {
                    throw new RuntimeException(e);
                }
            }); //
        }

        private APIlistRequest copy() {
            APIlistRequest copy = new APIlistRequest();
            copy.events(events);
            copy.currentPage(currentPage);
            copy.pageSize(pageSize);
            return copy;
        }

        /**
         * Execute list request with HTTP info returned
         * 
         * @return ApiResponse&lt;WebhooksListResponse&gt;
         * 
         * @throws ApiException
         *             If fail to call the API, e.g. server error or cannot deserialize the response body
         * 
         * @http.response.details
         *                        <table summary="Response Details" border="1">
         *                        <tr>
         *                        <td>Status Code</td>
         *                        <td>Description</td>
         *                        <td>Response Headers</td>
         *                        </tr>
         *                        <tr>
         *                        <td>200</td>
         *                        <td>Success</td>
         *                        <td>-</td>
         *                        </tr>
         *                        </table>
         */
        public ApiResponse<WebhooksListResponse> executeWithHttpInfo() throws ApiException {
            return listWithHttpInfo(events, currentPage, pageSize);
        }

        /**
         * Execute list request (asynchronously)
         * 
         * @param _callback
         *            The callback to be executed when the API call finishes
         * 
         * @return The request call
         * 
         * @throws ApiException
         *             If fail to process the API call, e.g. serializing the request body object
         * 
         * @http.response.details
         *                        <table summary="Response Details" border="1">
         *                        <tr>
         *                        <td>Status Code</td>
         *                        <td>Description</td>
         *                        <td>Response Headers</td>
         *                        </tr>
         *                        <tr>
         *                        <td>200</td>
         *                        <td>Success</td>
         *                        <td>-</td>
         *                        </tr>
         *                        </table>
         * 
         *                        public okhttp3.Call executeAsync(final ApiCallback<WebhooksListResponse> _callback)
         *                        throws ApiException { return listAsync(events, currentPage, pageSize, _callback); }
         */
    }

    /**
     * List all webhooks Requests to this endpoint return a list of your webhooks (with all their details). You can
     * filter what the webhook list that the API returns using the parameters described below.
     * 
     * @return APIlistRequest
     * 
     * @http.response.details
     *                        <table summary="Response Details" border="1">
     *                        <tr>
     *                        <td>Status Code</td>
     *                        <td>Description</td>
     *                        <td>Response Headers</td>
     *                        </tr>
     *                        <tr>
     *                        <td>200</td>
     *                        <td>Success</td>
     *                        <td>-</td>
     *                        </tr>
     *                        </table>
     */
    public APIlistRequest list() {
        return new APIlistRequest();
    }

    /**
     * Build call for create
     * 
     * @param webhooksCreationPayload
     *            (required)
     * @param _callback
     *            Callback for upload/download progress
     * 
     * @return Call to execute
     * 
     * @throws ApiException
     *             If fail to serialize the request body object
     * 
     * @http.response.details
     *                        <table summary="Response Details" border="1">
     *                        <tr>
     *                        <td>Status Code</td>
     *                        <td>Description</td>
     *                        <td>Response Headers</td>
     *                        </tr>
     *                        <tr>
     *                        <td>201</td>
     *                        <td>Created</td>
     *                        <td>-</td>
     *                        </tr>
     *                        <tr>
     *                        <td>400</td>
     *                        <td>Bad Request</td>
     *                        <td>-</td>
     *                        </tr>
     *                        </table>
     */
    private okhttp3.Call createCall(WebhooksCreationPayload webhooksCreationPayload, final ApiCallback _callback)
            throws ApiException {
        Object localVarPostBody = webhooksCreationPayload;

        // create path and map variables
        String localVarPath = "/webhooks";

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        Map<String, String> localVarHeaderParams = new HashMap<String, String>();
        Map<String, String> localVarCookieParams = new HashMap<String, String>();
        Map<String, Object> localVarFormParams = new HashMap<String, Object>();

        final String[] localVarAccepts = { "application/json" };
        final String localVarAccept = localVarApiClient.selectHeaderAccept(localVarAccepts);
        if (localVarAccept != null) {
            localVarHeaderParams.put("Accept", localVarAccept);
        }

        final String[] localVarContentTypes = { "application/json" };
        final String localVarContentType = localVarApiClient.selectHeaderContentType(localVarContentTypes);
        localVarHeaderParams.put("Content-Type", localVarContentType);

        String[] localVarAuthNames = new String[] { "bearerAuth" };
        return localVarApiClient.buildCall(localVarPath, "POST", localVarQueryParams, localVarCollectionQueryParams,
                localVarPostBody, localVarHeaderParams, localVarCookieParams, localVarFormParams, localVarAuthNames,
                _callback);
    }

    @SuppressWarnings("rawtypes")
    private okhttp3.Call createValidateBeforeCall(WebhooksCreationPayload webhooksCreationPayload,
            final ApiCallback _callback) throws ApiException {

        // verify the required parameter 'webhooksCreationPayload' is set
        if (webhooksCreationPayload == null) {
            throw new ApiException("Missing the required parameter 'webhooksCreationPayload' when calling create");
        }

        if (webhooksCreationPayload.getEvents() == null) {
            throw new ApiException(
                    "Missing the required parameter 'webhooksCreationPayload.events' when calling create");
        }

        if (webhooksCreationPayload.getUrl() == null) {
            throw new ApiException("Missing the required parameter 'webhooksCreationPayload.url' when calling create");
        }

        okhttp3.Call localVarCall = createCall(webhooksCreationPayload, _callback);
        return localVarCall;
    }

    /**
     * Create Webhook Webhooks can push notifications to your server, rather than polling api.video for changes. We
     * currently offer four events: * &#x60;&#x60;&#x60;video.encoding.quality.completed&#x60;&#x60;&#x60; When a new
     * video is uploaded into your account, it will be encoded into several different HLS sizes/bitrates. When each
     * version is encoded, your webhook will get a notification. It will look like &#x60;&#x60;&#x60;{
     * \\\&quot;type\\\&quot;: \\\&quot;video.encoding.quality.completed\\\&quot;, \\\&quot;emittedAt\\\&quot;:
     * \\\&quot;2021-01-29T16:46:25.217+01:00\\\&quot;, \\\&quot;videoId\\\&quot;: \\\&quot;viXXXXXXXX\\\&quot;,
     * \\\&quot;encoding\\\&quot;: \\\&quot;hls\\\&quot;, \\\&quot;quality\\\&quot;: \\\&quot;720p\\\&quot;}
     * &#x60;&#x60;&#x60;. This request says that the 720p HLS encoding was completed. *
     * &#x60;&#x60;&#x60;live-stream.broadcast.started&#x60;&#x60;&#x60; When a livestream begins broadcasting, the
     * broadcasting parameter changes from false to true, and this webhook fires. *
     * &#x60;&#x60;&#x60;live-stream.broadcast.ended&#x60;&#x60;&#x60; This event fores when the livestream has finished
     * broadcasting, and the broadcasting parameter flips from false to true. *
     * &#x60;&#x60;&#x60;video.source.recorded&#x60;&#x60;&#x60; This event is similar to
     * &#x60;&#x60;&#x60;video.encoding.quality.completed&#x60;&#x60;&#x60;, but tells you if a livestream has been
     * recorded as a VOD.
     * 
     * @param webhooksCreationPayload
     *            (required)
     * 
     * @return Webhook
     * 
     * @throws ApiException
     *             If fail to call the API, e.g. server error or cannot deserialize the response body
     * 
     * @http.response.details
     *                        <table summary="Response Details" border="1">
     *                        <tr>
     *                        <td>Status Code</td>
     *                        <td>Description</td>
     *                        <td>Response Headers</td>
     *                        </tr>
     *                        <tr>
     *                        <td>201</td>
     *                        <td>Created</td>
     *                        <td>-</td>
     *                        </tr>
     *                        <tr>
     *                        <td>400</td>
     *                        <td>Bad Request</td>
     *                        <td>-</td>
     *                        </tr>
     *                        </table>
     */
    public Webhook create(WebhooksCreationPayload webhooksCreationPayload) throws ApiException {
        ApiResponse<Webhook> localVarResp = createWithHttpInfo(webhooksCreationPayload);
        return localVarResp.getData();
    }

    /**
     * Create Webhook Webhooks can push notifications to your server, rather than polling api.video for changes. We
     * currently offer four events: * &#x60;&#x60;&#x60;video.encoding.quality.completed&#x60;&#x60;&#x60; When a new
     * video is uploaded into your account, it will be encoded into several different HLS sizes/bitrates. When each
     * version is encoded, your webhook will get a notification. It will look like &#x60;&#x60;&#x60;{
     * \\\&quot;type\\\&quot;: \\\&quot;video.encoding.quality.completed\\\&quot;, \\\&quot;emittedAt\\\&quot;:
     * \\\&quot;2021-01-29T16:46:25.217+01:00\\\&quot;, \\\&quot;videoId\\\&quot;: \\\&quot;viXXXXXXXX\\\&quot;,
     * \\\&quot;encoding\\\&quot;: \\\&quot;hls\\\&quot;, \\\&quot;quality\\\&quot;: \\\&quot;720p\\\&quot;}
     * &#x60;&#x60;&#x60;. This request says that the 720p HLS encoding was completed. *
     * &#x60;&#x60;&#x60;live-stream.broadcast.started&#x60;&#x60;&#x60; When a livestream begins broadcasting, the
     * broadcasting parameter changes from false to true, and this webhook fires. *
     * &#x60;&#x60;&#x60;live-stream.broadcast.ended&#x60;&#x60;&#x60; This event fores when the livestream has finished
     * broadcasting, and the broadcasting parameter flips from false to true. *
     * &#x60;&#x60;&#x60;video.source.recorded&#x60;&#x60;&#x60; This event is similar to
     * &#x60;&#x60;&#x60;video.encoding.quality.completed&#x60;&#x60;&#x60;, but tells you if a livestream has been
     * recorded as a VOD.
     * 
     * @param webhooksCreationPayload
     *            (required)
     * 
     * @return ApiResponse&lt;Webhook&gt;
     * 
     * @throws ApiException
     *             If fail to call the API, e.g. server error or cannot deserialize the response body
     * 
     * @http.response.details
     *                        <table summary="Response Details" border="1">
     *                        <tr>
     *                        <td>Status Code</td>
     *                        <td>Description</td>
     *                        <td>Response Headers</td>
     *                        </tr>
     *                        <tr>
     *                        <td>201</td>
     *                        <td>Created</td>
     *                        <td>-</td>
     *                        </tr>
     *                        <tr>
     *                        <td>400</td>
     *                        <td>Bad Request</td>
     *                        <td>-</td>
     *                        </tr>
     *                        </table>
     */
    public ApiResponse<Webhook> createWithHttpInfo(WebhooksCreationPayload webhooksCreationPayload)
            throws ApiException {
        okhttp3.Call localVarCall = createValidateBeforeCall(webhooksCreationPayload, null);
        Type localVarReturnType = new TypeToken<Webhook>() {
        }.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

}
