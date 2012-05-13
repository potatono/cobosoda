package com.jplt.cobosoda;

import java.util.ArrayList;
import processing.core.*;
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Jacob Joaquin
 */
public class PMethod {
	public static final double AMP_MINIMUM = 0.01;
	public static final double AMP_MAXIMUM = 0.5;
	public static final double FREQUENCY_MINIMUM = 0.01;
	public static final double FREQUENCY_MAXIMUM = 4.0;
	public static final double PHASE_MINIMUM = 0.0;
	public static final double PHASE_MAXIMUM = 360.0;
	public static final double SPRING_K = 3.0;
	public static final int RACE_TIME = 60;
	public static final double MAX_MODEL_HEIGHT = 2;
	public static final double MAX_MODEL_WIDTH = 3;
	
	private PMethod() {
		throw new AssertionError("\"You can't touch this.\" -MC Hammer");		
	}
	
	public static double calculateCenter(Model m) {
		double left = findLeftJoint(m).getX();
		double right = findRightJoint(m).getX();

		return (right - left) / 2.0 + left;
	}
	
	private static Joint findLeftJoint(Model m) {
		Joint left = m.getJoint(0);
		
		for (Joint j : m.getJoints()) {
			if (j.getX() < left.getX()) {
				left = j;
			}
		}
		
		return left;
	}
	
	private static Joint findRightJoint(Model m) {
		Joint right = m.getJoint(0);
		
		for (Joint j : m.getJoints()) {
			if (j.getX() > right.getX()) {
				right = j;
			}
		}
		
		return right;
	}	
	
	
	public static void drawModel(Model model, double offset, int ground, PApplet pa) {
		drawTriangles(model, offset, ground, pa, 1);

		for (Limb s : model.getLimbs()) {
			drawLimb(s, offset, ground, pa, 1);
		}

		for (Joint joint : model.getJoints()) {
			drawJoint(joint, offset, ground, pa, 1);
		}		
	}
	
	public static void drawModel(Model model, double offset, int ground, PApplet pa, double ppm) {
		drawTriangles(model, offset, ground, pa, ppm);

		for (Limb s : model.getLimbs()) {
			drawLimb(s, offset, ground, pa, ppm);
		}

		for (Joint joint : model.getJoints()) {
			drawJoint(joint, offset, ground, pa, ppm);
		}		
	}
	
	public static void drawTriangles(Model model, double offset, int ground, PApplet pa, double scale) {
		int nJoints = model.getNumberOfJoints();
		double ppm = Config.PIXELS_PER_METER * scale;
		offset *= scale;
		offset -= Config.SIZE_WORLD_X / 2.0;
		int windowX = Config.WINDOW_X / 2;
		int yOffset = (int) (Config.WINDOW_Y - ground + Config.JOINT_RADIUS * scale);
		
		pa.fill(255, 0, 0, 24);
		pa.noStroke();
		
		for (int i = 0; i < nJoints - 2; i++) {
			Joint ji = model.getJoint(i);

			for (int j = i + 1; j < nJoints - 1; j++) {
				Joint jj = model.getJoint(j);

				for (int k = j + 1; k < nJoints; k++) {
					Joint jk = model.getJoint(k);

					if (ji.isCommonLimb(jj) && ji.isCommonLimb(jk) &&
							jj.isCommonLimb(jk)) {
						int x0 = (int) ((ji.getX() + offset) * ppm) + windowX;
						int x1 = (int) ((jj.getX() + offset) * ppm) + windowX;
						int x2 = (int) ((jk.getX() + offset) * ppm) + windowX;
						int y0 = Config.WINDOW_Y - (int) (ji.getY() * ppm) - yOffset;
						int y1 = Config.WINDOW_Y - (int) (jj.getY() * ppm) - yOffset;
						int y2 = Config.WINDOW_Y - (int) (jk.getY() * ppm) - yOffset;

						pa.triangle(x0, y0, x1, y1, x2, y2);
					}
				}
			}
		}
	}


	public static void drawLimb(Limb s, double offset, int ground, PApplet pa, double scale) {
		double ppm = Config.PIXELS_PER_METER * scale;
		offset *= scale;
		
		int x1 = (int) ((s.getJoint(0).getX() + offset - Config.SIZE_WORLD_X / 2.0) * ppm) + Config.WINDOW_X / 2;
		int x2 = (int) ((s.getJoint(1).getX() + offset - Config.SIZE_WORLD_X / 2.0) * ppm) + Config.WINDOW_X / 2;

//		int x1 = (int) ((s.getJoint(0).getX() + offset) * ppm);
//		int x2 = (int) ((s.getJoint(1).getX() + offset) * ppm);
		int y1 = Config.WINDOW_Y - (int) (s.getJoint(0).getY() *
				ppm) - (int) (Config.WINDOW_Y - ground + Config.JOINT_RADIUS * scale);
		int y2 = Config.WINDOW_Y - (int) (s.getJoint(1).getY() *
				ppm) - (int) (Config.WINDOW_Y - ground + Config.JOINT_RADIUS * scale);

		
		pa.strokeWeight(1);
		setLimbColor(s, pa);
		pa.line(x1, y1, x2, y2);
	}

