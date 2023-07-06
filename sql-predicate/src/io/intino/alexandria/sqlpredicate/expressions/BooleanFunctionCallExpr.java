/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.intino.alexandria.sqlpredicate.expressions;

import io.intino.alexandria.sqlpredicate.context.EvaluationContext;

import java.util.List;

/**
 * Function call expression that evaluates to a boolean value.  Selector parsing requires BooleanExpression objects for
 * Boolean expressions, such as operands to AND, and as the final result of a selector.  This provides that interface
 * for function call expressions that resolve to Boolean values.
 * <p/>
 * If a function can return different types at evaluation-time, the function implementation needs to decide whether it
 * supports casting to Boolean at parse-time.
 *
 * @see FunctionCallExpression#createFunctionCall
 */

public class BooleanFunctionCallExpr extends FunctionCallExpression implements BooleanExpression {

	public BooleanFunctionCallExpr(String func_name, List<Expression> args) throws InvalidFunctionExpressionException {
		super(func_name, args);
	}

	public boolean matches(EvaluationContext context) throws Exception {
		Boolean result = (Boolean) evaluate(context);
		return result != null && result;

	}
}

