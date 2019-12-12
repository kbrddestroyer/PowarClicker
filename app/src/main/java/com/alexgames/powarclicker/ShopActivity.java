package com.alexgames.powarclicker;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ShopActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    public long cost = 0L;
    public long dollars = 0L;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        SharedPreferences coins_buffer = getSharedPreferences("cbuffer", 0);
        dollars = coins_buffer.getLong("dollars", 0L);
        TextView text = findViewById(R.id.dollars);
        text.setText(String.valueOf(dollars));
        listDraw();
        buy();
    }

    void listDraw()
    {
        ListView list = (ListView)findViewById(R.id.items_list);
        final String[] items = new String[] {
                "Повар","Игра Тетрис","Кофемашина","Калькулятор","Видеокарта","Майнинг ферма",
                "Поварбанк","Шеф-повар", "Сервер", "Датацентр"
        };
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, items);
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View itemClicked, int position,
                                    long id) {
                String caption = String.valueOf(((TextView) itemClicked).getText());
                showInfo(caption);
            }
        });
    }

    void buy()
    {
        final Button buy = findViewById(R.id.item_button);
        buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dollars - cost >= 0) {
                    dollars -= cost;
                    TextView tv_d = findViewById(R.id.dollars);
                    tv_d.setText(String.valueOf(dollars));
                    String caption = String.valueOf(buy.getText());
                    switch (caption) {
                        case "Повар": {
                            SharedPreferences items_memory = getSharedPreferences("items_buffer", 0);
                            SharedPreferences.Editor editor = items_memory.edit();

                            SharedPreferences coins_buffer = getSharedPreferences("cbuffer", 0);
                            SharedPreferences.Editor coins_editor = coins_buffer.edit();

                            long click = coins_buffer.getLong("click", 1L);

                            click += 1;
                            cost = Math.round(cost * 1.5);

                            editor.putLong("cost1", cost);
                            coins_editor.putLong("click", click);

                            TextView tv = findViewById(R.id.item_cost);
                            tv.setText(String.valueOf(cost));

                            coins_editor.commit();
                            editor.commit();

                            Toast toast = Toast.makeText(getApplicationContext(),
                                    "Вы купили Повара. Какова его профессия?..", Toast.LENGTH_SHORT);
                            toast.show();
                            break;
                        }
                        case "Игра Тетрис": {
                            SharedPreferences items_memory = getSharedPreferences("items_buffer", 0);
                            SharedPreferences.Editor editor = items_memory.edit();

                            SharedPreferences coins_buffer = getSharedPreferences("cbuffer", 0);
                            SharedPreferences.Editor coins_editor = coins_buffer.edit();

                            long auto_click = coins_buffer.getLong("auto_click", 0L);

                            auto_click += 1;
                            cost = Math.round(cost * 1.5);

                            editor.putLong("cost2", cost);
                            coins_editor.putLong("auto_click", auto_click);

                            TextView tv = findViewById(R.id.item_cost);
                            tv.setText(String.valueOf(cost));

                            coins_editor.commit();
                            editor.commit();

                            Toast toast = Toast.makeText(getApplicationContext(),
                                    "Вы купили Тетрис. Уберите эту стену!!!", Toast.LENGTH_SHORT);
                            toast.show();
                            break;
                        }
                        case "Кофемашина": {
                            SharedPreferences items_memory = getSharedPreferences("items_buffer", 0);
                            SharedPreferences.Editor editor = items_memory.edit();

                            SharedPreferences coins_buffer = getSharedPreferences("cbuffer", 0);
                            SharedPreferences.Editor coins_editor = coins_buffer.edit();

                            long click = coins_buffer.getLong("click", 1L);

                            click += 5;
                            cost = Math.round(cost * 1.5);

                            editor.putLong("cost3", cost);
                            coins_editor.putLong("click", click);

                            TextView tv = findViewById(R.id.item_cost);
                            tv.setText(String.valueOf(cost));

                            coins_editor.commit();
                            editor.commit();

                            Toast toast = Toast.makeText(getApplicationContext(),
                                    "Вы купили Кофемашину. КОФЕ!!!", Toast.LENGTH_SHORT);
                            toast.show();
                            break;
                        }
                        case "Калькулятор": {
                            SharedPreferences items_memory = getSharedPreferences("items_buffer", 0);
                            SharedPreferences.Editor editor = items_memory.edit();

                            SharedPreferences coins_buffer = getSharedPreferences("cbuffer", 0);
                            SharedPreferences.Editor coins_editor = coins_buffer.edit();

                            long click = coins_buffer.getLong("click", 1L);

                            click += 25;
                            cost = Math.round(cost * 1.5);

                            editor.putLong("cost4", cost);
                            coins_editor.putLong("click", click);

                            TextView tv = findViewById(R.id.item_cost);
                            tv.setText(String.valueOf(cost));

                            coins_editor.commit();
                            editor.commit();

                            Toast toast = Toast.makeText(getApplicationContext(),
                                    "Вы купили Калькулятор. Тетрис потянет? ", Toast.LENGTH_SHORT);
                            toast.show();
                            break;
                        }
                        case "Видеокарта": {
                            SharedPreferences items_memory = getSharedPreferences("items_buffer", 0);
                            SharedPreferences.Editor editor = items_memory.edit();

                            SharedPreferences coins_buffer = getSharedPreferences("cbuffer", 0);
                            SharedPreferences.Editor coins_editor = coins_buffer.edit();

                            long auto_click = coins_buffer.getLong("auto_click", 0L);

                            auto_click += 10;
                            cost = Math.round(cost * 1.5);

                            editor.putLong("cost5", cost);
                            coins_editor.putLong("auto_click", auto_click);

                            TextView tv = findViewById(R.id.item_cost);
                            tv.setText(String.valueOf(cost));

                            coins_editor.commit();
                            editor.commit();

                            Toast toast = Toast.makeText(getApplicationContext(),
                                    "Вы купили Видеокарту. Ну теперь то тетрис заработает?!", Toast.LENGTH_SHORT);
                            toast.show();
                            break;
                        }
                        case "Майнинг ферма": {
                            SharedPreferences items_memory = getSharedPreferences("items_buffer", 0);
                            SharedPreferences.Editor editor = items_memory.edit();

                            SharedPreferences coins_buffer = getSharedPreferences("cbuffer", 0);
                            SharedPreferences.Editor coins_editor = coins_buffer.edit();

                            long auto_click = coins_buffer.getLong("auto_click", 0L);

                            auto_click += 50;
                            cost = Math.round(cost * 1.5);

                            editor.putLong("cost6", cost);
                            coins_editor.putLong("auto_click", auto_click);

                            TextView tv = findViewById(R.id.item_cost);
                            tv.setText(String.valueOf(cost));

                            coins_editor.commit();
                            editor.commit();

                            Toast toast = Toast.makeText(getApplicationContext(),
                                    "Вы купили Майнинг-ферму. Тетрис, работай!!!", Toast.LENGTH_SHORT);
                            toast.show();
                            break;
                        }
                        case "Поварбанк": {
                            SharedPreferences items_memory = getSharedPreferences("items_buffer", 0);
                            SharedPreferences.Editor editor = items_memory.edit();

                            SharedPreferences coins_buffer = getSharedPreferences("cbuffer", 0);
                            SharedPreferences.Editor coins_editor = coins_buffer.edit();

                            long click = coins_buffer.getLong("click", 1L);

                            click += 125;
                            cost = Math.round(cost * 1.5);

                            editor.putLong("cost7", cost);
                            coins_editor.putLong("click", click);

                            TextView tv = findViewById(R.id.item_cost);
                            tv.setText(String.valueOf(cost));

                            coins_editor.commit();
                            editor.commit();

                            Toast toast = Toast.makeText(getApplicationContext(),
                                    "Вы купили Поварбанк. Что значит - нужно заряжать?!", Toast.LENGTH_SHORT);
                            toast.show();
                            break;
                        }
                        case "Шеф-повар": {
                            SharedPreferences items_memory = getSharedPreferences("items_buffer", 0);
                            SharedPreferences.Editor editor = items_memory.edit();

                            SharedPreferences coins_buffer = getSharedPreferences("cbuffer", 0);
                            SharedPreferences.Editor coins_editor = coins_buffer.edit();

                            long click = coins_buffer.getLong("click", 1L);

                            click += 725;
                            cost = Math.round(cost * 1.5);

                            editor.putLong("cost8", cost);
                            coins_editor.putLong("click", click);

                            TextView tv = findViewById(R.id.item_cost);
                            tv.setText(String.valueOf(cost));

                            coins_editor.commit();
                            editor.commit();

                            Toast toast = Toast.makeText(getApplicationContext(),
                                    "Вы купили Шеф-Повара. Он милиционер?", Toast.LENGTH_SHORT);
                            toast.show();
                            break;
                        }
                        case "Сервер": {
                            SharedPreferences items_memory = getSharedPreferences("items_buffer", 0);
                            SharedPreferences.Editor editor = items_memory.edit();

                            SharedPreferences coins_buffer = getSharedPreferences("cbuffer", 0);
                            SharedPreferences.Editor coins_editor = coins_buffer.edit();

                            long auto_click = coins_buffer.getLong("auto_click", 0L);

                            auto_click += 250;
                            cost = Math.round(cost * 1.5);

                            editor.putLong("cost9", cost);
                            coins_editor.putLong("auto_click", auto_click);

                            TextView tv = findViewById(R.id.item_cost);
                            tv.setText(String.valueOf(cost));

                            coins_editor.commit();
                            editor.commit();

                            Toast toast = Toast.makeText(getApplicationContext(),
                                    "Вы купили Сервер. Тетрис по-прежнему не работает", Toast.LENGTH_SHORT);
                            toast.show();
                            break;
                        }
                        case "Датацентр": {
                            SharedPreferences items_memory = getSharedPreferences("items_buffer", 0);
                            SharedPreferences.Editor editor = items_memory.edit();

                            SharedPreferences coins_buffer = getSharedPreferences("cbuffer", 0);
                            SharedPreferences.Editor coins_editor = coins_buffer.edit();

                            long auto_click = coins_buffer.getLong("auto_click", 0L);

                            auto_click += 1250;
                            cost = Math.round(cost * 1.5);

                            editor.putLong("cost10", cost);
                            coins_editor.putLong("auto_click", auto_click);

                            TextView tv = findViewById(R.id.item_cost);
                            tv.setText(String.valueOf(cost));

                            coins_editor.commit();
                            editor.commit();

                            Toast toast = Toast.makeText(getApplicationContext(),
                                    "Вы купили Датацентр. Вычисляем блоки для тетриса", Toast.LENGTH_SHORT);
                            toast.show();
                            break;
                        }
                    }
                } else
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(ShopActivity.this);
                    builder.setTitle("Внимание!")
                            .setMessage("Не хватает Повар-Долларов!")
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
        });
    }


    void showInfo(final String name)
    {
        Button btn = findViewById(R.id.item_button);
        TextView cost_text = findViewById(R.id.item_cost);
        TextView desc = findViewById(R.id.item_info);
        switch (name)
        {
            case "Повар":
            {
                btn.setText("Повар");
                SharedPreferences items_memory = getSharedPreferences("items_buffer", 0);
                cost = items_memory.getLong("cost1", 100);
                cost_text.setText(String.valueOf(cost));
                String descr = "Клик +1";
                desc.setText(descr);
                break;
            }
            case "Игра Тетрис":
            {
                btn.setText("Игра Тетрис");
                SharedPreferences items_memory = getSharedPreferences("items_buffer", 0);
                cost = items_memory.getLong("cost2", 500);
                cost_text.setText(String.valueOf(cost));
                String descr = "+1 коин/сек";
                desc.setText(descr);
                break;
            }
            case "Кофемашина":
            {
                btn.setText("Кофемашина");
                SharedPreferences items_memory = getSharedPreferences("items_buffer", 0);
                cost = items_memory.getLong("cost3", 2500);
                cost_text.setText(String.valueOf(cost));
                String descr = "Клик +5";
                desc.setText(descr);
                break;
            }
            case "Калькулятор":
            {
                btn.setText("Калькулятор");
                SharedPreferences items_memory = getSharedPreferences("items_buffer", 0);
                cost = items_memory.getLong("cost4", 12500);
                cost_text.setText(String.valueOf(cost));
                String descr = "Клик +25";
                desc.setText(descr);
                break;
            }
            case "Видеокарта":
            {
                btn.setText("Видеокарта");
                SharedPreferences items_memory = getSharedPreferences("items_buffer", 0);
                cost = items_memory.getLong("cost5", 62500);
                cost_text.setText(String.valueOf(cost));
                String descr = "+10 коин/сек";
                desc.setText(descr);
                break;
            }
            case "Майнинг ферма":
            {
                btn.setText("Майнинг ферма");
                SharedPreferences items_memory = getSharedPreferences("items_buffer", 0);
                cost = items_memory.getLong("cost6", 312500);
                cost_text.setText(String.valueOf(cost));
                String descr = "+50 коин/сек";
                desc.setText(descr);
                break;
            }
            case "Поварбанк":
            {
                btn.setText("Поварбанк");
                SharedPreferences items_memory = getSharedPreferences("items_buffer", 0);
                cost = items_memory.getLong("cost7", 1565000);
                cost_text.setText(String.valueOf(cost));
                String descr = "Клик +125";
                desc.setText(descr);
                break;
            }
            case "Шеф-повар":
            {
                btn.setText("Шеф-повар");
                SharedPreferences items_memory = getSharedPreferences("items_buffer", 0);
                cost = items_memory.getLong("cost8", 7815000);
                cost_text.setText(String.valueOf(cost));
                String descr = "Клик +725";
                desc.setText(descr);
                break;
            }
            case "Сервер":
            {
                btn.setText("Сервер");
                SharedPreferences items_memory = getSharedPreferences("items_buffer", 0);
                cost = items_memory.getLong("cost9", 40000000);
                cost_text.setText(String.valueOf(cost));
                String descr = "+250 коин/сек";
                desc.setText(descr);
                break;
            }
            case "Датацентр":
            {
                btn.setText("Датацентр");
                SharedPreferences items_memory = getSharedPreferences("items_buffer", 0);
                cost = items_memory.getLong("cost10", 200000000);
                cost_text.setText(String.valueOf(cost));
                String descr = "+1250 коин/сек";
                desc.setText(descr);
                break;
            }
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
        getMenuInflater().inflate(R.menu.shop, menu);
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

            Intent intent = new Intent(ShopActivity.this, InfoActivity.class);

            startActivity(intent);

            SharedPreferences coins_buffer = getSharedPreferences("cbuffer", 0);
            SharedPreferences.Editor coins_buffer_editor = coins_buffer.edit();
            coins_buffer_editor.putLong("dollars", dollars);
            coins_buffer_editor.commit();

            ShopActivity.this.finish();

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
            Intent intent = new Intent(ShopActivity.this, MainActivity.class);

            startActivity(intent);

            SharedPreferences coins_buffer = getSharedPreferences("cbuffer", 0);
            SharedPreferences.Editor coins_buffer_editor = coins_buffer.edit();
            coins_buffer_editor.putLong("dollars", dollars);
            coins_buffer_editor.commit();

            ShopActivity.this.finish();
        } else if (id == R.id.nav_change) {
            Intent intent = new Intent(ShopActivity.this, ChangeActivity.class);

            startActivity(intent);

            SharedPreferences coins_buffer = getSharedPreferences("cbuffer", 0);
            SharedPreferences.Editor coins_buffer_editor = coins_buffer.edit();
            coins_buffer_editor.putLong("dollars", dollars);
            coins_buffer_editor.commit();

            ShopActivity.this.finish();
        } else if (id == R.id.nav_payment) {
            Intent intent = new Intent(ShopActivity.this, PaymentActivity.class);

            startActivity(intent);

            SharedPreferences coins_buffer = getSharedPreferences("cbuffer", 0);
            SharedPreferences.Editor coins_buffer_editor = coins_buffer.edit();
            coins_buffer_editor.putLong("dollars", dollars);
            coins_buffer_editor.commit();

            ShopActivity.this.finish();
        } else if (id == R.id.nav_settings) {
            Intent intent = new Intent(ShopActivity.this, SettingActivity.class);

            startActivity(intent);

            SharedPreferences coins_buffer = getSharedPreferences("cbuffer", 0);
            SharedPreferences.Editor coins_buffer_editor = coins_buffer.edit();
            coins_buffer_editor.putLong("dollars", dollars);
            coins_buffer_editor.commit();

            ShopActivity.this.finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
