/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jplt.cobosoda;

import processing.core.*;
import java.io.*;
/**
 *
 * @author Jacob Joaquin
 */
public class ProcessingFinalScore extends ProcessingMode {
	private static final String FILE_PATH = "arcade";
	private Model originalModel;
	private Model winnerModel;
	private ProcessingRaceStats originalStats;
	private ProcessingRaceStats winnerStats;
	private ProcessingTestModel tester;
	private int counter = 0;
	
	public ProcessingFinalScore(PApplet papplet, ProcessingInterface proc,
			Model mOriginal, Model mWinner) {
		super(papplet, proc);
		
		originalModel = mOriginal;
		winnerModel = mWinner;
		
		/* Get race stats */
		tester = new ProcessingTestModel(60);
		originalStats = tester.race(originalModel.clone());
		tester = new ProcessingTestModel(60);
		winnerStats = tester.race(winnerModel.clone());

		originalModel.setDistanceWalked(originalStats.getMaxDistance());
		winnerModel.setDistanceWalked(winnerStats.getMaxDistance());
		
		System.out.println(originalModel.getName());
		System.out.println(originalModel.getCreatorInitials());
		System.out.println(originalModel.getMutationIndex());
		System.out.println(originalModel.getDistanceWalked());
		System.out.println(winnerModel.getName());
		System.out.println(winnerModel.getCreatorInitials());
		System.out.println(winnerModel.getMutationIndex());
		System.out.println(winnerModel.getDistanceWalked());

		/* Save files */
		originalModel.save(FILE_PATH + "/" + originalModel.getName() + ".user");
		winnerModel.save(FILE_PATH + "/" + winnerModel.getName() + ".mutant");

//		pi.reloadScores();
	}

	@Override
	public void display() {
		updateCounter();
		PMethod.drawModel(originalModel, -2, 280, pa);
		PMethod.drawModel(winnerModel, 2, 280, pa);

		drawHeader();
		drawName();
		drawRace1();
		drawRace1Stats();
		drawRace2();
		drawRace2Stats();
		drawEvolutions();
		drawEvolutionsStats();
		drawImprovement();
		drawImprovementStats();
		
		if (counter == 8 * 30) {
			String s = originalModel.getName();
//			pi.setMode(new ProcessingFinalRank(pa, pi, s));
			pi.reset();
		}
	}
	
	private void updateCounter() {
		counter++;
	}
	
	public static final int LEFT_BORDER = 40;
	public static final int Y = 400;
	public static final int OFFSET_Y = 40;
	
	public void drawHeader() {
		PMethod.writeCenteredText(pa, "S C O R E",
				Config.WINDOW_X / 2, 56, 48, pi.font48, 48);
	}
	
	public void drawName() {
		PMethod.writeCenteredText(pa, PMethod.substituteDelimiter(originalModel.getName()),
				Config.WINDOW_X / 2, 340, 36, pi.font36, 36);
	}
	
	public void drawRace1() {
		PMethod.writeText(pa, "RACE 1:", LEFT_BORDER,
				Y, 26, pi.font24, 24);
	}

	public void drawRace1Stats() {
		PMethod.writeText(pa, "" + originalStats.getMaxDistance(),
				360, Y, 22, pi.font24, 24);
	}

	public void drawRace2() {
		PMethod.writeText(pa, "RACE 2", LEFT_BORDER,
				Y + OFFSET_Y, 26, pi.font24, 24);
	}

	public void drawRace2Stats() {
		PMethod.writeText(pa, "" + winnerStats.getMaxDistance(),
				360, Y + OFFSET_Y, 22, pi.font24, 24);
	}

	public void drawEvolutions() {
		PMethod.writeText(pa, "EVOLUTIONS", LEFT_BORDER,
				Y + OFFSET_Y * 2, 26, pi.font24, 24);
	}
	
	public void drawEvolutionsStats() {
		PMethod.writeText(pa, "" + winnerModel.getMutationIndex(),
				360, Y + OFFSET_Y * 2, 22, pi.font24, 24);
	}

	public void drawImprovement() {
		PMethod.writeText(pa, "RATIO", LEFT_BORDER,
				Y + OFFSET_Y * 3, 26, pi.font24, 24);
	}
	
	public void drawImprovementStats() {
		double imp = (winnerStats.getMaxDistance() / originalStats.getMaxDistance());
		String s = String.format("%.10f", imp);
		PMethod.writeText(pa, s + " %", 360, Y + OFFSET_Y * 3, 24,
				pi.font24, 24);
	}
}
