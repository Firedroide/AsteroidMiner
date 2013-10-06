package ch.kanti_wohlen.asteroidminer.entities;

import ch.kanti_wohlen.asteroidminer.Textures;
import ch.kanti_wohlen.asteroidminer.screen.GameScreen;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

public class Laser extends Entity {
	
	private static final float SPEED = 60f;
	private final SpaceShip ship;
	
	public Laser(World world, SpaceShip spaceShip) {
		super(world, createBodyDef(spaceShip), createCollisionBox());
		ship = spaceShip;
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
		if (super.isRemoved()) return true;
		Vector2 loc = getPhysicsBody().getPosition();
		return Math.abs(loc.x) > GameScreen.WORLD_SIZE || Math.abs(loc.y) > GameScreen.WORLD_SIZE;
	}
	
	public SpaceShip getShooter() {
		return ship;
	}
	
	private static BodyDef createBodyDef(SpaceShip ship) {
		BodyDef bd = new BodyDef();
		bd.allowSleep = false;
		bd.type = BodyType.KinematicBody;
		bd.fixedRotation = true;
		bd.gravityScale = 0f;
		
		final Body body = ship.getPhysicsBody();
		final float x = -MathUtils.sin(body.getAngle());
		final float y = MathUtils.cos(body.getAngle());
		final Vector2 vertex = new Vector2(0f, 0f);
		((PolygonShape) body.getFixtureList().get(0).getShape()).getVertex(3, vertex);
		final float d = vertex.y / 2f;
		
		bd.position.set(body.getPosition().x + x * d, body.getPosition().y + y * d);
		bd.linearVelocity.set(x * SPEED, y * SPEED);
		bd.angle = body.getAngle();
		
		return bd;
	}
	
	private static Shape createCollisionBox() {
		PolygonShape ps = new PolygonShape();
		ps.setAsBox(0.1f, 0.02f);
		return ps;
	}
}
