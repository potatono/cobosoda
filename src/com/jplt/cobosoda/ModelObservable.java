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

public class ModelObservable extends Observable {
	public static final String VERSION = "$Revision: 1.4 $";
	public static final String ID = "$Id: ModelObservable.java,v 1.4 2008/05/08 04:54:05 potatono Exp $";

	public void notifyTime(Model model, int ms) {
		this.setChanged();
		this.notifyObservers(new Object[] { Model.EVENT_TIME, model, ms });
	}
	
	public void notifyCollision(Model model, Joint joint, int collisionType) {
		this.setChanged();
		this.notifyObservers(new Object[] { Model.EVENT_COLLISION, model, joint, collisionType });
	}

	public void notifySpring(Model model, Limb spring, int springType) {
		this.setChanged();
		this.notifyObservers(new Object[] { Model.EVENT_SPRING, model, spring, springType});
	}
}
