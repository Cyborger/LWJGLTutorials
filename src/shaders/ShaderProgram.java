package shaders;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

public abstract class ShaderProgram {
	private int programID;  // ID of the shader program
	private int vertexShaderID;  // ID of the vertex shader
	private int fragmentShaderID;  // ID of the fragment shader
	
	public ShaderProgram(String vertexFile, String fragmentFile) {
		vertexShaderID = loadShader(vertexFile, GL20.GL_VERTEX_SHADER);  // Load vertex shader
		fragmentShaderID = loadShader(fragmentFile, GL20.GL_FRAGMENT_SHADER);  // Load fragment shader
		programID = GL20.glCreateProgram();  // Create the shader program
		GL20.glAttachShader(programID, vertexShaderID);  // Attach the vertex shader to the shader program
		GL20.glAttachShader(programID,  fragmentShaderID);  // Attach the fragment shader to the shader program
		GL20.glLinkProgram(programID);  // Properly link the shader program and its components
		GL20.glValidateProgram(programID);  // Make sure the shader is valid
		bindAttributes();  // Handle the connections between the vertex and fragment shaders, depends on the program
	}
	
	public void start() {
		GL20.glUseProgram(programID);  // Use this shader when rendering
	}
	
	public void stop() {
		GL20.glUseProgram(0);  // Don't use this shader anymore
	}
	
	public void cleanUp() {
		stop();  // Stop the program, just in case it is still running
		GL20.glDetachShader(programID, vertexShaderID);  // Remove the vertex shader from the program
		GL20.glDetachShader(programID, fragmentShaderID);  // Remove the fragment shader from the program
		GL20.glDeleteShader(vertexShaderID);  // Free the vertex shader from memory
		GL20.glDeleteShader(fragmentShaderID);  // Free the fragment shader from memory
		GL20.glDeleteProgram(programID);  // Free the shader program from memory
	}
	
	protected abstract void bindAttributes();
	
	protected void bindAttribute(int attribute, String variableName) {
		GL20.glBindAttribLocation(programID, attribute, variableName);  // Connect an attribute so a given variable can modify it
	}
	
	private static int loadShader(String file, int type) {
		StringBuilder shaderSource = new StringBuilder();  // StringBuilder to connect the contents of the shader file
		try {
			BufferedReader reader  = new BufferedReader(new FileReader(file));  // Reader for reading the shader file
			String line;
			while((line = reader.readLine()) != null) {  // Go through each line of the shader file and append them togther
				shaderSource.append(line).append("\n");
			}
			reader.close();
		} catch(IOException e) {
			System.err.println("Could not read shader file");
			e.printStackTrace();
			System.exit(-1);
		}
		int shaderID = GL20.glCreateShader(type);  // Create an empty shader of given type
		GL20.glShaderSource(shaderID, shaderSource);  // Load the source file to the shader
		GL20.glCompileShader(shaderID);  // Compile the source for use
		if(GL20.glGetShaderi(shaderID, GL20.GL_COMPILE_STATUS) == GL11.GL_FALSE) {
			System.out.println(GL20.glGetShaderInfoLog(shaderID, 500));
			System.err.println("Could not compile shader");
			System.exit(-1);
		}
		return shaderID;
	}
}
