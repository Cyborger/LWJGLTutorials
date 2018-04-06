package engineTester;


import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;

import entities.Camera;
import entities.Entity;
import entities.Light;
import entities.Player;
import models.RawModel;
import models.TexturedModel;
import renderEngine.DisplayManager;
import renderEngine.Loader;
import renderEngine.MasterRenderer;
import renderEngine.OBJLoader;
import terrain.Terrain;
import textures.ModelTexture;
import textures.TerrainTexture;
import textures.TerrainTexturePack;

public class MainGameLoop {
	public static void main(String[] args) {
		DisplayManager.createDisplay();  // The window
		
		Loader loader  = new Loader();  // Loads textures and VAOs
		MasterRenderer renderer = new MasterRenderer();  // Renders objects to the display

		// Terrain Textures
		TerrainTexture backgroundTexture = new TerrainTexture(loader.loadTexture("alistar"));
		TerrainTexture rTexture = new TerrainTexture(loader.loadTexture("alistarOld"));
		TerrainTexture gTexture = new TerrainTexture(loader.loadTexture("alistarMooCow"));
		TerrainTexture bTexture = new TerrainTexture(loader.loadTexture("alistarGold"));
		
		TerrainTexturePack texturePack = new TerrainTexturePack(backgroundTexture, rTexture, gTexture,
				bTexture);
		TerrainTexture blendMap = new TerrainTexture(loader.loadTexture("blendMap"));
		// Floral
		RawModel treeModel = OBJLoader.loadObjModel("tree", loader);
		ModelTexture treeTexture = new ModelTexture(loader.loadTexture("alistar"));
		TexturedModel staticTreeModel = new TexturedModel(treeModel, treeTexture);
		
		RawModel grassModel = OBJLoader.loadObjModel("grassModel", loader);
		ModelTexture grassTexture = new ModelTexture(loader.loadTexture("alistar"));
		grassTexture.setHasTransparency(true);
		TexturedModel staticGrassModel = new TexturedModel(grassModel, grassTexture);
		
		RawModel fernModel = OBJLoader.loadObjModel("fern", loader);
		ModelTexture fernTexture = new ModelTexture(loader.loadTexture("alistar"));
		grassTexture.setHasTransparency(true);
		TexturedModel staticFernModel = new TexturedModel(fernModel, fernTexture);
		
		Terrain terrain = new Terrain(0, -1, loader, texturePack, blendMap, "heightMap");  // A wide piece of terrain
		Terrain terrain2 = new Terrain(-1, -1, loader, texturePack, blendMap, "heightMap");  // A wide piece of terrain
		
		List<Entity> entities = new ArrayList<Entity>();
		Random random = new Random();
		
		for (int i = 0; i < 2000; i++) {
			if (i % 4 == 0) {
				float x = -800 + random.nextFloat() * (800 + 800);
				float z = random.nextFloat() * -800;
				float y;
				if(x > 0) {
					y = terrain.getHeightOfTerrain(x, z);
				} else {
					y = terrain2.getHeightOfTerrain(x, z);
				}
				float angle = random.nextFloat() * 360;
				entities.add(new Entity(staticFernModel, new Vector3f(x, y, z), 0, angle, 0, 0.9f));
			}
			if (i % 8 == 0) {
				float x = -800 + random.nextFloat() * (800 + 800);
				float z = random.nextFloat() * -800;
				float y;
				if(x > 0) {
					y = terrain.getHeightOfTerrain(x,  z);
				} else {
					y = terrain2.getHeightOfTerrain(x,  z);
				}
				float angle = random.nextFloat() * 360;
				float size = random.nextFloat() * 8.2f + 7f;
				entities.add(new Entity(staticTreeModel, new Vector3f(x, y, z), 0, angle, 0, size));
			}	
		}
		
		Light light = new Light(new Vector3f(20000, 20000, 20000), new Vector3f(1, 1, 1));  // Illuminates objects
		
		RawModel alistarModel = OBJLoader.loadObjModel("person", loader);
		TexturedModel staticAlistarModel = new TexturedModel(alistarModel, 
				new ModelTexture(loader.loadTexture("playerTexture")));
		Player player = new Player(staticAlistarModel, new Vector3f(0, 0, 0), 0, 0, 0, 1);
		
		Camera camera = new Camera(player);  // Determines what section of 3d space is rendered
		
		while(!Display.isCloseRequested()) {
			camera.move();
			if(player.getPosition().x > 0) { 
				player.move(terrain);
			}else{ 
				player.move(terrain2); 
			}
			// Set objects to be rendered
			renderer.processEntity(player);
			renderer.processTerrain(terrain);
			renderer.processTerrain(terrain2);
			for (Entity entity : entities) {
				renderer.processEntity(entity);
			}
			System.out.println("Player position: " + player.getPosition());
			renderer.render(light, camera);  // Render all objects
			DisplayManager.updateDisplay();  // Update the display
		}
		
		renderer.cleanUp();  // Free shader programs from memory
		loader.cleanUp();  // Free all VAOs/VBO/s from memory
		DisplayManager.closeDisplay();  // Close the window

	}
}
