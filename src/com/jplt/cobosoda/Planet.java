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

public class Planet {
	public static final String VERSION = "$Revision: 1.2 $";
	public static final String ID = "$Id: Planet.java,v 1.2 2008/04/15 03:03:48 potatono Exp $";

    private double gravity = Config.GRAVITY;
    private double elasticity = Config.ELASTICITY;
    private double friction = Config.FRICTION;
    private double width = Config.SIZE_WORLD_X;
    private double height = Config.SIZE_WORLD_Y;
    //private double spring_k = Config.SPRING_K;
    private double mass = Config.JOINT_MASS;
    private Model model = new Model();
    private long frameNumber = 0;
    private long timeElapsed = 0;
    private IFitnessTest fitnessTest = null;
    
    public void setWidth(double x) 			{ width = x; }
    public void setHeight(double y) 			{ height = y; }
    public void setWidth(int x) 				{ width = x / Config.PIXELS_PER_METER; }
    public void setHeight(int y) 			{ height = y / Config.PIXELS_PER_METER; }
    public double getWidth() 				{ return width; }
    public double getHeight() 				{ return height; }
    public void setGravity(double g) 		{ gravity = g; }
    public double getGravity() 				{ return gravity; }
    public void setSpringElasticity(double e){ elasticity = e; }
    public double getSpringElasticity() 		{ return elasticity; }
    public void setFriction(double f) 		{ friction = f; }
    public double getFriction() 				{ return friction; }
    public void setModel(Model m)			{ model = m; frameNumber = 0; timeElapsed = 0; }
    public Model getModel()					{ return model; }
    public void setMass(double m)			{ mass = m; }
    public double getMass()					{ return mass; }
    public long getFrameNumber()				{ return frameNumber; }
    public long getTimeElapsed()				{ return timeElapsed; }
    
    public void setFitnessTest(IFitnessTest fitnessTest) { 
    	this.fitnessTest = fitnessTest;
    }
    
    public IFitnessTest getFitnessTest() {
    	return this.fitnessTest;
    }
    
    public void elapseTime(int ms) {
    	model.elapseTime(ms);
    	timeElapsed += ms;
    	frameNumber++;
    	if (fitnessTest != null) {
    		fitnessTest.measure();
    	}
    }

}
