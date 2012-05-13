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
public class ProcessingBootGrid extends ProcessingMode {
	private static final int GRID_SIZE = 32;
	private static final int STROKE = 255;
	private static final int BACKGROUND = 0;
	private static final int STROKE_WEIGHT = 4;
	private static final int TIME = 20;
	private int counter = 0;
	
	public ProcessingBootGrid(PApplet papplet, ProcessingInterface proc) {
		super(papplet, proc);
	}	
	
	public void display() {
		pa.background(BACKGROUND);
		pa.stroke(STROKE);
		pa.strokeWeight(STROKE_WEIGHT);

		for (int i = 2; i < 798; i = i + GRID_SIZE) {
			for (int j = 2; j < 598; j = j + GRID_SIZE) {
				pa.line(i, 0, i, 598);
				pa.line(0, j, 798, j);
			}
		}
		
		timer();
	}
	
	private void timer() {
		if (counter >= TIME) {
			pi.setMode(new ProcessingBootTest(pa, pi));
		}
		counter++;
	}		
}
