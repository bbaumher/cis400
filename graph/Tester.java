package graph;

import graph.ProbabilityDistributionAlgorithm.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import matchings.Matching;
import matchings.MatchingGraphGenerator;
import utilities.StreamUtilities;

public class Tester {
	private static final Random RANDOM = new Random();
	
	public static void main(String[] args) {
		compareAlgorithms(10);
	}
	
	/** Run the algorithm on a test graph.
	 */
	public static void main4(String[] args) {
		GraphGenerator gg = new StandardGraphGenerator(RANDOM);
		Graph<Integer> testGraph;
		int k = 3;

    System.out.println("Iteration\tStandard cover time\tMixed cover time");
    NodeCoverRunner standardRunner =
      new NodeCoverRunner(
        RANDOM,
        ProbabilityDistributionAlgorithm::calculateCredits4,
        k,
        Method.CLONING
      );
    NodeCoverRunner mixedRunner =
      new NodeCoverRunner(
        RANDOM,
        ProbabilityDistributionAlgorithm.getSimpleMixedCalculator(0.5),
        k,
        Method.CLONING);
		//System.out.println(testGraph);
		//System.out.println(SCCTester.isStronglyConnected(testGraph));
		for (int i = 1; i <= 100; i++) {
			testGraph = gg.generateAdjListGraph(2000, 0.0010, true);
      ReadableNode<Integer> startNode = testGraph.getNodes().findAny().get();
			int standardCoverTime =
				standardRunner.getCoverTime(testGraph, startNode);
      int mixedCoverTime =
        mixedRunner.getCoverTime(testGraph, startNode);
			System.out.println(i + "\t" + standardCoverTime + '\t' + mixedCoverTime);
		}
	}
	
	public static void main3(String[] args) {
		//create nodes
		int s = 0;
		int a = 1;
		int b = 2;
		int c = 3;
		
		//create graph
		AdjListGraph testGraph = new AdjListGraph(4);
		
		//add edges to the graph
		testGraph.addEdge(s,a);
		testGraph.addEdge(a,b);
		testGraph.addEdge(b,c);
		testGraph.addEdge(c,s);
		
		int counter = 0;
		Iterator<ReadableGraph<Integer>> iter =
      SCCTester.getStronglyConnectedComponents(testGraph);
		while (iter.hasNext()) {
			ReadableGraph<Integer> g = iter.next();
			System.out.println("graph " + counter + ": ");
			g.printGraph();
			counter++;
		}
		System.out.println(iter + " graphs in total");
	}
	
	/** Run the algorithm on a test graph.
	 */
	public static void main2(String[] args) {
		//create nodes
		int s = 0;
		int a = 1;
		int b = 2;
		int c = 3;
		int d = 4;
		int e = 5;
		int f = 6;
		int g = 7;
		int h = 8;
		int i = 9;
		int j = 10;
		int k = 11;
		
		//create graph
		AdjListGraph testGraph = new AdjListGraph(12);
		
		//add edges to the graph
		testGraph.addEdge(s,a);
		testGraph.addEdge(s,b);
		testGraph.addEdge(s,c);
		testGraph.addEdge(s,d);
		testGraph.addEdge(a,e);
		testGraph.addEdge(a,g);
		testGraph.addEdge(b,e);
		testGraph.addEdge(b,f);
		testGraph.addEdge(c,d);
		testGraph.addEdge(c,h);
		testGraph.addEdge(c,i);
		testGraph.addEdge(d,h);
		testGraph.addEdge(e,j);
		testGraph.addEdge(g,j);
		testGraph.addEdge(i,k);
		
		Node<Integer> sNode = testGraph.getNode(s);
		Iterator<Node<Integer>> iter = testGraph.getDFSIterator(sNode);
		
		while (iter.hasNext()) {
			Node<Integer> node = iter.next();
			System.out.println(node);
		}
		
		Graph<Integer> graph =
			new StandardGraphGenerator(RANDOM).generateAdjListGraph(20, 0.2, true);
		graph.printGraph();
		ReadableGraph<Integer> largestComponent =
      getLargestStronglyConnectedComponent(graph);
		
		largestComponent.printGraph();
		
		//run the algorithm
		TransitionMatrix<Integer> transitionMatrix =
			ProbabilityDistributionAlgorithm
				.getTransitionMatrix(
					largestComponent,
					3,
					ProbabilityDistributionAlgorithm::calculateCredits,
          Method.CLONING);
		
		//display the output
		print(transitionMatrix.getTransitionVectors());
		
		System.out.println(
			ConvergenceTester.forTransitionMatrix(transitionMatrix)
				.logStepsForConvergence(0.125));
		
		ConvergenceTester<Integer> convergenceTester =
			ConvergenceTester.forTransitionMatrix(transitionMatrix);
			
		int iteration = 1;
		while (true) {
			convergenceTester.iterateDistributions(1);
			System.out.print("2^");
			System.out.print(iteration++);
			System.out.print(" iterations; distance ");
			System.out.println(convergenceTester.convergenceDistance());
			print(convergenceTester.getTransitionMatrix().getTransitionVectors()
			);
		}
	}
	
