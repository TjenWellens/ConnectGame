/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.tjenwellens.connectgame.state_pattern.interfaces;

import eu.tjenwellens.connectgame.Cell;

/**
 *
 * @author tjen
 */
public interface Accessable
{

    boolean isCrossTurn();

    Cell getCell(int row, int col);

    void setCell(Cell newCell, int row, int col);

    int getRows();

    int getCols();

    boolean isFull();

    void sendMessage(int i);

    public int getConnect();
}
