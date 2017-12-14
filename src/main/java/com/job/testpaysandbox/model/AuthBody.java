package com.job.testpaysandbox.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by max on 12/14/17.
 */
public class AuthBody {
    @SerializedName("grant_type")
    public final String grantType;

    public AuthBody(String grantType) {
        this.grantType = grantType;
    }
}
