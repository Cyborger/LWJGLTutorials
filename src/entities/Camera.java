package entities;

import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector3f;

public class Camera {
	
	private Vector3f position = new Vector3f(0, 5, 0);  // Position in 3d space
	private float pitch = 10;  // Aim up or down
	private float yaw;  // Aim left or right
	private float roll;  // Rotate angle of view
	
	public void handleInput() {
		if (Keyboard.isKeyDown(Keyboard.KEY_W))
			position.z -= 0.2f;  // Forward
		if (Keyboard.isKeyDown(Keyboard.KEY_S))
			position.z += 0.2f;  // Backwards
		if (Keyboard.isKeyDown(Keyboard.KEY_D))
			position.x += 0.2f;  // Right
		if (Keyboard.isKeyDown(Keyboard.KEY_A))
			position.x -= 0.2f;  // Left
		if (Keyboard.isKeyDown(Keyboard.KEY_SPACE))
			position.y += 0.2f;  // Up
		if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT))
			position.y -= 0.2f;  // Down
		if (Keyboard.isKeyDown(Keyboard.KEY_C))
			yaw += 0.5f;  // Rotate to right
		if (Keyboard.isKeyDown(Keyboard.KEY_Z))
			yaw += -0.5f;  // Rotate to left
	}

	public Vector3f getPosition() {
		return position;
	}

	public float getPitch() {
		return pitch;
	}

	public float getYaw() {
		return yaw;
	}

	public float getRoll() {
		return roll;
	}
}
