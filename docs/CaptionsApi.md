# CaptionsApi

All URIs are relative to *https://ws.api.video*

Method | HTTP request | Description
------------- | ------------- | -------------
[**upload**](CaptionsApi.md#upload) | **POST** /videos/{videoId}/captions/{language} | Upload a caption
[**get**](CaptionsApi.md#get) | **GET** /videos/{videoId}/captions/{language} | Retrieve a caption
[**update**](CaptionsApi.md#update) | **PATCH** /videos/{videoId}/captions/{language} | Update a caption
[**delete**](CaptionsApi.md#delete) | **DELETE** /videos/{videoId}/captions/{language} | Delete a caption
[**list**](CaptionsApi.md#list) | **GET** /videos/{videoId}/captions | List video captions


<a name="upload"></a>
# **upload**
> Caption upload(videoId, language, file)
> okhttp3.Call uploadAsync(videoId, language, file, callback)
> ApiResponse<Caption> uploadWithHttpInfo(videoId, language, file)

Upload a caption

Upload a VTT file to add captions to your video. More information can be found [here](https://docs.api.video/vod/add-captions)

### Example
```java
// Import classes:
import video.api.client.ApiVideoClient;
import video.api.client.api.ApiException;
import video.api.client.api.models.*;
import video.api.client.api.clients.CaptionsApi;
import java.util.*;

public class Example {
  public static void main(String[] args) {
    ApiVideoClient client = new ApiVideoClient("YOUR_API_KEY");
    // if you rather like to use the sandbox environment:
    // ApiVideoClient client = new ApiVideoClient("YOUR_SANDBOX_API_KEY", Environment.SANDBOX);

    CaptionsApi apiInstance = client.captions();
    
    String videoId = "vi4k0jvEUuaTdRAEjQ4Prklg"; // The unique identifier for the video you want to add a caption to.
    String language = "en"; // A valid language identifier using IETF language tags. You can use primary subtags like `en` (English), extended subtags like `fr-CA` (French, Canada), or region subtags like `zh-Hans-CN` (Simplified Chinese used in the PRC).  - This parameter **only accepts dashes for separators**, for example `fr-CA`. If you use a different separator in your request, the API returns an error. - When the value in your request does not match any covered language, the API returns an error. - You can find the list of supported tags [here](https://docs.api.video/vod/add-captions#supported-caption-language-tags).
    File file = new File("/path/to/file"); // The video text track (VTT) you want to upload.

    try {
      Caption result = apiInstance.upload(videoId, language, file);
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling CaptionsApi#upload");
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
 **videoId** | **String**| The unique identifier for the video you want to add a caption to. |
 **language** | **String**| A valid language identifier using IETF language tags. You can use primary subtags like &#x60;en&#x60; (English), extended subtags like &#x60;fr-CA&#x60; (French, Canada), or region subtags like &#x60;zh-Hans-CN&#x60; (Simplified Chinese used in the PRC).  - This parameter **only accepts dashes for separators**, for example &#x60;fr-CA&#x60;. If you use a different separator in your request, the API returns an error. - When the value in your request does not match any covered language, the API returns an error. - You can find the list of supported tags [here](https://docs.api.video/vod/add-captions#supported-caption-language-tags). |
 **file** | **File**| The video text track (VTT) you want to upload. |

### Return type


[**Caption**](Caption.md)

### Authorization

[API key](../README.md#api-key)

### HTTP request headers

 - **Content-Type**: multipart/form-data
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
**200** | Success |  * X-RateLimit-Limit - The request limit per minute. <br>  * X-RateLimit-Remaining - The number of available requests left for the current time window. <br>  * X-RateLimit-Retry-After - The number of seconds left until the current rate limit window resets. <br>  |
**400** | Bad request error |  * X-RateLimit-Limit - The request limit per minute. <br>  * X-RateLimit-Remaining - The number of available requests left for the current time window. <br>  * X-RateLimit-Retry-After - The number of seconds left until the current rate limit window resets. <br>  |
**404** | Not Found |  * X-RateLimit-Limit - The request limit per minute. <br>  * X-RateLimit-Remaining - The number of available requests left for the current time window. <br>  * X-RateLimit-Retry-After - The number of seconds left until the current rate limit window resets. <br>  |
**429** | Too Many Requests |  * X-RateLimit-Limit - The request limit per minute. <br>  * X-RateLimit-Remaining - The number of available requests left for the current time window. <br>  * X-RateLimit-Retry-After - The number of seconds left until the current rate limit window resets. <br>  |

<a name="get"></a>
# **get**
> Caption get(videoId, language)
> okhttp3.Call getAsync(videoId, language, callback)
> ApiResponse<Caption> getWithHttpInfo(videoId, language)

Retrieve a caption

Retrieve a caption for a video in a specific language. If the language is available, the caption is returned. Otherwise, you will get a error indicating the caption was not found.

Tutorials that use the [captions endpoint](https://api.video/blog/endpoints/captions).

### Example
```java
// Import classes:
import video.api.client.ApiVideoClient;
import video.api.client.api.ApiException;
import video.api.client.api.models.*;
import video.api.client.api.clients.CaptionsApi;
import java.util.*;

public class Example {
  public static void main(String[] args) {
    ApiVideoClient client = new ApiVideoClient("YOUR_API_KEY");
    // if you rather like to use the sandbox environment:
    // ApiVideoClient client = new ApiVideoClient("YOUR_SANDBOX_API_KEY", Environment.SANDBOX);

    CaptionsApi apiInstance = client.captions();
    
    String videoId = "vi4k0jvEUuaTdRAEjQ4Prklg"; // The unique identifier for the video you want captions for.
    String language = "en"; // A valid language identifier using IETF language tags. You can use primary subtags like `en` (English), extended subtags like `fr-CA` (French, Canada), or region subtags like `zh-Hans-CN` (Simplified Chinese used in the PRC).  - This parameter **only accepts dashes for separators**, for example `fr-CA`. If you use a different separator in your request, the API returns an error. - When the value in your request does not match any covered language, the API returns an error. - You can find the list of supported tags [here](https://docs.api.video/vod/add-captions#supported-caption-language-tags).

    try {
      Caption result = apiInstance.get(videoId, language);
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling CaptionsApi#get");
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
 **videoId** | **String**| The unique identifier for the video you want captions for. |
 **language** | **String**| A valid language identifier using IETF language tags. You can use primary subtags like &#x60;en&#x60; (English), extended subtags like &#x60;fr-CA&#x60; (French, Canada), or region subtags like &#x60;zh-Hans-CN&#x60; (Simplified Chinese used in the PRC).  - This parameter **only accepts dashes for separators**, for example &#x60;fr-CA&#x60;. If you use a different separator in your request, the API returns an error. - When the value in your request does not match any covered language, the API returns an error. - You can find the list of supported tags [here](https://docs.api.video/vod/add-captions#supported-caption-language-tags). |

### Return type


[**Caption**](Caption.md)

### Authorization

[API key](../README.md#api-key)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
**200** | Success |  * X-RateLimit-Limit - The request limit per minute. <br>  * X-RateLimit-Remaining - The number of available requests left for the current time window. <br>  * X-RateLimit-Retry-After - The number of seconds left until the current rate limit window resets. <br>  |
**400** | Bad request error |  * X-RateLimit-Limit - The request limit per minute. <br>  * X-RateLimit-Remaining - The number of available requests left for the current time window. <br>  * X-RateLimit-Retry-After - The number of seconds left until the current rate limit window resets. <br>  |
**404** | Not Found |  * X-RateLimit-Limit - The request limit per minute. <br>  * X-RateLimit-Remaining - The number of available requests left for the current time window. <br>  * X-RateLimit-Retry-After - The number of seconds left until the current rate limit window resets. <br>  |
**429** | Too Many Requests |  * X-RateLimit-Limit - The request limit per minute. <br>  * X-RateLimit-Remaining - The number of available requests left for the current time window. <br>  * X-RateLimit-Retry-After - The number of seconds left until the current rate limit window resets. <br>  |

<a name="update"></a>
# **update**
> Caption update(videoId, language, captionsUpdatePayload)
> okhttp3.Call updateAsync(videoId, language, captionsUpdatePayload, callback)
> ApiResponse<Caption> updateWithHttpInfo(videoId, language, captionsUpdatePayload)

Update a caption

To have the captions on automatically, use this method to set default: true.

### Example
```java
// Import classes:
import video.api.client.ApiVideoClient;
import video.api.client.api.ApiException;
import video.api.client.api.models.*;
import video.api.client.api.clients.CaptionsApi;
import java.util.*;

public class Example {
  public static void main(String[] args) {
    ApiVideoClient client = new ApiVideoClient("YOUR_API_KEY");
    // if you rather like to use the sandbox environment:
    // ApiVideoClient client = new ApiVideoClient("YOUR_SANDBOX_API_KEY", Environment.SANDBOX);

    CaptionsApi apiInstance = client.captions();
    
    String videoId = "vi4k0jvEUuaTdRAEjQ4Prklg"; // The unique identifier for the video you want to have automatic captions for.
    String language = "en"; // A valid language identifier using IETF language tags. You can use primary subtags like `en` (English), extended subtags like `fr-CA` (French, Canada), or region subtags like `zh-Hans-CN` (Simplified Chinese used in the PRC).  - This parameter **only accepts dashes for separators**, for example `fr-CA`. If you use a different separator in your request, the API returns an error. - When the value in your request does not match any covered language, the API returns an error. - You can find the list of supported tags [here](https://docs.api.video/vod/add-captions#supported-caption-language-tags).
    CaptionsUpdatePayload captionsUpdatePayload = new CaptionsUpdatePayload(); // 
    captionsUpdatePayload.setDefault(); // Set this parameter to &#x60;true&#x60; to define a caption as the default for a video.


    try {
      Caption result = apiInstance.update(videoId, language, captionsUpdatePayload);
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling CaptionsApi#update");
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
 **videoId** | **String**| The unique identifier for the video you want to have automatic captions for. |
 **language** | **String**| A valid language identifier using IETF language tags. You can use primary subtags like &#x60;en&#x60; (English), extended subtags like &#x60;fr-CA&#x60; (French, Canada), or region subtags like &#x60;zh-Hans-CN&#x60; (Simplified Chinese used in the PRC).  - This parameter **only accepts dashes for separators**, for example &#x60;fr-CA&#x60;. If you use a different separator in your request, the API returns an error. - When the value in your request does not match any covered language, the API returns an error. - You can find the list of supported tags [here](https://docs.api.video/vod/add-captions#supported-caption-language-tags). |
 **captionsUpdatePayload** | [**CaptionsUpdatePayload**](CaptionsUpdatePayload.md)|  |

### Return type


[**Caption**](Caption.md)

### Authorization

[API key](../README.md#api-key)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
**200** | Success |  * X-RateLimit-Limit - The request limit per minute. <br>  * X-RateLimit-Remaining - The number of available requests left for the current time window. <br>  * X-RateLimit-Retry-After - The number of seconds left until the current rate limit window resets. <br>  |
**400** | Bad request error |  * X-RateLimit-Limit - The request limit per minute. <br>  * X-RateLimit-Remaining - The number of available requests left for the current time window. <br>  * X-RateLimit-Retry-After - The number of seconds left until the current rate limit window resets. <br>  |
**404** | Not Found |  * X-RateLimit-Limit - The request limit per minute. <br>  * X-RateLimit-Remaining - The number of available requests left for the current time window. <br>  * X-RateLimit-Retry-After - The number of seconds left until the current rate limit window resets. <br>  |
**429** | Too Many Requests |  * X-RateLimit-Limit - The request limit per minute. <br>  * X-RateLimit-Remaining - The number of available requests left for the current time window. <br>  * X-RateLimit-Retry-After - The number of seconds left until the current rate limit window resets. <br>  |

<a name="delete"></a>
# **delete**
> delete(videoId, language)
> okhttp3.Call deleteAsync(videoId, language, callback)
> ApiResponse<Void> deleteWithHttpInfo(videoId, language)

Delete a caption

Delete a caption in a specific language by by video id.

### Example
```java
// Import classes:
import video.api.client.ApiVideoClient;
import video.api.client.api.ApiException;
import video.api.client.api.models.*;
import video.api.client.api.clients.CaptionsApi;
import java.util.*;

public class Example {
  public static void main(String[] args) {
    ApiVideoClient client = new ApiVideoClient("YOUR_API_KEY");
    // if you rather like to use the sandbox environment:
    // ApiVideoClient client = new ApiVideoClient("YOUR_SANDBOX_API_KEY", Environment.SANDBOX);

    CaptionsApi apiInstance = client.captions();
    
    String videoId = "vi4k0jvEUuaTdRAEjQ4Prklgc"; // The unique identifier for the video you want to delete a caption from.
    String language = "en"; // A valid language identifier using IETF language tags. You can use primary subtags like `en` (English), extended subtags like `fr-CA` (French, Canada), or region subtags like `zh-Hans-CN` (Simplified Chinese used in the PRC).  - This parameter **only accepts dashes for separators**, for example `fr-CA`. If you use a different separator in your request, the API returns an error. - When the value in your request does not match any covered language, the API returns an error. - You can find the list of supported tags [here](https://docs.api.video/vod/add-captions#supported-caption-language-tags).

    try {
      apiInstance.delete(videoId, language);
    } catch (ApiException e) {
      System.err.println("Exception when calling CaptionsApi#delete");
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
 **videoId** | **String**| The unique identifier for the video you want to delete a caption from. |
 **language** | **String**| A valid language identifier using IETF language tags. You can use primary subtags like &#x60;en&#x60; (English), extended subtags like &#x60;fr-CA&#x60; (French, Canada), or region subtags like &#x60;zh-Hans-CN&#x60; (Simplified Chinese used in the PRC).  - This parameter **only accepts dashes for separators**, for example &#x60;fr-CA&#x60;. If you use a different separator in your request, the API returns an error. - When the value in your request does not match any covered language, the API returns an error. - You can find the list of supported tags [here](https://docs.api.video/vod/add-captions#supported-caption-language-tags). |

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
**204** | No Content |  * X-RateLimit-Limit - The request limit per minute. <br>  * X-RateLimit-Remaining - The number of available requests left for the current time window. <br>  * X-RateLimit-Retry-After - The number of seconds left until the current rate limit window resets. <br>  |
**400** | Bad request error |  * X-RateLimit-Limit - The request limit per minute. <br>  * X-RateLimit-Remaining - The number of available requests left for the current time window. <br>  * X-RateLimit-Retry-After - The number of seconds left until the current rate limit window resets. <br>  |
**404** | Not Found |  * X-RateLimit-Limit - The request limit per minute. <br>  * X-RateLimit-Remaining - The number of available requests left for the current time window. <br>  * X-RateLimit-Retry-After - The number of seconds left until the current rate limit window resets. <br>  |
**429** | Too Many Requests |  * X-RateLimit-Limit - The request limit per minute. <br>  * X-RateLimit-Remaining - The number of available requests left for the current time window. <br>  * X-RateLimit-Retry-After - The number of seconds left until the current rate limit window resets. <br>  |

<a name="list"></a>
# **list**
> CaptionsListResponse list(videoId).currentPage(currentPage).pageSize(pageSize).execute()
> okhttp3.Call executeAsync(callback) 
> ApiResponse<CaptionsListResponse> executeWithHttpInfo()

List video captions

Retrieve a list of available captions by video id.

### Example
```java
// Import classes:
import video.api.client.ApiVideoClient;
import video.api.client.api.ApiException;
import video.api.client.api.models.*;
import video.api.client.api.clients.CaptionsApi;
import java.util.*;

public class Example {
  public static void main(String[] args) {
    ApiVideoClient client = new ApiVideoClient("YOUR_API_KEY");
    // if you rather like to use the sandbox environment:
    // ApiVideoClient client = new ApiVideoClient("YOUR_SANDBOX_API_KEY", Environment.SANDBOX);

    CaptionsApi apiInstance = client.captions();
    
    String videoId = "vi4k0jvEUuaTdRAEjQ4Prklg"; // The unique identifier for the video you want to retrieve a list of captions for.
    Integer currentPage = 1; // Choose the number of search results to return per page. Minimum value: 1
    Integer pageSize = 25; // Results per page. Allowed values 1-100, default is 25.

    try {
      Page<Caption> result = apiInstance.list(videoId)
            .currentPage(currentPage)
            .pageSize(pageSize)
            .execute();
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling CaptionsApi#list");
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
 **videoId** | **String**| The unique identifier for the video you want to retrieve a list of captions for. |
 **currentPage** | **Integer**| Choose the number of search results to return per page. Minimum value: 1 | [optional] [default to 1]
 **pageSize** | **Integer**| Results per page. Allowed values 1-100, default is 25. | [optional] [default to 25]

### Return type

[**Page**](pagination.md)<[**Caption**](Caption.md)>


### Authorization

[API key](../README.md#api-key)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
**200** | Success |  * X-RateLimit-Limit - The request limit per minute. <br>  * X-RateLimit-Remaining - The number of available requests left for the current time window. <br>  * X-RateLimit-Retry-After - The number of seconds left until the current rate limit window resets. <br>  |
**404** | Not Found |  * X-RateLimit-Limit - The request limit per minute. <br>  * X-RateLimit-Remaining - The number of available requests left for the current time window. <br>  * X-RateLimit-Retry-After - The number of seconds left until the current rate limit window resets. <br>  |
**429** | Too Many Requests |  * X-RateLimit-Limit - The request limit per minute. <br>  * X-RateLimit-Remaining - The number of available requests left for the current time window. <br>  * X-RateLimit-Retry-After - The number of seconds left until the current rate limit window resets. <br>  |

