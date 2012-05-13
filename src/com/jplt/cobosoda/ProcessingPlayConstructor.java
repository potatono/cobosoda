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
public class ProcessingPlayConstructor extends ProcessingMode {
	/* Constants */

	public static final int MAX_JOINTS = 12;
	public static final int MIN_JOINTS = 12;
	public static final double AMP_MINIMUM = PMethod.AMP_MINIMUM;
	public static final double AMP_MAXIMUM = PMethod.AMP_MAXIMUM;
	public static final double FREQUENCY_MINIMUM = PMethod.FREQUENCY_MINIMUM;
	public static final double FREQUENCY_MAXIMUM = PMethod.FREQUENCY_MAXIMUM;
	public static final double PHASE_MINIMUM = PMethod.PHASE_MINIMUM;
	public static final double PHASE_MAXIMUM = PMethod.PHASE_MAXIMUM;
	private static final int MODEL_WINDOW_WIDTH = 512;
	private static final int MODEL_WINDOW_HEIGHT = 360;
	private static final int MODEL_WINDOW_X = Config.WINDOW_X / 2 -
			MODEL_WINDOW_WIDTH / 2;
	private static final int MODEL_WINDOW_Y = 64;
	private static final int EDIT_JOINT = 0;
	private static final int EDIT_SPRING = 1;
	private static final int EDIT_MUSCLE = 2;
	public static final int MOUSE_CLICK = 0;
	public static final int MOUSE_HOLD = 1;
	public static final int MOUSE_RELEASE = 2;
	public static final int MOUSE_OFF = 3;
	private static final int SPRING_K = 3;
	private static final double MUSCLE_FREQUENCY = 0.25;
	private static final double MUSCLE_AMP = 0.25;
	private static final double MUSCLE_PHASE = 0;
	private static final int SLIDER_WIDTH = 360;
	private static final int SLIDER_X = Config.WINDOW_X / 2 - SLIDER_WIDTH / 2;
	private static final int SLIDER_Y = 500;
	private static final int SLIDER_Y_OFFSET = 24;
	private static final double JOINT_SCALE = Config.JOINT_RADIUS * 3.0;
	/* Variables */
	private int edit_mode = EDIT_JOINT;
	private int mouseButtonStatus = MOUSE_OFF;
	private boolean isMouseDown = false;
	private Model model;
	private Joint jointLocked;
	private ArrayList<Joint> joints;
	private ArrayList<Spring> springs;
	private ArrayList<Muscle> muscles;
	private ProcessingButton buttonJoint;
	private ProcessingButton buttonSpring;
	private ProcessingButton buttonMuscle;
	private ProcessingButtonTrash buttonTrash;
	private ProcessingButton buttonDone;
	private Joint selectedJoint;
	private Spring selectedSpring;
	private Muscle selectedMuscle;
	private Joint hoverJoint;
	private Spring hoverSpring;
	private Muscle hoverMuscle;
	private ProcessingSlider sliderAmp;
	private ProcessingSlider sliderFrequency;
	private ProcessingSlider sliderPhase;
	private ProcessingSlider selectedSlider;
	private SynthEngine synth;
	private String errorMessage;
	private static final int ERROR_TOO_FEW_JOINTS = 0;
	private static final int ERROR_BROKEN_MODEL = 1;
	private int errorMessageDisplay = ERROR_TOO_FEW_JOINTS;
	private int errorMessageCounter = 0;

	public void drawErrorMessage() {
		if (errorMessageCounter > 0) {
			switch (errorMessageDisplay) {
				case ERROR_TOO_FEW_JOINTS:
					pa.stroke(128);
					pa.fill(0);
					PMethod.writeCenteredText(pa, "MINIMUM OF THREE", 400, 200, 18, pi.font18, 18);
					PMethod.writeCenteredText(pa, "LIMBS REQUIRED.", 400, 230, 18, pi.font18, 18);
					break;
				case ERROR_BROKEN_MODEL:
					PMethod.writeCenteredText(pa, "TWO OR MORE BOTS DETECTED.", 400, 200, 18, pi.font18, 18);
					PMethod.writeCenteredText(pa, "ONLY ONE BOT ALLOWED.", 400, 230, 18, pi.font18, 18);
					break;
			}
			errorMessageCounter--;
		}

	}

