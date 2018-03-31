package renderEngine;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

public class Loader {
	
	private List<Integer> vaos = new ArrayList<Integer>();  // Stores a list of all VAOs in memory
	private List<Integer> vbos = new ArrayList<Integer>();  // Stores a list of all VBOs in memory
	
	public RawModel loadToVao(float[] positions, int[] indices) {
		int vaoID = createVAO();                     // Get a new empty VAO
		bindIndicesBuffer(indices);                  // Create indices VBO and place in VAO 
		storeDataInAttributeList(0, positions);      // Store the position in the VAO
		unbindVAO();                                 // Set the current VAO point to nothing
		return new RawModel(vaoID, indices.length);  // Return a newly created model
	}
	
	public void cleanUp() {
		for(int vao:vaos) {
			GL30.glDeleteVertexArrays(vao);  // Remove the VAO from memory
		}
		for(int vbo:vbos) {
			GL15.glDeleteBuffers(vbo);       // Remove the VBO from memory
		}
	}
	private int createVAO() {
		int vaoID = GL30.glGenVertexArrays(); // Creates an empty VAO
		vaos.add(vaoID);                      // Add to a list of all VAOs in memory
		GL30.glBindVertexArray(vaoID);        // Set the new VAO as the current VAO
		return vaoID;                         // Return the ID of the new VAO so it can be used easily
	}
	
	private void storeDataInAttributeList(int attributeNumber, float[] data) {
		int vboID = GL15.glGenBuffers();                    // Create an empty VBO
		vbos.add(vboID);                                    // Add to a list of all VBOs in memory
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboID);     // Set the new VBO to be the current, have to specify the type of VBO
		FloatBuffer buffer = storeDataInFloatBuffer(data);  // Data has to be stored in a float buffer
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);  // Store the data in the VBO, no changes will occur to it
		GL20.glVertexAttribPointer(attributeNumber, 3, GL11.GL_FLOAT, false, 0, 0);  // Place the VBO in the VAO
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);         // Unbind the VBO as it will no longer be used
	}
	
	private void unbindVAO() {
		GL30.glBindVertexArray(0);  // Set the current VAO to 0 (no VAO)
	}
	
	private void bindIndicesBuffer(int[] indices) {
		int vboID = GL15.glGenBuffers();  // Create empty VBO
		vbos.add(vboID);  // Add to list of VBOs in memory
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vboID);  // Set VBO to current
		IntBuffer buffer = storeDataInIntBuffer(indices);  // Place indices into an int buffer
		GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);  // Place indice buffer into VBO
	}
	
	private IntBuffer storeDataInIntBuffer(int[] data) {
		IntBuffer buffer = BufferUtils.createIntBuffer(data.length);  // Create an empty int buffer
		buffer.put(data);                                             // Place the data in the newly created buffer
		buffer.flip();                                                // Tells the buffer that data will no longer be added, 
		return buffer;                                                //                                           only read
	}
	
	private FloatBuffer storeDataInFloatBuffer(float[] data) {
		FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length);  // Create an empty float buffer
		buffer.put(data);                                                 // Place the data in the newly created buffer
		buffer.flip();                                                    // Tells the buffer that data is no longer being 
		return buffer;                                                    //                  placed in it, only read from
	}
}
