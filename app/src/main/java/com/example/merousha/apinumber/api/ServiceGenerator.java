package com.example.merousha.apinumber.api;

import com.example.merousha.apinumber.service.GitHubClient;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by merousha on 2018/06/27.
 */

public class ServiceGenerator {

    static Retrofit.Builder builder = new Retrofit.Builder()
            .baseUrl("https://api.github.com/")
            .addConverterFactory(GsonConverterFactory.create());

    static Retrofit retrofit = builder.build();

    public static GitHubClient createService(){

        return retrofit.create(GitHubClient.class);
    }



}
