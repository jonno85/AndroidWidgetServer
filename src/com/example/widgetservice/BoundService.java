package com.example.widgetservice;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

public class BoundService extends Service {

	private static final String TAG = BoundService.class.getSimpleName();
	public static final int TIMERATE = 500;

	private int seed;
	Random r;
	boolean run = true;
	private Timer timer = new Timer("scheduled generator");
	private GeneratedValueResult result = new GeneratedValueResult();
	//private ScheduledExecutorService ex;
	
	//to synchronized operation on result variable
	private final Object valueResultLock = new Object();
	
	private List<IGeneratedValueListener> listeners = 
					new ArrayList<IGeneratedValueListener>();

	private IBoundService.Stub apiEndpoint = new IBoundService.Stub() {
		
		@Override
		public void setServiceRunning(boolean value) throws RemoteException {
			synchronized(this){
				BoundService.this.run = value;
				timer.purge();
				if(value){
					Log.e(TAG, "boot");
					startExcutor();
				}
			}
		}
		
		@Override
		public void removeListener(IGeneratedValueListener listener)
				throws RemoteException {
			synchronized(listeners){
				listeners.remove(listener);
			}
		}
		
		@Override
		public void putSeed(int seed) throws RemoteException {
			BoundService.this.seed = seed;
			//Log.e("TIMER", "CHIAMATO PUTSEED");
			timer.purge();
			startExcutor();
		}
		
		@Override
		public int getValue() throws RemoteException {
			return result.getValue(); 
		}
		
		@Override
		public GeneratedValueResult getCurrentResult() throws RemoteException {
			synchronized(valueResultLock){
				return result;
			}
		}
		
		@Override
		public void addListener(IGeneratedValueListener listener)
				throws RemoteException {
			synchronized(listeners){
				listeners.add(listener);
			}
		}
		
		private void startExcutor(){
			timer.schedule(getNewTimerTask(), 0);
		}
	};

	@Override
	public IBinder onBind(Intent intent) {
		return apiEndpoint;
	}

	private TimerTask getNewTimerTask(){
		return new TimerTask() {
			
			@Override
			public void run() {
				
				r = new Random(seed);
				result.setSeed(seed);
				result.setnGener(result.getnGener() + 1); 
				while(run){
					result.setValue(r.nextInt());
					synchronized(this){
						try {
							this.wait(TIMERATE);
							//fibJ(37);
						} catch (InterruptedException e1) {
							e1.printStackTrace();
						}
					}
					synchronized(listeners){
						for (IGeneratedValueListener listener : listeners) {
							try {
								listener.handleGeneratedValue();
							} catch (RemoteException e) {
								Log.w(TAG, "Failed to notify listener " + listener, e);
							}
						}
					}
				}
			}
		};
	}

	public long fibJ(long n) { // 
		if(run){
			if (n <= 0)
				return 0;
			if (n == 1)
				return 1;
			return fibJ(n - 1) + fibJ(n - 2);
		}
		return 0;
	}
}
