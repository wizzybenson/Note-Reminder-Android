package com.ernest.gestionnote;

import android.content.Context;
import com.ernest.gestionnote.R;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteViewHolder> implements ListAdapterInterface{

    private Context mContext;
    private List<NoteData> noteList;
    private RetrofitApiInterface retrofitApiInstance;
    private final int _SUCCESS_ = 200;
    private int position;

    public NoteAdapter(Context mContext, List<NoteData> noteList,RetrofitApiInterface retrofit) {
        this.mContext = mContext;
        this.noteList = noteList;
        this.retrofitApiInstance = retrofit;
    }

    @Override
    public NoteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.note_item,null);
        return new NoteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(NoteViewHolder holder, int position) {
        NoteData note = noteList.get(position);
        holder.card.setCardBackgroundColor(Color.parseColor(note.getCouleur()));
        holder.descriptionText.setText(note.getTache());
        holder.echeanceText.setText(note.getEcheance());
        //holder.priorityText.setText(String.valueOf(note.getPriority()));

    }

    @Override
    public int getItemCount() {
        return noteList.size();
    }

    @Override
    public void onItemDismiss(int position) {
        ///start
        this.position = position;
        NoteData note = noteList.get(position);
        int id = Integer.parseInt(note.getId());
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        String cookie = preferences.getString("reminderCookie", "");
        if (!"".equals(cookie)) {
            retrofitApiInstance.remove(cookie,id ).enqueue(new Callback<ApiGenericGetResponse>() {
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
                            noteList.remove(NoteAdapter.this.position);
                            notifyItemRemoved(NoteAdapter.this.position);
                            Toast.makeText(mContext, "Note removed", Toast.LENGTH_LONG).show();
                        }
                    } else Log.d("responserror", response.toString());

                }

                @Override
                public void onFailure(Call<ApiGenericGetResponse> call, Throwable t) {

                }
            });
        }
    }




        ///end



    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(noteList, i, i + 1);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(noteList, i, i - 1);
            }
        }


        NoteData noteTo = noteList.get(toPosition);
        NoteData noteFrom = noteList.get(fromPosition);
        int idTo = Integer.parseInt(noteTo.getId());
        int idFrom = Integer.parseInt(noteFrom.getId());
        int orderTo = toPosition;
        int orderFrom = fromPosition;
        String texteFrom = noteFrom.getTache();
        String eFrom = noteFrom.getEcheance();
        String cFrom = noteFrom.getCouleur();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        String cookie = preferences.getString("reminderCookie", "");
        if (!"".equals(cookie)) {
            retrofitApiInstance.change(cookie,idFrom, texteFrom, eFrom, orderTo, cFrom).enqueue(new Callback<ApiGenericPostResponse>() {
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

                            Toast.makeText(mContext, "order modifié", Toast.LENGTH_LONG).show();
                        }
                    } else Log.d("responserror", response.toString());

                }

                @Override
                public void onFailure(Call<ApiGenericPostResponse> call, Throwable t) {

                }
            });


        }
        notifyItemMoved(fromPosition, toPosition);
        return true;
    }

    class NoteViewHolder extends RecyclerView.ViewHolder implements ListViewHolderInterface, View.OnClickListener{

        TextView descriptionText,echeanceText,priorityText;
        CardView card;
        public NoteViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            card = itemView.findViewById(R.id.card);
            descriptionText = itemView.findViewById(R.id.description);
            echeanceText = itemView.findViewById(R.id.echeance);
            priorityText = itemView.findViewById(R.id.priority);

        }


        @Override
        public void onClick(View view) {
            NoteData note = noteList.get(this.getAdapterPosition());
            Toast.makeText(mContext, "yes", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(mContext,DetailNoteActivity.class);
            String des = note.getTache();
            String color = note.getCouleur();
            int order = Integer.parseInt(note.getOrdre());
            int id = Integer.parseInt(note.getId());
            String echeance = note.getEcheance();
            //String echeance = new SimpleDateFormat("dd/MM/yyy").format(note.getEcheance());
            intent.putExtra("DESCRIPTION",des);
            intent.putExtra("ECHEANCE",echeance);
            intent.putExtra("IDNOTE",id);
            intent.putExtra("COLOR",color);
            intent.putExtra("ORDER",order);
            mContext.startActivity(intent);

        }

        @Override
        public void onItemSelected() {
            itemView.setBackgroundColor(Color.LTGRAY);
        }

        @Override
        public void onItemClear() {
            itemView.setBackgroundColor(0);
        }
    }
}
