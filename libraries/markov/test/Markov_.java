import io.intino.alexandria.markov.Markov;
import org.junit.Test;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class Markov_ {

	@Test
	public void given_no_transitions() {
		Markov markov = new Markov("A", "B");
		assertThat(markov.transitions()).isEqualTo(new int[][]{{0, 0}, {0, 0}});
		assertThat(markov.transitionProbabilities()).isEqualTo(new double[][]{{1, 0}, {0, 1}});
		assertThat(markov.count()).isEqualTo(0);
		assertThat(markov.isAlreadyTrained()).isFalse();
		assertThat(markov.randomWalk("A")).isEqualTo(new double[]{1, 0});
		assertThat(markov.randomWalk("B")).isEqualTo(new double[]{0, 1});
		assertThat(markov.randomWalk()).isEqualTo(new double[]{0, 0});
		assertThat(markov.serialize()).isEqualTo("A:0,0|B:0,0");
	}

	@Test
	public void given_some_transitions_without_defining_initial_states() {
		Markov markov = new Markov()
				.include("A", "A").include("A", "A")
				.include("A", "B").include("B", "B")
				.include("B", "C").include("C", "B").include("B", "C")
				.include("C", "A").include("A", "B")
				.include("B", "B");
		assertThat(markov.transitions()).isEqualTo(new int[][]{{2, 2, 0}, {0, 2, 2}, {1, 1, 0}});
		assertThat(markov.count()).isEqualTo(10);
		assertThat(markov.stateProbabilities()).isEqualTo(new double[]{0.3, 0.5, 0.2});
		assertThat(markov.transitionProbabilities()).isEqualTo(new double[][]{{0.5, 0.5, 0.0}, {0.0, 0.5, 0.5}, {0.5, 0.5, 0.0}});
		assertThat(markov.serialize()).isEqualTo("A:2,2,0|B:0,2,2|C:1,1,0");
		assertThat(markov.randomWalk("A")).isEqualTo(new double[]{0.25, 0.5, 0.25});
		assertThat(markov.randomWalk("B")).isEqualTo(new double[]{0.25, 0.5, 0.25});
		assertThat(markov.randomWalk("C")).isEqualTo(new double[]{0.25, 0.5, 0.25});
		assertThat(markov.randomWalk()).isEqualTo(new double[]{0.25, 0.5, 0.25});
	}

	@Test
	public void given_many_transitions() {
		Markov markov = new Markov("A", "B", "C");
		for (int i = 0; i < 1 << 22; i++) {
			markov
					.include("A", "B").include("A", "B")
					.include("A", "A").include("A", "B")
					.include("B", "A").include("B", "A")
					.include("B", "B")
					.include("B", "A").include("B", "A")
					.include("C", "C").include("C", "C");
		}
		assertThat(markov.transitions()).isEqualTo(new int[][]{{10922, 32768, 0}, {32768, 8192, 0}, {0, 0, 32768}});
		assertThat(markov.serialize()).isEqualTo("A:10922,32768,0|B:32768,8192,0|C:0,0,32768");
		assertThat(markov.stateProbabilities()).isEqualTo(new double[]{0.37208945817506683, 0.34883918990274065, 0.2790713519221925});
		assertThat(markov.randomWalk("A")).isEqualTo(new double[]{0.5161252215002965, 0.4838747784997058, 0.0});
		assertThat(markov.randomWalk("B")).isEqualTo(new double[]{0.5161252215002965, 0.48387477849970584, 0.0});
		assertThat(markov.randomWalk()).isEqualTo(new double[]{0.37208945817506767, 0.34883918990274143, 0.2790713519221925});
	}

	@Test
	public void should_deserialize() {
		Markov markov = Markov.deserialize("A:1,4|B:3,1");
		assertThat(markov.states()).isEqualTo(List.of("A", "B"));
		assertThat(markov.transitions()).isEqualTo(new int[][]{{1, 4}, {3, 1}});
		assertThat(markov.transitionProbabilities()).isEqualTo(new double[][]{{0.2, 0.8}, {0.75, 0.25}});
	}
}