	private static void print(double[][] transitionVectors) {
		Arrays.stream(transitionVectors)
			.map(
				transitionVector -> {
					Iterable<String> iterable =
						Arrays.stream(transitionVector)
								.mapToObj(Double::toString)
							::iterator;
					return String.join(" ", iterable);
				}
			)
			.forEachOrdered(System.out::println);
		System.out.println();
	}
	
	/**
	 * Return a {@link Graph} representing the largest strongly-connected
	 * component of the specified {@link Graph}.
	 * 
	 * @param graph The original graph.
	 * @return {@code graph}'s largest strongly connected component.
	 */
	private static <T> ReadableGraph<T> getLargestStronglyConnectedComponent(
    ReadableGraph<T> graph)
  {
		Iterable<ReadableGraph<T>> iterable =
			() -> SCCTester.getStronglyConnectedComponents(graph);
		return
			StreamSupport.stream(iterable.spliterator(), false)
				.sorted(
					(component1, component2) ->
						Integer
							.compare(
								component2.getNodeCnt(),
								component1.getNodeCnt()))
				.findFirst()
				.get();
	}
	
	private static void testZeroWeights() {
		Graph<Integer> graph = new AdjListGraph(4);
		graph.addEdge(0, 1);
		graph.addEdge(0, 2);
		graph.addEdge(1, 2);
		graph.addEdge(2, 3);
		graph.addEdge(3, 0);
		TransitionMatrix<Integer> transitionMatrix =
			ProbabilityDistributionAlgorithm
				.getTransitionMatrix(
					graph,
					2,
					ProbabilityDistributionAlgorithm::calculateCredits,
          Method.CLONING);
		print(transitionMatrix.getTransitionVectors());
	}
	
	private static void testLollipop() {
		int nodeCount = 10;
		int k = 2;
		ReadableGraph<Integer> graph =
      LollipopGraphGenerator.generateAdjListGraph(nodeCount);
		TransitionMatrix<Integer> transitionMatrix =
			ProbabilityDistributionAlgorithm
				.getTransitionMatrix(
					graph,
					k,
					ProbabilityDistributionAlgorithm::calculateCredits,
          Method.CLONING);
		print(transitionMatrix.getTransitionVectors());
	}
	
