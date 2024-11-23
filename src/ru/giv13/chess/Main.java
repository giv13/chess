package ru.giv13.chess;

public class Main {
    public static void main(String[] args) {
        Board board = new Board();
        board.setDefaultPosition();

        Game game = new Game(board);
        game.gameLoop();
    }
}