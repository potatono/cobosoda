/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.jplt.cobosoda;

import com.thumbuki.slipmat.module.synth.SinePerc;
import com.thumbuki.slipmat.*;
import com.thumbuki.slipmat.module.*;
import com.thumbuki.slipmat.module.effects.*;
import com.thumbuki.slipmat.module.mixer.*;
/**
 *
 * @author Jacob Joaquin
 */
public class SynthEngine {
	
	private static final double DURATION = 60 * 60 * 10;
	private ProcessingInterface pi;
	private SynthRack synthRack;
	private SynthStart startGame;
	private SinePerc sinePerc;
	private SynthGameChip mutationChip;
	private Add mixerLeft;
	private Add mixerRight;
	private Reverb reverbLeft;
	private Reverb reverbRight;
	private Output output;
	
	public SynthEngine(ProcessingInterface proc) {
		pi = proc;
		synthRack = new SynthRack(true);
		setupRack();
		synthRack.setDuration(DURATION);
		synthRack.startCsound();
	}
	
	private void setupRack() {
		startGame = new SynthStart();
		sinePerc = new SinePerc();
		mutationChip = new SynthGameChip();
		
		mixerLeft = new Add();
		mixerRight = new Add();
		reverbLeft = new Reverb(0.1, 0.5, 0.125);
		reverbRight = new Reverb(0.1, 0.4, 0.126);
		output = new Output();

		synthRack.addModule(startGame);
		synthRack.addModule(sinePerc);
		synthRack.addModule(mutationChip);
		synthRack.addModule(mixerLeft);
		synthRack.addModule(mixerRight);
		synthRack.addModule(reverbLeft);
		synthRack.addModule(reverbRight);
		synthRack.addModule(output);
		
		
		mixerLeft.addInputs(sinePerc.getOutput(), mutationChip.getLeftOutput(), startGame.getOutput());
		mixerRight.addInputs(sinePerc.getOutput(), mutationChip.getRightOutput(), startGame.getOutput());
		
		reverbLeft.setInput(mixerLeft.getOutput());
		reverbRight.setInput(mixerLeft.getOutput());
		
		output.setInput(reverbLeft.getOutput(), reverbRight.getOutput());
		
		sinePerc.setDuration(0.25);
	}

	public void testTone() {
		mutationChip.playNote(0.0, 0.05, 0.2, 100, 1, 0.5);
	}
	
	public void playStartSound() {
		startGame.playNote(0.0, 0.5);
	}
	
	public void buttonMode() {
		mutationChip.playNote(0.0, 0.05, 0.1, 440.0, 1, 0.5);
	}
	
	public void buttonModeFinish() {
		mutationChip.playNote(0.0, 0.09, 0.1, 880.0, 1, 0.5);
	}

	public void clickJoint() {
		mutationChip.playNote(0.0, 0.02, 0.1, 300.0, 1, 0.5);
		mutationChip.playNote(0.02, 0.03, 0.1, 450.0, 1, 0.5);
	}
	
	public void clickJointInSpringMode() {
		mutationChip.playNote(0.0, 0.02, 0.1, 600.0, 1, 0.5);
		mutationChip.playNote(0.02, 0.03, 0.1, 900.0, 1, 0.5);
	}
	
	public void selectSpring() {
		mutationChip.setFreqEnv(1.5, 0.04, 1, 0.01, 1, 0.001, 1);
		mutationChip.playNote(0.0, 0.04, 0.1, 600.0, 1, 0.5);
	}
	
	public void attachSpring() {
		mutationChip.playNote(0.0, 0.02, 0.1, 900.0, 1, 0.5);
		mutationChip.playNote(0.02, 0.03, 0.1, 600.0, 1, 0.5);
	}
	
	public void clickSlider() {
		mutationChip.playNote(0.0, 0.02, 0.1, 100.0, 1, 0.5);
		mutationChip.playNote(0.02, 0.03, 0.1, 250.0, 1, 0.5);
	}

	public void trash() {
		mutationChip.setFreqEnv(5, 0.1, 1, 0.01, 1, 0.001, 1);
		mutationChip.playNote(0.0, 0.1, 0.1, 40, 1, 0.5);
		
	}
	
	public void footStep(Joint j) {
		/* Foot */
		sinePerc.playNote(0.0, 0.1, 100 * (j.getIndex() + 4));

		/* Ground */
			double scale[] = { 0, 2, 4, 5, 7, 9, 11, 12 };
		
		int index = (int) (j.getX() / 10.0 * scale.length);
		
		while (index < 0) {
			index += scale.length;
		}
		
		while (index >= scale.length) {
			index -= scale.length;
		}

		double freq = 440.0 * halfStepsToRatio(scale[index]);
		sinePerc.playNote(0.0, 0.1, freq);
	}
	
	public void mutation(int mutationsLeft, int successfulMutations) {
		double amp = 0.25 * (double) mutationsLeft /
				(double) ProcessingEvolveStage.MAX_MUTATIONS_LEFT;
		
		double freq = 30.0 * Math.pow(2, (Math.round(Math.random() *
				(double) successfulMutations * 3.0)) / 12.0);

		mutationChip.playNote(0.0, 0.1, amp, freq, 1, Math.random());
	}

	public void evolve(int successfulMutations) {
		double freq = 880;
		double time = 0.0;
		
		for (int i = 0; i < successfulMutations; i++) {
			sinePerc.playNote(time, 0.3, freq);
			time += 0.05;
			
			double d = Math.floor(Math.random() * 3.0 + 3);
			double r = Math.floor(Math.random() * 3.0 + 3);
			
			freq = freq * d / r;
			
			while (freq < 50 || freq > 20000) {
				freq = freq * d / r;
			}
		}
	}
	
//		public void playNote(double time, double dur, double amp, double freq,
//			int t, double pan) {
	public void keyPressed(String s) {
		if (s.equals("BACK")) {
			mutationChip.playNote(0.0, 0.05, 0.2, 50, 1, 0.5);
		} else if (s.equals("ENTER")) {
			mutationChip.playNote(0.0, 0.09, 0.1, 880.0, 1, 0.5);
		} else {
			mutationChip.playNote(0.0, 0.05, 0.1, 440.0, 1, 0.5);
		}
	}
	
	/**
	 * Converts half steps into a ratio.
	 * 
	 * @param hs Half-steps
	 * @return   Ratio
	 */
	private double halfStepsToRatio(double hs) {
		return Math.pow(2.0, hs / 12.0);
	}	
}










