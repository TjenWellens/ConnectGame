package eu.tjenwellens.connectgame;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Toast;

/**
 *
 * @author tjen
 */
public class MessageHandler extends Handler
{

    private Context context;
    private View view;

    public MessageHandler(Context context, View view)
    {
        this.context = context;
        this.view = view;
    }

    @Override
    public void handleMessage(Message msg)
    {
        switch (msg.what)
        {
            case 0:
                // repaint, needs to be done in another thread?
                view.invalidate();
                break;
            // Messages need to be shown in another thread?
            case 1:
                Toast.makeText(context, R.string.crossMessage, Toast.LENGTH_SHORT).show();
                break;
            case 2:
                Toast.makeText(context, R.string.circleMessage, Toast.LENGTH_SHORT).show();
                break;
            case 3:
                Toast.makeText(context, R.string.drawMessage, Toast.LENGTH_SHORT).show();
                break;
            case 4:
                Toast.makeText(context, R.string.wrongPlaceMessage, Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }

        super.handleMessage(msg);
    }
}
