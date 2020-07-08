package com.duty.dutycalculator.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class CategoryModel implements Serializable {

    @Expose
    @SerializedName("no")
    public String no = "";

    @Expose
    @SerializedName("description")
    public String description = "";

    @Expose
    @SerializedName("cet_code")
    public String cet_code = "";

    @Expose
    @SerializedName("import_duty")
    public String import_duty = "";

    @Expose
    @SerializedName("vat")
    public String vat = "";

    @Override
    public String toString() {
        return description;
    }
}
