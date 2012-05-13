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

import java.io.FileReader;
import java.util.*;
import java.io.*;

public class Model {

	public static final String VERSION = "$Revision: 1.13 $";
	public static final String ID = "$Id: Model.java,v 1.13 2009/05/31 17:46:49 potatono Exp $";
	private static final long serialVersionUID = -2808505005831015007L;
	public static final int EVENT_TIME = 1;
	public static final int EVENT_COLLISION = 2;
	public static final int EVENT_SPRING = 3;
	public static final int COLLISION_RIGHT = 1;
	public static final int COLLISION_LEFT = 2;
	public static final int COLLISION_BOTTOM = 3;
	public static final int SPRING_FOO = 1;
	public static final int SPRING_BAR = 2;
	private double distance = 0;
	private double lastX = 0;
	private String name = "No Name";
	private ModelMechanics mechanics = null;
	private ModelObservable timeObservers = null;
	private ModelObservable collisionObservers = null;
	private ModelObservable springObservers = null;
	private List<Joint> joints = new ArrayList<Joint>();
	private List<Limb> limbs = new ArrayList<Limb>();
	/* Members for CoboArcade */
	private String creatorInitials = new String("___");
	private int mutationIndex = 0;  /* Index of evolution. 0 = original. */
	private double distanceWalked = 0.0;  /* Distance walked */
	
	/**
	 * Model class constructor.
	 */
	public Model() {
		mechanics = new ModelMechanics();
	}

	/**
	 * Set name of model.
	 * @param name New name.
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Return name of model.
	 * @return Name of model.
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Return spring mechanics.
	 * 
	 * @return SpringMechanics.
	 */
	public ModelMechanics getMechanics() {
		return mechanics;
	}

	/**
	 * Create new joint, set cartesian coordinates, and specify
	 * fixed status.
	 * 
	 * @param nx     X coordinate.
	 * @param ny     Y coordinate.
	 * @param fixed  Is fixed?
	 */
	public Joint spawnJoint(double nx, double ny, boolean fixed) {
		Joint joint = new Joint(this, nx, ny);
		joints.add(joint);
		return joint;
	}

	/**
	 * Create a new joint by cloning a joint
	 * @param joint Joint to spawn by cloning with a new parent.
	 */
	public Joint spawnJoint(Joint joint) {
		Joint j = joint.clone(this);
		joints.add(j);
		return j;
	}

	/**
	 * Return list of joints.
	 * @return Joint list.
	 */
	public List<Joint> getJoints() {
		return joints;
	}

	/**
	 * Return the number of joints.
	 * 
	 * @return The number of joints.
	 */
	public int getNumberOfJoints() {
		return joints.size();
	}

	/**
	 * Return joint at specified joint list index.
	 * 
	 * @param i Index of joint list.
	 * @return  Joint at index i.
	 */
	public Joint getJoint(int i) {
		return joints.get(i);
	}

	/**
	 * Return the index of a specified joint.
	 * 
	 * @param j  Joint to get index of.
	 * @return   Index of j in list Joints.
	 */
	public int getIndexOfJoint(Joint j) {
		return joints.indexOf(j);
	}

	/* TODO: Re-implement removeJoint.  Must remove references
	 * to attached springs.
	 */
	/**
	 * Remove a joint from model.
	 * 
	 * @param joint Index of joint to remove.
	 */
//	public void removeJoint(int joint) {
//		joints.remove(joint);
//	}

	/**
	 * Remove a joint from model
	 * 
	 * @param joint Joint to remove
	 */
	public void removeJoint(Joint joint) {
		ArrayList<Limb> toss = new ArrayList<Limb>();
		
//		for (Limb l : limbs) {
//			toss.add(l);
//		}
//		
//		for (Limb l : toss) {
//			removeLimb(l);
//		}
		
		joints.remove(joint);
	}

	/**
	 * Create new spring, attach to joints, and add to spring list.
	 * 
	 * @param from From joint, by index.
	 * @param to   To joint, by index.
	 * @param k    The spring constant of this spring
	 * @throws Exception
	 */
	public Spring spawnSpring(int from, int to, double k) throws Exception {
		Joint fromJoint = joints.get(from);
		Joint toJoint = joints.get(to);

		if (fromJoint != null && toJoint != null) {
			return spawnSpring(fromJoint, toJoint, k);
		}

		return null;
	}

