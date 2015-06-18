package eu.tjenwellens.connectgame;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import eu.tjenwellens.connectgame.animate.Translatable;

public abstract class Cell
{

    public void draw(Canvas g, Resources res, int col, int row, int cellWidth, int cellHeight)
    {
        Bitmap im = getBitmap(res);
        g.drawBitmap(im, null, new Rect(col * cellWidth, row * cellHeight, (col * cellWidth) + cellWidth, (row * cellHeight) + cellHeight), new Paint());
    }

    abstract public int getSort();

    abstract protected Bitmap getBitmap(Resources res);
}