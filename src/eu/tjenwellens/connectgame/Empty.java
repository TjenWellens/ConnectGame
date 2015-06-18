package eu.tjenwellens.connectgame;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class Empty extends Cell
{

    private static Empty empty = new Empty();

    private Empty()
    {
    }

    public static Empty getInstance()
    {
        return empty;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (obj instanceof Empty)
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
        return " ";
    }

    @Override
    protected Bitmap getBitmap(Resources res)
    {
        return BitmapFactory.decodeResource(res, R.drawable.vazio);
    }

    @Override
    public int hashCode()
    {
        int hash = 0;
        return hash;
    }

    @Override
    public int getSort()
    {
        return 0;
    }
}