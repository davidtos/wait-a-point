package something.wait_a_point;

import android.content.Context;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.google.gson.Gson;

/**
 * Created by Fatih on 23-10-2015.
 */
public class PopUpMenuEventHandle implements PopupMenu.OnMenuItemClickListener {
    Context context;
    String name;
    String from;
    public PopUpMenuEventHandle(Context context, String name, String from){
        this.context =context;
        this.name = name;
        this.from = from;
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        if (item.getItemId()== 1) {
            SendTo sendTo = new SendTo("Challenge",this.name,this.from,"Challenge for a game!");

            Gson gson = new Gson();
            String sendToInString = gson.toJson(sendTo);
            SingleSocket.getInstance().getsm().SendTo(sendToInString);
            Toast.makeText(context, "Uitdaging gestuurd naar "+this.name, Toast.LENGTH_SHORT).show();
            return true;
        }
        else
        {
            return false;
        }
    }
}
