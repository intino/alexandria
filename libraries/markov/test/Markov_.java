import io.intino.alexandria.markov.Markov;
import org.junit.Test;

import static io.intino.alexandria.markov.Markov.Type.Undirected;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


public class Markov_ {

	@Test
	public void given_no_transitions() {
		Markov markov = new Markov("A","B");
		assertThat(markov.transitions()).isEqualTo(new int[][]{{0,0},{0, 0}});
		assertThat(markov.transitionProbabilities()).isEqualTo(new double[][]{{1,0},{0, 1}});
		assertThat(markov.serialize()).isEqualTo("A:0,0|B:0,0");
	}

	@Test
	public void given_some_transitions() {
		Markov markov = new Markov("A","B")
				.add("A","A")
				.add("A","B").add("A","B").add("A","B")
				.add("B","A").add("B","A").add("B","A").add("B","A")
				.add("B","B");
		assertThat(markov.transitions()).isEqualTo(new int[][]{{1, 3},{4, 1}});
		assertThat(markov.transitionProbabilities()).isEqualTo(new double[][]{{0.25,0.75},{0.8, 0.2}});
		assertThat(markov.serialize()).isEqualTo("A:1,3|B:4,1");
		assertThat(markov.randomWalk("A")).isEqualTo(new double[] {0.5161290322580654, 0.48387096774193616});
		assertThat(markov.randomWalk("B")).isEqualTo(new double[] {0.5161290322580652, 0.4838709677419361});
	}

	@Test
	public void given_undirected_graph() {
		Markov markov = new Markov("A", "B", "C", "D", "E", "F").set(Undirected)
				.add("A", "B")
				.add("B", "C").add("B", "D").add("B", "E").add("B", "F")
				.add("C", "E")
				.add("D", "F");
		assertThat(markov.serialize()).isEqualTo("A:0,1,0,0,0,0|B:1,0,1,1,1,1|C:0,1,0,0,1,0|D:0,1,0,0,0,1|E:0,1,1,0,0,0|F:0,1,0,1,0,0");
		assertThat(markov.randomWalk("A")).isEqualTo(new double[] {0.0714285714285715, 0.35714285714285726, 0.14285714285714296, 0.14285714285714296, 0.14285714285714296, 0.14285714285714296});
	}

	@Test
	public void given_many_transitions() {
		Markov markov = new Markov("A","B");
		for (int i = 0; i < 1<<22; i++) {
				markov
					.add("A","B").add("A","B")
					.add("A","A").add("A","B")
					.add("B","A").add("B","A")
					.add("B","B")
					.add("B","A").add("B","A");
		}
		assertThat(markov.transitions()).isEqualTo(new int[][]{{10922, 32768},{32768, 8192}});
		assertThat(markov.serialize()).isEqualTo("A:10922,32768|B:32768,8192");
	}

	@Test
	public void should_deserialize() {
		Markov markov = Markov.deserialize("A:1,4|B:3,1");
		assertThat(markov.states()).isEqualTo(new String[] {"A","B"});
		assertThat(markov.transitions()).isEqualTo(new int[][]{{1, 4},{3, 1}});
		assertThat(markov.transitionProbabilities()).isEqualTo(new double[][]{{0.2, 0.8}, {0.75, 0.25}});
	}
}
