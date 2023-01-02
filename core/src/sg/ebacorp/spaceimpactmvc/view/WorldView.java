package sg.ebacorp.spaceimpactmvc.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import sg.ebacorp.spaceimpact.utils.ExecutionState;
import sg.ebacorp.spaceimpactmvc.model.Live;
import sg.ebacorp.spaceimpactmvc.model.RenderAble;
import sg.ebacorp.spaceimpactmvc.model.World;
import sg.ebacorp.spaceimpactmvc.model.XRay;

public class WorldView {

    public static float CAMERA_WIDTH = 16f;
    public static float CAMERA_HEIGHT = 9f;

    private World world;
    public static OrthographicCamera camera;
    private SpriteBatch batch;
    private BitmapFont font;
    private float ppuX;
    private float ppuY;

    Texture backgroundImage;

    public WorldView(World world) {
        this.world = world;
        camera = new OrthographicCamera(CAMERA_WIDTH, CAMERA_HEIGHT);
        camera.position.set(0, CAMERA_HEIGHT / 2, 0);
        camera.update(); // fun fact: camera update must be called each time we change any of its properties
        // This will work once we fix `batch.setProjectionMatrix(camera)`.
//        camera.zoom += 50.0f;

        backgroundImage = new Texture(Gdx.files.internal("bg_debug.png"));

        batch = new SpriteBatch();
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("pixelfont.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 60;
        parameter.borderWidth = 1;
        parameter.color = Color.BLACK;
        parameter.shadowOffsetX = 1;
        parameter.shadowOffsetY = 1;
        parameter.shadowColor = new Color(0, 0.5f, 0, 0.75f);
        BitmapFont font24 = generator.generateFont(parameter); // font size 24 pixels
        generator.dispose();
        font = font24;
    }

    public void render() {
        batch.begin();
        if (world.getState() == ExecutionState.NONE) {
            printWelcome();
        } else {
            if (world.getPlayer().getLives() > 0) {
                drawDebugMarkers();

                // render all renderables
                for (RenderAble renderAble : world.getAllRenderAbles()) {
                    batch.draw(renderAble.getTexture(), renderAble.getPosition().x * ppuX, renderAble.getPosition().y * ppuY,
                            renderAble.getBounds().width * ppuX, renderAble.getBounds().height * ppuY);
                }

                renderUI();
            } else {
                printGameOver();
            }
        }
        batch.end();
    }

    private void drawDebugMarkers() {
        // corners
        batch.draw(backgroundImage, 0,0);
        batch.draw(backgroundImage, 1280-128,720-128);
        batch.draw(backgroundImage, 1280-128, 0);
        batch.draw(backgroundImage, 0, 720-128);

        // centraal ALAAAAAA
        batch.draw(backgroundImage, 640-64, 360-64);
    }

    private void printWelcome() {
//        font.getData().setScale(0.94f);
//        font.draw(batch, "SPACE IMPACT", 40, 128);
//        font.getData().setScale(0.5f);
        font.draw(batch, "Press [SPACE] to START", 40, 64);
//        font.getData().setScale(0.94f);
    }

    private void printGameOver() {
        font.getData().setScale(0.94f);
        font.draw(batch, "GAME OVER!", 40, 128);
        font.getData().setScale(0.35f);
        font.draw(batch, "Press [SPACE] to restart", 40, 64);
        font.draw(batch, "- or press [ESC] to quit", 40, 32);
        font.getData().setScale(0.94f);
    }

    private void renderUI() {
        if (world.getPlayer().getLives() > 0) {
            int lives = world.getPlayer().getLives();
            for (int i = 0; i < lives && i < 5; i++) {
                batch.draw(Live.texture, ppuX * i + 16, 8 * ppuY);
            }
        }
        font.draw(batch, String.format("%05d", world.getPlayer().getScore()), 12 * ppuX, 9 * ppuY-16);
//        if (world.getPlayer().getXray() > 0) {
//            batch.draw(XRay.xRayImage, 4 * ppuX, 6 * ppuY);
//            font.draw(batch, String.valueOf(world.getPlayer().getXray()), 5 * ppuX, 6.9f * ppuY);
//        }
    }

    public void setSize(int width, int height) {
        ppuX = (float) width / CAMERA_WIDTH;
        ppuY = (float) height / CAMERA_HEIGHT;
    }
}
