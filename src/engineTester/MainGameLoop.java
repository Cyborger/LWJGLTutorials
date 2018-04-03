package engineTester;


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
	
	public static void main(String[] args) {
		DisplayManager.createDisplay();  // The window
		
		Loader loader  = new Loader();  // Loads textures and VAOs
		MasterRenderer renderer = new MasterRenderer();  // Renders objects to the display
		
		RawModel model = OBJLoader.loadObjModel("dragon", loader);  // Load an obj file as a RawModel
		ModelTexture texture = new ModelTexture(loader.loadTexture("stallTexture"));  // Load a png file as a ModelTexture
		texture.setReflectivity(1);  // Set how reflective the texture is
		texture.setShineDamper(10);  // Set how dampened the angle of reflection is
		TexturedModel staticModel = new TexturedModel(model, texture);  // Combine the VAO model and texture into one
		
		Entity entity = new Entity(staticModel, new Vector3f(0, 0, -50), 0, 0, 0, 1);  // Create an entity from the staticModel
		Terrain terrain = new Terrain(0, 0, loader, new ModelTexture(loader.loadTexture("alistar")));  // A wide piece of terrain
		Terrain terrain2 = new Terrain(1, 0, loader, new ModelTexture(loader.loadTexture("alistar")));  // A second piece of terrain
		Light light = new Light(new Vector3f(20000, 20000, 20000), new Vector3f(1, 1, 1));  // Illuminates objects
		
		Camera camera = new Camera();  // Determines what section of 3d space is rendered
		
		while(!Display.isCloseRequested()) {
			entity.increaseRotation(0, 1, 0);  // Rotate entity on y axis
			camera.handleInput();  // Move the camera with keyboard input
			
			// Set objects to be rendered
			renderer.processTerrain(terrain);
			renderer.processTerrain(terrain2);
			renderer.processEntity(entity);
			
			renderer.render(light, camera);  // Render all objects
			DisplayManager.updateDisplay();  // Update the display
		}
		
		renderer.cleanUp();  // Free shader programs from memory
		loader.cleanUp();  // Free all VAOs/VBO/s from memory
		DisplayManager.closeDisplay();  // Close the window

	}

}
