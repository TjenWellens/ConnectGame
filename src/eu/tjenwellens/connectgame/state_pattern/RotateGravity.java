/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.tjenwellens.connectgame.state_pattern;

import eu.tjenwellens.connectgame.state_pattern.interfaces.Accessable;
import eu.tjenwellens.connectgame.Cell;
import eu.tjenwellens.connectgame.Circle;
import eu.tjenwellens.connectgame.Empty;
import eu.tjenwellens.connectgame.state_pattern.interfaces.RotateGravityI;

/**
 *
 * @author tjen
 */
public class RotateGravity extends Gravity implements RotateGravityI
{

    public RotateGravity(Accessable game)
    {
        super(game);
    }

    @Override
    public void rotated()
    {

        for (int col = 0; col < game.getCols(); col++)
        {// every column
            for (int row = game.getRows() - 1; row >= 0; row--)
            {// every cell per column (aka all rows), start with bottom cells
                // only fall down into this cell when the current cell is empty
                if (game.getCell(row, col) instanceof Empty)
                {
                    for (int row2 = row; row2 >= 0; row2--)
                    {
                        if (!(game.getCell(row2, col) instanceof Empty))
                        {
                            // switch cells
                            Cell temp = game.getCell(row, col);
                            game.setCell(game.getCell(row2, col), row, col);
                            game.setCell(temp, row2, col);
                            // stop falling down into the current cell
                            break;
                        }
                    }
                }
            }
        }
    }

    @Override
    public int determineWinner()
    {
        int counter;
        Cell previousCell;
        int cirleCounter = 0;
        int crossCounter = 0;

        // check horizontally
        for (int row = 0; row < game.getRows(); row++)
        {
            previousCell = null;
            counter = 0;
            for (int col = 0; col < game.getCols(); col++)
            {
                System.out.print(game.getCell(row, col));
                if (!game.getCell(row, col).equals(previousCell) || game.getCell(row, col) instanceof Empty)
                {

                    previousCell = game.getCell(row, col);
                    counter = 0;
                } else
                {
                    counter++;
                }
                if (counter >= game.getConnect() - 1)
                {
                    if (game.getCell(row, col) instanceof Circle)
                    {
                        cirleCounter++;
                    } else
                    {
                        crossCounter++;
                    }
                }

            }
            System.out.println("");
        }

        // check vertically
        for (int col = 0; col < game.getCols(); col++)
        {
            previousCell = null;
            counter = 0;
            for (int row = 0; row < game.getRows(); row++)
            {
                System.out.print(game.getCell(row, col));
                if (!game.getCell(row, col).equals(previousCell) || game.getCell(row, col) instanceof Empty)
                {
                    previousCell = game.getCell(row, col);
                    counter = 0;
                } else
                {
                    counter++;
                }

                if (counter >= game.getConnect() - 1)
                {
                    if (game.getCell(row, col) instanceof Circle)
                    {
                        cirleCounter++;
                    } else
                    {
                        crossCounter++;
                    }
                }

            }
            System.out.println("");
        }

        // check diagonally upper left to lower right
        for (int startCol = 0; startCol < game.getCols(); startCol++)
        {
            previousCell = null;
            counter = 0;
            int row = 0;
            int col = startCol;
            while (isInRange(col, row))
            {
                System.out.print(game.getCell(row, col));
                if (!game.getCell(row, col).equals(previousCell) || game.getCell(row, col) instanceof Empty)
                {
                    previousCell = game.getCell(row, col);
                    counter = 0;
                } else
                {
                    counter++;
                }

                if (counter >= game.getConnect() - 1)
                {
                    if (game.getCell(row, col) instanceof Circle)
                    {
                        cirleCounter++;
                    } else
                    {
                        crossCounter++;
                    }
                }
                col++;
                row++;
            }
        }
        for (int startRow = 1; startRow < game.getRows(); startRow++)
        {
            previousCell = null;
            counter = 0;
            int row = startRow;
            int col = 0;
            while (isInRange(col, row))
            {
                System.out.print(game.getCell(row, col));
                if (!game.getCell(row, col).equals(previousCell) || game.getCell(row, col) instanceof Empty)
                {
                    previousCell = game.getCell(row, col);
                    counter = 0;
                } else
                {
                    counter++;
                }

                if (counter >= game.getConnect() - 1)
                {
                    if (game.getCell(row, col) instanceof Circle)
                    {
                        cirleCounter++;
                    } else
                    {
                        crossCounter++;
                    }
                }
                col++;
                row++;
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

    private boolean isInRange(int col, int row)
    {
        if (0 <= row && row < game.getRows() && 0 <= col && col < game.getCols())
        {
            return true;
        }
        return false;
    }
}
