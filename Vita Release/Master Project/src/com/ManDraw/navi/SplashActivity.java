package com.ManDraw.navi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;

public class SplashActivity extends Activity {
    Utils wUtils = new Utils(this);
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        final View view = View.inflate(this, R.layout.activity_splash, null);

        setContentView(view);
        wUtils.updateIP();
        AlphaAnimation aa = new AlphaAnimation(1.0f, 1.0f);
        aa.setDuration(2000);
        view.startAnimation(aa);
        aa.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationEnd(Animation arg0) {
                redirectToLogin();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationStart(Animation animation) {
            }
        });

		
	}

    private void redirectToLogin() {
        Intent intentToLogin = new Intent(this, VitaLoginActivity.class);
        Intent intentToBrowse = new Intent(this, MainActivity.class);

        if (wUtils.getPreferencesBool("UserLogged")) {
            startActivity(intentToBrowse);
        } else {
            startActivity(intentToLogin);
        }
        finish();
    }
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.splash, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	

}
