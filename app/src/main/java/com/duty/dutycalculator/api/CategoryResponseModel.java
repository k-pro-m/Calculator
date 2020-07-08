package com.duty.dutycalculator.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class CategoryResponseModel implements Serializable {

    @Expose
    @SerializedName("msg")
    public String msg;
    @Expose
    @SerializedName("data")
    public ArrayList<CategoryModel> listCategoryModel;
}
