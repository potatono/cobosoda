package com.jplt.cobosoda;

public interface IReproduce {
	public static final String VERSION = "$Revision: 1.3 $";
	public static final String ID = "$Id: IReproduce.java,v 1.3 2008/05/25 23:06:44 potatono Exp $";

	public int getNumberOfParents();
	public int getRequiredNumberOfParents();
	public void addParent(Model parent);
	public Model reproduce();
	public void reset();
}
