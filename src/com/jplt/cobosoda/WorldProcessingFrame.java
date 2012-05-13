package com.jplt.cobosoda;

import processing.core.*;
import java.awt.*;
//import java.awt.event.*;
//import java.io.*;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Jacob Joaquin
 */
public class WorldProcessingFrame extends Frame {

	PApplet embed = new WorldProcessing();
	private GraphicsDevice graphicsDevice;
	private DisplayMode origDisplayMode;
//	private GraphicsDevice graphicsDevice;
//	private DisplayMode origDisplayMode;
	public WorldProcessingFrame() {
		super("cobosoda chance^2 evolution");

		/* Full Screen */
		GraphicsEnvironment graphicsEnvironment =
				GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice[] devices =
				graphicsEnvironment.getScreenDevices();
		fullScreen(devices[0]);

		
//		setLocation(200, 200);
		setBackground(Color.black);
		setSize(800, 600);
		setLayout(new BorderLayout());
		add(embed, BorderLayout.CENTER);
		embed.init();
	}

	public void update() {
		embed.redraw();
	}

	public PApplet getPApplet() {
		return embed;
	}

	public void fullScreen(GraphicsDevice gd) {
		graphicsDevice = gd;
		
		if (graphicsDevice.isFullScreenSupported()) {
			setResizable(false);
			setUndecorated(true);
			graphicsDevice.setFullScreenWindow(this);
			validate();
		}
	}
}
