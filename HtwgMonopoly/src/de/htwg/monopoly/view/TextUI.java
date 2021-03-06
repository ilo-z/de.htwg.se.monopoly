package de.htwg.monopoly.view;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.inject.Inject;

import de.htwg.monopoly.controller.IController;
import de.htwg.monopoly.entities.impl.Player;
import de.htwg.monopoly.observer.IObserver;
import de.htwg.monopoly.util.GameStatus;
import de.htwg.monopoly.util.IMonopolyUtil;
import de.htwg.monopoly.util.MonopolyUtils;
import de.htwg.monopoly.util.UserAction;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Scanner;
import java.util.TreeMap;

public class TextUI implements IObserver {

    /* logger */
    private final Logger logger = LogManager.getLogger("htwgMonopoly");
    private Scanner in;

    /* internationalization */
    private final ResourceBundle bundle = ResourceBundle.getBundle("Messages",
            Locale.GERMAN);

    /* THE controller */
    private final IController controller;
	private boolean gameStarted;

    // Bidirectional map for user input and enum actions
    private static final BiMap<String, UserAction> ENUM_USER_OPTION = HashBiMap
            .create();
    private static final BiMap<UserAction, String> CHAR_USER_OPTION;

    static {
        ENUM_USER_OPTION.put("d", UserAction.START_TURN);
        ENUM_USER_OPTION.put("x", UserAction.SURRENDER);
        ENUM_USER_OPTION.put("b", UserAction.END_TURN);
        ENUM_USER_OPTION.put("y", UserAction.BUY_STREET);
        ENUM_USER_OPTION.put("f", UserAction.REDEEM_WITH_MONEY);
        ENUM_USER_OPTION.put("r", UserAction.ROLL_DICE);
        ENUM_USER_OPTION.put("c", UserAction.REDEEM_WITH_CARD);
        ENUM_USER_OPTION.put("w", UserAction.REDEEM_WITH_DICE);
        ENUM_USER_OPTION.put("k", UserAction.DRAW_CARD);
        ENUM_USER_OPTION.put("q", UserAction.REDEEM_WITH_QUESTION);
        ENUM_USER_OPTION.put("s", UserAction.SAVE_GAME);
        ENUM_USER_OPTION.put("l", UserAction.LOAD_GAME);
        ENUM_USER_OPTION.put("del", UserAction.DELETE_GAME);

        CHAR_USER_OPTION = ENUM_USER_OPTION.inverse();
    }

    @Inject
    public TextUI(IController controller) {
        this.controller = controller;
        controller.addObserver(this);
    }

    /**
     * Prints a welcome message and is awaiting the number and names of the
     * players from stdin. If successful, the actual game is started. FIXME:
     * This needs to happen in a Thread which can be interrupted. (Reason: If
     * the game initialization is happening in another Instance, e.g. GUI, the
     * game has to start whether the reading was successful or not).
     */
    public void startGame() {

        // print Hello screen
        printInitialisation();

        // read number and name of players from stdin
        String[] playerArray = null;
        in = new Scanner(System.in);
        if (in.hasNext()) {
        	if (gameStarted) {
        		processInputLine(in.nextLine());
        		return;
        	}
            int number = setNumberOfPlayer();
            playerArray = setNameOfPlayers(number);
        }

        logger.info(IMonopolyUtil.START);

        // start actual game
        controller.startNewGame(Arrays.asList(playerArray));
    }

    public void printInitialisation() {
        logger.info(IMonopolyUtil.GAME_NAME);
        logger.info("Herzlich Willkommen zu Monopoly!");
        logger.info("Um das Spiel zu starten, beliebigen Wert eingeben und bestätigen.");
    }

    @Override
    public void update(GameStatus phase) {
        switch (phase) {
            case NOT_STARTED:
                logger.info(controller.getMessage());
                startGame();
                break;
            case STOPPED:
                logger.info("Game is stopped.");
                break;
            case STARTED:
                printTUI();
                logger.info("Spieler " + controller.getCurrentPlayer()
                        + ". Sie sind an der Reihe.");
                printOptions();
                gameStarted = true;
                break;
            case BEFORE_TURN:
            case BEFORE_TURN_IN_PRISON:
                printMessage();
                logger.info("Spieler " + controller.getCurrentPlayer()
                        + ". Sie sind an der Reihe.");
                printOptions();
                break;
            case DURING_TURN:
                printMessage();
                onField();
                printOptions();
                break;
            case AFTER_TURN:
                printTUI();
                break;
            case DICE_RESULT:
                printRoll();
                break;
            case DICE_ROLL_FOR_PRISON:
                printMessage();
                printOptions();
                break;
        }

    }

