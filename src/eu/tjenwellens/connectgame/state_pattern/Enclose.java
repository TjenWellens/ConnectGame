/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.tjenwellens.connectgame.state_pattern;

import eu.tjenwellens.connectgame.state_pattern.interfaces.Accessable;
import android.graphics.Point;
import eu.tjenwellens.connectgame.Cell;
import eu.tjenwellens.connectgame.Circle;
import eu.tjenwellens.connectgame.Cross;
import eu.tjenwellens.connectgame.Empty;
import eu.tjenwellens.connectgame.state_pattern.interfaces.EncloseI;

/**
 *
 * @author tjen
 */
public class Enclose implements EncloseI
{

    protected Accessable game;

    public Enclose(Accessable game)
    {
        this.game = game;
    }

    @Override
    public int determineWinner()
    {
        int cirleCounter = 0;
        int crossCounter = 0;
        Cell countedCell;
        for (int row = 0; row < game.getRows(); row++)
        {
            for (int col = 0; col < game.getCols(); col++)
            {
                countedCell = game.getCell(row, col);
                if (countedCell instanceof Circle)
                {
                    cirleCounter++;
                } else if (countedCell instanceof Cross)
                {
                    crossCounter++;
                } else
                {
                    return 0;
                }
            }
        }
        if (crossCounter > cirleCounter)
        {
            return 1;
        } else if (cirleCounter > crossCounter)
        {
            return 2;
        } else
        {
            return 0;
        }
    }

    @Override
    public boolean hasSomeoneWon()
    {
        if (game.isFull())
        {
            int cirleCounter = 0;
            int crossCounter = 0;
            Cell countedCell;
            for (int row = 0; row < game.getRows(); row++)
            {
                for (int col = 0; col < game.getCols(); col++)
                {
                    countedCell = game.getCell(row, col);
                    if (countedCell instanceof Circle)
                    {
                        cirleCounter++;
                    } else if (countedCell instanceof Cross)
                    {
                        crossCounter++;
                    } else
                    {
                        return false;
                    }
                }
            }
            return cirleCounter != crossCounter;
        } else
        {
            return false;
        }
    }

    @Override
    public void pieceInserted(int col, int row)
    {
        Point p;
        if ((p = collides(row, col, 1, 0)) != null)
        {
            for (int rowi = row; rowi <= p.y; rowi++)
            {
                if (rowi != row)
                {
                    game.setCell((game.isCrossTurn() ? Cross.getInstance() : Circle.getInstance()), rowi, col);
                }
            }
        }
        if ((p = collides(row, col, -1, 0)) != null)
        {
            for (int rowi = row; rowi >= p.y; rowi--)
            {
                if (rowi != row)
                {
                    game.setCell((game.isCrossTurn() ? Cross.getInstance() : Circle.getInstance()), rowi, col);
                }
            }
        }
        if ((p = collides(row, col, 0, 1)) != null)
        {
            for (int coli = col; coli <= p.x; coli++)
            {
                if (coli != col)
                {
                    game.setCell((game.isCrossTurn() ? Cross.getInstance() : Circle.getInstance()), row, coli);
                }
            }
        }
        if ((p = collides(row, col, 0, -1)) != null)
        {
            for (int coli = col; coli >= p.x; coli--)
            {
                if (coli != col)
                {
                    game.setCell((game.isCrossTurn() ? Cross.getInstance() : Circle.getInstance()), row, coli);
                }
            }
        }
    }

    private Point collides(int startRow, int startCol, int rowIncrement, int colIncrement)
    {
        int rowi = startRow;
        int coli = startCol;
        while ((0 <= rowi) && (rowi < game.getRows()) && (0 <= coli) && (coli < game.getCols()))
        {
            if (rowi != startRow || coli != startCol)
            {
                if ((game.isCrossTurn() && game.getCell(rowi, coli) instanceof Cross) || (!game.isCrossTurn() && game.getCell(rowi, coli) instanceof Circle))
                {
                    // gebotst tegen een veld van eigen kleur
                    return new Point(coli, rowi);
                } else if (game.getCell(rowi, coli) instanceof Empty)
                {
                    break;
                }
            }
            rowi += rowIncrement;
            coli += colIncrement;
        }
        return null;
    }
}
