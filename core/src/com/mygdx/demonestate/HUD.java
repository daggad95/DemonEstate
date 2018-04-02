package com.mygdx.demonestate;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.demonestate.entity.Player;

public class HUD {
    private final int TOP_LEFT = 0;
    private final float HB_WIDTH = 8.6f;
    private final float HB_HEIGHT = 2f;
    private final float BOX_WIDTH = 10f;
    private final float BOX_HEIGHT = 10f;
    private final float BOX_BORDER_WIDTH = 0.7f;
    private final float BOX_BORDER_HEIGHT = 1.5f;
    private final float AMMO_WIDTH = 5f;
    private final float FONT_SCALE = 0.1f;
    private final float FONT_OFFSET_X = 0.5f;
    private final float CLIP_FONT_OFFSET_Y = -0.4f;
    private final float MONEY_FONT_OFFSET_Y = -2.5f;

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
        switch (player.getId()) {
            case TOP_LEFT:
                boxPosition.x = 0;
                boxPosition.y = 100 - BOX_HEIGHT;
                hbPosition.x = BOX_BORDER_WIDTH;
                hbPosition.y = 100 - HB_HEIGHT - BOX_BORDER_HEIGHT;
                break;
        }
    }

    public void draw(SpriteBatch batch) {
        float health = player.getHealth();
        float maxHealth = player.getMaxHealth();
        int maxClip = player.getCurrentWeapon().getClipSize();
        int currentClip = player.getCurrentWeapon().getClip();
        int numClips = player.getCurrentAmmo() / maxClip;
        String money = Integer.toString(player.getMoney());

        if (player.getCurrentAmmo() % maxClip != 0) {
            numClips += 1;
        }
        String clipNum = "x" + Integer.toString(numClips);

        drawAbsolute(batch,
                TextureHandler.getTexture("box"),
                boxPosition.x,
                boxPosition.y,
                BOX_WIDTH,
                BOX_HEIGHT);

        drawAbsolute(batch,
                TextureHandler.getTexture("health_bg"),
                hbPosition.x,
                hbPosition.y,
                HB_WIDTH,
                HB_HEIGHT);

        drawAbsolute(batch,
                TextureHandler.getTexture("health_fg"),
                hbPosition.x,
                hbPosition.y,
                HB_WIDTH * (health / maxHealth),
                HB_HEIGHT);


        for (int i = 0; i < maxClip; i++) {
            if (i < currentClip) {
                drawAbsolute(batch,
                        TextureHandler.getTexture("ammo"),
                        hbPosition.x + (AMMO_WIDTH / maxClip) * i,
                        hbPosition.y - HB_HEIGHT,
                        AMMO_WIDTH / maxClip,
                        HB_HEIGHT);
            }
        }

        drawAbsolute(batch,
                font,
                clipNum,
                hbPosition.x + AMMO_WIDTH + FONT_OFFSET_X,
                hbPosition.y + CLIP_FONT_OFFSET_Y);

        drawAbsolute(batch,
                font,
                money,
                hbPosition.x,
                hbPosition.y + MONEY_FONT_OFFSET_Y);
    }

    public void drawAbsolute(SpriteBatch batch, Texture texture, float relX, float relY, float relW, float relH) {
        float graphicsWidth = Gdx.graphics.getWidth();
        float graphicsHeight = Gdx.graphics.getHeight();
        float absX = graphicsWidth * (relX / 100f);
        float absY = graphicsHeight * (relY / 100f);
        float absW = graphicsWidth * (relW / 100f);
        float absH = graphicsHeight * (relH / 100f);

        batch.draw(texture, absX, absY, absW, absH);
    }

    public void drawAbsolute(SpriteBatch batch, BitmapFont font, String text, float relX, float relY) {
        float graphicsWidth = Gdx.graphics.getWidth();
        float graphicsHeight = Gdx.graphics.getHeight();
        float absX = graphicsWidth * (relX / 100f);
        float absY = graphicsHeight * (relY / 100f);

        font.draw(batch, text, absX, absY);
    }

}

