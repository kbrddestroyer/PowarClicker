package com.alexgames.powarclicker;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.text.format.Formatter;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;


public class PaymentActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public Socket clientSocket = null;
    public ServerSocket serverSocket = null;
    public long dollars = 0L;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
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
        TextView tv = findViewById(R.id.dollars);
        tv.setText(String.valueOf(dollars));
        Button start = findViewById(R.id.server_start_btn);
        Button stop = findViewById(R.id.stop_server_btn);

        start.setEnabled(true);
        stop.setEnabled(false);

        server();
        client();
        stopServer();
    }

    @Override
    protected void onStart()
    {
        super.onStart();
        SharedPreferences coins_buffer = getSharedPreferences("cbuffer", 0);

        dollars = coins_buffer.getLong("dollars", 0L);
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        SharedPreferences coins_buffer = getSharedPreferences("cbuffer", 0);

        dollars = coins_buffer.getLong("dollars", 0L);
    }


    void getIP()
    {
        WifiManager wm = (WifiManager) getSystemService(WIFI_SERVICE);
        String ip = Formatter.formatIpAddress(wm.getConnectionInfo().getIpAddress());
        TextView ip_addr = findViewById(R.id.server_ip);
        ip_addr.setText("Ваш IP: " + ip);
    }

    void stopServer()
    {
        final Button stop = findViewById(R.id.stop_server_btn);
        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Button btn = findViewById(R.id.server_start_btn);
                btn.setEnabled(true);
                stop.setEnabled(false);
                final Handler handler = new Handler();
                final Thread clientThread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Looper.prepare();
                        try {
                            Long dollars_to_send_var = 0L;
                            Socket socket = new Socket("0.0.0.0", 4444);
                            PrintWriter output = new PrintWriter(socket.getOutputStream());
                            output.println(String.valueOf(dollars_to_send_var));
                            output.flush();

                            BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                            final String catchData = input.readLine();

                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    msgbox("Сервер остановлен!", catchData);
                                }
                            });

                            socket.getOutputStream().close();
                            output.close();
                            socket.close();

                        } catch (final Exception e) {
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    msgbox("Ошибка!", "Нельзя остановить сервер! ");
                                }
                            });
                        }
                    }
                });
                clientThread.start();
            }
        });
    }

    void server()
    {
        final Button start = findViewById(R.id.server_start_btn);
        start.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                getIP();
                Button stop = findViewById(R.id.stop_server_btn);
                start.setEnabled(false);
                stop.setEnabled(true);
                final Handler handler = new Handler();
                Thread serverThread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Looper.prepare();
                        try{
                            serverSocket = new ServerSocket(4444);
                        } catch (final Exception e) {
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    msgbox("Ошибка!", "Ошибка в запуске: " + String.valueOf(e));
                                }
                            });
                        }
                        boolean end = false;
                        while(!end) {
                            try {
                                clientSocket = serverSocket.accept();
                            } catch (final Exception e)
                            {
                                msgbox("Ошибка", "Ошибка в подключении клиента! " + String.valueOf(e));
                            }
                            PrintWriter output = null;
                            BufferedReader input = null;
                            try {
                                output = new PrintWriter(clientSocket.getOutputStream());
                                input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                            } catch (Exception e)
                            {
                                msgbox("Ошибка!", "Ошибка в чтении данных!");
                            }
                            String data;

                            try {
                                data = input.readLine();
                            } catch (Exception e)
                            {
                                msgbox("Ошибка!", "Ошибка в чтении буффера! " + String.valueOf(e));
                                break;
                            }

                            final String data_read = data;
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    msgbox("Зачисление: ", "На ваш счет пришло " + data_read + " повар-долларов");
                                    SharedPreferences coins_buffer = getSharedPreferences("cbuffer", 0);
                                    SharedPreferences.Editor coins_buffer_editor = coins_buffer.edit();
                                    dollars += Long.parseLong(data_read);
                                    coins_buffer_editor.putLong("dollars", dollars);
                                    coins_buffer_editor.commit();
                                    TextView tv = findViewById(R.id.dollars);
                                    tv.setText(String.valueOf(dollars));
                                }
                            });


                            SharedPreferences coins_memory = getSharedPreferences("coin_val", 0);
                            SharedPreferences.Editor editor = coins_memory.edit();
                            editor.putLong("dollar_num", dollars);
                            editor.commit();
                            output.write("Транзакция прошла успешно!");
                            output.flush();
                            output.close();

                            try {
                                clientSocket.close();
                            } catch (final Exception e)
                            {
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        msgbox("Ошибка!", "Ошибка в закрытии клиента! " + String.valueOf(e));
                                    }
                                });
                            }
                            break;
                        }
                    }
                });
                serverThread.start();
            }
        });
    }

    void client()
    {
        final Button start = findViewById(R.id.client_start_btn);
        final EditText ip_input = findViewById(R.id.server_ip_input);
        final EditText dollars_in = findViewById(R.id.dollars_value_send);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String ip = String.valueOf(ip_input.getText());
                final long dollars_to_send = Long.parseLong(String.valueOf(dollars_in.getText()));
                final Handler handler = new Handler();
                final Thread clientThread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Looper.prepare();
                        try {
                            Long dollars_to_send_var = 0L;
                            Socket socket = new Socket(ip, 4444);
                            if (dollars - dollars_to_send >= 0) {
                                dollars -= dollars_to_send;
                                dollars_to_send_var = dollars_to_send;
                            } else {
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        msgbox("Внимание!", "Не хватает долларов!");
                                    }
                                });
                                dollars_to_send_var = 0L;
                            }
                            PrintWriter output = new PrintWriter(socket.getOutputStream());
                            output.println(String.valueOf(dollars_to_send_var));
                            output.flush();

                            BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                            final String catchData = input.readLine();

                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    msgbox("Завершено", catchData);
                                    SharedPreferences coins_buffer = getSharedPreferences("cbuffer", 0);
                                    SharedPreferences.Editor coins_buffer_editor = coins_buffer.edit();
                                    coins_buffer_editor.putLong("dollars", dollars);
                                    coins_buffer_editor.commit();
                                    TextView tv = findViewById(R.id.dollars);
                                    tv.setText(String.valueOf(dollars));
                                }
                            });

                            socket.getOutputStream().close();
                            output.close();
                            socket.close();

                        } catch (final Exception e) {
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    msgbox("Ошибка!", "Нельзя запустить клиент! ");
                                }
                            });
                        }
                    }
                });
                clientThread.start();
            }
        });
    }


    void msgbox(String caption, String message)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(PaymentActivity.this);
        builder.setTitle(caption)
                .setMessage(message)
                .setCancelable(false)
                .setNegativeButton("Ok",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
        AlertDialog alert = builder.create();
        alert.show();
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
        getMenuInflater().inflate(R.menu.payment, menu);
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

            Intent intent = new Intent(PaymentActivity.this, InfoActivity.class);

            SharedPreferences coins_buffer = getSharedPreferences("cbuffer", 0);
            SharedPreferences.Editor coins_buffer_editor = coins_buffer.edit();
            coins_buffer_editor.putLong("dollars", dollars);
            coins_buffer_editor.commit();

            startActivity(intent);


            PaymentActivity.this.finish();

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
            Intent intent = new Intent(PaymentActivity.this, MainActivity.class);

            SharedPreferences coins_buffer = getSharedPreferences("cbuffer", 0);
            SharedPreferences.Editor coins_buffer_editor = coins_buffer.edit();
            coins_buffer_editor.putLong("dollars", dollars);
            coins_buffer_editor.commit();

            startActivity(intent);


            PaymentActivity.this.finish();
        } else if (id == R.id.nav_change) {
            Intent intent = new Intent(PaymentActivity.this, ChangeActivity.class);
            SharedPreferences coins_buffer = getSharedPreferences("cbuffer", 0);
            SharedPreferences.Editor coins_buffer_editor = coins_buffer.edit();
            coins_buffer_editor.putLong("dollars", dollars);
            coins_buffer_editor.commit();
            startActivity(intent);


            PaymentActivity.this.finish();
        } else if (id == R.id.nav_payment) {

        } else if (id == R.id.nav_shop) {
            Intent intent = new Intent(PaymentActivity.this, ShopActivity.class);
            SharedPreferences coins_buffer = getSharedPreferences("cbuffer", 0);
            SharedPreferences.Editor coins_buffer_editor = coins_buffer.edit();
            coins_buffer_editor.putLong("dollars", dollars);
            coins_buffer_editor.commit();
            startActivity(intent);


            PaymentActivity.this.finish();
        } else if (id == R.id.nav_settings) {
            Intent intent = new Intent(PaymentActivity.this, SettingActivity.class);
            SharedPreferences coins_buffer = getSharedPreferences("cbuffer", 0);
            SharedPreferences.Editor coins_buffer_editor = coins_buffer.edit();
            coins_buffer_editor.putLong("dollars", dollars);
            coins_buffer_editor.commit();
            startActivity(intent);


            PaymentActivity.this.finish();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
