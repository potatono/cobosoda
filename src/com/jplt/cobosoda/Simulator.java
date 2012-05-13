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

public class Simulator {
	public static final String VERSION = "$Revision: 1.4 $";
	public static final String ID = "$Id: Simulator.java,v 1.4 2009/05/31 17:46:49 potatono Exp $";

    static SimulatorInterface simulatorInterface;
    
    public static void main(String argv[]) {
    	if (Config.ARCADE_MODE) {
    		simulatorInterface = new ProcessingInterface();
    	}
    	else {
    		simulatorInterface = new WindowInterface();
    	}
    }

}
