package something.wait_a_point;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.Observable;
import java.util.Observer;
import java.util.Random;

public class MainActivity extends Activity implements Observer {
  //  SocketMessenger sm = new SocketMessenger();
    RelativeLayout ll;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
       String name = getIntent().getStringExtra("Name");

        SingleSocket.getInstance().AddObserver(this);
        SingleSocket.getInstance().Start(name);
       // sm.addObserver(this);
      //  sm.start(name);
        ll = (RelativeLayout)findViewById(R.id.relativeLayout);
       //SingleSocket.getInstance().getsm().send("hallo ik ben een phone");
      //  SingleSocket.getInstance().getsm().send("David");
        //CreatePlayers("Fatih");
       // CreatePlayers("David");
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
        SingleSocket.getInstance().RemoveObserver(this);
        Intent intent = new Intent(this, FirstChallange.class);
        intent.putExtra("player1", true);
        intent.putExtra("player1name", "David");
        intent.putExtra("player2name", "David");

        startActivity(intent);

        SendTo sendTo = new SendTo("SendTo","David","Fatih","Challange");

        Gson gson = new Gson();
        String sendToInString = gson.toJson(sendTo);
        //sm.addUser("David");
        //sm.SendTo(sendToInString);
        //sm.send("hallo ik ben een android telefoon derp derp");
    }

    public void showPopUp(View view, String name){
        PopupMenu popupMenu=new PopupMenu(this,view);
        MenuInflater menuInflater=popupMenu.getMenuInflater();

        PopUpMenuEventHandle popUpMenuEventHandle=new PopUpMenuEventHandle(getApplicationContext(),name);
        popupMenu.setOnMenuItemClickListener(popUpMenuEventHandle);
        menuInflater.inflate(R.menu.challenge_menu, popupMenu.getMenu());

        popupMenu.getMenu().add(0, 0, 0, name);
        popupMenu.getMenu().add(0, 1, 0, " > Challenge");

        popupMenu.show();
    }

    public void TestWindowChallenge(View view) // delete this
    {
        Intent Challenge = new Intent(MainActivity.this,ChallengeWindow.class);
        startActivityForResult(Challenge, 1);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                    Toast.makeText(getApplicationContext(), data.getStringExtra("answer"), Toast.LENGTH_SHORT).show();
            } else if (resultCode == RESULT_CANCELED) {
                // declined
            }
        }
    }

    public void CreatePlayers(final String name)
    {
        runOnUiThread(new Runnable() {
            public void run() {
                Button myButton = new Button(getApplicationContext());
                myButton.setBackground(getApplicationContext().getResources().getDrawable(R.drawable.profileround, getTheme()));
                myButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showPopUp(v, name);
                    }
                });
                Random r = new Random();
                float x = r.nextInt(800 - 0);
                Random r2 = new Random();
                float y = r2.nextInt(1600 - 0);
                myButton.setX(x);
                myButton.setY(y);
                ll.addView(myButton, 200, 200);
            }
        });
    }

    @Override
    public void update(Observable observable, Object data) {
        System.out.println("update triggerd :" + ((Object[]) data)[0].toString());

        // TODO
        // controlleer of de verkregen OBJECT alle gebruikers zijn
        // ( object is instance of users ) ?
        // als dat zo is : refresh de spelers op de scherm
        //           CreatePlayers(((Object[])data)[0].toString());
        // else if kijken of er een uitdaging is verzonden
        //           Challenge window tevoorschijnhalen
        // als dat niet zo is: niks doen
    }
}
