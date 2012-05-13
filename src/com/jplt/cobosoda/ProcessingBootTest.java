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
public class ProcessingBootTest extends ProcessingMode {
	private static final int BACKGROUND = 0;
	private static final int FILL = 128;
	private static final int FONT_SIZE = 32;
	private static final int TIME = 60;
	private PFont font;
	private int counter = 0;
	
	public ProcessingBootTest(PApplet papplet, ProcessingInterface proc) {
		super(papplet, proc);

		font = pa.loadFont("VectorBattle-48.vlw");
	}	
	
	public void display() {
		pa.background(BACKGROUND);
		pa.fill(FILL);
		pa.textFont(font, FONT_SIZE);
		pa.text("PROCESSING   ... OK", 30, 60);
		pa.text("SLIPMAT      ... OK", 30, 100);
		pa.text("KERNEL       ... OK", 30, 140);
		timer();
	}
	
	private void timer() {
		if (counter >= TIME) {
			pi.setMode(new ProcessingStandbyTitle(pa, pi));
		}
		counter++;
	}	
}
