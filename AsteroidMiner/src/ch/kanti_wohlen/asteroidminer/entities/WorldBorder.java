package ch.kanti_wohlen.asteroidminer.entities;

import ch.kanti_wohlen.asteroidminer.Textures;
import ch.kanti_wohlen.asteroidminer.screen.GameScreen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.scenes.scene2d.utils.TiledDrawable;

public class WorldBorder extends Entity {

	private final BorderSide borderSide;

	public static void addBorders(World world) {
		for (BorderSide side : BorderSide.values()) {
			new WorldBorder(world, side);
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
	}

	@Override
	public void render(SpriteBatch batch) {
		TiledDrawable border = Textures.BORDER;
		TiledDrawable line = Textures.BORDER_LINE;
		Vector2 borderLoc = getPhysicsBody().getPosition().cpy().scl(BOX2D_TO_PIXEL);
		Vector2 borderSize = borderSide.getObjectSize().scl(BOX2D_TO_PIXEL);
		Vector2 lineLoc = borderSide.getLinePosition().scl(BOX2D_TO_PIXEL);
		Vector2 lineSize = borderSide.getLineSize().scl(BOX2D_TO_PIXEL);

		border.draw(batch, borderLoc.x, borderLoc.y, borderSize.x, borderSize.y);
		line.draw(batch, lineLoc.x, lineLoc.y, lineSize.x, lineSize.y);
	}

	@Override
	public EntityType getType() {
		return EntityType.WORLD_BORDER;
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

	public enum BorderSide {
		TOP(-GameScreen.WORLD_SIZE, GameScreen.WORLD_SIZE),
		BOTTOM(-GameScreen.WORLD_SIZE, -GameScreen.WORLD_SIZE),
		LEFT(-GameScreen.WORLD_SIZE, -GameScreen.WORLD_SIZE),
		RIGHT(GameScreen.WORLD_SIZE, -GameScreen.WORLD_SIZE);

		private static final float BORDER_DIAMETER = 64f;
		private static final float LINE_DIAMETER = 0.4f;

		private final float x;
		private final float y;

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
	}
}
