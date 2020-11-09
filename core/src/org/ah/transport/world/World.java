package org.ah.transport.world;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import org.ah.transport.Transport;
import org.ah.transport.world.generators.PerlinWorldGenerator;
import org.ah.transport.world.generators.SimpleWorldGenerator;
import org.ah.transport.world.populators.MudPopulator;
import org.ah.transport.world.populators.RiverPopulator;
import org.ah.transport.world.populators.TreePopulator;
import org.ah.transport.world.populators.WorldPopulator;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;

import sun.nio.cs.ext.DoubleByte.Decoder_EBCDIC;

public class World {

	public enum Tile {
		EMPTY(0), GRASS(1), WATER(2), DIRT(3), BEACH(4), ROAD(5);

		private int id;

		private Tile(int id) {
			this.id = id;
		}

		public int getID() {
			return id;
		}

		public static Tile getById(int id) {
			for (Tile e : values()) {
				if (e.getID() == id)
					return e;
			}
			return EMPTY;
		}

		public Tile increment() {
			int h_id = 0;
			for (Tile e : values()) {
				if (e.getID() > h_id)
					h_id = e.getID();
			}
			return getById((id + 1) % (h_id + 1));
		}

	}

	public enum Decor {
		NONE(0), CONIFER_TALL(1), CONIFER_SHORT(2), AUTUMN_SHORT(3), AUTUMN_TALL(4), TREE_SHORT(5), TREE_TALL(6);

		private int id;

		private Decor(int id) {
			this.id = id;
		}

		public int getID() {
			return id;
		}

		public static Decor getById(int id) {
			for (Decor e : values()) {
				if (e.getID() == id)
					return e;
			}
			return NONE;
		}

		public static Decor randomTree() {
			Random r = new Random();
			int tree = r.nextInt(3);
			if (tree == 0) {
				if (r.nextBoolean()) {
					return Decor.CONIFER_SHORT;
				} else {
					return Decor.CONIFER_TALL;
				}
			} else if (tree == 1) {
				if (r.nextBoolean()) {
					return Decor.AUTUMN_SHORT;
				} else {
					return Decor.AUTUMN_TALL;
				}
			} else if (tree == 2) {
				if (r.nextBoolean()) {
					return Decor.TREE_SHORT;
				} else {
					return Decor.TREE_TALL;
				}
			}

			return Decor.TREE_SHORT;

		}

		public Decor increment() {
			int h_id = 0;
			for (Decor e : values()) {
				if (e.getID() > h_id)
					h_id = e.getID();
			}
			return getById((id + 1) % (h_id + 1));
		}

	}

	private Texture grassT;
	private Texture dirtT;
	private Texture waterT;
	private Texture roadT;

	private HashMap<String, Texture> roads;
	private HashMap<String, Texture> banks;
	private Texture[] trees;

	private int tHeight;
	private int tWidth;
	private Tile[][] terrain;
	private Decor[][] decoration;
	private int size;

	private float gap_value = 49;
	private Transport transport;

	private WorldGenerator generator;
	private LinkedList<WorldPopulator> populators;

	private List<Point> selected2d;
	private Texture selectedT;

	public World(int size, Transport transport, long seed) {
		this.transport = transport;
		roads = new HashMap<String, Texture>();
		banks = new HashMap<String, Texture>();
		trees = new Texture[Decor.values().length];
		this.size = size;
		tHeight = 57;
		tWidth = 100;

		terrain = new Tile[size][size];
		decoration = new Decor[size][size];

		generator = new PerlinWorldGenerator(this, seed);
		populators = new LinkedList<WorldPopulator>();
		populators.add(new MudPopulator(this, seed));
		populators.add(new RiverPopulator(this, seed));

		populators.add(new TreePopulator(this, seed));

		selected2d = new ArrayList<Point>();
	}

	public Tile getTile(int x, int y) {
		if (x < terrain.length && x > -1 && y < terrain[x].length && y > -1)
			return terrain[x][y];
		return Tile.EMPTY;
	}

	public Decor getDecor(int x, int y) {
		if (x < decoration.length && x > -1 && y < decoration[x].length && y > -1)
			return decoration[x][y];
		return Decor.NONE;
	}

	public void setTile(int x, int y, Tile t) {
		if (x < terrain.length && x > -1 && y < terrain[x].length && y > -1)
			terrain[x][y] = t;
		update(x, y);
	}

	public void setDecor(int x, int y, Decor d) {
		if (x < decoration.length && x > -1 && y < decoration[x].length && y > -1)
			decoration[x][y] = d;
	}

