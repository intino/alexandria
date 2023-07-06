package io.intino.alexandria.sqlpredicate.expressions;

import io.intino.alexandria.sqlpredicate.context.EvaluationContext;

import java.util.*;


/**
 * A MultiExpressionEvaluator is used to evaluate multiple expressions in single
 * method call. <p/> Multiple Expression/ExpressionListener pairs can be added
 * to a MultiExpressionEvaluator object. When the MultiExpressionEvaluator
 * object is evaluated, all the registered Expressions are evaluated and then the
 * associated ExpressionListener is invoked to inform it of the evaluation
 * result. <p/> By evaluating multiple expressions at one time, some
 * optimizations can be made to reduce the number of computations normally
 * required to evaluate all the expressions. <p/> When this class adds an
 * Expression it wraps each node in the Expression's AST with a CacheExpression
 * object. Then each CacheExpression object (one for each node) is placed in the
 * cachedExpressions map. The cachedExpressions map allows us to find the sub
 * expressions that are common across two different expressions. When adding an
 * Expression in, if a sub Expression of the Expression is already in the
 * cachedExpressions map, then instead of wrapping the sub expression in a new
 * CacheExpression object, we reuse the CacheExpression already int the map.
 * <p/> To help illustrate what going on, lets try to give an example: If we
 * denote the AST of a Expression as follows:
 * [AST-Node-Type,Left-Node,Right-Node], then A expression like: "3*5+6" would
 * result in "[*,3,[+,5,6]]" <p/> If the [*,3,[+,5,6]] expression is added to
 * the MultiExpressionEvaluator, it would really be converted to:
 * [c0,[*,3,[c1,[+,5,6]]]] where c0 and c1 represent the CacheExpression
 * expression objects that cache the results of the * and the + operation.
 * Constants and Property nodes are not cached. <p/> If later on we add the
 * following expression [=,11,[+,5,6]] ("11=5+6") to the
 * MultiExpressionEvaluator it would be converted to: [c2,[=,11,[c1,[+,5,6]]]],
 * where c2 is a new CacheExpression object but c1 is the same CacheExpression
 * used in the previous expression. <p/> When the expressions are evaluated, the
 * c1 CacheExpression object will only evaluate the [+,5,6] expression once and
 * cache the resulting value. Hence evauating the second expression costs less
 * because that [+,5,6] is not done 2 times. <p/> Problems: - cacheing the
 * values introduces overhead. It may be possible to be smarter about WHICH
 * nodes in the AST are cached and which are not. - Current implementation is
 * not thread safe. This is because you need a way to invalidate all the cached
 * values so that the next evaluation re-evaluates the nodes. By going single
 * threaded, chache invalidation is done quickly by incrementing a 'view'
 * counter. When a CacheExpressionnotices it's last cached value was generated
 * in an old 'view', it invalidates its cached value.
 * <p>
 * $Date: 2005/08/27 03:52:36 $
 */
public class MultiExpressionEvaluator {

	Map<String, ExpressionListenerSet> rootExpressions = new HashMap<String, ExpressionListenerSet>();
	Map<Expression, CacheExpression> cachedExpressions = new HashMap<Expression, CacheExpression>();

	int view;

	/**
	 * A UnaryExpression that caches the result of the nested expression. The
	 * cached value is valid if the
	 * CacheExpression.cview==MultiExpressionEvaluator.view
	 */
	public class CacheExpression extends UnaryExpression {
		short refCount;
		int cview = view - 1;
		Object cachedValue;
		int cachedHashCode;

		public CacheExpression(Expression realExpression) {
			super(realExpression);
			cachedHashCode = realExpression.hashCode();
		}

		public Object evaluate(EvaluationContext message) throws Exception {
			if (view == cview) return cachedValue;
			cachedValue = right.evaluate(message);
			cview = view;
			return cachedValue;
		}

		public int hashCode() {
			return cachedHashCode;
		}

		public boolean equals(Object o) {
			if (o == null) return false;
			return ((CacheExpression) o).right.equals(right);
		}

		public String getExpressionSymbol() {
			return null;
		}

		public String toString() {
			return right.toString();
		}
	}

	static class ExpressionListenerSet {
		Expression expression;
		List<ExpressionListener> listeners = new ArrayList<ExpressionListener>();
	}

	interface ExpressionListener {
		void evaluateResultEvent(Expression selector, EvaluationContext message, Object result);
	}

	public void addExpressionListner(Expression selector, ExpressionListener c) {
		ExpressionListenerSet data = rootExpressions.get(selector.toString());
		if (data == null) {
			data = new ExpressionListenerSet();
			data.expression = addToCache(selector);
			rootExpressions.put(selector.toString(), data);
		}
		data.listeners.add(c);
	}


	public boolean removeEventListener(String selector, ExpressionListener c) {
		ExpressionListenerSet d = rootExpressions.get(selector);
		if (d == null || !d.listeners.remove(c)) return false;
		if (d.listeners.size() == 0) {
			removeFromCache((CacheExpression) d.expression);
			rootExpressions.remove(selector);
		}
		return true;
	}

	private CacheExpression addToCache(Expression expr) {
		CacheExpression n = cachedExpressions.get(expr);
		if (n == null) {
			n = new CacheExpression(expr);
			cachedExpressions.put(expr, n);
			if (expr instanceof UnaryExpression) {
				UnaryExpression un = (UnaryExpression) expr;
				un.setRight(addToCache(un.getRight()));
			} else if (expr instanceof BinaryExpression) {
				BinaryExpression bn = (BinaryExpression) expr;
				bn.setRight(addToCache(bn.getRight()));
				bn.setLeft(addToCache(bn.getLeft()));
			}
		}
		n.refCount++;
		return n;
	}

	private void removeFromCache(CacheExpression cn) {
		cn.refCount--;
		Expression realExpr = cn.getRight();
		if (cn.refCount == 0) cachedExpressions.remove(realExpr);
		if (realExpr instanceof UnaryExpression un) removeFromCache((CacheExpression) un.getRight());
		if (realExpr instanceof BinaryExpression bn) removeFromCache((CacheExpression) bn.getRight());
	}

	public void evaluate(EvaluationContext message) {
		Collection<ExpressionListenerSet> expressionListeners = rootExpressions.values();
		for (ExpressionListenerSet els : expressionListeners) {
			try {
				Object result = els.expression.evaluate(message);
				els.listeners.forEach(l -> l.evaluateResultEvent(els.expression, message, result));
			} catch (Throwable e) {
				e.printStackTrace();
			}
		}
	}
}
