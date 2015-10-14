package io.github.anon10w1z.periodictable;

import javax.swing.SwingUtilities;

/**
 * Main class of the periodic table
 */
public class PeriodicTable {
	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> new PeriodicTableFrame().display());
	}
}
