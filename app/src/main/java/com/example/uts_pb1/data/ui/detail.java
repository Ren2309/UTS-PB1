package com.example.uts_pb1.data.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.uts_pb.R;
import com.example.uts_pb1.data.response.GithubUser;
import com.example.uts_pb1.data.retrofit.ApiConfig;
import com.example.uts_pb1.data.retrofit.ApiService;
import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class detail extends AppCompatActivity {

    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.example.uts_pb.R.layout.detail);

        progressBar = findViewById(R.id.progressBar);

        Bundle extras = getIntent().getExtras();
        if (extras != null){
            String username = extras.getString("username");
            ApiService apiService = ApiConfig.getApiService();
            Call<GithubUser> userCall = apiService.getUser(username);

            TextView textView = findViewById(R.id.nama);
            TextView textView2 = findViewById(R.id.username);
            TextView textView3 = findViewById(R.id.bio);
            ImageView imageView = findViewById(R.id.gambar);

            showLoading(true);
            userCall.enqueue(new Callback<GithubUser>() {
                @Override
                public void onResponse(Call<GithubUser> call, Response<GithubUser> response) {
                    if (response.isSuccessful()){
                        showLoading(false);
                        GithubUser user = response.body();
                        if (user != null){
                            String name = "Name: " + user.getName();
                            String usernames = "Username: " + user.getUsername();
                            String bio = "Bio: " + user.getBio();
                            String gambar = user.getAvatarUrl();

                            textView.setText(name);
                            textView2.setText(usernames);
                            textView3.setText(bio);
                            Picasso.get().load(gambar).into(imageView);
                        }else {
                            Toast.makeText(detail.this, "Failed to get user data", Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onFailure(Call<GithubUser> call, Throwable t) {
                    Toast.makeText(detail.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void showLoading(Boolean isLoading) {
        if (isLoading) {
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.GONE);
        }
    }

}