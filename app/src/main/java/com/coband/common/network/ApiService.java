package com.coband.common.network;

import com.coband.cocoband.mvp.model.bean.AvatarBean;
import com.coband.cocoband.mvp.model.bean.AvatarFileBean;
import com.coband.cocoband.mvp.model.bean.ChatMessageBean;
import com.coband.cocoband.mvp.model.bean.ConvertUnitBean;
import com.coband.cocoband.mvp.model.bean.DynamicURLBean;
import com.coband.cocoband.mvp.model.bean.ForgotPasswordInfo;
import com.coband.cocoband.mvp.model.bean.GetMedalBean;
import com.coband.cocoband.mvp.model.bean.LoginInfo;
import com.coband.cocoband.mvp.model.bean.ResultsList;
import com.coband.cocoband.mvp.model.bean.SignUpInfo;
import com.coband.cocoband.mvp.model.bean.SignUpUserInfo;
import com.coband.cocoband.mvp.model.bean.SingleWeightBean;
import com.coband.cocoband.mvp.model.bean.SurfaceImgBean;
import com.coband.cocoband.mvp.model.bean.SurfaceImgFileBean;
import com.coband.cocoband.mvp.model.bean.TargetInfo;
import com.coband.cocoband.mvp.model.bean.TotalWalkCountBean;
import com.coband.cocoband.mvp.model.bean.UnReadBean;
import com.coband.cocoband.mvp.model.bean.UpdatedAtBean;
import com.coband.cocoband.mvp.model.bean.User;
import com.coband.cocoband.mvp.model.bean.followersandfollowees.FollowersAndFolloweesBean;
import com.coband.cocoband.mvp.model.bean.message.ConversationBean;
import com.coband.cocoband.mvp.model.bean.message.ConversationCreateResultBean;
import com.coband.cocoband.mvp.model.bean.message.FileBean;
import com.coband.cocoband.mvp.model.bean.message.SendMessageBean;
import com.coband.cocoband.mvp.model.bean.todayrank.CreatedResult;
import com.coband.cocoband.mvp.model.bean.todayrank.RankLikesBean;
import com.coband.cocoband.mvp.model.bean.todayrank.TodayRankBean;
import com.coband.cocoband.mvp.model.bean.todayrank.TodayRankStep;
import com.coband.cocoband.mvp.model.bean.todayrank.UpdateResult;
import com.coband.cocoband.mvp.model.bean.todayrank.UserTodayRankBean;
import com.coband.cocoband.mvp.model.entity.BatchUploadData;
import com.coband.cocoband.mvp.model.entity.BatchUploadResult;
import com.coband.cocoband.mvp.model.entity.BloodPressureInfo;
import com.coband.cocoband.mvp.model.entity.EmptyResult;
import com.coband.cocoband.mvp.model.entity.FOTAResult;
import com.coband.cocoband.mvp.model.entity.HeartRateInfo;
import com.coband.cocoband.mvp.model.entity.request.DeviceMessage;
import com.coband.cocoband.mvp.model.entity.request.EmailRegisterContent;
import com.coband.cocoband.mvp.model.entity.request.LogInBody;
import com.coband.cocoband.mvp.model.entity.request.PhoneRegisterContent;
import com.coband.cocoband.mvp.model.entity.request.ResetPwdBody;
import com.coband.cocoband.mvp.model.entity.request.UpdateAccountInfo;
import com.coband.cocoband.mvp.model.entity.request.UpdateTargetRequest;
import com.coband.cocoband.mvp.model.entity.request.UploadDataInfo;
import com.coband.cocoband.mvp.model.entity.response.LogInResponse;
import com.coband.cocoband.mvp.model.entity.response.RegisterResult;
import com.coband.cocoband.mvp.model.entity.Result;
import com.coband.cocoband.mvp.model.entity.SleepInfo;
import com.coband.cocoband.mvp.model.entity.SportInfo;
import com.coband.cocoband.mvp.model.entity.UploadResult;
import com.coband.cocoband.mvp.model.entity.Weight;
import com.coband.cocoband.mvp.model.entity.filter.DateFilter;
import com.coband.cocoband.mvp.model.entity.filter.HeartRateDateFilter;
import com.coband.cocoband.mvp.model.entity.filter.HeartRateDateLimitFilter;
import com.coband.cocoband.mvp.model.entity.filter.HeartRateUserFilter;
import com.coband.cocoband.mvp.model.entity.filter.PressureUserFilter;
import com.coband.cocoband.mvp.model.entity.filter.SleepDateFilter;
import com.coband.cocoband.mvp.model.entity.filter.SleepUserFilter;
import com.coband.cocoband.mvp.model.entity.filter.SportDateFilter;
import com.coband.cocoband.mvp.model.entity.filter.SportDateLimitFilter;
import com.coband.cocoband.mvp.model.entity.filter.SportUserFilter;
import com.coband.cocoband.mvp.model.entity.filter.WeightDateFilter;
import com.coband.cocoband.mvp.model.entity.filter.WeightDateLimitFilter;
import com.coband.cocoband.mvp.model.entity.filter.WeightUserFilter;
import com.coband.cocoband.mvp.model.entity.response.ResetPwdResponse;
import com.coband.cocoband.mvp.model.entity.response.UpdateAccountResponse;
import com.coband.cocoband.mvp.model.entity.response.UpdateTargetResponse;
import com.coband.cocoband.mvp.model.entity.response.UploadDataResponse;
import com.coband.cocoband.mvp.model.entity.response.UploadDeviceResponse;
import com.coband.cocoband.mvp.model.entity.response.VerifyCodeResult;
import com.coband.common.utils.FOTAConfig;
import com.coband.watchassistant.BuildConfig;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

