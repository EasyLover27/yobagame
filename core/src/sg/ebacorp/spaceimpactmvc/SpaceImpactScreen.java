package sg.ebacorp.spaceimpactmvc;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ScreenUtils;
import sg.ebacorp.spaceimpact.utils.RuntimeConfig;
import sg.ebacorp.spaceimpactmvc.controller.WorldController;
import sg.ebacorp.spaceimpactmvc.model.MyPixelRenderer;
import sg.ebacorp.spaceimpactmvc.model.World;
import sg.ebacorp.spaceimpactmvc.view.WorldView;

import static sg.ebacorp.spaceimpactmvc.view.WorldView.batch;
import static sg.ebacorp.spaceimpactmvc.view.WorldView.playerCamera;
import static sg.ebacorp.spaceimpactmvc.view.WorldView.ppuX;
import static sg.ebacorp.spaceimpactmvc.view.WorldView.ppuY;

public class SpaceImpactScreen implements Screen, InputProcessor {

    World world;
    WorldController worldController;
    WorldView worldView;
    private Texture texture;

    @Override
    public void show() {
        Pixmap pixmap = new Pixmap(RuntimeConfig.getInstance().screenWidth, RuntimeConfig.getInstance().screenHeight, Pixmap.Format.RGB888);
        MyPixelRenderer.generatePerlin(pixmap, true, 128);
        texture = new Texture(pixmap);
        world = new World();
        worldView = new WorldView(world);
        worldController = new WorldController(world);
        Gdx.input.setInputProcessor(this);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0.698f, 0.741f, 0.31f, 1);
        batch.begin();
        batch.draw(texture, 0f, 0f);
        batch.end();

        worldController.update(delta);
        // Works only if called here
        //playerCamera.position.x = world.getPlayer().getPosition().x * ppuX;
        //playerCamera.position.y = world.getPlayer().getPosition().y * ppuY;
        //playerCamera.update();
        batch.setProjectionMatrix(playerCamera.combined);
        worldView.render();
    }

    @Override
    public void resize(int width, int height) {
//        worldController.setSize(width, height);
//        worldView.setSize(width, height);
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

    // Input processor

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if (button == Input.Buttons.LEFT) {
            //world.spawnAsteroid((float) screenX/ppuX,  (float) 9 - screenY/ppuY, new Vector2());
            world.spawnAsteroid(screenX, RuntimeConfig.getInstance().screenHeight - screenY, new Vector2(), false);
        } else if (button == Input.Buttons.RIGHT) {
            world.spawnAsteroid(screenX, RuntimeConfig.getInstance().screenHeight - screenY, new Vector2(), true);
        }
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        return false;
    }
}
