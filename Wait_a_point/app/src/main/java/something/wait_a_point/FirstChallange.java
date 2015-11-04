package something.wait_a_point;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.Switch;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.Observable;
import java.util.Observer;

public class FirstChallange extends Activity implements Observer {


    private enum messageStrings {
        Black, White, RoundWon, Punish
    }

    FirstChallange firstChallange;
    Boolean isWhite;
    int Rounds;
    String player1name;
    String player2name;
    TextView t;

    int player1Score = 0;
    int player2Score = 0;

    Boolean player1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_challange);
        firstChallange = this;
        SingleSocket.getInstance().AddObserver(firstChallange);
        t = (TextView) findViewById(R.id.textViewScore);
        player1name = getIntent().getExtras().getString("player1name");
        player2name = getIntent().getExtras().getString("player2name");
        player1 = getIntent().getExtras().getBoolean("player1");

        if(player1){
            SendToOtherPlayer(messageStrings.Black.toString());
        }
        setBackgroundBlack();
        startRound();
    }

    public void UpdateScore(){
        t.setText(Integer.toString(player1Score) + " - " + Integer.toString(player2Score));
    }

    public void startRound(){
        if(player1){
            ChangeBackgroundColorThread changeBackgroundColorThread = new ChangeBackgroundColorThread(this,3000,6000);
            Thread t = new Thread(changeBackgroundColorThread);
            t.start();
        }
    }

    public void SendToOtherPlayer(String Message){
        if(player1){
            SendTo sendTo = new SendTo("SendTo",player2name,player1name,Message);

            Gson gson = new Gson();
            String sendToInString = gson.toJson(sendTo);
            SingleSocket.getInstance().getsm().SendTo(sendToInString);
        }
        else{
            SendTo sendTo = new SendTo("SendTo",player1name,player2name,Message);

            Gson gson = new Gson();
            String sendToInString = gson.toJson(sendTo);
            SingleSocket.getInstance().getsm().SendTo(sendToInString);
        }
    }


    public void setBackgroundBlack(){
        setActivityBackgroundColor(getResources().getColor(android.R.color.black));
        isWhite = false;
    }

    public void setBackgroundWhite(){

        firstChallange.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                setActivityBackgroundColor(getResources().getColor(android.R.color.white));
                isWhite = true;
            }
        });
    }

    public void setActivityBackgroundColor(int color) {
        View view = this.getWindow().getDecorView();
        view.setBackgroundColor(color);
    }

    public void ClickedWhite(View v) {
        if (isWhite){
            setBackgroundBlack();
            SendToOtherPlayer(messageStrings.RoundWon.toString());
            if (player1){
                player1Score++;

            }
            else{
                player2Score++;
            }
            Rounds++;
            UpdateScore();
            startRound();
        }
        else{
            if (player1){
                player1Score--;
            }
            else{
                player2Score--;
            }
            SendToOtherPlayer(messageStrings.Punish.toString());
        }
        UpdateScore();
        Rounds++;
        if (Rounds == 10) {
            SingleSocket.getInstance().RemoveObserver(firstChallange);
            Intent intent = new Intent(firstChallange, Find.class);
            intent.putExtra("player1", player1);
            intent.putExtra("player1name", player1name);
            intent.putExtra("player2name", player2name);

            startActivity(intent);
        }
    }

    public void playerWonRound(){
        firstChallange.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                setBackgroundBlack();

                if (player1) {
                    SendToOtherPlayer(messageStrings.Black.toString());
                }
                if (!player1) {
                    player1Score++;
                } else {
                    player2Score++;
                }

                if (Rounds == 10) {
                    SingleSocket.getInstance().RemoveObserver(firstChallange);
                    Intent intent = new Intent(firstChallange, Find.class);
                    intent.putExtra("player1", player1);
                    intent.putExtra("player1name", player1name);
                    intent.putExtra("player2name", player2name);

                    startActivity(intent);
                }
                Rounds++;
                UpdateScore();
                startRound();

            }
        });





    }

    public void playerPunish(){
        //setBackgroundBlack();
        if (player1){
            player2Score--;
        }
        else{
            player1Score--;
        }

        firstChallange.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                UpdateScore();
            }
        });

    }


    @Override
    public void update(Observable observable, Object data) {
        System.out.println("update triggerd :" + ((Object[]) data)[0].toString());

        Gson gson = new Gson();
        SendTo received = gson.fromJson(((Object[]) data)[0].toString(),SendTo.class);
        System.out.println(received.getMessage());

        switch(received.getMessage()){
            case "White":
                setBackgroundWhite();
                break;
            case "Black":
                setBackgroundBlack();
                break;
            case "RoundWon":
                //the other player won
                playerWonRound();
                break;
            case "Punish":
                playerPunish();
                break;
        }
    }
}


//TODO Game rules/order
        /*
            Story
            1. screen opens
            2. if i am player1(challanger)
                2.1 than i tell player2 when his screen is white
            3. if i am player2(challanged) then i wait on the setWhite call
            4. when i recieve a message i check if  he won
                4.1 if i am player 1 and he won
                    4.1.1 i reset the timer
                    4.1.2 i will add a point to him
                4.2 if i am player 1 and he lost
                    4.2.1 i will remove 1 point from him

                4.3 if i am player 2 and he won
                    4.3.1 i will add a point to him
                4.4 if i am player 2 and he lost
                    4.4.1 i will remove 1 point from him
            5. if 5 rounds are played
                i will go to the find intent

         */