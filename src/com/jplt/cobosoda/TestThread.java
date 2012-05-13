package com.jplt.cobosoda;

import java.util.Random;

public class TestThread implements Runnable {
	public static final String VERSION = "$Revision: 1.1 $";
	public static final String ID = "$Id: TestThread.java,v 1.1 2008/05/25 23:06:44 potatono Exp $";

	private static long iterations = 0;
	private static long resetIterations = 10000;

	private Nature nature;
	private IFitnessTest fitnessTest;
	private boolean isRunning = false;
	private Model baseModel;
	private Random random;
	
	public TestThread(Model baseModel, IFitnessTest fitnessTest, Nature nature) {
		this.nature = nature;
		this.fitnessTest = fitnessTest.newInstance();
		this.baseModel = baseModel;
		random = new Random();
	}

	private long getNextIteration() {
		synchronized(this) {
			iterations++;
			return iterations;
		}
	}
	
	public static void reset() {
		iterations = 0;
	}
	
	public void run() {
		isRunning = true;
		long iteration;
		
		while(true) {
			iteration = getNextIteration();
			
			// Run a test
			Model testModel = getModel();
			TestRunner testRunner = new TestRunner(testModel,fitnessTest);
			testRunner.run();
			Model winningModel = testRunner.getModel();
			winningModel.setName(baseModel.getName() + " Mutation " + iteration);
			nature.compete(winningModel, testRunner.getResults());
	
			// TODO FIXME  Need a way to reset other threads.
//			if (iterations > resetIterations) {
//				reset(winningModel);
//			}
			
			if (!isRunning) 
				break;
		}
	}
	
	public void stop() {
		isRunning = false;
	}
	
	private Model getModel() throws NullPointerException {
		Model testModel = null;

		for (int i=0; i<100; i++) {
			testModel = nature.getChild();
			if (testModel != null)
				break;
		}
		
		if (testModel == null) {
			throw new NullPointerException("Could not get a child model after 100 tries");
		}
		
		return testModel;
	}
}
