package com.slezkin.table;

import com.slezkin.Constants;

import java.util.*;

/**
 * Class responsible for representation and interaction of all table cells with each other. It's organized via communication graph.
 */
public final class Graph {
    private final Map<Integer, List<Integer>> graph;
    private final Map<Integer, List<Integer>> reversedGraph;
    private final List<Integer> cellsInCycle;

    public Graph() {
        graph = new HashMap<>();
        reversedGraph = new HashMap<>();
        cellsInCycle = new LinkedList<>();
    }

    public void addVertex(int v) {
        if (!graph.containsKey(v)) {
            graph.put(v, new ArrayList<>());
        }
        if (!reversedGraph.containsKey(v)) {
            reversedGraph.put(v, new ArrayList<>());
        }
    }

    public void clearVertexConnection(int v) {
        if (graph.containsKey(v)) {
            List<Integer> prev = graph.get(v);
            for (int x : prev) {
                if (reversedGraph.get(x).contains(v))
                    reversedGraph.get(x).remove(Integer.valueOf(v));
            }
            prev.clear();
        }
    }

    public void addEdge(int v1, int v2) {
        addVertex(v1);
        addVertex(v2);
        if (graph.containsKey(v1)) {
            List<Integer> next = graph.get(v1);
            if (!next.contains(v2))
                next.add(v2);
        } else {
            List<Integer> next = new ArrayList<>();
            next.add(v2);
            graph.put(v1, next);
        }

        if (reversedGraph.containsKey(v2)) {
            List<Integer> prev = reversedGraph.get(v2);
            if (!prev.contains(v1))
                prev.add(v1);
        } else {
            List<Integer> prev = new ArrayList<>();
            prev.add(v1);
            reversedGraph.put(v2, prev);
        }
    }

    private List<Boolean> used;
    private List<Integer> result, order, component;

    private void dfsReversedTopologicalSort(int v) {
        used.set(v, true);
        List<Integer> list = reversedGraph.get(v);
        for (Integer to : list) {
            if (!used.get(to))
                dfsReversedTopologicalSort(to);
        }
        result.add(v);
    }

    public List<Integer> getTopologicalSort(Integer v) {
        result = new LinkedList<>();
        used = new ArrayList<>(Collections.nCopies(Constants.MAX_GRAPH_SIZE, false));
        dfsReversedTopologicalSort(v);
        return result;
    }

    private void dfsTopologicalSort(int v) {
        used.set(v, true);
        List<Integer> list = graph.get(v);
        for (Integer to : list) {
            if (!used.get(to))
                dfsTopologicalSort(to);
        }
        order.add(v);
    }

    private void dfsGetStrongComponent(int v) {
        used.set(v, true);
        component.add(v);
        List<Integer> list = reversedGraph.get(v);
        for (Integer to : list) {
            if (!used.get(to))
                dfsGetStrongComponent(to);
        }
    }

    private void dfsAllCellsInCycle(int v) {
        used.set(v, true);
        cellsInCycle.add(v);
        List<Integer> list = reversedGraph.get(v);
        for (Integer to : list) {
            if (!used.get(to))
                dfsAllCellsInCycle(to);
        }
    }

    public boolean isCellInCycle(int x) {
        return cellsInCycle.contains(x);
    }

    public void getCellsInCycle() {
        result = new LinkedList<>();
        used = new ArrayList<>(Collections.nCopies(Constants.MAX_GRAPH_SIZE, false));
        order = new LinkedList<>();
        for (Integer i : graph.keySet()) {
            if (!used.get(i))
                dfsTopologicalSort(i);
        }

        used = new ArrayList<>(Collections.nCopies(Constants.MAX_GRAPH_SIZE, false));
        Collections.reverse(order);
        component = new LinkedList<>();
        for (Integer v : order) {
            if (!used.get(v)) {
                dfsGetStrongComponent(v);
                if (component.size() > 1)
                    result.addAll(component);
                component.clear();
            }
        }

        for (Integer i : graph.keySet()) {
            if (graph.get(i).contains(i))
                result.add(i);
        }

        used = new ArrayList<>(Collections.nCopies(Constants.MAX_GRAPH_SIZE, false));
        cellsInCycle.clear();
        for (Integer v : result) {
            if (!used.get(v))
                dfsAllCellsInCycle(v);
        }
    }
}
