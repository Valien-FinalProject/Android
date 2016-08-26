package com.theironyard.finalproject.services;

import android.content.Context;
import android.content.SharedPreferences;

import com.theironyard.finalproject.command.ChoreCommand;
import com.theironyard.finalproject.command.RewardCommand;
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
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/**
 * Created by vasantia on 8/19/16.
 */
public class ParentChoreService {
    public static String BASE_URL = "https://vast-fortress-99365.herokuapp.com/";
    public static String PARENT_BASE = BASE_URL + "parent/";
    protected static SharedPreferences chorePrefs;
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

    public static void saveReward(RewardCommand rewardCommand){
        getEditor().putString("rewardCommand", String.valueOf(rewardCommand)).commit();
    }

    public static void saveChore(ChoreCommand choreCommand){
        getEditor().putString("choreCommand", String.valueOf(choreCommand)).commit();
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

    public interface Login{
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

    public interface ParentAPI{
        @GET("children")
        Call<ArrayList<Child>> getChildren(@Header(TOKEN_KEY) String token);

        @GET("child/{id}/current")
        Call<ArrayList<Chore>>getCurrentChores(@Header(TOKEN_KEY) String token, @Path("id") int id);

        @GET("child/{id}/pending")
        Call<ArrayList<Chore>>getPendingChores(@Header(TOKEN_KEY) String token, @Path("id") int id);

        @GET("child/{id}/complete")
        Call<ArrayList<Chore>>getCompletedChores(@Header(TOKEN_KEY) String token, @Path("id") int id);

        @POST("child")
        Call<UserCommand> getChildInfo(@Body UserCommand user);

        @POST("reward")
        Call<RewardCommand> getRewardInfo(@Body RewardCommand reward);

        @GET("rewards")
        Call<ArrayList<Reward>> getParentRewards(@Header(TOKEN_KEY) String token);

        @POST("chore")
        Call<ChoreCommand> getChoreInfo(@Body ChoreCommand choreCommand);

        @GET("child/{id}/wishlist")
        Call<ArrayList<Reward>> getChildWishlist(@Header(TOKEN_KEY) String token, @Path("id") int id);

        @PUT("child/{childId}/wishlist/{rewardId}")
        Call<RewardCommand> updateRewardInfo(@Body RewardCommand rewardCommand,
                                             @Path("childId") int childId, @Path("rewardId") int rewardId);

        @DELETE("reward/{id}")
        Call<ArrayList<Reward>> deleteReward(@Header(TOKEN_KEY) String token, @Path("id") int id);

        @PUT("profile")
        Call<UserCommand> updateParentInfo(@Body UserCommand user);

        @POST("register")
        Call<UserCommand> getParentInfo(@Body UserCommand user);

    }
}

