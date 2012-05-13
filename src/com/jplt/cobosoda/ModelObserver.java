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
import java.util.Observer;

public class ModelObserver implements Observer {
	public static final String VERSION = "$Revision: 1.3 $";
	public static final String ID = "$Id: ModelObserver.java,v 1.3 2008/04/29 20:09:12 thumbuki Exp $";

	public ModelObserver(Model m) {
	}
	
	public void update(Observable o, Object arg) {
		Object[] args = (Object[])arg;
		int eventType = ((Integer)args[0]).intValue();
		
		switch (eventType) {
		case Model.EVENT_SPRING:
			updateSpring((Model)args[1], (Spring)args[2], ((Integer)args[3]).intValue());
			break;
		case Model.EVENT_COLLISION:
			updateCollision((Model)args[1], (Joint)args[2], ((Integer)args[3]).intValue());
			break;
		case Model.EVENT_TIME:
			updateTime((Model)args[1], ((Integer)args[2]).intValue());
			break;
		default:
			System.out.println("EVENT not recognized");
		}
	}

	// Observe
	public void observeCollisions(Model model) {
		System.out.println("observeCollisions(Model model) called");	
		model.addCollisionObserver(this);
	}
	
	public void observeTime(Model model) {
		System.out.println("observeTime(Model model) called");	
		model.addTimeObserver(this);
	}

	public void observeSpring(Model model) {
		System.out.println("observeSpring(Model model) called");	
		model.addSpringObserver(this);
	}

	// Update
	public void updateCollision(Model model, Joint joint, int collisionType) {
		// Override this method and do stuff..
	}
	
	public void updateTime(Model model, int timeElapsed) {
		// Override this method and do stuff..
	}

	public void updateSpring(Model model, Spring spring, int springType) {
		// Override this method and do stuff..
	}
}
