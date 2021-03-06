package de.htwg.monopoly.util;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author Steffen
 *
 */
public class UserActionTest {

	private UserAction action;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		action = UserAction.BUY_STREET;
		
	}

	/**
	 * Test method for {@link de.htwg.monopoly.util.UserAction#getDescription()}.
	 */
	@Test
	public void testGetDescription() {
		assertEquals("Stra&szlig;e kaufen", action.getDescription());
	}

}
