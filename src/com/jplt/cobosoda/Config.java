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

import java.util.*;
import java.io.*;

public class Config {

	public static final String VERSION = "$Revision: 1.11 $";
	public static final String ID = "$Id: Config.java,v 1.11 2009/05/31 17:46:49 potatono Exp $";
	public static final boolean ARCADE_MODE = false;
	public static final boolean DEBUG_MATH = false;
	public static final boolean DEBUG_SPRINGS = false;
	public static final boolean DEBUG_MUSCLES = false;
	public static final boolean DEBUG_FORCES = false;
	public static final boolean PLAY_SYNTH = false;
	public static double GRAVITY = 5;
	public static double ELASTICITY = 0.45;
	public static double FRICTION = 0.90;		  // Coefficient of kenetic friction
	public static double FRICTION_START = 0.07;  // Newtons required to overcome static friction
	public static double JOINT_MASS = 0.0040;
	public static int JOINT_RADIUS = ARCADE_MODE ? 8 : 5;
	public static int JOINT_DIAMETER = JOINT_RADIUS * 2;
	public static int JOINT_MAX_SPRINGS = 16;
	public static double PIXELS_PER_METER = 100.0;
	public static int WINDOW_X = 800;
	public static int WINDOW_Y = 600;
	public static double SIZE_WORLD_X = WINDOW_X / PIXELS_PER_METER;
	public static double SIZE_WORLD_Y = WINDOW_Y / PIXELS_PER_METER;
	public static double SPRING_K = 3.0;
	public static int FRAMES_PER_SECOND = 30;
	public static int TIME_PER_FRAME = 1;
	public static int ROUND_TO = 1000;
	public static double MAX_SPRING = 0.25;
	public static double MIN_SPRING = -0.25;
	public static int TEST_TIME_LIMIT = 300000;
	public static int WINNER_POOL_SIZE = 100;
	public static int SPEED_NORMAL = 1;
	public static int SPEED_HIGH = 25;
	public static int SPEED_LUDICROUS = 500;
	public static final int MODE_BOOT_OFF = 0;
	public static final int MODE_BOOT_GRID = 1;
	public static final int MODE_BOOT_TEST = 2;
	public static final int MODE_STANDBY_TITLE = 3;
	public static final int MODE_STANDBY_DEMO = 4;
	public static final int MODE_STANDBY_HIGH_SCORE = 5;
	public static final int MODE_STANDBY_CREDITS = 6;
	public static final int MODE_PLAY_CONSTRUCTOR = 7;
	public static final int MODE_PLAY_INITIAL_RACE = 8;
	public static final int MODE_PLAY_EVOLVE = 9;
	public static final int MODE_PLAY_FINAL_RACE = 10;
	public static final int MODE_PLAY_SCORE_CHART = 11;
	public static final int MODE_TEST_FOO = 12;
	public static String MUTATION_CLASSES =
			"com.jplt.cobosoda.MuscleMutation," +
			"com.jplt.cobosoda.JointTranslationMutation," +
			"com.jplt.cobosoda.JointAdditionMutation," +
			"com.jplt.cobosoda.SpringSpliceMutation," +
			"com.jplt.cobosoda.AdditionalSpringMutation";
	public static int CONCURRENT_THREADS = 10;
	public static String REPRODUCTION_CLASS = "com.jplt.cobosoda.SexualReproduction";

	/* Collection Magic Numbers here */
	public static double GRAVITY_ANGLE = Math.toRadians(270.0);  /* Direction of Gravity */


	public static void load(String path) {
		try {
			Properties properties = new Properties();
			properties.load(new FileInputStream(path));

			GRAVITY = getDouble(properties, "physics.gravity", GRAVITY);
			FRICTION = getDouble(properties, "physics.friction", FRICTION);
			FRICTION_START = getDouble(properties, "physics.startingFriction", FRICTION_START);
			ROUND_TO = getInt(properties, "physics.roundTo", ROUND_TO);
			JOINT_MASS = getDouble(properties, "joint.mass", JOINT_MASS);
			JOINT_RADIUS = getInt(properties, "joint.radius", JOINT_RADIUS);
			JOINT_MAX_SPRINGS = getInt(properties, "joint.maxSprings", JOINT_MAX_SPRINGS);
			ELASTICITY = getDouble(properties, "spring.elasticity", ELASTICITY);
			SPRING_K = getDouble(properties, "spring.k", SPRING_K);
			MAX_SPRING = getDouble(properties, "spring.max", MAX_SPRING);
			MIN_SPRING = getDouble(properties, "spring.min", MIN_SPRING);
			FRAMES_PER_SECOND = getInt(properties, "window.fps", FRAMES_PER_SECOND);
			TIME_PER_FRAME = getInt(properties, "window.timePerFrame", TIME_PER_FRAME);
			PIXELS_PER_METER = getDouble(properties, "window.pixelsPerMeter", PIXELS_PER_METER);
			WINDOW_X = getInt(properties, "window.width", WINDOW_X);
			WINDOW_Y = getInt(properties, "window.height", WINDOW_Y);
			SIZE_WORLD_X = WINDOW_X / PIXELS_PER_METER;
			SIZE_WORLD_Y = WINDOW_Y / PIXELS_PER_METER;
			TEST_TIME_LIMIT = getInt(properties, "genetics.timeLimit", TEST_TIME_LIMIT);
			WINNER_POOL_SIZE = getInt(properties, "genetics.winnerPoolSize", WINNER_POOL_SIZE);
			MUTATION_CLASSES = properties.getProperty("genetics.mutationClasses", MUTATION_CLASSES);
			REPRODUCTION_CLASS = properties.getProperty("genetics.reproductionClass", REPRODUCTION_CLASS);
			CONCURRENT_THREADS = getInt(properties, "genetics.threads", CONCURRENT_THREADS);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	private static double getDouble(Properties p, String s, double d) {
		try {
			return Double.parseDouble(p.getProperty(s, Double.toString(d)));
		} catch (Exception e) {
			return d;
		}
	}

	private static int getInt(Properties p, String s, int d) {
		try {
			return Integer.parseInt(p.getProperty(s, Integer.toString(d)));
		} catch (Exception e) {
			return d;
		}
	}
}
