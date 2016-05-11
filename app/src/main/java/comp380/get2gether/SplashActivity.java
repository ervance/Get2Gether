package comp380.get2gether;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

/**
 * Created by olgak on 4/14/16.
 */
public class SplashActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen_layout);

        final ImageView logo = (ImageView) findViewById(R.id.imageLogo);
        Animation rotation = AnimationUtils.loadAnimation(getBaseContext(), R.anim.fadein);
        final Animation fade = AnimationUtils.loadAnimation(getBaseContext(),R.anim.abc_fade_out);

        logo.startAnimation(rotation);
        rotation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }
            @Override
            public void onAnimationEnd(Animation animation) {
                logo.startAnimation(fade);
                finish();
                Intent i = new Intent(getBaseContext(), LoginActivity.class);
                startActivity(i);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }

            });
    }
}
