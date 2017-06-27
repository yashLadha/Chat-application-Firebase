package io.github.yashladha.chat_application;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class Index extends Activity {

    private String TAG = getClass().getSimpleName();

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseDatabase database;
    private FirebaseUser currentUser;

    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index);

        recyclerView = (RecyclerView) findViewById(R.id.index_recycler_view);

        database = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    currentUser = user;
                } else {
                    currentUser = null;
                }
                checkDatabaseModule(user);
            }
        };

        InflateUsersAsync usersAsync = new InflateUsersAsync();
        usersAsync.execute(database);
    }

    public class InflateUsersAsync extends AsyncTask<FirebaseDatabase, Void, Void> {

        @Override
        protected Void doInBackground(FirebaseDatabase... params) {
            final DatabaseReference data = params[0].getReference();
            final ArrayList<Person> person_data = new ArrayList<>();
            data.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    ArrayList<String> users = new ArrayList<String>();
                    for (DataSnapshot user: dataSnapshot.getChildren() ) {
                        String userId = user.getKey();
                        Log.d(TAG, userId);
                        if (!userId.equals(currentUser.getUid()))
                            users.add(userId);
                    }
                    for (int i = 0; i < users.size(); ++i) {
                        Person temp = new Person();
                        if (!users.get(i).equals(currentUser.getUid())) {
                            temp.setUid(users.get(i));
                            person_data.add(temp);
                        }
                   }
                    inflateRecyclerView(person_data);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            return null;
        }
    }

    public void inflateRecyclerView(ArrayList<Person> person_data) {
        IndexAdapter adapter = new IndexAdapter(person_data, currentUser);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
    }

    private void checkDatabaseModule(final FirebaseUser user) {
        final DatabaseReference userDatabase = database.getReference(user.getUid());
        userDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Log.d("Index: ", "Database exists");
                } else {
                    Log.d("Index: ", "Database doesn't exists");
                    HashMap<String, Boolean> temp = new HashMap<String, Boolean>();
                    temp.put("Alive", true);
                    userDatabase.child("status").setValue(temp);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mAuth.removeAuthStateListener(mAuthListener);
    }
}
