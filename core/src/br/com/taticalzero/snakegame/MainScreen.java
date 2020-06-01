package br.com.taticalzero.snakegame;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class MainScreen implements Screen {

    private Game game;
    private Viewport viewport;
    private SpriteBatch batch;

    private Texture[] fundo;
    private float tempo;
    private boolean pressionando;


    public  MainScreen(Game game){
        this.game = game;
    }

    @Override
    public void show() {
        batch = new SpriteBatch();

        viewport = new FillViewport(1000 ,1500);
        viewport.apply();

        fundo = new Texture[2];
        fundo[0] = new Texture("fundo0.png");
        fundo[1] = new Texture("fundo1.png");

        tempo = 0f;
        pressionando = false;

        Gdx.input.setInputProcessor(null);
    }

    @Override
    public void render(float delta) {
        tempo += delta;

        input();


        batch.setProjectionMatrix(viewport.getCamera().combined);
        Gdx.gl.glClearColor(0.29f,0.894f,0.373f,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();

        batch.draw(fundo[(int)tempo%2], 0 , 0 , 1000 , 1500);

        batch.end();

    }
    private void input(){
        if(Gdx.input.isTouched()){
            pressionando = true;
        }else if(!Gdx.input.isTouched() && pressionando){
            pressionando = false;
            game.setScreen(new GameScreen(game));
            // mudar a tela
        }
    }
    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
