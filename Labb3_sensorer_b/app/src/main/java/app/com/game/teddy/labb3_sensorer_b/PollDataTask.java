package app.com.game.teddy.labb3_sensorer_b;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

class PollDataTask extends AsyncTask<Void, Void, String> {

	private boolean running;
	private final static String FILENAME = "testfil.txt";


	protected PollDataTask(MainActivity activity, BluetoothDevice noninDevice) {
		this.activity = activity;
		this.noninDevice = noninDevice;
		this.adapter = BluetoothAdapter.getDefaultAdapter();
		running = true;
	}

	/**
	 * A simple example: poll one frame of data from the Nonin sensor
	 */
	@Override
	protected String doInBackground(Void... v) {
		String output = "";

		// an ongoing discovery will slow down the connection
		adapter.cancelDiscovery();

		BluetoothSocket socket = null;

		OutputStream fos = null;
		File path = Environment.getExternalStoragePublicDirectory(
				Environment.DIRECTORY_DOWNLOADS);
		File file = new File(path, FILENAME);
		PrintWriter pr = null;
		try {
			socket = noninDevice
					.createRfcommSocketToServiceRecord(STANDARD_SPP_UUID);
			socket.connect();

			InputStream is = socket.getInputStream();
			OutputStream os = socket.getOutputStream();

			os.write(FORMAT);
			os.flush();
			byte[] reply = new byte[1];
			is.read(reply);

			OutputStreamWriter osw = null;
			if (reply[0] == ACK) {
				try {
					path.mkdirs();
					fos = new FileOutputStream(file);
					pr = new PrintWriter(fos);
					String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date());
					//fos.write(timestamp.getBytes());
					pr.println(timestamp);
				}catch(Exception e){e.printStackTrace();}
				Log.v("Before ", " while loop");

				while(running) {
					byte[] frame = new byte[4]; // this -obsolete- format specifies
					// 4 bytes per frame

					is.read(frame);

					int value1 = unsignedByteToInt(frame[1]);
					int value2 = unsignedByteToInt(frame[2]);
					output = value1 + "; " + value2 + "\r\n";
					Log.v("In ", "while loop" + " " +value1 + " " +value2);
					String out = value1 + " " +value2 +"\n";
					pr.println(value1 +" " +value2);
					pr.flush();
				}
			}
		} catch (Exception e) {
			output = e.getMessage();
			e.printStackTrace();
		} finally {
			try {if (socket != null)socket.close();} catch (Exception e) {}
			try {if (pr != null)pr.close();} catch (Exception e) {}
			try {if (fos != null)fos.close();} catch (Exception e) {}

		}
		MediaScannerConnection.scanFile(activity,
				new String[] { file.toString() }, null,
				new MediaScannerConnection.OnScanCompletedListener() {
					public void onScanCompleted(String path, Uri uri) {
						Log.i("ExternalStorage", "Scanned " + path + ":");
						Log.i("ExternalStorage", "-> uri=" + uri);
					}
				});

		Log.v("After while", " loop");
		ServerConnector sc = new ServerConnector("130.229.188.158");
		File path1 = Environment.getExternalStoragePublicDirectory(
				Environment.DIRECTORY_DOWNLOADS);
		File file1 = new File(path, FILENAME);
		sc.sendData(file);
		return output;
	}

	@Override
	protected void onCancelled() {
		super.onCancelled();
		running = false;
	}

	/**
	 * update the UI (executed on the main thread)
	 */
	@Override
	protected void onPostExecute(String output) {

		activity.displayData(output);
	}

	// The byte sequence to set sensor to a basic, and obsolete, format
	private static final byte[] FORMAT = { 0x44, 0x31 };
	private static final byte ACK = 0x06; // ACK from Nonin sensor

	private static final UUID STANDARD_SPP_UUID = UUID
			.fromString("00001101-0000-1000-8000-00805F9B34FB");

	private MainActivity activity;
	private BluetoothDevice noninDevice;
	private BluetoothAdapter adapter;

	// NB! Java does not support unsigned types
	private int unsignedByteToInt(byte b) {
		return (int) b & 0xFF;
	}


}
