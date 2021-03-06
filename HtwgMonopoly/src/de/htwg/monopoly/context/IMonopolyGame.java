package de.htwg.monopoly.context;

import de.htwg.monopoly.controller.IPlayerController;
import de.htwg.monopoly.controller.IPlayfield;
import de.htwg.monopoly.entities.impl.Dice;
import de.htwg.monopoly.entities.impl.PrisonQuestion;
import de.htwg.monopoly.util.GameStatus;

public interface IMonopolyGame {

	IPlayfield getPlayfield();

	IPlayerController getPlayerController();

	PrisonQuestion getPrisonQuestions();

	GameStatus getCurrentGamePhase();

	String getId();

	String getName();

	int getParkingMoney();

	Dice getDice();

	boolean getDrawCardFlag();

	int getDiceFlag();

	String getMessage();


    String getRev();
	/**
	 * Needs to be called every time before or after the object is going to be saved/restored 
	 */
	void makeReady(boolean bool);

}
