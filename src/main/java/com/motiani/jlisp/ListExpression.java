package com.motiani.jlisp;

import java.util.List;
import java.util.stream.Collectors;

// This class stores a list. But if it's evaluated, it will check that first arg should evaluate 
// to a callable. So it's doing the job of both a proc and a list. Not sure if that's a good way, 
// but that's what I am keeping for time being. 
final class ListExpression extends Expression {

	private List<Expression> expressions;

	ListExpression(List<Expression> expressions) {
		this.expressions = expressions;
	}

	Expression evaluate(Scope scope) {
		if (expressions == null || expressions.size() == 0)
			throw new RuntimeException("Not a valid expression to run");

		Expression fExp = expressions.get(0).evaluate(scope);

		// TODO: Is there ever any way of getting rid of instanceof and
		// downcasting
		if (!(fExp instanceof Callable))
			throw new RuntimeException("Not a callable.");

		List<Expression> args = expressions.stream().skip(1)
				.map(expression -> expression.evaluate(scope))
				.collect(Collectors.toList());

		return ((Callable) fExp).call(args);

	}
}
