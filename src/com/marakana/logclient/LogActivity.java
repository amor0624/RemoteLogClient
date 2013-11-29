package com.marakana.logclient;

import com.marakana.logservice.ILogService;
import com.marakana.logservice.Message;

import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteException;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class LogActivity extends Activity implements OnClickListener{
	private static final String TAG  = "LogActivity";
	ILogService logService;
	LogConnection conn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.log);
		
		// Request bind to the service
		conn = new LogConnection();
		Intent intent = new Intent("com.marakana.logservice.ILogService");
		intent.putExtra("version", "1.0");
		bindService(intent, conn, Context.BIND_AUTO_CREATE);
	
		// Attach listener to button
		((Button) findViewById(R.id.buttonClick)).setOnClickListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.log, menu);
		return true;
	}

	@Override
    protected void onDestroy() {
		super.onDestroy();
		Log.d(TAG, "onDestroyed");
		
		unbindService(conn);
		
		logService = null;
    }

	@Override
    public void onClick(View v) {
	    try {
	        logService.log_d("LogClient", "Hello from onClick()");
	        Message msg = new Message(Parcel.obtain());
	        msg.setTag("LogClient");
	        msg.setText("Hello from onClick() version 1.1");
	        logService.log(msg);
        } catch (RemoteException e) {
	        Log.e(TAG, "onClick failed", e);
        }
    }
	
	class LogConnection implements ServiceConnection {

		@Override
        public void onServiceConnected(ComponentName name, IBinder service) {
	       logService = ILogService.Stub.asInterface(service);
	       Log.i(TAG, "connected");
        }

		@Override
        public void onServiceDisconnected(ComponentName name) {
	        logService = null;
	        Log.i(TAG, "disconnected");
        }
		
	}

}
