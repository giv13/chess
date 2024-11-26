package ru.giv13.chess;

public class Main {
    public static void main(String[] args) {
        //Board board = Board.setDefaultPosition();
        String fen =  Input.inputFEN();
        Board board = Board.fromFEN(fen);
        Game game = new Game(board);
        game.gameLoop();
    }
}