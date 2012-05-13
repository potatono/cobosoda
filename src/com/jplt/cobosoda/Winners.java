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
import java.lang.IllegalStateException;

public class Winners {
	public static final String VERSION = "$Revision: 1.3 $";
	public static final String ID = "$Id: Winners.java,v 1.3 2008/05/25 23:06:44 potatono Exp $";

	private int winnerPoolSize = Config.WINNER_POOL_SIZE;
	private List<Winner> winners = Collections.synchronizedList(new ArrayList<Winner>());

	public boolean isWinner(double score) {
		// Assuming the list is already sorted, all we have to do is check the last one in the list.
		return (winners.size() < winnerPoolSize || winners.get(winners.size()-1).getScore() < score);
	}
	
	public boolean isWinner(Double score) {
		return isWinner(score.doubleValue());
	}

	public void add(Winner winner) {
		winners.add(winner);

		synchronized(winners) {
			Collections.sort(winners,new WinnerComparator());
		
			if (winners.size() > winnerPoolSize) {
				winners.remove(winnerPoolSize);
			}
		}
	}
	
	public void add(Model model, double score) {
		Winner winner = new Winner(model,score);
		this.add(winner);
	}
	
	public Winner getRandom() throws IllegalStateException {
		if (winners.size() == 0)
			throw new IllegalStateException("There are no winners to choose from");
		
		int idx = (int)Math.floor(Math.random()*winners.size());

		return winners.get(idx);
	}
	
	public Winner getRandomExclusive(Winner winner) {
		if (winners.size() <= 1)
			throw new IllegalStateException("There aren't enough winners to choose exclusively");
		
		Winner newWinner = this.getRandom();
		while (newWinner == winner) {
			newWinner = this.getRandom();
		}
		
		return newWinner;
	}
	
	public int getWinnerPoolSize() {
		return winnerPoolSize;
	}

	public void setWinnerPoolSize(int winnerPoolSize) {
		this.winnerPoolSize = winnerPoolSize;
	}
	
	public Winner get(int i) {
		return winners.get(i);
	}
	
	public Winner getWinner() {
		return winners.get(0);
	}
}
