package com.mygdx.demonestate;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.demonestate.entity.Player;

public class HUD {
    private final int TOP_LEFT = 0;
    private final float HB_WIDTH = 3.4f;
    private final float HB_HEIGHT = 0.3f;
    private final float BOX_WIDTH = 4f;
    private final float BOX_HEIGHT = 2f;
    private final float BOX_BORDER = 0.3f;
    private final float AMMO_WIDTH = 2.5f;
    private final float FONT_SCALE = 0.1f;

    private Player player;
    private Vector2 hbPosition;
    private Vector2 boxPosition;
    private float viewWidth;
    private float viewHeight;
    private BitmapFont font;

    public HUD(Player player) {
        this.player = player;
        this.hbPosition = new Vector2();
        this.boxPosition = new Vector2();

        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();
        viewHeight = GameScreen.VIEW_WIDTH * (h/w);
        viewWidth = GameScreen.VIEW_WIDTH;
        font = new BitmapFont();

        setPosition();
    }

    private void setPosition() {
        switch (player.getId()) {
            case TOP_LEFT:
                boxPosition.x = 0;
                boxPosition.y = viewHeight - BOX_HEIGHT;
                hbPosition.x = BOX_BORDER;
                hbPosition.y = viewHeight - HB_HEIGHT - BOX_BORDER;
                break;
        }
    }

    public void draw(SpriteBatch batch) {
        float health = player.getHealth();
        float maxHealth = player.getMaxHealth();
        int maxClip = player.getCurrentWeapon().getClipSize();
        int currentClip = player.getCurrentWeapon().getClip();
        CharSequence clipNum = "x2";

        batch.draw(TextureHandler.getTexture("box"),
                boxPosition.x, boxPosition.y, BOX_WIDTH, BOX_HEIGHT);

        batch.draw(TextureHandler.getTexture("health_bg"),
                hbPosition.x, hbPosition.y, HB_WIDTH, HB_HEIGHT);
        batch.draw(TextureHandler.getTexture("health_fg"),
                hbPosition.x, hbPosition.y, HB_WIDTH * (health / maxHealth), HB_HEIGHT);

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
    }
}