    /**
     * print information about dice
     */
    private void printRoll() {
        String out = MessageFormat.format(bundle.getString("tui_dice"),
                controller.getDice().getDice1(), controller.getDice()
                        .getDice2());
        logger.info(out);
    }

    /**
     * Print information, where the player is currently standing on.
     */
    public void onField() {
        String currentFile = controller.getCurrentField().toString();
        String out = MessageFormat.format(bundle.getString("tui_playfield"),
                currentFile);
        logger.info(out);
    }

    /**
     * Print the options, which are available for the current player.
     */
    private void printOptions() {
        StringBuilder sb = new StringBuilder();
        sb.append(bundle.getString("tui_options"));

        for (UserAction currentOption : controller.getOptions()) {
            sb.append("\n");
            sb.append("(");
            sb.append(CHAR_USER_OPTION.get(currentOption));
            sb.append(") - ");
            sb.append(currentOption.getDescription());

        }
        logger.info(sb.toString());
    }

    /**
     * Print the event message from the controller, which describes the current
     * state of the game.
     */
    public void printMessage() {
        logger.info(controller.getMessage());
    }

    /**
     * print the game field and its properties
     */
    private void printTUI() {
        StringBuilder sb = new StringBuilder();
        StringBuilder streets = new StringBuilder();

        sb.append("\n_________________________________\n");
        sb.append(bundle.getString("player")).append("\t|Budget\t|").append(bundle.getString("ownership")).append("\n");
        sb.append("-------\t|------\t|--------------\n");
        for (int i = 0; i < controller.getNumberOfPlayers(); i++) {

            Player player = controller.getPlayer(i);
            sb.append(player.getName()).append("\t|").append(player.getBudget()).append("\t|").append(player.getOwnership()).append("\n");
        }

        int z = IMonopolyUtil.TUI_HIGH;
        String[] zeichen = new String[z];
        z = 0;
        zeichen[z] = "|-------";
        zeichen[++z] = "|___x___";
        zeichen[++z] = "|       ";
        zeichen[++z] = "|_______";

        String x = "x";
        for (int zeile = 0; zeile < zeichen.length - 1; zeile++) {
            sb.append("\n");
            for (int i = 0; i < controller.getFieldSize(); i++) {
                if (zeile == 1) {
                    zeichen[1] = zeichen[1].replace(x, "" + i);
                    x = "" + i;
                }
                sb.append(zeichen[zeile]);
            }
            sb.append("|");
        }
        for (int i = 0; i < controller.getFieldSize(); i++) {
            streets.append(i).append("=")
                    .append(controller.getFieldAtIndex(i).toString())
                    .append("\n");
        }

        sb.append("\n").append(streets);
        logger.info(sb.toString());

    }

    /**
     * handle user input
     *
     * @param line a char indicating the option
     * @return false, if the player has ended the game ('x'), true otherwise.
     */
    public boolean processInputLine(String line) {

        // map the input from stdin with an Enum
        UserAction choosedOption = ENUM_USER_OPTION.get(line);

        if (choosedOption == null) {
            // wrong input, option not mapped.
            logger.info(bundle.getString("tui_wrong_input"));
            return true;
        }

        if (!controller.isCorrectOption(choosedOption)) {
            // wrong input, option not available
            logger.info(bundle.getString("tui_wrong_input"));
            return true;
        }

        // controller.performAction(choosedOption); maybe in the future..

        // perform action according to the input of the user
        switch (choosedOption) {
            case START_TURN:
                controller.startTurn();
                break;
            case END_TURN:
                controller.endTurn();
                break;
            case BUY_STREET:
                controller.buyStreet();
                break;
            case REDEEM_WITH_MONEY:
                controller.redeemWithMoney();
                break;
            case REDEEM_WITH_CARD:
                controller.redeemWithCard();
                break;
            case REDEEM_WITH_DICE:
                controller.redeemWithDice();
                break;
            case ROLL_DICE:
                controller.rollDiceToRedeem();
                break;
            case SURRENDER:
                // for now the game finishes completely
                logger.info("Spiel beendet!");
                controller.exitGame();
                return false;
            case DRAW_CARD:
                controller.drawCard();
                break;
            case REDEEM_WITH_QUESTION:
                // display question and wait for answer of the user
                logger.info(bundle.getString("tui_answer_prison_question"));
                logger.info(controller.getPrisonQuestion());
                controller.checkPlayerAnswer(retrieveAnswer());
                break;
            case LOAD_GAME:
                loadGame();
                break;
            case SAVE_GAME:
                saveGame();
                break;
            case DELETE_GAME:
                deleteGame();
                break;
        }

        // always return true in order to keep the game going.
        return true;
    }