	public void resetErrorMessageCounter() {
		System.out.println("resetErrorMessageCounter() called.");
		errorMessageCounter = 60;
	}

	public ProcessingPlayConstructor(PApplet papplet, ProcessingInterface proc) {
		super(papplet, proc);
		synth = pi.getSynth();
		model = new Model();
		joints = new ArrayList<Joint>();
		springs = new ArrayList<Spring>();
		muscles = new ArrayList<Muscle>();

		/* Create joints */
		for (int i = 0; i < MAX_JOINTS; i++) {
			joints.add(new Joint(model,
					(MODEL_WINDOW_X + i * JOINT_SCALE) + JOINT_SCALE,
					MODEL_WINDOW_Y + JOINT_SCALE));
		}

		int buttonOffset = MODEL_WINDOW_X - 96;
		buttonJoint = new ProcessingButton(pa, pi, buttonOffset,
				MODEL_WINDOW_Y, "JOINT");
		buttonSpring = new ProcessingButton(pa, pi, buttonOffset,
				MODEL_WINDOW_Y + 40, "SPRING");
		buttonMuscle = new ProcessingButton(pa, pi, buttonOffset,
				MODEL_WINDOW_Y + 80, "MUSCLE");
		buttonTrash = new ProcessingButtonTrash(pa, pi, buttonOffset + 14,
				MODEL_WINDOW_Y + 200, "TRASH");
		buttonDone = new ProcessingButton(pa, pi, buttonOffset,
				MODEL_WINDOW_Y + MODEL_WINDOW_HEIGHT - ProcessingButton.HEIGHT,
				"FINISH");

		buttonJoint.setToggle(true);

		sliderAmp = new ProcessingSlider(pa, pi, SLIDER_X, SLIDER_Y,
				SLIDER_WIDTH, AMP_MINIMUM, AMP_MAXIMUM);
		sliderFrequency = new ProcessingSlider(pa, pi, SLIDER_X,
				SLIDER_Y + SLIDER_Y_OFFSET, SLIDER_WIDTH, FREQUENCY_MINIMUM,
				FREQUENCY_MAXIMUM);
		sliderPhase = new ProcessingSlider(pa, pi, SLIDER_X,
				SLIDER_Y + SLIDER_Y_OFFSET * 2, SLIDER_WIDTH, PHASE_MINIMUM,
				PHASE_MAXIMUM);
	}

	public void display() {
		handleMouseEvents();
		drawConstructorWindow();
		handleButtons();
		handleSliders();

		switch (edit_mode) {
			case EDIT_JOINT:
				editJointMode();
				break;

			case EDIT_SPRING:
				editSpringMode();
				break;

			case EDIT_MUSCLE:
				editMuscleMode();
				break;
		}

		drawSprings();
		drawMuscles();
		drawJoints();
		drawButtons();
		drawSliders();
		drawHeader();
		drawMuscleHeader();
		drawErrorMessage();
	}

	private void drawHeader() {
		pa.fill(255);
		pa.textFont(pi.font48, 48);
//		pa.line(200, 0, 200, 100);  /* For Centering */
//		pa.line(600, 0, 600, 100);
		pa.text("D E S I G N", 197, 56);
	}

	private void drawMuscleHeader() {
		pa.fill(255);
		pa.textFont(pi.font36, 36);
//		pa.line(200, 0, 200, 800);  /* For Centering */
//		pa.line(600, 0, 600, 800);
		pa.text("MUSCLE CONTROLS", 190, 476);
	}

	private void resetSelection() {
		selectedJoint = null;
		selectedSpring = null;
		selectedMuscle = null;
	}

