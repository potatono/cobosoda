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
public class ProcessingButton extends ProcessingMode {

	public static final int WIDTH = 90;
	public static final int HEIGHT = 32;
	private static final int FONT_SIZE = 18;
	protected int x;
	protected int y;
	private String text;
	private boolean isSet = false;
	private PFont font;

	public ProcessingButton(PApplet papplet, ProcessingInterface proc,
			int posX, int posY, String s) {
		super(papplet, proc);

		x = posX;
		y = posY;
		text = s;
		font = pi.font18;
	}

	public void display() {
		if (!isSet) {
			pa.fill(0);
		} else {
			pa.fill(60);
		}

		pa.stroke(128);
		pa.rect(x, y, WIDTH, HEIGHT);

		if (isSet) {
			pa.fill(0);
			pa.textFont(font, FONT_SIZE);
			pa.text(text, x + 4, y + HEIGHT - 4);
			pa.text(text, x + 3, y + HEIGHT - 4);
		} else {
			pa.fill(255);
			pa.textFont(font, FONT_SIZE);
			pa.text(text, x + 3, y + HEIGHT - 4);
		}

	}

	public void setToggle(boolean b) {
		isSet = b;
	}

	public boolean isMouseHovering() {
		if (pa.mouseX > x && pa.mouseX < x + WIDTH &&
				pa.mouseY > y && pa.mouseY < y + HEIGHT) {
			return true;
		}

		return false;
	}
}
