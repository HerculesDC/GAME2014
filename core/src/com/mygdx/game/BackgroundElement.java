package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;

public class BackgroundElement extends Actor {
    //TODO: Check why the textures are skipping...
    //Later, convert the Bacground Textures into this...
    private float m_fScrollSpeed;
    private Texture m_texture;

    //BackgroundElement(){ super(); }

    BackgroundElement(String texturePath, float scrollSpeed, Stage s){
        super();
        m_texture = new Texture(texturePath);
        m_fScrollSpeed = scrollSpeed;

        this.setX(0.0f);
        this.setY(0.0f);
        //remember to set the size for the actor
        this.setSize(m_texture.getWidth(), m_texture.getHeight());

        s.addActor(this);
    }

    @Override
    public void act(float delta){
        super.act(delta);
        moveBy(-1*m_fScrollSpeed*delta, 0);
        if (getX() <= -m_texture.getWidth()){
            setX(0.0f);
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha){

        if(isVisible()){
            batch.draw(m_texture, getX(), getY(), Gdx.graphics.getWidth(), 1.3f*Gdx.graphics.getHeight());
            batch.draw(m_texture, getX()+getWidth(), getY(), Gdx.graphics.getWidth(), 1.3f*Gdx.graphics.getHeight());
            batch.draw(m_texture, getX()+(2*getWidth()), getY(), Gdx.graphics.getWidth(), 1.3f*Gdx.graphics.getHeight());
        }
        super.draw(batch, parentAlpha);
    }

    //@Override public float getHeight(){ return getHeight();}
}
