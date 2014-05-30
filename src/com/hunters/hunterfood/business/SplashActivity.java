package com.hunters.hunterfood.business;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;

import com.hunters.hunterfood.R;

/**
 * Classe responsavel por iniciar a activity_splash
 * @author BrunoMeira
 *
 */
public class SplashActivity extends Activity {
	
	private static int SPLASH_TIME_OUT = 2000;
	
	/**
	 * Metodo responsavel por criar a tela
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {		
		super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
                                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_splash);
		
		new Handler().postDelayed(new Runnable() {
			public void run() {
				Intent i = new Intent(SplashActivity.this, MainActivity.class);
				startActivity(i);
				finish();
			}
		}, SPLASH_TIME_OUT);
	}
}