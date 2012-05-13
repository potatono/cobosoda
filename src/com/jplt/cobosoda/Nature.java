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

import java.util.Random;
import java.lang.IllegalStateException;

public class Nature {
	public static final String VERSION = "$Revision: 1.4 $";
	public static final String ID = "$Id: Nature.java,v 1.4 2009/05/31 17:46:49 potatono Exp $";

	private Winners winners;
	private IReproduce reproduction;
	private int generation = 0;
	
	public Nature(Model model) {
		winners = new Winners();
		winners.add(model,0);
		
		try {
			Class reproductionClass = Class.forName(Config.REPRODUCTION_CLASS);		
			reproduction = (IReproduce)reproductionClass.newInstance();
		}
		catch (Exception e) {
			e.printStackTrace(System.err);
			System.err.println("Falling back to AsexualReproduction");
			reproduction = new AsexualReproduction();
		}
	}
	
	public Nature(Winners winners) {
		this.winners = winners;
	}

	public Model getChild() throws IllegalStateException {
		Model child = null;
		
		synchronized(this) {
			reproduction.reset();
		
			for (int i=0; i<reproduction.getRequiredNumberOfParents(); i++) {
				reproduction.addParent(winners.getRandom().getModel());
			}
		
			try {
				child = reproduction.reproduce();
			}
			catch (Exception e) {
				e.printStackTrace(System.err);
			}
			
			generation++;
		}
		
		return child;
	}
	
	public void reset() {
		generation = 0;
	}
	
	public int getGeneration() {
		return generation;
	}
	
	public void compete(Model model, double score) {
		if (winners.isWinner(score)) {
			winners.add(model,score);
			//System.out.println(model.getName() + " is a winner with " + String.format("%1$.3f", score));
		}
	}
	
	public Model getWinner() {
		Winner winner = winners.getWinner();
		//Winner winner = winners.getRandom();
		//System.out.println("Winner is " + winner.getModel().getName() + " with score " + String.format("%1$.3f",winner.getScore()));
		return winner.getModel();
	}
}
