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
import java.awt.*;

public class WorldCanvas extends Canvas {	
	public static final String VERSION = "$Revision: 1.6 $";
	public static final String ID = "$Id: WorldCanvas.java,v 1.6 2009/05/31 17:46:49 potatono Exp $";

	private static final long serialVersionUID = -342564276539546584L;

	public void paint(Graphics g) {
		drawWorld(g);
	}
	
	public void drawJoint(Joint j, Graphics g) {
		int x = doubleToPixels(j.getX());
		int y = invertVertical(doubleToPixels(j.getY()));
		
		if (j.getDragging()) {
			g.setColor(Color.red);
		}
		else {
			g.setColor(Color.green);
		}
		
		if (j.getHovering()) {
			g.drawOval(x-Config.JOINT_RADIUS-3,y-Config.JOINT_RADIUS-3,Config.JOINT_RADIUS*2+6,Config.JOINT_RADIUS*2+6);
		}
		g.fillOval(x-Config.JOINT_RADIUS,y-Config.JOINT_RADIUS,Config.JOINT_RADIUS*2,Config.JOINT_RADIUS*2);
	}
	
	public void drawLimb(Limb s, Graphics g) {
		int x1 = doubleToPixels(s.getJoint(0).getX());
		int y1 = invertVertical(doubleToPixels(s.getJoint(0).getY()));
		int x2 = doubleToPixels(s.getJoint(1).getX());
		int y2 = invertVertical(doubleToPixels(s.getJoint(1).getY()));
		
		g.setColor(getLimbColor(s));
		g.drawLine(x1, y1, x2, y2);
	}
	
	public void drawWorld(Graphics g) {
		Model model = World.getModel();
		
		for (Limb s:model.getLimbs()) {
			drawLimb(s,g);
		}
		for (Joint joint:model.getJoints()) {
			drawJoint(joint,g);
		}

		g.setColor(Color.darkGray);
		int x = doubleToPixels(model.getDistanceMarkerX());
		g.drawLine(x, 0, x, this.getHeight());
	
		g.setColor(Color.white);
		g.drawString(model.getName(),10,20);
		
		g.drawString("Distance: " + String.format("%1$.3f",model.getDistanceTravelled()) + " m", 500, 20);
		g.drawString("Time: " + String.format("%1$.3f",World.getTimeElapsed() / 1000.0) + " s", 650, 20);
		
		int generation = World.getEvolutionGeneration();
		if (generation > 0) {
			g.drawString("Generation: " + generation, 350, 20);
		}
	}
				
	private int doubleToPixels(double d) {
		return (int)Math.floor(d * Config.PIXELS_PER_METER);
	}
	
	private int invertVertical(int y) {
		return this.getHeight() - y;
	}
	
	private Color getLimbColor(Limb s) {
		if (s instanceof Muscle) {
			float str = (float)((Muscle)s).getValue();
			
			if (str < 0) {
				str = Math.abs(str);
				return new Color(0.25f,0.25f,str);
			}
			else {
				return new Color(str,0.25f,0.25f);
			}
		}
		
		return Color.white;
	}
}
