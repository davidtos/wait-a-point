package something.wait_a_point;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.IOException;
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
    Camera mCam;
    String player1name;
    String player2name;
    Boolean player1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find);
        SingleSocket.getInstance().AddObserver(this);
        mCam = Camera.open();
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

    Camera cam = null;
    public void turnOnFlashLight() {
        try {
            if (getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)) {
                cam = Camera.open();
                Camera.Parameters p = cam.getParameters();
                p.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
                cam.setParameters(p);
                cam.startPreview();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getBaseContext(), "Exception throws in turning on flashlight.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void update(Observable observable, Object data) {
        System.out.println("update triggerd :" + ((Object[]) data)[0].toString());

        Gson gson = new Gson();
        SendTo received = gson.fromJson(((Object[]) data)[0].toString(),SendTo.class);
        System.out.println(received.getMessage());

        if(received.getMessage().equals("Flash")){

            if(!isFlashOn){

                Camera.Parameters p = mCam.getParameters();
                p.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
                mCam.setParameters(p);
                SurfaceTexture mPreviewTexture = new SurfaceTexture(0);

                try {
                    mCam.setPreviewTexture(mPreviewTexture);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                mCam.startPreview();
                //mCam.release();
                isFlashOn = true;
            }
            else{
              //  Camera mCam = Camera.open();
                Camera.Parameters p = mCam.getParameters();
                p.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
                mCam.setParameters(p);
                SurfaceTexture mPreviewTexture = new SurfaceTexture(0);

                try {
                    mCam.setPreviewTexture(mPreviewTexture);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                mCam.startPreview();
             //   mCam.release();
                isFlashOn = false;
            }

        }
    }
}
