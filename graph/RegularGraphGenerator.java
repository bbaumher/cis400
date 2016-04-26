package graph;

import java.util.Arrays;
import java.util.Random;
import java.util.stream.IntStream;
import utilities.IntBiConsumer;

final class RegularGraphGenerator {
  private final Random random;

  RegularGraphGenerator(Random random) {
    this.random = random;
  }

  /**
   * Generate a regular graph from an asymptotically-uniform distribution,
   * assuming d is O(n^(1/3-ε)).
   * The algorithm runs in expected time O(nd^2) (assuming the above bound
   * on d).
   *
   * This algorithm is a simplification of the one presented in <i>Generating
   * random regular graphs quickly</i>, by A. Steger and N. C. Wormald (1999).
   * The modification is that we perform phase 2 using the algorithm for
   * phase 1, since this doesn't increase the asymptotic space or time
   * requirements of the algorithm.
   */
  ReadableGraph<Integer> generateRegularGraph(int nodeCount, int degree) {
    if (nodeCount <= degree || (1 & nodeCount & degree) != 0) {
      throw new IllegalArgumentException();
    }

    while (true) {
      Graph<Integer> graph = new AdjListGraph(nodeCount);

      IntSet points = new IntSet(nodeCount * degree, true);
      IntSet remainingVertices = new IntSet(nodeCount, true);
      int[] vertexCounts = new int[nodeCount];
      Arrays.fill(vertexCounts, degree);

      // Phases 1 and 2
      int phaseThreshold = degree << 1;
      while (remainingVertices.getSize() >= phaseThreshold) {
        int point1 = points.getValue(random.nextInt(points.getSize()));
        int point2;
        do {
          point2 = points.getValue(random.nextInt(points.getSize()));
        } while (point1 == point2);
        int vertex1 = point1 / degree;
        int vertex2 = point2 / degree;
        if (isSuitablePair(points, degree, vertex1, vertex2)) {
          points.removeValue(point1);
          points.removeValue(point2);
          graph.addEdge(vertex1, vertex2);
          graph.addEdge(vertex2, vertex1);
          if (vertexCounts[vertex1]-- <= 1) {
            remainingVertices.removeValue(vertex1);
          }
          if (vertexCounts[vertex2]-- <= 1) {
            remainingVertices.removeValue(vertex2);
          }
        }
      }

      // Phase 3
      IntSet eligibleEdges =
        new IntSet(
          remainingVertices.getSize() * remainingVertices.getSize(),
          true);
      for (
        int edge = eligibleEdges.getCapacity() - 1;
        edge >= 0;
        edge -= remainingVertices.getSize() + 1)
      {
        eligibleEdges.removeValue(edge);
      }
      for (
        int index = points.getCapacity() - 2;
        index >= points.getSize();
        index -= 2)
      {
        int vertex1 = points.getValue(index) / degree;
        int vertex2 = points.getValue(index + 1) / degree;
        if (
          remainingVertices.contains(vertex1)
            && remainingVertices.contains(vertex2))
        {
          eligibleEdges
            .removeValue(
              remainingVertices.getIndex(vertex1) * remainingVertices.getSize()
                + remainingVertices.getIndex(vertex2));
          eligibleEdges
            .removeValue(
              remainingVertices.getIndex(vertex2) * remainingVertices.getSize()
                + remainingVertices.getIndex(vertex1));
        }
      }

      IntSet incompleteVertices = new IntSet(remainingVertices.getSize(), true);

      while (eligibleEdges.getSize() > 0) {
        int edge =
          eligibleEdges.getValue(random.nextInt(eligibleEdges.getSize()));
        int reducedVertex1 = edge / remainingVertices.getSize();
        int reducedVertex2 = edge % remainingVertices.getSize();
        int fullVertex1 = remainingVertices.getValue(reducedVertex1);
        int fullVertex2 = remainingVertices.getValue(reducedVertex2);
        if (
          random.nextInt(eligibleEdges.getCapacity())
            < vertexCounts[fullVertex1] * vertexCounts[fullVertex2])
        {
          eligibleEdges.removeValue(edge);
          eligibleEdges
            .removeValue(
              reducedVertex2 * remainingVertices.getSize() + reducedVertex1);
          graph.addEdge(fullVertex1, fullVertex2);
          graph.addEdge(fullVertex2, fullVertex1);
          IntBiConsumer updateVertex =
            (reducedVertex, fullVertex) -> {
              if (vertexCounts[fullVertex]-- <= 1) {
                incompleteVertices.removeValue(reducedVertex);
                for (
                  int otherVertex = remainingVertices.getSize() - 1;
                  otherVertex >= 0;
                  otherVertex--)
                {
                  eligibleEdges
                    .removeValue(
                      reducedVertex * remainingVertices.getSize()
                        + otherVertex);
                  eligibleEdges
                    .removeValue(
                      otherVertex * remainingVertices.getSize()
                        + reducedVertex);
                }
              }
            };
          updateVertex.accept(reducedVertex1, fullVertex1);
          updateVertex.accept(reducedVertex2, fullVertex2);
        }
      }

      if (incompleteVertices.getSize() <= 0) {
        return graph;
      }
    }
  }

  private boolean isSuitablePair(
    IntSet points,
    int degree,
    int vertex1,
    int vertex2)
  {
    if (vertex1 == vertex2) {
      return false;
    }
    int valueStart = vertex2 * degree;
    int valueStop = valueStart + degree;
    for (
      int point = (vertex1 + 1) * degree - 1;
      point >= vertex1 * degree;
      point--)
    {
      if (!points.contains(point)) {
        int index = points.getIndex(point);
        int value = points.getValue(index + 1 - ((index & 1) << 1));
        if (valueStart <= value && value < valueStop) {
          return false;
        }
      }
    }

    return true;
  }

  private final class IntSet {
    private final int[] values;
    private final int[] indices;
    private int size;

    private IntSet(int size, boolean startFull) {
      values = IntStream.range(0, size).toArray();
      indices = IntStream.range(0, size).toArray();
      this.size = startFull ? size : 0;
    }

    private boolean removeValue(int value) {
      return removeIndex(indices[value]);
    }

    private boolean removeIndex(int index) {
      if (size <= index) {
        return false;
      }
      else {
        swapValues(index, --size);
        return true;
      }
    }

    private boolean addValue(int value) {
      return addIndex(indices[value]);
    }

    private boolean addIndex(int index) {
      if (size > index) {
        return false;
      }
      else {
        swapValues(index, size++);
        return true;
      }
    }

    private void swapValues(int index1, int index2) {
      int value1 = values[index1];
      int value2 = values[index2];

      values[index1] = value2;
      values[index2] = value1;

      indices[value1] = index2;
      indices[value2] = index1;
    }

    private int getValue(int index) {
      return values[index];
    }

    private int getIndex(int value) {
      return indices[value];
    }

    private boolean contains(int value) {
      return indices[value] < size;
    }

    private IntStream stream() {
      return Arrays.stream(values, 0, size);
    }

    private int getSize() {
      return size;
    }

    private int getCapacity() {
      return values.length;
    }
  }
}
