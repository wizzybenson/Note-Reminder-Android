package com.ernest.gestionnote;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateNoteActivity extends AppCompatActivity {
    EditText echeance;
    EditText description;
    int order = 0;
    Calendar myCalendar;
    Spinner spinner;
    ArrayAdapter adapter;
    Button add_reminder;
    String color;
    private RetrofitApiInterface retrofitApiInstance;
    private final int _SUCCESS_ = 200;
    DatePickerDialog.OnDateSetListener date;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_note);
        myCalendar = Calendar.getInstance();
        retrofitApiInstance = RetrofitApiClass.getClient();

        echeance= (EditText) findViewById(R.id.add_echeance);
        add_reminder = (Button)findViewById(R.id.add_note_button);
        description = (EditText)findViewById(R.id.new_note_text_field);
        //order = (EditText)findViewById(R.id.add_order);
        spinner = (Spinner) findViewById(R.id.colors_spinner);
        date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };
        add_reminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String texte = description.getText().toString();
                String e = echeance.getText().toString();
                int o = order;
                String c = color;
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(CreateNoteActivity.this);
                String cookie = preferences.getString("reminderCookie", "");
                if (!"".equals(cookie)) {
                    retrofitApiInstance.add(cookie, texte, e, o, c).enqueue(new Callback<ApiGenericPostResponse>() {
                        @Override
                        public void onResponse(Call<ApiGenericPostResponse> call, Response<ApiGenericPostResponse> response) {

                            try {
                                if (!(response.errorBody() == null))
                                    Log.d("requesterror", response.errorBody().string());

                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            if (!(response.body() == null)) {

                                ApiGenericPostResponse apiResponse = response.body();

                                Log.d("CALLOBJECT", response.body().toString());

                                if (apiResponse.getCode() == _SUCCESS_) {
                                    Toast.makeText(getBaseContext(), "Note enregistré", Toast.LENGTH_LONG).show();
                                }
                            } else Log.d("responserror", response.toString());

                        }

                        @Override
                        public void onFailure(Call<ApiGenericPostResponse> call, Throwable t) {

                        }
                    });
                }
            }


        });

        echeance.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(CreateNoteActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        //Log.d("COLORS", (R.array.colors_array)

        // Create an ArrayAdapter using the string array and a default spinner layout
                Resources res = getResources();
                final String[] colorArray = res.getStringArray(R.array.colors_array);
                adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item,colorArray );
        //        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
        //                R.array.colors_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                adapterView.getItemAtPosition(i);
                if (i!=0){
                    spinner.setBackgroundColor(Color.parseColor(colorArray[i]));
                    color = colorArray[i];
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void updateLabel() {
         //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        echeance.setText(sdf.format(myCalendar.getTime()));
    }
}
