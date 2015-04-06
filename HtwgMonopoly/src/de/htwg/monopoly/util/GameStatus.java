/**
 * 
 */
package de.htwg.monopoly.util;

/**
 * These enums represents the different phases of the game.
 * 
 * @author stgorenf
 *
 */
public enum GameStatus {

	/**
	 * The game is started. This event happens only one time per game, right after
	 * the game has started
	 */
	STARTED,

	/**
	 * The player is about to begin his turn (e.g. roll the dice and start his
	 * turn).
	 */
	BEFORE_TURN,

	/**
	 * The player is about to begin his turn, but is in prison
	 */
	BEFORE_TURN_IN_PRISON,

	/**
	 * The player is during his turn and is able to perform his moves (e.g. buy
	 * a street).
	 */
	DURING_TURN,

	/**
	 * The player has ended his turn.
	 */
	AFTER_TURN,

	/**
	 * The game has ended.
	 */
	STOPPED,

	/**
	 * The dice was thrown by the user.
	 */
	DICE_RESULT, 
	
	/**
	 * When the player tries to redeem himself with a dice roll.
	 */
	DICE_ROLL_FOR_PRISON,
	
	/**
	 * The game hasn't started yet and is about to begin.
	 */
	NOT_STARTED;

}
