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
public class ProcessingBootOff extends ProcessingMode {
	private static final int BACKGROUND = 0;
	private static final int TIME = 20;
	private int counter = 0;
	
	public ProcessingBootOff(PApplet papplet, ProcessingInterface proc) {
		super(papplet, proc);
	}	
	
	public void display() {
		pa.background(BACKGROUND);
		timer();
	}
	
	private void timer() {
		if (counter >= TIME) {
			pi.setMode(new ProcessingBootGrid(pa, pi));
		}
		counter++;
	}
}
