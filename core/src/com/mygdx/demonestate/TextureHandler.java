package com.mygdx.demonestate;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;

import java.util.HashMap;

/**
 * Created by David on 3/17/2017.
 */
public class TextureHandler {
    private static HashMap<String, Texture> textures;

    public static void loadTextures() {
        FileHandle folder = new FileHandle("assets/sprites");
        textures = new HashMap<String, Texture>();

        for (FileHandle f : folder.list()) {
            textures.put(f.name().split("\\.")[0], new Texture(f.path()));
        }


        folder = new FileHandle("assets/weapons");
        for (FileHandle f : folder.list()) {
            textures.put(f.name().split("\\.")[0], new Texture(f.path()));
        }

        folder = new FileHandle("assets/hud");
        for (FileHandle f : folder.list()) {
            textures.put(f.name().split("\\.")[0], new Texture(f.path()));
            System.out.println(f.name().split("\\.")[0]);
        }
    }

    public static Texture getTexture(String textureName) {
        return textures.get(textureName);
    }
}
