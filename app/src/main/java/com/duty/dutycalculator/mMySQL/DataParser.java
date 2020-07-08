package com.duty.dutycalculator.mMySQL;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.duty.dutycalculator.mDataObject.HSCodeSpinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class DataParser extends AsyncTask<Void, Void, Integer> {

    Context c;
    Spinner sp;
    String jsonData;
    ProgressDialog pd;
    ArrayList<String> hscodes = new ArrayList<>();

    public DataParser(Context c, Spinner sp, String jsonData) {
        this.c = c;
        this.sp = sp;
        this.jsonData = jsonData;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        pd = new ProgressDialog(c);
        pd.setTitle("Parse Status");
        pd.setMessage("Parsing... Please wait!");
        pd.show();
    }

    int id;
    String description;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    protected Integer doInBackground(Void... voids) {
        return this.parseData();
    }

    @Override
    protected void onPostExecute(Integer result) {
        super.onPostExecute(result);

        pd.dismiss();

        if (result==0)
        {
            Toast.makeText(c, "Unable to Parse", Toast.LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText(c, "Parsed Successfully", Toast.LENGTH_SHORT).show();

            //BIND
            ArrayAdapter adapter = new ArrayAdapter(c, android.R.layout.simple_list_item_1, hscodes);
            sp.setAdapter(adapter);
        }
    }

    private int parseData()
    {
        try {
            JSONArray ja = new JSONArray(jsonData);
            JSONObject jo = null;

            hscodes.clear();
            HSCodeSpinner h = null;

            for (int i=0; i<ja.length(); i++)
            {
                jo=ja.getJSONObject(i);

                int id=jo.getInt("id");
                String description = jo.getString("Description");

                h=new HSCodeSpinner();

                h.setId(id);
                h.setDescription(description);

                hscodes.add(description);
            }

            return  1;


        } catch (JSONException e) {
            e.printStackTrace();
        }

        return 0;
    }
}
