package com.xcheng.serialportsample;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import android.util.Log;

import com.xcheng.serialportsample.R;

public class MainActivity extends Activity implements View.OnClickListener, PortManager.PortManagerListener, PortManagerGS0.PortManagerGS0Listener {
    public static final String TAG = "serialportsample";
    private static final String PROP_USB_ACM = "persist.vendor.radio.port_index";

    private PortManager mPortManager;
    private PortManagerGS0 mPortManagerGS0;

    private LinearLayout mRoot;
    private Button mOpen;
    private Button mClose;
    private Button mConnect;
    private Button mDisConnect;
    private Button mSend;
    private Button mRecv;
    private Button mReset;
    private Button mCancelRecv;
    private Button mNonBlockRecv;
    private TextView mPortManagerRecvData;
    private TextView mPortManagerGS0RecvData;
//    byte[] var1 = new byte[]{0x17,0x18,0x19,0x20,0x21,0x22};
    byte[] var1 = new byte[]{0x01,0x00,0x02,0x45,0x39,0x7E};
//    byte[] var1 = new byte[]{0x31,0x32};
    int var2 = 2048;
    private static final String deviceNameUSB = "/dev/ttyGS0";
    private static final String deviceNameSP = "/dev/ttyS1";
    private static final int MSG_GS_TO_S = 1;
    private static final int MSG_S_TO_GS = 2;
    private HandlerThread mHandlerThread;
    private ReadWriteHandler mReadWriteHandler;
    private boolean mThreadFlag = true;
    private Handler mHandler = new Handler();

    private Context mContext;

    class ReadWriteHandler extends Handler {

