package domain.model;

import java.util.Arrays;


public record GameBoard(int[][] board) {

    public static final int SIZE = 3;

    public static final int EMPTY = 0;

    public static final int X = 1;

    public static final int O = 2;

    public GameBoard {
        if (board == null || board.length != SIZE) {
            throw new IllegalArgumentException("Поле должно быть квадратным " + SIZE + "x" + SIZE);
        }
    }

    public GameBoard() {
        this(new int[SIZE][SIZE]);
    }


    public static int opponentOf(int mark) {
        return mark == X ? O : X;
    }


    public boolean hasEmptyCell() {
        for (int[] row : board) {
            for (int cell : row) {
                if (cell == EMPTY) {
                    return true;
                }
            }
        }
        return false;
    }


    public boolean isValidMove(GameBoard updatedBoard) {
        int oldX = 0, oldO = 0;
        int newX = 0, newY = 0;

        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                int oldValue = board[i][j];
                int newValue = updatedBoard.board()[i][j];

                if (oldValue != EMPTY && oldValue != newValue) {
                    return false;
                }
                if (newValue == X) newX++;
                if (newValue == O) newY++;
                if (oldValue == X) oldX++;
                if (oldValue == O) oldO++;
            }
        }

        return (newX + newY) == (oldX + oldO + 1)
                && Math.abs(newX - newY) <= 1;
    }


    public boolean isWinner(int mark) {
        for (int i = 0; i < SIZE; i++) {
            if (board[i][0] == mark && board[i][1] == mark && board[i][2] == mark) {
                return true;
            }
            if (board[0][i] == mark && board[1][i] == mark && board[2][i] == mark) {
                return true;
            }
        }
        return (board[0][0] == mark && board[1][1] == mark && board[2][2] == mark)
                || (board[0][2] == mark && board[1][1] == mark && board[2][0] == mark);
    }


    @Override
    public String toString() {
        return Arrays.deepToString(board);
    }
}
