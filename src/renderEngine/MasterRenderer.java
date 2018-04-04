package renderEngine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Matrix4f;

import entities.Camera;
import entities.Entity;
import entities.Light;
import models.TexturedModel;
import shaders.StaticShader;
import shaders.TerrainShader;
import terrain.Terrain;

public class MasterRenderer {
	
	private static final float FOV = 70;  // How wide the angle of viewing is
	private static final float NEAR_PLANE = 0.1f;  // Start of view
	private static final float FAR_PLANE = 1000;  // End of view
	
	private static final float SKY_RED = 0.125f;
	private static final float SKY_BLUE = 1.0f;
	private static final float SKY_GREEN = 0.7f;
	
	private Matrix4f projectionMatrix;  // Current projection matrix
	
	private StaticShader shader = new StaticShader();  // Create shader program
	private EntityRenderer renderer;  // Renderer for entities
	
	private TerrainShader terrainShader = new TerrainShader();  // Terrain shader program
	private TerrainRenderer terrainRenderer;  // Renderer for terrains
	
	private Map<TexturedModel, List<Entity>> entities = new HashMap<TexturedModel, List<Entity>>();  // Entities using each texture
	private List<Terrain> terrains = new ArrayList<Terrain>();  // All terrains
	
	public MasterRenderer() {
		enableCulling();
		createProjectionMatrix();  // Determine distortion caused by perspective
		renderer = new EntityRenderer(shader, projectionMatrix);
		terrainRenderer = new TerrainRenderer(terrainShader, projectionMatrix);
	}
	
	public static void enableCulling() {
		GL11.glEnable(GL11.GL_CULL_FACE);  // Enables culling of faces
		GL11.glCullFace(GL11.GL_BACK);  // Only cull faces that aren't facing the camera
	}
	
	public static void disableCulling() {
		GL11.glDisable(GL11.GL_CULL_FACE);
	}
	
	public void render(Light sun, Camera camera) {
		prepare();
		shader.start();
		shader.loadSkyColor(SKY_RED, SKY_GREEN, SKY_BLUE);
		shader.loadLight(sun);
		shader.loadViewMatrix(camera);
		renderer.render(entities);
		shader.stop();

		terrainShader.start();
		terrainShader.loadSkyColor(SKY_RED, SKY_GREEN, SKY_BLUE);
		terrainShader.loadLight(sun);
		terrainShader.loadViewMatrix(camera);
		terrainRenderer.render(terrains);
		terrainShader.stop();
		
		terrains.clear();
		entities.clear();
	}
	
	public void processTerrain(Terrain terrain) {
		terrains.add(terrain);
	}
	
	public void processEntity(Entity entity) {
		TexturedModel entityModel = entity.getModel();
		List<Entity> batch = entities.get(entityModel);
		if (batch != null) {
			batch.add(entity);
		} else {
			List<Entity> newBatch = new ArrayList<Entity>();
			newBatch.add(entity);
			entities.put(entityModel, newBatch);
		}
	}
	public void cleanUp() {
		shader.cleanUp();
		terrainShader.cleanUp();
	}
	
	public void prepare() {
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glClearColor(SKY_RED, SKY_GREEN, SKY_BLUE, 1);           // Set the color for clearing the display
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);  // Clear the display
	}
	
	private void createProjectionMatrix() {
		float aspectRatio = (float) Display.getWidth() / (float) Display.getHeight();
		float y_scale = (float) ((1f / Math.tan(Math.toRadians(FOV / 2f))) * aspectRatio);
		float x_scale = y_scale / aspectRatio;
		float frustum_length = FAR_PLANE - NEAR_PLANE;
		
		projectionMatrix = new Matrix4f();
		projectionMatrix.m00 = x_scale;
		projectionMatrix.m11 = y_scale;
		projectionMatrix.m22 = -((FAR_PLANE + NEAR_PLANE) / frustum_length);
		projectionMatrix.m23 = -1;
		projectionMatrix.m32 = -((2 * NEAR_PLANE * FAR_PLANE) / frustum_length);
		projectionMatrix.m33 = 0;
	}
}
