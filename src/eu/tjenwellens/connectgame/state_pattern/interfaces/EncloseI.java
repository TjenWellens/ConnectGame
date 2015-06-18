/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.tjenwellens.connectgame.state_pattern.interfaces;

/**
 *
 * @author tjen
 */
public interface EncloseI
{

    int determineWinner();

    boolean hasSomeoneWon();

    void pieceInserted(int col, int row);
}
