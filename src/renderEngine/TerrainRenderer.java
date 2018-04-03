package renderEngine;

import java.util.List;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import models.RawModel;
import shaders.TerrainShader;
import terrain.Terrain;
import textures.ModelTexture;
import toolbox.Maths;

public class TerrainRenderer {
	private TerrainShader shader;
	
	public TerrainRenderer(TerrainShader shader, Matrix4f projectionMatrix) {
		this.shader = shader;
		shader.start();
		shader.loadProjectionMatrix(projectionMatrix);
		shader.stop();
	}
	
	public void render(List<Terrain> terrains) {
		for(Terrain terrain : terrains) {
			prepareTerrain(terrain);
			loadModelMatrix(terrain);
			GL11.glDrawElements(GL11.GL_TRIANGLES, 
					terrain.getModel().getVertexCount(), 
					GL11.GL_UNSIGNED_INT, 0);  // Draw triangles to the display using the VAO
			unbindTexturedModel();
		}
	}
	
	private void prepareTerrain(Terrain terrain) {
		RawModel rawModel = terrain.getModel();              // Get the VAO from the model
		GL30.glBindVertexArray(rawModel.getVaoID());                  // Set the model to be the current VAO
		
		GL20.glEnableVertexAttribArray(0);                         // Enable the position VBO for data reading
		GL20.glEnableVertexAttribArray(1);                         // Enable the texture coord VBO for data reading
		GL20.glEnableVertexAttribArray(2);
		
		ModelTexture texture = terrain.getTexture();
		shader.loadShineVariables(texture.getShineDamper(), texture.getReflectivity());
		GL13.glActiveTexture(GL13.GL_TEXTURE0);                    // Activate texture buffer for texture sampler
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, 
				texture.getID());               // Set the texture to be current
	}
	
	private void unbindTexturedModel() {
		GL20.glDisableVertexAttribArray(0);                        // Disable the position VBO so data can no longer be retrieved
		GL20.glDisableVertexAttribArray(1);                        // Disable the texture coord VBO also
		GL20.glDisableVertexAttribArray(2);
		GL30.glBindVertexArray(0);                                 // Set the current VAO to point to nothing
	}
	
	private void loadModelMatrix(Terrain terrain) {
		Matrix4f transformationMatrix = Maths.createTransformationMatrix(new Vector3f(terrain.getX(), 0, terrain.getZ()), 
				0, 0, 0, 1);
		shader.loadTransformationMatrix(transformationMatrix);
	}
}
