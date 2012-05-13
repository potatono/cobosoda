package com.jplt.cobosoda;

import java.util.Hashtable;

/**
 * Pre-calculated energy distribution map for a Model.
 * 
 * @author Jacob Joaquin
 *
 */
public class ModelEnergyMap extends Hashtable<Spring, Double> {
	private static final double INITIAL_ENERGY         = 1.0;
	private static final double DEFAULT_RATIO          = 0.5;
	private static final double DEFAULT_THRESHOLD      = 0.00001;
	private static final double RATIO_UPPER_LIMIT      = 1.0 - 0.00001;
	private static final double RATIO_BOTTOM_LIMIT     = 0.00001;
	private static final double THRESHOLD_BOTTOM_LIMIT = 0.0000001;

	/**
	 * Create a normalized coefficient energy map for a model.
	 * 
	 * @param map   Stores energy propagation coefficient.
	 * @param joint Energy comes into the joint.
	 */	
	public ModelEnergyMap(Joint joint) {
		computeMap(joint, INITIAL_ENERGY, DEFAULT_RATIO, DEFAULT_THRESHOLD);	
	}
	
	/**
	 * Create a coefficient energy map for a model.
	 * 
	 * @param joint     Energy comes into the joint.
	 * @param ratio     Energy stays with string to energy that propagates.
	 * @param threshold Stop propagation when energy level falls below this point.
	 */ 
	public ModelEnergyMap(Joint joint, double ratio, double threshold) {
		/* Sanitize input */
		if (ratio > RATIO_UPPER_LIMIT)
			ratio = RATIO_UPPER_LIMIT;

		if (ratio < RATIO_BOTTOM_LIMIT)
			ratio = RATIO_BOTTOM_LIMIT;

		if (threshold < THRESHOLD_BOTTOM_LIMIT) 
			threshold = THRESHOLD_BOTTOM_LIMIT;		

		computeMap(joint, INITIAL_ENERGY, ratio, threshold);
	}
	
	/**
	 * Creates a normalized coefficient energy map for a model.
	 * This recursive method propagates energy through a model,
	 * accumulating the distributed energy into a Hashtable.
	 * 
	 * @param joint     Energy comes into the joint.
	 * @param energy    Energy left to be distributed.
	 * @param ratio     Energy stays with string to energy that propagates.
	 * @param threshold Stops propagating if energy falls below this.
	 */
	private void computeMap(Joint joint, double energy, double ratio, double threshold) {
		/* Split the energy between all connection springs */
		energy = energy / (double) joint.getLimbCount();
		
		for (Limb limb:joint.getLimbs()) {
			if (limb instanceof Spring) {
				Spring spring = (Spring)limb;
				
				/* Create Hash Entry if does not exist */
				if(!this.containsKey(spring))
					this.put(spring, new Double(0.0));

				/* Add energy to spring */
				this.put(spring, new Double(this.get(spring).doubleValue() + energy * ratio));
			
				/* Propagate energy if threshold not reached */
				if (energy >= threshold)
					computeMap(joint.findOppositeJoint(spring), energy * (1.0 - ratio),
						ratio, threshold);
			}
		}
	}

	/**
	 * Return the total energy. This class rounds off energy distribution
	 * based on the threshold. Should be slightly less than zero.
	 * 
	 * @return total Total energy within map.
	 */
	public double getTotalEnergy() {
		double total = 0.0;
		
		for (Double d:this.values()) {
			total += d.doubleValue();
		}
		
		return total;
	}
}