	public Spring spawnSpring(Joint from, Joint to, double k) {
		// Make sure we don't double connect
		if (!from.isCommonLimb(to)) {
			Spring s = new Spring(this, k);
			limbs.add(s);
			from.attachLimb(s);
			to.attachLimb(s);
			s.attachJoint(from, to);
			return s;
		}

		return null;
	}

	/**
	 * Create new spring, attach to joints, and add to spring list.
	 * 
	 * @deprecated Use spawnSpring(from,to,k) instead
	 * @param from From joint, by index.
	 * @param to   To joint, by index.
	 * @throws Exception
	 */
	public Spring spawnSpring(int from, int to) throws Exception {
		return spawnSpring(from, to, mechanics.getSpringK());
	}

	/**
	 * Create new spring muscle, attach to joints,
	 * set muscle parameters, and add to spring list.
	 * 
	 * @param from  From joint, by index.
	 * @param to    To joint, by index.
	 * @param freq  Frequency.
	 * @param amp   Amplitude.
	 * @param ofs   Offset.
	 * @throws Exception
	 * @deprecated  Use spawnMuscle(from,to,k,freq,amp,ofs) instead
	 */
	public Muscle spawnMuscle(int from, int to, double freq, double amp, double ofs) throws Exception {
		return spawnMuscle(from, to, mechanics.getSpringK(), freq, amp, ofs);
	}

	/**
	 * Create new spring muscle, attach to joints,
	 * set muscle parameters, and add to spring list.
	 * 
	 * @param from  From joint, by index.
	 * @param to    To joint, by index.
	 * @param k     Spring constant
	 * @param freq  Frequency.
	 * @param amp   Amplitude.
	 * @param ofs   Offset.
	 * @throws Exception
	 */
	public Muscle spawnMuscle(int from, int to, double k, double freq, double amp, double ofs) throws Exception {
		Joint fromJoint = joints.get(from);
		Joint toJoint = joints.get(to);

		if (!fromJoint.isCommonLimb(toJoint)) {
			Muscle s = new Muscle(this, k, freq, amp, ofs);
			limbs.add(s);
			fromJoint.attachLimb(s);
			toJoint.attachLimb(s);
			s.attachJoint(fromJoint, toJoint);
			return s;
		}

		return null;
	}

	public Muscle spawnMuscle(Joint fromJoint, Joint toJoint, double k, double freq, double amp, double ofs) throws Exception {
//		Joint fromJoint = joints.i;
//		Joint toJoint = joints.get(to);

		if (!fromJoint.isCommonLimb(toJoint)) {
			Muscle s = new Muscle(this, k, freq, amp, ofs);
			limbs.add(s);
			fromJoint.attachLimb(s);
			toJoint.attachLimb(s);
			s.attachJoint(fromJoint, toJoint);
			return s;
		}

		return null;
	}

//	public Spring spawnMuscle(Joint from, Joint to, double k, double freq, double amp, double ofs) throws Exception {
//		// Make sure we don't double connect
//		if (!from.isCommonLimb(to)) {
//			Spring s = new Spring(this, k);
//			limbs.add(s);
//			from.attachLimb(s);
//			to.attachLimb(s);
//			s.attachJoint(from, to);
//			return s;
//		}
//
//		return null;
//	}
	
	
	/**
	 * Spawns a limb by cloning it
	 * @param limb
	 */
	public Limb spawnLimb(Limb limb) {
		Limb clone;

		if (limb instanceof Muscle) {
			clone = (Muscle) limb.clone(this);
		} else if (limb instanceof Spring) {
			clone = (Spring) limb.clone(this);
		} else {
			clone = limb.clone(this);
		}

		int from = limb.getJoint(0).getIndex();
		int to = limb.getJoint(1).getIndex();
		Joint fromJoint = getJoint(from);
		Joint toJoint = getJoint(to);

		fromJoint.attachLimb(clone);
		toJoint.attachLimb(clone);
		clone.attachJoint(fromJoint, toJoint);

		limbs.add(clone);

		return clone;
	}