	public void loadTextures(AssetManager assetManager) {
		assetManager.load("tiles/grass.png", Texture.class);
		assetManager.load("tiles/dirtHigh.png", Texture.class);
		assetManager.load("tiles/water.png", Texture.class);
		assetManager.load("tiles/crossroad.png", Texture.class);

		assetManager.load("tiles/roadCornerES.png", Texture.class);
		assetManager.load("tiles/roadCornerNE.png", Texture.class);
		assetManager.load("tiles/roadCornerNW.png", Texture.class);
		assetManager.load("tiles/roadCornerWS.png", Texture.class);
		assetManager.load("tiles/roadEast.png", Texture.class);
		assetManager.load("tiles/roadEndEast.png", Texture.class);
		assetManager.load("tiles/roadEndNorth.png", Texture.class);
		assetManager.load("tiles/roadEndSouth.png", Texture.class);
		assetManager.load("tiles/roadEndWest.png", Texture.class);
		assetManager.load("tiles/roadNorth.png", Texture.class);
		assetManager.load("tiles/roadTEast.png", Texture.class);
		assetManager.load("tiles/roadTSouth.png", Texture.class);
		assetManager.load("tiles/roadTWest.png", Texture.class);
		assetManager.load("tiles/roadTNorth.png", Texture.class);

		assetManager.load("tiles/bridgeNorth.png", Texture.class);
		assetManager.load("tiles/bridgeEast.png", Texture.class);

		assetManager.load("tiles/treeConiferShort.png", Texture.class);
		assetManager.load("tiles/treeConiferTall.png", Texture.class);
		assetManager.load("tiles/treeShort_autumn.png", Texture.class);
		assetManager.load("tiles/treeTall_autumn.png", Texture.class);
		assetManager.load("tiles/treeShort.png", Texture.class);
		assetManager.load("tiles/treeTall.png", Texture.class);

		assetManager.load("tiles/waterCornerEast.png", Texture.class);
		assetManager.load("tiles/waterCornerNorth.png", Texture.class);
		assetManager.load("tiles/waterCornerWest.png", Texture.class);
		assetManager.load("tiles/waterCornerSouth.png", Texture.class);

		assetManager.load("tiles/waterEast.png", Texture.class);
		assetManager.load("tiles/waterNorth.png", Texture.class);
		assetManager.load("tiles/waterWest.png", Texture.class);
		assetManager.load("tiles/waterSouth.png", Texture.class);

		assetManager.load("tiles/waterNoneEast.png", Texture.class);
		assetManager.load("tiles/waterNoneNorth.png", Texture.class);
		assetManager.load("tiles/waterNoneWest.png", Texture.class);
		assetManager.load("tiles/waterNoneSouth.png", Texture.class);

		assetManager.load("tiles/outline.png", Texture.class);

	}

	public void createTextures(AssetManager assetManager) {
		grassT = (Texture) assetManager.get("tiles/grass.png");
		dirtT = (Texture) assetManager.get("tiles/dirtHigh.png");
		waterT = (Texture) assetManager.get("tiles/water.png");
		roadT = (Texture) assetManager.get("tiles/crossroad.png");

		selectedT = (Texture) assetManager.get("tiles/outline.png");

		roads.put("ES", (Texture) assetManager.get("tiles/roadCornerES.png"));
		roads.put("SW", (Texture) assetManager.get("tiles/roadCornerWS.png"));
		roads.put("NE", (Texture) assetManager.get("tiles/roadCornerNE.png"));
		roads.put("NW", (Texture) assetManager.get("tiles/roadCornerNW.png"));

		roads.put("EW", (Texture) assetManager.get("tiles/roadEast.png"));
		roads.put("NS", (Texture) assetManager.get("tiles/roadNorth.png"));

		roads.put("EWa", (Texture) assetManager.get("tiles/bridgeEast.png"));
		roads.put("NSa", (Texture) assetManager.get("tiles/bridgeNorth.png"));

		roads.put("N", (Texture) assetManager.get("tiles/roadEndNorth.png"));
		roads.put("E", (Texture) assetManager.get("tiles/roadEndEast.png"));
		roads.put("S", (Texture) assetManager.get("tiles/roadEndSouth.png"));
		roads.put("W", (Texture) assetManager.get("tiles/roadEndWest.png"));

		roads.put("NES", (Texture) assetManager.get("tiles/roadTEast.png"));
		roads.put("ESW", (Texture) assetManager.get("tiles/roadTSouth.png"));
		roads.put("NSW", (Texture) assetManager.get("tiles/roadTWest.png"));
		roads.put("NEW", (Texture) assetManager.get("tiles/roadTNorth.png"));

		roads.put("NESW", (Texture) assetManager.get("tiles/crossroad.png"));

		trees[Decor.CONIFER_SHORT.id] = (Texture) assetManager.get("tiles/treeConiferShort.png");
		trees[Decor.CONIFER_TALL.id] = (Texture) assetManager.get("tiles/treeConiferTall.png");

		trees[Decor.AUTUMN_SHORT.id] = (Texture) assetManager.get("tiles/treeShort_autumn.png");
		trees[Decor.AUTUMN_TALL.id] = (Texture) assetManager.get("tiles/treeTall_autumn.png");

		trees[Decor.TREE_SHORT.id] = (Texture) assetManager.get("tiles/treeShort.png");
		trees[Decor.TREE_TALL.id] = (Texture) assetManager.get("tiles/treeTall.png");

		banks.put("U", (Texture) assetManager.get("tiles/waterCornerNorth.png"));
		banks.put("D", (Texture) assetManager.get("tiles/waterCornerSouth.png"));
		banks.put("L", (Texture) assetManager.get("tiles/waterCornerWest.png"));
		banks.put("R", (Texture) assetManager.get("tiles/waterCornerEast.png"));

		banks.put("N", (Texture) assetManager.get("tiles/waterNorth.png"));
		banks.put("S", (Texture) assetManager.get("tiles/waterSouth.png"));
		banks.put("W", (Texture) assetManager.get("tiles/waterWest.png"));
		banks.put("E", (Texture) assetManager.get("tiles/waterEast.png"));

		banks.put("u", (Texture) assetManager.get("tiles/waterNoneNorth.png"));
		banks.put("d", (Texture) assetManager.get("tiles/waterNoneSouth.png"));
		banks.put("l", (Texture) assetManager.get("tiles/waterNoneWest.png"));
		banks.put("r", (Texture) assetManager.get("tiles/waterNoneEast.png"));

		banks.put("all", (Texture) assetManager.get("tiles/water.png"));

	}

