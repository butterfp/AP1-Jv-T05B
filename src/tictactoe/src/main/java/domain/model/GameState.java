package domain.model;


public enum GameState {

    WAITING,
    X_TURN,
    O_TURN,
    DRAW,
    WIN_X,
    WIN_O;

    public boolean isFinished() {
        return this == DRAW || this == WIN_X || this == WIN_O;
    }

    public boolean isActive() {
        return this == X_TURN || this == O_TURN;
    }
}