	private void handleButtons() {
		if (buttonJoint.isMouseHovering() && mouseButtonStatus == MOUSE_CLICK) {
			buttonJoint.setToggle(true);
			buttonSpring.setToggle(false);
			buttonMuscle.setToggle(false);

			if (edit_mode != EDIT_JOINT) {
				resetSelection();
				synth.buttonMode();
			}

			edit_mode = EDIT_JOINT;
		} else if (buttonSpring.isMouseHovering() && mouseButtonStatus == MOUSE_CLICK) {
			buttonJoint.setToggle(false);
			buttonSpring.setToggle(true);
			buttonMuscle.setToggle(false);
			if (edit_mode != EDIT_SPRING) {
				resetSelection();
				synth.buttonMode();
			}

			edit_mode = EDIT_SPRING;
		} else if (buttonMuscle.isMouseHovering() && mouseButtonStatus == MOUSE_CLICK) {
			buttonJoint.setToggle(false);
			buttonSpring.setToggle(false);
			buttonMuscle.setToggle(true);
			if (edit_mode != EDIT_MUSCLE) {
				resetSelection();
				synth.buttonMode();
			}

			edit_mode = EDIT_MUSCLE;
		} else if (buttonTrash.isMouseHovering() && mouseButtonStatus == MOUSE_CLICK) {
			/* Trash selected item */
			if (selectedMuscle != null) {
				model.removeLimb(selectedMuscle);
				muscles.remove(selectedMuscle);
				selectedMuscle = null;
				synth.trash();
			} else if (selectedSpring != null) {
				model.removeLimb(selectedSpring);
				springs.remove(selectedSpring);
				selectedSpring = null;
				synth.trash();
			}
		} else if (buttonDone.isMouseHovering() && mouseButtonStatus == MOUSE_CLICK) {
			makeModel();
			synth.buttonModeFinish();
		}
	}

	private void handleSliders() {
		if (selectedMuscle != null) {
			sliderAmp.setActive(true);
			sliderFrequency.setActive(true);
			sliderPhase.setActive(true);

			sliderAmp.setValue(selectedMuscle.getAmplitude());
			sliderFrequency.setValue(selectedMuscle.getFrequency());
			sliderPhase.setValue(selectedMuscle.getOffset());

			sliderAmp.move(mouseButtonStatus);
			sliderFrequency.move(mouseButtonStatus);
			sliderPhase.move(mouseButtonStatus);

			selectedMuscle.setAmplitude(sliderAmp.getValue());
			selectedMuscle.setFrequency(sliderFrequency.getValue());
			selectedMuscle.setOffset(sliderPhase.getValue());
		} else {
			sliderAmp.setActive(false);
			sliderFrequency.setActive(false);
			sliderPhase.setActive(false);
		}
	}

	private void drawConstructorWindow() {
//		pa.background(0);
		pa.strokeWeight(1);
		pa.stroke(128);
		pa.noFill();
		pa.rect(MODEL_WINDOW_X, MODEL_WINDOW_Y, MODEL_WINDOW_WIDTH,
				MODEL_WINDOW_HEIGHT);
	}

	private void drawSprings() {
		for (Spring s : springs) {
			pa.stroke(128);
			pa.strokeWeight(1);
			pa.line((int) s.getJoint(0).getX(), (int) s.getJoint(0).getY(),
					(int) s.getJoint(1).getX(), (int) s.getJoint(1).getY());

			/* Selected spring. */
			if (s.equals(selectedSpring)) {
				pa.strokeWeight(4);

				pa.line((int) s.getJoint(0).getX(), (int) s.getJoint(0).getY(),
						(int) s.getJoint(1).getX(), (int) s.getJoint(1).getY());
			}

			/* Hover spring. */
			if (s.equals(hoverSpring)) {
				pa.stroke(0, 128, 0);
				pa.strokeWeight(4);

				pa.line((int) s.getJoint(0).getX(), (int) s.getJoint(0).getY(),
						(int) s.getJoint(1).getX(), (int) s.getJoint(1).getY());
			}
		}
	}