	private static void compareAlgorithms(int logSteps) {
    Scanner scanner = new Scanner(System.in);
    System.out.print("Number of nodes: ");
    int nodeCount = scanner.nextInt(10);
    System.out.print("Node degree: ");
    int degree = scanner.nextInt(10);
    System.out.print("k: ");
    int k = scanner.nextInt(10);
		int logSize = 32 - Integer.numberOfLeadingZeros(nodeCount);
    ReadableGraph<Integer> smallGraph =
      new AdjListGraph(
        getLargestStronglyConnectedComponent(
          new RegularGraphGenerator(RANDOM)
            .generateRegularGraph(nodeCount, degree)));
    smallGraph.printGraph();
    System.out.println(smallGraph.getNodeCnt());
		ReadableGraph<Matching> graph =
      getLargestStronglyConnectedComponent(
        MatchingGraphGenerator.generate(smallGraph));
//		ReadableGraph<Integer> graph =
//      getLargestStronglyConnectedComponent(
//				new StandardGraphGenerator(RANDOM)
//					.generateAdjListGraph(
//						nodeCount,
//						0.01,
//            true));
//		ReadableGraph<Integer> graph =
//      LollipopGraphGenerator.generateAdjListGraph(nodeCount);
		System.out.println(graph.getNodeCnt());
    System.out
      .println(
        graph.getNodes().filter(node -> node.getId().isPerfect()).count()
          + " perfect matchings");
    System.out.println(
      graph.getNodes()
        .collect(
          StreamUtilities.<ReadableNode<Matching>>randomElementCollector())
        .getId()
        .toString());
    List<Supplier<TransitionMatrix<Matching>>>
      transitionMatrixGenerators = new ArrayList<>();
    transitionMatrixGenerators
      .add(() -> UniformDistributionAlgorithm.getTransitionMatrix(graph));
		List<ConvergenceTester<Matching>> testers;
		Consumer<ProbabilityDistributionAlgorithm.CreditCalculator> addAlgorithm =
			calculator -> {
				transitionMatrixGenerators.add(
					() ->
						ProbabilityDistributionAlgorithm
              .getTransitionMatrix(graph, k, calculator, Method.CLONING));
        transitionMatrixGenerators.add(
					() ->
						ProbabilityDistributionAlgorithm
							.getTransitionMatrix(graph, k, calculator, Method.FORWARD_SPLIT));
        transitionMatrixGenerators.add(
					() ->
						ProbabilityDistributionAlgorithm
							.getTransitionMatrix(graph, k, calculator, Method.DEGREE_SPLIT));
      };
		addAlgorithm.accept(ProbabilityDistributionAlgorithm::calculateCredits);
		addAlgorithm.accept(ProbabilityDistributionAlgorithm::calculateCredits2);
		addAlgorithm.accept(ProbabilityDistributionAlgorithm::calculateCredits3);
    addAlgorithm.accept(
      ProbabilityDistributionAlgorithm.getSimpleMixedCalculator(0.5));
    String[] algorithmNames =
      {
        "Standard random walk",
				"Exactly k away cloning",
        "Exactly k away forward splitting",
        "Exactly k away degree splitting",
				"Up to k away cloning",
        "Up to k away forward splitting",
        "Up to k away degree splitting",
				"Up to k away, proportional to distance cloning",
				"Up to k away, proportional to distance forward splitting",
				"Up to k away, proportional to distance degree splitting",
        "Mixed (0.5) cloning",
        "Mixed (0.5) forward splitting",
        "Mixed (0.5) degree splitting"
      };
    if (logSteps <= 0) {
      System.out.print("Log steps");
      Arrays.stream(algorithmNames)
        .forEachOrdered(name -> System.out.print(';' + name));
      System.out.println();
      logSteps = 0;
      testers =
        transitionMatrixGenerators.stream()
          .map(
            transitionMatrixGenerator ->
              ConvergenceTester
                .forTransitionMatrix(transitionMatrixGenerator.get()))
          .collect(Collectors.<ConvergenceTester<Matching>>toList());
      while (true) {
        System.out.print(logSteps++);
        for (ConvergenceTester<Matching> tester : testers) {
          if (logSteps > 1) {
            tester.iterateDistributions(1);
          }
          System.out.print(';');
          System.out.print(tester.convergenceDistance());
        }
        System.out.println();
      }
    }
    else {
      double[][] results = new double[logSteps + 1][];
      for (int step = results.length - 1; step >= 0; step--) {
        results[step] = new double[transitionMatrixGenerators.size()];
      }
      for (
        int generatorIndex = transitionMatrixGenerators.size() - 1;
        generatorIndex >= 0;
        generatorIndex--)
      {
        ConvergenceTester<Matching> tester =
          ConvergenceTester.forTransitionMatrix(
            transitionMatrixGenerators.get(generatorIndex).get());
        for (int step = 0; step < results.length; step++) {
          if (step > 0) {
            tester.iterateDistributions(1);
          }
          results[step][generatorIndex] = tester.convergenceDistance();
          System.out.print(0);
        }
        Collection<Double> perfectMatchingDistribution = new ArrayList<>();
        Collection<Double> nearPerfectMatchingDistribution = new ArrayList<>();
        tester.getTransitionMatrix()
          .processEntries(
            0,
            (node, probability) ->
              (node.getId().isPerfect()
                ? perfectMatchingDistribution
                : nearPerfectMatchingDistribution
              ).add(probability)
          );
        System.out.println("\n" + algorithmNames[generatorIndex]);
        System.out.println("Perfect matchings");
        perfectMatchingDistribution.stream()
          .sorted()
          .forEachOrdered(System.out::println);
        System.out.println("Near-perfect matchings");
        nearPerfectMatchingDistribution.stream()
          .sorted()
          .forEachOrdered(System.out::println);
      }
      System.out.print("Log steps");
      Arrays.stream(algorithmNames)
        .forEachOrdered(name -> System.out.print(';' + name));
      System.out.println();
      for (int step = 0; step < results.length; step++) {
        System.out.print(step);
        for (
          int generatorIndex = transitionMatrixGenerators.size() - 1;
          generatorIndex >= 0;
          generatorIndex--)
        {
          System.out.print(';');
          System.out.print(results[step][generatorIndex]);
        }
        System.out.println();
      }
    }
	}

