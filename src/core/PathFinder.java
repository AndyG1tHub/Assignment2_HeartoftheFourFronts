/*
 * 159.261 Games Programming - Assignment 2
 * Team members:
 * - Guo Mingqi (ID: 24009196)
 * - Yu Han (ID: 24008995)
 * - Song Pengju (ID: 24009198)
 * - Li Qianzheng (ID: 24009199)
 */

package core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

/**
 * <p>BFS-based pathfinding system that computes the shortest route from a given tile to the base, avoiding obstacles and buildings.</p>
 */
public class PathFinder {
    public List<GridPosition> findPath(GridMap map, GridPosition start, GridPosition target) {
        if (!map.isInside(start) || !map.isInside(target)) {
            return Collections.emptyList();
        }
        return search(map, start, target, false);
    }

    public List<GridPosition> findPathIgnoringBuildings(GridMap map, GridPosition start, GridPosition target) {
        if (!map.isInside(start) || !map.isInside(target)) {
            return Collections.emptyList();
        }
        return search(map, start, target, true);
    }

    private List<GridPosition> search(GridMap map, GridPosition start, GridPosition target,
            boolean ignoreBuildings) {
        Queue<GridPosition> queue = new LinkedList<GridPosition>();
        Map<GridPosition, GridPosition> cameFrom = new HashMap<GridPosition, GridPosition>();
        Set<GridPosition> visited = new HashSet<GridPosition>();
        queue.add(start);
        visited.add(start);

        while (!queue.isEmpty()) {
            GridPosition current = queue.remove();
            if (current.equals(target)) {
                return reconstructPath(cameFrom, current);
            }
            addNeighbors(map, current, target, queue, visited, cameFrom, ignoreBuildings);
        }
        return Collections.emptyList();
    }

    private void addNeighbors(GridMap map, GridPosition current, GridPosition target,
            Queue<GridPosition> queue, Set<GridPosition> visited,
            Map<GridPosition, GridPosition> cameFrom, boolean ignoreBuildings) {
        for (GridPosition next : getNeighbors(current, target)) {
            if (isValidNeighbor(map, next, target, visited, ignoreBuildings)) {
                visited.add(next);
                cameFrom.put(next, current);
                queue.add(next);
            }
        }
    }

    private boolean isValidNeighbor(GridMap map, GridPosition next, GridPosition target,
            Set<GridPosition> visited, boolean ignoreBuildings) {
        if (visited.contains(next) || !map.isInside(next)) {
            return false;
        }
        if (next.equals(target)) {
            return true;
        }
        if (ignoreBuildings) {
            return map.isWalkableIgnoringBuilding(next);
        }
        return next.equals(target) || map.isWalkable(next);
    }

    private List<GridPosition> reconstructPath(Map<GridPosition, GridPosition> cameFrom,
            GridPosition current) {
        List<GridPosition> path = new ArrayList<GridPosition>();
        path.add(current);
        while (cameFrom.containsKey(current)) {
            current = cameFrom.get(current);
            path.add(current);
        }
        Collections.reverse(path);
        return path;
    }

    private List<GridPosition> getNeighbors(GridPosition position, final GridPosition target) {
        List<GridPosition> neighbors = new ArrayList<GridPosition>();
        neighbors.add(position.add(-1, 0));
        neighbors.add(position.add(1, 0));
        neighbors.add(position.add(0, -1));
        neighbors.add(position.add(0, 1));
        Collections.sort(neighbors, new Comparator<GridPosition>() {
            public int compare(GridPosition first, GridPosition second) {
                return Double.compare(distanceToTarget(first, target), distanceToTarget(second, target));
            }
        });
        return neighbors;
    }

    private double distanceToTarget(GridPosition position, GridPosition target) {
        int rowDiff = position.row - target.row;
        int colDiff = position.col - target.col;
        return Math.sqrt(rowDiff * rowDiff + colDiff * colDiff);
    }
}
