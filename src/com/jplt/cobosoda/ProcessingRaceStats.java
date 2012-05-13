/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.jplt.cobosoda;

/**
 *
 * @author Jacob Joaquin
 */
public class ProcessingRaceStats {
	private double initialCenter;
	private double distance;
	private double maxDistance = 0;
	
	private Model model;
	
	public ProcessingRaceStats(Model m) {
		model = m;
		initialCenter = PMethod.calculateCenter(model);
	}
	
	public double getDistance() {
		distance = initialCenter - PMethod.calculateCenter(model);
		
		if (Math.abs(distance) > maxDistance ) {
			maxDistance = Math.abs(distance);
		}

		return distance;
	}
	
	public double getMaxDistance() {
		return maxDistance;
	}
}
