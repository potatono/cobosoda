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
import java.util.*;

public class DefinitionReader {
	public static final String VERSION = "$Revision: 1.7 $";
	public static final String ID = "$Id: DefinitionReader.java,v 1.7 2008/07/25 21:02:10 thumbuki Exp $";

	StreamTokenizer tokenizer;
	HashMap <String,Integer> validCommands = null;
	
	public DefinitionReader(Reader arg0) {
		tokenizer = new StreamTokenizer(arg0);
		tokenizer.eolIsSignificant(true);
		tokenizer.slashSlashComments(true);
		tokenizer.slashStarComments(false);
	}

	public Model read() throws DefinitionFileException, IOException {
		Model m = new Model();
		String cmd = "";
		ArrayList<Object> values = new ArrayList<Object>();
		boolean ignoreLine = false;
		
		while (tokenizer.nextToken() != StreamTokenizer.TT_EOF) {
			try {
				switch (tokenizer.ttype) {
					case StreamTokenizer.TT_WORD:
						if (cmd.equals("")) {
							cmd = tokenizer.sval;
						}
						else {
							values.add(tokenizer.sval);
						}
						break;
					case StreamTokenizer.TT_NUMBER:
						values.add(new Double(tokenizer.nval));
						break;
					case StreamTokenizer.TT_EOL:
						if (ignoreLine) {
							ignoreLine = false;
						}
						else if (cmd.length() > 0){
							process(cmd,values,m);
						}
						values.clear();
						cmd = "";
						break;
					case 35: // #
						ignoreLine = true;
						break;
				}
			}
			catch (DefinitionFileException dfe) {
				System.out.println(dfe.getMessage() + " at line " + tokenizer.lineno());
				values.clear();
				cmd="";
			}
		}
		return m;
	}
	
	private void process(String cmd, ArrayList<Object> values, Model m) throws DefinitionFileException {
		double defaultK = Config.ELASTICITY;
		
		if (!isValidCommand(cmd)) {
			throw new DefinitionFileException("Invalid token " + cmd);
		}
		else if (!isValidArgumentCount(cmd,values.size())) {
			throw new DefinitionFileException("Invalid argument count " + values.size() + " for " + cmd);
		}
		else {
			try {
				if (cmd.equalsIgnoreCase("gravity"))
					World.setGravity((Double)values.get(0));
				else if (cmd.equalsIgnoreCase("springk")) {
					defaultK = (Double)values.get(0);
					World.setSpringElasticity(defaultK);
				}
				else if (cmd.equalsIgnoreCase("friction"))
					World.setFriction((Double)values.get(0));
				else if (cmd.equalsIgnoreCase("mass"))
					World.setMass((Double)values.get(0));
				else if (cmd.equalsIgnoreCase("joint"))
					m.spawnJoint((Double)values.get(0), 
							(Double)values.get(1), 
							(((Double)values.get(2)).intValue() != 0)
					);
				else if (cmd.equalsIgnoreCase("spring"))
					m.spawnSpring(((Double)values.get(0)).intValue(), 
							((Double)values.get(1)).intValue(), 
							(values.size() > 2 ? (Double)values.get(2) : defaultK)
					);
				else if (cmd.equalsIgnoreCase("muscle"))
					m.spawnMuscle(((Double)values.get(0)).intValue(), 
						((Double)values.get(1)).intValue(),
						(values.size() > 5 ? (Double)values.get(5) : defaultK),
						(Double)values.get(2), 
						(Double)values.get(3), 
						(Double)values.get(4)
					);
				
				else if (cmd.equalsIgnoreCase("name")) {
					// Some fancy language.  no join?

//					String name = "";
//					for (Object o:values) {
//						if (name.length() > 0) {
//							name += " ";
//						}
//						name += o.toString();
//					}
					m.setName((String)values.get(0));
					System.out.println((String)values.get(0));
				} else if(cmd.equalsIgnoreCase("creatorinitials")) { /* CoboArcade additions. */
					m.setCreatorInitials((String)values.get(0));
				} else if(cmd.equalsIgnoreCase("mutationindex")) { /* CoboArcade additions. */
					m.setMutationIndex(((Double) values.get(0)).intValue());
				} else if(cmd.equalsIgnoreCase("distancewalked")) { /* CoboArcade additions. */
					m.setDistanceWalked((Double) values.get(0));
				}
			}
			catch (Exception e) {
				throw new DefinitionFileException(e.getMessage());
			}
		}
	}

	private boolean isValidCommand(String cmd) {
		return getValidCommands().containsKey(cmd);
	}
	
	private boolean isValidArgumentCount(String cmd, int count) {
		int validCount = getValidCommands().get(cmd).intValue();
		
		return validCount < 0 || validCount <= count;
	}
	
	private HashMap<String,Integer> getValidCommands() {
		if (validCommands == null) {
			HashMap<String,Integer> valid = new HashMap<String,Integer>();
		
			valid.put("gravity", 1);
			valid.put("springk", 1);
			valid.put("mass",1);
			valid.put("jointcount",1);
			valid.put("joint",3);
			valid.put("spring",2);
			valid.put("muscle",5);
			valid.put("name", -1);
			
			/* new for CoboArcade */
			valid.put("creatorinitials", 1);
			valid.put("mutationindex", 1);
			valid.put("distancewalked", 1);
			
			validCommands = valid;
		}
		
		return validCommands;
	}
}
