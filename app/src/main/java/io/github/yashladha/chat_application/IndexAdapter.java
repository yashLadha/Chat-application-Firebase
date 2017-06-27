package io.github.yashladha.chat_application;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.Random;

public class IndexAdapter extends RecyclerView.Adapter<IndexAdapter.MyViewHolder>{

    private ArrayList<Person> persons;
    private FirebaseUser curUser;

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.profile_index, parent, false);
        return new MyViewHolder(itemView);
    }

    public IndexAdapter(ArrayList<Person> persons, FirebaseUser currentUser) {
        this.persons = persons;
        this.curUser = currentUser;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Person temp = persons.get(position);
        final String userUid = temp.getUid();
        holder.profile_name.setText(temp.getUid());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView temp = (TextView) v.findViewById(R.id.tvProfileName);
                Log.d("Adapter: ", temp.getText().toString());
                Intent i = new Intent(v.getContext(), Chat_person.class);
                i.putExtra("User uid", userUid);
                i.putExtra("Current uid", curUser.getUid());
                v.getContext().startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return persons.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView profile_name;

        public MyViewHolder(View itemView) {
            super(itemView);
            profile_name = (TextView) itemView.findViewById(R.id.tvProfileName);

            Random rnd = new Random();
            int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
            int textColor = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
            itemView.setBackgroundColor(color);
            profile_name.setTextColor(textColor);
        }
    }
}
