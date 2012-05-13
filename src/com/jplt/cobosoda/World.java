/* 
Cobosoda
Copyright 2007 Justin Day 

This file is part of Cobosoda.

Cobosoda is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

Cobosoda is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Cobosoda.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.jplt.cobosoda;
import java.io.FileReader;

public class World {

	public static final String VERSION = "$Revision: 1.10 $";
	public static final String ID = "$Id: World.java,v 1.10 2009/05/31 17:46:48 potatono Exp $";
	private static Planet planet = new Planet();
	private static double mouseX = 0;
	private static double mouseY = 0;
	private static Joint jointHovering = null;
	private static Joint jointDragging = null;
	private static int mode = Config.MODE_BOOT_OFF;
	private static int lastMode = mode;
	private static boolean modeChanged = false;
	private static int evolutionGeneration = 0;
	
	private World() {
	}

	public static Planet getPlanet() {
		if (planet == null) {
			planet = new Planet();
		}
		return planet;
	}

	public static void setWidth(double x) {
		planet.setWidth(x);
	}

	public static void setHeight(double y) {
		planet.setHeight(y);
	}

	public static void setWidth(int x) {
		planet.setWidth(x);
	}

	public static void setHeight(int y) {
		planet.setHeight(y);
	}

	public static double getWidth() {
		return planet.getWidth();
	}

	public static double getHeight() {
		return planet.getHeight();
	}

	public static void setGravity(double g) {
		planet.setGravity(g);
	}

	public static double getGravity() {
		return planet.getGravity();
	}

	public static void setSpringElasticity(double e) {
		planet.setSpringElasticity(e);
	}

	public static double getSpringElasticity() {
		return planet.getSpringElasticity();
	}

	public static void setFriction(double f) {
		planet.setFriction(f);
	}

	public static double getFriction() {
		return planet.getFriction();
	}

	public static void setModel(Model m) {
		planet.setModel(m);
	}

	public static Model getModel() {
		return planet.getModel();
	}

	public static void setMass(double m) {
		planet.setMass(m);
	}

	public static double getMass() {
		return planet.getMass();
	}

	public static long getFrameNumber() {
		return planet.getFrameNumber();
	}

	public static long getTimeElapsed() {
		return planet.getTimeElapsed();
	}

	public static void setFitnessTest(IFitnessTest fitnessTest) {
		planet.setFitnessTest(fitnessTest);
	}

	public static IFitnessTest getFitnessTest() {
		return planet.getFitnessTest();
	}

	public static int getEvolutionGeneration() {
		return evolutionGeneration;
	}
	
	public static void setEvolutionGeneration(int generation) {
		evolutionGeneration = generation;
	}
	
	public static Model initDemo() {
		Model model = planet.getModel();
		planet.setGravity(Config.GRAVITY);
		planet.setSpringElasticity(Config.ELASTICITY);
		planet.setMass(Config.JOINT_MASS);

//    	model.spawnJoint(3.0, 5,false);
//    	model.spawnJoint(1.0, 3,false);
//    	model.spawnJoint(2.0, 3,false);
//    	model.spawnJoint(3.0, 3,false);
//    	model.spawnJoint(4.0, 3,false);
//    	model.spawnJoint(5.0, 3,false);
//		
//    	model.spawnSpring(0, 1, 3);
//    	model.spawnSpring(0, 2, 3);
//    	model.spawnSpring(0, 3, 3);
//    	model.spawnSpring(0, 4, 3);
//    	model.spawnSpring(0, 5, 3);

/*
		model.spawnJoint(3.0, 1.8, false);  // 0
		model.spawnJoint(2.25, 1.55, false); // 1
		model.spawnJoint(2.75, 1.55, false); // 2
		model.spawnJoint(3.25, 1.55, false); // 3
		model.spawnJoint(3.75, 1.55, false); // 4
		model.spawnJoint(3.0, 1.3, false); // 5
		model.spawnJoint(2.5, 1.0, false); // 6
		model.spawnJoint(2.5, 1.0, false); // x - 7
		model.spawnJoint(3.5, 1.0, false); // 7 - 8
		model.spawnJoint(3.5, 1.0, false); // x - 9

		model.spawnSpring(0, 1, 3);
		model.spawnSpring(0, 4, 3);
		model.spawnSpring(0, 5, 3);
		model.spawnSpring(1, 5, 3);
		model.spawnSpring(4, 5, 3);
		model.spawnSpring(1, 2, 3);
		model.spawnSpring(2, 3, 3);
		model.spawnSpring(3, 4, 3);
		model.spawnSpring(3, 1, 3);
		model.spawnSpring(3, 0, 3);
		model.spawnSpring(2, 4, 3);
		model.spawnSpring(2, 0, 3);
		model.spawnSpring(2, 5, 3);
		model.spawnSpring(3, 5, 3);

		model.spawnMuscle(6, 1, 3, 0.5, 0.10, 0);
		model.spawnMuscle(6, 2, 3, 0.5, 0.10, 1.57);
		model.spawnMuscle(7, 1, 3, 0.5, 0.10, 3.14);
		model.spawnMuscle(7, 2, 3, 0.5, 0.10, 2.36);
		model.spawnMuscle(8, 3, 3, 0.5, 0.10, 0.79);
		model.spawnMuscle(8, 4, 3, 0.5, 0.10, 2.35);
		model.spawnMuscle(9, 3, 3, 0.5, 0.10, 3.92);
		model.spawnMuscle(9, 4, 3, 0.5, 0.10, 3.13);

		model.setName("Dainty Walker");
*/





