package io.intino.alexandria.ollama;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface OllamaParameters<Self extends OllamaParameters<Self>> {

	default Integer numGpu() {
		return parameter("num_gpu");
	}

	/**
	 * Indicates to llama.cpp how many GPUs are available. A value of 0 will disable the use of GPU for the request, and a value
	 * greater than 1 can be use to force llama.cpp to allocate more VRAM. This is useful if ollama is offloading less layers to the
	 * GPU than possible, but can generate OOM CUDA errors.
	 * */
	default Self numGpu(Integer numGpu) {
		return parameter("num_gpu", numGpu);
	}

	default Integer mirostat() {
		return parameter("mirostat");
	}
	/**
	 * Enable Mirostat sampling for controlling perplexity. (default: 0, 0 = disabled, 1 = Mirostat, 2 = Mirostat 2.0)
	 */
	default Self mirostat(Integer mirostat) {
		return parameter("mirostat", mirostat);
	}

	default Double mirostatEta() {
		return parameter("mirostat_eta");
	}
	/**
	 * Influences how quickly the algorithm responds to feedback from the generated text.
	 * A lower learning rate will result in slower adjustments, while a higher learning rate will make the algorithm more responsive. (Default: 0.1)
	 */
	default Self mirostatEta(Double mirostatEta) {
		return parameter("mirostat_eta", mirostatEta);
	}

	default Double mirostatTau() {
		return parameter("mirostat_tau");
	}
	/**
	 * Controls the balance between coherence and diversity of the output.
	 * A lower value will result in more focused and coherent text. (Default: 5.0)
	 */
	default Self mirostatTau(Double mirostatTau) {
		return parameter("mirostat_tau", mirostatTau);
	}

	default Integer numCtx() {
		return parameter("num_ctx");
	}
	/**
	 * Sets the size of the context window used to generate the next token. (Default: 2048)
	 */
	default Self numCtx(Integer numCtx) {
		return parameter("num_ctx", numCtx);
	}

	default Integer repeatLastN() {
		return parameter("repeat_last_n");
	}
	/**
	 * Sets how far back for the model to look back to prevent repetition. (Default: 64, 0 = disabled, -1 = num_ctx)
	 */
	default Self repeatLastN(Integer repeatLastN) {
		return parameter("repeat_last_n", repeatLastN);
	}

	default Double repeatPenalty() {
		return parameter("repeat_penalty");
	}
	/**
	 * Sets how strongly to penalize repetitions. A higher value (e.g., 1.5) will penalize repetitions more strongly, while a lower value (e.g., 0.9) will be more lenient. (Default: 1.1)
	 */
	default Self repeatPenalty(Double repeatPenalty) {
		return parameter("repeat_penalty", repeatPenalty);
	}

	default Double temperature() {
		return parameter("temperature");
	}
	/**
	 * The temperature of the model. Increasing the temperature will make the model answer more creatively. (Default: 0.8)
	 */
	default Self temperature(Double temperature) {
		return parameter("temperature", temperature);
	}

	default Integer seed() {
		return parameter("seed");
	}
	/**
	 * Sets the random number seed to use for generation. Setting this to a specific number will make the model generate
	 * the same text for the same prompt. (Default: 0)
	 */
	default Self seed(Integer seed) {
		return parameter("seed", seed);
	}

	default List<String> stop() {
		String[] stop = parameter("stop");
		return Arrays.asList(stop);
	}
	/**
	 * Sets the stop sequences to use. When this pattern is encountered the LLM will stop generating text and return.
	 * Multiple stop patterns may be set by specifying multiple separate stop parameters in a modelfile.
	 */
	default Self stop(Collection<String> stop) {
		return parameter("stop", stop == null ? null : stop.toArray(String[]::new));
	}
	/**
	 * Sets the stop sequences to use. When this pattern is encountered the LLM will stop generating text and return.
	 * Multiple stop patterns may be set by specifying multiple separate stop parameters in a modelfile.
	 */
	default Self stop(String... stop) {
		return stop(stop == null ? null : Arrays.asList(stop));
	}

	default Double tfsZ() {
		return parameter("tfs_z");
	}
	/**
	 * Tail free sampling is used to reduce the impact of less probable tokens from the output. A higher value (e.g., 2.0) will reduce the impact more, while a value of 1.0 disables this setting. (default: 1)
	 */
	default Self tfsZ(Double tfsZ) {
		return parameter("tfs_z", tfsZ);
	}

	default Integer numPredict() {
		return parameter("num_predict");
	}
	/**
	 * Maximum number of tokens to predict when generating text. (Default: 128, -1 = infinite generation, -2 = fill context)
	 */
	default Self numPredict(Integer numPredict) {
		return parameter("num_predict", numPredict);
	}

	default Integer topK() {
		return parameter("top_k");
	}
	/**
	 * Reduces the probability of generating nonsense. A higher value (e.g. 100) will give more diverse answers,
	 * while a lower value (e.g. 10) will be more conservative. (Default: 40)
	 */
	default Self topK(Integer topK) {
		return parameter("top_k", topK);
	}

	default Double topP() {
		return parameter("top_p");
	}
	/**
	 * Works together with top-k. A higher value (e.g., 0.95) will lead to more diverse text,
	 * while a lower value (e.g., 0.5) will generate more focused and conservative text. (Default: 0.9)
	 */
	default Self topP(Double topP) {
		return parameter("top_p", topP);
	}

	default Integer minP() {
		return parameter("min_p");
	}
	/**
	 * Alternative to the top_p, and aims to ensure a balance of quality and variety.
	 * The parameter p represents the minimum probability for a token to be considered,
	 * relative to the probability of the most likely token. For example, with p=0.05 and the most likely token having
	 * a probability of 0.9, logits with a value less than 0.045 are filtered out. (Default: 0.0)
	 */
	default Self minP(Integer minP) {
		return parameter("min_p", minP);
	}

	Map<String, Object> parametersMap();

	@SuppressWarnings("unchecked")
	default <T> T parameter(String name) {
		return (T) parametersMap().get(name);
	}

	default Self parameter(String name, Object value) {
		if(value == null) parametersMap().remove(name);
		parametersMap().put(name, value);
		return self();
	}

	@SuppressWarnings("unchecked")
	default Self self() {
		return (Self) this;
	}
}
