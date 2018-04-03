package renderEngine;

import java.util.List;
import java.util.Map;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;

import entities.Entity;
import models.RawModel;
import models.TexturedModel;
import shaders.StaticShader;
import textures.ModelTexture;
import toolbox.Maths;

public class EntityRenderer {
	
	private StaticShader shader;
	
	public EntityRenderer(StaticShader shader, Matrix4f projectionMatrix) {
		this.shader = shader;
		shader.start();  // Bind shader to be current
		shader.loadProjectionMatrix(projectionMatrix);  // Determine the distortion that will occur due to perspective
		shader.stop();  // Unbind shader
	}
	
	public void render(Map<TexturedModel, List<Entity>> entities) {
		for(TexturedModel model : entities.keySet()) {
			prepareTexturedModel(model);  // Enable VBOs for reading, load shader variables, and then bind texture
			List<Entity> batch = entities.get(model);  // Get all entities that share the given texture model
			for(Entity entity : batch) {
				prepareInstance(entity);  // Load transformation matrix of entity
				GL11.glDrawElements(GL11.GL_TRIANGLES, 
						model.getRawModel().getVertexCount(), 
						GL11.GL_UNSIGNED_INT, 0);  // Draw the entity to the screen using triangles
			}
			unbindTexturedModel();  // Texture is no longer being used
		}
	}
	
	private void prepareTexturedModel(TexturedModel model) {
		RawModel rawModel = model.getRawModel();  // Get the VAO from the model
		GL30.glBindVertexArray(rawModel.getVaoID());  // Set the model to be the current VAO
		
		GL20.glEnableVertexAttribArray(0);  // Enable the position VBO for data reading
		GL20.glEnableVertexAttribArray(1);  // Enable the texture coord VBO for data reading
		GL20.glEnableVertexAttribArray(2);  // Enable the normals for data reading
		
		ModelTexture texture = model.getTexture();
		shader.loadShineVariables(texture.getShineDamper(), texture.getReflectivity());
		GL13.glActiveTexture(GL13.GL_TEXTURE0);  // Activate texture buffer for texture sampler
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture.getID());  // Set the texture to be current
	}
	
	private void unbindTexturedModel() {
		GL20.glDisableVertexAttribArray(0);  // Disable the position VBO so data can no longer be retrieved
		GL20.glDisableVertexAttribArray(1);   // Disable the texture coord VBO
		GL20.glDisableVertexAttribArray(2);  // Disable the normals VBO
		GL30.glBindVertexArray(0);  // VBOs are no longer being used
	}
	
	private void prepareInstance(Entity entity) {
		Matrix4f transformationMatrix = Maths.createTransformationMatrix(entity.getPosition(), 
				entity.getRotX(), entity.getRotY(), entity.getRotZ(), entity.getScale());  // Create transformation matrix for entity
		shader.loadTransformationMatrix(transformationMatrix);  // Give transformation matrix to shader
	}
}
