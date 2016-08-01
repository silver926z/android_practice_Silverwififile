package com.example.silver.silverwififile;

/**
 * Created by silver on 16/7/31.
 */
import java.io.DataInputStream;
import java.io.IOException;
import  java.io.*;
import java.net.*;
import  java.io. OutputStreamWriter;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

public class MyServerThread extends Thread{
    private static final String LOG_TAG = "MyServerThread";

    private MainActivity  m_activityMain;

    public MyServerThread(MainActivity activityMain)
    {
        super();

        // Save the activity
        m_activityMain = activityMain;

    }

    @Override
    public void run()
    {
        while (true)
        {
            try
            {
                // Wait for new client connection
                Log.i(LOG_TAG, "Waiting for client connection...");
                Socket socketClient = m_activityMain.m_serverSocket.accept();

                Log.i(LOG_TAG, "Accepted connection from " + socketClient.getInetAddress().getHostAddress());
                (m_activityMain).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        m_activityMain.myText.setText("Accepted connection");
                    }
                });

                // Read input from client socket
                InputStream is = socketClient.getInputStream();
                OutputStream os = socketClient.getOutputStream();
                DataInputStream dis = new DataInputStream(is);


                byte [] mybytearray  = new byte [5242880]; //5mb
                int bytesRead;
                int current = 0;

                    FileOutputStream fos = new FileOutputStream("/storage/emulated/0/DCIM/received.jpg"); // destination path and name of file
                    BufferedOutputStream bos = new BufferedOutputStream(fos);
                try {
                    bytesRead = is.read(mybytearray, 0, mybytearray.length);
                    current = bytesRead;

                    System.out.print(current);
                }catch (Exception e)
                {
                    e.printStackTrace();
                }
                // thanks to A. Cádiz for the bug fix
                do {
                    //System.out.print(current);
                    bytesRead =
                            is.read(mybytearray, current, (mybytearray.length-current));
                    if(bytesRead >= 0) current += bytesRead;

                } while(bytesRead > -1);

                bos.write(mybytearray, 0 , current);
                bos.flush();
                System.out.println("done");
                bos.close();


                /*while (!socketClient.isClosed())
                {
                    /*
                    // Read a line
                    String sLine = dis.readLine();
                    Log.i(LOG_TAG, "Read client socket=[" + sLine + "]");
                    if (sLine == null)
                    {
                        break;
                    }



                }
                */

                // Close streams
                dis.close();
                os.close();
                is.close();

                // Close client socket
                Log.i(LOG_TAG, "Read data from client ok. Close connection from " + socketClient.getInetAddress().getHostAddress());
                socketClient.close();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }

            // Stop loop when server socket is closed
            if (m_activityMain.m_serverSocket.isClosed())
            {
                break;
            }
        }
    }
}
