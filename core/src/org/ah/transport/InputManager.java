package org.ah.transport;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector3;

import java.awt.Point;

import org.ah.transport.world.World.Decor;
import org.ah.transport.world.World.Tile;

import com.badlogic.gdx.Input.Keys;

public class InputManager implements InputProcessor {
	private Transport transport;
	private int lastX;
	private int lastY;
	private Point mouse;

	private boolean left = false;
	private boolean right = false;
	private boolean middle = false;

	private Point startPoint;

	public InputManager(Transport transport) {
		this.transport = transport;
		lastX = 0;
		lastY = 0;

		mouse = new Point(0, 0);
	}

	@Override
	public boolean keyDown(int keycode) {
		if (keycode == Keys.E) {
			transport.getWorld().cycleSelection();
		}
		if (keycode == Keys.R) {
			transport.getWorld().setSelection(Tile.ROAD);
		}

		if (keycode == Keys.T) {
			transport.getWorld().setSelection(Decor.randomTree());
		}

		if (keycode == Keys.W) {
			transport.getWorld().setSelection(Tile.WATER);
		}

		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		lastX = screenX;
		lastY = screenY;
		if (button == 1) {
			right = true;
			Vector3 unproject = transport.getCamera().unproject(new Vector3(mouse.x, mouse.y, 0));

			startPoint = new Point((int) (unproject.x), (int) (unproject.y));
			transport.getWorld().clearSelection();

		} else if (button == 2) {
			middle = true;
		} else if (button == 0) {
			left = true;
		}
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		if (button == 1) {
			right = false;
//			transport.getWorld().clearSelection();
		} else if (button == 2) {
			middle = false;
		} else if (button == 0) {
			left = false;
		}
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		mouse.setLocation(screenX, screenY);
		if (left) {

			float z = transport.getCamera().zoom;
			transport.getCamera().translate((lastX - screenX) * z, (screenY - lastY) * z);

			lastX = screenX;
			lastY = screenY;

//			Vector3 unproject = transport.getCamera().unproject(
//					new Vector3(transport.getCamera().viewportWidth / 2, transport.getCamera().viewportHeight / 2, 0));
//			Point centreTile = transport.getWorld().get2dPoint(new Point((int) (unproject.x), (int) (unproject.y)));
//			Point togo = null;
//			if (centreTile.x < 0) {
//				togo = new Point(0, centreTile.y);
//			}
//			if (centreTile.y < 0) {
//				togo = new Point(centreTile.x, 0);
//			}
//			if (togo != null) {
//				Point isotogo = transport.getWorld().getISOcoords(togo);
////				Vector3 project = transport.getCamera().project(new Vector3(isotogo.x, isotogo.y, 0));
////				isotogo.x -= transport.getCamera().viewportWidth / 2;
////				isotogo.y -= transport.getCamera().viewportHeight / 2;
//				transport.getCamera().position.x = isotogo.x;
//				transport.getCamera().position.y = isotogo.y;
//			}
		}

		if (right) {
			Vector3 unproject = transport.getCamera().unproject(new Vector3(mouse.x, mouse.y, 0));

			if (!Gdx.input.isKeyJustPressed(Keys.SHIFT_LEFT)) {
				transport.getWorld().clearSelection();
			}

			transport.getWorld().addISOsquaretoSelection(startPoint.x, startPoint.y, (int) (unproject.x),
					(int) (unproject.y));
		}
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {

		mouse.setLocation(screenX, screenY);
		Vector3 unproject = transport.getCamera().unproject(new Vector3(mouse.x, mouse.y, 0));

		if (!Gdx.input.isKeyJustPressed(Keys.SHIFT_LEFT)) {
			transport.getWorld().clearSelection();
		}
		transport.getWorld().addISOtoSelection((int) (unproject.x), (int) (unproject.y));

		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		if (amount < 0) {
			transport.getCamera().zoom *= 0.8f;
		} else {
			transport.getCamera().zoom *= 1 / 0.8f;
		}

		if (transport.getCamera().zoom > 3) {
			transport.getCamera().zoom = 3;
		}
		if (transport.getCamera().zoom < 0.1f) {
			transport.getCamera().zoom = 0.1f;
		}
		return false;
	}

}
