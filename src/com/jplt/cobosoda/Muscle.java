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

public class Muscle extends Spring {
	public static final String VERSION = "$Revision: 1.4 $";
	public static final String ID = "$Id: Muscle.java,v 1.4 2008/07/14 21:54:38 thumbuki Exp $";
	private double offset = 0;
	private double frequency = 0;
	private double amplitude = 0;
	private double muscle = 0;
	
	/**
	 * Creates a muscle, a spring that oscillates on a wave
	 * 
	 * @param parent
	 */
	public Muscle(Model parent, double k, double freq, double amp, double offset) {
		super(parent, k);
		this.offset = offset;
		this.frequency = freq;
		this.amplitude = amp;
	}
	
	/**
	 * Returns the current value of the muscle??
	 * @return double
	 */
	public double getValue() { 
		return muscle; 
	}
	
	/**
	 * Returns the amplitude of the wave controlling the muscle
	 * @return amplitude as double
	 */
	public double getAmplitude() { 
		return amplitude; 
	}
	
	/**
	 * Returns offset of the wave controlling the muscle
	 * @return offset as double
	 */
	public double getOffset() { 
		return offset; 
	}
	
	/**
	 * Returns the frequency of the wave controlling the muscle 
	 * @return frequency as double
	 */
	public double getFrequency() { 
		return frequency; 
	}
	
	/**
	 * Set the amplitude of the wave controlling the muscle
	 * @param a
	 */
	public void setAmplitude(double a) { 
		this.amplitude = a; 
	}
	
	/**
	 * Set the offset of the wave controlling the muscle
	 * @param o
	 */
	public void setOffset(double o) { 
		this.offset = o; 
	}
	
	/**
	 * Set the frequency of the wave controlling the muscle
	 * @param f
	 */
	public void setFrequency(double f) { 
		this.frequency = f; 
	}
	
	@Override
	protected double computeLoad(double l) {
		super.computeLoad(l);
//		double a = super.computeLoad(l);
		double t = getT();
		
		if (amplitude != 0) {
			/* Offset is now phase */
//			muscle = Math.cos(2 * Math.PI * frequency * (t + offset));
			muscle = Math.cos(2 * Math.PI * frequency * t + offset * 2 * Math.PI);
			a += muscle * amplitude * length;

			if (Config.DEBUG_MUSCLES)
			{
				System.out.println("DEBUG_MUSCLES: muscle=" + muscle +" t = " + t);
			}
		}
		
		return a;
	}
	
	@Override
	public Muscle clone(Model parent) {
		Muscle clone = new Muscle(parent, getK(), frequency, amplitude, offset);
		return clone;
	}
}
