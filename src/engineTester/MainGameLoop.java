package engineTester;

import org.lwjgl.opengl.Display;

import renderEngine.DisplayManager;
import renderEngine.Loader;
import renderEngine.RawModel;
import renderEngine.Renderer;

public class MainGameLoop {

	public static void main(String[] args) {
		DisplayManager.createDisplay();
		
		Loader loader  = new Loader();
		Renderer renderer = new Renderer();
		
		float[] vertices = {
			// Left bottom triangle
			-0.5f, 0.5f, 0f,
			-0.5f, -0.5f, 0f,
			0.5f, -0.5f, 0f,
			// Top right triangle
			0.5f, -0.5f, 0f,
			0.5f, 0.5f, 0f,
			-0.5f, 0.5f, 0f
		};
		
		RawModel model = loader.loadToVao(vertices);
		
		while(!Display.isCloseRequested()) {
			renderer.prepare();
			// game logic
			renderer.render(model);
			DisplayManager.updateDisplay();
		}
		
		DisplayManager.closeDisplay();

	}

}