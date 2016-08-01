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
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.io.DataOutputStream;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;
import org.json.JSONObject;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;
public class ClientActivity extends AppCompatActivity {
    public EditText myEdit;
    public Button myButton;
    private Thread thread;                //執行緒
    private Socket clientSocket;        //客戶端的socket
    private BufferedWriter bw;            //取得網路輸出串流
    private BufferedReader br;            //取得網路輸入串流
    private String tmp;                    //做為接收時的緩存
    private JSONObject json_write,json_read;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client);
        myEdit= (EditText)findViewById(R.id.EditText);
        myButton = (Button)findViewById(R.id.button);
        myButton.setOnClickListener(event);
        thread=new Thread(Connection);
    }
    private Runnable Connection=new Runnable(){
        @Override
        public void run() {
            InetAddress serverAddr = null;
            SocketAddress sc_add = null;
            Socket socket = null;
            //要傳送的字串
            String message = "Hello Socket!!!!!";

            try {

                serverAddr = InetAddress.getByName(myEdit.getText().toString());
                sc_add= new InetSocketAddress(serverAddr,5000);

                //與Server連線，timeout時間2秒
                socket = new Socket();
                socket.connect(sc_add,2000);

                //傳送資料
                /*DataOutputStream out = new DataOutputStream(socket.getOutputStream());
                out.writeUTF(message);
                out.flush();*/


                File myFile = new File ("/storage/emulated/0/DCIM/m_1.jpg");
                byte [] mybytearray  = new byte [(int)myFile.length()];
                FileInputStream fis = new FileInputStream(myFile);
                BufferedInputStream bis = new BufferedInputStream(fis);
                bis.read(mybytearray,0,mybytearray.length);
                OutputStream os = socket.getOutputStream();
                System.out.println("Sending...");
                System.out.println(mybytearray.length);
                os.write(mybytearray,0,mybytearray.length);


                //關閉socket
                socket.close();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };
    private OnClickListener event = new OnClickListener(){
        public void onClick(View v){
            thread.start();
        }
    };
}
