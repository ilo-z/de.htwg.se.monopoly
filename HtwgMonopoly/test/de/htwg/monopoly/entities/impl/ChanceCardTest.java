package de.htwg.monopoly.entities.impl;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ChanceCardTest {

	@Before
	public void setUp() throws Exception {
		new ChanceCard("Gehe in das Gef&auml;ngnis", null, false);
	}

	@Test
	public void testCommunityCard() {
		ChanceCard card2 = new ChanceCard("Gehe in das Gef&auml;ngnis", null,
				false);
		assertEquals("Gehe in das Gef&auml;ngnis", card2.getDescription());
	}

}
