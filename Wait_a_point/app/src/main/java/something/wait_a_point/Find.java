package something.wait_a_point;

import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.Observable;
import java.util.Observer;
import java.util.logging.Logger;

public class Find extends Activity implements Observer {

    //Set boolean flag when torch is turned on/off
    private boolean isFlashOn = false;
    //Create camera object to access flahslight
    private Camera camera;
    //Torch button
    private Button button;

    String player1name;
    String player2name;
    Boolean player1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find);
        SingleSocket.getInstance().AddObserver(this);

        player1name = getIntent().getExtras().getString("player1name");
        player2name = getIntent().getExtras().getString("player2name");
        player1 = getIntent().getExtras().getBoolean("player1");

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


    public void ClickedFlash(View v) {
        SendToOtherPlayer("Flash");
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (camera != null) {
            camera.release();
        }
    }

    @Override
    public void update(Observable observable, Object data) {
        System.out.println("update triggerd :" + ((Object[]) data)[0].toString());

        Gson gson = new Gson();
        SendTo received = gson.fromJson(((Object[]) data)[0].toString(),SendTo.class);
        System.out.println(received.getMessage());

        if(received.getMessage().equals("Flash")){

            //Context object to refer context of the application
            Context context = this;
            //Retrieve application packages that are currently installed
            //on the device which includes camera, GPS etc.
            PackageManager pm = context.getPackageManager();

            if(!pm.hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
                Logger message;
                Log.e("err", "Device has no camera!");
                //Toast a message to let the user know that camera is not
                //installed in the device
                Toast.makeText(getApplicationContext(),
                        "Your device doesn't have camera!", Toast.LENGTH_SHORT).show();
                //Return from the method, do nothing after this code block
                return;
            }
            camera = Camera.open();
            final Camera.Parameters p = camera.getParameters();

            //If Flag is set to true
            if (isFlashOn) {
                Log.i("info", "torch is turned off!");
                //Set the flashmode to off
                p.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
                //Pass the parameter ti camera object
                camera.setParameters(p);
                //Set flag to false
                isFlashOn = false;
                //Set the button text to Torcn-ON
                button.setText("Torch-ON");
            }
            //If Flag is set to false
            else {
                Log.i("info", "torch is turned on!");
                //Set the flashmode to on
                p.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
                //Pass the parameter ti camera object
                camera.setParameters(p);
//Set flag to true
                isFlashOn = true;
//Set the button text to Torcn-OFF
                button.setText("Torch-OFF");
            }
        }
    }
}