	private void drawMuscles() {
		for (Muscle m : muscles) {
			pa.stroke(128, 0, 0);
			pa.strokeWeight(1);
			pa.line((int) m.getJoint(0).getX(), (int) m.getJoint(0).getY(),
					(int) m.getJoint(1).getX(), (int) m.getJoint(1).getY());

			/* Selected spring. */
			if (m.equals(selectedMuscle)) {
				pa.strokeWeight(4);

				pa.line((int) m.getJoint(0).getX(), (int) m.getJoint(0).getY(),
						(int) m.getJoint(1).getX(), (int) m.getJoint(1).getY());
			}

			/* Hover Muscle. */
			if (m.equals(hoverMuscle)) {
				pa.stroke(0, 128, 0);
				pa.strokeWeight(4);

				pa.line((int) m.getJoint(0).getX(), (int) m.getJoint(0).getY(),
						(int) m.getJoint(1).getX(), (int) m.getJoint(1).getY());
			}

		}
	}

	private void drawJoints() {
		for (Joint j : joints) {
			pa.noFill();
			pa.stroke(128);
			pa.strokeWeight(1);
			pa.ellipse((int) j.getX(), (int) j.getY(),
					(int) JOINT_SCALE, (int) JOINT_SCALE);

			/* Selected joint. */
			if (j.equals(selectedJoint)) {
				pa.fill(0, 0, 128);
				pa.ellipse((int) j.getX(), (int) j.getY(),
						(int) JOINT_SCALE, (int) JOINT_SCALE);
			}

			/* Hover joint. */
			if (j.equals(hoverJoint)) {
				pa.fill(0, 128, 0);
				pa.stroke(0, 128, 0);
				pa.ellipse((int) j.getX(), (int) j.getY(),
						(int) JOINT_SCALE, (int) JOINT_SCALE);
			}
		}
	}

	private void drawButtons() {
		buttonJoint.display();
		buttonSpring.display();
		buttonMuscle.display();
		buttonTrash.display();
		buttonDone.display();
	}

	private void drawSliders() {
		sliderAmp.display();
		sliderFrequency.display();
		sliderPhase.display();

		/* Text */
		pa.fill(255);
		pa.textFont(pi.font18, 18);
		pa.text("Amplitude", 60, SLIDER_Y + SLIDER_Y_OFFSET / 4);
		pa.text("Frequency", 60, SLIDER_Y + SLIDER_Y_OFFSET + SLIDER_Y_OFFSET / 4);
		pa.text("Phase", 60, SLIDER_Y + SLIDER_Y_OFFSET * 2 + SLIDER_Y_OFFSET / 4);

		/* Values */
		if (selectedMuscle != null) {
			double round = 10000.0;

			Double d = (double) ((int) (selectedMuscle.getAmplitude() * round) /
					round);
			pa.text(d.toString(), SLIDER_X + SLIDER_WIDTH + 36,
					SLIDER_Y + SLIDER_Y_OFFSET / 4);

			d = (double) ((int) (selectedMuscle.getFrequency() * round) / round);
			pa.text(d.toString(), SLIDER_X + SLIDER_WIDTH + 36,
					SLIDER_Y + SLIDER_Y_OFFSET + SLIDER_Y_OFFSET / 4);

			d = (double) ((int) (selectedMuscle.getOffset() * round) / round);
			pa.text(d.toString(), SLIDER_X + SLIDER_WIDTH + 36,
					SLIDER_Y + SLIDER_Y_OFFSET * 2 + SLIDER_Y_OFFSET / 4);
		}
	}

