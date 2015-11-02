package something.wait_a_point;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class ChallengeWindow extends Activity {

    String challeger;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_challenge_window);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int)(width*.7),(int)(height*.4));
        this.challeger = getIntent().getStringExtra("Challeger");

    }

    public void Accept(View v)
    {
        Intent intent = new Intent();
        intent.putExtra("Challeger",this.challeger);
        setResult(RESULT_OK, intent);
        finish();
    }

}
