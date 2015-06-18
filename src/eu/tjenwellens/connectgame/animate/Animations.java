/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.tjenwellens.connectgame.animate;

import android.view.View;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author tjen
 */
public abstract class Animations
{

    private static HashMap<View, Updater> updaters = new HashMap<View, Updater>();

    public static UpdateObservable getObservable(View view, long restMillis)
    {
        return getUpdater(view, restMillis);
    }

    private static Updater getUpdater(View view, long restMillis)
    {
        Updater u = updaters.get(view);
        if (u == null)
        {
            u = new Updater(view, restMillis);
            updaters.put(view, new Updater(view, restMillis));
        }
        return u;
    }

    public static void addTranslation(View view, long restMillis, Translatable translatable, float originX, float destinationX, float originY, float destinationY, long durationMillis)
    {
        Updater o = getUpdater(view, restMillis);
        o.registerObserver(new TranslateAnimater(o, translatable, originX, destinationX, originY, destinationY, durationMillis, restMillis));
        if (!o.isAlive())
        {
            o.start();
        }
    }

    public static void addTranslations(View view, long restMillis, List<UpdateObserver> updateObservers)
    {
        Updater o = getUpdater(view, restMillis);
        for (UpdateObserver updateObserver : updateObservers)
        {
            o.registerObserver(updateObserver);
        }
        if (!o.isAlive())
        {
            o.start();
        }
    }
}
