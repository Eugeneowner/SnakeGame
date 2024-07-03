package snake.client;

import com.codenjoy.dojo.client.Solver;
import com.codenjoy.dojo.client.WebSocketRunner;
import com.codenjoy.dojo.services.Direction;
import com.codenjoy.dojo.services.Point;
import com.google.common.collect.Iterables;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

public class YourSolver implements Solver<Board> {
    private final int width = 15;
    private final int height = 15;
    private final int maxLength = 32;

    Direction doSolve(Board board) {
        Lee lee = new Lee(width, height);

        List<Point> barriers = board.getBarriers();
        List<Point> snake = board.getSnake();

        Point apple = board.getApples().get(0);
        Point head = board.getHead();
        Optional<Point> stone = board.getStones().stream().findFirst();

        boolean isBiggerThanMaxLength = stone.isPresent() && snake.size() > maxLength;

        Optional<Iterable<Point>> trace = lee.trace(head, isBiggerThanMaxLength ? stone.get() : apple, new HashSet<>(barriers));
        if (trace.isEmpty()) {
            if (stone.get().getX() < head.getX()) return Direction.LEFT;
            else if (stone.get().getX() > head.getX()) return Direction.RIGHT;
            else if (stone.get().getY() > head.getY()) return Direction.UP;
            else return Direction.DOWN;
        }
        Point currentPosition = Iterables.get(trace.get(), 0);
        Point nextPosition = Iterables.get(trace.get(), 1);

        if (currentPosition.getX() == nextPosition.getX() && currentPosition.getY() < nextPosition.getY())
            return Direction.UP;
        if (currentPosition.getX() == nextPosition.getX() && currentPosition.getY() > nextPosition.getY())
            return Direction.DOWN;
        if (currentPosition.getX() < nextPosition.getX() && currentPosition.getY() == nextPosition.getY())
            return Direction.RIGHT;
        if (currentPosition.getX() > nextPosition.getX() && currentPosition.getY() == nextPosition.getY())
            return Direction.LEFT;

        return null;
    }

    @Override
    public String get(Board board) {
        return doSolve(board).toString();
    }

    public static void main(String[] args) {
        String url = "http://138.197.189.109/codenjoy-contest/board/player/t60wibpvyzlaov6pl8s4?code=795927836493004451";

        WebSocketRunner.runClient(
                url,
                new YourSolver(),
                new Board());
    }

}
