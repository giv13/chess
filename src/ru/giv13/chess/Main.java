package ru.giv13.chess;

public class Main {
    public static void main(String[] args) {
        int mode = Input.inputMode();
        Board board = mode == 1 ? Board.setDefaultPosition() : Board.fromFEN(Input.inputFEN());
        Game game = new Game(board);
        game.gameLoop();
    }
}