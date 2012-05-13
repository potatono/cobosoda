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
public class ProcessingEnterInitials extends ProcessingMode {
	private static final int MAX_CHARACTERS = 3;
	private ProcessingKeyboard keyboard;
	private boolean isLocked = true;
	private int mouseButtonStatus = ProcessingPlayConstructor.MOUSE_OFF;
	private Model model;
	private ProcessingCharacterInput characters;
	
	public ProcessingEnterInitials(PApplet papplet, ProcessingInterface proc,
			Model m) {
		super(papplet, proc);
	
		model = m.clone();
		keyboard = new ProcessingKeyboard(pa, pi);
		characters = new ProcessingCharacterInput(pa, pi, MAX_CHARACTERS);
	}
	
	@Override
	public void display() {
		listenToMouse();
		
		if (mouseButtonStatus == ProcessingPlayConstructor.MOUSE_CLICK) {
			String key = keyboard.getKeyPressed();
			processKeyboard(key);
		}
		
		drawModel();
		keyboard.display();
		
		drawEnterName();
		characters.display();
	}

	private void processKeyboard(String s) {
		if (s.matches("SPACE")) {
			characters.addLetter("x");
		} else if (s.matches("BACK")) {
			characters.removeLetter();
		} else if (s.matches("ENTER")) {
			nextMode();
		} else {
			characters.addLetter(s);
		}
	}

	public void drawEnterName() {
		pa.stroke(90);
//		pa.line(250, 0, 250, 600);  /* For Centering */
//		pa.line(550, 0, 550, 600);
		PMethod.writeCenteredText(pa, "WHAT ARE YOUR INITIALS?", 400,
				264, 24, pi.font24, 24);
	}
	
	public void drawModel() {
		PMethod.drawModel(model, 0, 228, pa);
	}
	
	private void listenToMouse() {
		if (!isLocked && pa.mousePressed) {
			isLocked = true;
			mouseButtonStatus = ProcessingPlayConstructor.MOUSE_CLICK;
		} else if (isLocked && pa.mousePressed) {
			mouseButtonStatus = ProcessingPlayConstructor.MOUSE_HOLD;
		} else if (isLocked && !pa.mousePressed) {
			mouseButtonStatus = ProcessingPlayConstructor.MOUSE_RELEASE;
			isLocked = false;
		} else {
			mouseButtonStatus = ProcessingPlayConstructor.MOUSE_OFF;
			isLocked = false;
		}
	}
	
	private void nextMode() {
		model.setCreatorInitials(characters.getString());
		pi.setMode(new ProcessingPlayFirstRace(pa, pi, model.clone()));		
	}
}
		