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

public class JointTranslationMutation extends NullMutation {
	public static final String VERSION = "$Revision: 1.3 $";
	public static final String ID = "$Id: JointTranslationMutation.java,v 1.3 2008/04/29 20:05:16 thumbuki Exp $";

	private double maxOffset = 0.5;
	
	public JointTranslationMutation() {
		super();
	}
	
	public JointTranslationMutation(double maxOffset) {
		super();
		this.maxOffset = maxOffset;
	}
	
	public double getMaxOffset() {
		return maxOffset;
	}

	public void setMaxOffset(double maxOffset) {
		this.maxOffset = maxOffset;
	}

	public Model mutate(Model m) {
		Joint j = m.getJoint((int)Math.floor(Math.random() * m.getNumberOfJoints()));
		j.setX(randomlyShift(j.getX(),maxOffset));
		j.setY(randomlyShift(j.getY(),maxOffset));
		
		return m;
	}
	
	private double randomlyShift(double n, double m) {
		return n + (Math.random() * m) * (Math.random() > 0.5 ? -1 : 1);
	}

}
