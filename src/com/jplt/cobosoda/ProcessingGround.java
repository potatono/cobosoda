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
public class ProcessingGround extends ProcessingMode {
	private static final int MARKER_OFFSET = 20;
	private int groundLevel;
	private double offset;
	
	public ProcessingGround(PApplet papplet, ProcessingInterface proc, int y) {
		super(papplet, proc);
		
		groundLevel = y;
	}
	
	@Override
	public void display() {
		pa.stroke(0, 128, 0);
		pa.strokeWeight(1);
		
		pa.line(0, groundLevel, Config.WINDOW_X, groundLevel);
		
		drawMarkers();
	}
	
	private void drawMarkers() {
		int o = (int) (offset * Config.PIXELS_PER_METER ) % MARKER_OFFSET;
		
		pa.stroke(0, 128, 0);
		pa.strokeWeight(1);
		
		for (int i = 0; i < Config.WINDOW_X + MARKER_OFFSET; i += MARKER_OFFSET) {
			pa.line(i + o, groundLevel, i + o, groundLevel + 5);
		}
	}
	
	public void setOffset(double o) {
		offset = o;
	}
}