	/**
	 * Detach joints from limb, limb from joints and limb from model
	 * @param l Limb to remove
	 */
	public void removeLimb(Limb l) {
		l.detachAll();
		limbs.remove(l);
	}

	/**
	 * Add spring to list.
	 * @param s  Spring to add to list.
	 */
	public void addToLimbs(Spring s) {
		limbs.add(s);
	}

	/**
	 * Return the number of springs.
	 * 
	 * @return Number of springs.
	 */
	public int getNumberOfLimbs() {
		return limbs.size();
	}

	/**
	 * Return the index of Spring s.
	 * 
	 * @param s  Spring to get index.
	 * @return   Index of Spring s.
	 */
	public int getIndexOfLimb(Limb s) {
		return limbs.indexOf(s);
	}

	/**
	 * Return List of springs.
	 * 
	 * @return Spring list.
	 */
	public List<Limb> getLimbs() {
		return limbs;
	}

	public ArrayList<Spring> getSprings() {
		ArrayList<Spring> springs = new ArrayList<Spring>();
		
		for (Limb l : limbs) {
			if (l instanceof Spring) {
				springs.add((Spring) l);
			}
		}
		
		return springs;
	}
	
	public ArrayList<Muscle> getMuscles() {
		ArrayList<Muscle> muscles = new ArrayList<Muscle>();
		
		for (Limb l : limbs) {
			if (l instanceof Muscle) {
				muscles.add((Muscle) l);
			}
		}
		
		return muscles;
	}
	
	public void elapseTime(int ms) {
		handleForces(ms);
		handleTranslation();
		handleSprings();
		measureDistance();

		if (timeObservers != null) {
			timeObservers.notifyTime(this, ms);
		}
	}

	public void handleForces(int ms) {
		mechanics.elapseTime(ms);

		for (Joint joint : joints) {
			joint.elapseTime(ms);
		}
	}

	public void handleTranslation() {
		for (Joint joint : joints) {
			joint.translate();

			if (joint.getX() < 0) {
				// TODO FIXME Make this a collisionObserver
				if (!Config.ARCADE_MODE)
					joint.bounceX();

				if (collisionObservers != null) {
					collisionObservers.notifyCollision(this, joint, COLLISION_LEFT);
				}
			} 
			else if (joint.getX() > World.getWidth()) {
				// TODO FIXME Make this a collisionObserver
				if (!Config.ARCADE_MODE) 
					joint.bounceX();
				
				measureDistance();
				resetDistanceMarker();

				if (collisionObservers != null) {
					collisionObservers.notifyCollision(this, joint, COLLISION_RIGHT);
				}
			}

			if (joint.getY() < 0) {
				joint.bounceY();

				if (collisionObservers != null) {
					collisionObservers.notifyCollision(this, joint, COLLISION_BOTTOM);
				}
			}
		}
	}


	/* TODO: Most likely pointless to observe for changes, since changes
	 * between frames is almost always inevitable.  Perhaps observer models
	 * should just poll spring values at the frame rate?
	 */
	/**
	 * Notify Spring Observers of changes.
	 */
	public void handleSprings() {
		for (Limb s : limbs) {
			if (springObservers != null) {
				springObservers.notifySpring(this, s, SPRING_FOO);
			}
		}
	}

	/**
	 * Return furthest joint.
	 * 
	 * @return Furthest joint.
	 */
	public Joint getDistantJoint() {
		Joint furthest = joints.get(0);
		LocationComparator comparator = new LocationComparator();

		for (Joint j : joints) {
			if (comparator.compare(j, furthest) < 0) {
				furthest = j;
			}
		}

		return furthest;
	}

	/**
	 * Return distance traveled of furthest joint.
	 * 
	 * @return Distance traveled.
	 */
	public double measureDistance() {
		double x = this.getDistantJoint().getX();
		double delta;

		if (lastX == 0) {
			delta = 0;
		} else if (mechanics.getMuscleFlow() > 0) {
			delta = x - lastX;
		} else {
			delta = lastX - x;
		}

		lastX = x;
		distance += delta;
		return delta;
	}

