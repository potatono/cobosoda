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
public class ProcessingSlider extends ProcessingMode {

	private static final int SLIDER_WIDTH = 60;
	private static final int SLIDER_HEIGHT = 20;
	private static final int STROKE = 128;
	private int x;
	private int y;
	private int track_width;
	private double minimumValue;
	private double maximumValue;
	private double position;  /* 0 to 1 */
	private boolean isActive = false;
	private SynthEngine synth;
	public ProcessingSlider(PApplet papplet, ProcessingInterface proc,
			int nx, int ny, int w, double min, double max) {
		super(papplet, proc);
		
		synth = pi.getSynth();
		x = nx;
		y = ny;
		track_width = w;
		minimumValue = min;
		maximumValue = max;
	}

	public void display() {
		pa.noFill();
		pa.stroke(STROKE);
		pa.strokeWeight(1);

		/* Track */
		pa.line(x, y, x + track_width, y);

		/* Slider */
		if (isActive) {
			pa.fill(STROKE);
			pa.rect((int) ((double) x + (position * (double) track_width)) -
					SLIDER_WIDTH / 2, y - SLIDER_HEIGHT / 2,
					SLIDER_WIDTH, SLIDER_HEIGHT);
		}
	}

	public double getPosition() {
		return position;
	}

	public double getValue() {
		return position * (maximumValue - minimumValue) + minimumValue;
	}

	public void setPosition(double p) {
		position = p;

		if (position > 1.0) {
			position = 1.0;
		} else if (position < 0.0) {
			position = 0;
		}
	}

	public void setValue(double v) {
		setPosition((v - minimumValue) / (maximumValue - minimumValue));
	}

	public void setCoordinate() {
		setPosition(((double) pa.mouseX - (double) x) / (double) track_width);
	}
	
	public void setActive(boolean b) {
		isActive = b;
	}
	
	public boolean isMouseHovering() {
		if (pa.mouseX >= (x + (position * (double) track_width)) - SLIDER_WIDTH / 2 &&
				pa.mouseY >= y - SLIDER_HEIGHT / 2 &&
				pa.mouseX < (x + (position * (double) track_width)) - SLIDER_WIDTH / 2 + SLIDER_WIDTH &&
				pa.mouseY < y - SLIDER_HEIGHT / 2 + SLIDER_HEIGHT) {
			return true;
		}
		
		return false;
	}

	private boolean isLocked;
	
	public void move(int mouseButtonStatus) {
		if (mouseButtonStatus == ProcessingPlayConstructor.MOUSE_CLICK) {
			if (isMouseHovering()) {
				synth.clickSlider();
				isLocked = true;
			}
		} else if (mouseButtonStatus == ProcessingPlayConstructor.MOUSE_HOLD) {
			if (isLocked) {
				setCoordinate();
			}
		} else if (mouseButtonStatus == ProcessingPlayConstructor.MOUSE_RELEASE) {
			isLocked = false;
		}		
	}
}
