package sg.ebacorp.spaceimpactmvc.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;

public class RandomPickup implements RenderAble {

    private static Texture randomPickupImage;

    static {
        randomPickupImage = new Texture(Gdx.files.internal("heal.png"));
    }

    private Rectangle position;

    public RandomPickup(float x, float y) {
        this.position = new Rectangle(x, y, 64, 64);
    }

    public void moveLeft(float v) {
        position.x = position.x - v;
    }

    @Override
    public Rectangle getPosition() {
        return position;
    }

    @Override
    public Texture getTexture() {
        return randomPickupImage;
    }
}
