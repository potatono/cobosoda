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

public class DistanceFitnessTest extends AbstractFitnessTest {
	public static final String VERSION = "$Revision: 1.3 $";
	public static final String ID = "$Id: DistanceFitnessTest.java,v 1.3 2008/05/25 23:06:44 potatono Exp $";

	double results = 0;
	
	public DistanceFitnessTest() {
		super();
	}
	
	public DistanceFitnessTest(Planet planet) {
		super(planet);
	}
	
	@Override
	public double getResults() {
		return results;
	}

	@Override
	public void measure() {
		if (!this.isTimeUp()) {
			Planet planet = getPlanet();
			if (planet != null) {
				results = planet.getModel().getDistanceTravelled();
			}
		}
	}
	
	public void reset() {
		results = 0;
	}
	
	public IFitnessTest newInstance() {
		return new DistanceFitnessTest();
	}
}