/**
 * Created by Administrator on 2016/3/23.
 */
public interface ApiService {

    @GET
    Observable<DynamicURLBean> getDynamicURL(@Url String url, @Query("appId") String appId);

    @POST
    Observable<User> login(@Url String url, @Body LoginInfo info);

    @POST
    Observable<Result> forceSendEmail(@Url String url, @Body ForgotPasswordInfo info);


    // SportInfo
    @GET
    Observable<Result<SportInfo>> queryDaySportInfo(@Url String url, @JsonQueryParameters.Json @Query("where")
            SportDateFilter where);

    @GET
    Observable<Result<SportInfo>> queryAllSportInfo(@Url String url, @JsonQueryParameters.Json @Query("where")
            SportUserFilter where);

    @GET
    Observable<Result<SportInfo>> queryMultiSportInfo(@Url String url, @JsonQueryParameters.Json @Query("where")
            SportDateLimitFilter where);

    @POST
    Observable<UploadResult> postDaySportInfo(@Url String url, @Body SportInfo info);

    @POST
    Observable<List<BatchUploadResult>> batchPost(@Url String url, @Body BatchUploadData data);

    @PUT
    Observable<User> updateSingleWeight(@Url String url, @Header("X-LC-Session") String session,
                                        @Body SingleWeightBean weight);
    // end

    // SleepInfo
    @POST
    Observable<UploadResult> postDaySleepInfo(@Url String url, @Body SleepInfo info);

    @GET
    Observable<Result<SleepInfo>> queryDaySleepInfo(@Url String url, @JsonQueryParameters.Json @Query("where")
            SleepDateFilter where);

    @GET
    Observable<Result<SleepInfo>> queryAllSleepInfo(@Url String url, @JsonQueryParameters.Json @Query("where")
            SleepUserFilter where);


    @POST
    Observable<User> register(@Url String url, @Body SignUpInfo info);

    @PUT
    Observable<User> updateTarget(@Url String url, @Header("X-LC-Session") String session,
                                  @JsonQueryParameters.Json @Body TargetInfo info);


    @PUT
    Observable<User> updateUserInfo(@Url String url, @Header("X-LC-Session") String session,
                                    @JsonQueryParameters.Json @Body SignUpUserInfo info);

