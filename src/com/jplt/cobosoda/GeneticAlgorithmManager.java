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

public class GeneticAlgorithmManager {	
	public static final String VERSION = "$Revision: 1.5 $";
	public static final String ID = "$Id: GeneticAlgorithmManager.java,v 1.5 2009/05/31 17:46:49 potatono Exp $";

	private ArrayList<TestThread> testThreads;
	private Nature nature;
	private IFitnessTest fitnessTest;
	private boolean isRunning = false;
	private String winnersPath = null;
	private Model model;
	
	public GeneticAlgorithmManager(Model model, IFitnessTest fitnessTest) {
		this.nature = new Nature(model);
		this.model = model;
		this.fitnessTest = fitnessTest;
	}
	
	public void startTesting() {
		// First, make sure we stop testing if we're already running..
		if (isRunning) {
			stopTesting();
		}
		
		TestThread.reset();
		isRunning = true;
		testThreads = new ArrayList<TestThread>();
		
		for (int i=0; i<Config.CONCURRENT_THREADS; i++) {
			// Start a testing thread..
			TestThread testThread = new TestThread(model, fitnessTest, nature);
			Thread thread = new Thread(testThread);
			thread.start();
			testThreads.add(testThread);
			
			// Keep the threads separated a bit.
//			try {
//				Thread.sleep((int)Math.floor(Math.random()*100));
//			}
//			catch (InterruptedException e) {}
		}
	}
	
	public void stopTesting() {
		for (TestThread t:testThreads) {
			t.stop();
		}
		nature.reset();
		isRunning = false;
	}
	
	public boolean isRunning() {
		return isRunning;
	}
		
	public Model getWinner() {
		Model model = nature.getWinner();
		return model;
	}

	public int getGeneration() {
		return nature.getGeneration();
	}
	
	public String getWinnersPath() {
		return winnersPath;
	}

	public void setWinnersPath(String winnersPath) {
		this.winnersPath = winnersPath;
	}
	
	private String getWinnerPath() {
		return winnersPath + "/" + UUID.randomUUID().toString() + ".def";
	}	
}
