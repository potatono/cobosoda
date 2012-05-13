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

public class Spring extends Limb {
	public static final String VERSION = "$Revision: 1.7 $";
	public static final String ID = "$Id: Spring.java,v 1.7 2008/07/12 01:22:15 thumbuki Exp $";

	protected double a;
	private double k;
	private double load;
	
	/**
	 * A Limb connector that applies spring mechanics between two joints
	 * 
	 * @param parent
	 * @param k
	 */
	public Spring(Model parent, double k) {
		super(parent);
		this.k = k;
	}

	/**
	 * Returns the load of the spring, that is length it's been stretched or compressed beyond its natural
	 * resting point
	 * @return The load as double
	 */
	public double getLoad() { 
		return load; 
	}
	
	/**
	 * Sets the spring constant.
	 * 
	 * @param k  The value of the spring constant to set
	 */
	public void setK(double value) {
		k = value;
	}

	protected ModelMechanics getMechanics() {
		return parent.getMechanics();
	}

	protected double getT() { 
		return getMechanics().getSpringT(); 
	}

	protected double getK() {
		return k;
	}

	/**
	 * Return the resultant force of this spring to the calling joint.
	 * 
	 * @param j  The joint requesting the resultant force
	 * @return   Resultant force.
	 */
	public MVector resultantForce(Joint j) {
		double dy;
		double dx;
		double thisLength;
		double t = getT();

		// If there are no joints attached, there is no resulting force.
		if (jointList.size() != 2) { 
			return MVector.ZERO;
		}

		/* Compute Deltas */
		dy = computeDeltaY(j);
		dx = computeDeltaX(j);
		
		// Compute the new load on the spring
		thisLength = computeLength(dy, dx);
		load = computeLoad(thisLength);
		MVector result = computeForce(dy, dx, load);
		
		return result;
	}

	protected double computeDeltaY(Joint j) {
		int endPoint = getIndexOfJoint(j);

		return getJoint(1 - endPoint).getLastY() - getJoint(endPoint).getLastY();
//		return getJoint(1 - endPoint).getY() - getJoint(endPoint).getY();
	}
	
	protected double computeDeltaX(Joint j) {
		int endPoint = getIndexOfJoint(j);
		
		return getJoint(1 - endPoint).getLastX() - getJoint(endPoint).getLastX();
//		return getJoint(1 - endPoint).getX() - getJoint(endPoint).getX();
	}
	

	protected double computeLoad(double l) {
		a = length - l;
		return a;
	}
		
	protected MVector computeForce(double dy, double dx, double thisLoad) {
		double angle = Math.atan2(dy, dx);
		double magnitude = -getK() * thisLoad / (double) jointList.size();
		
		if (magnitude > Config.MAX_SPRING) {
			magnitude = Config.MAX_SPRING;
		} else if (magnitude < Config.MIN_SPRING) {
			magnitude = Config.MIN_SPRING;
		}

		return new MVector(angle, magnitude);
	}
	
	@Override
	public Spring clone() {
		Spring clone = new Spring(parent, this.k);		
		clone.k = this.k;
		return clone;
	}
	
	public Spring clone(Model parent) {
		Spring clone = new Spring(parent, this.k);		
		clone.length = this.length;
		return clone;
	}
}