//		model.setName("Debugger");
//		model.spawnJoint(3.0, 3.0, false);
//		model.spawnJoint(3.0, 4.0, false);
//		model.spawnJoint(4.0, 4.0, false);
//		model.spawnJoint(4.0, 3.0, false);
//		model.spawnSpring(0, 1, 3);
//		model.spawnSpring(1, 2, 3);
//		model.spawnSpring(2, 3, 3);
//		model.spawnSpring(3, 0, 3);
//		model.spawnMuscle(0, 2, 3, 1, 0.25, 0);
//		model.spawnMuscle(1, 3, 3, 1, 0.25, 0.5);



//		model.setName("Debugger");
//		model.spawnJoint(3.0, 3.0, false);
//		model.spawnJoint(3.0, 4.0, false);
//		model.spawnJoint(4.0, 4.0, false);
//		model.spawnJoint(4.0, 3.0, false);
//		model.spawnSpring(0, 1, 3);
//		model.spawnSpring(1, 2, 3);
//		model.spawnSpring(2, 3, 3);
//		model.spawnSpring(3, 0, 3);
//		model.spawnMuscle(0, 2, 3, 1, 0.25, 0);


//		model.spawnJoint(3.0, 2.0, false);
//		model.spawnJoint(4.0, 3.0, false);
//		model.spawnJoint(5.0, 2.0, false);
//		model.spawnSpring(model.getJoint(0), model.getJoint(1), 3);
//		model.spawnMuscle(2, 0, 3.0, 0.5, 0.2, 0.0);
//		model.spawnMuscle(1, 2, 3.0, 0.5, 0.2, Math.PI);




