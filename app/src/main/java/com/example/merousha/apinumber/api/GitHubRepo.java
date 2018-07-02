package com.example.merousha.apinumber.api;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by merousha on 2018/06/27.
 */

public class GitHubRepo extends RealmObject{

    @PrimaryKey
    @SerializedName("name")
    private String myName;


    public String getMyName() {
        return myName;
    }

    public void setMyName(String name) {
        this.myName = name;
    }
}



