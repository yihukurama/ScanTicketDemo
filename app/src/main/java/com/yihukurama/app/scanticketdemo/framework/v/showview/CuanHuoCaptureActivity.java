package com.yihukurama.app.scanticketdemo.framework.v.showview;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.Result;
import com.iecheck.app.scanticketdemo.R;
import com.yihukurama.app.scanticketdemo.opensource.zxing.camera.CameraManager;
import com.yihukurama.app.scanticketdemo.opensource.zxing.decoding.CaptureActivityHandler;
import com.yihukurama.app.scanticketdemo.opensource.zxing.decoding.InactivityTimer;
import com.wilddog.client.ChildEventListener;
import com.wilddog.client.DataSnapshot;
import com.wilddog.client.ValueEventListener;
import com.wilddog.client.Wilddog;
import com.wilddog.client.WilddogError;
import com.yihukurama.app.scanticketdemo.framework.v.CaptureActivity;
import com.yihukurama.app.scanticketdemo.framework.v.ViewfinderView;

import java.io.IOException;

/**
 * Initial the camera
 * @author Ryan.Tang
 */
public class CuanHuoCaptureActivity extends CaptureActivity implements View.OnClickListener {
	private String areadID;
	private String areadName;
	private Intent intent;
	private Context context;
	private Button lightOn;
	private Button lightOff;
	private TextView hint;

	private String qrCode;
	Wilddog refTicket;
	Wilddog be;
	Wilddog user1;
	Wilddog user2;

