package sg.ebacorp.spaceimpactmvc.controller;

import java.util.Iterator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.TimeUtils;
import sg.ebacorp.spaceimpact.utils.ExecutionState;
import sg.ebacorp.spaceimpactmvc.model.Enemy;
import sg.ebacorp.spaceimpactmvc.model.Laser;
import sg.ebacorp.spaceimpactmvc.model.RandomPickup;
import sg.ebacorp.spaceimpactmvc.model.ShootSound;
import sg.ebacorp.spaceimpactmvc.model.World;

import static sg.ebacorp.spaceimpactmvc.Config.IS_DEBUG;

public class WorldController {
    private static final float DAMP = 0.97f;
    private static final float ACCELERATION = 20f;
    public static final float LASER_SPEED = 1.75f;
    public static final int RANDOM_SPEED = 1;
    public static final float ENEMY_DAMAGED_GRAVITY = -5f;
    public static final int CHASE_ACCELERATION = -20;
    private final long PROJECTILE_SPAWN_RATE_COMPARATOR = 236000000;
    World world;
    long lastEnemySpawnTime = TimeUtils.millis();
    long lastRandomItemSpawnTime = TimeUtils.millis();
    long lastProjectileSpawnTime = TimeUtils.millis();

    public WorldController(World world) {
        this.world = world;
    }

    public void update(float delta) {
        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
            Gdx.app.exit();
        }
        if (world.getState() == ExecutionState.NONE) {
            if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
                world.reset();
                world.start();
            }
        } else {
            if (world.getPlayer().alive()) {
                processInputs();

                if (!IS_DEBUG) {
                    spawnEnemies();
                    spawnRandomItems();
                    updateEnemyPosition(delta);
                    updateRandomItemPosition();
                    updateLaserPosition();
                }

                world.getPlayer().update(delta, DAMP);
            } else {
                if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
                    world.reset();
                }
            }
        }
    }

    private void updateRandomItemPosition() {
        Iterator<RandomPickup> iterator = world.getRandomPickups().iterator();
        while (iterator.hasNext()) {
            RandomPickup randomPickup = iterator.next();
            randomPickup.moveLeft(Gdx.graphics.getDeltaTime());
            if (randomPickup.getPosition().x + RANDOM_SPEED < 0) {
                iterator.remove();
            }
            if (randomPickup.getPositionAsRectangle().overlaps(world.getPlayer().getPositionAsRectangle())) {
                float mathRandom = MathUtils.random();
                if (mathRandom < .4) {
                    if (world.getPlayer().getLives() <= 3) {
                        world.getPlayer().liveUp();
                    }
                    //FIXME figure out what to do with lastPickedUpItemType
                    //lastPickedUpItemType = 0;
                    world.getPlayer().scoreUp();
                    world.getPlayer().scoreUp();
                } else if (mathRandom > .4) {
                    if (world.getPlayer().getXray() < 3) {
                        world.getPlayer().xrayUp();
                    }
                    //FIXME figure out what to do with lastPickedUpItemType
                    //lastPickedUpItemType = 1;
                    world.getPlayer().scoreUp();
                    world.getPlayer().scoreUp();
                    world.getPlayer().scoreUp();
                } else {
                    //FIXME figure out what to do with sockwaves
//                    // shock wave will spawn only if
//                    shockWaveCount++;
//                    lastPickedUpItemType = 2;
//                    scoreCount += 120;
                }
                iterator.remove();
            }
        }
    }

    private void updateLaserPosition() {
        Iterator<Laser> laserIterator = world.getLasers().iterator();
        while (laserIterator.hasNext()) {
            Laser laser = laserIterator.next();
            laser.getPosition().x += LASER_SPEED * Gdx.graphics.getDeltaTime();
            Iterator<Enemy> enemyIterator = world.getEnemies().iterator();
            while (enemyIterator.hasNext()) {
                Enemy enemy = enemyIterator.next();
                if (enemy.getPositionAsRectangle().overlaps(laser.getPositionAsRectangle())) {
                    world.getPlayer().scoreUp();
                    laserIterator.remove();
                    enemy.getAcceleration().y = ENEMY_DAMAGED_GRAVITY;
                }
            }
        }
    }

    private void updateEnemyPosition(float delta) {
        Iterator<Enemy> iterator = world.getEnemies().iterator();
        while (iterator.hasNext()) {
            Enemy enemy = iterator.next();
            enemy.update(delta);
            if (enemy.getPosition().x + enemy.getBounds().width < 0 || enemy.getPosition().y + enemy.getBounds().height < 0) {
                iterator.remove();
            }
            // enemy is closer than 3 game units we should enable acceleration
            if (enemy.getPosition().dst(world.getPlayer().getPosition()) < 3) {
                enemy.getAcceleration().x = CHASE_ACCELERATION;
            }
            if (enemy.getPositionAsRectangle().overlaps(world.getPlayer().getPositionAsRectangle())) {
                world.getPlayer().liveDown();
                iterator.remove();
            }
        }

    }

    private void spawnRandomItems() {
        if (TimeUtils.millis() - lastRandomItemSpawnTime > MathUtils.random(10000, 60000)) {
            float y = MathUtils.random(1f, 5f);
            lastRandomItemSpawnTime = TimeUtils.millis();
            world.spawnRandomItem(11, y);
        }
    }

    private void spawnEnemies() {
        if (TimeUtils.millis() - lastEnemySpawnTime > MathUtils.random(1000, 10000)) {
            float y = MathUtils.random(1f, 5f);
            lastEnemySpawnTime = TimeUtils.millis();
            world.spawnEnemy(11, y);
        }
    }

    private void processInputs() {
        // WASD Movement
        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            world.getPlayer().getAcceleration().y = ACCELERATION;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            world.getPlayer().getAcceleration().y = -ACCELERATION;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            world.getPlayer().getAcceleration().x = ACCELERATION;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            world.getPlayer().getAcceleration().x = -ACCELERATION;
        }
        // Jump
        if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
            world.getPlayer().jump();

        }
        if (!Gdx.input.isKeyPressed(Input.Keys.RIGHT) && !Gdx.input.isKeyPressed(Input.Keys.LEFT) && !Gdx.input.isKeyPressed(Input.Keys.UP) &&
                !Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            world.getPlayer().clearAcceleration();
        }

        if (!IS_DEBUG) {
            if (Gdx.input.isKeyPressed(Input.Keys.SPACE) && TimeUtils.nanoTime() - lastProjectileSpawnTime > PROJECTILE_SPAWN_RATE_COMPARATOR) {
                ShootSound.sound.play();
                spawnProjectile();
            }
        }
    }

    private void spawnProjectile() {
        world.spawnLaser(world.getPlayer().getPosition().x + world.getPlayer().getBounds().width,
                world.getPlayer().getPosition().y + (world.getPlayer().getBounds().height / 2));
        lastProjectileSpawnTime = TimeUtils.nanoTime();
    }
}