	public void dispose() {
	}

	public void generate() {
		long t = generator.generate();
		System.out.println(generator.getName() + " world generated in " + t + "ms");

	}

	public void populate() {
		for (WorldPopulator p : populators) {
			long t = p.populate();
			System.out.println(p.getName() + " completed in " + t + "ms");
		}

	}

	public void update(int x, int y) {
		if (getDecor(x, y) != Decor.NONE) {
			if (getTile(x, y) != Tile.GRASS) {
				setDecor(x, y, Decor.NONE);
			}
		}
	}

	public Point get2dPoint(Point pt) {
		float m = gap_value;
		Point tempPt = new Point(0, 0);
		tempPt.x = (int) (((2 * pt.y + pt.x) / 2) / m);
		tempPt.y = (int) (((2 * pt.y - pt.x) / 2) / m);
		return (tempPt);
	}

	public Point getISOcoords(Point pt) {
		float m = gap_value;
		Point tempPt = new Point(0, 0);
		tempPt.x = (int) (pt.x * m - pt.y * m);
		tempPt.y = (int) ((pt.x * m + pt.y * m) / 2);
		return (tempPt);
	}

	public void drawDecor(int x, int y, SpriteBatch batch) {
		Decor decor = getDecor(x, y);
		Point coords2d = new Point(x + 1, y);
		Point isocoords = getISOcoords(coords2d);
		Texture t = trees[decor.getID()];
		if (decor != Decor.NONE)
			batch.draw(t, isocoords.x, isocoords.y);
	}

	public void drawTile(int x, int y, SpriteBatch batch) {
		Tile tile = getTile(x, y);
		Point coords2d = new Point(x, y);
		Point isocoords = getISOcoords(coords2d);
		Texture t = grassT;

		if (tile == Tile.DIRT) {
			t = dirtT;
		} else if (tile == Tile.WATER) {
			t = waterT;
		} else if (tile == Tile.ROAD) {
			t = roadT;

			String code = getRoadCode(x, y);
			if (roads.containsKey(code)) {
				t = roads.get(code);
			} else {
				t = roadT;
			}
		} else if (tile == Tile.GRASS) {
			t = grassT;
			String code = "";
			boolean u = getTile(x + 1, y + 1) == Tile.WATER;
			boolean d = getTile(x - 1, y - 1) == Tile.WATER;
			boolean l = getTile(x - 1, y + 1) == Tile.WATER;
			boolean r = getTile(x + 1, y - 1) == Tile.WATER;

			boolean n = getTile(x, y + 1) == Tile.WATER;
			boolean e = getTile(x + 1, y) == Tile.WATER;
			boolean s = getTile(x, y - 1) == Tile.WATER;
			boolean w = getTile(x - 1, y) == Tile.WATER;

			if (u)
				code = "U";
			if (d)
				code = "D";
			if (l)
				code = "L";
			if (r)
				code = "R";

			if (getTile(x, y + 1) == Tile.WATER)
				code = "N";
			if (getTile(x + 1, y) == Tile.WATER)
				code = "E";
			if (getTile(x, y - 1) == Tile.WATER)
				code = "S";
			if (getTile(x - 1, y) == Tile.WATER)
				code = "W";

			if (w && s) {
				code = "u";
			}

			if (n && e) {
				code = "d";
			}

			if (e && s) {
				code = "l";
			}

			if (n && w) {
				code = "r";
			}

			if (banks.containsKey(code)) {
				t = banks.get(code);

			}
		}

		if (tile != Tile.EMPTY)
			batch.draw(t, isocoords.x, isocoords.y);
		drawDecor(x, y, batch);

		selected2d.forEach((p) -> {
			if (p.x == x && p.y == y) {
				batch.draw(selectedT, isocoords.x, isocoords.y);
			}
		});
	}

