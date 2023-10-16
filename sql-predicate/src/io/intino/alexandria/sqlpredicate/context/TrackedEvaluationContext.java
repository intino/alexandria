package io.intino.alexandria.sqlpredicate.context;


public interface TrackedEvaluationContext extends EvaluationContext {

	void track(String variable);
}
