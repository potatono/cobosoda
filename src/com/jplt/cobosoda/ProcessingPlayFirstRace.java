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
public class ProcessingPlayFirstRace extends ProcessingMode {
	private static final int GROUND_LEVEL = 468;
	private Model model;
	private Model modelOriginal;
	private ProcessingGround ground;
	private ProcessingScore score;
	private ProcessingTimer timeLeft;
	private ProcessingWalkingModel walker;
	private SynthModelObserver synthObserver;
			
	public ProcessingPlayFirstRace(PApplet papplet, ProcessingInterface proc,
			Model m) {
		super(papplet, proc);

		timeLeft = new ProcessingTimer(pa, pi, PMethod.RACE_TIME);
		timeLeft.setTextLocation(343, 48);
		
		modelOriginal = m.clone();
		model = modelOriginal.clone();
		walker = new ProcessingWalkingModel(pa, pi, model, GROUND_LEVEL);
		ground = new ProcessingGround(pa, pi, GROUND_LEVEL);
		score = new ProcessingScore(pa, pi);
		score.setTextLocation(16, GROUND_LEVEL + 36);
		
		synthObserver = new SynthModelObserver(model, pi);
	}
	
	public void display() {
		pa.background(0);
		pa.stroke(255);
		walker.update();
		ground.setOffset(walker.getAverageOffset());
		score.setScore(walker.getActualOffset());

		walker.display();
		ground.display();
		score.display();
		timeLeft.display();
		
		if (timeLeft.hasTimerExpired()) {
			pi.setMode(new ProcessingEvolveStage(pa, pi, modelOriginal.clone()));
		}
	}
}