    @POST
    Observable<User> sendEmail(@Url String url, @Body ForgotPasswordInfo info);

    // leaderBoard

    @GET
    Observable<ResultsList<TodayRankBean>> getTodayRank(@Url String url, @Query("where") String where,
                                                        @Query("limit") int limit,
                                                        @Query("order") String order,
                                                        @Query("include") String include);

    @GET
    Observable<ResultsList<TodayRankBean>> getUserTodayRank(@Url String url, @Query("where") String where,
                                                            @Query("include") String include);


    @POST
    Observable<CreatedResult> createUserTodayRank(@Url String url, @Body UserTodayRankBean userTodayRankBean);

    @PUT
    Observable<UpdateResult> updateUserTodayRank(@Url String url, @Body TodayRankStep step);

    @PUT
    Observable<UpdateResult> updateLikes(@Url String url, @JsonQueryParameters.Json @Body RankLikesBean likesBean);
    // end

    // Message
    @GET
    Observable<ResultsList<ConversationBean>> getUserConversation(@Url String url, @Query("where") String userId, @Query("order") String order);

    @GET
    Observable<ResultsList<User>> getUsersInfo(@Url String url, @Query("where") String userIds);

    // @Query("reversed") boolean reversed,  加上这个属性就会查询失败，原因未知
    @GET
    Observable<ArrayList<ChatMessageBean>> getChatMessage(@Url String url, @Query("convid") String convid,
                                                          @Query("max_ts") long timestamp,
                                                          @Query("limit") int limit);

    @GET
    Observable<UnReadBean> getUnReadCount(@Url String url);


    @POST
    Observable<ConversationCreateResultBean> createConversation(@Url String url, @Body ConversationBean conversationBean);

    @Streaming //大文件时要加不然会OOM
    @Headers({"Content-Type: audio/x-wav"})
    @GET
    Observable<ResponseBody> downloadFile(@Url String fileUrl);

    @Headers({"X-LC-Key: h6umob59ghl0f723hbjgs4fniakknmgjl2vlt1s47n409uys,master"})
    @POST
    Observable<Object> sendTextMessage(@Url String url, @Body SendMessageBean sendMessageBean);

    @Headers({"Content-Type: audio/x-wav"})
    @POST
    Observable<FileBean> upLoadFile(@Url String url, @Body RequestBody body);

    @Headers({"Content-Type: image/png"})
    @POST
    Observable<FileBean> upLoadImageFile(@Url String url, @Body RequestBody body);

    // end

    // Follower and Followee
    @GET
    Observable<FollowersAndFolloweesBean> getFollowersAndFollowees(@Url String url);

    @POST
    Observable<ResultsList> addFriends(@Url String url);

    @DELETE
    Observable<ResultsList> deleteFriends(@Url String url);

    @GET
    Observable<ResultsList<User>> getActiveUser(@Url String url, @Query("limit") int limit, @Query("order") String order);

    @GET
    Observable<ResultsList<User>> searchUserByName(@Url String url, @Query("where") String userName);

    @GET
    Observable<ResultsList<User>> searchUserByEmail(@Url String url, @Query("where") String email);

    @GET("users")
    Observable<ResultsList<User>> getUserByEmail(@Url String url, @Query("where") String email);
    //end

    // HeartRateInfo
    @POST
    Observable<UploadResult> postDayHeartRateInfo(@Url String url, @Body HeartRateInfo info);

    @GET
    Observable<Result<HeartRateInfo>> queryDayHeartRateInfo(@Url String url, @JsonQueryParameters.Json @Query("where")
            HeartRateDateFilter where);

    @GET
    Observable<Result<HeartRateInfo>> queryAllHeartRateInfo(@Url String url, @JsonQueryParameters.Json @Query("where")
            HeartRateUserFilter where);

    @GET
    Observable<Result<HeartRateInfo>> queryMultiHeartRateInfo(@Url String url, @JsonQueryParameters.Json @Query("where")
            HeartRateDateLimitFilter where);
    //end

    //weight
    @POST
    Observable<UploadResult> postDayWeight(@Url String url, @Body Weight weight);

