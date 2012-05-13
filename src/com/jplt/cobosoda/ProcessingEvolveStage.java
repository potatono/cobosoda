/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jplt.cobosoda;

import processing.core.*;

/**
 *
 * @author Jacob Joaquin
 */
public class ProcessingEvolveStage extends ProcessingMode {

	public final static int NUMBER_OF_MUTATIONS = 100;
	public final static int MAX_MUTATIONS_LEFT = 200;
	private final static double TEST_TIME = 60;
	private Model originalModel;
	private Model cloneModel;
	private ProcessingTestModel tester;
	private ProcessingRaceStats stat;
	private int mutationsLeft = NUMBER_OF_MUTATIONS;
	private Model winningModel;
	private ProcessingMutate mutator;
	private int successfulMutations = 0;
	private String name;
	private SynthEngine synth;

	public ProcessingEvolveStage(PApplet papplet, ProcessingInterface proc,
			Model m) {
		super(papplet, proc);

		originalModel = m.clone();
		cloneModel = originalModel.clone();
		winningModel = originalModel.clone();

		stat = new ProcessingRaceStats(originalModel);
		tester = new ProcessingTestModel(TEST_TIME);
		stat = tester.race(originalModel.clone());

		name = originalModel.getName();

		synth = pi.getSynth();
	}

	@Override
	public void display() {
		pa.stroke(255);

		mutator = new ProcessingMutate(winningModel.clone());
		cloneModel = mutator.evolve();
		cloneModel = PMethod.centerModel(cloneModel);
		cloneModel = PMethod.groundModel(cloneModel);

		Model cloneRace = cloneModel.clone();

		if (mutationsLeft > 0) {
			ProcessingRaceStats thisStat = tester.race(cloneRace);
			synth.mutation(mutationsLeft, successfulMutations);

//			System.out.println("EvolveStage " + " " + successfulMutations +
//					" " + mutationsLeft + " " + stat.getMaxDistance() + " " +
//					thisStat.getMaxDistance());

			if (thisStat.getMaxDistance() > stat.getMaxDistance()) {
				stat = thisStat;
				winningModel = cloneModel;
				successfulMutations++;
				mutationsLeft += NUMBER_OF_MUTATIONS;
				synth.evolve(successfulMutations);

				if (mutationsLeft > MAX_MUTATIONS_LEFT) {
					mutationsLeft = MAX_MUTATIONS_LEFT;
				}
			}
		} else {
			winningModel.setMutationIndex(successfulMutations);
			pi.setMode(new ProcessingPlaySecondRace(pa, pi, originalModel.clone(), winningModel.clone()));
		}

		mutationsLeft--;

		PMethod.drawModel(winningModel, 0,
				ProcessingPlaySecondRace.GROUND_LEVEL_1, pa);
		PMethod.drawModel(cloneModel, 0,
				ProcessingPlaySecondRace.GROUND_LEVEL_0, pa);

		drawIteration();
		drawMutationsLeft();
		drawHeader();
	}

	private void drawIteration() {
//		pa.fill(255);
//		pa.textFont(pi.font24, 24);

		StringBuffer sb = new StringBuffer(20);
		sb.append(PMethod.substituteDelimiter(name));
		sb.append(".");
		sb.append(successfulMutations);
		PMethod.writeCenteredText(pa, sb.toString(), 400,
				ProcessingPlaySecondRace.GROUND_LEVEL_1 + 26, 26, pi.font24, 24);
	}

	private void drawMutationsLeft() {
		PMethod.writeCenteredText(pa, "MUTATIONS LEFT:" + mutationsLeft, 400,
				ProcessingPlaySecondRace.GROUND_LEVEL_1 + 52, 26, pi.font24, 24);
	}

	private void drawHeader() {
		pa.fill(255);
		pa.textFont(pi.font48, 48);
//		pa.line(200, 0, 200, 100);  /* For Centering */
//		pa.line(600, 0, 600, 100);
		pa.text("E V O L V E", 197, 56);
	}
}
	
		
		
		
		
		
		