package com.example.silver.silverwififile;

import android.support.v7.app.AppCompatActivity;
import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.content.Intent;
import android.widget.ToggleButton;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.InetAddress;
import android.net.wifi.WifiManager;
import android.net.wifi.WifiInfo;

public class MainActivity extends AppCompatActivity {
    public Button myButton;
    public TextView myText;
    public ServerSocket     m_serverSocket      = null;

    // Private Controls
    private ToggleButton    m_toggleButtonServerOnOff;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myButton = (Button)findViewById(R.id.button);
        myButton.setOnClickListener(event);

        // Initialize control variables
        m_toggleButtonServerOnOff       = (ToggleButton)findViewById(R.id.toggleButton);

        // Set listener functions
        m_toggleButtonServerOnOff.setOnClickListener(onToggleServerOnOff);
    }
    private String getMyIp(){
        //新增一個WifiManager物件並取得WIFI_SERVICE
        WifiManager wifi_service = (WifiManager)getSystemService(WIFI_SERVICE);
        //取得wifi資訊
        WifiInfo wifiInfo = wifi_service.getConnectionInfo();
        //取得IP，但這會是一個詭異的數字，還要再自己換算才行
        int ipAddress = wifiInfo.getIpAddress();
        //利用位移運算和AND運算計算IP
        String ip = String.format("%d.%d.%d.%d",(ipAddress & 0xff),(ipAddress >> 8 & 0xff),(ipAddress >> 16 & 0xff),(ipAddress >> 24 & 0xff));
        return ip;
    }
    private OnClickListener event = new OnClickListener(){
        public void onClick(View v){
                /*String str= myEdit.getText().toString();
                myText.setText(str);*/

            Intent intent = new Intent();
            intent.setClass(MainActivity.this, ClientActivity.class);
            /*Bundle bundle = new Bundle();
            bundle.putDouble("height",height );
            bundle.putDouble("weight", weight);

            //將Bundle物件assign給intent
            intent.putExtras(bundle);*/
            startActivity(intent);

        }
    };

    private Button.OnClickListener onToggleServerOnOff = new Button.OnClickListener(){
        public void onClick(View v)
        {
            myText = (TextView)findViewById(R.id.textView2);
            myText.setText("IP:\n"+getMyIp());

            if (m_toggleButtonServerOnOff.isChecked())
            {
                try
                {
                    // Open a server socket
                    int nServerPort = 5000;
                    m_serverSocket = new ServerSocket(nServerPort);

                    // Start a server thread to do socket-accept tasks
                    MyServerThread serverThread = new MyServerThread(MainActivity.this);
                    serverThread.start();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
            else
            {   myText.setText("Server offline\n");
                try
                {
                    if(m_serverSocket != null) {
                        // Close server socket
                        m_serverSocket.close();
                    }
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        }
    };

}