	public static void drawJoint(Joint j, double offset, int ground, PApplet pa, double scale) {
		double ppm = Config.PIXELS_PER_METER * scale;
		offset *= scale;
		
		int x = (int) ((j.getX() + offset - Config.SIZE_WORLD_X / 2.0) * ppm) + Config.WINDOW_X / 2;
		int y = Config.WINDOW_Y - (int) (j.getY() * ppm) -
				(int) (Config.WINDOW_Y - ground + Config.JOINT_RADIUS * scale);

		if (j.getHovering()) {
			pa.noFill();
			pa.stroke(255, 0, 255, 128);
			pa.ellipse(x, y, (int) (Config.JOINT_DIAMETER * scale), (int) (Config.JOINT_DIAMETER * scale));
		}

		if (j.getDragging()) {
			pa.stroke(255, 0, 255, 64);
			pa.fill(0, 255, 0, 64);
			pa.ellipse(x, y, (int) (Config.JOINT_DIAMETER * scale), (int) (Config.JOINT_DIAMETER * scale));
		} else {
			pa.stroke(255, 0, 255, 64);
			pa.fill(0, 0, 255, 64);
			pa.ellipse(x, y, (int) (Config.JOINT_DIAMETER * scale), (int) (Config.JOINT_DIAMETER * scale));
		}
	}	
	
	private static void setLimbColor(Limb s, PApplet pa) {
		if (s instanceof Muscle) {
			int strength = (int) ((float) ((Muscle) s).getValue() * 255.0f);

			if (strength < 0) {
				pa.stroke(64, 64, Math.abs(strength), 255);
			} else {
				pa.stroke(strength, 64, 64, 255);
			}

		} else {
			pa.stroke(255, 128);
		}
	}
	
	
	
	public static void sanitizeModel(Model m) {
		ArrayList<Joint> toToss = getUnattachedJoints(m);
		removeJoints(m, toToss);
	}
	
	public static Model removeUnattachedJoints(Model m) {
		Model model = m.clone();
		
		ArrayList<Joint> toToss = getUnattachedJoints(model);
		removeJoints(model, toToss);
		
		return model.clone();
	}
	
	public static ArrayList<Joint> getUnattachedJoints(Model m) {
		ArrayList<Joint> unattachedJoints = new ArrayList<Joint>();
	
		for (Joint j : m.getJoints()) {
			if (j.getLimbCount() < 1) {
				System.out.println("Unattached Joint found.");
				unattachedJoints.add(j);
			}
		}
		
		return unattachedJoints;
	}
	
	public static void removeJoints(Model m, ArrayList<Joint> joints) {
		for (Joint j : joints) {
			System.out.println("Joint removed");
			m.removeJoint(j);
		}
	}
	
	public static boolean testForBrokenLimb(Model m) {
		for (Limb l : m.getLimbs()) {
			if (l.getJoint(0) == null || l.getJoint(1) == null) {
				return true;
			}
		}
		
		return false;
	}
	
	public static boolean testForBrokenJoints(Model m) {
		for (Joint j : m.getJoints()) {
			if (j.getLimbCount() <= 0) {
				return true;
			}
		}
		
		return false;
	}
	
	public static double getRandomAmp() {
		return (Math.random() * (AMP_MAXIMUM - AMP_MINIMUM)) + AMP_MINIMUM;
	}
	
	public static double getRandomFrequency() {
		return (Math.random() * (FREQUENCY_MAXIMUM - FREQUENCY_MINIMUM)) +
				FREQUENCY_MINIMUM;
	}
	
	public static double getRandomPhase() {
		return (Math.random() * (PHASE_MAXIMUM - PHASE_MINIMUM)) + PHASE_MINIMUM;
	}
	
	public static Model centerModel(Model m) {
		Model model = m.clone();
		
		Joint jointLeft = m.getJoint(0);
		Joint jointRight = m.getJoint(0);
		
			/* Center Model*/
			for (Joint j : model.getJoints()) {
				if (j.getX() < jointLeft.getX()) {
					jointLeft = j;
				}
				
				if (j.getX() > jointRight.getX()) {
					jointRight = j;
				}
			}
		
			double width = jointRight.getX() - jointLeft.getX();
			double center = jointLeft.getX() + (width / 2.0);
			
			double difference = (Config.SIZE_WORLD_X / 2.0) - center;
			
			for (Joint j : model.getJoints()) {
				j.setX(j.getX() + difference);
			}
					
		return model.clone();
	}

