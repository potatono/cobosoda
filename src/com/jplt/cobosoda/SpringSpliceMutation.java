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

public class SpringSpliceMutation extends NullMutation {
	public static final String VERSION = "$Revision: 1.6 $";
	public static final String ID = "$Id: SpringSpliceMutation.java,v 1.6 2008/05/25 23:06:44 potatono Exp $";

	public Model mutate(Model m) {
		// Get a random joint
		Joint joint = m.getJoint((int)Math.floor(Math.random() * m.getNumberOfJoints()));
		
		// Make sure we haven't orphaned the joint in a previous mutation
		if (joint.getLimbCount() > 0) {
			// Get a random limb
			Limb limb = joint.getLimb((int)Math.floor(Math.random() * joint.getLimbCount()));

			// Get the two joints connected to this limb 
			Joint j1 = limb.getJoint(0);
			Joint j2 = limb.getJoint(1);
			
			// Determine the center of two joints
			double minx = Math.min(j1.getX(),j2.getX());
			double maxx = Math.max(j1.getX(), j2.getX());
			double x = minx + (maxx-minx) / 2;
			double miny = Math.min(j1.getY(),j2.getY());
			double maxy = Math.max(j1.getY(), j2.getY());
			double y = miny + (maxy-miny) / 2;

			// Spawn the new joint that will be jointed to both springs
			Joint j3 = m.spawnJoint(x,y,false);
			
			// Detach j2 from j1, and then attach j1 to j3
			limb.detachJoint(j2);
			limb.attachJoint(j3);
			
			// Attach j2 to j3 with a new spring.
			// TODO FIXME If Limb isn't a Spring, this will fail.
			m.spawnSpring(j2, j3, ((Spring)limb).getK());
		}

		return m;
	}
}
