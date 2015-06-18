/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.tjenwellens.connectgame.state_pattern.interfaces;

/**
 *
 * @author tjen
 */
public interface RotateGravityI extends GravityI
{

    // gravity
    @Override
    boolean isValidInput(int col, int row);

    @Override
    boolean insertPiece(int col, int row);

    // rotate gravity
    void rotated();

    int determineWinner();
}
