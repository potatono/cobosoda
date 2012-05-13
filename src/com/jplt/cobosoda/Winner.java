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

public class Winner {
	public static final String VERSION = "$Revision: 1.2 $";
	public static final String ID = "$Id: Winner.java,v 1.2 2008/04/15 03:03:47 potatono Exp $";

	private Model model;
	private Double score;
	
	public Winner(Model model, double score) {
		this.model = model;
		this.score = score;
	}
	
	public Model getModel() {
		return model;
	}
	
	public void setModel(Model model) {
		this.model = model;
	}
	
	public Double getScore() {
		return score;
	}
	
	public void setScore(double score) {
		this.score = new Double(score);
	}
	
	public void setScore(Double score) {
		this.score = score;
	}
}
