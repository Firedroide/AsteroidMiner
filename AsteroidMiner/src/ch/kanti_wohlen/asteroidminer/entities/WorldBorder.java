package ch.kanti_wohlen.asteroidminer.entities;

import ch.kanti_wohlen.asteroidminer.Textures;
import ch.kanti_wohlen.asteroidminer.screen.GameScreen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

public class WorldBorder extends Entity {

	private static final WorldBorder[] borders = new WorldBorder[BorderSide.values().length];
	private static final float MOD_WIDTH = Textures.BORDER.getRegion().getRegionWidth();
	private static final float MOD_HEIGHT = Textures.BORDER.getRegion().getRegionHeight();

	private final BorderSide borderSide;
	private final Rectangle boundingBox;

	public static void addBorders(World world) {
		for (int i = 0; i < BorderSide.values().length; ++i) {
			borders[i] = new WorldBorder(world, BorderSide.values()[i]);
		}
	}

	public static void renderAllBorders(SpriteBatch batch, Rectangle visibleRectangle) {
		final Rectangle pixelRect = scaleRectangle(visibleRectangle, BOX2D_TO_PIXEL);

		for (WorldBorder border : borders) {
			if (border == null) continue;
			border.render(batch, pixelRect);
		}
	}

	public static boolean isOutside(Entity e) {
		return isOutside(e.getPhysicsBody());
	}

	public static boolean isOutside(Body b) {
		Vector2 loc = b.getPosition();
		float w = Gdx.graphics.getWidth() * PIXEL_TO_BOX2D;
		float h = Gdx.graphics.getHeight() * PIXEL_TO_BOX2D;
		return Math.abs(loc.x) > GameScreen.WORLD_SIZE + w || Math.abs(loc.y) > GameScreen.WORLD_SIZE + h;
	}

	public WorldBorder(World world, BorderSide side) {
		super(world, createBodyDef(side), createFixture(side));
		borderSide = side;

		final Vector2 pos = new Vector2(side.x, side.y);
		final Vector2 size = side.getObjectSize();
		if (side == BorderSide.LEFT) {
			pos.sub(BorderSide.BORDER_DIAMETER, 0f);
		} else if (side == BorderSide.BOTTOM) {
			pos.sub(0f, BorderSide.BORDER_DIAMETER);
		}
		boundingBox = new Rectangle(pos.x, pos.y, size.x, size.y);
	}

	/**
	 * Does not actually render the world border.<br>
	 * Call <code>WorldBorder.renderAllBorders</code> to render the borders.
	 */
	@Override
	public void render(SpriteBatch batch) {}

	@Override
	public boolean isRemoved() {
		return isRemovedByUser();
	}

	private void render(SpriteBatch batch, Rectangle pixelRect) {
		final Rectangle lineRect = scaleRectangle(borderSide.getLineRectangle(), BOX2D_TO_PIXEL);
		final Rectangle lineIntersect = new Rectangle();
		if (!Intersector.intersectRectangles(lineRect, pixelRect, lineIntersect)) {
			return;
		}

		final Vector2 pixelMod = new Vector2(pixelRect.x % MOD_WIDTH, pixelRect.y % MOD_HEIGHT);
		if (pixelMod.x < 0) pixelMod.x += MOD_WIDTH;
		if (pixelMod.y < 0) pixelMod.y += MOD_HEIGHT;
		final Rectangle modPixelRect = new Rectangle(pixelRect.x - pixelMod.x, pixelRect.y - pixelMod.y,
				pixelRect.width + pixelMod.x, pixelRect.height + pixelMod.y);

		final Rectangle borderRect = scaleRectangle(borderSide.getObjectRectangle(), BOX2D_TO_PIXEL);
		final Rectangle borderIntersect = new Rectangle();
		Intersector.intersectRectangles(borderRect, modPixelRect, borderIntersect);

		Textures.BORDER.draw(batch, borderIntersect.x, borderIntersect.y, borderIntersect.width, borderIntersect.height);
		Textures.BORDER_LINE.draw(batch, lineIntersect.x, lineIntersect.y, lineIntersect.width, lineIntersect.height);
	}

