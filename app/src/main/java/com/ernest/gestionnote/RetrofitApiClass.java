package com.ernest.gestionnote;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitApiClass {
    private static Retrofit retrofit = null;
    public static RetrofitApiInterface getClient(){
        if(retrofit == null){
            retrofit = new Retrofit.Builder()
                    .baseUrl("http://daviddurand.info/D228/reminder/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        RetrofitApiInterface retrofitApi = retrofit.create(RetrofitApiInterface.class);
        return retrofitApi;
    }
}
