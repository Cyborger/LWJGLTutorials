package engineTester;

import org.lwjgl.opengl.Display;

import models.RawModel;
import models.TexturedModel;
import renderEngine.DisplayManager;
import renderEngine.Loader;
import renderEngine.Renderer;
import shaders.StaticShader;
import textures.ModelTexture;

public class MainGameLoop {

	public static void main(String[] args) {
		DisplayManager.createDisplay();  // Create window
		
		Loader loader  = new Loader();  // Create loader
		Renderer renderer = new Renderer();  // Create renderer
		StaticShader shader = new StaticShader();  // Create shader program
		
		float[] vertices = {
			// Quad
			-0.5f, 0.5f, 0f,  //V0
			-0.5f, -0.5f, 0f, //V1
			0.5f, -0.5f, 0f,  //V2
			0.5f, 0.5f, 0f,   //V3
		};
		
		int[] indices = {
				0, 1, 3, // Top left triangle
				3, 1, 2  // Bottom right triangle
		};
		
		float[] textureCoords = {
				0, 0,  //V0
				0, 1,  //V1
				1, 1,  //V2
				1, 0   //V3
		};
		
		RawModel model = loader.loadToVao(vertices, textureCoords, indices);  // Convert the list of vertices to a VAO model
		ModelTexture texture = new ModelTexture(loader.loadTexture("alistar"));  // Load res/alistar.png as a texture
		TexturedModel texturedModel = new TexturedModel(model, texture);  // Combine the VAO model with the texture
		while(!Display.isCloseRequested()) {
			renderer.prepare();  // Clear the display before rendering
			shader.start();  // Enable the shader
			renderer.render(texturedModel);  // Render the model
			shader.stop();  // Disable the shader now that the rendering is finished
			DisplayManager.updateDisplay();  // Update the display to show what has been recently rendered
		}
		
		shader.cleanUp();  // Remove shader from memory
		loader.cleanUp();  // Remove all VAOs/VBO/s from memory
		DisplayManager.closeDisplay();  // Close the window

	}

}
