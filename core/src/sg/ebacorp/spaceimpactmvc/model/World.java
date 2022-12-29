package sg.ebacorp.spaceimpactmvc.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import sg.ebacorp.spaceimpact.utils.ExecutionState;

public class World {

    ExecutionState executionState = ExecutionState.NONE;

    private SpaceShip spaceShip;
    private Set<Enemy> enemies;
    private Set<Laser> lasers;
    private Set<RandomPickup> randomPickups;

    public World() {
        createWorld();
    }

    private void createWorld() {
        this.spaceShip = new SpaceShip();
        this.enemies = new HashSet<>();
        this.lasers = new HashSet<>();
        this.randomPickups = new HashSet<>();
    }

    public SpaceShip getPlayer() {
        return spaceShip;
    }

    public void spawnEnemy(float x, float y) {
        Enemy enemy = new Enemy(x, y);
        enemies.add(enemy);
    }

    public Set<Enemy> getEnemies() {
        return enemies;
    }

    public Set<Laser> getLasers() {
        return lasers;
    }

    public Set<RandomPickup> getRandomPickups() {
        return randomPickups;
    }

    public void spawnLaser(float x, float y) {
        Laser laser = new Laser(x, y);
        lasers.add(laser);
    }

    public void spawnRandomItem(float x, float y) {
        RandomPickup randomPickup = new RandomPickup(x, y);
        randomPickups.add(randomPickup);
    }

    public void reset() {
        enemies.clear();
        lasers.clear();
        randomPickups.clear();
        spaceShip.init();
    }

    public void start() {
        executionState = ExecutionState.RUNNING;
    }

    public ExecutionState getState() {
        return executionState;
    }

    public ArrayList<RenderAble> getAllRenderAbles() {
        ArrayList<RenderAble> result = new ArrayList<>();
        result.add(spaceShip);
        result.addAll(enemies);
        result.addAll(lasers);
        result.addAll(randomPickups);
        return result;
    }
}
