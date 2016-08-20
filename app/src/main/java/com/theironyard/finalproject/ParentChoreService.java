package com.theironyard.finalproject;

import android.content.Context;
import android.content.SharedPreferences;

import com.theironyard.finalproject.command.TokenCommand;
import com.theironyard.finalproject.command.UserCommand;
import com.theironyard.finalproject.representations.Child;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * Created by vasantia on 8/19/16.
 */
public class ParentChoreService {
    public static String BASE_URL = "https://vast-fortress-99365.herokuapp.com/";
    public static String PARENT_BASE = BASE_URL + "parent/";
    private static SharedPreferences chorePrefs;
    private static final String TOKEN_KEY = "token";


    /*****************************************
     * SharedPrefs saving methods
     *****************************************/
    public static void initChorePrefs(Context context){
        chorePrefs = context.getSharedPreferences("chores", Context.MODE_PRIVATE);
    }

    public static void saveToken(String token){
        getEditor().putString(TOKEN_KEY, token).commit();
    }

    private static SharedPreferences.Editor getEditor(){
        return chorePrefs.edit();
    }

    public static String getCurrentToken(){
        return chorePrefs.getString(TOKEN_KEY, null);
    }

    //Login

    public Login getLoginApi(){
        return new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(PARENT_BASE)
                .build().create(Login.class);
    }

    interface Login{
        @POST("token")
        Call<TokenCommand> getParentToken(@Body UserCommand user);
    }

    /************************************
     * Parent Method
     ************************************/
    public static ParentAPI getParentApi() throws Exception {
        String token = ParentChoreService.getCurrentToken();
        if(token == null){
            throw new Exception("Cannot use api without a token");
        }

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request request = chain.request().newBuilder()
                                .addHeader("Authorization", "Bearer " + ParentChoreService.getCurrentToken())
                                .build();
                        return chain.proceed(request);
                    }
                }).build();

        return new Retrofit.Builder()
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(PARENT_BASE)
                .build().create(ParentAPI.class);
    }

    interface ParentAPI{
        @GET("children")
        Call<ArrayList<Child>> getChildren();
    }

}

