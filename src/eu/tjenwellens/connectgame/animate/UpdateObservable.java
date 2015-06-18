/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.tjenwellens.connectgame.animate;

/**
 *
 * @author tjen
 */
interface UpdateObservable
{
    void registerObserver(UpdateObserver updateObserver);
    void removeObserver(UpdateObserver updateObserver);
}
