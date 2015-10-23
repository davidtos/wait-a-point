package something.wait_a_point;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.gson.Gson;

import java.util.Observable;
import java.util.Observer;

public class MainActivity extends Activity implements Observer {
    SocketMessenger sm = new SocketMessenger();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sm.addObserver(this);
        sm.start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void Sendhoi(View v){
        sm.deleteObserver(this);
        Intent intent = new Intent(this, FirstChallange.class);
        startActivity(intent);
//        SendTo sendTo = new SendTo("SendTo","David","Fatih","Challange");
//
//        Gson gson = new Gson();
//        String sendToInString = gson.toJson(sendTo);
//        sm.addUser("David");
//        sm.SendTo(sendToInString);
   //   sm.send("hallo ik ben een android telefoon derp derp");
    }

    @Override
    public void update(Observable observable, Object data) {
        System.out.println(((Object[])data)[0].toString());
    }
}
