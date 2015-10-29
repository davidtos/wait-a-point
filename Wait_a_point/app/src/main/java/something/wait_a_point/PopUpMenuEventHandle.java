package something.wait_a_point;

import android.content.Context;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.PopupMenu;
import android.widget.Toast;

/**
 * Created by Fatih on 23-10-2015.
 */
public class PopUpMenuEventHandle implements PopupMenu.OnMenuItemClickListener {
    Context context;
    String name;
    public PopUpMenuEventHandle(Context context, String name){
        this.context =context;
        this.name = name;
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        if (item.getItemId()== 1) {
            Toast.makeText(context, "Uitdaging gestuurd naar "+this.name, Toast.LENGTH_SHORT).show();
            return true;
        }
        return false;
    }
}
