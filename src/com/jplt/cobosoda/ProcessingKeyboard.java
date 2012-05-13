/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jplt.cobosoda;

import java.util.ArrayList;
import processing.core.*;

/**
 *
 * @author Jacob Joaquin
 */
public class ProcessingKeyboard extends ProcessingMode {

	private static final int KEY_SIZE = 36;
	private static final int OFFSET = 6;
	private static final int Y_OFFSET = 360;
	private ArrayList<Key> keys;
	private SynthEngine synth;
	
	public ProcessingKeyboard(PApplet papplet, ProcessingInterface proc) {
		super(papplet, proc);

		synth = pi.getSynth();
		
		keys = new ArrayList<Key>();

		String row0 = "1234567890";
		String row1 = "QWERTYUIOP";
		String row2 = "ASDFGHJKL-";
		String row3 = "ZXCVBNM";
//		String row4 = "QWERTYUIOP";

		int xo = (Config.WINDOW_X - (KEY_SIZE + OFFSET) * 10) / 2;

		/* Create Keyboard */
		for (int i = 0; i < row0.length(); i++) {
			keys.add(new Key(pa, pi, i * (KEY_SIZE + OFFSET) + xo, Y_OFFSET,
					KEY_SIZE, KEY_SIZE, row0.substring(i, i + 1)));
		}

		for (int i = 0; i < row1.length(); i++) {
			keys.add(new Key(pa, pi, i * (KEY_SIZE + OFFSET) + xo,
					Y_OFFSET + KEY_SIZE + OFFSET, KEY_SIZE, KEY_SIZE,
					row1.substring(i, i + 1)));
		}

		for (int i = 0; i < row2.length(); i++) {
			keys.add(new Key(pa, pi, i * (KEY_SIZE + OFFSET) + xo,
					Y_OFFSET + (KEY_SIZE + OFFSET) * 2, KEY_SIZE, KEY_SIZE,
					row2.substring(i, i + 1)));
		}

		for (int i = 0; i < row3.length(); i++) {
			keys.add(new Key(pa, pi, i * (KEY_SIZE + OFFSET) + xo,
					Y_OFFSET + (KEY_SIZE + OFFSET) * 3, KEY_SIZE, KEY_SIZE,
					row3.substring(i, i + 1)));
		}

		keys.add(new Key(pa, pi, 7 * (KEY_SIZE + OFFSET) + xo,
				Y_OFFSET + (KEY_SIZE + OFFSET) * 3, (KEY_SIZE + OFFSET) * 3,
				KEY_SIZE, "BACK"));

		keys.add(new Key(pa, pi, xo,
				Y_OFFSET + (KEY_SIZE + OFFSET) * 4, (KEY_SIZE + OFFSET) * 5,
				KEY_SIZE, "SPACE"));

		keys.add(new Key(pa, pi, 6 * (KEY_SIZE + OFFSET) + xo,
				Y_OFFSET + (KEY_SIZE + OFFSET) * 4, (KEY_SIZE + OFFSET) * 4,
				KEY_SIZE, "ENTER"));
	}

	@Override
	public void display() {
		pa.noFill();
		pa.stroke(255);

		for (Key k : keys) {
			k.display();
		}
	}

	public String getKeyPressed() {
		for (Key k : keys) {
			if (k.isMouseHovering()) {
				synth.keyPressed(k.getText());
				return k.getText();
			}
		}
		
		return "";
	}

	public class Key extends ProcessingMode {

		private int x;
		private int y;
		private int height;
		private int width;
		private String text;
		private int fontSize;

		public Key(PApplet papplet, ProcessingInterface proc, int nx, int ny,
				int w, int h, String t) {
			super(papplet, proc);

			x = nx;
			y = ny;
			width = w;
			height = h;
			text = t;
			fontSize = 36;
		}

		@Override
		public void display() {
			pa.stroke(128);
			pa.noFill();
			pa.rect(x, y, width, height);

			pa.fill(255);
			pa.textFont(pi.font24, 24);
			int tw = text.length() * fontSize + 2;
			int c = (width - tw) / 2;
			pa.text(text, x + 8, y + height - 6);
		}

		public String getText() {
			return text;
		}

		public boolean isMouseHovering() {
			if (pa.mouseX > x && pa.mouseX < x + width &&
					pa.mouseY > y && pa.mouseY < y + height) {
				return true;
			}

			return false;
		}
	}
}
		