	public void editJointMode() {
		hoverJoint = null;

		/* Move joint */
		if (mouseButtonStatus == MOUSE_CLICK) {
			/* Lock joint. */
			for (Joint j : joints) {
				if (isMouseHoveringJoint(j)) {
					jointLocked = j;
					selectedJoint = j;
					synth.clickJoint();
					break;
				}
			}
		} else if (mouseButtonStatus == MOUSE_HOLD) {
			/* Move locked joint, if applicable. */
			if (jointLocked != null) {
				moveJoint(jointLocked);
			}
		} else if (mouseButtonStatus == MOUSE_RELEASE) {
			/* Release locked joint. */
			jointLocked = null;
		} else {
			/* Hover joint */
			for (Joint j : joints) {
				if (isMouseHoveringJoint(j) && !j.equals(selectedJoint)) {
					hoverJoint = j;
					break;
				}
			}
		}
	}

	public void editSpringMode() {
		hoverJoint = null;
		hoverSpring = null;

		if (mouseButtonStatus == MOUSE_CLICK) {
			/* Lock spring to joint. */
			for (Joint j : joints) {
				if (isMouseHoveringJoint(j)) {
					jointLocked = j;
					synth.clickJointInSpringMode();
					break;
				}
			}

			/* Select spring. */
			if (jointLocked == null) {
				for (Spring s : springs) {
					if (isMouseHoveringLimb(s)) {
						selectedSpring = s;
						synth.selectSpring();
						break;
					}
				}
			}
		} else if (mouseButtonStatus == MOUSE_HOLD) {
			/* Drag spring. */
			if (jointLocked != null) {
				pa.stroke(128);
				pa.fill(128);
				pa.line((int) jointLocked.getX(), (int) jointLocked.getY(),
						pa.mouseX, pa.mouseY);
			}

			/* Hover joint */
			for (Joint j : joints) {
				if (isMouseHoveringJoint(j) &&
						!j.equals(selectedJoint) &&
						!j.equals(jointLocked) &&
						!doesLimbExistBetweenJoints(j, jointLocked)) {
					hoverJoint = j;
					break;
				}
			}

		} else if (mouseButtonStatus == MOUSE_RELEASE) {
			/* Connect spring. */
			if (jointLocked != null) {
				for (Joint j : joints) {
					if (isMouseHoveringJoint(j) && !j.equals(jointLocked)) {
						if (!doesLimbExistBetweenJoints(j, jointLocked)) {
							Spring s = new Spring(model, SPRING_K);
							s.attachJoint(j, jointLocked);
							springs.add(s);
							selectedSpring = s;
							synth.attachSpring();
							break;
						}
					}
				}
			}

			jointLocked = null;
		} else {
			/* Hover spring. */
			for (Spring s : springs) {
				if (isMouseHoveringLimb(s) && !s.equals(selectedSpring)) {
					hoverSpring = s;
					break;
				}
			}

			/* Hover joint */
			for (Joint j : joints) {
				if (isMouseHoveringJoint(j) && !j.equals(selectedJoint)) {
					hoverJoint = j;
					break;
				}
			}
		}
	}

