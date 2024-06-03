package com.example.gestorgastos;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import com.example.gestorgastos.databinding.ActivityMain2InicioBinding;

public class Main2ActivityInicio extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private String nombreUsu;
    private int idUsuario;
    private ActivityMain2InicioBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMain2InicioBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarMain2ActivityInicio.toolbar);

        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;

        View headerView = navigationView.getHeaderView(0);
        TextView nombreCuentaNavHeader = headerView.findViewById(R.id.nombreCuentaNavHeader);
        Intent intent = getIntent();
        nombreUsu = intent.getStringExtra("usuarioNombre");
        idUsuario = intent.getIntExtra("idUsuario", 0);
        nombreCuentaNavHeader.setText(nombreUsu);

        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main2_activity_inicio);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.itemMenuCuenta) {
                    Intent intent = new Intent(Main2ActivityInicio.this, MainActivityCuentas.class);
                    intent.putExtra("idUsuario", idUsuario);
                    intent.putExtra("nombreUsuario", nombreUsu);
                    startActivity(intent);
                    finish();
                    return true;
                }
                return NavigationUI.onNavDestinationSelected(item, navController) || Main2ActivityInicio.super.onOptionsItemSelected(item);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main2_activity_inicio, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main2_activity_inicio);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration) || super.onSupportNavigateUp();
    }
}
