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
public class SynthGameChip extends Module {

	private final double DEFAULT_DURATION = 0.1;
	private double duration = DEFAULT_DURATION;
	
	private Instrument setupNoise;
	private Instrument instr;
	private Table table1;
	private Table table2;
	private Table table3;
	private ChnA audioLeftOut;
	private ChnA audioRightOut;
	private EnvelopeData freqEnv;
	private EnvelopeData ampEnv;
	
	public void setup() {
		freqEnv = new EnvelopeData();
		ampEnv = new EnvelopeData();
		
		setupNoise = new Instrument(assignInstrumentNumber());
		addInstrument(setupNoise);
		
		instr = new Instrument(assignInstrumentNumber());
		addInstrument(instr);
		
		table1 = new Table(16, -2, 0, 0, 1, 0, 1, 0, 0, 0, 0, 1, 1, 1, 0, 1, 1,
				1);
		table2 = new Table(32, -2, 0, 0, 1, 0, 1, 1, 0, 0, 1, 1, 1, 1, 1, 0, 0,
				0, 1, 1, 0, 1, 1, 1, 0, 1, 0, 1, 0, 0, 0, 0, 1);
		table3 = new Table(512, 21, 2, 1);
		
		addTable(table1);
		addTable(table2);
		addTable(table3);
		
		audioLeftOut = new ChnA(true);
		audioRightOut = new ChnA(true);
		addChn(audioLeftOut);
		addChn(audioRightOut);
	}

	public void compile() {
		instr.appendln("iamp = p4");
		instr.appendln("ifreq = p5");
		instr.appendln("iwave = p6");
		instr.appendln("ipan = p7");
		instr.appendln("ifres = p8");
		instr.appendln("iares = p9");
		instr.appendln("iaa = p10");
		instr.appendln("iadur1 = p11");
		instr.appendln("iab = p12");
		instr.appendln("iadur2 = p13");
		instr.appendln("iac = p14");
		instr.appendln("iarel = p15");
		instr.appendln("iaiz = p16");
		instr.appendln("ifa = p17");
		instr.appendln("ifdur1 = p18");
		instr.appendln("ifb = p19");
		instr.appendln("ifdur2 = p20");
		instr.appendln("ifc = p21");
		instr.appendln("ifrel = p22");
		instr.appendln("ifiz = p23");

		instr.appendln("kenv expsegr ifa, ifdur1, ifb, ifdur2, ifc, ifrel, ifiz");
		instr.appendln("kenv = kenv * ifres");
		instr.appendln("kenv = int(kenv)");
		instr.appendln("kenv = kenv / ifres");
		instr.appendln("kenv = kenv - 1");
		instr.appendln("kfreq = ifreq * (kenv + 1);");

		instr.appendln("kenv2 linsegr iaa, iadur1, iab, iadur2, iac, iarel, iaiz");
		instr.appendln("kenv2 = kenv2 * iares");
		instr.appendln("kenv2 = int(kenv2)");
		instr.appendln("kenv2 = kenv2 / iares");
		instr.appendln("kamp = iamp * kenv2");
		instr.appendln("aosc oscil kamp, kfreq, " + table1.getName() + ", -1");

		instr.appendln("chnmix aosc * sqrt(1 - ipan), " + audioLeftOut.getQuote());
		instr.appendln("chnmix aosc * sqrt(ipan), " + audioRightOut.getQuote());
	}

	public void initialScoreEvents() {
		/* setup noise table */
	}

	/**
	 * Get the audio output.
	 * 
	 * @return audioOut Audio output.
	 */
	public ChnA getLeftOutput() {
		return audioLeftOut;
	}

	public ChnA getRightOutput() {
		return audioRightOut;
	}

	/**
	 * Plays a percussive sinewave.
	 * 
	 * @param time When to play note.
	 * @param amp  Amplitude.
	 * @param freq Frequency.
	 */
	public void playNote(double time, double dur, double amp, double freq,
			int t, double pan) {
		double a0 = ampEnv.getData().get(0).doubleValue();
		double a1 = ampEnv.getData().get(1).doubleValue();
		double a2 = ampEnv.getData().get(2).doubleValue();
		double a3 = ampEnv.getData().get(3).doubleValue();
		double a4 = ampEnv.getData().get(4).doubleValue();
		double a5 = ampEnv.getData().get(5).doubleValue();
		double a6 = ampEnv.getData().get(6).doubleValue();

		double f0 = freqEnv.getData().get(0).doubleValue();
		double f1 = freqEnv.getData().get(1).doubleValue();
		double f2 = freqEnv.getData().get(2).doubleValue();
		double f3 = freqEnv.getData().get(3).doubleValue();
		double f4 = freqEnv.getData().get(4).doubleValue();
		double f5 = freqEnv.getData().get(5).doubleValue();
		double f6 = freqEnv.getData().get(6).doubleValue();
		
		
		iEvent(instr, time, dur, amp, freq, 1, pan, 14, 8,
				a0, a1, a2, a3, a4, a5, a6, f0, f1, f2, f3, f4, f5, f6);
		
		ampEnv.reset();
		freqEnv.reset();
	}
	
	private void setEnv(EnvelopeData env, double na, double ndur1, double nb,
			double ndur2, double nc, double nrel, double nd) {
		env.setData(na, ndur1, nb, ndur2, nc, nrel, nd);
	}
	
	public void setAmpEnv(double na, double ndur1, double nb, double ndur2,
			double nc, double nrel, double nd) {
		setEnv(ampEnv, na, ndur1, nb, ndur2, nc, nrel, nd);
	}
	
	public void setFreqEnv(double na, double ndur1, double nb, double ndur2,
			double nc, double nrel, double nd) {
		setEnv(freqEnv, na, ndur1, nb, ndur2, nc, nrel, nd);
	}
	
	public class EnvelopeData {
		private double a;
		private double dur1;
		private double b;
		private double dur2;
		private double c;
		private double release;
		private double d;
		
		public EnvelopeData() {
			reset();
		}
		
		public void reset() {
			a = 1.0;
			dur1 = 1.0;
			b = 1.0;
			dur2 = 1.0;
			c = 1.0;
			release = 0.01;
			d = 0.0001;
		}
		
		public void setData(double na, double ndur1, double nb,
				double ndur2, double nc, double nrel, double nd) {
			a = na;
			dur1 = (ndur1 <= 0.0 ? 0.0001 : ndur1);
			b = nb;
			dur2 = (ndur2 <= 0.0 ? 0.0001 : ndur2);
			c = nc;
			release = (nrel <= 0.0 ? 0.0001 : nrel);
			d = nd;
		}
		
		public ArrayList<Double> getData() {
			ArrayList<Double> env = new ArrayList<Double>();
			
			env.add(a);
			env.add(dur1);
			env.add(b);
			env.add(dur2);
			env.add(c);
			env.add(release);
			env.add(d);
		
			return env;
		}
	}
}

	