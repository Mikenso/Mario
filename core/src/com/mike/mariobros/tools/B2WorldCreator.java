package com.mike.mariobros.tools;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.mike.mariobros.MarioBros;
import com.mike.mariobros.screens.PlayScreen;
import com.mike.mariobros.sprites.Brick;
import com.mike.mariobros.sprites.Coin;

/**
 * Created by Mike on 27.10.2016.
 */
public class B2WorldCreator {
    public B2WorldCreator(PlayScreen screen) {

        World world = screen.getWorld();
        TiledMap map = screen.getMap();
        BodyDef bodyDef = new BodyDef();
        PolygonShape shape = new PolygonShape();
        FixtureDef fDef = new FixtureDef();
        Body body;


        for (MapObject object : map.getLayers().get(2).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            bodyDef.type = BodyDef.BodyType.StaticBody;
            bodyDef.position.set((rect.getX() + rect.getWidth() / 2) / MarioBros.PPM , ( rect.getY() + rect.getHeight() / 2) / MarioBros.PPM );

            body = world.createBody(bodyDef);
            shape.setAsBox(rect.getWidth()/2 / MarioBros.PPM , rect.getHeight()/2 / MarioBros.PPM);
            fDef.shape = shape;
            body.createFixture(fDef);
        }


        //pipes
        for (MapObject object : map.getLayers().get(3).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            bodyDef.type = BodyDef.BodyType.StaticBody;
            bodyDef.position.set((rect.getX() + rect.getWidth() / 2) / MarioBros.PPM , ( rect.getY() + rect.getHeight() / 2) / MarioBros.PPM );

            body = world.createBody(bodyDef);
            shape.setAsBox(rect.getWidth()/2 / MarioBros.PPM , rect.getHeight()/2 / MarioBros.PPM);
            fDef.shape = shape;
            fDef.filter.categoryBits = MarioBros.OBJECT_BIT;
            body.createFixture(fDef);
        }

        //bricks
        for (MapObject object : map.getLayers().get(5).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            new Brick(screen, rect);
        }

        //coins
        for (MapObject object : map.getLayers().get(4).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            new Coin(screen, rect);
        }
    }
}