	int currentNum;
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.more_cuanhuobangding_capture);
		context=this;
		//ViewUtil.addTopView(getApplicationContext(), this, R.string.scan_card);
		CameraManager.init(getApplication());
		viewfinderView = (ViewfinderView) findViewById(R.id.viewfinder_view);
		lightOn = (Button) findViewById(R.id.MCC_light_on);
		lightOff = (Button) findViewById(R.id.MCC_light_off);
		hint = (TextView) findViewById(R.id.MCC_hint_tv);

		hasSurface = false;
		inactivityTimer = new InactivityTimer(this);
		initBeepSound();
		lightOn.setOnClickListener(this);
		lightOff.setOnClickListener(this);

		areadID = getIntent().getStringExtra("SALE_AREA_ID");
		areadName = getIntent().getStringExtra("SALE_AREA");

		try {
			hint.setText(getResources().getString(R.string.chbd_capture_hint).replace("#name#", areadName));
		}catch (Exception e){
			e.printStackTrace();
			hint.setText(getResources().getString(R.string.chbd_capture_hint_noname));
		}

		/*new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				lightOn.setEnabled(true);
			}

		}, 1500);*/
		lightOn.setVisibility(View.INVISIBLE);
		refTicket = new Wilddog("https://ticketonline.wilddogio.com/5191");

		be = new Wilddog("https://ticketonline.wilddogio.com/5230/be");
		user1 = new Wilddog("https://ticketonline.wilddogio.com/5230/user1");
		user2 = new Wilddog("https://ticketonline.wilddogio.com/5230/user2");
		be.addValueEventListener(val2);

	}


	ValueEventListener val2 = new ValueEventListener() {
		public void onDataChange(DataSnapshot snapshot) {

			currentNum = Integer.parseInt(snapshot.getValue().toString());
			System.out.println(currentNum);

		}

		public void onCancelled(WilddogError error) {
			if (error != null) {
				System.out.println(error.getCode());
			}
		}
	};

		ValueEventListener val3 = new ValueEventListener(){
		public void onDataChange(DataSnapshot snapshot){

			if(!snapshot.exists()){
				//qrCode节点不存在
				Toast.makeText(CuanHuoCaptureActivity.this, "该卡券二维码不合法或已被兑换", Toast.LENGTH_SHORT).show();
				System.out.println("该卡券二维码不合法或已被兑换");

				((Activity)context).finish();

				intent = new Intent(CuanHuoCaptureActivity.this,
						ResultActivity.class);
				intent.putExtra("DATA","该卡券二维码不合法或已被兑换");
				startActivity(intent);
				return;
			}

			refTicket.addChildEventListener(cval);
			refTicket.child(qrCode).removeValue();

		}

		public void onCancelled(WilddogError error){
			if(error != null){
				System.out.println(error.getCode());
			}
		}

	};


	ChildEventListener cval = new ChildEventListener() {
		@Override
		public void onChildAdded(DataSnapshot dataSnapshot, String s) {

		}

		@Override
		public void onChildChanged(DataSnapshot dataSnapshot, String s) {

		}

		@Override
		public void onChildRemoved(DataSnapshot dataSnapshot) {

			Toast.makeText(CuanHuoCaptureActivity.this, "兑换"+dataSnapshot.getValue(), Toast.LENGTH_SHORT).show();
			System.out.println("兑换"+dataSnapshot.getValue());
			currentNum++;
			be.setValue(currentNum+"");
			user1.setValue(0.02*currentNum+"");
			user2.setValue(0.02*currentNum+"");

			((Activity)context).finish();
			intent = new Intent(CuanHuoCaptureActivity.this,
					ResultActivity.class);
			intent.putExtra("DATA",dataSnapshot.getValue().toString());
			startActivity(intent);

			refTicket.removeEventListener(cval);
			refTicket.child(qrCode).removeEventListener(val3);
		}

		@Override
		public void onChildMoved(DataSnapshot dataSnapshot, String s) {

		}

		@Override
		public void onCancelled(WilddogError wilddogError) {
		}
	};

	/**
	 * Handler scan result
	 * @param result
	 * @param barcode
	 */
	public void handleDecode(Result result, Bitmap barcode) {
		inactivityTimer.onActivity();
		playBeepSoundAndVibrate();
		String resultString = result.getText();
		//FIXME
		if (resultString.equals("")) {
			Toast.makeText(CuanHuoCaptureActivity.this, "Scan failed!", Toast.LENGTH_SHORT).show();
			qrCode = null;
		}else {

			qrCode = resultString;

			refTicket.child(qrCode).addListenerForSingleValueEvent(val3);

			Intent resultIntent = new Intent();
			Bundle bundle = new Bundle();
			bundle.putString("result", resultString);
			resultIntent.putExtras(bundle);
			this.setResult(RESULT_OK, resultIntent);

		}

	}
	
	private void initCamera(SurfaceHolder surfaceHolder) {
		try {
			CameraManager.get().openDriver(surfaceHolder);
		} catch (IOException ioe) {
			return;
		} catch (RuntimeException e) {
			return;
		}
		if (handler == null) {
			handler = new CaptureActivityHandler(this, decodeFormats,
					characterSet);
		}
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {

	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		if (!hasSurface) {
			hasSurface = true;
			initCamera(holder);
		}

	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		hasSurface = false;

	}

	public ViewfinderView getViewfinderView() {
		return viewfinderView;
	}

	public Handler getHandler() {
		return handler;
	}

	public void drawViewfinder() {
		viewfinderView.drawViewfinder();

	}



	private void initBeepSound() {
		if (playBeep && mediaPlayer == null) {
			// The volume on STREAM_SYSTEM is not adjustable, and users found it
			// too loud,
			// so we now play on the music stream.
			setVolumeControlStream(AudioManager.STREAM_MUSIC);
			mediaPlayer = new MediaPlayer();
			mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
			mediaPlayer.setOnCompletionListener(beepListener);

			AssetFileDescriptor file = getResources().openRawResourceFd(
					R.raw.beep);
			try {
				mediaPlayer.setDataSource(file.getFileDescriptor(),
						file.getStartOffset(), file.getLength());
				file.close();
				mediaPlayer.setVolume(BEEP_VOLUME, BEEP_VOLUME);
				mediaPlayer.prepare();
			} catch (IOException e) {
				mediaPlayer = null;
			}
		}
	}

	private static final long VIBRATE_DURATION = 200L;

	private void playBeepSoundAndVibrate() {
		if (playBeep && mediaPlayer != null) {
			mediaPlayer.start();
		}
		if (vibrate) {
			Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
			vibrator.vibrate(VIBRATE_DURATION);
		}
	}

	/**
	 * When the beep has finished playing, rewind to queue up another one.
	 */
	private final OnCompletionListener beepListener = new OnCompletionListener() {
		public void onCompletion(MediaPlayer mediaPlayer) {
			mediaPlayer.seekTo(0);
		}
	};



	@Override
	public void onClick(View view) {
		switch (view.getId()) {
			case R.id.icheckapp_supershoppingme_leftbtn:
				finish();
				break;
			case R.id.MCC_light_on:// 手电筒
				CameraManager.get().openLight();
				lightOn.setVisibility(View.GONE);
				lightOff.setVisibility(View.VISIBLE);

				break;
			case R.id.MCC_light_off:
				CameraManager.get().offLight();// 关闭手电筒
				lightOn.setVisibility(View.VISIBLE);
				lightOff.setVisibility(View.GONE);

				break;
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK ) {
			finish();
			return true;
		} else
			return super.onKeyDown(keyCode, event);
	}

}