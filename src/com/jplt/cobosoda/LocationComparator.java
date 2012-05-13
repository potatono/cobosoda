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

import java.util.Comparator;

public class LocationComparator implements Comparator<Joint> {
	public static final String VERSION = "$Revision: 1.3 $";
	public static final String ID = "$Id: LocationComparator.java,v 1.3 2008/05/08 04:54:05 potatono Exp $";

	public int compare(Joint o1, Joint o2) {
		if (o1.getParentModel().getMechanics().getMuscleFlow() > 0) {
			return Double.compare(o1.getX(),o2.getX());
		}
		else {
			return Double.compare(o2.getX(),o1.getX());
		}
	}

}
