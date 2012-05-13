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
public class ProcessingTimer extends ProcessingMode {
	private final static int DEFAULT_TIME_LIMIT = PMethod.RACE_TIME;
	private int x = 50;
	private int y = 200;
	private double timeLeft;
	
	public ProcessingTimer(PApplet papplet, ProcessingInterface proc) {
		super(papplet, proc);
		timeLeft = DEFAULT_TIME_LIMIT;
	}
		
	public ProcessingTimer(PApplet papplet, ProcessingInterface proc, int t) {
		super(papplet, proc);
		setTime(t);
	}
		
	@Override
	public void display() {
//		pa.fill(255);
//		pa.textFont(pi.font36, 36);
//		pa.text("TIME\n " + (int) timeLeft, x, y);
		PMethod.writeCenteredText(pa, "TIME", 400,
				y, 38, pi.font36, 36);
		
		String s = String.format("%02d", (int) timeLeft);
		
		PMethod.writeCenteredText(pa, s, 400,
				y + 40, 38, pi.font36, 36);

//		PMethod.writeCenteredText(pa, "MUTATIONS LEFT:" + mutationsLeft, 400,
//				ProcessingPlaySecondRace.GROUND_LEVEL_1 + 52, 26, pi.font24, 24);

		timeLeft = timeLeft - 1.0 / (double) Config.FRAMES_PER_SECOND;
	}
	
	public void setTextLocation(int nx, int ny) {
		x = nx;
		y = ny;
	}
	
	public boolean hasTimerExpired() {
		if (timeLeft <= 0.0) {
			return true;
		}
		
		return false;
	}
	
	public void setTime(int t) {
		timeLeft = Math.abs(t);
	}
}
