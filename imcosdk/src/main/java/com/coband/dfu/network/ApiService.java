package com.coband.dfu.network;


import com.coband.dfu.network.bean.AllFwResultBean;
import com.coband.dfu.network.bean.FwBean;
import com.coband.dfu.network.bean.FwResultBean;
import com.coband.dfu.network.bean.LastFirmwareResultBean;
import com.coband.dfu.network.bean.LastFwBean;
import com.coband.utils.FOTAConfig;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

/**
 * Created by Administrator on 2016/3/23.
 */
public interface ApiService {

    @Headers({"AppOS: Android", "AppKey: " + FOTAConfig.K9_APP_KEY})
    @POST("checkin")
    Observable<FwResultBean> checkFw(@Header("Timestamp") String timestamp,
                                     @Header("AppVersion") String appVersion,
                                     @Header("Nonce") String nonce,
                                     @Header("Sign") String sign,
                                     @Body FwBean info);

//           .addHeader("Timestamp", (System.currentTimeMillis()/1000)+"")
//            .addHeader("AppOS", "android")
    @POST("checkAll")
    Observable<AllFwResultBean> checkAllFw(@Body FwBean info);

    @Headers({"AppOS: Android", "AppKey: " + FOTAConfig.K9_APP_KEY})
    @POST("latestFirmwares")
    Observable<LastFirmwareResultBean> checkLastFirmwares(
            @Header("Timestamp") String timestamp,
            @Header("Nonce") String nonce,
            @Header("Sign") String sign,
            @Body LastFwBean info
    );