    @GET
    Observable<Result<Weight>> queryAllWeight(@Url String url, @JsonQueryParameters.Json @Query("where")
            WeightUserFilter where);

    @GET("classes/Weight")
    Observable<Result<Weight>> queryDayWeight(@Url String url, @JsonQueryParameters.Json @Query("where")
            WeightDateFilter where);

    @GET("classes/Weight")
    Observable<Result<Weight>> queryMultiWeight(@Url String url, @JsonQueryParameters.Json @Query("where")
            WeightDateLimitFilter where);
    //end

    //image
    @PUT
    Observable<User> updateAvatar(@Url String url, @Header("X-LC-Session") String session,
                                  @JsonQueryParameters.Json @Body AvatarBean info);

    @PUT
    Observable<User> attachAvatarFile(@Url String url, @Header("X-LC-Session") String session,
                                      @JsonQueryParameters.Json @Body AvatarFileBean info);

    @PUT
    Observable<User> updateSurfaceImg(@Url String url, @Header("X-LC-Session") String session,
                                      @JsonQueryParameters.Json @Body SurfaceImgBean info);

    @PUT
    Observable<User> attachSurfaceFile(@Url String url, @Header("X-LC-Session") String session,

                                       @JsonQueryParameters.Json @Body SurfaceImgFileBean info);
    //end

    //unit
    @PUT
    Observable<User> updateUnit(@Url String url, @Header("X-LC-Session") String session,
                                @JsonQueryParameters.Json @Body ConvertUnitBean info);

    @GET
    Observable<Result<UpdatedAtBean>> queryUpdateAt(@Url String url, @Query("where") String objID, @Query("keys") String updatedAt);

    @PUT
    Observable<User> updateAllDB(@Url String url, @Header("X-LC-Session") String session,
                                 @JsonQueryParameters.Json @Body ConvertUnitBean user);

    @DELETE("classes/Weight/{userObjId}")
    Observable<EmptyResult> deleteWeight(@Url String url, @Path("userObjId") String userObjId,
                                         @JsonQueryParameters.Json @Query("where") DateFilter filter);

    @GET
    Observable<Result<User>> queryUserName(@Url String url, @Query("where") String username);

    @GET
    Observable<Result<User>> queryMail(@Url String url, @Query("where") String mail);


    @DELETE
    Observable<EmptyResult> deleteWeight(@Url String url, @Header("X-LC-Session") String session);

    @PUT
    Observable<User> updateTotalWalk(@Url String url, @Header("X-LC-Session") String session,
                                     @Body TotalWalkCountBean totalWalkCount);

    @GET
    Observable<Result<BloodPressureInfo>> queryAllPressureInfo(@Url String url, @JsonQueryParameters.Json @Query("where")
            PressureUserFilter where);

    // Achievement Medal
    @PUT
    Observable<User> updateMedal(@Url String url, @Header("X-LC-Session") String session,
                                 @JsonQueryParameters.Json @Body GetMedalBean medal);

    /************************************* FOTA Service *******************************************/

    @Headers({"AppOS: Android", "AppVersion: " + BuildConfig.VERSION_NAME,
            "AppKey: " + FOTAConfig.K9_APP_KEY})
    @GET("api/v2/enableTester/{address}")
    Observable<FOTAResult> postJoinBetaMessage(@Header("Timestamp") String timestamp,
                                               @Header("Sign") String sign,
                                               @Header("Nonce") String nonce,
                                               @Path("address") String address);

    @Headers({"AppOS: Android", "AppVersion: " + BuildConfig.VERSION_NAME,
            "AppKey: " + FOTAConfig.K9_APP_KEY})
    @GET("api/v2/IsTester/{address}")
    Observable<FOTAResult> queryJoinBetaState(@Header("Timestamp") String timestamp,
                                              @Header("Sign") String sign,
                                              @Header("Nonce") String nonce,
                                              @Path("address") String address);

