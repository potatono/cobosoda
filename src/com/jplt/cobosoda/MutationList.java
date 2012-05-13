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

import java.util.*;

public class MutationList extends ArrayList<IMutation> {
	public static final String VERSION = "$Revision: 1.3 $";
	public static final String ID = "$Id: MutationList.java,v 1.3 2008/05/25 23:06:44 potatono Exp $";

	private static final long serialVersionUID = 1L;
	private static MutationList instance;
	
	private MutationList() {
		String[] classNames = Config.MUTATION_CLASSES.split("\\s*,\\s*");
		for (String className:classNames) {
			try {
				Class c = Class.forName(className);
				IMutation m = (IMutation)c.newInstance();
				this.add(m);
			}
			catch (Exception e) {
				e.printStackTrace(System.err);
			}
		}
	}
	
	public static MutationList getInstance() {
		if (instance == null) 
			instance = new MutationList();
		
		return instance;
	}
	
	public Model applyMutation(Model m) {
		if (this.size() > 0) {
			IMutation mutation = this.get((int)Math.floor(Math.random() * this.size()));
			mutation.mutate(m);
		}
		
		return m;
	}	
}
