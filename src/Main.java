import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

public class Main {

    enum gameMode {TwoPlayer, ManMachine, UndefeatableMachine}

    private static String[] board;
    private static gameMode mode;
    private static String winner;
    private static int turn = 0;
    private static final String[] symbol = new String[]{"X", "O"};
    private static final int[][] winConditions = new int[][]{{0, 1, 2}, {3, 4, 5}, {6, 7, 8}, {0, 3, 6}, {1, 4, 7}, {2, 5, 8}, {0, 4, 8}, {2, 4, 6}};

    private static Scanner in;

    public static void main(String[] args) {
        // initialize the game board
        board = new String[9];
        Arrays.fill(board, ".");

        // welcome and game setting
        in = new Scanner(System.in);
        System.out.println("Welcome to 3x3 Tic Tac Toe!");
        System.out.println();
        System.out.println(" 1 | 2 | 3 ");
        System.out.println("---+---+---");
        System.out.println(" 4 | 5 | 6 ");
        System.out.println("---+---+---");
        System.out.println(" 7 | 8 | 9 ");
        System.out.println();
        System.out.print("Please select the game mode: A) Two Players B) Man vs Machine C) Unbeatable Machine ");
        String modeChoice = in.next();
        if (modeChoice.equals("a") || modeChoice.equals("A")) {
            mode = gameMode.TwoPlayer;
            System.out.println("You chose the two players mode. Player X moves first.");
        } else if (modeChoice.equals("b") || modeChoice.equals("B")) {
            mode = gameMode.ManMachine;
            System.out.println("You chose to play against the computer.");
        } else {
            mode = gameMode.UndefeatableMachine;
            System.out.println("You chose to play against the unbeatable computer.");
        }

        // game
        while (winner == null) {
            drawBoard();
            if (mode == gameMode.ManMachine && turn % 2 == 1)
                computerMove();
            else if (mode == gameMode.UndefeatableMachine && turn % 2 == 1)
                undefeatableComputerMove();
            else userMove(turn % 2);
            checkGameState();
            turn++;
        }
        drawBoard();
        if (winner.equalsIgnoreCase("draw")) {
            System.out.println("It's a draw! Thanks for playing.");
        } else {
            System.out.println("Congratulations! " + winner + "'s player have won! Thanks for playing.");
        }
        in.close();
    }

    public static void drawBoard() {
        System.out.println();
        System.out.println(" " + board[0] + " | " + board[1] + " | " + board[2]);
        System.out.println("---+---+---");
        System.out.println(" " + board[3] + " | " + board[4] + " | " + board[5]);
        System.out.println("---+---+---");
        System.out.println(" " + board[6] + " | " + board[7] + " | " + board[8]);
        System.out.println();
    }

    public static void checkGameState() {
        for (int[] winCondition : winConditions) {
            if (!board[winCondition[0]].equals(".") && board[winCondition[0]].equals(board[winCondition[1]]) && board[winCondition[1]].equals(board[winCondition[2]])) {
                winner = board[winCondition[0]];
            }
        }
        if (turn >= 8 && winner == null) {
            winner = "draw";
        }
    }

    public static void userMove(int turn) {
        if (mode == gameMode.ManMachine) {
            System.out.print("Your turn, please enter your move (1 - 9) : ");
        } else {
            System.out.print("Player " + symbol[turn] + "'s turn, please enter your move (1 - 9) : ");
        }
        int input;
        while (true) {
            input = in.nextInt();
            if (!(input > 0 && input <= 9)) {
                System.out.println("Invalid input; re-enter your move (1 - 9) :");
            } else if (board[input - 1].equals("X") || board[input - 1].equals("O")) {
                System.out.println("Invalid input; re-enter your move (1 - 9) :");
            } else break;
        }
        board[input - 1] = symbol[turn];
        System.out.println(symbol[turn] + " makes a move to grid " + input);
    }

    public static void computerMove() {
        Random rand = new Random();
        int move;
        while (true) {
            move = rand.nextInt(10);
            if (board[move].equals(".")) {
                break;
            }
        }
        board[move] = "O";
        System.out.println("Computer's turn, its move is " + (move + 1));
    }

    /* Undefeatable move strategy with Minimax Algorithm */
    public static void undefeatableComputerMove() {
        int move = minimax("O")[0];
        board[move] = "O";
        System.out.println("Computer's turn, its move is " + (move + 1));
    }

    public static int[] minimax(String player) {
        String maxPlayer = "O";
        String nextPlayer = player.equals("O") ? "X" : "O";
        int bestMove = -1;

        checkGameState();
        if (winner != null) {
            if (winner.equals("O")) {
                return new int[]{bestMove, (9 - turn)};
            } else {
                return new int[]{bestMove, -(9 - turn)};
            }
        } else if (turn >= 9) {
            return new int[]{bestMove, 0};
        }

        int bestScore = player.equals(maxPlayer) ? Integer.MIN_VALUE : Integer.MAX_VALUE;
        for (int i = 0; i < 9; i++) {
            if (!board[i].equals(".")) continue;
            board[i] = player;
            turn++;
            int[] pos_score = minimax(nextPlayer);

            // undo
            board[i] = ".";
            turn--;
            pos_score[0] = i;
            winner = null;

            if (player.equals(maxPlayer)) {
                if (pos_score[1] > bestScore) {
                    bestScore = pos_score[1];
                    bestMove = pos_score[0];
                }
            } else {
                if (pos_score[1] < bestScore) {
                    bestScore = pos_score[1];
                    bestMove = pos_score[0];
                }
            }
        }
        return new int[]{bestMove, bestScore};
    }
}
