/* Slipmat
 * Copyright 2008 by Jacob Joaquin
 * 
 * This file is part of Slipmat.
 * 
 *     Slipmat is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Lesser General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 * 
 *     Slipmat is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Lesser General Public License for more details.
 * 
 *     You should have received a copy of the GNU Lesser General Public License
 *     along with Slipmat.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.jplt.cobosoda;

import java.util.ArrayList;
import com.thumbuki.slipmat.ChnA;
import com.thumbuki.slipmat.ChnK;
import com.thumbuki.slipmat.Instrument;
import com.thumbuki.slipmat.Module;
import com.thumbuki.slipmat.Table;

/**
 * This module produces a short sinewave burst with a percussive
 * attack.  This is a development instrument used to test Slipmat.
 * 
 * @author Jacob Joaquin
 */
public class SynthStart extends Module {

	private final double DEFAULT_DURATION = 0.1;
	private double duration = DEFAULT_DURATION;
	
	private Instrument instr;
	private Table triangle;
	private ChnA audioOut;
	
	public void setup() {
		instr = new Instrument(assignInstrumentNumber());
		addInstrument(instr);
		
		triangle = new Table(256, -7, 0, 64, 1, 128, -1, 64, 0);
		addTable(triangle);
		
		audioOut = new ChnA(true);
		addChn(audioOut);
	}

	public void compile() {
		instr.appendln("kenv expseg 2, 0.14938, 15, 0, 8, 0.17, 22, 0, 13, 0.17, 28");
        instr.appendln("aenv2 linseg 1, p3 - 0.02, 1, 0.2, 0");

        instr.appendln("kphasor phasor 1 / 0.01625");
        instr.appendln("ktrig trigger kphasor, 0.5, 0");
        instr.appendln("ksamp samphold kenv, ktrig");
 
        instr.appendln("a1 oscil 0.5, 1 / (0.01625 / int(ksamp)), " + triangle.getName() + ", -1");
		instr.appendln("chnmix a1 * aenv2 * p4, " + audioOut.getQuote());
	}

	public void initialScoreEvents() {
		/* None */
	}

	/**
	 * Get the audio output.
	 * 
	 * @return audioOut Audio output.
	 */
	public ChnA getOutput() {
		return audioOut;
	}

	/**
	 * Plays a percussive sinewave.
	 * 
	 * @param time When to play note.
	 * @param amp  Amplitude.
	 * @param freq Frequency.
	 */
	public void playNote(double time, double amp) {
		iEvent(instr, time, (0.14938 + 0.17 + 0.17), amp);
	}

}

	