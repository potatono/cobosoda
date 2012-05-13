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

import java.lang.IllegalStateException;;

public class AsexualReproduction implements IReproduce {
	public static final String VERSION = "$Revision: 1.3 $";
	public static final String ID = "$Id: AsexualReproduction.java,v 1.3 2008/05/25 23:06:44 potatono Exp $";

	Model parent;
	
	public void addParent(Model parent) throws IllegalStateException {
		if (this.parent == null) {
			this.parent = parent;
		}
		else {
			throw new IllegalStateException("Only one parent allowed");
		}
	}

	public int getNumberOfParents() {
		return 1;
	}
	
	public int getRequiredNumberOfParents() {
		return 1;
	}
	
	public void reset() {
		parent = null;
	}

	public int getNumberOfMutations() {
		int number = (int)Math.floor(Math.log(Math.random()*10)) + 1;
		return number;
	}
	
	public Model reproduce() throws IllegalStateException {
		int mutationCount = this.getNumberOfMutations();
		MutationList mutations = MutationList.getInstance();
		Model child = parent.clone();
		
		for (int i=0; i<mutationCount; i++) {
			try {
				mutations.applyMutation(child);
			}
			catch (Exception e) {
				// Catch the exception and move on, so we don't kill the thread.
				e.printStackTrace(System.err);
			}
		}
		
		return child;
	}
}
