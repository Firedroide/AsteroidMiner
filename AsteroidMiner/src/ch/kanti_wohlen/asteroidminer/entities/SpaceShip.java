package ch.kanti_wohlen.asteroidminer.entities;

import ch.kanti_wohlen.asteroidminer.Textures;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;

public class SpaceShip extends Entity {
	
	public SpaceShip(World world) {
		super(world, createBodyDef(), createCollisionBox());
	}
	
	@Override
	public void render(SpriteBatch batch) {
		Sprite s = Textures.SPACESHIP;
		positionSprite(s);
		s.draw(batch);
	}
	
	@Override
	public boolean isRemoved() {
		return false;
	}
	
	public Laser fireLaser() {
		return new Laser(getPhysicsBody().getWorld(), this);
	}
	
	private static BodyDef createBodyDef() {
		BodyDef bd = new BodyDef();
		bd.allowSleep = false;
		bd.type = BodyType.DynamicBody;
		bd.angularDamping = 10f;
		bd.linearDamping = 2.5f;
		bd.position.set(2f, 2f);
		
		return bd;
	}
	
	private static Shape createCollisionBox() {
		PolygonShape ps = new PolygonShape();
		ps.setAsBox(Textures.SPACESHIP.getWidth() / 2f * PIXEL_TO_BOX2D, Textures.SPACESHIP.getHeight() / 2f * PIXEL_TO_BOX2D);
		
		return ps;
	}
}
