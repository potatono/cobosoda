/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jplt.cobosoda;

import java.util.ArrayList;

/**
 *
 * @author Jacob Joaquin
 */
public class ProcessingMutate {
	/*
	 * move joint
	 * shift muscle frequency
	 * shift muscle frequency
	 * shift muscle frequency
	 * add joint
	 * remove joint
	 * splice spring
	 * convert muscle to spring
	 * convert spring to muscle
	 * 
	 * Changes exponential?
	 */

	private static final double MAX_JOINT_MOVE = 0.5;
	private static final double MAX_AMP_CHANGE = 0.5; /* absolute */

	private static final double MAX_PHASE_CHANGE = 180.0; /* degrees */

	private static final double MAX_FREQUENCY_CHANGE = 2.0;
//	private static final int MIN_N_JOINTS = 3; /* ProcessingConstructor */
//	private static final int MAX_N_JOINTS = 16;
	private static final double MOVE_JOINT = 100;
	private static final double SHIFT_AMP = MOVE_JOINT + 100;
	private static final double SHIFT_PHASE = SHIFT_AMP + 100;
	private static final double SHIFT_FREQUENCY = SHIFT_PHASE + 100;
	private static final double ADD_JOINT = SHIFT_FREQUENCY;
	private static final double REMOVE_JOINT = ADD_JOINT + 0;
	private static final double SPLICE_SPRING = REMOVE_JOINT + 0;
	private static final double CONVERT_TO_MUSCLE = SPLICE_SPRING + 50;
	private static final double CONVERT_TO_SPRING = CONVERT_TO_MUSCLE + 50;
	private static final double TOTAL = CONVERT_TO_SPRING;
	private double odds = 1.0; /* on mutate, divide by 0.5 */

	private Model model;

	public ProcessingMutate(Model m) {
		model = m.clone();
//		mutate();
	}

	public Model evolve() {
		mutate();
		Model modelOriginal = model;
		model = PMethod.constrictModelSize(model);

		if (!PMethod.validateModel(model)) {
			return modelOriginal;
		}
		return model.clone(); /* Clone to recalculate limb lengths */
	}

	private void mutate() {
		double r = Math.random() * TOTAL;

		if (r >= 0 && r < MOVE_JOINT) {
			moveJoint();
		} else if (r >= MOVE_JOINT && r < SHIFT_AMP) {
			shiftAmp();
		} else if (r >= SHIFT_AMP && r < SHIFT_PHASE) {
			shiftPhase();
		} else if (r >= SHIFT_PHASE && r < SHIFT_FREQUENCY) {
			shiftFrequency();
		} else if (r >= SHIFT_FREQUENCY && r < ADD_JOINT) {
			addJoint();
		} else if (r >= ADD_JOINT && r < REMOVE_JOINT) {
		} else if (r >= REMOVE_JOINT && r < SPLICE_SPRING) {
		} else if (r >= SPLICE_SPRING && r < CONVERT_TO_MUSCLE) {
			convertToMuscle();
		} else if (r >= CONVERT_TO_MUSCLE && r < CONVERT_TO_SPRING) {
			convertToSpring();
		}

		odds = odds / 2.0;

		if (Math.random() < odds) {
			mutate();
		}
	}

	private void moveJoint() {
		int move = (int) (Math.random() * (double) model.getNumberOfJoints());

		double x = model.getJoint(move).getX();
		double y = model.getJoint(move).getY();

		double r = Math.random();
		double dx = Math.sin(r * 2 * Math.PI);
		double dy = Math.cos(r * 2 * Math.PI);

		r = Math.random() * MAX_JOINT_MOVE; /* Amplitude */
		model.getJoint(move).setX(x + r * dx);
		model.getJoint(move).setY(y + r * dy);
	}

	private void shiftAmp() {
		ArrayList<Muscle> muscles = model.getMuscles();

		if (muscles.isEmpty()) {
			return;
		}

		int c = (int) (Math.random() * (double) muscles.size());
		double r = (Math.random() * MAX_AMP_CHANGE) - MAX_AMP_CHANGE / 2.0;
		double amp = muscles.get(c).getAmplitude();
		amp = amp + r;

		if (amp < ProcessingPlayConstructor.AMP_MINIMUM) {
			amp = ProcessingPlayConstructor.AMP_MINIMUM;
		} else if (amp > ProcessingPlayConstructor.AMP_MAXIMUM) {
			amp = ProcessingPlayConstructor.AMP_MAXIMUM;
		}

		muscles.get(c).setAmplitude(amp);
	}

