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
public class ProcessingCharacterInput extends ProcessingMode {
	private static final int UNDERSCORE_OFFSET = 6;
	private static final int LETTER_OFFSET = 24;
	
	private int maxLength;
	private StringBuffer text;
	private int x;
	private int y;
	
	
	
	public ProcessingCharacterInput(PApplet papplet, ProcessingInterface proc,
			int length) {
		super(papplet, proc);
		
		maxLength = length;
		text = new StringBuffer(maxLength);
		x = Config.WINDOW_X - (LETTER_OFFSET + UNDERSCORE_OFFSET) * maxLength;
		x = x / 2;
		y = 320;
	}
	
	@Override
	public void display() {
		drawUnderscores();
		drawCursor();
		drawLetters();
	}

	public void drawUnderscores() {
		pa.stroke(128);
		
		for (int i = 0; i < maxLength; i++) {
			pa.line(x + i * (LETTER_OFFSET + UNDERSCORE_OFFSET),
					y,
					x + i * (LETTER_OFFSET + UNDERSCORE_OFFSET) + LETTER_OFFSET,
					y);
		}
	}
	
	public void drawCursor() {
		pa.stroke(128);
		pa.noFill();
		
		int p = x + text.length() * (LETTER_OFFSET + UNDERSCORE_OFFSET);
		
		int h = y - 6;
		
		pa.quad(p, h,
				p + LETTER_OFFSET, h,
				p + LETTER_OFFSET, h - 24,
				p, h - 24);
	}

	public void drawLetters() {
		int p = x + text.length() * (LETTER_OFFSET + UNDERSCORE_OFFSET);

		pa.fill(255);
		pa.textFont(pi.font24, 24);
		for (int i = 0; i < text.length(); i++) {
			String s = text.substring(i, i + 1);
			
			/* Spaces are stored internally as "x" */
			if (s.matches("x")) {
				s = " ";
			}
			
			pa.text(s,
					x + i * (LETTER_OFFSET + UNDERSCORE_OFFSET),
					y - 6);
		}
	}
	
	public void addLetter(String s) {
		if (text.length() < maxLength) {
			text.append(s);
		}
	}
	
	public void removeLetter() {
		if (text.length() > 0) {
			text.deleteCharAt(text.length() - 1);
		}
	}	
	
	public String getString() {
		return sanitizeInput();
	}
	
	private String sanitizeInput() {
		StringBuffer sanitized = new StringBuffer(16);
		sanitized.append(text.toString());
		
		/* extend lenth of string to max_characters */
		while(sanitized.length() < maxLength) {
			sanitized.append("x");
		}
		
		return sanitized.toString();
	}
}












