package com.mike.mariobros.sprites;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.mike.mariobros.screens.PlayScreen;

/**
 * Created by Mike on 30.10.2016.
 */
public abstract class Enemy extends Sprite {
    protected World world;
    protected PlayScreen screen;
    protected Body b2body;

    public Enemy(PlayScreen screen, float x, float y) {
        this.world = screen.getWorld();
        this.screen = screen;
        setPosition(x, y);
        defineEnemy();
    }
    protected abstract  void defineEnemy();

    public abstract void hitOnHead();
}
