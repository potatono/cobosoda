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
public class ProcessingEnterName extends ProcessingMode {

	private static final int MAX_CHARACTERS = 16;
	private ProcessingKeyboard keyboard;
	private boolean isLocked = true;
	private int mouseButtonStatus = ProcessingPlayConstructor.MOUSE_OFF;
	private StringBuffer input;
	private Model model;
	private ProcessingCharacterInput characters;
	private int textCounter = 0;

	public ProcessingEnterName(PApplet papplet, ProcessingInterface proc,
			Model m) {
		super(papplet, proc);

		model = m.clone();
//		model = PMethod.centerModel(World.createTestModel());
		model = PMethod.groundModel(model);
		input = new StringBuffer(MAX_CHARACTERS);
		keyboard = new ProcessingKeyboard(pa, pi);
		characters = new ProcessingCharacterInput(pa, pi, MAX_CHARACTERS);
	}

	@Override
	public void display() {
		listenToMouse();
		updateTextCounter();
		
		if (mouseButtonStatus == ProcessingPlayConstructor.MOUSE_CLICK) {
			String key = keyboard.getKeyPressed();
			processKeyboard(key);
		}

		drawModel();
		keyboard.display();

		drawEnterName();
		characters.display();
	}

	private void updateTextCounter() {
		if (textCounter > 0) {
			textCounter--;
		}
	}
	public void drawEnterName() {
		pa.stroke(90);
//		pa.line(250, 0, 250, 600);  /* For Centering */
//		pa.line(550, 0, 550, 600);
		pa.fill(255);
		pa.textFont(pi.font24, 24);

		String message;

		if (textCounter <= 0) {
			message = "WHAT IS ITS NAME?";
		} else {
			message = "NAME TAKEN. TRY AGAIN.";
		}

		PMethod.writeCenteredText(pa, message, 400,
				264, 24, pi.font24, 24);
	}

	public void drawModel() {
		PMethod.drawModel(model, 0, 228, pa);
	}

	private void processKeyboard(String s) {
		if (s.matches("SPACE")) {
			characters.addLetter("x");
		} else if (s.matches("BACK")) {
			characters.removeLetter();
		} else if (s.matches("ENTER")) {
			if (!isDuplicate()) {
				nextMode();
			} else {
				textCounter = 60;
			}
				
		} else {
			characters.addLetter(s);
		}
	}

	private boolean isDuplicate() {
		for (ProcessingModelScore pms : pi.getUserHighScores()) {
			if (pms.getName().equals(characters.getString())) {
				return true;
			}
		}

		return false;
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
		model.setName(characters.getString());
		pi.setMode(new ProcessingEnterInitials(pa, pi, model.clone()));
	}
}
		