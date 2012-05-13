/* 
Cobosoda
Copyright 2007 Justin Day 

This file is part of Cobosoda.

Cobosoda is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

Cobosoda is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Cobosoda.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.jplt.cobosoda;

import java.util.ArrayList;
import java.util.List;

public class Joint implements IPart<Joint> {

	public static final String VERSION = "$Revision: 1.10 $";
	public static final String ID = "$Id: Joint.java,v 1.10 2008/07/19 21:36:35 thumbuki Exp $";
	private boolean fixed;
	private double dx;
	private double dy;
	private double x;
	private double y;
	private double t;
	private double dt;
	private MVector force = new MVector();
	private MVector velocity = new MVector();
	private MVector acceleration = new MVector();
	private boolean dragging = false;
	private boolean hovering = false;
	private double mass = Config.JOINT_MASS;
	private Model parent = null;
	private List<Limb> limbList = new ArrayList<Limb>();  /* Attached springs */

	private double lastx;
	private double lasty;

	public Joint(Model parent, double nx, double ny, boolean nf) {
		this.parent = parent;
		x = nx;
		y = ny;
		lastx = x;
		lasty = y;

		t = 0;
		fixed = nf;
	}

	public Joint(Model parent, double nx, double ny) {
		this.parent = parent;
		x = nx;
		y = ny;
		lastx = x;
		lasty = y;

		t = 0;
		fixed = false;
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public double getDX() {
		return dx;
	}

	public double getDY() {
		return dy;
	}

	public int getIndex() {
		return parent.getIndexOfJoint(this);
	}

	public boolean isFixed() {
		return fixed;
	}

	public int getLimbCount() {
		return limbList.size();
	}

	public Limb getLimb(int i) {
		return limbList.get(i);
	}

	public boolean getDragging() {
		return dragging;
	}

	public void setDragging(boolean b) {
		this.dragging = b;
	}

	public void setX(double value) {
		x = value;
	}

	public void setY(double value) {
		y = value;
	}

	public void setHovering(boolean b) {
		this.hovering = b;
	}

	public boolean getHovering() {
		return this.hovering;
	}

	public void setMass(double m) {
		mass = m;
	}

	public double getMass() {
		return mass;
	}

	public Model getParentModel() {
		return parent;
	}

	public void setParent(Model model) {
		parent = model;
	}

	public void translate() {
		moveTo(x + dx, y + dy);
	}

	public void moveTo(double nx, double ny) {
		x = nx;
		y = ny;

		lastx = x;
		lasty = y;
	}

	public double getLastX() {
		return lastx;
	}

	public double getLastY() {
		return lasty;
	}

	public void onTimeSlice(double ndt) {
		MVector limbForce;
		MVector gravityForce;

		/* Move time */
		dt = ndt;
		t += dt;

		for (Limb limb : limbList) {
			limbForce = limb.resultantForce(this);
			applyForce(limbForce);
		}

		/* Apply gravity */
		gravityForce = new MVector(Config.GRAVITY_ANGLE, World.getGravity());
		gravityForce.multiplyBy(mass);
		applyForce(gravityForce);

		/* Nix the force if starting friction applies */
		if (y <= 0.0) {
			double a = force.getAngle();

			if (((a > Math.PI && a < 2 * Math.PI) || (a < 0 && a > -2 * Math.PI)) &&
					force.Xvector().getMagnitude() < Config.FRICTION_START) {
				force = new MVector();
			}
		}

		/* TODO: Is this necessary? What is its function? */
		force.roundTo(Config.ROUND_TO);

		// Calculate acceleration
		acceleration = force;
		acceleration.divideBy(mass);

		/* Reset force for next time slice */
		force = new MVector();

		/* Calculate velocity */
		acceleration.multiplyBy(dt);
		velocity.add(acceleration);
		velocity.multiplyBy(World.getFriction());

		/* Velocity becomes translation  */
		dx = velocity.getX() * dt;
		dy = velocity.getY() * dt;
	}

	public void applyForce(double na, double nf) {
		force.add(new MVector(na, nf));
	}

	public void applyForce(MVector f) {
		force.add(f);
	}

	public void bounceY() {
		if (y < 0) {
			y = 0;

			if (dy < -0.0175) {
				velocity.absY();
			} else {
				velocity = new MVector();
			}
		}
	}

	public void bounceX() {
		if (x <= 0) {
			x = 0;

			velocity.absX();
			parent.getMechanics().setMuscleFlow(1);
		} else if (x >= World.getWidth()) {
			x = World.getWidth();

			velocity.nabsX();
			parent.getMechanics().setMuscleFlow(-1);
		}
	}

	/**
	 * Returns true if the two joints share a common spring.
	 * 
	 * @param j
	 * @return  True if common spring exists, else false.
	 */
	public boolean isCommonLimb(Joint j) {
		/* Problem checked here. Seek getCommonLimb. */
		return (getCommonLimb(j) != null);
	}

	/**
	 * Return the string instance common to this joint and specified joint.
	 * If a common string does not exist, return null.
	 * 
	 * @param j Opposite joint.
	 * @return  Common string or null if not common string exists. 
	 */
	public Limb getCommonLimb(Joint j) {
		Joint j2;

		for (Limb s : limbList) {
			j2 = findOppositeJoint(s);

			if (j2 != null && (j2.getIndex() == j.getIndex())) {
				return s;
			}
		}

		return null;
	}

	/**
	 * Returns the joint on the opposite side of a specified spring.
	 * 
	 * @param s Spring to find opposite joint.
	 * @return  Joint instance on opposite side of string.
	 */
	public Joint findOppositeJoint(Limb s) {
		int idx = s.getIndexOfJoint(this);

		if (idx == 0 || idx == 1) {
			return s.getJoint(1 - idx);
		}
		
		return null;
	}

	/**
	 * Attach this joint to a specified spring instance.
	 * 
	 * @param s Spring joint attaches itself to.
	 */
	public void attachLimb(Limb s) {
		limbList.add(s);
	}

	/**
	 * Detach the specified spring instance from this join
	 * @param s Spring to detach
	 */
	public void detachLimb(Limb s) {
		limbList.remove(s);
	}

	public void elapseTime(int ms) {
		onTimeSlice(ms / 1000.0 * Config.TIME_PER_FRAME);
	}

	public boolean collides(double cx, double cy) {
		double radius = (Config.JOINT_RADIUS / Config.PIXELS_PER_METER) + 0.05;

		return (cx >= x - radius &&
				cx <= x + radius &&
				cy >= y - radius &&
				cy <= y + radius);
	}

	/**
	 * Returns the springs attached to this joint.
	 * 
	 * @return List of springs attached to this joint.
	 */
	public List<Limb> getLimbs() {
		return limbList;
	}

	/**
	 * Returns a clone of this joint
	 */
	@Override
	public Joint clone() {
		Joint clone = new Joint(this.parent, this.x, this.y, this.fixed);

		return clone;
	}

	/**
	 * Creates a clone but with a new parent model
	 * @param parent parent model that the clone should be associated with
	 * @return a cloned joint
	 */
	public Joint clone(Model parent) {
		Joint clone = new Joint(parent, this.x, this.y, this.fixed);

		return clone;
	}
}
