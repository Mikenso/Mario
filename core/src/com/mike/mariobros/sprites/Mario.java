package com.mike.mariobros.sprites;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.mike.mariobros.MarioBros;
import com.mike.mariobros.screens.PlayScreen;

/**
 * Created by Mike on 27.10.2016.
 */
public class Mario extends Sprite {
    public World world;
    public Body b2body;

    private TextureRegion marioStand;

    public Mario(World world, PlayScreen screen) {
        super( screen.getAtlas().findRegion("little_mario"));
        this.world = world;
       defineMario();
        marioStand = new TextureRegion(getTexture(),0, 0, 16, 16);
        setBounds(0,0, 16 /MarioBros.PPM, 16 /MarioBros.PPM );
        setRegion(marioStand);
    }

    private void defineMario() {
        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(32 / MarioBros.PPM, 32/ MarioBros.PPM);
        bodyDef.type = BodyDef.BodyType.DynamicBody;

        b2body = world.createBody(bodyDef);

        FixtureDef fixtureDef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(6/ MarioBros.PPM);
        fixtureDef.shape = shape;

        b2body.createFixture(fixtureDef);
    }

    public  void update(float dt) {
        setPosition(b2body.getPosition().x - getWidth() /2 , b2body.getPosition().y - getHeight() /2 );
    }
}
