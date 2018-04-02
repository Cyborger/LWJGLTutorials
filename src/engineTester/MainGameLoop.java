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
import renderEngine.OBJLoader;
import renderEngine.Renderer;
import shaders.StaticShader;
import textures.ModelTexture;

public class MainGameLoop {

	public static void main(String[] args) {
		DisplayManager.createDisplay();            // Create window
		Loader loader  = new Loader();             // Create loader
		StaticShader shader = new StaticShader();  // Create shader program
		Renderer renderer = new Renderer(shader);        // Create renderer
		
		RawModel model = OBJLoader.loadObjModel("dragon", loader);     // Convert the list of vertices to a VAO model
		ModelTexture texture = new ModelTexture(loader.loadTexture("stallTexture"));  // Load res/alistar.png as a texture
		texture.setShineDamper(10);
		texture.setReflectivity(1);
		TexturedModel staticModel = new TexturedModel(model, texture);         // Combine the VAO model with the texture
		
		Entity entity = new Entity(staticModel, new Vector3f(0, 0, -50), 0, 0, 0, 1);
		Light light = new Light(new Vector3f(0, 0, -20), new Vector3f(1, 1, 1));
		
		Camera camera = new Camera();
		while(!Display.isCloseRequested()) {
			entity.increaseRotation(0, 1, 0);
			camera.move();
			renderer.prepare();              // Clear the display before rendering
			shader.start();                  // Enable the shader
			shader.loadLight(light);
			shader.loadviewMatrix(camera);
			renderer.render(entity, shader);  // Render the model
			shader.stop();                   // Disable the shader now that the rendering is finished
			DisplayManager.updateDisplay();  // Update the display to show what has been recently rendered
		}
		
		shader.cleanUp();  // Remove shader from memory
		loader.cleanUp();  // Remove all VAOs/VBO/s from memory
		
		DisplayManager.closeDisplay();  // Close the window

	}

}
