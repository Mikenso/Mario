package com.mike.mariobros.sprites;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;
import com.mike.mariobros.MarioBros;


/**
 * Created by Mike on 27.10.2016.
 */
public class Coin extends  InterActiveTileObject {
    public Coin(World world, TiledMap map, Rectangle bounds) {
        super(world, map, bounds);
        fixture.setUserData(this);
        setCategoryFilter(MarioBros.COIN_BIT);

    }

    @Override
    public void onHeadHit() {
        Gdx.app.log("Coin", "Collusion");
    }
}
