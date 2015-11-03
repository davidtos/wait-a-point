package something.wait_a_point;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.Observable;
import java.util.Observer;
import java.util.Random;

public class MainActivity extends Activity implements Observer {
    //  SocketMessenger sm = new SocketMessenger();
    RelativeLayout rl;
    String username;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        username = getIntent().getStringExtra("Name");

        SingleSocket.getInstance().AddObserver(this);
        SingleSocket.getInstance().Start(username);
        rl = (RelativeLayout)findViewById(R.id.relativeLayout);

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

        PopUpMenuEventHandle popUpMenuEventHandle=new PopUpMenuEventHandle(getApplicationContext(),name, username);
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
                //accepted
                SendTo sendTo = new SendTo("ChallengeAnswer",data.getStringExtra("Challeger"),this.username,"Accept");

                Gson gson = new Gson();
                String sendToInString = gson.toJson(sendTo);
                SingleSocket.getInstance().getsm().SendTo(sendToInString);

                SingleSocket.getInstance().RemoveObserver(this);
                Intent intent = new Intent(this, FirstChallange.class);
                // inviter is player 1
                String p1 = data.getStringExtra("Challeger");
                // i am getting invited for a game = this.user is player 2
                String p2 = this.username;
                intent.putExtra("player1", false);
                intent.putExtra("player1name", p1);
                intent.putExtra("player2name", p2);

                startActivity(intent);
            } else if (resultCode == RESULT_CANCELED) {
                // declined
                SendTo sendTo = new SendTo("ChallengeAnswer",data.getStringExtra("Challeger"),this.username,"Decline");

                Gson gson = new Gson();
                String sendToInString = gson.toJson(sendTo);
                SingleSocket.getInstance().getsm().SendTo(sendToInString);
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
                rl.addView(myButton, 200, 200);
            }
        });
    }

    @Override
    public void update(Observable observable, Object data) {
        System.out.println("update triggerd :" + ((Object[]) data)[0].toString());
        Gson gson = new Gson();

        try {
            JsonParser parser = new JsonParser();
            JsonObject obj = parser.parse(((Object[]) data)[0].toString()).getAsJsonObject();
            Object typenode = obj.get("type");

            if (typenode  != null) // type node doesn't exist in message
            {
                String type = typenode.toString().substring(1,typenode.toString().length()-1); // trim dubble quotes (node value is wrapped in dubble quotes)
                if (type.equals("NewUser")) {
                    SendAll received = gson.fromJson(((Object[]) data)[0].toString(), SendAll.class);
                    String fromUser = received.getFrom();
                    //if (!fromUser.equals(username)){// its myself, don't update
                    CreatePlayers(received.getFrom());
                    //}
                    System.out.println(received.getMessage());
                }
                else if(type.equals("Challenge"))
                {
                    SendTo received = gson.fromJson(((Object[]) data)[0].toString(), SendTo.class);
                    Intent Challenge = new Intent(MainActivity.this,ChallengeWindow.class);
                    Challenge.putExtra("Challeger",received.getFrom());
                    startActivityForResult(Challenge, 1);
                }
                else if(type.equals("ChallengeAnswer"))
                {
                    SendTo received = gson.fromJson(((Object[]) data)[0].toString(), SendTo.class);
                    // accepted
                    String answer = received.getMessage();
                    if(answer.equals("Accept"))
                    {

                        SingleSocket.getInstance().RemoveObserver(this);
                        Intent intent = new Intent(this, FirstChallange.class);
                        // from = sender = game inviter = player 1
                        String p1 = received.getFrom();
                        // to = receiver = game accepter player 2
                        String p2 = received.getTo();
                        intent.putExtra("player1", true);
                        intent.putExtra("player1name", p2);
                        intent.putExtra("player2name", p1);

                        startActivity(intent);

                        runOnUiThread(new Runnable() {
                            public void run() {
                                Toast.makeText(MainActivity.this, "Accepted by other player", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                    else
                    {

                        runOnUiThread(new Runnable() {
                            public void run() {
                                Toast.makeText(MainActivity.this, "Declined by other player", Toast.LENGTH_SHORT).show();
                            }});
                    }

                    // declined
                }
            }
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }

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