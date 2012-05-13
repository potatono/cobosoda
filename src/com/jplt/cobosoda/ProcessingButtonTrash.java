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
public class ProcessingButtonTrash extends ProcessingButton {

	private static final double SCALE = 9.0;

	public ProcessingButtonTrash(PApplet papplet, ProcessingInterface proc,
			int posX, int posY, String s) {
		super(papplet, proc, posX, posY, s);
	}

	@Override
	public void display() {
		drawBody();

	}

	public void drawBucket() {
		drawBody();
	}

	public void drawBody() {
		/* Body */
		int x0 = (int) (0.0 * SCALE) + x;
		int y0 = (int) (0.0 * SCALE) + y;
		int x1 = (int) (6.0 * SCALE) + x;
		int y1 = (int) (0.0 * SCALE) + y;
		int x2 = (int) (5.0 * SCALE) + x;
		int y2 = (int) (7.0 * SCALE) + y;
		int x3 = (int) (1.0 * SCALE) + x;
		int y3 = (int) (7.0 * SCALE) + y;

		pa.noFill();
		pa.stroke(128);
		pa.quad(x0, y0, x1, y1, x2, y2, x3, y3);

		/* Body Accent lines */
		x0 = (int) (1.5 * SCALE) + x;
		y0 = (int) (1.0 * SCALE) + y;
		x1 = (int) (2.0 * SCALE) + x;
		y1 = (int) (6.0 * SCALE) + y;

		pa.line(x0, y0, x1, y1);

		x0 = (int) (3 * SCALE) + x;
		y0 = (int) (1.0 * SCALE) + y;
		x1 = (int) (3.0 * SCALE) + x;
		y1 = (int) (6.0 * SCALE) + y;

		pa.line(x0, y0, x1, y1);

		x0 = (int) (4.5 * SCALE) + x;
		y0 = (int) (1.0 * SCALE) + y;
		x1 = (int) (4.0 * SCALE) + x;
		y1 = (int) (6.0 * SCALE) + y;

		pa.line(x0, y0, x1, y1);

		/* Lid handle */
		x0 = (int) (2.0 * SCALE) + x;
		y0 = (int) (-0.5 * SCALE) + y;
		x1 = (int) (4.0 * SCALE) + x;
		y1 = (int) (-0.5 * SCALE) + y;
		x2 = (int) (4.0 * SCALE) + x;
		y2 = (int) (-1.0 * SCALE) + y;
		x3 = (int) (2.0 * SCALE) + x;
		y3 = (int) (-1.0 * SCALE) + y;

		pa.noFill();
		pa.quad(x0, y0, x1, y1, x2, y2, x3, y3);

		/* Lid */
		x0 = (int) (-0.5 * SCALE) + x;
		y0 = (int) (0.0 * SCALE) + y;
		x1 = (int) (6.5 * SCALE) + x;
		y1 = (int) (0.0 * SCALE) + y;
		x2 = (int) (6.5 * SCALE) + x;
		y2 = (int) (-0.5 * SCALE) + y;
		x3 = (int) (-0.5 * SCALE) + x;
		y3 = (int) (-0.5 * SCALE) + y;

		pa.fill(128);
		pa.quad(x0, y0, x1, y1, x2, y2, x3, y3);
	}

	public boolean isMouseHovering() {
		if (pa.mouseX > x && pa.mouseX < x + SCALE * 6.0 &&
				pa.mouseY > y && pa.mouseY < y + SCALE * 7.0) {
			return true;
		}

		return false;
	}	
	
}
