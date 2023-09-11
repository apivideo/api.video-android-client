# WebhooksApi

All URIs are relative to *https://ws.api.video*

Method | HTTP request | Description
------------- | ------------- | -------------
[**create**](WebhooksApi.md#create) | **POST** /webhooks | Create Webhook
[**get**](WebhooksApi.md#get) | **GET** /webhooks/{webhookId} | Retrieve Webhook details
[**delete**](WebhooksApi.md#delete) | **DELETE** /webhooks/{webhookId} | Delete a Webhook
[**list**](WebhooksApi.md#list) | **GET** /webhooks | List all webhooks


<a name="create"></a>
# **create**
> Webhook create(webhooksCreationPayload)

Create Webhook

Webhooks can push notifications to your server, rather than polling api.video for changes. We currently offer four events:  * ```video.encoding.quality.completed``` Occurs when a new video is uploaded into your account, it will be encoded into several different HLS and mp4 qualities. When each version is encoded, your webhook will get a notification.  It will look like ```{ \"type\": \"video.encoding.quality.completed\", \"emittedAt\": \"2021-01-29T16:46:25.217+01:00\", \"videoId\": \"viXXXXXXXX\", \"encoding\": \"hls\", \"quality\": \"720p\"} ```. This request says that the 720p HLS encoding was completed. * ```live-stream.broadcast.started```  When a live stream begins broadcasting, the broadcasting parameter changes from false to true, and this webhook fires. * ```live-stream.broadcast.ended```  This event fires when a live stream has finished broadcasting. * ```video.source.recorded```  This event occurs when a live stream is recorded and submitted for encoding.

### Example
```java
// Import classes:
import video.api.client.ApiVideoClient;
import video.api.client.api.ApiException;
import video.api.client.api.models.*;
import video.api.client.api.clients.WebhooksApi;
import java.util.*;

public class Example {
  public static void main(String[] args) {
    ApiVideoClient client = new ApiVideoClient("YOUR_API_KEY");
    // if you rather like to use the sandbox environment:
    // ApiVideoClient client = new ApiVideoClient("YOUR_SANDBOX_API_KEY", Environment.SANDBOX);

    WebhooksApi apiInstance = client.webhooks();
    
    WebhooksCreationPayload webhooksCreationPayload = new WebhooksCreationPayload(); // 
    webhooksCreationPayload.setEvents(Arrays.asList("video.encoding.quality.completed")); // A list of the webhooks that you are subscribing to. There are Currently four webhook options:
* &#x60;&#x60;&#x60;video.encoding.quality.completed&#x60;&#x60;&#x60;  Occurs when a new video is uploaded into your account, it will be encoded into several different HLS and mp4 qualities. When each version is encoded, your webhook will get a notification.  It will look like &#x60;&#x60;&#x60;{ \&quot;type\&quot;: \&quot;video.encoding.quality.completed\&quot;, \&quot;emittedAt\&quot;: \&quot;2021-01-29T16:46:25.217+01:00\&quot;, \&quot;videoId\&quot;: \&quot;viXXXXXXXX\&quot;, \&quot;encoding\&quot;: \&quot;hls\&quot;, \&quot;quality\&quot;: \&quot;720p\&quot;} &#x60;&#x60;&#x60;. This request says that the 720p HLS encoding was completed.
* &#x60;&#x60;&#x60;live-stream.broadcast.started&#x60;&#x60;&#x60;  When a live stream begins broadcasting, the broadcasting parameter changes from false to true, and this webhook fires.
* &#x60;&#x60;&#x60;live-stream.broadcast.ended&#x60;&#x60;&#x60;  This event fires when a live stream has finished broadcasting.
* &#x60;&#x60;&#x60;video.source.recorded&#x60;&#x60;&#x60;  Occurs when a live stream is recorded and submitted for encoding.
    webhooksCreationPayload.setUrl("https://example.com/webhooks"); // The the url to which HTTP notifications are sent. It could be any http or https URL.


    try {
      Webhook result = apiInstance.create(webhooksCreationPayload);
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling WebhooksApi#create");
      System.err.println("Status code: " + e.getCode());
      System.err.println("Reason: " + e.getMessage());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }
  }
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **webhooksCreationPayload** | [**WebhooksCreationPayload**](WebhooksCreationPayload.md)|  |

### Return type


[**Webhook**](Webhook.md)

### Authorization

[API key](../README.md#api-key)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
**201** | Created |  -  |
**400** | Bad Request |  -  |

<a name="get"></a>
# **get**
> Webhook get(webhookId)

Retrieve Webhook details

Retrieve webhook details by id.

### Example
```java
// Import classes:
import video.api.client.ApiVideoClient;
import video.api.client.api.ApiException;
import video.api.client.api.models.*;
import video.api.client.api.clients.WebhooksApi;
import java.util.*;

public class Example {
  public static void main(String[] args) {
    ApiVideoClient client = new ApiVideoClient("YOUR_API_KEY");
    // if you rather like to use the sandbox environment:
    // ApiVideoClient client = new ApiVideoClient("YOUR_SANDBOX_API_KEY", Environment.SANDBOX);

    WebhooksApi apiInstance = client.webhooks();
    
    String webhookId = "webhookId_example"; // The unique webhook you wish to retreive details on.

    try {
      Webhook result = apiInstance.get(webhookId);
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling WebhooksApi#get");
      System.err.println("Status code: " + e.getCode());
      System.err.println("Reason: " + e.getMessage());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }
  }
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **webhookId** | **String**| The unique webhook you wish to retreive details on. |

### Return type


[**Webhook**](Webhook.md)

### Authorization

[API key](../README.md#api-key)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
**200** | Success |  -  |

<a name="delete"></a>
# **delete**
> delete(webhookId)

Delete a Webhook

This method will delete the indicated webhook.

### Example
```java
// Import classes:
import video.api.client.ApiVideoClient;
import video.api.client.api.ApiException;
import video.api.client.api.models.*;
import video.api.client.api.clients.WebhooksApi;
import java.util.*;

public class Example {
  public static void main(String[] args) {
    ApiVideoClient client = new ApiVideoClient("YOUR_API_KEY");
    // if you rather like to use the sandbox environment:
    // ApiVideoClient client = new ApiVideoClient("YOUR_SANDBOX_API_KEY", Environment.SANDBOX);

    WebhooksApi apiInstance = client.webhooks();
    
    String webhookId = "webhookId_example"; // The webhook you wish to delete.

    try {
      apiInstance.delete(webhookId);
    } catch (ApiException e) {
      System.err.println("Exception when calling WebhooksApi#delete");
      System.err.println("Status code: " + e.getCode());
      System.err.println("Reason: " + e.getMessage());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }
  }
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **webhookId** | **String**| The webhook you wish to delete. |

### Return type


null (empty response body)

### Authorization

[API key](../README.md#api-key)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
**204** | No Content |  -  |
**404** | Not Found |  -  |

<a name="list"></a>
# **list**
> WebhooksListResponse list().events(events).currentPage(currentPage).pageSize(pageSize).execute();

List all webhooks

Thie method returns a list of your webhooks (with all their details). 

You can filter what the webhook list that the API returns using the parameters described below.

### Example
```java
// Import classes:
import video.api.client.ApiVideoClient;
import video.api.client.api.ApiException;
import video.api.client.api.models.*;
import video.api.client.api.clients.WebhooksApi;
import java.util.*;

public class Example {
  public static void main(String[] args) {
    ApiVideoClient client = new ApiVideoClient("YOUR_API_KEY");
    // if you rather like to use the sandbox environment:
    // ApiVideoClient client = new ApiVideoClient("YOUR_SANDBOX_API_KEY", Environment.SANDBOX);

    WebhooksApi apiInstance = client.webhooks();
    
    String events = "video.encoding.quality.completed"; // The webhook event that you wish to filter on.
    Integer currentPage = 1; // Choose the number of search results to return per page. Minimum value: 1
    Integer pageSize = 25; // Results per page. Allowed values 1-100, default is 25.

    try {
      Page<Webhook> result = apiInstance.list()
            .events(events)
            .currentPage(currentPage)
            .pageSize(pageSize)
            .execute();
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling WebhooksApi#list");
      System.err.println("Status code: " + e.getCode());
      System.err.println("Reason: " + e.getMessage());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }
  }
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **events** | **String**| The webhook event that you wish to filter on. | [optional]
 **currentPage** | **Integer**| Choose the number of search results to return per page. Minimum value: 1 | [optional] [default to 1]
 **pageSize** | **Integer**| Results per page. Allowed values 1-100, default is 25. | [optional] [default to 25]

### Return type

[**Page**](pagination.md)<[**Webhook**](Webhook.md)>


### Authorization

[API key](../README.md#api-key)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
**200** | Success |  -  |

