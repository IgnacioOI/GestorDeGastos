package com.example.gestorgastos;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.os.Bundle;

import com.airbnb.lottie.LottieAnimationView;

public class MainActivityBienvenida extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_bienvenida);

        LottieAnimationView animationView = findViewById(R.id.animationView);
        animationView.addAnimatorListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                // Redirigir a MainActivity después de que la animación haya terminado
                Intent intent = new Intent(MainActivityBienvenida.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

}