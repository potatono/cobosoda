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

public class AdditionalSpringMutation extends NullMutation {
	public static final String VERSION = "$Revision: 1.4 $";
	public static final String ID = "$Id: AdditionalSpringMutation.java,v 1.4 2008/05/24 21:02:50 potatono Exp $";

	public Model mutate(Model m) {
		int jn1 = (int)Math.floor(Math.random() * m.getNumberOfJoints());
		int jn2 = -1;
		int tries = 0;
		
		while (tries < 10) {
			jn2 = (int)Math.floor(Math.random() * m.getNumberOfJoints());
			if (jn2 != jn1)
				break;
		}
		
		if (jn2 != jn1) {
			try { m.spawnSpring(jn1, jn2, 2.5 + Math.random()); } catch (Exception e) {}
		}
		
		return m;
	}
}