        public ReadWriteHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_GS_TO_S:
                    new Thread(new Runnable(){
                        public void run() {
                            byte[] recvNonBlockData = null;
                            while(mThreadFlag){
                                recvNonBlockData = mPortManager.recv(2048);
                                if (recvNonBlockData != null) {
                                    String rec = bytesToHexString(recvNonBlockData);
                                    Log.d(TAG, "TtyJni recvNonBlockData = " + rec);
                                    mHandler.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            mPortManagerRecvData.setText(rec);
                                            /*if (rec.equals("010003FF0104F9")) {
                                                onPortSend(hexStringToByteArray("06"));
                                                try {
                                                    Thread.sleep(100);
                                                } catch (InterruptedException e) {
                                                    throw new RuntimeException(e);
                                                }
                                                onPortSend(hexStringToByteArray("0100085A0104DF850230300F"));
                                                try {
                                                    Thread.sleep(500);
                                                } catch (InterruptedException e) {
                                                    throw new RuntimeException(e);
                                                }
                                                onPortSend(hexStringToByteArray("01000245397E"));
                                            } else {
                                                onPortSend(hexStringToByteArray("01000245397E"));
                                            }*/
                                        }
                                    });
                                } else {
                                    Log.d(TAG, "recvNonBlockData is null ");
                                    mHandler.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            mPortManagerRecvData.setText("recvNonBlockData is null ");
                                        }
                                    });
                                }
                                /*Log.d(TAG, "WUDI sleep +++++++ ");
                                try {
                                    Thread.sleep(10000);
                                } catch (InterruptedException e) {
                                    throw new RuntimeException(e);
                                }
                                Log.d(TAG, "WUDI sleep -------");*/
                                /*recvNonBlockData = mPortManager.recvNonBlock();
                                if(recvNonBlockData != null){
                                    String rec = bytesToHexString(recvNonBlockData);
                                    Log.d(TAG, "recvNonBlockData = " + rec);
                                    mHandler.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            mPortManagerRecvData.setText(rec);
                                        }
                                    });
                                } else {
                                    Log.d(TAG, "recvNonBlockData is null ");
                                    mHandler.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            mPortManagerRecvData.setText("recvNonBlockData is null ");
                                        }
                                    });
                                }*/
                            }
                        }
                    }).start();
                    break;
                case MSG_S_TO_GS:
                    break;
                default:
                    break;
            }
        }
    }

    public static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i + 1), 16));
        }
        return data;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this.getApplicationContext();
        initLayout();
    }
    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause");
    }

    @Override
    protected void onResume() {
        super.onResume();
        mHandlerThread = new HandlerThread(TAG);
        mHandlerThread.start();
        mReadWriteHandler = new ReadWriteHandler(mHandlerThread.getLooper());
        mPortManager = new PortManager(this,this);
        mPortManager.onPortStart();
        mPortManagerGS0 = new PortManagerGS0(this,this);
        mPortManagerGS0.onPortStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
        mThreadFlag = false;
        onPortClose();
        onPortCloseGS();
        onPortDisconnect();
        onPortDisconnectGS();
        mPortManager.onPrinterStop();
        mPortManagerGS0.onPrinterStop();
    }

    @Override
    public void onServiceConnected() {

    }

    @Override
    public void onServiceConnectedGS0() {

    }

    private void initLayout(){
        mRoot = (LinearLayout) this.findViewById(R.id.content_view);
        mOpen = (Button) mRoot.findViewById(R.id.id_port_open);
        mClose = (Button)mRoot.findViewById(R.id.id_port_close);
        mSend = (Button) mRoot.findViewById(R.id.id_port_send);
        mRecv = (Button) mRoot.findViewById(R.id.id_port_recv);

        mReset = (Button)mRoot.findViewById(R.id.id_port_reset);
        mCancelRecv = (Button) mRoot.findViewById(R.id.id_port_cancelrecv);
        mNonBlockRecv = (Button) mRoot.findViewById(R.id.id_port_nonblockrecv);

        mConnect = (Button)mRoot.findViewById(R.id.id_port_connect);
        mDisConnect = (Button)mRoot.findViewById(R.id.id_port_disconnect);

        mPortManagerRecvData = (TextView) this.findViewById(R.id.tv_portmanager_recvdata);
        mPortManagerGS0RecvData = (TextView) this.findViewById(R.id.tv_portmanagergs0_recvdata);

        mOpen.setOnClickListener(this);
        mClose.setOnClickListener(this);
        mSend.setOnClickListener(this);
        mRecv.setOnClickListener(this);
//        mRecv.setVisibility(View.GONE);
        mReset.setOnClickListener(this);
        mCancelRecv.setOnClickListener(this);
        mNonBlockRecv.setOnClickListener(this);
        mConnect.setOnClickListener(this);
        mDisConnect.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch(id){
            case R.id.id_port_open:
                onPortOpen();
                onPortGSOpen();
                mReadWriteHandler.sendEmptyMessage(MSG_GS_TO_S);
                break;
            case R.id.id_port_close:
                onPortDisconnect();
                onPortDisconnectGS();
                onPortClose();
                onPortCloseGS();
//                finish();
                break;
            case R.id.id_port_send:
//                mReadWriteHandler.sendEmptyMessage(MSG_GS_TO_S);
                onPortSend(var1);
                onPortSendGS(var1);
                break;
            case R.id.id_port_recv:
                //onPortRecv(var2);
                break;
            case R.id.id_port_reset:
                //onPortReset();
                break;
            case R.id.id_port_cancelrecv:
                //onPortCancelRecv();
                break;
            case R.id.id_port_nonblockrecv:
                //onPortNonBlockRecv();
                //onPortNonBlockRecvNew();
                break;
            case R.id.id_port_connect:
                onPortDisconnect();
                onPortDisconnectGS();
                onPortConnect(deviceNameUSB);
                onPortGSConnect(deviceNameSP);
                mReadWriteHandler.sendEmptyMessage(MSG_GS_TO_S);
                break;
            case R.id.id_port_disconnect:
                onPortDisconnect();
                onPortDisconnectGS();
                break;
        }
    }

    private void onPortOpen(){
        mPortManager.open();
    }

    private void onPortGSOpen(){
        mPortManagerGS0.open();
    }

    private void onPortConnect(String port){
        mPortManager.connect(port);
    }

    private void onPortGSConnect(String port){
        mPortManagerGS0.connect(port);
    }

    private void onPortClose(){
        mPortManager.close();
    }

    private void onPortCloseGS(){
        mPortManagerGS0.close();
    }

    private void onPortDisconnect(){
        if (mPortManager.getConnectStatus() != 0){
            mPortManager.disconnect();
        }
    }

    private void onPortDisconnectGS(){
        if (mPortManagerGS0.getConnectStatus() != 0){
            mPortManagerGS0.disconnect();
        }
    }

    private void onPortSend(byte[] var){
        mPortManager.send(var);
    }

    private void onPortSendGS(byte[] var){
        mPortManagerGS0.send(var);
    }

    private void onPortRecv(int var){
        byte[] recvData = null;
        recvData = mPortManager.recv(var);
        Toast toast=Toast.makeText(MainActivity.this,bytesToHexString(recvData),Toast.LENGTH_SHORT);
        toast.show();
        mPortManager.send("Reply".getBytes());
    }

    private void onPortReset(){
       mPortManager.reset();
    }

     private void onPortCancelRecv(){
        mPortManager.cancelRecv();
    }

    private void onPortNonBlockRecv(){
       byte[] recvNonBlockingData = null;
       recvNonBlockingData = mPortManager.recvNonBlocking();
       Toast toast=Toast.makeText(MainActivity.this,bytesToHexString(recvNonBlockingData),Toast.LENGTH_SHORT);
       toast.show();
    }

    private void onPortNonBlockRecvNew(){
       byte[] recvNonBlockingData = null;
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        recvNonBlockingData = mPortManager.recvNonBlock();
       Toast toast=Toast.makeText(MainActivity.this,bytesToHexString(recvNonBlockingData),Toast.LENGTH_SHORT);
       toast.show();
    }

    public String bytesToHexString(byte[] bArr) {
        StringBuffer sb = new StringBuffer(bArr.length);
        String sTmp;

        for (int i = 0; i < bArr.length; i++) {
            sTmp = Integer.toHexString(0xFF & bArr[i]);
            if (sTmp.length() < 2)
                sb.append(0);
            sb.append(sTmp.toUpperCase());
        }

        return sb.toString();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        // getMenuInflater().inflate(R.menu.menu_main, menu);
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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}