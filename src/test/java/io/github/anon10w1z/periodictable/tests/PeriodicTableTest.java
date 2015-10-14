package io.github.anon10w1z.periodictable.tests;

import io.github.anon10w1z.periodictable.ElementData;
import io.github.anon10w1z.periodictable.ElementData.MatterState;
import io.github.anon10w1z.periodictable.ElementData.MetallicProperties;
import org.junit.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static org.junit.Assert.*;

public class PeriodicTableTest {
	static {
		ElementData.loadElements(true);
	}

	@Test
	public void testElementData() {
		assertEquals("Returned non-empty element for null property type", ElementData.lookupElement("Hydrogen", null), ElementData.emptyElement);
		assertEquals("Unexpected atomic number for empty element", ElementData.emptyElement.atomicNumber, 0);
		assertEquals("Unexpected metallic properties for empty element", ElementData.emptyElement.metallicProperties, MetallicProperties.NOT_APPLICABLE);
		assertEquals("Unexpected matter state for empty element", ElementData.emptyElement.matterState, MatterState.NOT_APPLICABLE);
	}

	@Test
	public void testLevenshteinDistances() {
		try {
			Method levenshteinMethod = ElementData.class.getDeclaredMethod("levenshteinDistance", String.class, String.class);
			levenshteinMethod.setAccessible(true);
			String wrongDistanceMessage = "Wrong levenshtein distance returned";
			assertEquals(wrongDistanceMessage, levenshteinMethod.invoke(null, "book", "back"), 2);
			assertEquals(wrongDistanceMessage, levenshteinMethod.invoke(null, "heir", "hair"), 1);
			assertEquals(wrongDistanceMessage, levenshteinMethod.invoke(null, "superduperubercool", "supperdupperubbercewl"), 5);
		} catch (NoSuchMethodException e) {
			fail("Could not find levenshtein distance method");
		} catch (InvocationTargetException | IllegalAccessException e) {
			fail("Failed to invoke levenshtein distance method");
		}
	}
}