	public void editMuscleMode() {
		hoverJoint = null;
		hoverMuscle = null;

		if (mouseButtonStatus == MOUSE_CLICK) {
			/* Lock spring to joint. */
			for (Joint j : joints) {
				if (isMouseHoveringJoint(j)) {
					jointLocked = j;
					synth.clickJointInSpringMode();
					break;
				}
			}

			/* Select muscle. */
			if (jointLocked == null) {
				for (Muscle m : muscles) {
					if (isMouseHoveringLimb(m)) {
						selectedMuscle = m;
						synth.selectSpring();
						break;
					}
				}
			}
		} else if (mouseButtonStatus == MOUSE_HOLD) {
			/* Drag muscle. */
			if (jointLocked != null) {
				pa.stroke(128, 0, 0);
				pa.fill(128);
				pa.line((int) jointLocked.getX(), (int) jointLocked.getY(),
						pa.mouseX, pa.mouseY);
			}

			/* Hover joint */
			for (Joint j : joints) {
				if (isMouseHoveringJoint(j) &&
						!j.equals(selectedJoint) &&
						!j.equals(jointLocked) &&
						!doesLimbExistBetweenJoints(j, jointLocked)) {
					hoverJoint = j;
					break;
				}
			}

		} else if (mouseButtonStatus == MOUSE_RELEASE) {
			/* Connect spring. */
			if (jointLocked != null) {
				for (Joint j : joints) {
					if (isMouseHoveringJoint(j) && !j.equals(jointLocked)) {
						if (!doesLimbExistBetweenJoints(j, jointLocked)) {
							Muscle m = new Muscle(model, SPRING_K,
									MUSCLE_FREQUENCY, MUSCLE_AMP, MUSCLE_PHASE);
							m.attachJoint(j, jointLocked);
							muscles.add(m);
							selectedMuscle = m;
							synth.attachSpring();
							break;
						}
					}
				}
			}

			jointLocked = null;
		} else {
			/* Hover muscle. */
			for (Muscle m : muscles) {
				if (isMouseHoveringLimb(m) && !m.equals(selectedMuscle)) {
					hoverMuscle = m;
					break;
				}
			}

			/* Hover joint */
			for (Joint j : joints) {
				if (isMouseHoveringJoint(j) && !j.equals(selectedJoint)) {
					hoverJoint = j;
					break;
				}
			}
		}
	}

	private boolean doesLimbExistBetweenJoints(Joint j0, Joint j1) {
		int index0 = joints.indexOf(j0);
		int index1 = joints.indexOf(j1);
		boolean checkSpring = false;
		boolean checkMuscle = false;
		/* Ensure j0 != j1 */
		if (j0.equals(j1)) {
			return false;
		}

		/* Check for existing spring. */
		for (Limb limb : springs) {
			if ((joints.indexOf(limb.getJoint(0)) == index0 &&
					joints.indexOf(limb.getJoint(1)) == index1) ||
					(joints.indexOf(limb.getJoint(0)) == index1 &&
					joints.indexOf(limb.getJoint(1)) == index0)) {
				checkSpring = true;
			}
		}

		for (Limb limb : muscles) {
			if ((joints.indexOf(limb.getJoint(0)) == index0 &&
					joints.indexOf(limb.getJoint(1)) == index1) ||
					(joints.indexOf(limb.getJoint(0)) == index1 &&
					joints.indexOf(limb.getJoint(1)) == index0)) {
				checkMuscle = true;
			}
		}

		return (checkSpring || checkMuscle);
	}

	private boolean isMouseHoveringJoint(Joint j) {
		int js = (int) (JOINT_SCALE / 2.0);

		if (pa.mouseX > j.getX() - js &&
				pa.mouseX < j.getX() + js &&
				pa.mouseY > j.getY() - js &&
				pa.mouseY < j.getY() + js) {
			return true;
		}

		return false;
	}

	private boolean isMouseHoveringLimb(Limb limb) {
		double x0 = limb.getJoint(0).getX();
		double y0 = limb.getJoint(0).getY();
		double x1 = limb.getJoint(1).getX();
		double y1 = limb.getJoint(1).getY();
		double y;
		double x;
		double angleNatural = getAngle(x0, y0, x1, y1);
		double angleMouse = getAngle(x0, y0, (double) pa.mouseX, (double) pa.mouseY);
		double distanceNatural = getDistance(x0, y0, x1, y1);
		double distanceMouse = getDistance(x0, y0, (double) pa.mouseX, (double) pa.mouseY);
		double foo = 3;

		if ((angleMouse > angleNatural - foo) && (angleMouse < angleNatural + foo)) {
			if (distanceMouse < distanceNatural) {
				return true;
			}
		}

		return false;
	}

	private double getDistance(double x0, double y0, double x1, double y1) {
		return Math.sqrt(Math.pow(y1 - y0, 2) + Math.pow(x1 - x0, 2));
	}

	private double roundFloat(double v, double r) {
		v = v * r;
		int v2 = (int) v;

		return (double) v2 / r;
	}

