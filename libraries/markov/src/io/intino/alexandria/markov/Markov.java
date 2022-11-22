package io.intino.alexandria.markov;

import java.util.*;

import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;
import static java.util.stream.IntStream.range;

public class Markov {
	private final List<String> states;
	private final Map<String, Integer> index;
	private int[][] transitions;
	private int count;

	public Markov(String... states) {
		this(states, new int[states.length][states.length]);
	}

	private Markov(String[] states, int[][] transitions) {
		this.states = new ArrayList<>(List.of(states));
		this.index = indexOf(states);
		this.transitions = transitions;
		this.count = stream(this.transitions).mapToInt(this::sum).sum();
	}

	private int sum(int[] transitions) {
		return stream(transitions).sum();
	}

	private Map<String, Integer> indexOf(String[] states) {
		return range(0, states.length)
				.boxed()
				.collect(toMap(i -> states[i], i -> i));
	}

	public List<String> states() {
		return Collections.unmodifiableList(states);
	}

	public int indexOf(String state) {
		return index.getOrDefault(state, -1);
	}

	public Markov include(String from, String to) {
		increment(include(from), include(to));
		return this;
	}

	private void increment(String from, String to) {
		increment(index.get(from), index.get(to));
	}

	public int count() {
		return count;
	}

	public boolean isAlreadyTrained() {
		return (double) count / size() >= 15;
	}

	private static final int Threshold = 1 << 16;

	private void increment(int row, int col) {
		count++;
		if (transitions.length < size()) transitions = resize();
		if (++transitions[row][col] >= Threshold) shrink(row);
	}

	private String include(String node) {
		if (index.containsKey(node)) return node;
		states.add(node);
		index.put(node, index.size());
		return node;
	}

	private int[][] resize() {
		int[][] result = new int[size()][size()];
		for (int i = 0; i < transitions.length; i++)
			System.arraycopy(transitions[i], 0, result[i], 0, transitions.length);
		return result;
	}


	private int size() {
		return states.size();
	}

	public int[][] transitions() {
		return transitions;
	}

	public double[][] transitionProbabilities() {
		double[][] result = new double[size()][];
		for (int row = 0; row < size(); row++)
			result[row] = transitionProbabilities(row);
		return result;
	}

	private double[] transitionProbabilities(int row) {
		double[] result = new double[size()];
		double sum = sum(row);
		for (int i = 0; i < size(); i++)
			result[i] = sum > 0 ? transitions[row][i] / sum : (i == row) ? 1. : 0.;
		return result;
	}

	public double[] randomWalk(String from) {
		return power(transitionProbabilities())[index.get(from)];
	}

	public double[] randomWalk() {
		double[] stateProbabilities = stateProbabilities();
		double[][] randomWalks = power(transitionProbabilities());
		double[] result = new double[size()];
		for (int i = 0; i < size(); i++)
			for (int j = 0; j < size(); j++)
				result[j] += stateProbabilities[i] * randomWalks[i][j];
		return result;
	}


	public double[] stateProbabilities() {
		double[] result = new double[size()];
		double total = 0;
		for (int i = 0; i < size(); i++)
			for (int j = 0; j < size(); j++) {
				result[i] += transitions[j][i];
				total += transitions[j][i];
			}
		if (total == 0) return result;
		for (int i = 0; i < size(); i++) result[i] = result[i] / total;
		return result;
	}

	private double[][] power(double[][] matrix) {
		double[][] result = matrix;
		for (int i = 1; i < 100; i++)
			result = multiply(result, matrix);
		return result;
	}

	private double[][] multiply(double[][] a, double[][] b) {
		double[][] c = new double[size()][size()];
		range(0, size()).parallel().forEach(i -> {
			for (int k = 0; k < size(); k++)
				for (int j = 0; j < size(); j++)
					c[i][j] += a[i][k] * b[k][j];
		});
		return c;
	}

	private int sum(int row) {
		return Arrays.stream(transitions[row]).sum();
	}

	private void shrink(int row) {
		for (int i = 0; i < size(); i++)
			transitions[row][i] >>= 1;
	}

	public String serialize() {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < size(); i++)
			sb.append('|').append(states.get(i)).append(':').append(serialize(transitions[i]));
		return sb.substring(1);
	}

	private String serialize(int[] count) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < size(); i++)
			sb.append(',').append(count[i]);
		return sb.substring(1);
	}

	public static Markov deserialize(String content) {
		return deserialize(content.split("\\|"));
	}

	private static Markov deserialize(String[] lines) {
		return deserialize(stream(lines).filter(l -> !l.isEmpty()).map(Markov::parse).collect(toList()));
	}

	private static Markov deserialize(List<String[]> data) {
		return new Markov(statesIn(data), countIn(data));
	}

	private static String[] statesIn(List<String[]> data) {
		return data.stream().map(l -> l[0]).toArray(String[]::new);
	}

	private static int[][] countIn(List<String[]> data) {
		return data.stream().map(Markov::parse).toArray(int[][]::new);
	}

	private static String[] parse(String line) {
		return line.split("[:,]");
	}

	private static int[] parse(String[] data) {
		return stream(data).skip(1).mapToInt(Integer::parseInt).toArray();
	}
}