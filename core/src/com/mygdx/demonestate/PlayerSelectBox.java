package com.mygdx.demonestate;

import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.mygdx.demonestate.entity.Entity;
import com.mygdx.demonestate.entity.EntityHandler;
import com.mygdx.demonestate.entity.Player;
import com.mygdx.demonestate.entity.PlayerController;

import java.util.ArrayList;

public class PlayerSelectBox {
    private static final String[] pImageNames =
            {"Missing", "Zambie_Right", "SpookySkele", "Spoky Wolf"};
    private static final int TEXTURE_SIZE = 64;
    private VerticalGroup group;
    private Stack imageStack;
    private Image playerImage;
    private Image selectImage;
    private Image upArrow;
    private Image downArrow;
    private boolean imageSelected;
    private int currentChoice;
    private int pId;

    public PlayerSelectBox(Table table, int pId) {
        this.pId = pId;
        imageSelected = false;
        currentChoice = 0;

        group = new VerticalGroup();
        imageStack = new Stack();
        playerImage = new Image(TextureHandler.getTexture("Missing"));
        selectImage = new Image(TextureHandler.getTexture("green"));
        upArrow = new Image(TextureHandler.getTexture("up_arrow"));
        downArrow = new Image(TextureHandler.getTexture("down_arrow"));
        selectImage.getColor().a = .25f;

        playerImage.setOrigin(Align.center);
        playerImage.scaleBy(table.getHeight() / TEXTURE_SIZE / 8);
        selectImage.setOrigin(Align.center);
        selectImage.scaleBy(table.getHeight() / TEXTURE_SIZE / 8);
        selectImage.setVisible(false);
        downArrow.setOrigin(Align.center);
        downArrow.scaleBy(table.getHeight() / TEXTURE_SIZE / 32);
        upArrow.setOrigin(Align.center);
        upArrow.scaleBy(table.getHeight() / TEXTURE_SIZE / 32);
        imageStack.add(playerImage);
        imageStack.add(selectImage);

        group.space(TEXTURE_SIZE);
        group.addActor(upArrow);
        group.addActor(imageStack);
        group.addActor(downArrow);
        group.pad(table.getHeight() / 8);

        table.add(group);
    }

    public void changeSelection(int dir) {
        if (!imageSelected) {
            if (dir == 1)
                currentChoice = (currentChoice + 1) % pImageNames.length;
            else
                currentChoice = (currentChoice - 1) < 0
                        ? pImageNames.length - 1 : currentChoice - 1;

            Texture pTexture = TextureHandler.getTexture(pImageNames[currentChoice]);
            TextureRegion pTextureRegion = new TextureRegion(pTexture);
            playerImage.setDrawable(new TextureRegionDrawable(pTextureRegion));
        }
    }

    public void toggleCharSelect() {
        imageSelected = !imageSelected;
        selectImage.setVisible(!selectImage.isVisible());
    }

    public void createPlayer() {
        Player player = new Player(EntityHandler.getPlayerSpawnPos().add(pId, 0),
                                   TextureHandler.getTexture(pImageNames[currentChoice]));
        EntityHandler.getPlayers().add(player);

        Controller c = Controllers.getControllers().get(pId);
        PlayerController pc = new PlayerController(player, c);
        EntityHandler.getPlayerControllers().add(pc);
    }

    public boolean isImageSelected() {
        return imageSelected;
    }

}
