package com.mygdx.demonestate;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.demonestate.entity.Player;

public class HUD {
    private final int TOP_LEFT = 0;
    private final float HB_WIDTH = 9f;
    private final float HB_HEIGHT = 1f;
    private final float BOX_WIDTH = 10f;
    private final float BOX_HEIGHT = 5f;
    private final float BOX_BORDER = 1f;
    private final float AMMO_WIDTH = 2.5f;
    private final float FONT_SCALE = 0.1f;

    private Player player;
    private Vector2 hbPosition;
    private Vector2 boxPosition;
    private BitmapFont font;

    public HUD(Player player) {
        this.player = player;
        this.hbPosition = new Vector2();
        this.boxPosition = new Vector2();

        font = new BitmapFont();

        setPosition();
    }

    private void setPosition() {
        float graphicsWidth = Gdx.graphics.getWidth();
        float graphicsHeight = Gdx.graphics.getHeight();
        float boxAbsHeight = (graphicsHeight * (BOX_HEIGHT / 100f));
        float hbAbsHeight = (graphicsHeight * (HB_HEIGHT / 100f));

        switch (player.getId()) {
            case TOP_LEFT:
                boxPosition.x = 0;
                boxPosition.y = graphicsHeight - boxAbsHeight;
                hbPosition.x = BOX_BORDER;
                hbPosition.y = graphicsHeight - hbAbsHeight - BOX_BORDER;
                break;
        }
    }

    public void draw(SpriteBatch batch) {
        float graphicsWidth = Gdx.graphics.getWidth();
        float graphicsHeight = Gdx.graphics.getHeight();
        float boxAbsHeight = (graphicsHeight * (BOX_HEIGHT / 100f));
        float boxAbsWidth = (graphicsWidth * (BOX_WIDTH / 100f));
        float hbAbsHeight = (graphicsHeight * (HB_HEIGHT / 100f));
        float hbAbsWidth = (graphicsHeight * (HB_WIDTH / 100f));

        float health = player.getHealth();
        float maxHealth = player.getMaxHealth();
        int maxClip = player.getCurrentWeapon().getClipSize();
        int currentClip = player.getCurrentWeapon().getClip();
        CharSequence clipNum = "x2";

        batch.draw(TextureHandler.getTexture("box"),
                boxPosition.x, boxPosition.y, boxAbsWidth, boxAbsHeight);
        batch.draw(TextureHandler.getTexture("health_bg"),
                hbPosition.x, hbPosition.y, hbAbsWidth, hbAbsHeight);
        batch.draw(TextureHandler.getTexture("health_fg"),
                hbPosition.x, hbPosition.y, HB_WIDTH * (health / maxHealth), HB_HEIGHT);

        /*
        for (int i = 0; i < maxClip; i++) {
            if (i < currentClip) {
                batch.draw(TextureHandler.getTexture("ammo"),
                        hbPosition.x + (AMMO_WIDTH / maxClip) * i,
                        hbPosition.y - HB_HEIGHT,
                        AMMO_WIDTH/ maxClip,
                        HB_HEIGHT);
            }
        }

        font.draw(batch, clipNum, hbPosition.x + AMMO_WIDTH, hbPosition.y - HB_HEIGHT);
        */
    }

}

