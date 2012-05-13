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

import com.thumbuki.slipmat.*;
import com.thumbuki.slipmat.module.*;
import com.thumbuki.slipmat.module.effects.*;
import com.thumbuki.slipmat.module.synth.SinePerc;

/**
 * Translates real-time cobosoda model data into sound.
 * 
 * @author Jacob Joaquin
 */
public class SynthModelObserver extends ModelObserver {

//	private static final int DURATION = 1000;
//	private int totalTime = 0;
//	private int printTime = 0;
//	private int result;
//	private SynthRack synthRack = new SynthRack(true);
//	private SinePerc sinePerc = new SinePerc();
//	private Reverb reverb = new Reverb();
//	private Output output = new Output();
//
	
	private ProcessingInterface pi;
	private SynthEngine synth;
	public SynthModelObserver(Model m, ProcessingInterface proc) {
		super(m);
		this.observeCollisions(m);

		pi = proc;
		synth = pi.getSynth();
	}

	//	@Override
	public void updateCollision(Model model, Joint joint, int collisionType) {
		if (collisionType == Model.COLLISION_BOTTOM) {
			synth.footStep(joint);
		}
	}

	/**
	 * Converts half steps into a ratio.
	 * 
	 * @param hs Half-steps
	 * @return   Ratio
	 */
	private double halfStepsToRatio(double hs) {
		return Math.pow(2.0, hs / 12.0);
	}
}


