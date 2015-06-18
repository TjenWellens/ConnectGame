package eu.tjenwellens.connectgame;

import android.content.Context;
import android.graphics.Point;
import android.os.Message;
import eu.tjenwellens.connectgame.state_pattern.interfaces.Accessable;
import eu.tjenwellens.connectgame.state_pattern.Enclose;
import eu.tjenwellens.connectgame.state_pattern.Gravity;
import eu.tjenwellens.connectgame.state_pattern.RotateGravity;
import eu.tjenwellens.connectgame.state_pattern.interfaces.EncloseI;
import eu.tjenwellens.connectgame.state_pattern.interfaces.GravityI;
import eu.tjenwellens.connectgame.state_pattern.interfaces.RotateGravityI;

public class ChooseOptionsGame extends ConnectGame implements Accessable
{

    private EncloseI encloseI;
    private GravityI gravityI;
    private RotateGravityI rotateGravityI;

    public ChooseOptionsGame(Context context, int cols, int rows, int connect, boolean gravity, boolean enclose, boolean rotateGravity)
    {
        super(context);
        this.cols = cols;
        this.rows = rows;
        if (enclose)
        {
            this.encloseI = new Enclose(this);
        }
        if (gravity)
        {
            if (rotateGravity)
            {
                this.rotateGravityI = new RotateGravity(this);
                this.gravityI = this.rotateGravityI;
            } else
            {
                this.gravityI = new Gravity(this);
            }
        }
        this.connect = connect;
        resetGameBoard();
    }

    public ChooseOptionsGame(Context context, Cell[][] cells, int cols, int rows, int connect, boolean gravity, boolean enclose, boolean rotationGravity)
    {
        super(context, cells, rows, cols, connect);
        if (enclose)
        {
            encloseI = new Enclose(this);
        }
        if (gravity)
        {
            if (rotationGravity)
            {
                rotateGravityI = new RotateGravity(this);
                gravityI = rotateGravityI;
            } else
            {
                gravityI = new Gravity(this);
            }
        }
        repaintGameBoard();
    }

    public boolean isEnclose()
    {
        return encloseI != null;
    }

    public boolean isGravity()
    {
        return gravityI != null;
    }

    public boolean isRotateGravity()
    {
        return rotateGravityI != null;
    }

    @Override
    protected boolean isValidInput(int col, int row)
    {
        if (gravityI != null)
        {
            return gravityI.isValidInput(col, row);
        } else
        {
            if (cells[row][col] instanceof Empty)
            {
                return true;
            } else
            {
                // message wrong place
                handler.sendMessage(Message.obtain(handler, 4));
                return false;
            }
        }
    }

    @Override
    protected boolean insertPiece(int col, int row)
    {
        if (gravityI != null)
        {
            return gravityI.insertPiece(col, row);
        } else
        {
            // draw a cross/ball
            Cell cel;
            if (isCrossTurn())
            {
                cel = Cross.getInstance();
            } else
            {
                cel = Circle.getInstance();
            }
            cells[row][col] = cel;
        }
        return true;
    }

    private Point collides(int startRow, int startCol, int rowIncrement, int colIncrement)
    {
        int rowi = startRow;
        int coli = startCol;
        while ((0 <= rowi) && (rowi < rows) && (0 <= coli) && (coli < cols))
        {
            if (rowi != startRow || coli != startCol)
            {
                if ((!isCrossTurn() && cells[rowi][coli] instanceof Cross) || (!(!isCrossTurn()) && cells[rowi][coli] instanceof Circle))
                {
                    // gebotst tegen een veld van eigen kleur
                    return new Point(coli, rowi);
                } else if (cells[rowi][coli] instanceof Empty)
                {
                    break;
                }
            }
            rowi += rowIncrement;
            coli += colIncrement;
        }
        return null;
    }

    @Override
    protected boolean hasSomeoneWon()
    {
        if (encloseI != null)
        {
            return encloseI.hasSomeoneWon();
        } else
        {
            return super.hasSomeoneWon();
        }
    }

    @Override
    protected int determineWinner()
    {
        if (encloseI != null)
        {
            return encloseI.determineWinner();
        } else if (rotateGravityI != null)
        {
            return rotateGravityI.determineWinner();
        } else
        {
            return super.determineWinner();
        }
    }

    @Override
    protected void rotated()
    {
        if (rotateGravityI != null)
        {
            rotateGravityI.rotated();
        }
    }

    @Override
    protected void pieceInserted(int col, int row)
    {
        if (encloseI != null)
        {
            encloseI.pieceInserted(col, row);
        }
    }

    public Cell getCell(int row, int col)
    {
        return cells[row][col];
    }

    public void setCell(Cell newCell, int row, int col)
    {
        cells[row][col] = newCell;
    }

    public int getRows()
    {
        return rows;
    }

    public int getCols()
    {
        return cols;
    }

    public void sendMessage(int i)
    {
        handler.sendMessage(Message.obtain(handler, i));
    }

    public int getConnect()
    {
        return connect;
    }
}
