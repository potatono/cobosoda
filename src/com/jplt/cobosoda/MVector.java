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

public class MVector {
	public static final String VERSION = "$Revision: 1.5 $";
	public static final String ID = "$Id: MVector.java,v 1.5 2008/07/12 01:22:15 thumbuki Exp $";
	public static final MVector ZERO = new MVector();
	
    double angle;
    double magnitude;

    public MVector()
    {
        angle = 0;
        magnitude = 0;
    }

    public MVector(double a, double m)
    {
        angle=a;
        magnitude=m;
    }

    public MVector(int a, double m)
    {
        angle =  Math.toRadians(a);
        magnitude = m;
    }

    public double getAngle()        { return angle; }
    public int getDegrees()         { return (int) Math.toDegrees(angle); }
    public double getMagnitude()    { return magnitude; }
    public double getX()            { return magnitude * Math.cos(angle); }
    public double getY()            { return magnitude * Math.sin(angle); }
    public void roundTo(double n)
    {
        magnitude = ((int)(magnitude*n))/n;
    }

    public void add(MVector v2)
    {
        double x,y;

        x = magnitude * Math.cos(angle) + v2.magnitude * Math.cos(v2.angle);
        y = magnitude * Math.sin(angle) + v2.magnitude * Math.sin(v2.angle);

        magnitude = Math.sqrt(x*x + y*y);
        angle = Math.atan2(y,x);
    }

    public void add(double m)
    {
        magnitude += m;
    }

    public void negateAngle()
    {
        double an = angle + Math.PI;
        while (an > 2*Math.PI) { an -= 2* Math.PI; }
        angle = an;

//		angle = (angle + Math.PI) % (2 * Math.PI);
    }

    public void subtract(MVector v2)
    {
        v2.negateAngle();
        add(v2);
    }

    public void subtract(double m)
    {
        magnitude -= m;
    }

    public void absX()
    {
        double x,y;

	    x = Math.abs(magnitude * Math.cos(angle));
	    y = magnitude * Math.sin(angle);

	    angle = Math.atan2(y,x);
    }

    public void absY()
    {
        double x,y;

        x = magnitude * Math.cos(angle);
        y = Math.abs(magnitude * Math.sin(angle));

        angle = Math.atan2(y,x);
    }

    public void invertX()
    {
        double x,y;

        x = -magnitude * Math.cos(angle);
        y = magnitude * Math.sin(angle);

        angle = Math.atan2(y,x);
    }

       public void invertY()
    {
        double x,y;

        x = magnitude * Math.cos(angle);
        y = -magnitude * Math.sin(angle);

        angle = Math.atan2(y,x);
    }

    public void nabsX()
    {
        double x,y;

	    x = -Math.abs(magnitude * Math.cos(angle));
	    y = magnitude * Math.sin(angle);

	    angle = Math.atan2(y,x);
    }

    public void nabsY()
    {
        double x,y;

        x = magnitude * Math.cos(angle);
        y = -Math.abs(magnitude * Math.sin(angle));

        angle = Math.atan2(y,x);
    }

    public MVector Xvector()
    {
        double x,a;

        x = magnitude * Math.cos(angle);
        a = x < 0 ? Math.PI : 0;

        return new MVector(a,Math.abs(x));
    }

    public MVector Yvector()
    {
        double y,a;

        y = magnitude * Math.sin(angle);
        a = y<0 ? 3*Math.PI/2 : Math.PI/2;

        return new MVector(a,Math.abs(y));
    }

    public String toString()
    {
        return magnitude + "@" + this.getDegrees();
    }

    public void multiplyBy(double n) {
        magnitude *= n;
    }

    public void divideBy(double n) {
        magnitude /= n;
    }
    
}
