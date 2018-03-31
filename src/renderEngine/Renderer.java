package renderEngine;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import models.RawModel;
import models.TexturedModel;

public class Renderer {
	public void prepare() {
		GL11.glClearColor(1, 0, 0, 1);  // Set the color for clearing the display
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);  // Clear the display
	}
	
	public void render(TexturedModel texturedModel) {
		RawModel model = texturedModel.getRawModel();  // Get the VAO from the model
		GL30.glBindVertexArray(model.getVaoID());  // Set the model to be the current VAO
		GL20.glEnableVertexAttribArray(0);  // Enable the position VBO for data reading
		GL20.glEnableVertexAttribArray(1);  // Enable the texture coord VBO for data reading
		GL13.glActiveTexture(GL13.GL_TEXTURE0);  // Activate texture buffer for texture sampler
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texturedModel.getTexture().getID());  // Set the texture to be current
		GL11.glDrawElements(GL11.GL_TRIANGLES, model.getVertexCount(),
				GL11.GL_UNSIGNED_INT, 0);  // Draw triangles to the display using the VAO
		GL20.glDisableVertexAttribArray(0);  // Disable the attribute list so data can no longer be retrieved
		GL20.glDisableVertexAttribArray(1);
		GL30.glBindVertexArray(0);  // Set the current VAO to point to nothing
	}
}
