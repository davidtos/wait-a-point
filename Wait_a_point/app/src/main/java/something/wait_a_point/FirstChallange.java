package something.wait_a_point;

import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.Switch;

import com.google.gson.Gson;

import java.util.Observable;
import java.util.Observer;

public class FirstChallange extends Activity implements Observer {


    private enum messageStrings {
        Black, White, RoundWon, Punish
    }

    Boolean isWhite;
    int Rounds;
    String player1name;
    String player2name;

    int player1Score = 0;
    int player2Score = 0;

    Boolean player1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_challange);
        SingleSocket.getInstance().AddObserver(this);

        player1name = getIntent().getExtras().getString("player1name");
        player2name = getIntent().getExtras().getString("player2name");
        player1 = getIntent().getExtras().getBoolean("player1");

        setBackgroundBlack();
        startRound();
    }

    public void startRound(){
        if(player1){
            ChangeBackgroundColorThread changeBackgroundColorThread = new ChangeBackgroundColorThread(this,8000,15000);
            Thread t = new Thread(changeBackgroundColorThread);
            t.start();
        }
    }

    private void SendToOtherPlayer(String Message){
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
        if(player1){
            SendToOtherPlayer(messageStrings.Black.toString());
        }
        setActivityBackgroundColor(getResources().getColor(android.R.color.black));
        isWhite = false;
    }

    public void setBackgroundWhite(){
        if(player1) {
            SendToOtherPlayer(messageStrings.White.toString());
        }
        setActivityBackgroundColor(getResources().getColor(android.R.color.white));
        isWhite = true;
    }

    public void setActivityBackgroundColor(int color) {
        View view = this.getWindow().getDecorView();
        view.setBackgroundColor(color);
    }

    public void ClickedWhite(View v) {
        if (isWhite){
            SendToOtherPlayer(messageStrings.RoundWon.toString());
            if (player1){
                player1Score++;
            }
            else{
                player2Score++;
            }
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
    }

    public void playerWonRound(){
        setBackgroundBlack();
        if (!player1){
            player1Score++;
        }
        else{
            player2Score++;
        }
        startRound();
    }

    public void playerPunish(){
        setBackgroundBlack();
        if (!player1){
            player1Score--;
        }
        else{
            player2Score--;
        }
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