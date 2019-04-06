/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hakavo.game.ai.pathfinding;

/**
 *
 * @author Statia17
 */
interface AStarHeuristic {
    public float getCost(TileBasedMap map, int x, int y, int tx, int ty);	
}
