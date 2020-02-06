package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class FirstGame extends Game {

	private static FirstGame m_Game;
	public FirstGame(){m_Game = this;}

	private static float m_fMusicVolume;
	private static float m_fSFXVolume;

	public static void setMusicVolume(float newVolume){ m_fMusicVolume = Math.min(1.0f, Math.max(newVolume, 0.0f));}
	public static float getMusicVolume(){return m_fMusicVolume;}

	public static void setSFXVolume(float newVolume){ m_fSFXVolume = Math.min(1.0f, Math.max(newVolume, 0.0f));}
	public static float getSFXVolume(){return m_fSFXVolume;}

	private static float m_fSensitivity;
	public static void setSensitivity(float newSensitivity){ m_fSensitivity = Math.min(1.0f, Math.max(newSensitivity, 0.0f));}
	public static float getSensitivity(){return m_fSensitivity;}

	public static Skin m_Skin;

	@Override
	public void create () {
		m_fMusicVolume = 1.0f;
		m_fSFXVolume = 0.8f;
		m_fSensitivity = 0.5f;

		InputMultiplexer m_input = new InputMultiplexer();
		Gdx.input.setInputProcessor(m_input);

		m_Skin = new Skin(Gdx.files.internal("pixthulhu-ui.json"));

		//TODO: Return to SplashScreen() when done
		setActiveScreen(new SplashScreen());
	}

	public static Skin getSkin(){return m_Skin;}

	public static void setActiveScreen(ScreenBeta sc){
		m_Game.setScreen(sc);
	}
}
