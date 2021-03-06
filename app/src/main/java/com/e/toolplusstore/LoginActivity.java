package com.e.toolplusstore;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.firebase.ui.auth.AuthUI;


import java.util.Arrays;
import java.util.List;

public class LoginActivity extends AppCompatActivity {
    private static int AUTH_REQUEST_CODE = 786;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener listener;
    private List<AuthUI.IdpConfig> providers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    @Override
    protected void onStart() {
        super.onStart();
        SignInUser();
        firebaseAuth.addAuthStateListener(listener);
    }
    @Override
    protected void onStop() {
        super.onStop();
        if (listener != null)
            firebaseAuth.removeAuthStateListener(listener);

    }
    private void SignInUser() {
        providers = Arrays.asList(
                new AuthUI.IdpConfig.PhoneBuilder().build(),
                new AuthUI.IdpConfig.EmailBuilder().build()
        );
        firebaseAuth = FirebaseAuth.getInstance();
        listener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    Toast.makeText(LoginActivity.this, "user already logged in with uid" + user.getUid(), Toast.LENGTH_SHORT).show();
                } else {
                    startActivityForResult(AuthUI.getInstance()
                            .createSignInIntentBuilder()
                            .setLogo(R.drawable.logo_white)
                            .setTheme(R.style.LoginTheme)
                            .setAvailableProviders(providers)
                            .build(),AUTH_REQUEST_CODE);

                }
            }
        };
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==AUTH_REQUEST_CODE){
            if(resultCode == RESULT_OK){
                sendUserToHomeScreen();
            }
        }
    }

    private void sendUserToHomeScreen() {
        Intent in = new Intent(LoginActivity.this, HomeActivity.class);
        startActivity(in);
        finish();
    }
}