    private void loadGame() {
        performDbAction("load");
    }

    private void deleteGame() {
        performDbAction("delete");
    }

    private void saveGame() {

        logger.info("Name f�r das aktuelle Spiel vergeben:");

        String name = in.nextLine();

        try {
            controller.saveGameToDB(name);
        } catch (IllegalAccessException e) {
            logger.info("Nicht m�glich das Spiel zu speichern."
                    + e.getMessage());
        }

    }

    private void performDbAction(String action) {
        Map<String, String> savedGames = controller.getSavedGames();
        Map<Integer, String> tuiSavedGames = new TreeMap<Integer, String>();
        int size = savedGames.size();
        int max = IMonopolyUtil.MAX_NUMBER_GAMES_TO_DISPAY;

        if (savedGames.isEmpty()) {
            logger.info("Keine gespeicherten Spiele");
            return;
        }

        // print saved games
        logger.info("Folgende Spiele sind gespeichert: (" + size + ")");
        int i = 0;
        for (String currentID : savedGames.keySet()) {
            i++;
            tuiSavedGames.put(i, currentID);
            logger.info(i + ": " + savedGames.get(currentID));

            // stop every 10 items
            if ((i % max) == 0) {
                logger.info("N�chste " + ((size - i) > max ? max : (size - i))
                        + " Eintr�ge zeigen? (y/n).");
                if (!retrieveAnswer()) {
                    break;
                }
            }
        }

        // o_O that do-while-while block needs to be verified
        int id;
        do {
            logger.info("Korrekte Spielnummer ausw�hlen");

            // if next token is not an integer
            while (!in.hasNextInt()) {
                logger.info("Bitte eine Nummer eingeben.");
                in.next();
            }

            id = in.nextInt();

            // if read integer is not valid
        } while (id > size || id < 1);

        if (action.equals("load")) {
            controller.loadGameFromDB(tuiSavedGames.get(id));
        } else if (action.equals("delete")) {
            controller.deleteGame(tuiSavedGames.get(id));
        }
    }

    /**
     * Method retrieves an answer written to stdout. Either yes, y, no or n.
     *
     * @return
     */
    private boolean retrieveAnswer() {
        String answer = in.nextLine();
        while (!answer.matches("y|n|yes|no")) {
            logger.info(bundle.getString("tui_wrong_input"));
            answer = in.nextLine();
        }
        return answer.matches("y|yes");
    }

    /**
     * function to read number of player
     */
    private int readNumberOfPlayer() {

        int tmpNumberOfPlayer = 0;

        if (in.hasNext()) {
            // check if input is an integer
            if (in.hasNextInt()) {
                tmpNumberOfPlayer = in.nextInt();
                in.nextLine();
            } else {
                in.nextLine();
                return 0;
            }
        }

        // check if input is smaller than the maximum of player and bigger than
        // the minimum
        if (!MonopolyUtils.verifyPlayerNumber(tmpNumberOfPlayer)) {
            return 0;
        }

        // if scanned number is correct, save it
        return tmpNumberOfPlayer;
    }

    private int setNumberOfPlayer() {
        logger.info(IMonopolyUtil.Q_NUMBER_OF_PLAYER);
        int readNumber = readNumberOfPlayer();
        while (readNumber == 0) {
            logger.info(IMonopolyUtil.ERR_NUMBER_OF_PLAYER);
            readNumber = readNumberOfPlayer();
        }
        return readNumber;
    }

    private String[] setNameOfPlayers(int numberOfPlayer) {
        String[] configNameOfPlayer = new String[numberOfPlayer];
        for (int i = 0; i < numberOfPlayer; i++) {
            logger.info("Player " + (i + 1) + " " + IMonopolyUtil.Q_NAME_PLAYER);
            if (in.hasNext()) {
                configNameOfPlayer[i] = in.nextLine();
            }

        }
        return configNameOfPlayer;
    }

}
