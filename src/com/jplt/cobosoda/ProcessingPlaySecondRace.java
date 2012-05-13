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
public class ProcessingPlaySecondRace extends ProcessingMode {
	public static final int GROUND_LEVEL_0 = 520;
	public static final int GROUND_LEVEL_1 = 320 - 36 - 16;
	private Model model0;
	private Model modelOriginal0;
	private ProcessingGround ground0;
	private ProcessingScore score0;
	private ProcessingTimer timeLeft;
	private ProcessingWalkingModel walker0;
	private Model model1;
	private Model modelOriginal1;
	private ProcessingGround ground1;
	private ProcessingScore score1;
	private ProcessingWalkingModel walker1;
	private SynthModelObserver synthObserver0;
	private SynthModelObserver synthObserver1;
			
	public ProcessingPlaySecondRace(PApplet papplet, ProcessingInterface proc,
			Model mOriginal, Model mEvolved) {
		super(papplet, proc);
		
		timeLeft = new ProcessingTimer(pa, pi, PMethod.RACE_TIME);
		timeLeft.setTextLocation(343, 48);
		
		/* Initial Design */
		modelOriginal0 = mOriginal.clone();
//		modelOriginal0 = World.createTestModel();
		model0 = modelOriginal0.clone();
		walker0 = new ProcessingWalkingModel(pa, pi, model0, GROUND_LEVEL_0);
		ground0 = new ProcessingGround(pa, pi, GROUND_LEVEL_0);
		score0 = new ProcessingScore(pa, pi);
		
		score0.setTextLocation(16, GROUND_LEVEL_0 + 36);
		
		/* Evolved Design */
		modelOriginal1 = mEvolved.clone();
//		modelOriginal1 = World.createTestModel();
		model1 = modelOriginal1.clone();
		walker1 = new ProcessingWalkingModel(pa, pi, model1, GROUND_LEVEL_1);
		ground1 = new ProcessingGround(pa, pi, GROUND_LEVEL_1);
		score1 = new ProcessingScore(pa, pi);
		
		score1.setTextLocation(16, GROUND_LEVEL_1 + 36);
		
		synthObserver0 = new SynthModelObserver(model0, pi);
		synthObserver1 = new SynthModelObserver(model1, pi);
	}
	
	public void display() {
		walker0.update();
		walker1.update();
		timeLeft.display();

		walker0.setExternalOffset(walker1.getActualOffset() -
				walker0.getActualOffset());
		ground0.setOffset(walker0.getAverageOffset());
		score0.setScore(walker0.getActualOffset());

		walker0.display();
		ground0.display();
		score0.display();

		ground1.setOffset(walker1.getAverageOffset());
		score1.setScore(walker1.getActualOffset());

		walker1.display();
		ground1.display();
		score1.display();
		
		if (timeLeft.hasTimerExpired()) {
//			pi.setMode(new ProcessingStandbyTitle(pa, pi));
			pi.setMode(new ProcessingFinalScore(pa, pi, modelOriginal0,
					modelOriginal1));
		}
	}
}
