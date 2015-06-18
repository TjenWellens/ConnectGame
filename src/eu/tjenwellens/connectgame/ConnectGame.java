package eu.tjenwellens.connectgame;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;

public abstract class ConnectGame extends View
{

    protected Cell[][] cells = null;
    protected int cols = 3;
    protected int rows = 3;
    private boolean crossTurn = false;
    protected int connect = 3;
    protected Paint paint;
    // needed for threads?
    protected MessageHandler handler;

    // constructor
    public ConnectGame(Context context)
    {
        super(context);
        handler = new MessageHandler(context, this);
        initRaster();
        resetGameBoard();
    }

    // constructor
    public ConnectGame(Context context, Cell[][] cells, int rows, int cols, int connect)
    {
        super(context);
        this.cells = cells;
        this.rows = rows;
        handler = new MessageHandler(context, this);
        initRaster();
        handler.sendMessage(Message.obtain(handler, 0));
        repaintGameBoard();
    }

    public Cell[][] getCells()
    {
        return cells;
    }

    public int getCols()
    {
        return cols;
    }

    public int getConnect()
    {
        return connect;
    }

    public int getRows()
    {
        return rows;
    }

    // public methods
    public boolean isCrossTurn()
    {
        return crossTurn;
    }

    public void rotateClockwise()
    {
        int newRows = cols;
        int newCols = rows;

        Cell[][] newCells = new Cell[newRows][newCols];

        for (int col = 0; col < newCols; col++)
        {
            for (int row = 0; row < newRows; row++)
            {
                newCells[row][col] = cells[newCols - col - 1][row];
            }
        }
        cells = newCells;
        rows = newRows;
        cols = newCols;
        rotated();
        updateIsGameEnded();
        handler.sendMessage(Message.obtain(handler, 0));
    }

    public void rotateCounterClockwise()
    {
        int newRows = cols;
        int newCols = rows;

        Cell[][] newCells = new Cell[newRows][newCols];

        for (int col = 0; col < newCols; col++)
        {
            for (int row = 0; row < newRows; row++)
            {
                newCells[row][col] = cells[col][newRows - row - 1];
            }
        }
        cells = newCells;
        rows = newRows;
        cols = newCols;
        rotated();
        updateIsGameEnded();
        handler.sendMessage(Message.obtain(handler, 0));
    }
    
    public void rotate180(int newOrientation)
    {
        int newRows = rows;
        int newCols = cols;

        Cell[][] newCells = new Cell[newRows][newCols];

        for (int col = 0; col < newCols; col++)
        {
            for (int row = 0; row < newRows; row++)
            {
                newCells[row][col] = cells[newRows - row - 1][newCols - col - 1];
            }
        }
        cells = newCells;
        rows = newRows;
        cols = newCols;
        rotated();
        updateIsGameEnded();
        handler.sendMessage(Message.obtain(handler, 0));
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        int col = (int) (event.getX() / (this.getWidth() / cols));
        int row = (int) (event.getY() / (this.getHeight() / rows));
        handleUserMove(col, row);
        return super.onTouchEvent(event);
    }

    // *final
    final public boolean isFull()
    {
        for (int row = 0; row < rows; row++)
        {
            for (int col = 0; col < cols; col++)
            {
                if (cells[row][col] instanceof Empty)
                {
                    return false;
                }
            }
        }
        return true;
    }

    // protected methods
    protected void switchPlayerTurn()
    {
        crossTurn = !crossTurn;
    }

    // *abstract
    abstract protected boolean insertPiece(int col, int row);

    abstract protected boolean isValidInput(int col, int row);

    abstract protected void pieceInserted(int col, int row);

    // *final
    final protected boolean isInRange(int col, int row)
    {
        if (0 <= row && row < rows && 0 <= col && col < cols)
        {
            return true;
        }
        return false;
    }
    
    final protected void repaintGameBoard()
    {
        handler.sendMessage(Message.obtain(handler, 0));
    }

    final public void resetGameBoard()
    {
        cells = new Cell[rows][cols];

        for (int row = 0; row < rows; row++)
        {
            for (int col = 0; col < cols; col++)
            {
                cells[row][col] = Empty.getInstance();
            }
        }
        repaintGameBoard();
    }

    final protected void updateIsGameEnded()
    {
        // check if game is done
        if (hasSomeoneWon())
        {// someone has won
            handleWinnerMessage(determineWinner());
            resetGameBoard();

        } else if (isFull())
        { // field is full without someone winning, so it's a draw
            System.out.println(R.string.drawMessage);
            handler.sendMessage(Message.obtain(handler, 3));
            resetGameBoard();
        }
    }

