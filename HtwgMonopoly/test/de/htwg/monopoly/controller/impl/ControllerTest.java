package de.htwg.monopoly.controller.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import org.junit.Before;
import org.junit.Test;

import com.google.inject.Guice;
import com.google.inject.Injector;

import de.htwg.monopoly.TestMonopolyModule;
import de.htwg.monopoly.controller.IController;

public class ControllerTest {

	private IController testController;
	ResourceBundle bundle = ResourceBundle.getBundle("Messages", Locale.GERMAN);

	@Before
	public void setUp() throws Exception {

		/* Initialization */
		Injector injector = Guice.createInjector(new TestMonopolyModule());

		testController = injector.getInstance(IController.class);

		List<String> playerList = new ArrayList<String>();
		playerList.add("0");
		playerList.add("1");
		
		testController.startNewGame(playerList);
	}

	@Test
	public void testStartTurn() {
		testController.getCurrentPlayer().setInPrison(true);
		testController.startTurn();
		assertTrue(testController.getCurrentPlayer().isInPrison());
		testController.startTurn();
	}

	@Test
	public void testRollDice() {
		testController.rollDiceToRedeem();
	}

	@Test
	public void testEndTurn() {
		testController.endTurn();
		assertEquals("1", testController.getCurrentPlayer().getName());
	}

	@Test
	public void testPerformCommCardAction() {

	}

	@Test
	public void testExitGame() {
		testController.exitGame();
	}

	@Test
	public void testBuyStreet() {
		testController.getCurrentPlayer().setPosition(1);
		assertTrue(testController.buyStreet());
		testController.getCurrentPlayer().setBudget(0);
		assertFalse(testController.buyStreet());
	}

	@Test
	public void testGetMessage() {
		testController.getMessage();
	}

	@Test
	public void testNumberOfPlayer() {
		testController.getNumberOfPlayers();
	}

	@Test
	public void testGetPlayer() {
		testController.getPlayer(0);
	}

	@Test
	public void testGetDice() {
		testController.getDice();
	}
}
