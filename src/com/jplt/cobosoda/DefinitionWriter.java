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

import java.io.*;

public class DefinitionWriter {
	public static final String VERSION = "$Revision: 1.6 $";
	public static final String ID = "$Id: DefinitionWriter.java,v 1.6 2008/07/21 22:09:44 thumbuki Exp $";

	private Writer writer;
	
	public DefinitionWriter(Writer writer) {
		this.writer = writer;
	}
	
	public void write(Model m) throws IOException {
		writeHeader();
		writeArcade(m);
		writeModel(m);
		writer.close();
	}
	
	public void write(Planet p) throws IOException {
		writeHeader();
		writePlanet(p);
		writeModel(p.getModel());
		writer.close();
	}
	
	private void writeHeader() throws IOException {
		writer.write("// cobosoda model definition\n\n");
	}

	private void writeArcade(Model m) throws IOException {
		writer.write("creatorinitials\t" + m.getCreatorInitials() + "\n");
		writer.write("mutationindex\t" + m.getMutationIndex() + "\n");
		writer.write("distancewalked\t" + m.getDistanceWalked() + "\n");
	}
	
	/* TODO: Fix. */
	private void writeModel(Model m) throws IOException {
		writer.write("name\t"+m.getName()+"\n");
		
		for(Joint j:m.getJoints()) {
			writer.write("joint\t"+j.getX()+"\t"+j.getY()+"\t"+"0\n");
		}
		
		for(int i=0; i<m.getNumberOfJoints(); i++) {
			Joint j = m.getJoint(i);
			for (int k=0; k<j.getLimbCount(); k++) {
				Limb s = j.getLimb(k);
				
				// If this joint is the first end of this spring
				// We don't want to write the spring twice.
				if (s.getIndexOfJoint(j) == 0) {
					if (s instanceof Muscle) {
						writer.write("muscle\t" + s.getJoint(0).getIndex() + "\t" + 
							s.getJoint(1).getIndex() + "\t" +
							((Muscle)s).getFrequency() + "\t" + 
							((Muscle)s).getAmplitude() + "\t" + 
							((Muscle)s).getOffset() + "\t" + 
							((Muscle)s).getK() + "\n");
					}
					else if (s instanceof Spring) {
						writer.write("spring\t" + s.getJoint(0).getIndex() + "\t" + 
							s.getJoint(1).getIndex() + "\t" +
							((Spring)s).getK() + "\n");
					}
					else {
						writer.write("limb\t" + s.getJoint(0).getIndex() + "\t" + 
								s.getJoint(1).getIndex() + "\n");
					}
				}
			}
		}
	}
	
	private void writePlanet(Planet p) throws IOException {
		writer.write("gravity\t"+p.getGravity()+"\n");
		writer.write("mass\t"+p.getMass()+"\n");
		writer.write("springk\t"+p.getSpringElasticity()+"\n");
	}
}
