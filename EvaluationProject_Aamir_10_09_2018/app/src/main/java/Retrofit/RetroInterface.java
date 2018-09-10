package Retrofit;

import java.util.List;

import Model.DataModel;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface RetroInterface {
    /*@GET("users/android/repos")
    Call<DataModel> getRepoList();*/

    @GET("users/android/repos")
    Call<List<DataModel>> getRepoList();
}