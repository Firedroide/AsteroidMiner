package ch.kanti_wohlen.asteroidminer.entities;

import ch.kanti_wohlen.asteroidminer.Textures;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

public class Asteroid extends Entity {
	
	private static final float PI2 = 2 * MathUtils.PI;
	
	public Asteroid(World world, Vector2 location, float radius) {
		super(world, createBodyDef(location), createCircle(radius));
	}
	
	@Override
	public void render(SpriteBatch batch) {
		Sprite s = Textures.ASTEROID;
		positionSprite(s);
		s.draw(batch);
	}
	
	@Override
	public boolean isRemoved() {
		return false;
	}
	
	private static BodyDef createBodyDef(Vector2 position) {
		final BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyType.DynamicBody;
		bodyDef.position.set(position);
		bodyDef.angle = MathUtils.random(PI2);
		
		return bodyDef;
	}
	
	private static Shape createCircle(float radius) {
		final CircleShape cs = new CircleShape();
		cs.setRadius(radius);
		return cs;
	}
}