    @Headers({"AppOS: Android", "AppVersion: " + BuildConfig.VERSION_NAME,
            "AppKey: " + FOTAConfig.K9_APP_KEY})
    @GET("api/v2/disableTester/{address}")
    Observable<FOTAResult> postOutBetaMessage(@Header("Timestamp") String timestamp,
                                              @Header("Sign") String sign,
                                              @Header("Nonce") String nonce,
                                              @Path("address") String address);


    /******************************************* CoBand *******************************************/

    // phone register
    @POST("/app/register")
    Observable<RegisterResult> phoneRegister(@Header("Timestamp") String timestamp,
                                             @Header("Sign") String sign,
                                             @Header("Nonce") String nonce,
                                             @Body PhoneRegisterContent content);

    @POST("/app/register")
    Observable<RegisterResult> emailRegister(@Header("Timestamp") String timestamp,
                                             @Header("Sign") String sign,
                                             @Header("Nonce") String nonce,
                                             @Body EmailRegisterContent content);

    @GET("/app/send_sms_code?")
    Observable<VerifyCodeResult> requestVerifyCode(@Header("Timestamp") String timestamp,
                                                   @Header("Sign") String sign,
                                                   @Header("Nonce") String nonce,
                                                   @QueryMap Map<String, String> map);

    // update user info
    @POST("/app/personal_info")
    Observable<UpdateAccountResponse> updateAccountInfo(@Header("Authorization") String authorization,
                                                        @Header("Timestamp") String timestamp,
                                                        @Header("Sign") String sign,
                                                        @Header("Nonce") String nonce,
                                                        @Body UpdateAccountInfo info);

    // log in
    @POST("/app/login")
    Observable<LogInResponse> logIn(@Header("Timestamp") String timestamp,
                                    @Header("Sign") String sign,
                                    @Header("Nonce") String nonce,
                                    @Body LogInBody body);

    @POST("/app/sport_target")
    Observable<UpdateTargetResponse> updateTarget(@Header("Authorization") String authorization,
                                                  @Header("Timestamp") String timestamp,
                                                  @Header("Sign") String sign,
                                                  @Header("Nonce") String nonce,
                                                  @Body UpdateTargetRequest body);

    @Multipart
    @POST("/app/avatar")
    Observable<UpdateAccountResponse> uploadAvatar(@Header("Authorization") String authorization,
                                                   @Header("Timestamp") String timestamp,
                                                   @Header("Sign") String sign,
                                                   @Header("Nonce") String nonce,
                                                   @Part List<MultipartBody.Part> partList);

    @Multipart
    @POST("/app/background")
    Observable<UpdateAccountResponse> uploadBackground(@Header("Authorization") String authorization,
                                                       @Header("Timestamp") String timestamp,
                                                       @Header("Sign") String sign,
                                                       @Header("Nonce") String nonce,
                                                       @Part List<MultipartBody.Part> partList);

    @POST("/app/bind")
    Observable<UploadDeviceResponse> uploadDeviceMessage(@Header("Authorization") String authorization,
                                                         @Header("Timestamp") String timestamp,
                                                         @Header("Sign") String sign,
                                                         @Header("Nonce") String nonce,
                                                         @Body DeviceMessage body);

    @POST("/app/reset_password")
    Observable<ResetPwdResponse> requestResetPwdByPhone(@Header("Timestamp") String timestamp,
                                                        @Header("Sign") String sign,
                                                        @Header("Nonce") String nonce,
                                                        @Body ResetPwdBody body);

    @GET("/app/send_email_reset_pwd")
    Observable<ResetPwdResponse> requestResetPwdByEmail(@Header("Timestamp") String timestamp,
                                                        @Header("Sign") String sign,
                                                        @Header("Nonce") String nonce,
                                                        @Query("email") String email);

    @POST("/app/upload")
    Observable<UploadDataResponse> uploadDayData(@Header("Authorization") String authorization,
                                                @Header("Timestamp") String timestamp,
                                                @Header("Sign") String sign,
                                                @Header("Nonce") String nonce,
                                                @Body UploadDataInfo body);
}
