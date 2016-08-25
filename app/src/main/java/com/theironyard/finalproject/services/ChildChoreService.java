package com.theironyard.finalproject.services;

import android.content.Context;
import android.content.SharedPreferences;

import com.theironyard.finalproject.command.TokenCommand;
import com.theironyard.finalproject.command.UserCommand;
import com.theironyard.finalproject.representations.Child;
import com.theironyard.finalproject.representations.Chore;
import com.theironyard.finalproject.representations.Reward;

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
import retrofit2.http.Path;

/**
 * Created by vasantia on 8/19/16.
 */
public class ChildChoreService {
    public static String BASE_URL = "https://vast-fortress-99365.herokuapp.com/";
    public static String CHILD_BASE = BASE_URL + "child/";
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

    public static void saveUser(UserCommand userCommand){
        getEditor().putString("usercommand", String.valueOf(userCommand)).commit();
    }

    private static SharedPreferences.Editor getEditor(){
        return chorePrefs.edit();
    }

    public static String getCurrentToken(){
        return chorePrefs.getString(TOKEN_KEY, null);
    }

    /************************************
     * Login
     ************************************/

    public Login getLoginApi(){
        return new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(CHILD_BASE)
                .build().create(Login.class);
    }

    public interface Login{
        @POST("token")
        Call<TokenCommand> getChildToken(@Body UserCommand user);
    }

    /************************************
     * Child Method
     ************************************/
    public static ChildAPI getChildApi() throws Exception {
        String token = ChildChoreService.getCurrentToken();
        if(token == null){
            throw new Exception("Cannot use api without a token");
        }

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request request = chain.request().newBuilder()
                                .addHeader("Authorization", "Bearer " + ChildChoreService.getCurrentToken())
                                .build();
                        return chain.proceed(request);
                    }
                }).build();

        return new Retrofit.Builder()
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(CHILD_BASE)
                .build().create(ChildAPI.class);
    }

    public interface ChildAPI{
        @GET("chores")
        Call<ArrayList<Chore>> getChores();

        @GET("points")
        Call<Integer> getPoints();

        @GET("rewards")
        Call<ArrayList<Reward>> getRewards();

        @GET("deduct")
        Call<Child> deductPoints(@Body int id);
    }


}

