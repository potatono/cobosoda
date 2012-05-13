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

public class NullMutation implements IMutation {
	public static final String VERSION = "$Revision: 1.2 $";
	public static final String ID = "$Id: NullMutation.java,v 1.2 2008/04/15 03:03:48 potatono Exp $";

	
	public NullMutation() {
	}
	
	public Model mutate(Model m) {
		return m;
	}
}
