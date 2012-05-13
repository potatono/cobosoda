/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.jplt.cobosoda;
import processing.core.*;
/**
 *
 * @author Jacob Joaquin
 */
abstract public class ProcessingMode {
	protected PApplet pa;
	protected ProcessingInterface pi;
	
	public ProcessingMode(PApplet papplet, ProcessingInterface proc) {
		pa = papplet;
		pi = proc;
	}
	
	abstract void display();
}
