package com.example.android.animallog;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;


public class MainActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener
{
    private static final String TAG = "LoginActivity";
    private static final int RC_SIGN_IN = 1001;
    private GoogleApiClient mGoogleApiClient;
    private FirebaseAuth mFirebaseAuth;
    boolean signedIn = false;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mFirebaseAuth = FirebaseAuth.getInstance();
        FirebaseAuth.AuthStateListener mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    //user is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    //user is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
            }
        };

        SignInButton mGoogleSignInButton = findViewById(R.id.sign_in_button);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();



        mGoogleSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });

    }

    private void signIn()
    {
        mFirebaseAuth.signOut();
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        mGoogleApiClient.clearDefaultAccountAndReconnect();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }



    private void firebaseAuthWithGoogle(final GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGooogle:" + acct.getId());
        final AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        if (mFirebaseAuth.getCurrentUser() != null)
        {
            mFirebaseAuth.getCurrentUser().linkWithCredential(credential).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                @Override
                public void onSuccess(AuthResult authResult)
                {
                    newActivity();
                    signedIn = true;
                }
            })

                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            e.printStackTrace();
                            Log.e(TAG, "onFailure: " + e.getMessage());
                            mFirebaseAuth.signInWithCredential(credential).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                @Override
                                public void onSuccess(AuthResult authResult)
                                {
                                }
                            });
                        }
                    });
        }
        else
        {
            mFirebaseAuth.signInWithCredential(credential)
                    .addOnCompleteListener(MainActivity.this, task -> {
                                if (task.isSuccessful() && task.isComplete())
                                {
                                    newActivity();
                                }
                                else {
                                }
                            });
        }
    }
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        // An unresolvable error has occurred and Google APIs (including Sign-In) will not
        // be available.
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
        Toast.makeText(this, "Google Play Services error.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN)
        {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess())
            {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);

            }
            else
                {
                Log.e(TAG, "Google Sign In failed.");
            }
        }
    }

    private void newActivity()
    {
        String name = mFirebaseAuth.getCurrentUser().getDisplayName();
        String email = mFirebaseAuth.getCurrentUser().getEmail();
        String photo = mFirebaseAuth.getCurrentUser().getPhotoUrl().toString();
        Intent profileActivity = new Intent(MainActivity.this, CatalogActivity.class);
//        profileActivity.putExtra("name", name);
//        profileActivity.putExtra("email", email);
//        profileActivity.putExtra("photo", photo);
        startActivity(profileActivity);
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        if(signedIn)
        {
            Intent profileActivity = new Intent(MainActivity.this, CatalogActivity.class);
//        profileActivity.putExtra("name", name);
//        profileActivity.putExtra("email", email);
//        profileActivity.putExtra("photo", photo);
            startActivity(profileActivity);
        }
    }

    @Override
    protected void onPause()
    {
        super.onPause();
    }
}