package io.github.anon10w1z.periodictable;

import io.github.anon10w1z.periodictable.ElementData.Element;

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Enumeration;

/**
 * The periodic table frame
 */
public class PeriodicTableFrame extends JFrame {
	/**
	 * Constructs a new periodic table frame
	 */
	public PeriodicTableFrame() {
		super("Periodic Table");
		this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}
		this.setLayout(new BoxLayout(this.getContentPane(), BoxLayout.PAGE_AXIS));
		JPanel propertyTypeOptionsPanel = new JPanel();
		ButtonGroup propertyTypeOptions = new ButtonGroup();
		JRadioButton atomicNumberOptionButton = new JRadioButton("Atomic #");
		propertyTypeOptions.add(atomicNumberOptionButton);
		propertyTypeOptionsPanel.add(atomicNumberOptionButton);
		JRadioButton nameOptionButton = new JRadioButton("Name");
		propertyTypeOptions.add(nameOptionButton);
		propertyTypeOptionsPanel.add(nameOptionButton);
		JRadioButton symbolOptionButton = new JRadioButton("Symbol");
		propertyTypeOptions.add(symbolOptionButton);
		propertyTypeOptionsPanel.add(symbolOptionButton);
		this.add(propertyTypeOptionsPanel);

		JPanel lookupPanel = new JPanel();
		JTextField propertyField = new JTextField(15);
		lookupPanel.add(propertyField);
		JButton lookupButton = new JButton("Lookup Element");
		lookupPanel.add(lookupButton);
		JButton reloadElementsButton = new JButton("Reload Elements");
		reloadElementsButton.addMouseListener(new MouseListener() {
			@Override
			public void mouseClicked(MouseEvent e) {

			}

			@Override
			public void mousePressed(MouseEvent e) {

			}

			@Override
			public void mouseReleased(MouseEvent e) {
				ElementData.loadElements(true);
				showInformationMessage("Reloaded elements from elements.txt");
			}

			@Override
			public void mouseEntered(MouseEvent e) {

			}

			@Override
			public void mouseExited(MouseEvent e) {

			}
		});
		lookupPanel.add(reloadElementsButton);
		this.add(lookupPanel);

		JPanel outputPanel = new JPanel();
		outputPanel.setLayout(new BoxLayout(outputPanel, BoxLayout.PAGE_AXIS));
		JTextPane atomicNumberTextPane = createUneditableTextPane("Atomic #: ");
		outputPanel.add(atomicNumberTextPane);
		JTextPane nameTextPane = createUneditableTextPane("Name: ");
		outputPanel.add(nameTextPane);
		JTextPane symbolTextPane = createUneditableTextPane("Symbol: ");
		outputPanel.add(symbolTextPane);
		JTextPane metallicPropertiesTextPane = createUneditableTextPane("Metallic Properties: ");
		outputPanel.add(metallicPropertiesTextPane);
		JTextPane matterStateTextPane = createUneditableTextPane("Matter State: ");
		outputPanel.add(matterStateTextPane);
		JTextPane atomicMassTextPane = createUneditableTextPane("Atomic Mass: ");
		outputPanel.add(atomicMassTextPane);
		lookupButton.addMouseListener(new MouseListener() {
			@Override
			public void mouseClicked(MouseEvent e) {

			}

			@Override
			public void mousePressed(MouseEvent e) {

			}

			@SuppressWarnings("ResultOfMethodCallIgnored")
			@Override
			public void mouseReleased(MouseEvent e) {
				String propertyType = "";
				Enumeration<AbstractButton> buttons = propertyTypeOptions.getElements();
				while (buttons.hasMoreElements()) {
					AbstractButton button = buttons.nextElement();
					if (button.isSelected())
						propertyType = button.getText();
				}
				if (propertyType.equals(""))
					showInformationMessage("Please select the property type for lookup.");
				else if (propertyField.getText().trim().isEmpty())
					showInformationMessage("Please enter a value to lookup.");
				else {
					Element element = ElementData.lookupElement(propertyField.getText(), propertyType);
					if (propertyType.equals("Name") || propertyType.equals("Symbol") && !propertyField.getText().trim().equalsIgnoreCase(element.name)) {
						try {
							Integer.parseInt(propertyField.getText().trim());
							showInformationMessage("Element not found.\nDid you mean to lookup by atomic number?");
						} catch (NumberFormatException e1) {
							showInformationMessage("Element not found.\nDid you mean to lookup " + element.name + '?');
						}
						element = ElementData.emptyElement;
					} else if (propertyType.equals("Symbol") && !propertyField.getText().trim().equalsIgnoreCase(element.symbol)) {
						try {
							Integer.parseInt(propertyField.getText().trim());
							showInformationMessage("Element not found.\nDid you mean to lookup by atomic number?");
						} catch (NumberFormatException ignored) {

						}
						element = ElementData.emptyElement;
					} else if (element == ElementData.emptyElement)
						showInformationMessage("Element not found.");
					atomicNumberTextPane.setText("Atomic #: " + (element.atomicNumber < 1 ? "N/A" : element.atomicNumber));
					nameTextPane.setText("Name: " + element.name);
					symbolTextPane.setText("Symbol: " + element.symbol);
					metallicPropertiesTextPane.setText("Metallic Properties: " + element.metallicProperties);
					matterStateTextPane.setText("Matter State: " + element.matterState);
					atomicMassTextPane.setText("Atomic Mass: " + (element.atomicMass == 0 ? "N/A" : element.atomicMass));
				}
				PeriodicTableFrame.this.pack();
			}

			@Override
			public void mouseEntered(MouseEvent e) {

			}

			@Override
			public void mouseExited(MouseEvent e) {

			}
		});
		this.add(outputPanel);
	}

	/**
	 * Displays this periodic table frame
	 */
	public void display() {
		ElementData.loadElements(false);
		this.pack();
		this.setLocationRelativeTo(null);
		this.setVisible(true);
	}

	/**
	 * Shows the specified information message in a message dialog box
	 *
	 * @param message The message
	 */
	private void showInformationMessage(String message) {
		JOptionPane.showMessageDialog(this, message, "Periodic Table", JOptionPane.INFORMATION_MESSAGE);
	}

	/**
	 * Creates an uneditable text pane with the specified text
	 *
	 * @param text The text to set the text pane text to
	 * @return An uneditable text pane with the specified text
	 */
	private JTextPane createUneditableTextPane(String text) {
		JTextPane textPane = new JTextPane();
		textPane.setText(text);
		textPane.setEditable(false);
		return textPane;
	}
}
