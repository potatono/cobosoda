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

public class ConsoleModelObserver extends ModelObserver {
	public static final String VERSION = "$Revision: 1.2 $";
	public static final String ID = "$Id: ConsoleModelObserver.java,v 1.2 2008/04/15 03:03:48 potatono Exp $";

	private int totalTime = 0;
	private int printTime = 0;
	
	public ConsoleModelObserver(Model m) {
		super(m);
		this.observeCollisions(m);
		this.observeTime(m);
	}

	@Override
	public void updateCollision(Model model, Joint joint, int collisionType) {
		if (collisionType == Model.COLLISION_BOTTOM) {
			System.out.println("Joint " + joint.getIndex() + " has collided with the floor");
		}
		else {
			System.out.println("Joint " + joint.getIndex() + " has collided with the wall");
		}
	}

	@Override
	public void updateTime(Model model, int timeElapsed) {
		totalTime += timeElapsed;
		printTime += timeElapsed;
		
		if (printTime >= 10000) {
			printTime = 0;
			System.out.println("The simulation has been running for " + (totalTime/1000) + " seconds.");
		}
	}
}
