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
import java.util.LinkedList;
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
import android.util.StringBuilderPrinter;
import android.widget.TextView;

import org.w3c.dom.Text;

class PollDataTask extends AsyncTask<Void, Void, String> {

	private boolean running;
	private final static String FILENAME = "testfil.txt";
	private TextView dataView;
	private LinkedList<Integer> bufferQueue, storeList;

	protected PollDataTask(MainActivity activity, BluetoothDevice noninDevice) {
		this.activity = activity;
		this.noninDevice = noninDevice;
		this.adapter = BluetoothAdapter.getDefaultAdapter();

		this.dataView = (TextView) activity.findViewById(R.id.dataView);
		running = true;

		bufferQueue = new LinkedList<>();
		storeList = new LinkedList<>();

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
		StringBuilder sb1 = new StringBuilder();
		try {
			socket = noninDevice
					.createRfcommSocketToServiceRecord(STANDARD_SPP_UUID);
			socket.connect();

			InputStream is = socket.getInputStream();
			OutputStream os = socket.getOutputStream();

			os.write(FORMAT2);
			os.flush();
			byte[] reply = new byte[1];
			is.read(reply);

			OutputStreamWriter osw = null;
			if(reply[0] == NAK) {
				Log.v("Nak", "NAK received");
			}

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

				int[] frameByte = new int[5];
				int counter = -1;
				while(running) {
					byte[] frame = new byte[4]; // this -obsolete- format specifies
					// 4 bytes per frame

					is.read(frame);

					final int value1 = unsignedByteToInt(frame[0]);
					final int value2 = unsignedByteToInt(frame[1]);
					final int value3 = unsignedByteToInt(frame[2]);
					final int value4 = unsignedByteToInt(frame[3]);

					bufferQueue.add(value1);
					bufferQueue.add(value2);
					bufferQueue.add(value3);
					bufferQueue.add(value4);

					if(bufferQueue.size() >= 10) {
						frameByte = getFrameByte(bufferQueue);
						if(frameByte != null && frameByte[1]%2 == 1){counter = 0;}
						else if(counter != -1) { counter= (counter+1)%24;}
					}
					if(frameByte != null) {
						storeList.add(frameByte[2]);
						pr.println(frameByte[2] +" ");
						pr.flush();
					}
					else {Log.v("Byte is: ", "Null");}
					if(counter == 1) {
						final int v1 = frameByte[3];
						String out = value1 + " " +value2 +"\n";
						dataView.post(new Runnable() {
							public void run() {
								dataView.setText("" +v1);
							}
						});
					}


					output = value1 + "; " + value2 + "\r\n";

					int tmp = 0;
					for(byte b: frame) {
						tmp = unsignedByteToInt(b);
						sb1.append(tmp + " ");
					}




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
		ServerConnector sc = new ServerConnector("130.229.164.1");
		File path1 = Environment.getExternalStoragePublicDirectory(
				Environment.DIRECTORY_DOWNLOADS);
		File file1 = new File(path, FILENAME);
		sc.sendData(file);
		return output;
	}
	private int[] getFrameByte(LinkedList<Integer> list) {
		int[] frameByte = new int[5];
		int firstByte = -1;
		do{
			firstByte = list.poll();
		}while(firstByte != 1);
		frameByte[0] = firstByte;
		for(int i = 1; i < 5; i++){
			frameByte[i] = list.poll();
		}
		int sum = 0;
		for(int i = 0; i < 4; i++) { sum += frameByte[i];}
		if(sum % 256 == frameByte[4]) { return frameByte;}
		return null;
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
	private static final byte[] FORMAT2 = { 0x02, 0x70, 0x04, 0x02, 0x02, 0x00, 0x78, 0x03};
	private static final byte ACK = 0x06; // ACK from Nonin sensor
	private static final byte NAK = 0x15;

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