    protected int determineWinner()
    {
        int counter;
        Cell previousCell;
        int cirleCounter = 0;
        int crossCounter = 0;

        // check horizontally
        for (int row = 0; row < rows; row++)
        {
            previousCell = null;
            counter = 0;
            for (int col = 0; col < cols; col++)
            {
                System.out.print(cells[row][col]);
                if (!cells[row][col].equals(previousCell) || cells[row][col] instanceof Empty)
                {

                    previousCell = cells[row][col];
                    counter = 0;
                } else
                {
                    counter++;
                }
                if (counter >= connect - 1)
                {
                    if (cells[row][col] instanceof Circle)
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
        for (int col = 0; col < cols; col++)
        {
            previousCell = null;
            counter = 0;
            for (int row = 0; row < rows; row++)
            {
                System.out.print(cells[row][col]);
                if (!cells[row][col].equals(previousCell) || cells[row][col] instanceof Empty)
                {
                    previousCell = cells[row][col];
                    counter = 0;
                } else
                {
                    counter++;
                }

                if (counter >= connect - 1)
                {
                    if (cells[row][col] instanceof Circle)
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
        for (int startCol = 0; startCol < cols; startCol++)
        {
            previousCell = null;
            counter = 0;
            int row = 0;
            int col = startCol;
            while (isInRange(col, row))
            {
                System.out.print(cells[row][col]);
                if (!cells[row][col].equals(previousCell) || cells[row][col] instanceof Empty)
                {
                    previousCell = cells[row][col];
                    counter = 0;
                } else
                {
                    counter++;
                }

                if (counter >= connect - 1)
                {
                    if (cells[row][col] instanceof Circle)
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
        for (int startRow = 1; startRow < rows; startRow++)
        {
            previousCell = null;
            counter = 0;
            int row = startRow;
            int col = 0;
            while (isInRange(col, row))
            {
                System.out.print(cells[row][col]);
                if (!cells[row][col].equals(previousCell) || cells[row][col] instanceof Empty)
                {
                    previousCell = cells[row][col];
                    counter = 0;
                } else
                {
                    counter++;
                }

                if (counter >= connect - 1)
                {
                    if (cells[row][col] instanceof Circle)
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

    protected boolean hasSomeoneWon()
    {
        switch (determineWinner())
        {
            case 0:
                return false;
            case 1:
            case 2:
                return true;

        }
        return false;
    }

    protected void rotated()
    {
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        // draw all fields
        int width = this.getWidth();
        int height = this.getHeight();
        int cellWidth = width / cols;
        int cellHeight = height / rows;
        for (int row = 0; row < rows; row++)
        {
            for (int col = 0; col < cols; col++)
            {
                cells[row][col].draw(canvas, getResources(), col, row, cellWidth, cellHeight);
            }
        }

        // draw raster
        for (int col = 0; col <= cols; col++)
        {
            canvas.drawLine(cellWidth * col, 0, cellWidth * col, height, paint);
        }
        for (int row = 0; row <= rows; row++)
        {
            canvas.drawLine(0, cellHeight * row, width, cellHeight * row, paint);
        }

        super.onDraw(canvas);
    }

    private void handleUserMove(int col, int row)
    {
        // only draw if selected field is valid
        if (!isValidInput(col, row))
        {
            // message wrong place
            handler.sendMessage(Message.obtain(handler, 4));
            return;
        }

        // place the correct piece
        if (insertPiece(col, row))
        {
            // hook for behaviour after piece inserted
            pieceInserted(col, row);
        }
        // repaint board
        handler.sendMessage(Message.obtain(handler, 0));

        // check game over
        updateIsGameEnded();

        // switch player
        switchPlayerTurn();
    }

    protected void handleWinnerMessage(int determineWinner)
    {
        switch (determineWinner())
        {
            case 0:
                System.out.println(R.string.drawMessage);
                handler.sendMessage(Message.obtain(handler, 3));
                break;
            case 1:
                System.out.println(R.string.crossMessage);
                handler.sendMessage(Message.obtain(handler, 1));
                break;
            case 2:
                System.out.println(R.string.circleMessage);
                handler.sendMessage(Message.obtain(handler, 2));
                break;
        }
    }

    private void initRaster()
    {
        this.paint = new Paint();
        this.paint.setARGB(255, 0, 0, 0);
        this.paint.setAntiAlias(true);
        this.paint.setStyle(Style.STROKE);
        this.paint.setStrokeWidth(5);
    }
}