	@Override
	public EntityType getType() {
		return EntityType.WORLD_BORDER;
	}

	@Override
	public Rectangle getBoundingBox() {
		return new Rectangle(boundingBox);
	}

	private static BodyDef createBodyDef(BorderSide side) {
		BodyDef bd = new BodyDef();
		bd.type = BodyType.StaticBody;
		bd.fixedRotation = true;
		bd.gravityScale = 0f;

		bd.position.set(side.getObjectPosition());

		return bd;
	}

	private static FixtureDef createFixture(BorderSide side) {
		final FixtureDef fixture = new FixtureDef();
		final PolygonShape ps = new PolygonShape();
		final Vector2 sh = side.getObjectSize().scl(0.5f);

		ps.setAsBox(sh.x, sh.y, sh, 0f);
		fixture.density = 0f;
		fixture.shape = ps;
		fixture.filter.maskBits = (short) 0xfffe;

		return fixture;
	}

	private static Rectangle scaleRectangle(Rectangle rect, float scale) {
		return new Rectangle(rect.x * scale, rect.y * scale, rect.width * scale, rect.height * scale);
	}

	public enum BorderSide {
		TOP(-GameScreen.WORLD_SIZE, GameScreen.WORLD_SIZE),
		BOTTOM(-GameScreen.WORLD_SIZE, -GameScreen.WORLD_SIZE),
		LEFT(-GameScreen.WORLD_SIZE, -GameScreen.WORLD_SIZE),
		RIGHT(GameScreen.WORLD_SIZE, -GameScreen.WORLD_SIZE);

		private static final float BORDER_DIAMETER = 48f;
		private static final float LINE_DIAMETER = 0.4f;

		public final float x;
		public final float y;

		private BorderSide(float x, float y) {
			this.x = x;
			this.y = y;
		}

		public Vector2 getObjectPosition() {
			float ox = x > 0 ? x : x - BORDER_DIAMETER;
			float oy = y > 0 ? y : y - BORDER_DIAMETER;
			if (this == LEFT || this == RIGHT) {
				oy += BORDER_DIAMETER;
			}
			return new Vector2(ox, oy);
		}

		public Vector2 getObjectSize() {
			switch (this) {
			case TOP:
			case BOTTOM:
				return new Vector2(2f * (GameScreen.WORLD_SIZE + BORDER_DIAMETER), BORDER_DIAMETER);
			case LEFT:
			case RIGHT:
				return new Vector2(BORDER_DIAMETER, 2f * GameScreen.WORLD_SIZE);
			default:
				return null;
			}
		}

		public Rectangle getObjectRectangle() {
			return createRect(getObjectPosition(), getObjectSize());
		}

		public Vector2 getLinePosition() {
			float ox = x > 0 ? x : x - LINE_DIAMETER;
			float oy = y > 0 ? y : y - LINE_DIAMETER;
			if (this == LEFT || this == RIGHT) {
				oy += LINE_DIAMETER;
			}
			return new Vector2(ox, oy);
		}

		public Vector2 getLineSize() {
			switch (this) {
			case TOP:
			case BOTTOM:
				return new Vector2(2f * (GameScreen.WORLD_SIZE + LINE_DIAMETER), LINE_DIAMETER);
			case LEFT:
			case RIGHT:
				return new Vector2(LINE_DIAMETER, 2f * GameScreen.WORLD_SIZE);
			default:
				return null;
			}
		}

		public Rectangle getLineRectangle() {
			return createRect(getLinePosition(), getLineSize());
		}

		private static Rectangle createRect(Vector2 pos, Vector2 size) {
			return new Rectangle(pos.x, pos.y, size.x, size.y);
		}
	}
}
