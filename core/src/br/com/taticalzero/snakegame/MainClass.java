package br.com.taticalzero.snakegame;
import com.badlogic.gdx.Game;

public class MainClass extends Game {

	@Override
	public void create () {
		setScreen(new MainScreen(this));
	}

}
