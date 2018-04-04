package engineTester;


import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;

import entities.Camera;
import entities.Entity;
import entities.Light;
import models.RawModel;
import models.TexturedModel;
import renderEngine.DisplayManager;
import renderEngine.Loader;
import renderEngine.MasterRenderer;
import renderEngine.OBJLoader;
import terrain.Terrain;
import textures.ModelTexture;

public class MainGameLoop {
	
	static private Random randomGenerator = new Random();
	
	public static void main(String[] args) {
		DisplayManager.createDisplay();  // The window
		
		Loader loader  = new Loader();  // Loads textures and VAOs
		MasterRenderer renderer = new MasterRenderer();  // Renders objects to the display

		RawModel treeModel = OBJLoader.loadObjModel("tree", loader);
		ModelTexture treeTexture = new ModelTexture(loader.loadTexture("tree"));
		TexturedModel staticTreeModel = new TexturedModel(treeModel, treeTexture);
		
		RawModel grassModel = OBJLoader.loadObjModel("grassModel", loader);
		ModelTexture grassTexture = new ModelTexture(loader.loadTexture("grassTexture"));
		grassTexture.setHasTransparency(true);
		TexturedModel staticGrassModel = new TexturedModel(grassModel, grassTexture);
		
		RawModel fernModel = OBJLoader.loadObjModel("fern", loader);
		ModelTexture fernTexture = new ModelTexture(loader.loadTexture("fern"));
		grassTexture.setHasTransparency(true);
		TexturedModel staticFernModel = new TexturedModel(fernModel, fernTexture);
		
		List<Entity> entities = new ArrayList<Entity>();
		
		for (int i = 0; i < 500; i++) {
			entities.add(new Entity(staticTreeModel, getRandomPosition(), 0, 0, 0, 5));
			entities.add(new Entity(staticGrassModel, getRandomPosition(), 0, 0, 0, 1));
			entities.add(new Entity(staticGrassModel, getRandomPosition(), 0, 0, 0, 1));
			entities.add(new Entity(staticGrassModel, getRandomPosition(), 0, 0, 0, 1));
			entities.add(new Entity(staticFernModel, getRandomPosition(), 0, 0, 0, 0.6f));
		}
		
		Terrain terrain = new Terrain(0, -1, loader, new ModelTexture(loader.loadTexture("grass")));  // A wide piece of terrain
		Terrain secondTerrain = new Terrain(-1, -1, loader, new ModelTexture(loader.loadTexture("grass")));  // A wide piece of terrain
		Light light = new Light(new Vector3f(20000, 20000, 20000), new Vector3f(1, 1, 1));  // Illuminates objects
		
		Camera camera = new Camera();  // Determines what section of 3d space is rendered
		
		while(!Display.isCloseRequested()) {
			camera.handleInput();  // Move the camera with keyboard input
			
			// Set objects to be rendered
			renderer.processTerrain(terrain);
			renderer.processTerrain(secondTerrain);
			for (Entity entity : entities) {
				renderer.processEntity(entity);
			}
			
			renderer.render(light, camera);  // Render all objects
			DisplayManager.updateDisplay();  // Update the display
		}
		
		renderer.cleanUp();  // Free shader programs from memory
		loader.cleanUp();  // Free all VAOs/VBO/s from memory
		DisplayManager.closeDisplay();  // Close the window

	}
	
	private static Vector3f getRandomPosition() {
		return new Vector3f(randomGenerator.nextFloat() * 800 - 400, 0,
				randomGenerator.nextFloat() * -600);
	}

}