	/**
	 * Reset distance marker.
	 */
	public void resetDistanceMarker() {
		lastX = getDistantJoint().getX();
	}

	/**
	 * Return distance.
	 * 
	 * @return Distance.
	 */
	public double getDistanceMarkerX() {
		return lastX;
	}

	public double getDistanceTravelled() {
		return distance;
	}

	/**
	 * Clone this model.
	 * 
	 * @return Cloned model.
	 */
	public Model clone() {
		Model clone = new Model();
		clone.setName(name);
		
		/* New for CoboArcade. */
		clone.setCreatorInitials(creatorInitials);
		clone.setMutationIndex(mutationIndex);
		clone.setDistanceWalked(distanceWalked);
		
		/* 
		 * First we clone the joints using a simple iterator.
		 * They need to all exist before we start adding springs.
		 */
		for (Joint j : joints) {
			clone.spawnJoint(j.getX(), j.getY(), !j.isFixed());
		}

		/* Next we add the springs */
		for (Limb s : limbs) {
			try {
				if (s instanceof Muscle) {
					clone.spawnMuscle(
							s.getJoint(0).getIndex(),
							s.getJoint(1).getIndex(),
							((Muscle) s).getK(),
							((Muscle) s).getFrequency(),
							((Muscle) s).getAmplitude(),
							((Muscle) s).getOffset());
				} else {
					clone.spawnSpring(
							s.getJoint(0).getIndex(),
							s.getJoint(1).getIndex(),
							((Spring) s).getK());
				}
			} catch (Exception e) {
				System.out.println("Could not add spring: " + e.getMessage());
			}
		}

		clone.distance = 0;
		clone.lastX = 0;

		return clone;
	}

	public static Model load(String path) {
		try {
			DefinitionReader r = new DefinitionReader(new FileReader(path));
			Model m = r.read();
			return m;
		} catch (Exception e) {
			System.out.println(path + ": " + e.getMessage());
			return null;
		}
	}

	public void save(String path) {
		try {
			DefinitionWriter w = new DefinitionWriter(new FileWriter(path));
			w.write(this);
		} catch (Exception e) {
			System.out.println(path + ": " + e.getMessage());
		}
	}

	/* Add Observer */
	public void addTimeObserver(Observer o) {
		ensureTimeObservers();
		timeObservers.addObserver(o);
	}

	public void addCollisionObserver(Observer o) {
		ensureCollisionObservers();
		collisionObservers.addObserver(o);
	}

	public void addSpringObserver(Observer o) {
		ensureSpringObservers();
		springObservers.addObserver(o);
	}

	/* Ensure Observers */
	private void ensureTimeObservers() {
		if (timeObservers == null) {
			timeObservers = new ModelObservable();
		}
	}

	private void ensureCollisionObservers() {
		if (collisionObservers == null) {
			collisionObservers = new ModelObservable();
		}
	}

	private void ensureSpringObservers() {
		if (springObservers == null) {
			springObservers = new ModelObservable();
		}
	}

	/* Delete Observer. Currently unused. */
	public void deleteTimeObserver(Observer o) {
		ensureTimeObservers();
		timeObservers.deleteObserver(o);
	}

	public void deleteCollisionObserver(Observer o) {
		ensureCollisionObservers();
		collisionObservers.deleteObserver(o);
	}

	public void deleteSpringObserver(Observer o) {
		ensureSpringObservers();
		springObservers.deleteObserver(o);
	}

	/**
	 * Removes orphaned joints from a model
	 */
	public void removeDisconnectedJoints() {
		for (int i = 0; i < joints.size(); i++) {
			if (joints.get(i).getLimbCount() == 0) {
				joints.remove(i);
				i--;
			}
		}
	}
	
	public void setCreatorInitials(String s) {
		creatorInitials = new String(s);
	}
	
	public void setMutationIndex(int i) {
		mutationIndex = i;
	}
	
	public void setDistanceWalked(double d) {
		distanceWalked = d;
	}
	
	public String getCreatorInitials() {
		return creatorInitials;
	}
	
	public int getMutationIndex() {
		return mutationIndex;
	}
	
	public double getDistanceWalked() {
		return distanceWalked;
	}
}
