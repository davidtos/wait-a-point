package something.wait_a_point;

import android.os.Bundle;
import android.app.Activity;
import android.view.View;

public class FirstChallange extends Activity {

    Boolean isWhite;
    int Rounds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_challange);
        setBackgroundBlack();
        ChangeBackgroundColorThread changeBackgroundColorThread = new ChangeBackgroundColorThread(this,5000,6000);
        Thread t = new Thread(changeBackgroundColorThread);
        t.start();
    }

    public void setBackgroundBlack(){
        setActivityBackgroundColor(getResources().getColor(android.R.color.black));
        isWhite = false;
    }

    public void setBackgroundWhite(){
        setActivityBackgroundColor(getResources().getColor(android.R.color.white));
        isWhite = true;
    }

    public void setActivityBackgroundColor(int color) {
        View view = this.getWindow().getDecorView();
        view.setBackgroundColor(color);
    }

    public void ClickedWhite(View v) {

        if (isWhite){

        }
    }


}