//    	model.spawnJoint(3.0,1.8,false);
//    	model.spawnJoint(2.25,1.55,false);
//    	model.spawnJoint(2.75,1.55,false);
//    	model.spawnJoint(3.25,1.55,false);
//    	model.spawnJoint(3.75,1.55,false);
//    	model.spawnJoint(3.0,1.3,false);
//    	model.spawnJoint(2.5,1.0,false);
//    	model.spawnJoint(2.5,1.0,false);
//    	model.spawnJoint(3.5,1.0,false);
//    	model.spawnJoint(3.5,1.0,false);
//   
//    	model.spawnSpring(0,1,3);
//    	model.spawnSpring(0,4,3);
//    	model.spawnSpring(0,5,3);
//    	model.spawnSpring(1,5,3);
//    	model.spawnSpring(4,5,3);
//    	model.spawnSpring(1,2,3);
//    	model.spawnSpring(2,3,3);
//    	model.spawnSpring(3,4,3);
//    	model.spawnSpring(3,1,3);
//    	model.spawnSpring(3,0,3);
//    	model.spawnSpring(2,4,3);
//    	model.spawnSpring(2,0,3);
//    	model.spawnSpring(2,5,3);
//    	model.spawnSpring(3,5,3);
// 
//    	model.spawnMuscle(6,1,3,0.5,0.10,0);
//    	model.spawnMuscle(6,2,3,0.5,0.10,1.57);
//    	model.spawnMuscle(7,1,3,0.5,0.10,3.14);
//    	model.spawnMuscle(7,2,3,0.5,0.10,2.36);
//    	model.spawnMuscle(8,3,3,0.5,0.10,0.79);
//    	model.spawnMuscle(8,4,3,0.5,0.10,2.35);
//    	model.spawnMuscle(9,3,3,0.5,0.10,3.92);
//    	model.spawnMuscle(9,4,3,0.5,0.10,3.13);
//
//		model.setName("Dainty Walker");

		return model;
	}

	public static Model loadModel(String path) {
		try {
			DefinitionReader r = new DefinitionReader(new FileReader(path));
			Model m = r.read();
			return m;
		} catch (Exception e) {
			System.out.println(path + ": " + e.getMessage());
			return null;
		}
	}
	
	
	public static Model createTestModel() {
		Model model = new Model();

		model.spawnJoint(3.0, 1.8, false);  // 0
		model.spawnJoint(2.25, 1.55, false); // 1
		model.spawnJoint(2.75, 1.55, false); // 2
		model.spawnJoint(3.25, 1.55, false); // 3
		model.spawnJoint(3.75, 1.55, false); // 4
		model.spawnJoint(3.0, 1.3, false); // 5
		model.spawnJoint(2.5, 1.0, false); // 6
		model.spawnJoint(2.5, 1.0, false); // x - 7
		model.spawnJoint(3.5, 1.0, false); // 7 - 8
		model.spawnJoint(3.5, 1.0, false); // x - 9

		try {
			model.spawnSpring(0, 1, 3);
			model.spawnSpring(0, 4, 3);
			model.spawnSpring(0, 5, 3);
			model.spawnSpring(1, 5, 3);
			model.spawnSpring(4, 5, 3);
			model.spawnSpring(1, 2, 3);
			model.spawnSpring(2, 3, 3);
			model.spawnSpring(3, 4, 3);
			model.spawnSpring(3, 1, 3);
			model.spawnSpring(3, 0, 3);
			model.spawnSpring(2, 4, 3);
			model.spawnSpring(2, 0, 3);
			model.spawnSpring(2, 5, 3);
			model.spawnSpring(3, 5, 3);

//			model.spawnMuscle(6, 1, 3, 0.5, 0.10, 0);
//			model.spawnMuscle(6, 2, 3, 0.5, 0.10, 1.57 / (2 * Math.PI));
//			model.spawnMuscle(7, 1, 3, 0.5, 0.10, 3.14 / (2 * Math.PI));
//			model.spawnMuscle(7, 2, 3, 0.5, 0.10, 2.36 / (2 * Math.PI));
//			model.spawnMuscle(8, 3, 3, 0.5, 0.10, 0.79 / (2 * Math.PI));
//			model.spawnMuscle(8, 4, 3, 0.5, 0.10, 2.35 / (2 * Math.PI));
//			model.spawnMuscle(9, 3, 3, 0.5, 0.10, 3.92 / (2 * Math.PI));
//			model.spawnMuscle(9, 4, 3, 0.5, 0.10, 3.13 / (2 * Math.PI));
			
//			model.spawnMuscle(6, 1, 3, 0.5, 0.10, 0);
//			model.spawnMuscle(6, 2, 3, 0.5, 0.10, 1.57);
//			model.spawnMuscle(7, 1, 3, 0.5, 0.10, 3.14);
//			model.spawnMuscle(7, 2, 3, 0.5, 0.10, 2.36);
//			model.spawnMuscle(8, 3, 3, 0.5, 0.10, 0.79);
//			model.spawnMuscle(8, 4, 3, 0.5, 0.10, 2.35);
//			model.spawnMuscle(9, 3, 3, 0.5, 0.10, 3.92);
//			model.spawnMuscle(9, 4, 3, 0.5, 0.10, 3.13);
			
			model.spawnMuscle(6, 1, 3, 0.5, 0.10, 0);
			model.spawnMuscle(6, 2, 3, 0.5, 0.10, 1.57 * 0.5);
			model.spawnMuscle(7, 1, 3, 0.5, 0.10, 3.14 * 0.5);
			model.spawnMuscle(7, 2, 3, 0.5, 0.10, 2.36 * 0.5);
			model.spawnMuscle(8, 3, 3, 0.5, 0.10, 0.79 * 0.5);
			model.spawnMuscle(8, 4, 3, 0.5, 0.10, 2.35 * 0.5);
			model.spawnMuscle(9, 3, 3, 0.5, 0.10, 3.92 * 0.5);
			model.spawnMuscle(9, 4, 3, 0.5, 0.10, 3.13 * 0.5);
			
		} catch (Exception e) {
		}

		model.setName("Dainty Walker");

		return model;
	}

	public static void elapseTime(int ms) {
		handleMouse();
		planet.elapseTime(ms);
	}

	public static void handleMouse() {
		Model model = planet.getModel();

		if (jointHovering == null) {
			for (Joint joint : model.getJoints()) {
				if (joint.collides(mouseX, mouseY)) {
					joint.setHovering(true);
					jointHovering = joint;
					break;
				}
			}
		} else if (jointDragging == null && !jointHovering.collides(mouseX, mouseY)) {
			jointHovering.setHovering(false);
			jointHovering = null;
		} else if (jointDragging != null) {
			jointDragging.setX(mouseX);
			jointDragging.setY(mouseY);
		}
	}

	public static void setMousePosition(double x, double y) {
		mouseX = x;
		mouseY = y;
	}

	public static void beginMouseDrag() {
		if (jointHovering != null) {
			jointDragging = jointHovering;
			jointDragging.setDragging(true);
		}
	}

	public static void endMouseDrag() {
		if (jointDragging != null) {
			jointDragging.setDragging(false);
			jointDragging = null;
		}
	}
}
