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
public class ProcessingScore extends ProcessingMode {
	private final static int FONT_SIZE = 24;
	private int x = 50;
	private int y = 100;
	private double score;
	private PFont font;
	
	public ProcessingScore(PApplet papplet, ProcessingInterface proc) {
		super(papplet, proc);

		font = pa.loadFont("VectorBattle-24.vlw");
	}

	
	@Override
	public void display() {
//		pa.fill(255);
//		pa.textFont(font, FONT_SIZE);
//		pa.text("DISTANCE: " + score, x, y);
		StringBuffer sb = new StringBuffer(64);
		sb.append("DISTANCE: ");
		sb.append(score);
		
		PMethod.writeText(pa, sb.toString(), x,
				y, 26, pi.font24, 24);
	}
	
	public void setScore(double s) {
		score = Math.abs(s);
	}
	
	public void setTextLocation(int nx, int ny) {
		x = nx;
		y = ny;
	}
}
