package io.github.anon10w1z.periodictable;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * Stores the data of the elements of the periodic table
 */
public class ElementData {
	public static Element emptyElement = new Element();
	public static List<Element> elements = new ArrayList<>();

	/**
	 * Loads/reloads the elements from the elements.txt file
	 * @param silent whether or not to print success message to console
	 */
	public static void loadElements(boolean silent) {
		try {
			InputStream elementDataInputStream = ElementData.class.getResourceAsStream("/elementdata.txt");
			if (elementDataInputStream == null)
				elementDataInputStream = new FileInputStream("elementdata.txt");
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(elementDataInputStream, "UTF-8"));
			int atomicNumber = 0;
			String line;
			while ((line = bufferedReader.readLine()) != null) {
				line = line.replaceAll("\\{|}", "");
				String[] properties = line.split(";");
				Element element = new Element();
				element.atomicNumber = ++atomicNumber;
				for (String property : properties) {
					String[] propertyArray = property.split(":");
					String propertyName = propertyArray[0].trim();
					String propertyValue = propertyArray[1].trim();
					Optional<Field> propertyFieldOptional = Arrays.stream(Element.class.getDeclaredFields()).filter(field -> field.getName().equals(propertyName)).findFirst();
					if (propertyFieldOptional.isPresent()) {
						Field propertyField = propertyFieldOptional.get();
						if (propertyField.getType() == int.class)
							propertyField.setInt(element, Integer.parseInt(propertyValue));
						else if (propertyField.getType() == double.class)
							propertyField.setDouble(element, Double.parseDouble(propertyValue));
						else if (propertyField.getType() == String.class)
							propertyField.set(element, propertyValue);
						else if (propertyField.getType() == MetallicProperties.class)
							propertyField.set(element, MetallicProperties.fromRealName(propertyValue));
						else if (propertyField.getType() == MatterState.class)
							propertyField.set(element, MatterState.fromRealName(propertyValue));
					}
				}
				elements.add(element);
			}
			bufferedReader.close();
			if (!silent)
				System.out.println("Loaded elements from file successfully.");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Looks up an element by property value and type
	 * @param property The value to lookup
	 * @param propertyType The type of value to lookup
	 * @return The element matching the value and value type
	 */
	public static Element lookupElement(String property, String propertyType) {
		if (property == null || property.trim().length() == 0 || propertyType == null)
			return emptyElement;
		property = property.trim();
		if (propertyType.equals("Atomic #"))
			try {
				int atomicNumber = Integer.parseInt(property);
				if (atomicNumber < 1 || atomicNumber > elements.size())
					return emptyElement;
				return elements.get(atomicNumber - 1);
			} catch (NumberFormatException ignored) {

			}

		for (Element element : elements)
			if (element.name.equalsIgnoreCase(property) && propertyType.equals("Name"))
				return element;
			else if (element.symbol.equalsIgnoreCase(property) && propertyType.equals("Symbol"))
				return element;
		if (propertyType.equals("Name")) {
			int smallestDistance = Integer.MAX_VALUE;
			Element closestElement = emptyElement;
			for (Element element : elements) {
				int distance = levenshteinDistance(element.name, property);
				if (distance < smallestDistance) {
					closestElement = element;
					smallestDistance = distance;
				}
			}
			return closestElement;
		}
		return emptyElement;
	}

	/**
	 * Returns the Levenshtein distance between the two strings
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
	 * @param e the enum
	 * @return The real name of the periodic table enum
	 */
	private static String getRealName(Enum e) {
		if (e.name().equals("NOT_APPLICABLE"))
			return "N/A";
		String name = e.name();
		return name.charAt(0) + name.substring(1, name.length()).toLowerCase().replaceAll("_", "-");
	}

	public enum MetallicProperties {
		NON_METALLIC,
		METALLOID,
		METALLIC,
		NOT_APPLICABLE;

		public static MetallicProperties fromRealName(String realName) {
			Optional<MetallicProperties> metallicProperties = Arrays.stream(values()).filter(metallicProperties1 -> getRealName(metallicProperties1).equals(realName)).findFirst();
			return metallicProperties.isPresent() ? metallicProperties.get() : null;
		}

		@Override
		public String toString() {
			return getRealName(this);
		}
	}

	public enum MatterState {
		SOLID,
		LIQUID,
		GAS,
		SYNTHETIC,
		NOT_APPLICABLE;

		public static MatterState fromRealName(String realName) {
			Optional<MatterState> matterState = Arrays.stream(values()).filter(matterState1 -> getRealName(matterState1).equals(realName)).findFirst();
			return matterState.isPresent() ? matterState.get() : null;
		}

		@Override
		public String toString() {
			return getRealName(this);
		}
	}

	/**
	 * Represents an element on the periodic table
	 */
	public static class Element {
		public int atomicNumber = 0;
		public String name = "N/A";
		public String symbol = "N/A";
		public MetallicProperties metallicProperties = MetallicProperties.NOT_APPLICABLE;
		public MatterState matterState = MatterState.NOT_APPLICABLE;
		public double atomicMass = 0;
	}
}
