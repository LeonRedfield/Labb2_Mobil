package app.com.game.teddy.labb3_sensorer_b;

import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.security.spec.ECField;

public class ServerConnector {
    private Socket socket;
    private String address;

    public ServerConnector(String address) {
        this.address = address;
        socket = null;
    }

    public void sendData(File file) {

        try {
            socket = new Socket(address, 6667);
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            String text = getFileText(file);
            Log.v("Textfile:", text);
            out.print(text);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

public String getFileText(File f) {
    BufferedReader br = null;
    String text = "";
    try {
        br = new BufferedReader(new FileReader(f));
        StringBuilder sb = new StringBuilder();
        String line = br.readLine();

        while (line != null) {
            sb.append(line);
            sb.append(System.lineSeparator());
            line = br.readLine();
        }
        text = sb.toString();
    }catch(Exception e){}
    finally
    {
        try{ br.close();} catch (Exception e){e.printStackTrace();}
    }
    return text;
    }
}