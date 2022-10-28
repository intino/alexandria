package io.intino.alexandria.markov;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;
import static java.util.stream.IntStream.range;

public class Markov {
	private final int size;
	private final String[] states;
	private final Map<String, Integer> map;
	private final int[][] count;
	private Type type;

	public Markov(String... states) {
		this(states, new int[states.length][states.length]);
	}

	private Markov(String[] states, int[][] count) {
		this.size = states.length;
		this.states = states;
		this.map = asMap(states);
		this.count = count;
		this.type = Type.Directed;
	}

	public Markov set(Type type) {
		this.type = type;
		return this;
	}

	public String[] states() {
		return states;
	}

	public int[][] transitions() {
		return count;
	}

	public double[][] transitionProbabilities() {
		double[][] result = new double[size][];
		for (int row = 0; row < size; row++)
			result[row] = transitionProbabilities(row);
		return result;
	}

	private double[] transitionProbabilities(int row) {
		double[] result = new double[size];
		double sum = sum(row);
		for (int i = 0; i < size; i++)
			result[i] = sum > 0 ? count[row][i] / sum : (i==row) ? 1. : 0.;
		return result;
	}

	public double[] randomWalk(String from) {
		return power(transitionProbabilities())[map.get(from)];
	}

	public double[] randomWalk() {
		double[] stateProbabilities = stateProbabilities();
		double[][] randomWalks = power(transitionProbabilities());
		double[] result = new double[size];
		for (int i = 0; i < size; i++)
			for (int j = 0; j < size; j++)
				result[j] += stateProbabilities[i] * randomWalks[i][j];
		return result;
	}


	public double[] stateProbabilities() {
		double[] result = new double[size];
		double total = 0;
		for (int i = 0; i < size; i++)
			for (int j = 0; j < size; j++) {
				result[i] += count[j][i];
				total += count[j][i];
			}
		if (total == 0) return result;
		for (int i = 0; i < size; i++) result[i] = result[i] / total;
		return result;
	}

	private double[][] power(double[][] matrix) {
		double[][] result = matrix;
		for (int i = 1; i < 100; i++)
			result = multiply(result, matrix);
		return result;
	}

	private double[][] multiply(double[][] a, double[][] b) {
		double[][] c = new double[size][size];
		IntStream.range(0,size).parallel().forEach(i -> {
			for (int k = 0; k < size; k++)
				for (int j = 0; j < size; j++)
					c[i][j] += a[i][k] * b[k][j];
		});
		return c;
	}
	private static final int Threshold = 1 << 16;

	public Markov add(String from, String to) {
		increment(from, to);
		if (type == Type.Undirected) increment(to, from);
		return this;
	}

	private void increment(String from, String to) {
		int row = map.get(from);
		int col = map.get(to);
		if (++count[row][col] >= Threshold) shrink(row);
	}

	private int sum(int row) {
		return Arrays.stream(count[row]).sum();
	}

	private void shrink(int row) {
		for (int i = 0; i < size; i++) {
			count[row][i] >>= 1;
		}
	}

	private static Map<String, Integer> asMap(String[] states) {
		return range(0, states.length)
				.boxed()
				.collect(toMap(i -> states[i], i -> i));
	}

	public String serialize() {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < size; i++)
			sb.append('|').append(states[i]).append(':').append(serialize(count[i]));
		return sb.substring(1);
	}

	private String serialize(int[] count) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < size; i++)
			sb.append(',').append(count[i]);
		return sb.substring(1);
	}

	public static Markov deserialize(String content) {
		return deserialize(content.split("\\|"));
	}

	private static Markov deserialize(String[] lines) {
		return deserialize(stream(lines).filter(l->!l.isEmpty()).map(Markov::parse).collect(toList()));
	}

	private static Markov deserialize(List<String[]> data) {
		return new Markov(statesIn(data), countIn(data));
	}

	private static String[] statesIn(List<String[]> data) {
		return data.stream().map(l->l[0]).toArray(String[]::new);
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

	public enum Type {
		Directed, Undirected
	}

}
