package sg.ebacorp.spaceimpactmvc.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import sg.ebacorp.spaceimpactmvc.NewScreen;

public class EnemyPawn implements RenderAble, Collidable, PolygonRenderAble {

    public static final int ENEMY_ACCELERATION = 0;
    public static final int ENEMY_VELOCITY = -1;
    private static Texture image;
    NewScreen.Hexagon hexagon;

    static {
        image = new Texture(Gdx.files.internal("enemy_ship.png"));
    }

    private Vector2 position;
    private Rectangle bounds;
    private Vector2 velocity;
    private Vector2 acceleration;

    public EnemyPawn(float x, float y) {
        position = new Vector2(x, y);
        bounds = new Rectangle();
        bounds.setWidth(1.25f);
        bounds.setHeight(1f);
        acceleration = new Vector2(ENEMY_ACCELERATION, 0);
        velocity = new Vector2(MathUtils.random(-3, -1), 0);
        hexagon = new NewScreen.Hexagon(new Vector2(position.x * 50, position.y * 50));
    }

    @Override
    public Rectangle getBounds() {
        return bounds;
    }

    @Override
    public Vector2 getPosition() {
        return position;
    }

    @Override
    public Vector2 getCentralPosition() {
        return new Vector2(position.x / bounds.width, position.y / bounds.height);
    }

    @Override
    public Vector2 getVelocity() {
        return velocity;

    }

    public Rectangle getPositionAsRectangle() {
        return new Rectangle(position.x, position.y, bounds.width, bounds.height);
    }

    @Override
    public Texture getTexture() {
        return image;
    }

    public Vector2 getAcceleration() {
        return acceleration;
    }

    public void update(float delta) {
        hexagon.update(position);
        hexagon.setAngle(delta);
        acceleration.scl(delta);
        velocity.add(acceleration.x, acceleration.y);
        velocity.scl(delta);
        position.add(velocity);
        velocity.scl(1 / delta);
        acceleration.scl(1 / delta);
    }

    @Override
    public void draw(PolygonSpriteBatch pSB, float ppuX, float ppuY) {
        hexagon.draw(pSB, ppuX, ppuY);
    }
}