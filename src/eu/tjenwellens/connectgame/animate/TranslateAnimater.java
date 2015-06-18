/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.tjenwellens.connectgame.animate;

import android.os.SystemClock;

/**
 *
 * @author tjen
 */
class TranslateAnimater implements UpdateObserver
{

    private UpdateObservable uo;
    private Translatable translatable;
    private float originX, destinationX, originY, destinationY;
    private long updateFrequency;
    private long prevTimestamp = -1;
    private long timeFinish;

    public TranslateAnimater(UpdateObservable uo, Translatable translatable, float originX, float destinationX, float originY, float destinationY, long durationMillis, long frequencyMillis)
    {
        this.uo = uo;
        this.translatable = translatable;
        this.originX = originX;
        this.destinationX = destinationX;
        this.originY = originY;
        this.destinationY = destinationY;
        this.updateFrequency = frequencyMillis;
        long now = SystemClock.uptimeMillis();
        this.timeFinish = now + durationMillis;
    }

    @Override
    public void update()
    {
        long now = SystemClock.uptimeMillis();

        if (prevTimestamp > 0)
        {
            updateFrequency = now - prevTimestamp;
        }
        long timeLeft = timeFinish - now;
        long stepsLeft = timeLeft / updateFrequency;

        float tempDestinationX = destinationX, tempDestinationY = destinationY;

        if (stepsLeft > 1)
        {
            tempDestinationX = originX + ((destinationX - originX) / (stepsLeft));
            tempDestinationY = originY + ((destinationY - originY) / (stepsLeft));
        } else
        {
            uo.removeObserver(this);
        }


        translatable.translate(originX, destinationX, tempDestinationX, tempDestinationY);
        originX = tempDestinationX;
        originY = tempDestinationY;

        prevTimestamp = now;
    }
}
