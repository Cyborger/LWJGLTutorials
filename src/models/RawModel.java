package models;

public class RawModel {
	private int vaoID;  // ID associated with this specific VAO in memory
	private int vertexCount;  // How many vertices the model contains
	
	public RawModel(int vaoID, int vertexCount) {
		this.vaoID = vaoID;
		this.vertexCount = vertexCount;
	}

	public int getVaoID() {
		return vaoID;
	}

	public int getVertexCount() {
		return vertexCount;
	}
	
	
}
