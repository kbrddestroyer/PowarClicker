package com.alexgames.powarclicker;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.content.DialogInterface;
import android.content.Context;
import android.widget.RadioButton;
import android.widget.Switch;
import android.widget.ToggleButton;

public class SettingActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    Context context = this;
    public long coins = 0L;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Сообщить о багах: alexgamesby@gmail.com", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        SharedPreferences coins_buffer = getSharedPreferences("cbuffer", 0);
        coins = coins_buffer.getLong("coins", 0L);
        Button check = findViewById(R.id.get_coins_btn);
        check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final LayoutInflater inflater = LayoutInflater.from(context);
                AlertDialog.Builder builder = new AlertDialog.Builder(SettingActivity.this);
                final View linearlayout = inflater.inflate(R.layout.devinput_dialog_layout, null);
                builder.setView(linearlayout);

                final EditText et = (EditText)linearlayout.findViewById(R.id.password);
                final EditText ed = (EditText)linearlayout.findViewById(R.id.value);
                builder.setTitle("Внимание!");
                builder.setMessage("Введите данные:");
                builder.setNegativeButton("Отмена",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                builder.setPositiveButton("Ок", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String pass = et.getText().toString();
                        String coins = ed.getText().toString();
                        getCoins(pass, coins);
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });

        SharedPreferences setting_buffer = getSharedPreferences("settings", 0);
        boolean isChecked = setting_buffer.getBoolean("sounds", true);
        Switch sw = findViewById(R.id.is_sound);
        sw.setChecked(isChecked);
        sw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferences setting_buffer = getSharedPreferences("settings", 0);
                SharedPreferences.Editor editor = setting_buffer.edit();
                editor.putBoolean("sounds", isChecked);
                editor.commit();
                editor.commit();
            }
        });

    }

    void getCoins(String password, String coins_value)
    {
        if(password.equals("alexgames19"))
        {
            SharedPreferences coins_buffer = getSharedPreferences("cbuffer", 0);
            long coin_val = coins_buffer.getLong("coins", 0L);
            coins = coin_val + Long.parseLong(coins_value);
            SharedPreferences.Editor coins_buffer_editor = coins_buffer.edit();
            coins_buffer_editor.putLong("coins", coins);
            coins_buffer_editor.commit();
        }
        else
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(SettingActivity.this);
            builder.setTitle("Внимание!")
                    .setMessage("Неверный пароль!")
                    .setCancelable(false)
                    .setNegativeButton("Cancel",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });
            AlertDialog alert = builder.create();
            alert.show();
        }
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.setting, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            SharedPreferences coins_buffer = getSharedPreferences("cbuffer", 0);
            SharedPreferences.Editor coins_buffer_editor = coins_buffer.edit();
            coins_buffer_editor.putLong("coins", coins);
            coins_buffer_editor.commit();
            coins_buffer_editor.commit();
            Intent intent = new Intent(SettingActivity.this, InfoActivity.class);
            startActivity(intent);

            SettingActivity.this.finish();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_bank) {
            SharedPreferences coins_buffer = getSharedPreferences("cbuffer", 0);
            SharedPreferences.Editor coins_buffer_editor = coins_buffer.edit();
            coins_buffer_editor.putLong("coins", coins);
            coins_buffer_editor.commit();
            coins_buffer_editor.commit();
            Intent intent = new Intent(SettingActivity.this, MainActivity.class);
            startActivity(intent);

            SettingActivity.this.finish();
        } else if (id == R.id.nav_change) {
            SharedPreferences coins_buffer = getSharedPreferences("cbuffer", 0);
            SharedPreferences.Editor coins_buffer_editor = coins_buffer.edit();
            coins_buffer_editor.putLong("coins", coins);
            coins_buffer_editor.commit();
            coins_buffer_editor.commit();
            Intent intent = new Intent(SettingActivity.this, ChangeActivity.class);
            startActivity(intent);

            SettingActivity.this.finish();
        } else if (id == R.id.nav_payment) {
            SharedPreferences coins_buffer = getSharedPreferences("cbuffer", 0);
            SharedPreferences.Editor coins_buffer_editor = coins_buffer.edit();
            coins_buffer_editor.putLong("coins", coins);
            coins_buffer_editor.commit();
            coins_buffer_editor.commit();
            Intent intent = new Intent(SettingActivity.this, PaymentActivity.class);
            startActivity(intent);

            SettingActivity.this.finish();
        } else if (id == R.id.nav_shop) {
            SharedPreferences coins_buffer = getSharedPreferences("cbuffer", 0);
            SharedPreferences.Editor coins_buffer_editor = coins_buffer.edit();
            coins_buffer_editor.putLong("coins", coins);
            coins_buffer_editor.commit();
            coins_buffer_editor.commit();
            Intent intent = new Intent(SettingActivity.this, ShopActivity.class);
            startActivity(intent);

            SettingActivity.this.finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
