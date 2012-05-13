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
public class ProcessingWalkingModel extends ProcessingMode {
	private static final double SLEW_RATE = 0.99;
	private Model model;
	private double actualOffset = 0;
	private double averageOffset = 0;
	private double externalOffset = 0;
	private int groundLevel;
	private ProcessingRaceStats stat;
	
	public ProcessingWalkingModel(PApplet papplet, ProcessingInterface proc,
			Model m, int ground) {
		super(papplet, proc);
		
		model = m;
		groundLevel = ground;
		stat = new ProcessingRaceStats(model);
	}
	
	@Override
	public void display() {
//		elapseTime();
		PMethod.drawModel(model, averageOffset, groundLevel, pa);
	}

	public void update() {
		elapseTime();
		averageOffset();
	}
	
	private void elapseTime() {
		model.elapseTime(1000 / Config.FRAMES_PER_SECOND);
	}
	
	private void averageOffset() {
		actualOffset = stat.getDistance();
		double mixedOffset = actualOffset + externalOffset;
		averageOffset = averageOffset * SLEW_RATE + mixedOffset * (1.0 - SLEW_RATE);
	}
	
	public double getAverageOffset() {
		return averageOffset;
	}
	
	public double getActualOffset() {
		return actualOffset;
	}
	
	public void setExternalOffset(double eo) {
		externalOffset = eo;
	}
}
