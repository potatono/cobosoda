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

public abstract class AbstractFitnessTest implements IFitnessTest {
	public static final String VERSION = "$Revision: 1.3 $";
	public static final String ID = "$Id: AbstractFitnessTest.java,v 1.3 2008/05/25 23:06:44 potatono Exp $";

	private int timeLimit = Config.TEST_TIME_LIMIT;
	private Planet planet;
	
	public AbstractFitnessTest() {
	}
	
	public AbstractFitnessTest(Planet planet) {
		this.planet = planet;
	}
	
	public double getResults() {
		return 0;
	}

	public int getTimeLimit() {
		return timeLimit;
	}

	public Planet getPlanet() {
		return planet;
	}
	
	public void setPlanet(Planet planet) {
		this.planet = planet;
	}
	
	public boolean isTimeUp() {
		return planet != null && planet.getTimeElapsed() >= timeLimit;
	}

	public void measure() {
	}

	public void setTimeLimit(int ms) {
		timeLimit = Config.TEST_TIME_LIMIT;
	}
	
	public IFitnessTest newInstance() {
		return null;
	}
}
