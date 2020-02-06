package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.input.GestureDetector.GestureListener;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

public abstract class ScreenBeta implements Screen, InputProcessor, GestureListener {

    protected Stage m_Stage;

    protected TextButton[] m_Buttons;

    protected Music m_BGM;
    protected Sound [] m_sfx;

    protected boolean m_bInitialized;

    ScreenBeta(){
        m_Stage = new Stage();
        m_bInitialized = false;
    }

    public abstract void initialize();

    @Override
    public void show(){
        if (!m_bInitialized){
            initialize(); //changing m_bInitialized to true needs to be done in children
        }
        InputMultiplexer im = (InputMultiplexer) Gdx.input.getInputProcessor();
        GestureDetector gd = new GestureDetector(this);
        im.addProcessor(gd);
        im.addProcessor(this);
        im.addProcessor(m_Stage);
    }

    @Override
    public void hide(){
        InputMultiplexer m_im = (InputMultiplexer) Gdx.input.getInputProcessor();
        m_im.removeProcessor(this);
        m_im.removeProcessor(m_Stage);
    }
}
