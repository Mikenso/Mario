package com.mike.mariobros.tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.mike.mariobros.sprites.InterActiveTileObject;

/**
 * Created by Mike on 28.10.2016.
 */
public class WorldContactListener implements ContactListener {

    @Override
    public void beginContact(Contact contact) {
        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();

        if (fixA.getUserData() == "head"  || fixB.getUserData() == "head"  ) {
            Fixture head = fixA.getUserData() == "head" ? fixA : fixB;
            Fixture object = head ==fixA ? fixB : fixA;

            if (object.getUserData() != null && InterActiveTileObject.class.isAssignableFrom(object.getUserData().getClass())) {
                ((InterActiveTileObject) object.getUserData()).onHeadHit();
            }
        }
    }

    @Override
    public void endContact(Contact contact) {
        Gdx.app.log("End contact", "");
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
