package eu.tjenwellens.connectgame;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class Cross extends Cell
{

    private static Cross cross = new Cross();

    private Cross()
    {
    }

    public static Cross getInstance()
    {
        return cross;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (obj instanceof Cross)
        {
            return true;
        } else
        {
            return false;
        }
    }

    @Override
    public String toString()
    {
        return "X";
    }

    @Override
    protected Bitmap getBitmap(Resources res)
    {
        return BitmapFactory.decodeResource(res, R.drawable.cruz);
    }

    @Override
    public int hashCode()
    {
        int hash = 1;
        return hash;
    }

    @Override
    public int getSort()
    {
        return 1;
    }

    public void translate(float originX, float destinationX, float originY, float destinationY)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}