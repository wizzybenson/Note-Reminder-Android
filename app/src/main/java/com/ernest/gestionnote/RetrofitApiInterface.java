package com.ernest.gestionnote;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;


public interface RetrofitApiInterface {
    @GET("register/{u}/{p}/{k}")
    Call<ApiGenericGetResponse> register(@Path("u") String user, @Path("p") String password, @Path("k") String key);
    @GET("connect/{u}/{p}")
    Call<ApiGenericGetResponse> connect(@Path("u") String user,@Path("p") String password);
    @GET("deconnect")
    Call<ApiGenericGetResponse> deconnect(@Header("Cookie") String cookie);
    @GET("remove/{id}")
    Call<ApiGenericGetResponse> remove(@Header("Cookie") String cookie,@Path("id") int id);
    @GET("list")
    Call<ApiListResponse> list(@Header("Cookie") String cookie);
    @FormUrlEncoded
    @POST("add")
    Call<ApiGenericPostResponse> add(@Header("Cookie") String cookie,@Field("texte") String texte,@Field("e") String echeance,@Field("o") int priority,@Field("c") String color);
    @FormUrlEncoded
    @POST("change/{id}")
    Call<ApiGenericPostResponse> change(@Header("Cookie") String cookie,@Path("id") int id, @Field("texte") String texte, @Field("e")
            String echeance, @Field("o") int priority, @Field("c") String color);

}