	public String getRoadCode(int x, int y) {
		String code = "";
		boolean n = getTile(x, y + 1) == Tile.ROAD;
		boolean e = getTile(x + 1, y) == Tile.ROAD;
		boolean s = getTile(x, y - 1) == Tile.ROAD;
		boolean w = getTile(x - 1, y) == Tile.ROAD;

		boolean na = getTile(x, y + 1) == Tile.WATER;
		boolean ea = getTile(x + 1, y) == Tile.WATER;
		boolean sa = getTile(x, y - 1) == Tile.WATER;
		boolean wa = getTile(x - 1, y) == Tile.WATER;

		if (n)
			code += "N";
		if (e)
			code += "E";
		if (s)
			code += "S";
		if (w)
			code += "W";

		if (code.equals("NS")) {
			if (ea && wa) {
				code += "a";
			}
		}
		if (code.equals("EW")) {
			if (na && sa) {
				code += "a";
			}
		}

		if (code.equals(""))
			code = "NESW";
		return code;
	}

	public void draw(SpriteBatch batch) {

		Vector3 earliest = transport.getCamera().unproject(new Vector3(0, transport.getCamera().viewportHeight, 0));
		Vector3 latest = transport.getCamera().unproject(new Vector3(transport.getCamera().viewportWidth, 0, 0));
		List<Point> toDraw = new ArrayList<Point>();
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < i + 1; j++) {
				toDraw.add(new Point(j, i - j));
			}
		}
		for (int i = 1; i < size + 1; i++) {
			for (int j = 0; j < size - i; j++) {
				toDraw.add(new Point(i + j, size - j - 1));
			}
		}
		java.util.Collections.reverse(toDraw);

		toDraw.forEach((p) -> {
			Point isocoords = getISOcoords(p);
			if (isocoords.x > earliest.x - 100) {
				if (isocoords.y > earliest.y - 60) {
					if (isocoords.x < latest.x + 60) {
						if (isocoords.y < latest.y + 60) {

							drawTile(p.x, p.y, batch);
						}
					}
				}
			}
		});

	}

	// TODO remove DEBUG
	public void cycleTileFromISO(int x, int y) {
		Point p = get2dPoint(new Point(x - 50, y - 21));
		Tile tile = getTile(p.x, p.y);
		setTile(p.x, p.y, tile.increment());

	}

	public void setTileISO(int x, int y, Tile t) {
		Point p = get2dPoint(new Point(x - 50, y - 21));
		setTile(p.x, p.y, t);
	}
	
	public void setSelection(Tile t) {
		for (Point p : selected2d) {
			setTile(p.x, p.y, t);
		}
	}
	public void setSelection(Decor t) {
		for (Point p : selected2d) {
			setDecor(p.x, p.y, t);
		}
	}
	public void cycleSelection() {
		for (Point p : selected2d) {
			Tile tile = getTile(p.x, p.y);
			setTile(p.x, p.y, tile.increment());
		}
	}

	public void setDecorISO(int x, int y, Decor t) {
		Point p = get2dPoint(new Point(x - 50, y - 21));
		setDecor(p.x, p.y, t);
	}

	public float getGapValue() {
		return gap_value;
	}

	public void setGapValue(float gap_value) {
		this.gap_value = gap_value;
	}

	public int getSize() {
		return size;
	}

	public void clearSelection() {
		selected2d.clear();
	}

	public void addSelection(Point... points) {
		for (Point p : points) {
			selected2d.add(p);
		}
	}
	
	public void addISOtoSelection(int x, int y) {
		Point p = get2dPoint(new Point(x - 50, y - 21));
		 selected2d.add(p);
	}
	
	public void addISOsquaretoSelection(int x1, int y1, int x2, int y2) {
		Point start = get2dPoint(new Point(x1 - 50, y1 - 21));
		Point end = get2dPoint(new Point(x2 - 50, y2 - 21));
		int xs = start.x;
		int xl = end.x;
		if (start.x > end.x) {
			xs = end.x;
			xl = start.x;
		}
		
		int ys = start.y;
		int yl = end.y;
		if (start.y > end.y) {
			ys = end.y;
			yl = start.y;
		}

		for (int x = xs; x <= xl; x++) {
			for (int y = ys; y <= yl; y++) {
				addSelection(new Point(x, y));				
			}
		}
		
	}
}
