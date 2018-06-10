package com.ernest.gestionnote;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class DetailNoteActivity extends AppCompatActivity {
    private TextView description;
    private TextView echeance;
    private Button modify;
    private String des ;
    private String color;
    private int order ;
    private int id;
    private String eche ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_note);
        description = (TextView)findViewById(R.id.note_detail_description);
        echeance = (TextView)findViewById(R.id.note_detail_echeance);
        modify = (Button)findViewById(R.id.add_note_button);
        Intent intent = getIntent();

        description.setText(intent.getStringExtra("DESCRIPTION"));
        echeance.setText(intent.getStringExtra("ECHEANCE"));
        des = intent.getStringExtra("DESCRIPTION");
        eche = intent.getStringExtra("ECHEANCE");
        id = intent.getIntExtra ("IDNOTE", -1);
        color = intent.getStringExtra("COLOR");
        order=intent.getIntExtra("ORDER",-1);

        modify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               Intent i =  new Intent(DetailNoteActivity.this, ModifyNoteActivity.class);
                i.putExtra("DESCRIPTION",des);
                i.putExtra("ECHEANCE",eche);
                i.putExtra("IDNOTE",id);
                i.putExtra("COLOR",color);
                i.putExtra("ORDER",order);
                DetailNoteActivity.this.startActivity(i);
            }
        });


    }
}
