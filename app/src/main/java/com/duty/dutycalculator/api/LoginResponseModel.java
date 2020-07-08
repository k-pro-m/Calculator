package com.duty.dutycalculator.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class LoginResponseModel implements Serializable {

    @Expose
    @SerializedName("msg")
    public String msg = "";

    @Expose
    @SerializedName("userID")
    public String userID = "";
}
