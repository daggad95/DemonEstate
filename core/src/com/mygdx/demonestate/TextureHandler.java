package com.mygdx.demonestate;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;

import java.util.HashMap;

/**
 * Created by David on 3/17/2017.
 */
public class TextureHandler {
    private static HashMap<String, Texture> textures;
    private static String[] folders
            = {"sprites", "hud", "weapons", "UI"};

    public static void loadTextures() {
        textures = new HashMap<String, Texture>();

        for (String folderName  : folders) {
            FileHandle folder = new FileHandle("assets/" + folderName);

            for (FileHandle f : folder.list()) {
                textures.put(f.name().split("\\.")[0], new Texture(f.path()));
            }
        }
    }

    public static Texture getTexture(String textureName) {

        // check if the map contains the specified texture, otherwise return a missing-texture texture
        if(!textures.containsKey(textureName)) {
            return textures.get("Missing");
        }
        return textures.get(textureName);
    }
}
