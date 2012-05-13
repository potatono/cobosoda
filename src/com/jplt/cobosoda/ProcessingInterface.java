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

import javax.swing.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Collections;
import processing.core.*;

/*
display distances in high score tables
display rank
And eliminate one known bug during evolution, disappearing limb.
transitions between race, the flashy idea I have.
refine select limb


- Message why a name isn't valid
- Message why a model isn't valid.
- select limb bug
-check of x's
	-finalScore
	-intro screen
    -initials in high score table?
		-_C_ causes misalignment
- convert all model named text with PMethod.substituteDelimiter(String s)
- Ensure a models name is unique in the Naming stage.
- create method to display text that remove _"
- validate text - sanitize text input
- Ensure a model is valid before finishing the design stage.
- bug spring can connect where a muscle already exists.
- Joint sizes in Constructor don't mouseHover right.
- Some bells and whistles in attract mode.
- High score tables. One for original design, one for best mutant.
- Update high scores after game.
- Audio. FX and maybe music.
- Make the evolution stage look nice.
- Statistics after second race.
 */
public class ProcessingInterface extends SimulatorInterface implements ActionListener {

	public static final String VERSION = "$Revision: 1.10 $";
	public static final String ID = "$Id: ProcessingInterface.java,v 1.10 2009/05/31 17:46:49 potatono Exp $";
	public static final String DIRECTORY = "arcade";
	private Timer timer;
	private WorldProcessingFrame processing;
	private PApplet p;
	private ProcessingMode mode;
	private String directory = "arcade";
	private ArrayList<ProcessingModelScore> userHighScores;
	private ArrayList<ProcessingModelScore> mutantHighScores;
	public PFont font18;
	public PFont font24;
	public PFont font36;
	public PFont font48;
	private SynthEngine synth;

	public ProcessingInterface() {
		userHighScores = new ArrayList<ProcessingModelScore>();
		mutantHighScores = new ArrayList<ProcessingModelScore>();
		loadHighScores();

		processing = new WorldProcessingFrame();
//		processing.setSize(Config.WINDOW_X, Config.WINDOW_Y);
//		processing.setSize(1024, 768);
		processing.setResizable(false);
		processing.setVisible(true);
		p = processing.getPApplet();
		synth = new SynthEngine(this);

		font18 = p.loadFont("VectorBattle-18.vlw");
		font24 = p.loadFont("VectorBattle-24.vlw");
		font36 = p.loadFont("VectorBattle-36.vlw");
		font48 = p.loadFont("VectorBattle-48.vlw");

//		Model foo = World.loadModel("arcade/XXXXXCCxxxxxxxxx.mutant");
//		Model foo = World.createTestModel();
		
//		Model bar = World.loadModel("arcade/DASH--.mutant");

//		Model foo = World.createTestModel();
//		foo = PMethod.centerModel(foo);
//		foo = PMethod.groundModel(foo);
		mode = new ProcessingBootOff(p, this);
//		mode = new ProcessingStandbyTitle(p, this);
//		mode = new ProcessingPlayConstructor(p, this);
//		mode = new ProcessingEnterName(p, this, foo);
//		mode = new ProcessingPlayFirstRace(p, this, foo);
//		mode = new ProcessingEvolveStage(p, this, foo);
//		mode = new ProcessingPlaySecondRace(p, this, foo, foo);
//		mode = new ProcessingFinalScore(p, this, foo, foo);
//		mode = new ProcessingHighScoreTable(p, this, userHighScores, 0);
//		mode = new ProcessingNull(p, this);

		timer = new Timer(1000 / Config.FRAMES_PER_SECOND, this);
		timer.start();
		
	}

	private Model createBrokenModel() {
		Model m = new Model();
		Joint j1 = new Joint(m, 0, 0, false);
		Joint j2 = new Joint(m, 0, 1, false);
		Joint j3 = new Joint(m, 1, 0, false);
		Joint j4 = new Joint(m, 1, 1, false);

		m.spawnJoint(j1);
		m.spawnJoint(j2);
		m.spawnJoint(j3);

		try {
//			m.spawnSpring(j1, j2, 3);
//			m.spawnSpring(j3, j4, 3);
//			m.spawnSpring(j3, j2, 3);
			m.spawnSpring(0, 1, 3);
			m.spawnSpring(1, 2, 3);
			m.spawnSpring(2, 0, 3);
			
		} catch (Exception e) {
		}
		
		return m;
	}

	public SynthEngine getSynth() {
		return synth;
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == timer) {
			timerFired(e);
		}
	}

	public void timerFired(ActionEvent e) {
		p.background(0);
		mode.display();
		processing.update();
	}

	public void setMode(ProcessingMode m) {
		mode = m;
	}

	public ArrayList<ProcessingModelScore> getUserHighScores() {
		return userHighScores;
	}

	public ArrayList<ProcessingModelScore> getMutantHighScores() {
		return mutantHighScores;
	}

	private void loadHighScores() {

		/* Clear ArrayLists */
		userHighScores = new ArrayList<ProcessingModelScore>();
		mutantHighScores = new ArrayList<ProcessingModelScore>();

		File dir = new File(DIRECTORY);

		/* Fill score tables. */
		if (dir.isDirectory()) {
			String[] dirContents = dir.list();

			for (int i = 0; i < dirContents.length; i++) {
				if (dirContents[i].endsWith(".user")) {
					System.out.println(dirContents[i]);
					Model m = World.loadModel(DIRECTORY + "/" + dirContents[i]);
					userHighScores.add(new ProcessingModelScore(dirContents[i],
							m.getDistanceWalked(), m.getName()));
				} else if (dirContents[i].endsWith(".mutant")) {
					System.out.println(dirContents[i]);
					Model m = World.loadModel(DIRECTORY + "/" + dirContents[i]);
					mutantHighScores.add(new ProcessingModelScore(
							dirContents[i], m.getDistanceWalked(), m.getName()));
				}
			}

			sortScoreTables();
		}
	}

	/**
	 * Sorts the score tables from highest to lowest.
	 */
	public void sortScoreTables() {
		Collections.sort(userHighScores, new DistanceComparator());
		Collections.sort(mutantHighScores, new DistanceComparator());
	}

	/*
	 * Returns highest to lowest distance walked.
	 */
	public class DistanceComparator implements Comparator {

		public int compare(Object o1, Object o2) {
			double v1 = ((ProcessingModelScore) o1).getDistance();
			double v2 = ((ProcessingModelScore) o2).getDistance();

			if (v1 == v2) {
				return 0;
			} else if (v1 < v2) {
				return 1;
			} else {
				return -1;
			}
		}
	}

	public void reset() {
		loadHighScores();
		mode = new ProcessingStandbyTitle(p, this);
	}

	public void reloadScores() {
		loadHighScores();
	}

	public ProcessingModelScore getMutantHighScoreByName(String s) {
		for (ProcessingModelScore ms : mutantHighScores) {
			System.out.println(ms.getName() + " " + s);

			if (ms.getName().equals(s)) {
				return ms;
			}
		}

		return null;
	}
}