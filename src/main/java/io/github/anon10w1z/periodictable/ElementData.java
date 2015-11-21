package io.github.anon10w1z.periodictable;

import org.apache.commons.io.FileUtils;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Stores the data of the elements of the periodic table
 */
public class ElementData {
	public static final Element emptyElement = new Element();
	public static List<Element> elements = new ArrayList<>();

	/**
	 * Loads/reloads the elements from the elements.ser file
	 *
	 * @param silent whether or not to print success message to console
	 */
	@SuppressWarnings("unchecked")
	public static void loadElements(boolean silent) {
		try {
			try {
				FileUtils.copyURLToFile(new URL("https://dl.dropboxusercontent.com/u/76347756/elements.ser"), new File("elements.ser"));
				System.out.println("Downloaded elements file successfully.");
			} catch (Exception e2) {
				System.out.println("Failed to download elements file.");
			}
			InputStream elementDataInputStream = new FileInputStream("elements.ser");
			ObjectInput objectInput = new ObjectInputStream(new BufferedInputStream(elementDataInputStream));
			elements = (List<Element>) objectInput.readObject();
			objectInput.close();
			if (!silent)
				System.out.println("Loaded elements from elements file successfully.");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Looks up an element by property value and type
	 *
	 * @param property     The value to lookup
	 * @param propertyType The type of value to lookup
	 * @return The element matching the value and value type
	 */
	public static Element lookupElement(String property, PropertyType propertyType) {
		if (property == null || property.trim().length() == 0 || propertyType == null)
			return emptyElement;
		property = property.trim();
		if (propertyType == PropertyType.ATOMIC_NUMBER)
			try {
				int atomicNumber = Integer.parseInt(property);
				if (atomicNumber < 1 || atomicNumber > elements.size())
					return emptyElement;
				return elements.get(atomicNumber - 1);
			} catch (NumberFormatException ignored) {

			}
		for (Element element : elements)
			if (element.name.equalsIgnoreCase(property) && propertyType == PropertyType.NAME)
				return element;
			else if (element.symbol.equalsIgnoreCase(property) && propertyType == PropertyType.SYMBOL)
				return element;
		return emptyElement;
	}

	/**
	 * Finds the element with the closest name to the specified element name
	 *
	 * @param elementName The element name
	 * @return The element with the closest name to the specified element name
	 */
	public static Element findClosestElement(String elementName) {
		int smallestDistance = Integer.MAX_VALUE;
		Element closestElement = emptyElement;
		for (Element element : elements) {
			int distance = levenshteinDistance(element.name, elementName);
			if (distance < smallestDistance) {
				closestElement = element;
				smallestDistance = distance;
			}
		}
		return closestElement;
	}

	/**
	 * Returns the Levenshtein distance between the two strings
	 *
	 * @param string1 The first string
	 * @param string2 The second string
	 * @return The Levenshtein distance between the two strings
	 */
	private static int levenshteinDistance(String string1, String string2) {
		if (string1 == null || string2 == null)
			return Integer.MAX_VALUE;
		string1 = string1.toLowerCase();
		string2 = string2.toLowerCase();
		int[] costs = new int[string2.length() + 1];
		for (int j = 0; j < costs.length; j++)
			costs[j] = j;
		for (int i = 1; i <= string1.length(); i++) {
			costs[0] = i;
			int nw = i - 1;
			for (int j = 1; j <= string2.length(); j++) {
				int cj = Math.min(1 + Math.min(costs[j], costs[j - 1]), string1.charAt(i - 1) == string2.charAt(j - 1) ? nw : nw + 1);
				nw = costs[j];
				costs[j] = cj;
			}
		}
		return costs[string2.length()];
	}

	/**
	 * Gets the real name of a periodic table enum
	 *
	 * @param e the enum
	 * @return The real name of the periodic table enum
	 */
	private static String getRealName(Enum e) {
		if (e.name().equals("NOT_APPLICABLE"))
			return "N/A";
		String name = e.name();
		return name.charAt(0) + name.substring(1, name.length()).toLowerCase().replaceAll("_", "-");
	}

	public enum PropertyType {
		NAME,
		ATOMIC_NUMBER,
		SYMBOL
	}

	@SuppressWarnings("unused")
	public enum MetallicProperties {
		NON_METALLIC,
		METALLOID,
		METALLIC,
		NOT_APPLICABLE;

		@Override
		public String toString() {
			return getRealName(this);
		}
	}

	@SuppressWarnings("unused")
	public enum MatterState {
		SOLID,
		LIQUID,
		GAS,
		SYNTHETIC,
		NOT_APPLICABLE;

		@Override
		public String toString() {
			return getRealName(this);
		}
	}

	/**
	 * Represents an element on the periodic table
	 */
	public static class Element implements Serializable {
		public int atomicNumber = 0;
		public String name = "N/A";
		public String symbol = "N/A";
		public MetallicProperties metallicProperties = MetallicProperties.NOT_APPLICABLE;
		public MatterState matterState = MatterState.NOT_APPLICABLE;
		public double atomicMass = 0;
	}
}
