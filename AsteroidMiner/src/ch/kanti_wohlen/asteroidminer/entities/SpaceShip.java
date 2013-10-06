package ch.kanti_wohlen.asteroidminer.entities;

import ch.kanti_wohlen.asteroidminer.Textures;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
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
		Body body = getPhysicsBody();
		
		Vector2 loc = new Vector2(body.getPosition());
		loc.mul(Entity.BOX2D_TO_PIXEL);
		
		s.setPosition(loc.x, loc.y);
		s.setRotation(body.getAngle() * MathUtils.radiansToDegrees);
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
		ps.setAsBox(0.1f, 0.1f);
		
		return ps;
	}
}
