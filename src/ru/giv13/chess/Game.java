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
        success("The game is started! Type \"fen\" during the game to get board position.");

        while (true) {
            if (board.halfmoveClock >= 100) {
                success("Draw (fifty-move rule)!");
                break;
            }

            if (board.isDeadPosition()) {
                success("Draw (dead position)!");
                break;
            }

            if (!board.isThereAvailableMoves()) {
                if (board.isCheck()) {
                    String color = board.turn.opposite().toString();
                    success("Checkmate! " + color.charAt(0) + color.substring(1).toLowerCase() + " win!");
                } else {
                    success("Draw (stalemate)!");
                }
                break;
            }

            Cell from = Input.inputPieceMoveFrom(board);
            Piece piece = board.getPiece(from);
            Set<Cell> availableCells = piece.getAvailableCells(board);
            renderer.render(board, availableCells);

            Cell to = Input.inputPieceMoveTo(board, availableCells);
            board.movePiece(from, to);
            renderer.render(board);
        }
    }

    private static void success (String success) {
        System.out.println(BoardRenderer.SUCCESS_TEXT_COLOR + success + BoardRenderer.RESET_COLOR);
    }
}
