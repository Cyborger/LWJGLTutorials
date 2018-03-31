package models;

import textures.ModelTexture;

public class TexturedModel {
	

	private RawModel rawModel;  // Contains the VAO
	private ModelTexture texture;  // Contains the texture
	
	public TexturedModel(RawModel model, ModelTexture texture) {
		this.rawModel = model;
		this.texture = texture;
	}
	
	public RawModel getRawModel() {
		return rawModel;
	}

	public ModelTexture getTexture() {
		return texture;
	}
}
