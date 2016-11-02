package com.mike.mariobros.sprites.items;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.mike.mariobros.MarioBros;
import com.mike.mariobros.screens.PlayScreen;
import com.mike.mariobros.sprites.Mario;

/**
 * Created by Mike on 31.10.2016.
 */
public class Mushroom extends Item {

    public Mushroom(PlayScreen screen, float x, float y) {
        super(screen, x, y);
        setRegion(screen.getAtlas().findRegion("mushroom"), 0, 0, 16, 16);
        velocity = new Vector2(0.7f, 0);
    }

    @Override
    public void defineItem() {
        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(getX(), getY());
        bodyDef.type = BodyDef.BodyType.DynamicBody;

        body = world.createBody(bodyDef);

        FixtureDef fixtureDef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(6 / MarioBros.PPM);

        fixtureDef.filter.categoryBits = MarioBros.ITEM_BIT;
        fixtureDef.filter.maskBits =
                MarioBros.MARIO_BIT  |
                MarioBros.GROUND_BIT |
                MarioBros.COIN_BIT |
                MarioBros.BRICK_BIT |
                MarioBros.MARIO_HEAD_BIT  |
                MarioBros.OBJECT_BIT;

        fixtureDef.shape = shape;

        body.createFixture(fixtureDef).setUserData(this);
    }

    @Override
    public void use(Mario mario) {
       if (!destroyed)
        destroy();
       if (!mario.isMarioIsBig()) mario.grow();
    }

    @Override
    public void update(float dt) {
        super.update(dt);
        setPosition(body.getPosition().x - getWidth() / 2, body.getPosition().y - getHeight() / 2);
        velocity.y = body.getLinearVelocity().y;
        body.setLinearVelocity(velocity);
    }
}
