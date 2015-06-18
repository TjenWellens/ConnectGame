package eu.tjenwellens.connectgame;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class Circle extends Cell
{

    private static Circle circle = new Circle();

    private Circle()
    {
    }

    public static Circle getInstance()
    {
        return circle;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (obj instanceof Circle)
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
        return "O";
    }

    @Override
    protected Bitmap getBitmap(Resources res)
    {
        return BitmapFactory.decodeResource(res, R.drawable.bola);
    }

    @Override
    public int hashCode()
    {
        int hash = 2;
        return hash;
    }

    @Override
    public int getSort()
    {
        return 2;
    }
}