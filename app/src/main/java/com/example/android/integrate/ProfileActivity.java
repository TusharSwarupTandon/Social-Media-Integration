package com.example.android.integrate;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.facebook.login.LoginManager;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity
{

    Button logoutBtn;
    TextView userName,userEmail;
    CircleImageView roundProfileImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_activity);

        logoutBtn = findViewById(R.id.logoutBtn);
        userName = findViewById(R.id.name);
        userEmail = findViewById(R.id.email);
        roundProfileImage = findViewById(R.id.profile_image);

        userName.setText(getIntent().getStringExtra("name"));
        userEmail.setText(getIntent().getStringExtra("email"));
        try
        {
            Uri photo = Uri.parse(getIntent().getStringExtra("photo")+"?type=large&access_token=546951886293366|hf8J3gbZtdcQr1rYoWZ33GcwYKQ&");
            Log.e("photo",photo+"");
            Glide.with(this).load(photo).into(roundProfileImage);
        }
        catch (NullPointerException e)
        {
            Toast.makeText(getApplicationContext(),"image not found",Toast.LENGTH_LONG).show();
        }

        logoutBtn.setOnClickListener(view -> {

            LoginManager.getInstance().logOut();
            gotoMainActivity();
            finish();
        });
    }


    private void gotoMainActivity()
    {
        Intent intent=new Intent(this, MainActivity.class);
        startActivity(intent);
    }

}
