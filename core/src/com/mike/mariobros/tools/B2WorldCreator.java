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
import com.badlogic.gdx.utils.Array;
import com.mike.mariobros.MarioBros;
import com.mike.mariobros.screens.PlayScreen;
import com.mike.mariobros.sprites.Brick;
import com.mike.mariobros.sprites.Coin;
import com.mike.mariobros.sprites.enemies.Enemy;
import com.mike.mariobros.sprites.enemies.Goomba;
import com.mike.mariobros.sprites.enemies.Turtle;

/**
 * Created by Mike on 27.10.2016.
 */
public class B2WorldCreator {

    private Array<Goomba> goombas = new Array<Goomba>();
    private Array<Turtle> turtles = new Array<Turtle>();

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


            new Brick(screen, object);
        }

        //coins
        for (MapObject object : map.getLayers().get(4).getObjects().getByType(RectangleMapObject.class)) {
            new Coin(screen, object);
        }

        //goombas


        goombas = new Array<Goomba>();
        for (MapObject object : map.getLayers().get(6).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            goombas.add(new Goomba(screen, rect.getX() / MarioBros.PPM, rect.getY() / MarioBros.PPM ));
        }

        turtles = new Array<Turtle>();
        for (MapObject object : map.getLayers().get(7).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            turtles.add(new Turtle(screen, rect.getX() / MarioBros.PPM, rect.getY() / MarioBros.PPM ));
        }
    }

    public Array<Goomba> getGoombas() {
        return goombas;
    }

    public Array<Enemy> getEnemies() {
        Array<Enemy> enemies = new Array<Enemy>();
        enemies.addAll(goombas);
        enemies.addAll(turtles);
        return enemies;
    }
}
