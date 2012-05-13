/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.jplt.cobosoda;

/**
 *
 * @author Jacob Joaquin
 */
public class ProcessingTestModel {
	private Model model;
	private double timeOfRaceInSeconds;
	private double timer = 0;
	private ProcessingRaceStats stat;
	
	public ProcessingTestModel(double t) {
		timeOfRaceInSeconds = t;
	}
	
	public ProcessingRaceStats race(Model m) {
		model = m.clone();
		stat = new ProcessingRaceStats(model);
		timer = timeOfRaceInSeconds;
		
		while(timer > 0.0) {
			elapseTime(1000 / Config.FRAMES_PER_SECOND);
			updateStats();
		}
		
		return stat;
	}

	private void elapseTime(int ms) {
		model.elapseTime(ms);
		timer -= (double) ms * 0.001;
    }	
	
	private void updateStats() {
		stat.getDistance();
	}
	
}
