package org.ah.transport;

import org.ah.transport.world.World;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Transport extends ApplicationAdapter {
	public SpriteBatch batch;
	private World world;
	private AssetManager assetManager;
	private OrthographicCamera camera;

	@Override
	public void create() {
		batch = new SpriteBatch();
		assetManager = new AssetManager();
		long seed = 1234;
		world = new World(128, this, 1234);
		world.generate();
		world.populate();

		camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		camera.setToOrtho(false);

		
		InputManager inputManager = new InputManager(this);
		Gdx.input.setInputProcessor(inputManager);
		loadAssets();

	}

	public void loadAssets() {
		world.loadTextures(assetManager);
		assetManager.finishLoading();

		world.createTextures(assetManager);
	}

	public void update() {
		if (Gdx.input.isKeyPressed(Keys.UP)) {
			camera.translate(0, 4);
		}
		if (Gdx.input.isKeyPressed(Keys.DOWN)) {
			camera.translate(0, -4);
		}
		if (Gdx.input.isKeyPressed(Keys.LEFT)) {
			camera.translate(-4, 0);
		}
		if (Gdx.input.isKeyPressed(Keys.RIGHT)) {
			camera.translate(4, 0);
		}


		camera.update();
	}

	@Override
	public void render() {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		world.draw(batch);
		batch.end();
		
		update();
	}

	@Override
	public void dispose() {
		batch.dispose();
		world.dispose();
	}

	public OrthographicCamera getCamera() {
		return camera;
	}

	public World getWorld() {
		return world;
	}

	public void setWorld(World world) {
		this.world = world;
	}

}