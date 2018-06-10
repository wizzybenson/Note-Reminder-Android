package com.ernest.gestionnote;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotesActivity extends AppCompatActivity {
    FloatingActionButton fab;
    RecyclerView recyclerView;
    NoteAdapter noteAdapter;
    List<Note> noteList;
    List<NoteData> noteDataList;
    private RetrofitApiInterface retrofitApiInstance;
    private final int _SUCCESS_ = 200;
    private ItemTouchHelper myItemTouchHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);
        retrofitApiInstance = RetrofitApiClass.getClient();
        fab = (FloatingActionButton)findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(NotesActivity.this,CreateNoteActivity.class));
            }
        });
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String cookie = preferences.getString("reminderCookie", "");
        if(!"".equals(cookie)){
            retrofitApiInstance.list(cookie).enqueue(new Callback<ApiListResponse>() {
                @Override
                public void onResponse(Call<ApiListResponse> call, Response<ApiListResponse> response) {

                    try {
                        if (!(response.errorBody()==null))
                            Log.d("requesterror", response.errorBody().string());

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if (!(response.body() == null)) {

                        ApiListResponse apiResponse = response.body();

                        Log.d("CALLOBJECT", response.body().toString());

                        if (apiResponse.getCode() == _SUCCESS_) {
                            noteDataList=apiResponse.getData();
                            recyclerView = (RecyclerView)findViewById(R.id.recylerView);
                            recyclerView.setHasFixedSize(true);
                            recyclerView.setLayoutManager(new LinearLayoutManager(NotesActivity.this));
                            noteAdapter = new NoteAdapter(NotesActivity.this,noteDataList,retrofitApiInstance);
                            recyclerView.setAdapter(noteAdapter);
                            ItemTouchHelper.Callback callback = new MyItemTouchHelperCallback(noteAdapter);
                            myItemTouchHelper = new ItemTouchHelper(callback);
                            myItemTouchHelper.attachToRecyclerView(recyclerView);
                        }
                    } else  Log.d("responserror", response.toString());

                }

                @Override
                public void onFailure(Call<ApiListResponse> call, Throwable t) {

                }
            });
        }
//        noteList = new ArrayList<>();
//        for(int i = 1;i<5;i++)
//        {
//            noteList.add(new Note("this describes it","#ccc",i,new Date()));
//        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String cookie = preferences.getString("reminderCookie", "");
        if (!"".equals(cookie)) {

            retrofitApiInstance.deconnect(cookie).enqueue(new Callback<ApiGenericGetResponse>() {
                @Override
                public void onResponse(Call<ApiGenericGetResponse> call, Response<ApiGenericGetResponse> response) {

                    try {
                        if (!(response.errorBody() == null))
                            Log.d("requesterror", response.errorBody().string());

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if (!(response.body() == null)) {

                        ApiGenericGetResponse apiResponse = response.body();

                        Log.d("CALLOBJECT", response.body().toString());

                        if (apiResponse.getCode() == _SUCCESS_) {
                            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(NotesActivity.this);
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.putString("reminderCookie","");
                            editor.apply();
                            startActivity(new Intent(NotesActivity.this, LoginActivity.class));
                            finish();
                        } else {
                            Toast.makeText(getBaseContext(), "Logout failed", Toast.LENGTH_LONG).show();

                        }
                    } else Log.d("responserror", response.toString());

                }

                @Override
                public void onFailure(Call<ApiGenericGetResponse> call, Throwable t) {

                }
            });
        }
            return super.onOptionsItemSelected(item);

    }
}
