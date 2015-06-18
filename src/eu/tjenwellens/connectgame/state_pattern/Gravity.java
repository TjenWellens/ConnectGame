/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.tjenwellens.connectgame.state_pattern;

import eu.tjenwellens.connectgame.state_pattern.interfaces.Accessable;
import eu.tjenwellens.connectgame.Cell;
import eu.tjenwellens.connectgame.Circle;
import eu.tjenwellens.connectgame.Cross;
import eu.tjenwellens.connectgame.Empty;
import eu.tjenwellens.connectgame.state_pattern.interfaces.GravityI;

/**
 *
 * @author tjen
 */
public class Gravity implements GravityI
{

    protected Accessable game;

    public Gravity(Accessable game)
    {
        this.game = game;
    }

    @Override
    public boolean isValidInput(int col, int row)
    {
        if (game.getCell(0, col) instanceof Empty)
        {
            return true;
        } else
        {
            // message wrong place
            game.sendMessage(4);
            return false;
        }
    }

    @Override
    public boolean insertPiece(int col, int row)
    {
        for (row = game.getRows() - 1; row >= 0; row--)
        {
            if (game.getCell(row, col) instanceof Empty)
            {
                break;
            }
        }
        if (!(game.getCell(row, col) instanceof Empty))
        {
            return false;
        }
        // draw a cross/ball
        Cell cel;
        if (game.isCrossTurn())
        {
            cel = Cross.getInstance();
        } else
        {
            cel = Circle.getInstance();
        }
        game.setCell(cel, row, col);
        return true;
    }
}
