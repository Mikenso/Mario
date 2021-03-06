package com.mike.mariobros.sprites.enemies;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.utils.Array;
import com.mike.mariobros.MarioBros;
import com.mike.mariobros.screens.PlayScreen;
import com.mike.mariobros.sprites.Mario;

/**
 * Created by Mike on 03.11.2016.
 */
public class Turtle extends Enemy {
    public static final int KICK_LEFT = -2;
    public static final int KICK_RIGHT = 2;

    public enum State {WALKING, STANDING_SHELL, MOVING_SHELL, DEAD};
    public State currentState;
    public State previousState;
    private float stateTime;
    private  float deadRotationDegrees;
    private Animation walkAnimation;
    private Array<TextureRegion> frames;
    TextureRegion shell;
    private boolean setToDestroy;
    private boolean destroyed;




    public Turtle(PlayScreen screen, float x, float y) {
        super(screen, x, y);

        frames = new Array<TextureRegion>();
        frames.add(new TextureRegion(screen.getAtlas().findRegion("turtle"), 0, 0, 16, 24));
        frames.add(new TextureRegion(screen.getAtlas().findRegion("turtle"), 16, 0, 16, 24));
        shell = new TextureRegion(screen.getAtlas().findRegion("turtle"), 64, 0, 16, 24);

        walkAnimation = new Animation(0.2f, frames);
        currentState = previousState = State.WALKING;

        setBounds(getX(), getY(), 16 / MarioBros.PPM, 24 / MarioBros.PPM);

   deadRotationDegrees = 0;

    }
    @Override
    protected void defineEnemy() {
        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(getX(), getY());
        bodyDef.type = BodyDef.BodyType.DynamicBody;

        b2body = world.createBody(bodyDef);

        FixtureDef fixtureDef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(6 / MarioBros.PPM);
        fixtureDef.filter.categoryBits = MarioBros.ENEMY_BIT;
        fixtureDef.filter.maskBits =
                MarioBros.GROUND_BIT |
                        MarioBros.ENEMY_BIT |
                        MarioBros.COIN_BIT |
                        MarioBros.BRICK_BIT |
                        MarioBros.OBJECT_BIT |
                        MarioBros.MARIO_BIT;

        fixtureDef.shape = shape;

        b2body.createFixture(fixtureDef).setUserData(this);

        PolygonShape head = new PolygonShape();
        Vector2[] vertice = new Vector2[4];
        vertice[0] = new Vector2(-5, 8).scl(1/MarioBros.PPM);
        vertice[1] = new Vector2(5, 8).scl(1/MarioBros.PPM);
        vertice[2] = new Vector2(-3, 3).scl(1/MarioBros.PPM);
        vertice[3] = new Vector2(3, 3).scl(1/MarioBros.PPM);
        head.set(vertice);

        fixtureDef.shape = head;
        fixtureDef.restitution = 1.5f;
        fixtureDef.filter.categoryBits = MarioBros.ENEMY_HEAD_BIT;
        b2body.createFixture(fixtureDef).setUserData(this);
    }

    @Override
    public void hitOnHead(Mario mario) {
          if (currentState ==State.STANDING_SHELL) {
              currentState = State.STANDING_SHELL;
              velocity.x = 0;
          }
          else {

              kick(mario.getX() <= this.getX() ? KICK_RIGHT : KICK_LEFT);
 }
    }

    @Override
    public void update(float dt) {
     setRegion(getFrame(dt));
        if (currentState == State.STANDING_SHELL && stateTime > 5 ) {
            currentState = State.WALKING;
            velocity.x = 1;
        }
        setPosition(b2body.getPosition().x - getWidth() /2 , b2body.getPosition().y - 8 /MarioBros.PPM);

        if (currentState == State.DEAD) {
            deadRotationDegrees +=3;
            rotate(deadRotationDegrees);
            if (currentState == State.DEAD && stateTime > 5 ) {
                world.destroyBody(b2body);
                destroyed = true;
            }
        }
        else b2body.setLinearVelocity(velocity);
    }

    private TextureRegion getFrame(float dt) {
        TextureRegion region;
        switch (currentState) {
            case STANDING_SHELL :
            case MOVING_SHELL :
                region = shell;
                break;
            case WALKING:
                default:
                    region = walkAnimation.getKeyFrame(stateTime, true);
                    break;
        }

        if (velocity.x > 0 && region.isFlipX()== false) {
            region.flip(true, false);
        }
        if (velocity.x < 0 && region.isFlipX()== true) {
            region.flip(true, false);
        }

        stateTime = currentState == previousState ? stateTime + dt : 0;
        previousState = currentState;
        return region;

    }

    public void kick(int speed) {
        velocity.x= speed;
        currentState = State.MOVING_SHELL;
    }

    public State getCurrentState() {
        return currentState;
    }

    @Override
    public void onEnemyHit(Enemy enemy) {
        if (enemy instanceof  Turtle) {
            if ( ((Turtle) enemy).currentState == State.MOVING_SHELL && currentState != State.MOVING_SHELL) {
                killed();
            }
            else if (currentState == State.MOVING_SHELL && ((Turtle) enemy).currentState == State.WALKING) {
                return;
            }
            else reverseVelocity(true, false);

        }
        else if (currentState != State.MOVING_SHELL) {
          reverseVelocity(true, false);
             }
    }

    public void killed() {
        currentState = State.DEAD;
        Filter filter = new Filter();
        filter.maskBits = MarioBros.NOTHING_BIT;

        for (Fixture fixture: b2body.getFixtureList()          ) {
            fixture.setFilterData(filter);
        }
        b2body.applyLinearImpulse(new Vector2(0, 5f), b2body.getWorldCenter(), true);
    }

    public void draw(Batch batch) {
        if (!destroyed) {
            super.draw(batch);
        }
    }
}
