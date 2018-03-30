package engineTester;

import org.lwjgl.opengl.Display;

import renderEngine.DisplayManager;
import renderEngine.Loader;
import renderEngine.RawModel;
import renderEngine.Renderer;
import shaders.StaticShader;

public class MainGameLoop {

	public static void main(String[] args) {
		DisplayManager.createDisplay();
		
		Loader loader  = new Loader();
		Renderer renderer = new Renderer();
		StaticShader shader = new StaticShader();
		
		float[] vertices = {
			// Left bottom triangle
			-0.5f, 0.5f, 0f,  //V0
			-0.5f, -0.5f, 0f, //V1
			0.5f, -0.5f, 0f,  //V2
			0.5f, 0.5f, 0f,   //V3
		};
		
		int[] indices = {
				0, 1, 3, // Top left triangle
				3, 1, 2  // Bottom right triangle
		};
		
		RawModel model = loader.loadToVao(vertices, indices);
		
		while(!Display.isCloseRequested()) {
			// game logic
			renderer.prepare();
			shader.start();
			renderer.render(model);
			shader.stop();
			DisplayManager.updateDisplay();
		}
		shader.cleanUp();
		loader.cleanUp();
		DisplayManager.closeDisplay();

	}

}
