/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.tjenwellens.connectgame.animate;

import android.view.View;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author tjen
 */
class Updater extends Thread implements UpdateObservable
{

    private volatile boolean listEmpty = true;
    private volatile boolean running = true;
    private View view;
    private long restMillis = 100;
    private List<UpdateObserver> updateObservers = new ArrayList<UpdateObserver>();

    public Updater(View view, long restMillis)
    {
        this.view = view;
        this.restMillis = restMillis;
    }

    public long getRestMillis()
    {
        return restMillis;
    }

    @Override
    public synchronized void registerObserver(UpdateObserver updateObserver)
    {
        updateObservers.add(updateObserver);
        listEmpty = false;
    }

    @Override
    public synchronized void removeObserver(UpdateObserver updateObserver)
    {
        updateObservers.remove(updateObserver);
        if (updateObservers.isEmpty())
        {
            listEmpty = true;
        }
    }

    public void run()
    {
        while (running && !listEmpty)
        {
            for (UpdateObserver updateObserver : updateObservers)
            {
                updateObserver.update();
            }
            try
            {

                Thread.sleep(restMillis);

            } catch (InterruptedException ex)
            {
            }
            view.postInvalidate();
        }
    }
}
