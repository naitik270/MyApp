package com.demo.nspl.restaurantlite.printerUtility;
import android.app.PendingIntent;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.hardware.usb.UsbManager;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;

import com.andprn.port.android.USBPort;
import com.andprn.port.android.USBPortConnection;
import com.demo.nspl.restaurantlite.Global.ClsGlobal;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class ClsPrinter {

    static BluetoothAdapter badapter;
    static volatile boolean stopWorker;
    static String btname = "";
    static String status = "";
    public static BluetoothSocket mmSocket;
    public static BluetoothDevice mmDevice;
    static BluetoothAdapter mBluetoothAdapter;
    static OutputStream mmOutputStream;
    static InputStream mmInputStream;

    static byte[] readBuffer;
    static int readBufferPosition;
    static Thread workerThread;
    static int Ptype = 0;

    static UsbManager mUsbManager;
    private static USBPort port;
    static int prn, type = 0;
    private static PendingIntent mPermissionIntent;
    static USBPortConnection connection;

    public static String _status = "";
    static String _printStatus = "";


    // start Bluetooth
    public static boolean isbluetoothconnection(Context c) {
        try {
            badapter = BluetoothAdapter.getDefaultAdapter();
            if (badapter != null) {
                badapter.enable();
                if (!badapter.isEnabled()) {
                    Intent enableBluetooth = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    c.startActivity(enableBluetooth);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }


    //getBluetooth Device List

    public static List<String> loadbtpname() {
        List<String> list = new ArrayList<>();
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
        if (pairedDevices.size() > 0) {
            for (BluetoothDevice device : pairedDevices) {
                list.add(device.getName());
            }
        }

        return list;
    }


    public static class ConnectBT extends AsyncTask<Void, Void, String> {

//        ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            /*pDialog = new ProgressDialog(context);
            pDialog.setCancelable(false);
            pDialog.setMessage("Connecting...");
            pDialog.show();*/
        }

        @Override
        protected String doInBackground(Void... params) {
            try {
                Log.e("doInBackground","doInBackground call");
                findBT();
                openBT();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
//            if (pDialog.isShowing())
//                pDialog.dismiss();

            if (mmSocket != null) {
                if (mmSocket.isConnected()) {
                    _status = "connected";

                    Log.d("btn_connect", "ClsPrinter- " + _status);

//                    btn_connect.setTag("1");
//                    btn_connect.setText("disconnect");
                } else {

                    _status = "disconnect";
                    Log.d("btn_connect", "ClsPrinter- " + _status);
//                    toast("Printer Not Connect");
                }
            } else {
//                toast("Printer Not Connect");
                _status = "disconnect";
                Log.d("btn_connect", "ClsPrinter- " + _status);

            }
            Log.d("btn_connect", "Final Block- " + _status);

        }
    }


    public static void findBT() {

        try {

            btname = ClsGlobal.defaultPrinterName;
            status = ClsGlobal.defaultStatus;

            mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();


            Log.d("btn_connect", "onClick: " + btname);

            if (mBluetoothAdapter == null) {
//                toast("No Bluetooth Adapter Available");

                _status = "NOT FOUND";
            }

            Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();

            if (pairedDevices.size() > 0) {
                for (BluetoothDevice device : pairedDevices) {

                    // RPP300 is the name of the bluetooth printer device
                    // we got this name from the list of paired devices
                    if (device.getName().equals(btname)) {
                        mmDevice = device;
                        break;
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void openBT() throws IOException {

        try {
            UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");
            mmSocket = mmDevice.createRfcommSocketToServiceRecord(uuid);
            mmSocket.connect();
            mmOutputStream = mmSocket.getOutputStream();
            mmInputStream = mmSocket.getInputStream();

            beginListenForData();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void beginListenForData() {
        try {
            final Handler handler = new Handler();

            // This is the ASCII code for a newline character
            final byte delimiter = 10;

            stopWorker = false;
            readBufferPosition = 0;
            readBuffer = new byte[1024];

            workerThread = new Thread(new Runnable() {
                public void run() {
                    while (!Thread.currentThread().isInterrupted()
                            && !stopWorker) {

                        try {

                            int bytesAvailable = mmInputStream.available();
                            if (bytesAvailable > 0) {
                                byte[] packetBytes = new byte[bytesAvailable];
                                mmInputStream.read(packetBytes);
                                for (int i = 0; i < bytesAvailable; i++) {
                                    byte b = packetBytes[i];
                                    if (b == delimiter) {
                                        byte[] encodedBytes = new byte[readBufferPosition];
                                        System.arraycopy(readBuffer, 0,
                                                encodedBytes, 0,
                                                encodedBytes.length);
                                        final String data = new String(
                                                encodedBytes, "US-ASCII");
                                        readBufferPosition = 0;

                                        handler.post(new Runnable() {
                                            public void run() {
                                                //       myLabel.setText(data);
                                            }
                                        });
                                    } else {
                                        readBuffer[readBufferPosition++] = b;
                                    }
                                }
                            }

                        } catch (IOException ex) {
                            stopWorker = true;
                        }

                    }
                }
            });

            workerThread.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void PrintData(String data, int printType) {
        Log.e("PrintData","PrintData call");
        try {
            if (printType == 1) {
                sendData(data, 0);
                LineFeed();
            } else if (printType == 2) {
                sendData2(data, 0);
                LineFeed();
            } else if (printType == 3) {
                sendData3(data, 0);
                LineFeed();
            } else if (printType == 4) {
                sendData4(data, 0);
                LineFeed();
            } else if (printType == 5) {
                if (data.length() > 13)
                    data = data.substring(0, 13);
               /* if (sendDataBarCcode(data)) {
                    sendData("     ", 0);
                    LineFeedBC(data);
                }*/
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void LineFeed() {
        try {
            if (mmOutputStream == null) {
//                Toast.makeText(context, "Output Stream is Null", Toast.LENGTH_SHORT).show();
                _printStatus = "Output Stream is Null";
            } else {
                String msg = "     " + "\n" + "     ";
                msg = msg + "\n";
                mmOutputStream.write(msg.getBytes(), 0, msg.getBytes().length);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    static boolean sendData2(String Printitem, int size) throws IOException {
        try {
            String msg = Printitem;
            msg = msg + "\n";
            byte[] arrayOfByte1 = {27, 33, 0};

            byte[] format = {27, 33, 0};
            byte[] center = new byte[]{0x1b, 0x61, 0x01};

            // Bold
            format[2] = ((byte) (0x8 | arrayOfByte1[2]));

            if (mmOutputStream != null) {
                mmOutputStream.write(center);
                mmOutputStream.write(format);
                mmOutputStream.write(msg.getBytes(), 0, msg.getBytes().length);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    static boolean sendData3(String Printitem, int size) throws IOException {
        try {

            String msg = Printitem;
            msg = msg + "\n";

            byte[] format = {27, 33, 20};
            byte[] center = new byte[]{0x1b, 0x61, 0x01};

            if (mmOutputStream != null) {
                mmOutputStream.write(format);
                mmOutputStream.write(center);
                mmOutputStream.write(msg.getBytes(), 0, msg.getBytes().length);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    static boolean sendData4(String Printitem, int size) throws IOException {
        try {

            String msg = Printitem;
            msg = msg + "\n";
            byte[] arrayOfByte1 = {27, 33, 0};

            byte[] format = {27, 33, 0};

            //Bold
            //format[2] = ((byte) (0x8 | arrayOfByte1[2]));

            // Height
            //format[2] = ((byte) (0x10 | arrayOfByte1[2]));

            // Width
            //format[2] = ((byte) (0x20 | arrayOfByte1[2]));

            // Underline
            format[2] = ((byte) (0x80 | arrayOfByte1[2]));

            // Small
            //format[2] = ((byte) (0x1 | arrayOfByte1[2]));

            /*if (size == 0) {
                format[2] = ((byte) (0x1 | arrayOfByte1[2]));
                mmOutputStream.write(format);
            } else {
                format[2] = ((byte) (0x20 | arrayOfByte1[2]));
                mmOutputStream.write(format);
            }*/

            if (mmOutputStream != null) {
                mmOutputStream.write(format);
                mmOutputStream.write(msg.getBytes(), 0, msg.getBytes().length);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }


    static boolean sendData(String Printitem, int size) throws IOException {
        try {

            String msg = Printitem;
            msg = msg + "\n";

            byte[] format = {27, 33, 0};
            byte[] center = new byte[]{0x1b, 0x61, 0x01};

            if (mmOutputStream != null) {
                mmOutputStream.write(center);
                mmOutputStream.write(format);
                mmOutputStream.write(msg.getBytes(), 0, msg.getBytes().length);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }


    public static void closeBT() throws IOException {
        try {
            stopWorker = true;
            if (mmOutputStream != null) {
                mmOutputStream.close();
                mmInputStream.close();
                mmSocket.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