  private static void compareCoverTimes() {
    Scanner scanner = new Scanner(System.in);
    System.out.print("Number of nodes: ");
    int nodeCount = scanner.nextInt(10);
    System.out.print("k: ");
    int k = scanner.nextInt(10);
		int logSize = 32 - Integer.numberOfLeadingZeros(nodeCount);
		List<NodeCoverRunner> runners = new ArrayList<>();
		runners.add(
			new NodeCoverRunner(
        RANDOM,
        ProbabilityDistributionAlgorithm::calculateCredits4,
        k,
        Method.CLONING));
		Consumer<ProbabilityDistributionAlgorithm.CreditCalculator> addTester =
			calculator -> {
				runners.add(
					new NodeCoverRunner(RANDOM, calculator, k, Method.CLONING));
        runners.add(
					new NodeCoverRunner(RANDOM, calculator, k, Method.FORWARD_SPLIT));
      };
		addTester.accept(ProbabilityDistributionAlgorithm::calculateCredits);
		addTester.accept(ProbabilityDistributionAlgorithm::calculateCredits2);
		addTester.accept(ProbabilityDistributionAlgorithm::calculateCredits3);
    addTester.accept(
      ProbabilityDistributionAlgorithm.getSimpleMixedCalculator(0.5));
		System.out.println(
      "Matchings;"
        + "Standard random walk;"
				+ "Exactly k away non;"
        + "Exactly k away splitting;"
				+ "Up to k away non;"
        + "Up to k away splitting;"
				+ "Up to k away, proportional to distance non;"
				+ "Up to k away, proportional to distance splitting;"
        + "Mixed (0.5) non;"
        + "Mixed (0.5) splitting");
		while (true) {
      ReadableGraph<Matching> graph =
        getLargestStronglyConnectedComponent(
          MatchingGraphGenerator.generate(
            new StandardGraphGenerator(RANDOM)
              .generateAdjListGraph(nodeCount, 0.05, false)));
//		ReadableGraph<Integer> graph =
//      LollipopGraphGenerator.generateAdjListGraph(nodeCount);
      ReadableNode<Matching> startNode = graph.getNodes().findAny().get();
      System.out.print(graph.getNodeCnt());
			for (NodeCoverRunner runner : runners) {
        System.out.print(';');
				System.out.print(runner.getCoverTime(graph, startNode));
			}
			System.out.println();
		}
	}

