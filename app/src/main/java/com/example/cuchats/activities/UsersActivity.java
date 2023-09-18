package com.example.cuchats.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import com.example.cuchats.adapters.UsersAdapter;
import com.example.cuchats.databinding.ActivityUsersBinding;
import com.example.cuchats.listeners.UserListener;
import com.example.cuchats.models.User;
import com.example.cuchats.utilities.Constants;
import com.example.cuchats.utilities.PreferenceManager;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.ArrayList;
import java.util.List;

public class UsersActivity extends AppCompatActivity implements UserListener {
    private ActivityUsersBinding binding;
    private PreferenceManager preferenceManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUsersBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        preferenceManager = new PreferenceManager(getApplicationContext());
        setListeners();
        getUsers();
    }
    private void setListeners(){
        binding.imageBack.setOnClickListener(v->onBackPressed());
    }
        public  void getUsers(){
           loading(true);
            FirebaseFirestore database= FirebaseFirestore.getInstance();
            database.collection(Constants.KEY_COLLECTION_USERS)
                    .get()
                    .addOnCompleteListener(task->{
                        loading(false);
                        String currentUserId = preferenceManager.getString(Constants.KEY_USER_ID);
                        if(task.isSuccessful() && task.getResult()!= null){
                            List<User> users = new ArrayList<>();
                            for(QueryDocumentSnapshot queryDocumentSnapshot:task.getResult()){
                                if(currentUserId.equals(queryDocumentSnapshot.getId())){
                                    continue;
                                }
                                User user = new User();
                                user.name = queryDocumentSnapshot.getString(Constants.KEY_NAME);
                                user.email = queryDocumentSnapshot.getString(Constants.KEY_EMAIL);
                                user.image = queryDocumentSnapshot.getString(Constants.KEY_IMAGE);
                                user.token =queryDocumentSnapshot.getString(Constants.KEY_FCM_TOKEN);
                                user.id = queryDocumentSnapshot.getId();
                                users.add(user);
                            }
                            if(users.size()>0){
                                UsersAdapter usersAdapter = new UsersAdapter(users, this);
                                binding.usersRecyclerView.setAdapter(usersAdapter);
                                binding.usersRecyclerView.setVisibility(View.VISIBLE);
                            }else{
                                showErrorMessage();
                            }
                        }else{
                            showErrorMessage();
                        }
                    });
        }
        public void showErrorMessage(){
         binding.textErrorMessage.setText(String.format("%s","No user available"));
         binding.textErrorMessage.setVisibility(View.VISIBLE);
        }
    private void loading(Boolean isLoading){
        if(isLoading){
            binding.progressBar.setVisibility(View.VISIBLE);
        }else{
            binding.progressBar.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onUserClicked(User user) {
        Intent intent = new Intent(getApplicationContext(),ChatActivity.class);
        intent.putExtra(Constants.KEY_USER ,user);
        startActivity(intent);
        finish();
    }
}