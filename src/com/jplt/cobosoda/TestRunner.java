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

import java.util.Observable;

public class TestRunner {
	public static final String VERSION = "$Revision: 1.3 $";
	public static final String ID = "$Id: TestRunner.java,v 1.3 2008/05/25 23:06:44 potatono Exp $";

	private Planet planet;
	private IFitnessTest test;
	private Model model;
	
	public TestRunner(Model model, IFitnessTest test) {
		this.test = test;
		this.model = model;
		planet = new Planet();
		// Running the simulation will corrupt the model's original state.  Use another clone.
		planet.setModel(model.clone());
		planet.setFitnessTest(test);
		// Ugh all these shitty circular references are pissing me off.
		test.setPlanet(planet);
	}
	
	public Double getResults() {
		return new Double(test.getResults());
	}
	
	public Model getModel() {
		return this.model;
	}
	
	public void setModel(Model model) {
		this.model = model;
	}
	
	public void run() {
		while(!test.isTimeUp()) {
			planet.elapseTime(1000/Config.FRAMES_PER_SECOND);
		}
	}
}
