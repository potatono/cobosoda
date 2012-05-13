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

public class ModelMechanics {
	public static final String VERSION = "$Revision: 1.1 $";
	public static final String ID = "$Id: ModelMechanics.java,v 1.1 2008/05/08 04:54:05 potatono Exp $";

	private int muscleFlow = 1;
    private double springT = 0.0;
    private double springK = Config.SPRING_K;
    
    public int getMuscleFlow() {
    	return muscleFlow;
    }
    
    public void setMuscleFlow(int muscleFlow) {
    	this.muscleFlow = muscleFlow;
    }
    
    public double getSpringT() {
		return springT;
	}

	public void setSpringT(double springT) {
		this.springT = springT;
	}

	public double getSpringK() {
		return springK;
	}

	public void setSpringK(double springK) {
		this.springK = springK;
	}

	public void adjustT(double dt) {
		springT += dt * muscleFlow;
	}
	
    public void elapseTime(int ms) {
    	adjustT(ms/1000.0*Config.TIME_PER_FRAME);
    }
}
