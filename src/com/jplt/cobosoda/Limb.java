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

/**
 * Connects two joints within a model
 * 
 * @author justin
 */
public class Limb implements IPart<Limb> {
	public static final String VERSION = "$Revision: 1.4 $";
	public static final String ID = "$Id: Limb.java,v 1.4 2008/07/12 01:22:15 thumbuki Exp $";

	protected double length;
	protected Model parent = null;
	protected ArrayList<Joint> jointList = new ArrayList<Joint>();

	/**
	 * Creates a new limb
	 * 
	 * @param parent Model two which the limb will belong
	 */
	public Limb(Model parent) {
		super();
		this.parent = parent;
	}

	/**
	 * Set the length of the Limb from the distance between the two joints attached
	 */
	protected void setLength() {
		double dy = this.getJoint(0).getY() - this.getJoint(1).getY();
		double dx = this.getJoint(0).getX() - this.getJoint(1).getX();
	
		length = computeLength(dy, dx);
	}

	protected static double computeLength(double dy, double dx) {
		return Math.sqrt(dy * dy + dx * dx);
	}
	
	/**
	 * Return the index of this Limb within the parent Model
	 * 
	 * @return The index as an integer
	 */
	public int getIndex() { 
		return parent.getIndexOfLimb(this); 
	}

	/**
	 * Returns the length of the limb
	 * 
	 * @return The length in meters
	 */
	public double getLength() { 
		return length; 
	}

	/**
	 * Add a joint to List jointList.  Attempting to add 2 or more will result in a stern warning.
	 * 
	 * @param j  The joint to associate with this spring
	 */
	public void attachJoint(Joint j) {
		if (jointList.size() < 2) {
			jointList.add(j);
		}
		else {
			System.out.println("Spring.addConnection(Joint j):  WARNING!!  Attempted to add more than 2 Spring endpoints.");
		}
	
		if (jointList.size() == 2) {
			setLength();
		}
	}

	/**
	 * Add two joints to List jointList.  Attempting to add 2 or more will result in warning.
	 * 
	 * @param j  The first joint to associate with this spring
	 * @param j2  The second joint to associate with this spring
	 */
	public void attachJoint(Joint j, Joint j2) {
		attachJoint(j);
		attachJoint(j2);
	}

	/**
	 * Get the joint from List connection at specified index
	 * 
	 * @param i  Index of jointList to get Joint
	 * @return joint
	 */
	public Joint getJoint(int i) {
		return jointList.get(i);
	}

	/**
	 * Gets the index of a specified joint in the list jointList
	 * @param j
	 * @return
	 */
	public int getIndexOfJoint(Joint j) {
		return jointList.indexOf(j);
	}
	
	/**
	 * Detach joint from this spring
	 * @param j Joint to detach
	 */
	public void detachJoint(Joint j) {
		jointList.remove(j);
	}
	
	/**
	 * Detach everything from this spring
	 */
	public void detachAll() {
		for (Joint j:jointList) {
			j.detachLimb(this);
			detachJoint(j);
		}
	}
	
	/**
	 * Returns the resulting force for this limb.  
	 * @return double
	 */
	public MVector resultantForce(Joint joint) {
		return MVector.ZERO;  // Default limb applies no force.
	}
	
	public Limb clone(Model parent) {
		Limb clone = new Limb(parent);
		clone.length = this.length;
		return clone;
	}
	
	public Limb clone() {
		return this.clone(parent);
	}
}