    @Streaming //大文件时要加不然会OOM
    @GET
    Observable<ResponseBody> downloadFile(@Url String fileUrl);
//    @POST("login")
//    Observable<User> login(@Body LoginInfo info);
//    @POST("login")
//    Observable<User> login(@Body LoginInfo info);
//
//    @POST("requestEmailVerify")
//    Observable<Result> forceSendEmail(@Body ForgotPasswordInfo info);
//
//
//    // SportInfo
//    @GET("classes/SportInfo")
//    Observable<Result<SportInfo>> queryDaySportInfo(@JsonQueryParameters.Json @Query("where")
//                                                            SportDateFilter where);
//
//    @GET("classes/SportInfo")
//    Observable<Result<SportInfo>> queryAllSportInfo(@JsonQueryParameters.Json @Query("where")
//                                                            SportUserFilter where);
//
//    @GET("classes/SportInfo")
//    Observable<Result<SportInfo>> queryMultiSportInfo(@JsonQueryParameters.Json @Query("where")
//                                                              SportDateLimitFilter where);
//
//    @POST("classes/SportInfo")
//    Observable<UploadResult> postDaySportInfo(@Body SportInfo info);
//
//    @POST("batch")
//    Observable<List<BatchUploadResult>> batchPost(@Body BatchUploadData data);
//    // end
//
//    // SleepInfo
//    @POST("classes/SleepInfo")
//    Observable<UploadResult> postDaySleepInfo(@Body SleepInfo info);
//
//    @GET("classes/SleepInfo")
//    Observable<Result<SleepInfo>> queryDaySleepInfo(@JsonQueryParameters.Json @Query("where")
//                                                            SleepDateFilter where);
//
//    @GET("classes/SleepInfo")
//    Observable<Result<SleepInfo>> queryAllSleepInfo(@JsonQueryParameters.Json @Query("where")
//                                                            SleepUserFilter where);
//
//
//    @POST("users")
//    Observable<User> register(@Body SignUpInfo info);
//
//    @PUT("users/{objID}")
//    Observable<User> updateTarget(@Header("X-LC-Session") String session, @Path("objID") String objID,
//                                  @JsonQueryParameters.Json @Body TargetInfo info);
//
//
//    @PUT("users/{objID}")
//    Observable<User> updateUserInfo(@Header("X-LC-Session") String session, @Path("objID") String objID,
//                                    @JsonQueryParameters.Json @Body SignUpUserInfo info);
//
//    @POST("requestPasswordReset")
//    Observable<User> sendEmail(@Body ForgotPasswordInfo info);
//
//    // leaderBoardload
//
//    @GET("classes/TodayRank")
//    Observable<ResultsList<TodayRankBean>> getTodayRank(@Query("where") String where,
//                                                        @Query("limit") int limit,
//                                                        @Query("order") String order,
//                                                        @Query("include") String include);
//
//    @GET("classes/TodayRank")
//    Observable<ResultsList<TodayRankBean>> getUserTodayRank(@Query("where") String where,
//                                                            @Query("include") String include);
//
//
//    @POST("classes/TodayRank")
//    Observable<CreatedResult> createUserTodayRank(@Body UserTodayRankBean userTodayRankBean);
//
//    @PUT("classes/TodayRank/{objID}")
//    Observable<UpdateResult> updateLikes(@Path("objID") String objID, @JsonQueryParameters.Json @Body RankLikesBean likesBean);
//    // end
//
//    // Message
//    @GET("classes/_Conversation")
//    Observable<ResultsList<ConversationBean>> getUserConversation(@Query("where") String userId, @Query("order") String order);
//
//    @GET("classes/_User")
//    Observable<ResultsList<User>> getUsersInfo(@Query("where") String userIds);
//
//    // @Query("reversed") boolean reversed,  加上这个属性就会查询失败，原因未知
//    @GET("rtm/messages/history")
//    Observable<ArrayList<ChatMessageBean>> getChatMessage(@Query("convid") String convid,
//                                                          @Query("max_ts") long timestamp,
//                                                          @Query("limit") int limit);
//
//    @GET("rtm/messages/unread/{CLIENT_ID}/{CONVERSATION_ID}")
//    Observable<UnReadBean> getUnReadCount(@Path("CLIENT_ID") String clientId, @Path("CONVERSATION_ID") String conversationId);
//
//
//    @POST("classes/_Conversation")
//    Observable<ConversationCreateResultBean> createConversation(@Body ConversationBean conversationBean);
//
//    @Streaming //大文件时要加不然会OOM
//    @Headers({"Content-Type: audio/x-wav"})
//    @GET
//    Observable<ResponseBody> downloadFile(@Url String fileUrl);
//
//    @Headers({"X-LC-Key: h6umob59ghl0f723hbjgs4fniakknmgjl2vlt1s47n409uys,master"})
//    @POST("rtm/messages")
//    Observable<Object> sendTextMessage(@Body SendMessageBean sendMessageBean);
//
//    @Headers({"Content-Type: audio/x-wav"})
//    @POST("files/{fileName}")
//    Observable<FileBean> upLoadFile(@Body RequestBody body, @Path("fileName") String fileName);
//
//    @Headers({"Content-Type: image/png"})
//    @POST("files/{fileName}")
//    Observable<FileBean> upLoadImageFile(@Body RequestBody body, @Path("fileName") String fileName);
//
//    // end
//
//    // Follower and Followee
//    @GET("users/{userID}/followersAndFollowees")
//    Observable<FollowersAndFolloweesBean> getFollowersAndFollowees(@Path("userID") String userID);
//
//    @POST("users/{masterId}/friendship/{followerId}")
//    Observable<ResultsList> addFriends(@Path("masterId") String masterId, @Path("followerId") String followerId);
//
//    @DELETE("users/{masterId}/friendship/{followerId}")
//    Observable<ResultsList> deleteFriends(@Path("masterId") String masterId, @Path("followerId") String followerId);
//
//    @GET("users")
//    Observable<ResultsList<User>> getActiveUser(@Query("limit") int limit, @Query("order") String order);
//
//    @GET("users")
//    Observable<ResultsList<User>> searchUserByName(@Query("where") String userName);
//
//    @GET("users")
//    Observable<ResultsList<User>> searchUserByEmail(@Query("where") String email);
//
//    @GET("users")
//    Observable<ResultsList<User>> getUserByEmail(@Query("where") String email);
//    //end
//
//    // HeartRateInfo
//    @POST("classes/HeartRateInfo")
//    Observable<UploadResult> postDayHeartRateInfo(@Body HeartRateInfo info);
//
//    @GET("classes/HeartRateInfo")
//    Observable<Result<HeartRateInfo>> queryDayHeartRateInfo(@JsonQueryParameters.Json @Query("where")
//                                                                    HeartRateDateFilter where);
//
//    @GET("classes/HeartRateInfo")
//    Observable<Result<HeartRateInfo>> queryAllHeartRateInfo(@JsonQueryParameters.Json @Query("where")
//                                                                    HeartRateUserFilter where);
//
//    @GET("classes/HeartRateInfo")
//    Observable<Result<HeartRateInfo>> queryMultiHeartRateInfo(@JsonQueryParameters.Json @Query("where")
//                                                                      HeartRateDateLimitFilter where);
//    //end
//
//    //weight
//    @POST("classes/Weight")
//    Observable<UploadResult> postDayWeight(@Body Weight weight);
//
//    @GET("classes/Weight")
//    Observable<Result<Weight>> queryAllWeight(@JsonQueryParameters.Json @Query("where")
//                                                      WeightUserFilter where);
//
//    @GET("classes/Weight")
//    Observable<Result<Weight>> queryDayWeight(@JsonQueryParameters.Json @Query("where")
//                                                      WeightDateFilter where);
//
//    @GET("classes/Weight")
//    Observable<Result<Weight>> queryMultiWeight(@JsonQueryParameters.Json @Query("where")
//                                                        WeightDateLimitFilter where);
//    //end
//
//    //image
//    @PUT("users/{objID}")
//    Observable<User> updateAvatar(@Header("X-LC-Session") String session, @Path("objID") String objID,
//                                  @JsonQueryParameters.Json @Body AvatarBean info);
//
//    @PUT("users/{objID}")
//    Observable<User> updateSurfaceImg(@Header("X-LC-Session") String session, @Path("objID") String objID,
//                                      @JsonQueryParameters.Json @Body SurfaceImgBean info);
//    //end
//
//    //unit
//    @PUT("users/{objID}")
//    Observable<User> updateUnit(@Header("X-LC-Session") String session, @Path("objID") String objID,
//                                @JsonQueryParameters.Json @Body ConvertUnitBean info);
//
//    @GET("classes/_User")
//    Observable<Result<UpdatedAtBean>> queryUpdateAt(@Query("where") String objID, @Query("keys") String updatedAt);
//
//    @PUT("users/{objID}")
//    Observable<User> updateAllDB(@Header("X-LC-Session") String session, @Path("objID") String objID,
//                                 @JsonQueryParameters.Json @Body ConvertUnitBean user);
//
//    @DELETE("classes/Weight/{userObjId}")
//    Observable<EmptyResult> deleteWeight(@Path("userObjId") String userObjId,
//                                         @JsonQueryParameters.Json @Query("where") DateFilter filter);
//
//    @GET("classes/_User")
//    Observable<Result<User>> queryUserName(@Query("where") String username);
//
//    @GET("classes/_User")
//    Observable<Result<User>> queryMail(@Query("where") String mail);
//
//
//    @DELETE("classes/Weight/{objId}")
//    Observable<EmptyResult> deleteWeight(@Header("X-LC-Session") String session, @Path("objId") String objId);

}
