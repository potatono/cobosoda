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
public class ProcessingFinalRank extends ProcessingMode {
	private ProcessingHighScoreTable highScore;
	private static final int MAX_TIME = 90;
	private int counter = 0;
	private boolean showingStep0 = true;
	
	public ProcessingFinalRank(PApplet papplet, ProcessingInterface proc,
			String s) {
		super(papplet, proc);

		System.out.println(s);
		ProcessingModelScore pms = pi.getMutantHighScoreByName(s);
		System.out.println(pi.getMutantHighScores().indexOf(pms.getName()));
		int rank = pi.getMutantHighScores().indexOf(pms.getName());
		System.out.println(rank);
		
		highScore = new ProcessingHighScoreTable(pa, pi, pi.getUserHighScores(),
				rank);
	}

	@Override
	public void display() {

		if (showingStep0) {
			PMethod.writeCenteredText(pa, "DESIGN RANK",
					Config.WINDOW_X / 2, 56, 48, pi.font48, 48);
		} else {
			PMethod.writeCenteredText(pa, "EVOLUTION RANK",
					Config.WINDOW_X / 2, 56, 48, pi.font48, 48);
			
		}

		highScore.display();
		updateCounter();
	}
	
	public void updateCounter() {
		if (counter == MAX_TIME) {
			highScore = new ProcessingHighScoreTable(pa, pi, pi.getMutantHighScores(),
					0);
			showingStep0 = false;
		} else if (counter >= MAX_TIME * 2) {
			pi.reset();
		}
		
		counter++;
	}
}
