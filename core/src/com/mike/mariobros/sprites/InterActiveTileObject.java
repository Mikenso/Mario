package com.mike.mariobros.sprites;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.mike.mariobros.MarioBros;
import com.mike.mariobros.screens.PlayScreen;

/**
 * Created by Mike on 27.10.2016.
 */
public abstract class InterActiveTileObject {
    protected World world;
    protected TiledMap map;
    protected TiledMapTile tile;
    protected Rectangle bounds;
    protected MapObject object;
    protected Body body;
    protected Fixture fixture;
    protected PlayScreen screen;

    public InterActiveTileObject(PlayScreen screen, MapObject object) {
       this.object = object;
        this.screen = screen;
        this.world = screen.getWorld();
        this.map = screen.getMap();
        this.bounds = ((RectangleMapObject) object).getRectangle();


        BodyDef bodyDef = new BodyDef();
        FixtureDef fDef = new FixtureDef();
        PolygonShape shape = new PolygonShape();

        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.position.set((bounds.getX() + bounds.getWidth() / 2) / MarioBros.PPM , ( bounds.getY() + bounds.getHeight() / 2) / MarioBros.PPM );

        body = world.createBody(bodyDef);
        shape.setAsBox(bounds.getWidth()/2 / MarioBros.PPM , bounds.getHeight()/2 / MarioBros.PPM);
        fDef.shape = shape;
        fixture = body.createFixture(fDef);
    }

    public abstract void onHeadHit(Mario mario);
    public void setCategoryFilter(short filterBit){
        Filter filter = new Filter();
        filter.categoryBits = filterBit;
        fixture.setFilterData(filter);
    }

    public TiledMapTileLayer.Cell getCell() {
        TiledMapTileLayer layer = (TiledMapTileLayer) map.getLayers().get(1);
        return layer.getCell((int)(body.getPosition().x * MarioBros.PPM /16), (int)(body.getPosition().y * MarioBros.PPM /16));
    }
}
