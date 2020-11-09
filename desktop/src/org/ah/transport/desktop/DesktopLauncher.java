package org.ah.transport.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import org.ah.transport.Transport;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		float scale = 1f;
		config.width = (int) (1920 * scale);
		config.height = (int) (1080 * scale);
		new LwjglApplication(new Transport(), config);
	}
}
