package ru.giv13.chess;

import ru.giv13.chess.piece.Piece;

import java.util.Set;

public class Game {
    private final Board board;
    private final BoardRenderer renderer = new BoardRenderer();

    public Game(Board board) {
        this.board = board;
    }

    public void gameLoop() {
        renderer.render(board);
        success("The game is started");
        boolean isThereAvailableMoves = board.isThereAvailableMoves();

        while (isThereAvailableMoves) {
            Cell from = Input.inputPieceMoveFrom(board);
            Piece piece = board.getPiece(from);
            Set<Cell> availableCells = piece.getAvailableCells(board);
            renderer.render(board, availableCells);

            Cell to = Input.inputPieceMoveTo(board.turn, availableCells);
            board.movePiece(from, to);
            renderer.render(board);

            isThereAvailableMoves = board.isThereAvailableMoves();
        }

        if (board.isCheck()) {
            String color = board.turn.opposite().toString();
            success("Checkmate! " + color.charAt(0) + color.substring(1).toLowerCase() + " Win!");
        } else {
            success("Stalemate! Draw!");
        }
    }

    private static void success (String success) {
        System.out.println(BoardRenderer.SUCCESS_TEXT_COLOR + success + BoardRenderer.RESET_COLOR);
    }
}
