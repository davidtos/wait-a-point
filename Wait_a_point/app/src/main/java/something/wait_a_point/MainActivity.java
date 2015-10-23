package something.wait_a_point;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.Observable;
import java.util.Observer;

public class MainActivity extends Activity implements Observer {
    SocketMessenger sm = new SocketMessenger();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
       String name = getIntent().getStringExtra("Name");

        sm.addObserver(this);
        sm.start(name);
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

    public void showPopUp(View view){
        PopupMenu popupMenu=new PopupMenu(this,view);
        MenuInflater menuInflater=popupMenu.getMenuInflater();

        PopUpMenuEventHandle popUpMenuEventHandle=new PopUpMenuEventHandle(getApplicationContext(),"persoon 1");
        popupMenu.setOnMenuItemClickListener(popUpMenuEventHandle);
        menuInflater.inflate(R.menu.challenge_menu, popupMenu.getMenu());
        popupMenu.show();
    }

    @Override
    public void update(Observable observable, Object data) {
        System.out.println(((Object[])data)[0].toString());
    }
}
