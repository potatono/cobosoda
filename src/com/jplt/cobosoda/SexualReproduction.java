package com.jplt.cobosoda;

public class SexualReproduction implements IReproduce {
	public static final String VERSION = "$Revision: 1.1 $";
	public static final String ID = "$Id: SexualReproduction.java,v 1.1 2008/05/25 23:06:45 potatono Exp $";

	Model mom = null;
	Model dad = null;
	
	/**
	 * Adds a parent to the reproduction mix
	 * @param parent The parent to add
	 * @throws IllegalStateException if you try to add a third parent
	 * @fixme There is no notion of male and female yet
	 */
	public void addParent(Model parent) throws IllegalStateException {
		if (mom == null)
			mom = parent;
		else if (dad ==  null)
			dad = parent;
		else
			throw new IllegalStateException("Only two parents allowed");
	}

	public int getNumberOfParents() {
		if (mom != null && dad != null)
			return 2;
		else if (mom != null || dad != null) 
			return 1;
		else
			return 0;
	}

	public int getRequiredNumberOfParents() {
		return 2;
	}
	
	/**
	 * Perform reproduction and return a new child
	 * @return A child of the two parents
	 */
	public Model reproduce() {
		Model child = new Model();
		Model expressedParent = getExpressedParent();
		Limb limb;
		
		for (int i=0; i<expressedParent.getNumberOfJoints(); i++) {
			// Spawn a new joint in the child by cloning the expressed parent's joint
			child.spawnJoint(expressedParent.getJoint(i));
		
			// Get a new parent to express for the next iteration
			expressedParent = getExpressedParent();
		}
		
		for (int i=0; i<expressedParent.getNumberOfLimbs(); i++) {
			limb = expressedParent.getLimbs().get(i);
			
			// Safety check, don't try to add to non-existent joints
			if (limb.getJoint(0).getIndex() >= child.getNumberOfJoints() ||
				limb.getJoint(1).getIndex() >= child.getNumberOfJoints()) 
					continue;
			
			// Spawn a new limb by cloning the expressed parent's limb.
			child.spawnLimb(limb);
			
			// Get a new parent to express for the next iteration
			expressedParent = getExpressedParent();
		}
		
		// Apply some mutations so we actually get some diversity.
		applyMutations(child);
		
		// Genetic clean up, remove disconnected joints
		child.removeDisconnectedJoints();
		
		return child;
	}
	
	/**
	 * Returns a random parent to be expressed in reproduction
	 * @param ratio The ratio of mom/dad
	 * @return A parent model to be expressed
	 */
	private Model getExpressedParent(double ratio) {
		return Math.random() > ratio ? dad : mom;
	}
	
	/**
	 * Returns a random parent to be expressed in reproduction, with even odds
	 * @return A parent model to be expressed
	 */
	private  Model getExpressedParent() {
		return getExpressedParent(0.5);
	}
	
	public void reset() {
		mom = null;
		dad = null;
	}
	
	public int getNumberOfMutations() {
		int number = (int)Math.floor(Math.log(Math.random()*10));
		return number;
	}
	
	public void applyMutations(Model child) {
		int numberOfMutations = getNumberOfMutations();
		
		MutationList mutations = MutationList.getInstance();
		
		for (int i=0; i<numberOfMutations; i++) {
			try {
				mutations.applyMutation(child);
			}
			catch (Exception e) {
				// Catch the exception and move on, so we don't kill the thread.
				e.printStackTrace(System.err);
			}
		}
	}
}
