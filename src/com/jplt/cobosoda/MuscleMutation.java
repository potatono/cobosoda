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

public class MuscleMutation extends NullMutation {
	public static final String VERSION = "$Revision: 1.6 $";
	public static final String ID = "$Id: MuscleMutation.java,v 1.6 2008/05/25 23:06:45 potatono Exp $";

	private double maxAmplitudeOffset = 1;
	private double maxFrequencyOffset = 1;
	private double maxAmplitude = 1;
	private double maxFrequency = 3.15;
	private double maxOffset = 2 * Math.PI;
	private double maxOffsetOffset = 1;
	
	public MuscleMutation() {
		super();
	}
	
	public MuscleMutation(double maxAmplitudeOffset, double maxFrequencyOffset) {
		super();
		this.maxAmplitudeOffset = maxAmplitudeOffset;
		this.maxFrequencyOffset = maxFrequencyOffset;
	}
	
	public double getMaxAmplitudeOffset() {
		return maxAmplitudeOffset;
	}

	public void setMaxAmplitudeOffset(double maxAmplitudeOffset) {
		this.maxAmplitudeOffset = maxAmplitudeOffset;
	}

	public double getMaxFrequencyOffset() {
		return maxFrequencyOffset;
	}

	public void setMaxFrequencyOffset(double maxFrequencyOffset) {
		this.maxFrequencyOffset = maxFrequencyOffset;
	}

	public Model mutate(Model m) {
		Joint j = m.getJoint((int)Math.floor(Math.random() * m.getNumberOfJoints()));
		
		if (j.getLimbCount() > 0) {
			Limb s = j.getLimb((int)Math.floor(Math.random() * j.getLimbCount()));
	
			// TODO FIXME Need a way to turn a spring into a muscle
			if (s instanceof Muscle) {
				Muscle muscle = (Muscle)s;
				double newAmplitude = randomlyShift(muscle.getAmplitude(),maxAmplitudeOffset);
				if (Math.abs(newAmplitude) < maxAmplitude) {
					muscle.setAmplitude(newAmplitude);
				}
	
				double newFrequency = randomlyShift(muscle.getFrequency(),maxFrequencyOffset);
				if (Math.abs(newFrequency) < maxFrequency) {
					muscle.setFrequency(newFrequency);
				}
				
				double newOffset = randomlyShift(muscle.getOffset(),maxOffsetOffset);
				if (Math.abs(newOffset) < maxOffset) {
					muscle.setOffset(newOffset);
				}
			}
			else {
				int from = s.getJoint(0).getIndex();
				int to = s.getJoint(1).getIndex();
				double k = (s instanceof Spring) ? ((Spring)s).getK() : m.getMechanics().getSpringK();
				
				try {
					m.removeLimb(s);
					m.spawnMuscle(from, to, k, 
							Math.random() / 2 * maxFrequency, 
							Math.random() / 2 * maxAmplitude, 
							Math.random() * maxOffset
					);
				}
				catch (Exception e) {}
			}
		}
		
		return m;
	}
	
	private double randomlyShift(double n, double m) {
		return n + (Math.random() * m) * (Math.random() > 0.5 ? -1 : 1);
	}
}