	private void shiftPhase() {
		ArrayList<Muscle> muscles = model.getMuscles();

		if (muscles.isEmpty()) {
			return;
		}

		int c = (int) (Math.random() * (double) muscles.size());  /* Muscle to shift */
		double r = (Math.random() * MAX_PHASE_CHANGE) - MAX_PHASE_CHANGE / 2.0;
		double phase = muscles.get(c).getOffset();
		phase = phase + r;

		if (phase < 0.0) {
			phase += 360.0;
		} else if (phase > 360.0) {
			phase -= 360;
		}

		muscles.get(c).setOffset(phase);
	}

	private void shiftFrequency() {
		ArrayList<Muscle> muscles = model.getMuscles();

		if (muscles.isEmpty()) {
			return;
		}

		int c = (int) (Math.random() * (double) muscles.size());  /* Muscle to shift */
		double r = (Math.random() * MAX_FREQUENCY_CHANGE) - MAX_FREQUENCY_CHANGE / 2.0;
		double frequency = muscles.get(c).getFrequency();
		frequency = frequency + r;

		if (frequency < ProcessingPlayConstructor.FREQUENCY_MINIMUM) {
			frequency = ProcessingPlayConstructor.FREQUENCY_MINIMUM;
		} else if (frequency > ProcessingPlayConstructor.FREQUENCY_MAXIMUM) {
			frequency = ProcessingPlayConstructor.FREQUENCY_MAXIMUM;
		}

		muscles.get(c).setFrequency(frequency);
	}

	private void convertToMuscle() {
		ArrayList<Spring> springs = model.getSprings();

		if (springs.isEmpty()) {
			return;
		}

		int c = (int) (Math.random() * (double) springs.size()); /* Spring index to convert */
		Spring s = model.getSprings().get(c);

		if (s != null) {
			try {
				int index0 = s.getJoint(0).getIndex();
				int index1 = s.getJoint(1).getIndex();

				model.removeLimb(s);
				model.spawnMuscle(index0, index1, PMethod.SPRING_K,
						PMethod.getRandomFrequency(), PMethod.getRandomAmp(),
						PMethod.getRandomPhase());
			} catch (Exception e) {
			}
		}
	}

	private void convertToSpring() {
		ArrayList<Muscle> muscles = model.getMuscles();

		if (muscles.isEmpty()) {
			return;
		}

		int c = (int) (Math.random() * (double) muscles.size()); /* Muscle index to convert */
		Muscle m = model.getMuscles().get(c);

		if (m != null) {
			try {
				int index0 = m.getJoint(0).getIndex();
				int index1 = m.getJoint(1).getIndex();

				model.removeLimb(m);
				model.spawnSpring(index0, index1, PMethod.SPRING_K);
			} catch (Exception e) {
			}
		}
	}

	private void addJoint() {
		System.out.println("addJoint() called.");

		Model m = model.clone();

		int r = (int) (Math.random() * (double) m.getNumberOfJoints());
		Joint toConnect = m.getJoint(r);

		double offset = 0.5;
		double x = toConnect.getX() + (Math.random() * offset - offset * 0.5);
		double y = toConnect.getY() + (Math.random() * offset - offset * 0.5);

		Joint j = new Joint(m, x, y);
		
//		int nLimbsToConnect = (int) (Math.random() * 3.0) + 1;
		
		j = m.spawnJoint(j);

		if (Math.random() < 0.5) {
			/* Spawn spring */
			try {
				m.spawnSpring(j, toConnect, 3.0);
			} catch (Exception e) {
			}
		} else {
			/* Spawn muscle */
			try {
				m.spawnMuscle(j, toConnect, 3.0, getRandomFreq(),
						getRandomAmp(), getRandomPhase());
			} catch (Exception e) {
			}
		}

		if (PMethod.validateModel(m) && m.getNumberOfJoints() <= 12) {
			model = m.clone();
		}
	}

	private double getRandomAmp() {
		return Math.random() * (PMethod.AMP_MAXIMUM - PMethod.AMP_MINIMUM) +
				PMethod.AMP_MINIMUM;
	}

	private double getRandomFreq() {
		return Math.random() * (PMethod.FREQUENCY_MAXIMUM - PMethod.FREQUENCY_MINIMUM) +
				PMethod.FREQUENCY_MINIMUM;
	}

	private double getRandomPhase() {
		return Math.random() * (PMethod.PHASE_MAXIMUM - PMethod.PHASE_MINIMUM) +
				PMethod.PHASE_MINIMUM;
	}
}








