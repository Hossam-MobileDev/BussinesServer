package com.hashtagco.bussinesserver.Remote;

import com.hashtagco.bussinesserver.Model.DataMessage;
import com.hashtagco.bussinesserver.Model.MyResponse;
import com.hashtagco.bussinesserver.Model.Sender;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface ApiService
{

    //send message Notification
    @Headers(
            {
                    "Content-Type: application/json",
                    "Authorization: key=AAAAPlHxnig:APA91bEMcdDt_t-pf-rWkXNPPCpYnwY3ibxC-tYI0-GuHpaNO5L17I3_J3V1gtaN9V3T1Qb7NPoeL48uEgooPJp93c6Xo2UEzXGlCcA7x2NSZXrXUK8jNuwi-h-yRP8TAsDdWRFxIDP_"
            })

    @POST("fcm/send")
    Call<MyResponse> sendNotification(@Body Sender body);



}
