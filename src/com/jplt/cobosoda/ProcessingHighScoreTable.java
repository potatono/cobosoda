/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jplt.cobosoda;

import java.util.ArrayList;
import processing.core.*;

/**
 *
 * @author Jacob Joaquin
 */
public class ProcessingHighScoreTable extends ProcessingMode {

	private ArrayList<ProcessingModelScore> highScoreTable;
	private ArrayList<ModelAndScore> modelAndScore;

	public ProcessingHighScoreTable(PApplet papplet, ProcessingInterface proc,
			ArrayList<ProcessingModelScore> scoreTable, int start) {
		super(papplet, proc);

		highScoreTable = scoreTable;
		modelAndScore = new ArrayList<ModelAndScore>();

		for (int i = start; i < start + 5; i++) {
			if (highScoreTable.get(i) == null) {
				break;
			}

			ProcessingModelScore s = highScoreTable.get(i);
			Model m = s.getModel();
			modelAndScore.add(new ModelAndScore(m, s));
			}
	}

	@Override
	public void display() {
		drawModels();
	}

	public void drawModels() {
		for (ModelAndScore ms : modelAndScore) {
			int i = modelAndScore.indexOf(ms);
			Model m = ms.getModel();

			PMethod.drawModel(m, 1, 100 + i * 110, pa, 0.3);
			String s = createFormattedRank(ms);

			PMethod.writeCenteredText(pa, s, 400,
					118 + i * 110, 20, pi.font18, 18);
			m.elapseTime(1000 / Config.FRAMES_PER_SECOND);

//			PMethod.writeCenteredText(pa, "DISTAINCE:   " + ms.getScore().getDistance(), 400,
//					138 + i * 110, 20, pi.font18, 18);
		}
	}

	public String createFormattedRank(ModelAndScore ms) {
		int length = "#001 JTJ    0123456789abcdef.000".length();
		Model m = ms.getModel();
		ProcessingModelScore s = ms.getScore();
		int rank = highScoreTable.indexOf(s) + 1;
		String initials = m.getCreatorInitials();
		String name = m.getName();
		int mutation = m.getMutationIndex();
		StringBuffer sb = new StringBuffer(length);

		sb.append("#");
		sb.append(String.format("%03d", rank));
		sb.append(" ");
		
		/* Ensure initial length == 3 spaces, convert x to " " */
		while (initials.length() < 3) {
			initials = initials + " ";
		}

		/* Continue writing shitty code here */
		StringBuffer newInitials = new StringBuffer(3);
		
		for (int i = 0; i < 3; i++) {
			if (initials.substring(i, i + 1).equals("x")) {
				newInitials.append(" ");
			} else {
				newInitials.append(initials.substring(i, i + 1));
			}
		}
		
		initials = newInitials.toString();
		sb.append(initials);
		
		sb.append("    ");

		String foo = PMethod.substituteDelimiter(name);

		if (mutation > 0) {
			foo = foo + "." + String.format("%03d", mutation);
		}

		for (int i = 0; i < 16 - foo.length(); i++) {
			sb.append(" ");
		}

		sb.append(foo);

		return sb.toString();
	}

	public class ModelAndScore {
		private Model model;
		private ProcessingModelScore score;
		private String s;

		public ModelAndScore(Model m, ProcessingModelScore s) {
			model = m;
			score = s;
		}

		public Model getModel() {
			return model;
		}

		public ProcessingModelScore getScore() {
			return score;
		}
	}
}
