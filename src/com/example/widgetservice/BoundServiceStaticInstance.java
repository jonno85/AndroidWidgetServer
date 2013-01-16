package com.example.widgetservice;

import java.util.Random;
import java.util.Timer;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;

public class BoundServiceStaticInstance extends Service {

	public static final int TIMERATE = 500;
	private int seed;
	private Random r;
	boolean run = true;
	private Timer timer = new Timer("scheduled generator");
	private GeneratedValueResult result = new GeneratedValueResult();
	private IBoundServiceStaticInstance sInstance;

	private IBoundServiceStaticInstance.Stub apiEndPoint = 
			new IBoundServiceStaticInstance.Stub(){

				@Override
				public void putSeed(int seed) throws RemoteException {
				}

				@Override
				public void setServiceRunning(boolean value)
						throws RemoteException {
					// TODO Auto-generated method stub
					
				}

				@Override
				public IBoundServiceStaticInstance getInstance()
						throws RemoteException {
					return sInstance;
				}

				@Override
				public GeneratedValueResult getCurrentResult()
						throws RemoteException {
					// TODO Auto-generated method stub
					return null;
				}

				@Override
				public void addListener(IGeneratedValueListener listener)
						throws RemoteException {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void removeListener(IGeneratedValueListener listener)
						throws RemoteException {
					// TODO Auto-generated method stub
					
				}
		
	};

	@Override
	public void onCreate() {
		super.onCreate();
		sInstance = (IBoundServiceStaticInstance) this;
	}

	@Override
	public IBinder onBind(Intent intent) {
		return apiEndPoint;
	}

}
