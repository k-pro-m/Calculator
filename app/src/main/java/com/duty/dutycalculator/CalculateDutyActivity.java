package com.duty.dutycalculator;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.constraint.solver.widgets.WidgetContainer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.duty.dutycalculator.api.ApiService;
import com.duty.dutycalculator.api.CategoryModel;
import com.duty.dutycalculator.api.CategoryResponseModel;
import com.duty.dutycalculator.mMySQL.Downloader;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CalculateDutyActivity extends AppCompatActivity {


    CategoryResponseModel mCategoryResponseModel = null;
    Spinner spHscode;
    AutoCompleteTextView autoCompleteTextView;
    TextView m_hsCode_textView ;
    TextView m_description_textView ;
    TextView m_hsImportDuty_textview ;
    TextView m_hsVAT_textView ;
    String spImportDuty = "0";
    String spVat = "0";
    String spDescription = "";
    String hs_code = "";
    MyApplication myApplication = MyApplication.getInstance();
    SharedPreferences pref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculate_duty);
         autoCompleteTextView = (AutoCompleteTextView) findViewById(R.id.acHsCode);
        spHscode = (Spinner) findViewById(R.id.spHscode);
        m_hsCode_textView = (TextView) findViewById(R.id.HSCode);
        m_description_textView = (TextView) findViewById(R.id.hsDescription);
        m_hsImportDuty_textview = (TextView) findViewById(R.id.hsImportDuty);
        m_hsVAT_textView = (TextView) findViewById(R.id.hsVAT);
        apiCallGetcategory();

        myApplication.setLoginLogout(CalculateDutyActivity.this, true);
    }

    public void onLogoutClick(View v) {
        myApplication.setLoginLogout(CalculateDutyActivity.this, false);
        startActivity(new Intent(CalculateDutyActivity.this, LoginActivity.class));
    }
    public void onButtonClick(View v) {


        EditText etFOB = (EditText) findViewById(R.id.etFOB);
        EditText etFreight = (EditText) findViewById(R.id.etFreight);
        TextView tvCalculateDuty = (TextView) findViewById(R.id.tvCalculateDuty);


        InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

        if(myApplication.isEmptyText(etFOB.getText().toString())){
            Toast.makeText(CalculateDutyActivity.this, "Please enter FOB value", Toast.LENGTH_SHORT).show();
            return;
        } else if(myApplication.isEmptyText(etFreight.getText().toString())){
            Toast.makeText(CalculateDutyActivity.this, "Please enter Freight value", Toast.LENGTH_SHORT).show();
            return;
        }  else if(myApplication.isEmptyText(autoCompleteTextView.getText().toString())){
            Toast.makeText(CalculateDutyActivity.this, "Please select HS code", Toast.LENGTH_SHORT).show();
            return;
        } else if(spImportDuty.equalsIgnoreCase("0")){
            Toast.makeText(CalculateDutyActivity.this, "Please select HS code", Toast.LENGTH_SHORT).show();
            return;
        }

        float fFOB = (Integer.parseInt(etFOB.getText().toString())* 360 );
        float fFreight = (Integer.parseInt(etFreight.getText().toString()) * 360);

        double insurance = 0.015 * fFOB;
        double cif = fFOB + insurance + fFreight;
        float fImportDuty = (float)(Integer.parseInt(spImportDuty)) / 100;
        double surfaceDuty =  (0.05*cif);//0.05 * cif;
        double surcharge = 0.07 * surfaceDuty;
        double etls = 0.005 * cif;
        double ciss = 0.01 * fFOB;
        float fVAT = (float)(Integer.parseInt(spVat)) / 100;
        double vat = 0.05 * (surfaceDuty + surcharge + etls + ciss + cif);
        double totalDuty = (surfaceDuty+ surcharge + etls + ciss + vat);


       /* String details = "surfaceDuty = "+ surfaceDuty +
                "\nsurcharge = " +surcharge +
                "\nciss = " +ciss +
                "\nvat = " +vat +
                "\ntotalDuty = " +totalDuty ;


        tvCalculateDuty.setText(details);*/
       tvCalculateDuty.setText(String.format("%.2f", totalDuty));
    }

    private void apiCallGetcategory() {
        // create an instance of the ApiService
        ApiService apiService = MyApplication.getInstance().getRetrofit().create(ApiService.class);
        apiService.getCategory("1").
                enqueue(new Callback<CategoryResponseModel>() {
                    @Override
                    public void onResponse(Call<CategoryResponseModel> call, Response<CategoryResponseModel> response) {
                        Log.i("==","==onResponse==response"+response.message());

                        mCategoryResponseModel = response.body();
                        if(mCategoryResponseModel !=null){
                            Log.i("onResponse","=CategoryResponseModel="+mCategoryResponseModel.listCategoryModel.size());
                            initializeUI();
                        } else {
                            Toast.makeText(CalculateDutyActivity.this, "Something went wrong please try again!!", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<CategoryResponseModel> call, Throwable t) {
                        Log.i("==","==onFailure==response"+t);
                        Toast.makeText(CalculateDutyActivity.this, "Something went wrong please try again!!", Toast.LENGTH_SHORT).show();
                    }
                });

    }

    ArrayList<CategoryModel> listspHscode = new ArrayList<>();
    private void initializeUI() {
        if(mCategoryResponseModel.listCategoryModel!=null)
            listspHscode = mCategoryResponseModel.listCategoryModel;

        /*ArrayAdapter<CategoryModel> adapter =
                new ArrayAdapter<CategoryModel>(getApplicationContext(),  android.R.layout.simple_spinner_dropdown_item, listspHscode);
        adapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item);

        spHscode.setAdapter(adapter);*/

        /*spHscode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });*/

        AutoCompleteAdapter2 adapter2 = new AutoCompleteAdapter2(CalculateDutyActivity.this, android.R.layout.simple_dropdown_item_1line, android.R.id.text1, listspHscode);

        autoCompleteTextView.setAdapter(adapter2);

        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick (AdapterView<?> parent, View view, int position, long id) {
                Log.i("==","=description="+((CategoryModel)parent.getAdapter().getItem(position)).description);
                spImportDuty = ((CategoryModel)parent.getAdapter().getItem(position)).import_duty;
                spVat = ((CategoryModel)parent.getAdapter().getItem(position)).vat;
                hs_code = ((CategoryModel)parent.getAdapter().getItem(position)).cet_code;
                spDescription = ((CategoryModel)parent.getAdapter().getItem(position)).description;

                m_hsCode_textView.setText("CET_CODE : " + hs_code);
                m_description_textView.setText("DESCRIPTION : " + spDescription);
                m_hsImportDuty_textview.setText("Import Duty : " + spImportDuty);
                m_hsVAT_textView.setText("VAT : " + spVat);
            }
        });
    }


    public class AutoCompleteAdapter2 extends ArrayAdapter<CategoryModel> implements Filterable {

        private ArrayList<CategoryModel> fullList;
        private ArrayList<CategoryModel> mOriginalValues;
        private ArrayFilter mFilter;

        public AutoCompleteAdapter2(Context context, int resource, int textViewResourceId, List<CategoryModel> objects) {

            super(context, resource, textViewResourceId, objects);
            fullList = (ArrayList<CategoryModel>) objects;
            mOriginalValues = new ArrayList<CategoryModel>(fullList);

        }

        @Override
        public int getCount() {
            return fullList.size();
        }

        @Override
        public CategoryModel getItem(int position) {
            return fullList.get(position);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            return super.getView(position, convertView, parent);
        }

        @Override
        public Filter getFilter() {
            if (mFilter == null) {
                mFilter = new ArrayFilter();
            }
            return mFilter;
        }


        private class ArrayFilter extends Filter {
            private Object lock;

            @Override
            protected FilterResults performFiltering(CharSequence prefix) {
                FilterResults results = new FilterResults();

                if (mOriginalValues == null) {
                    synchronized (lock) {
                        mOriginalValues = new ArrayList<CategoryModel>(fullList);
                    }
                }

                if (prefix == null || prefix.length() == 0) {
                    synchronized (lock) {
                        ArrayList<CategoryModel> list = new ArrayList<CategoryModel>(mOriginalValues);
                        results.values = list;
                        results.count = list.size();
                    }
                } else {
                    final String prefixString = prefix.toString().toLowerCase();

                    ArrayList<CategoryModel> values = mOriginalValues;
                    int count = values.size();

                    ArrayList<CategoryModel> newValues = new ArrayList<CategoryModel>(count);

                    for (int i = 0; i < count; i++) {
                        CategoryModel item = values.get(i);
                        if (item.toString().toLowerCase().contains(prefixString)) {
                            newValues.add(item);
                        }

                    }

                    results.values = newValues;
                    results.count = newValues.size();
                }

                return results;
            }

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {

                if(results.values!=null){
                    fullList = (ArrayList<CategoryModel>) results.values;
                }else{
                    fullList = new ArrayList<CategoryModel>();
                }
                if (results.count > 0) {
                    notifyDataSetChanged();
                } else {
                    notifyDataSetInvalidated();
                }
            }
        }
    }


    boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }
}