  private static void lollipopCoverTime() {
    ReadableGraph<Integer> graph =
      LollipopGraphGenerator.generateAdjListGraph(100);
		int k = 3;
    
    NodeCoverRunner standardRunner =
      new NodeCoverRunner(
        RANDOM,
        ProbabilityDistributionAlgorithm::calculateCredits4,
        k,
        Method.CLONING);
    NodeCoverRunner mixedRunner =
      new NodeCoverRunner(
        RANDOM,
        ProbabilityDistributionAlgorithm.getSimpleMixedCalculator(0.5),
        k,
        Method.CLONING);
    System.out.println("Iteration\tStandard cover time\tMixed cover time");
		for (int i = 1; i <= 100; i++) {
      List<ReadableNode<Integer>> nodes =
        graph.getNodes().collect(Collectors.<ReadableNode<Integer>>toList());
      ReadableNode<Integer> node = nodes.get(RANDOM.nextInt(nodes.size()));
			int standardCoverTime =
				standardRunner.getCoverTime(graph, node);
      int mixedCoverTime = mixedRunner.getCoverTime(graph, node);
			System.out.println(i + "\t" + standardCoverTime + "\t" + mixedCoverTime);
		}
  }

  private static void estimateDistribution() {
    Scanner scanner = new Scanner(System.in);
    System.out.print("Number of nodes: ");
    int nodeCount = scanner.nextInt(10);
    System.out.print("k: ");
    int k = scanner.nextInt(10);
    System.out.print("Full walks: ");
    int fullWalks = scanner.nextInt(10);
		int logSize = 32 - Integer.numberOfLeadingZeros(nodeCount);
//		ReadableGraph<Matching> graph =
//      getLargestStronglyConnectedComponent(
//        MatchingGraphGenerator.generate(
//          new StandardGraphGenerator(RANDOM)
//            .generateAdjListGraph(nodeCount, 0.05, false)));
		ReadableGraph<Integer> graph =
      getLargestStronglyConnectedComponent(
				new StandardGraphGenerator(RANDOM)
					.generateAdjListGraph(nodeCount, 0.01, true));
//		ReadableGraph<Integer> graph =
//      LollipopGraphGenerator.generateAdjListGraph(nodeCount);
		System.out.println(graph.getNodeCnt());
		List<CoarseDistributionEstimator> testers = new ArrayList<>(2);
    int steps = 10;
		testers.add(
			new CoarseDistributionEstimator(
        graph,
        steps,
        ProbabilityDistributionAlgorithm::calculateCredits4,
        Method.CLONING,
        0));
    testers.add(
			new CoarseDistributionEstimator(
        graph,
        steps,
        ProbabilityDistributionAlgorithm::calculateCredits,
        Method.CLONING,
        k));
    testers.forEach(estimator -> estimator.performWalks(fullWalks));
    ReadableNode<?> node =
      graph.getNodes()
        .collect(StreamUtilities.<ReadableNode<?>>randomElementCollector());
    List<int[]> hitCountArrays =
      testers.stream()
        .map(estimator -> estimator.getNodeProbabilities(node))
        .collect(Collectors.<int[]>toList());
		
		System.out.println("Log steps;Standard random walk;Exactly k away cloning");
		int logSteps = 0;
		for (int step = 0; step <= steps; step++) {
			System.out.print(step);
			for (int[] hitCountArray : hitCountArrays) {
				System.out.print(';');
				System.out.print(hitCountArray[step]);
			}
			System.out.println();
		}
  }
}
