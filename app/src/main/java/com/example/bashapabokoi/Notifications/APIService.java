package com.example.bashapabokoi.Notifications;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {

    @Headers(
            {
                    "Content-Type:application/json",
                    "Authorization: Key = AAAABTWquoM:APA91bHG0o8gV2hPstrYEfd7pOJXa7L4S5AvQUM6_JX7A8UwRAfr5B0mlX-bcgS-QInPoQuuYtwhlFzAi5DIt_hhfOW56ZaSafeRW0RJyZ6BSBoR2kXVNlvOQICH-B9tvNaJAyY0ghxt"
            }
    )

    @POST("fcm/send")
    Call<MyResponse> sendNotification(@Body Sender body);


}