	public static Model groundModel(Model m) {
		Model model = m.clone();
		
		Joint jointBottom = model.getJoint(0);
		
			for (Joint j : model.getJoints()) {
				if (j.getY() < jointBottom.getY()) {
					jointBottom = j;
				}
			}
		
			double difference = jointBottom.getY();
			
			for (Joint j : model.getJoints()) {
				j.setY(j.getY() - difference);
			}
			
		return model.clone();
	}
	
	public static Model invertModel(Model m) {
		Model model = m.clone();
		
		Joint jointTop = model.getJoint(0);
		Joint jointBottom = model.getJoint(0);
		
		for (Joint j : model.getJoints()) {
			if (jointTop.getY() < j.getY() ) {
				jointTop = j;
			}
			
			if (jointBottom.getY() > j.getY() ) {
				jointBottom = j;
			}
		}
		
		double difference = jointBottom.getY() - jointTop.getY();
		
		for (Joint j : model.getJoints()) {
			j.setY(difference - j.getY());
		}
		
		return model.clone();
	}
	
	public static Model constrictModelSize(Model m) {
		Model model = m.clone();
		double height = getModelHeight(model);
		double width = getModelWidth(model);
		
		if (height > MAX_MODEL_HEIGHT || width > MAX_MODEL_WIDTH) {
			double scaleX = (double) MAX_MODEL_WIDTH / width;
			double scaleY = (double) MAX_MODEL_HEIGHT / height;
			
			double scale = (scaleX < scaleY ? scaleX : scaleY);
			
			for (Joint j : model.getJoints()) {
				j.setX(j.getX() * scale);
				j.setY(j.getY() * scale);
			}
		}
		
		return model.clone();
	}

	public static Model constrictModelSize(Model m, double w, double h) {
		Model model = m.clone();
		double height = getModelHeight(model);
		double width = getModelWidth(model);
		
		if (height > h || width > w) {
			double scaleX = (double) w / width;
			double scaleY = (double) h / height;
			
			double scale = (scaleX < scaleY ? scaleX : scaleY);
			
			for (Joint j : model.getJoints()) {
				j.setX(j.getX() * scale);
				j.setY(j.getY() * scale);
			}
		}
		
		return model.clone();
	}
	
	public static double getModelHeight(Model m) {
		Model model = m;
		Joint jointBottom = model.getJoint(0);
		Joint jointTop = model.getJoint(0);
		
			for (Joint j : model.getJoints()) {
				if (j.getY() < jointBottom.getY()) {
					jointBottom = j;
				}
				
				if (j.getY() > jointTop.getY()) {
					jointTop = j;
				}
			}
		
		return jointTop.getY() - jointBottom.getY();
	}
	
	public static double getModelWidth(Model m) {
		Model model = m.clone();
		Joint jointLeft = m.getJoint(0);
		Joint jointRight = m.getJoint(0);
		
			for (Joint j : model.getJoints()) {
				if (j.getX() < jointLeft.getX()) {
					jointLeft = j;
				}
				
				if (j.getX() > jointRight.getX()) {
					jointRight = j;
				}
			}
			
		return  jointRight.getX() - jointLeft.getX();
	}
	
	
	public static void writeText(PApplet pa, String s, int x, int y, int offset, PFont f, int size) {
		pa.fill(255);
		pa.textFont(f, size);
		
		for (int i = 0; i < s.length(); i++) {
			pa.text(s.substring(i, i + 1), x + i * offset, y);
		}
	}
	
	public static void writeCenteredText(PApplet pa, String s, int x, int y, int offset, PFont f, int size) {
		pa.fill(255);
		pa.textFont(f, size);
		
		int width = s.length() * offset;
//		int x = (Config.WINDOW_X - width) / 2;
		x = x - (width / 2);
				
		for (int i = 0; i < s.length(); i++) {
			pa.text(s.substring(i, i + 1), x + i * offset, y);
		}
	}
	
	
	public static boolean validateModel(Model m) {
		ArrayList<Joint> network = new ArrayList<Joint>();
		
		getJointNetwork(m.getJoint(0), network);
		
//		for (Joint j : network) {
//			System.out.println(j.getIndex());
//		}

		for (Joint j : m.getJoints()) {
			if (!network.contains(j)) {
				return false;
			}
		}
		
		return true;
	}
	
	public static void getJointNetwork (Joint j, ArrayList<Joint> network) {
		
		for (Limb l: j.getLimbs()) {
			Joint opposite = j.findOppositeJoint(l);
			
			if (!network.contains(opposite)) {
				network.add(opposite);
				getJointNetwork(opposite, network);
			}
		}
	}
			
	public static String substituteDelimiter(String s) {
		StringBuffer temp = new StringBuffer(s.length());
		
		for (int i = 0; i < s.length(); i++) {
			if (s.substring(i, i + 1).equals("x")) {
				temp.append(" ");
			} else {
				temp.append(s.substring(i, i + 1));
			}
		}
		
		String foo = temp.toString();
		
		while (foo.endsWith(" ")) {
			foo = foo.substring(0, foo.length() - 1);
		}
		
		return foo;
	}
}