	private double getAngle(double x0, double y0, double x1, double y1) {
		return Math.atan2(y1 - y0, x1 - x0) * 180.0 / Math.PI;
	}

	private void moveJoint(Joint j) {
		int x = pa.mouseX;
		int y = pa.mouseY;
		int r = (int) (JOINT_SCALE / 2);

		/* Constrain to model constructor window */
		if (x < (MODEL_WINDOW_X + r)) {
			x = MODEL_WINDOW_X + r;
		}

		if (x > (MODEL_WINDOW_X + MODEL_WINDOW_WIDTH - r)) {
			x = MODEL_WINDOW_X + MODEL_WINDOW_WIDTH - r;
		}

		if (y < (MODEL_WINDOW_Y + r)) {
			y = MODEL_WINDOW_Y + r;
		}

		if (y > (MODEL_WINDOW_Y + MODEL_WINDOW_HEIGHT - r)) {
			y = MODEL_WINDOW_Y + MODEL_WINDOW_HEIGHT - r;
		}

		j.setX(x);
		j.setY(y);
	}

	private void handleMouseEvents() {
		if (!isMouseDown && pa.mousePressed) { /* Initial Click */
			mouseButtonStatus = MOUSE_CLICK;
			isMouseDown = true;
		} else if (isMouseDown && pa.mousePressed) {  /* Holding down mouse button. */
			mouseButtonStatus = MOUSE_HOLD;
		} else if (isMouseDown && !pa.mousePressed) { /* Mouse released */
			mouseButtonStatus = MOUSE_RELEASE;
			isMouseDown = false;
		} else {
			mouseButtonStatus = MOUSE_OFF;
		}
	}

	private void makeModel() {
		try {
			Model tempModel = new Model();

			/* Add joints to model */
			for (Joint j : joints) {
				int x = (int) j.getX();
				int y = (int) j.getY();
				tempModel.spawnJoint(x, y, false);
			}

			/* Add springs to model */
			for (Spring s : springs) {
				tempModel.spawnSpring(joints.indexOf(s.getJoint(0)),
						joints.indexOf(s.getJoint(1)),
						SPRING_K);
			}

			/* Add muscles to model */
			for (Muscle m : muscles) {
				tempModel.spawnMuscle(joints.indexOf(m.getJoint(0)),
						joints.indexOf(m.getJoint(1)),
						3,
						m.getFrequency(),
						m.getAmplitude(),
						m.getOffset() / 360);
			}


			/* Remove unattached joints. */
			tempModel = PMethod.removeUnattachedJoints(tempModel);

			/* Check stuff */
			int nLimbs = tempModel.getLimbs().size();

			/* Validate model */
			if (nLimbs < 2) {
				System.out.println("Too few limbs");
				resetErrorMessageCounter();
				errorMessageDisplay = ERROR_TOO_FEW_JOINTS;
			} else if (!PMethod.validateModel(tempModel)) {
				resetErrorMessageCounter();
				errorMessageDisplay = ERROR_BROKEN_MODEL;
			} else {

				/* Scale model to native model size, from pixels to meters. */
				tempModel = modelScreenToMeters(tempModel);

				/* Invert model (screen is upside in relation to model's native format. */
				tempModel = PMethod.invertModel(tempModel);

				/* Scale Model if to big. */
				tempModel = PMethod.constrictModelSize(tempModel);

				/* Center model. */
				tempModel = PMethod.centerModel(tempModel);

				/* Place model on ground */
				tempModel = PMethod.groundModel(tempModel);

				/* Next mode */
				pi.setMode(new ProcessingEnterName(pa, pi, tempModel.clone()));

			}


		} catch (Exception e) {
		}

	}

	public Model modelScreenToMeters(Model m) {
		model = m.clone();

		for (Joint j : model.getJoints()) {
			j.setX(j.getX() / Config.PIXELS_PER_METER);
			j.setY(j.getY() / Config.PIXELS_PER_METER);
		}

		return model.clone();
	}
}

















