package com.mygdx.demonestate.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.demonestate.MapHandler;
import com.mygdx.demonestate.TextureHandler;
import com.mygdx.demonestate.damagebox.DamageBox;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

/**
 * Created by David on 1/7/2017.
 */
public class EntityHandler {
    private static ArrayList<Entity> players;
    private static ArrayList<Entity> monsters;
    private static ArrayList<PlayerController> playerControllers;
    private static ArrayList<DamageBox> pDBoxes;
    private static ArrayList<DamageBox> mDBoxes;
    private static HashMap<Entity, ArrayList<Entity>> collisions;

    public static void init() {
        TextureHandler.loadTextures();


        //temp creation of players for testing
        Vector2 position = new Vector2(10, 13);
        Vector2 size = new Vector2(1, 1);
        Player player = new Player(position, size, TextureHandler.getTexture("dave"));
        //Player player2 = new Player(new Vector2(position).add(2, 2), new Vector2(size), TextureHandler.getTexture("bob"));

        players = new ArrayList<Entity>();
        players.add(player);
        //players.add(player2);

        //temp creation of player controller for testing
        if (Controllers.getControllers().size > 0) {
            Controller c = Controllers.getControllers().get(0);
            PlayerController pc = new PlayerController((Player) players.get(0), c);

            playerControllers = new ArrayList<PlayerController>();
            playerControllers.add(pc);
        }


        monsters = new ArrayList<Entity>();
        collisions = new HashMap<Entity, ArrayList<Entity>>();

       /*for (int i = 0; i < 20; i++) {
            Vector2 position2 = new Vector2((int) (Math.random() * 20), (int) (Math.random() * 20));
            Vector2 size2 = new Vector2(1, 1);

            if (!MapHandler.wallAt(position2, size2)) {
                Zombie zombie = new Zombie(position2, size2);
                monsters.add(zombie);
            }
        }

        Vector2 position2 = new Vector2(2, 2);
        Vector2 size2 = new Vector2(1f,  1f);
        Eyebat bat = new Eyebat(position2, size2);
        monsters.add(bat);*/

        pDBoxes = new ArrayList<DamageBox>();
        mDBoxes = new ArrayList<DamageBox>();
    }

    public static void update(SpriteBatch batch) {
        updateDamageBoxes(pDBoxes, monsters, batch);
        updateDamageBoxes(mDBoxes, players, batch);
        findCollisions();


        for (Entity m : monsters) {
            m.draw(batch);
            m.update();
        }

        for (Entity p : players) {
            p.draw(batch);
            p.update();
        }

        //removing dead entities
        for (int i = 0; i < monsters.size(); i++) {
            if (monsters.get(i).dead() &&
                    monsters.get(i).getDeathTimer() < 0) {
                monsters.get(i).die();
            }
        }
    }
    //testeringo
    public static void addPDamageBox(DamageBox db) {
        pDBoxes.add(db);
    }
    public static void addMDamageBox(DamageBox db) {
        mDBoxes.add(db);
    }

    public static void killMonster(Entity monster) {
        monsters.remove(monster);
    }

    public static ArrayList<Entity> getPlayers() {
        return players;
    }

    public static ArrayList<Entity> getMonsters() { return  monsters; }

    private static void updateDamageBoxes(ArrayList<DamageBox> dBoxes, ArrayList<Entity> entities, SpriteBatch batch) {
        int idx = 0;
        while (idx < dBoxes.size()) {
            DamageBox db = dBoxes.get(idx);
            if (db.dead()) {
                dBoxes.remove(idx);
            } else {
                db.update();
                db.draw(batch);

                //checking for hits on entities
                int idx2 = 0;
                while (idx2 < entities.size()) {
                    Entity entity = entities.get(idx2);
                    if (intersects(entity.getHitbox(), db.getHitbox())) {
                        db.applyDamage(entity);

                    }
                    idx2++;
                }
                idx++;
            }
        }
    }

    /*//returns true if given entity intersects with
    //any monster in the monster list.
    public static boolean collideMonster(Entity entity, Vector2 newPos) {
        Polygon hitBox = new Polygon(new float[]{0, 0, entity.getSize().x, 0,
                entity.getSize().x, entity.getSize().y, 0, entity.getSize().y});
        hitBox.setOrigin(entity.getSize().x / 2, entity.getSize().y / 2);
        hitBox.setPosition(newPos.x, newPos.y);

        for (Entity monster : monsters) {
            if (intersects(hitBox, monster.getHitbox())
                    && monster != entity) {
                return true;
            }
        }
        return false;
    }*/


    public static boolean intersects(Polygon hitBox1, Polygon hitBox2) {
        return Intersector.overlapConvexPolygons(hitBox1, hitBox2);
    }

    private static void findCollisions() {
       for (int i = 0; i < monsters.size(); i++) {
            Entity m1 = monsters.get(i);
            ArrayList m1Collisions = new ArrayList<Entity>();
            collisions.put(m1, m1Collisions);

            for (int j = 0; j < monsters.size(); j++) {
                Entity m2 = monsters.get(j);

                if (intersects(m1.getHitbox(), m2.getHitbox()) && m1 != m2) {
                    m1Collisions.add(m2);
                }
            }
        }
    }

    public static ArrayList<Entity> getCollisions(Entity e) {
        return collisions.get(e);
    }

    //FOR TESTING
    public static void addMonster(Vector2 position) {
        for (int i = 0; i < 1; i++) {
            Zombie zombie = new Zombie(
                    new Vector2(position).add((float) Math.random(), (float) Math.random()),
                    new Vector2(0.75f, 0.75f));
            monsters.add(zombie);
        }

        findCollisions();
    }
}
