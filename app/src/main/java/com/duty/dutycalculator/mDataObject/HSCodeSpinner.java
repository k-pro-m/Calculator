package com.duty.dutycalculator.mDataObject;

public class HSCodeSpinner
{
        /*
        INSTANCE FIELDS
         */
        private int id;
        private String CET_Code;
        private String Description;
        private int Import_Duty;
        private int VAT;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCET_Code() {
        return CET_Code;
    }

    public void setCET_Code(String CET_Code) {
        this.CET_Code = CET_Code;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public int getImport_Duty() {
        return Import_Duty;
    }

    public void setImport_Duty(int import_Duty) {
        Import_Duty = import_Duty;
    }

    public int getVAT() {
        return VAT;
    }

    public void setVAT(int VAT) {
        this.VAT = VAT;
    }
/*

//
//        /*
//        TOSTRING
//         */
//        @Override
//        public String toString() {
//            return name;
//        }
//    }

}

