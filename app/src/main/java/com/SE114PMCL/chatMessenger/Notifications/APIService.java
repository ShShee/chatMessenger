package com.SE114PMCL.chatMessenger.Notifications;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {
    @Headers(
            {
                    "Content-Type:application/json",
                    "Authorization:key=AAAAhg2BS7A:APA91bGN-6i4y3AnpEZ1t_hgKuSh7FFbwVJgNjSkd_2PAKsl1jJV5dIeiJKUi-w_57uhGs3MSm-fVm6l6qblyLZVbr7JvLrwijw2WwjE-MrYu7Kkvb7KtnPCpGE-wT7lQZfhFEIICl8M"
            }
    )

    @POST("fcm/send")
    Call<MyResponse> sendNotification(@Body Sender body);
}

