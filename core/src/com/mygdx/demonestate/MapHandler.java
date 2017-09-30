package com.mygdx.demonestate;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.MapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import java.util.ArrayList;
//help me

/**
 * Created by exastra on 3/17/2017.
 */
public class MapHandler {
	public static final float TILE_PIXEL_RATIO = 1/32f;
	private static final String DEFAULT_MAP = "100test.tmx";
	private static String DEFAULT_MAP_PATH = "assets/maps/";
	private static TiledMap map;
	private static MapRenderer mapRenderer;



	public static void init() {
		TmxMapLoader.Parameters parameters = new TmxMapLoader.Parameters();
		parameters.textureMagFilter = Texture.TextureFilter.Nearest;
		parameters.textureMinFilter = Texture.TextureFilter.Nearest;

		map = new TmxMapLoader().load(DEFAULT_MAP_PATH + DEFAULT_MAP, parameters);
		mapRenderer = new OrthogonalTiledMapRenderer(MapHandler.getMap(), TILE_PIXEL_RATIO);
	}

	public static void renderGroundLayer(OrthographicCamera camera) {
		int[] groundLayer = { 0 };

		mapRenderer.setView(camera);
		mapRenderer.render(groundLayer);
	}

	public static void renderWallLayer(OrthographicCamera camera) {
		int[] wallLayer = { 1 };

		mapRenderer.setView(camera);
		mapRenderer.render(wallLayer);
	}

	public static boolean wallAt(Vector2 pos, Vector2 size) {
		boolean botLeft = ((TiledMapTileLayer) map.getLayers().get("Walls")).getCell((int) pos.x, (int) pos.y) != null;
		boolean botRight = ((TiledMapTileLayer) map.getLayers().get("Walls")).getCell((int) (pos.x + size.x), (int) pos.y) != null;
		boolean topLeft = ((TiledMapTileLayer) map.getLayers().get("Walls")).getCell((int) pos.x, (int) (pos.y + size.y)) != null;
		boolean topRight = ((TiledMapTileLayer) map.getLayers().get("Walls")).getCell((int) (pos.x + size.x), (int) (pos.y + size.y)) != null;
		boolean left = ((TiledMapTileLayer) map.getLayers().get("Walls")).getCell((int) pos.x, (int) (pos.y + size.y * 0.5)) != null;
		boolean right = ((TiledMapTileLayer) map.getLayers().get("Walls")).getCell((int) (pos.x + size.x), (int) (pos.y + size.y * 0.5)) != null;
		boolean top = ((TiledMapTileLayer) map.getLayers().get("Walls")).getCell((int) (pos.x + size.x * 0.5), (int) (pos.y + size.y)) != null;
		boolean bot = ((TiledMapTileLayer) map.getLayers().get("Walls")).getCell((int) (pos.x + size.x * 0.5), (int) pos.y) != null;

		return botLeft || botRight || topLeft || topRight || left | right || top || bot;
	}


	public static Vector2[][]  genPathMap(Vector2 endPos) {
		int width = ((TiledMapTileLayer) map.getLayers().get("Walls")).getWidth();
		int height = ((TiledMapTileLayer) map.getLayers().get("Walls")).getHeight();
		int [][] pathMap = new int[width][height];

		ArrayList<Vector3> nodes = new ArrayList<Vector3>();

		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				pathMap[i][j] = Integer.MAX_VALUE;
			}
		}

		//if player out of map
		boolean invalid = endPos.x < 0 || endPos.x >= width
				|| endPos.y < 0 || endPos.y >= height;
		if (invalid) {
			return vectorizePathMap(pathMap);
		}

		//initializing end location as 0
		pathMap[(int) endPos.y][(int) endPos.x] = 0;
		nodes.add(new Vector3((int) endPos.x, (int) endPos.y, 0));


		int idx = 0;
		while (idx < nodes.size()) {
			Vector3 node = nodes.get(idx);
			ArrayList<Vector3> adjNodes = new ArrayList<Vector3>();
			adjNodes.add(new Vector3(node).add(0, -1, 1)); //bottom
			adjNodes.add(new Vector3(node).add(0, 1, 1));  //top
			adjNodes.add(new Vector3(node).add(1, 0, 1));  //right
			adjNodes.add(new Vector3(node).add(-1, 0, 1)); //left

			for (Vector3 adjNode : adjNodes) {
				invalid = adjNode.x < 0 || adjNode.x >= width
						|| adjNode.y < 0 || adjNode.y >= height;

				if (!invalid) {
					//if wall, set to -1
					if (((TiledMapTileLayer) map.getLayers().get("Walls")).getCell((int) adjNode.x, (int) adjNode.y) != null) {
						pathMap[(int) adjNode.y][(int) adjNode.x] = -1;
					}

					//if not wall and has lower value than current value at that location
					//replace with lower value.
					else if (pathMap[(int) adjNode.y][(int) adjNode.x] > (int) adjNode.z) {
						pathMap[(int) adjNode.y][(int) adjNode.x] = (int) adjNode.z;
						nodes.add(adjNode);
					}
				}
			}

			idx++;
		}

		return vectorizePathMap(pathMap);
	}

	public static Vector2[][]  vectorizePathMap(int [][] pathMap) {
		int rows = pathMap.length;
		int cols = pathMap[0].length;
		Vector2[][] vectorPathMap = new Vector2[rows][cols];

		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				int top = i + 1;
				int bot = i - 1;
				int right = j + 1;
				int left = j - 1;

				Vector2  dir = new Vector2(0, 0);
				Vector2 yDir = new Vector2(0, 0);
				Vector2 xDir = new Vector2(0, 0);

				if (top < rows && bot >= 0 && left >= 0 && right < cols) {
					int topVal = pathMap[top][j];
					int botVal = pathMap[bot][j];
					int leftVal = pathMap[i][left];
					int rightVal = pathMap[i][right];

					if (topVal != -1 && botVal != -1) {
						float yVal = botVal - topVal;
						yDir = new Vector2(0, yVal);

					}

					if (leftVal != -1 && rightVal != -1) {
						float xVal = leftVal - rightVal;
						xDir = new Vector2(xVal, 0);

					}
				}

				vectorPathMap[i][j] = dir.add(yDir).add(xDir).nor();
				if (Math.abs(vectorPathMap[i][j].x) < 0.1 && Math.abs(vectorPathMap[i][j].y) < 0.1) {
					vectorPathMap[i][j] = new Vector2((float) Math.random(), (float) Math.random()).nor();
				}
			}
		}

		return  vectorPathMap;
	}

	public static Vector2 getDirectionVector(Vector2 pos, Vector2 size, float speed, float dTime, Vector2[][] vectorMap) {
		int rows = vectorMap.length;
		int cols = vectorMap[0].length;

		int x = (int) pos.x;
		int y = (int) pos.y;


		if (x >= 0 && x < cols && y >= 0 && y < rows) {
			Vector2 dir = new Vector2(vectorMap[y][x]);

			Vector2 vel = dir.scl(speed * dTime);


			dir.nor();
			return (dir);
		}
		return new Vector2(0, 0);
	}

	
	public static TiledMap getMap(){
		return map;
	}
	
}
