package com.yihukurama.app.scanticketdemo.framework.v.showview;

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
import com.yihukurama.app.scanticketdemo.framework.v.CaptureActivity;
import com.yihukurama.app.scanticketdemo.framework.v.ViewfinderView;
import com.yihukurama.app.scanticketdemo.opensource.api.http.HttpApi;
import com.yihukurama.app.scanticketdemo.opensource.api.http.RequestHandler;
import com.yihukurama.app.scanticketdemo.opensource.api.http.responebean.YunduanPosBean;
import com.yihukurama.app.scanticketdemo.opensource.zxing.camera.CameraManager;
import com.yihukurama.app.scanticketdemo.opensource.zxing.decoding.CaptureActivityHandler;
import com.yihukurama.app.scanticketdemo.opensource.zxing.decoding.InactivityTimer;

import java.io.IOException;

/**
 * Initial the camera
 * @author Ryan.Tang
 */
public class YunduanExchangeCaptureActivity extends CaptureActivity implements View.OnClickListener {
	private String areadID;
	private String areadName;
	private Intent intent;
	private Context context;
	private Button lightOn;
	private Button lightOff;
	private TextView hint;

	private String qrCode;
	RequestHandler requestHandler;
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

		lightOn.setVisibility(View.INVISIBLE);

		requestHandler = new RequestHandler(this){
			@Override
			protected void yunduanPosAction(YunduanPosBean yunduanPosBean) {
				super.yunduanPosAction(yunduanPosBean);

				if (yunduanPosBean == null) return;
				if(yunduanPosBean.getOutresult().equals("error")){
					Toast.makeText(YunduanExchangeCaptureActivity.this, "服务器外层接口错误", Toast.LENGTH_SHORT).show();
					return;

				}

				if(yunduanPosBean.getOutdata().get(0).getResult().equals("ok")){
					Toast.makeText(YunduanExchangeCaptureActivity.this, "服务器相应成功，请查看日志；flag是"+yunduanPosBean.getOutdata().get(0).getFlag(), Toast.LENGTH_SHORT).show();

				}


			}
		};


	}



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
			Toast.makeText(YunduanExchangeCaptureActivity.this, "Scan failed!", Toast.LENGTH_SHORT).show();
			qrCode = null;
		}else {

			qrCode = resultString;


			HttpApi api = new HttpApi(this,requestHandler);
			api.yunduanPos("change",qrCode,"1");

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