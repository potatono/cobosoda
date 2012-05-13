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
public class ProcessingStandbyTitle extends ProcessingMode {
	private static final int TIME_PER_DISPLAY = Config.FRAMES_PER_SECOND * 15;
	private static final int MAX_MODEL_ROTATION = 5;
	private int modelRotation = 0;
	private int counter = 0;
	private int freePlayCounter = 0;
	private int displayCounter = 0; //TIME_PER_DISPLAY * 3;
	private ProcessingWalkingModel walker;
	private ProcessingHighScoreTable hst0;
	private ProcessingHighScoreTable hst1;
	
	Model model;
	
	public ProcessingStandbyTitle(PApplet papplet, ProcessingInterface proc) {
		super(papplet, proc);
		
		model = pi.getMutantHighScores().get(modelRotation()).getModel();
		walker = new ProcessingWalkingModel(pa, pi, model, 400);
		
		hst0 = new ProcessingHighScoreTable(pa, pi, pi.getUserHighScores(), 0);
		hst1 = new ProcessingHighScoreTable(pa, pi, pi.getMutantHighScores(), 0);
	}

	public void display() {
		pa.background(0);
		pa.fill(255);

		if (displayCounter < TIME_PER_DISPLAY * 2) {
		drawHeader();
			updateCounter();
			walker.update();
			walker.display();
			drawModelInfo();
			drawFreePlay();
		} else if(displayCounter >= TIME_PER_DISPLAY * 2&&
				displayCounter < TIME_PER_DISPLAY * 3) {
			PMethod.writeCenteredText(pa, "DESIGN HIGH SCORE",
					Config.WINDOW_X / 2, 56, 46, pi.font48, 48);
			hst0.display();
		} else if(displayCounter >= TIME_PER_DISPLAY * 3 &&
				displayCounter < TIME_PER_DISPLAY * 4) {
		PMethod.writeCenteredText(pa, "EVOLVE HIGH SCORE",
				Config.WINDOW_X / 2, 56, 46, pi.font48, 48);
			hst1.display();
		}
		
		if (pa.mousePressed) {
			pi.getSynth().playStartSound();
			pi.setMode(new ProcessingPlayConstructor(pa, pi));
		}
		
		updateDisplayCounter();
	}

	private int modelRotation() {
		modelRotation = (modelRotation + 1) % pi.getMutantHighScores().size();
		return modelRotation;
	}
	
	private void updateCounter() {
		counter++;
		
		if (counter >= 400) {
			model = pi.getMutantHighScores().get(modelRotation()).getModel();
			walker = new ProcessingWalkingModel(pa, pi, model, 400);
			counter = 0;
		}
	} 
	
	private void updateDisplayCounter() {
		if (displayCounter == 0) {
			hst0 = new ProcessingHighScoreTable(pa, pi, pi.getUserHighScores(), 0);
			hst1 = new ProcessingHighScoreTable(pa, pi, pi.getMutantHighScores(), 0);
		}
		
		displayCounter++;
		displayCounter %= TIME_PER_DISPLAY * 4;
	}
	
	private void drawHeader() {
		PMethod.writeCenteredText(pa, "C O B O S O D A",
				Config.WINDOW_X / 2, 56, 48, pi.font48, 48);
	}
	
	private void drawModelInfo() {
		String name = PMethod.substituteDelimiter(model.getName());
		
		if (model.getMutationIndex() > 0) {
			name = name + "." + model.getMutationIndex();
		}
		
		PMethod.writeCenteredText(pa, name,
				Config.WINDOW_X / 2, 550, 38, pi.font36, 36);
	}
	
	private void drawFreePlay() {
		if (freePlayCounter < 60) {
			PMethod.writeCenteredText(pa, "FREE PLAY",
					Config.WINDOW_X / 2, 100, 24, pi.font24, 24);
		}
		
		freePlayCounter++;
		freePlayCounter %= 120;
	}
}
