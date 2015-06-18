/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.tjenwellens.connectgame.state_pattern.interfaces;

/**
 *
 * @author tjen
 */
public interface GravityI
{
    boolean isValidInput(int col, int row);

    boolean insertPiece(int col, int row);
}
