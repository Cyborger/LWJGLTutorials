package renderEngine;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.ContextAttribs;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.PixelFormat;

public class DisplayManager {
	private static final int WIDTH = 1280;   // Width of window display
	private static final int HEIGHT = 720;   // Height of window display
	private static final int FPS_CAP = 120;  // Maximum frames per second
	
	public static void createDisplay() {
		ContextAttribs attribs = new ContextAttribs(3,2);  // Attribute settings that can passed to OpenGL
		attribs.withForwardCompatible(true);               // Allow this program to run on anything 3.2 or above
		attribs.withProfileCore(true);                     // Do not include deprecated functions
		
		try {
			Display.setDisplayMode(new DisplayMode(WIDTH, HEIGHT));  // Set dimensions of display
			Display.create(new PixelFormat(), attribs);              // Create display, using default pixel format and the settings set above
			Display.setTitle("First Display");                       // Set name of window
		} catch (LWJGLException e) {
			e.printStackTrace();  // If an error occurs, where is that error coming from
		}
		GL11.glViewport(0, 0, WIDTH, HEIGHT);  // Tell OpenGL where on the display it should render
	}
	
	public static void updateDisplay() {
		Display.sync(FPS_CAP);  // Restrict frames per second
		Display.update();       // Update the display
	}
	
	public static void closeDisplay() {
		Display.destroy();  // Destroy the window
	}
}
