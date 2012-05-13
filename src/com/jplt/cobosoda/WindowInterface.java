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
import javax.swing.*;


import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class WindowInterface extends SimulatorInterface implements ActionListener, WindowListener, 
	MouseMotionListener, MouseListener, ComponentListener
{
	public static final String VERSION = "$Revision: 1.9 $";
	public static final String ID = "$Id: WindowInterface.java,v 1.9 2009/05/31 17:46:49 potatono Exp $";

	JFrame frame;
    Timer timer;
    JPanel panel;
    WorldCanvas canvas;
    File lastOpenDirectory;
    int speed = Config.SPEED_NORMAL;
    GeneticAlgorithmManager gaManager;
    int secondsRunning = 0;
    Model currentModel;
//	private SynthModelObserver Synth;
//	private WorldProcessingFrame processing;
	
    public WindowInterface()
    {
//		processing = new WorldProcessingFrame();
//		processing.setSize(800,600);
//		processing.setVisible(true);
		
        JMenuBar menuBar;
   
        frame = new JFrame("Jobosoda");
        frame.addWindowListener(this);
        
        // Initialize Controls
        /* Construct Menu */
        menuBar = new JMenuBar();
        frame.setJMenuBar(menuBar);

        JMenu menu = new JMenu("File");
        menu.setMnemonic(KeyEvent.VK_F);

        JMenuItem menuItem = new JMenuItem("Open",KeyEvent.VK_O);
        menuItem.addActionListener(this);
        menu.add(menuItem);
        
        menuItem = new JMenuItem("Save",KeyEvent.VK_S);
        menuItem.addActionListener(this);
        menu.add(menuItem);
        
        menu.addSeparator();
        
        menuItem = new JMenuItem("Start Evolution");
        menuItem.addActionListener(this);
        menu.add(menuItem);
                
        menuItem = new JMenuItem("Stop Evolution");
        menuItem.addActionListener(this);
        menu.add(menuItem);
        
        menu.addSeparator();

        menuItem = new JMenuItem("Exit",KeyEvent.VK_X);
        menuItem.addActionListener(this);
        menu.add(menuItem);
        menuBar.add(menu);

        menu = new JMenu("View");
        menu.setMnemonic(KeyEvent.VK_V);
        
        menuItem = new JMenuItem("Normal Speed",KeyEvent.VK_N);
        menuItem.addActionListener(this);
        menu.add(menuItem);
        
        menuItem = new JMenuItem("High Speed",KeyEvent.VK_H);
        menuItem.addActionListener(this);
        menu.add(menuItem);

        menuItem = new JMenuItem("Ludicrous Speed",KeyEvent.VK_L);
        menuItem.addActionListener(this);
        menu.add(menuItem);
        menuBar.add(menu);

        menu = new JMenu("Help");
        menu.setMnemonic(KeyEvent.VK_H);

        menuItem = new JMenuItem("About",KeyEvent.VK_A);
        menuItem.addActionListener(this);
        menu.add(menuItem);

        menuBar.add(menu);

        // Drawing panel
        panel = new JPanel();
        panel.setLayout(new GridLayout(1,1));
        panel.setMinimumSize(new Dimension(800,600));
        panel.setPreferredSize(new Dimension(800,600));

        canvas = new WorldCanvas();
        canvas.setBackground(new Color(0.0f,0.0f,0.3f));
        canvas.addMouseMotionListener(this);
        canvas.addMouseListener(this);
        canvas.addComponentListener(this);
        
        World.setWidth((int)800);
        World.setHeight((int)600);
        
        World.initDemo();
        World.setModel(World.createTestModel());
        
        panel.add(canvas);
        
        frame.getContentPane().add(panel);
		
        try {
        	updateModel();
        }
        catch (Exception e) {
        }
        
        World.setFitnessTest(new DistanceFitnessTest(World.getPlanet()));

        timer = new Timer(1000/Config.FRAMES_PER_SECOND,this);
        timer.start();
        
        // Pack and Display
        frame.pack();
        frame.setVisible(true);
    }

    public void actionPerformed(ActionEvent e)
    {
       String action = e.getActionCommand();

       if (e.getSource() == timer) {
    	   timerFired(e);
       }
       else if (action.equals("Open")) {
    	   openRequested();
       }
       else if (action.equals("Save")) {
    	   saveRequested();
       }
       else if (action.equals("Exit")) {
           System.exit(0);
       }
       else if (action.equals("Normal Speed")) {
    	   speed = Config.SPEED_NORMAL;
       }
       else if (action.equals("High Speed")) {
    	   speed = Config.SPEED_HIGH;
       }
       else if (action.equals("Ludicrous Speed")) {
    	   speed = Config.SPEED_LUDICROUS;
       }
       else if (action.equals("Start Evolution")) {
    	   startEvolution();
       }
       else if (action.equals("Stop Evolution")) {
    	   stopEvolution();
       }
    }
    
	public void timerFired(ActionEvent e) {
		secondsRunning++;
		
		for (int i=0; i<speed; i++) {		
			World.elapseTime(1000/Config.FRAMES_PER_SECOND);
		}
		
		if (gaManager != null && gaManager.isRunning()) {
			Model m = gaManager.getWinner();
			if (m.getName() != World.getModel().getName()) {
				System.out.println("Updating displayed model..");

				updateModel(m);
			}
			
			World.setEvolutionGeneration(gaManager.getGeneration());
		}
		
		canvas.repaint();
		//processing.update();
	}

	private void startEvolution() {
		if (gaManager != null && gaManager.isRunning()) {
			gaManager.stopTesting();
		}
		
		IFitnessTest fitnessTest = new DistanceFitnessTest();
		gaManager = new GeneticAlgorithmManager(currentModel,fitnessTest);
		
		JFileChooser winnerDirChooser = new JFileChooser();
		if (lastOpenDirectory == null) {
			lastOpenDirectory = new File(System.getProperty("user.dir"));
		}
		winnerDirChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		winnerDirChooser.setCurrentDirectory(lastOpenDirectory);
		winnerDirChooser.setDialogTitle("Save winners to");
		winnerDirChooser.setDialogType(JFileChooser.SAVE_DIALOG);
		
		int returnVal = winnerDirChooser.showOpenDialog(frame);
		
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			gaManager.setWinnersPath(winnerDirChooser.getSelectedFile().getAbsolutePath());
		}
		
		gaManager.startTesting();
	}
	
	private void stopEvolution() {
		World.setEvolutionGeneration(0);
		gaManager.stopTesting();
	}
	
	public void windowClosing(WindowEvent e) { System.exit(0); }
	public void windowClosed(WindowEvent e) {}
	public void windowOpened(WindowEvent e) {}
	public void windowIconified(WindowEvent e) {}
	public void windowDeiconified(WindowEvent e) {}
	public void windowActivated(WindowEvent e) {}
	public void windowDeactivated(WindowEvent e) {}
    public void mouseClicked(MouseEvent arg0) {}
	public void mouseEntered(MouseEvent arg0) {}
	public void mouseExited(MouseEvent arg0) {}
	public void mousePressed(MouseEvent arg0) {
		World.beginMouseDrag();
	}
	public void mouseReleased(MouseEvent arg0) {
		World.endMouseDrag();
	}
	public void mouseDragged(MouseEvent arg0) {
		World.setMousePosition(
			(double)(arg0.getX()/Config.PIXELS_PER_METER),
			(double)((arg0.getComponent().getHeight() - arg0.getY())/Config.PIXELS_PER_METER)
		);		
	}
	public void mouseMoved(MouseEvent arg0) {
		World.setMousePosition(
			(double)(arg0.getX()/Config.PIXELS_PER_METER),
			(double)((arg0.getComponent().getHeight() - arg0.getY())/Config.PIXELS_PER_METER)
		);		
	}
	
	public void componentHidden(ComponentEvent arg0) {}
	public void componentMoved(ComponentEvent arg0) {}
	public void componentResized(ComponentEvent arg0) {
		World.setWidth(arg0.getComponent().getWidth());
		World.setHeight(arg0.getComponent().getHeight());
	}
	public void componentShown(ComponentEvent arg0) {}
	
	public void openRequested() {
		JFileChooser inputFileChooser;

		try {
			inputFileChooser = new JFileChooser();

			if (lastOpenDirectory == null) {
				lastOpenDirectory = new File(System.getProperty("user.dir"));
			}
			inputFileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
			inputFileChooser.setCurrentDirectory(lastOpenDirectory);
			
			int returnVal = inputFileChooser.showOpenDialog(frame);
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				// after the file chosen, load it.
				File file = inputFileChooser.getSelectedFile();
				DefinitionReader r = new DefinitionReader(new FileReader(file.getAbsolutePath()));
				Model m = r.read();
				updateModel(m);
				lastOpenDirectory = inputFileChooser.getCurrentDirectory();
		    }
		}
		catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	public void saveRequested() {
		JFileChooser outputFileChooser;

		try {
			outputFileChooser = new JFileChooser();

			if (lastOpenDirectory == null) {
				lastOpenDirectory = new File(System.getProperty("user.dir"));
			}
			outputFileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
			outputFileChooser.setCurrentDirectory(lastOpenDirectory);
			
			int returnVal = outputFileChooser.showSaveDialog(frame);
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				// after the file chosen, load it.
				File file = outputFileChooser.getSelectedFile();
				currentModel.save(file.getAbsolutePath());
				lastOpenDirectory = outputFileChooser.getCurrentDirectory();
		    }
		}
		catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	private void updateModel() {
		try {
			World.initDemo();
			currentModel = World.getModel().clone();
			
//			if( Config.PLAY_SYNTH )
//				Synth = new SynthModelObserver(World.getModel());
		}
		catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	private void updateModel(Model m) {
		World.setModel(m.clone());
		currentModel = m;
		
//		if( Config.PLAY_SYNTH )
//			Synth.SetModel(World.getModel());		
	}
}
