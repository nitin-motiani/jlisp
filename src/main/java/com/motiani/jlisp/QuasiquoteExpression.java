package com.motiani.jlisp;

import java.util.List;
import java.util.stream.Collectors;

final class QuasiquoteExpression extends ListExpression {

	QuasiquoteExpression(List<Data> items) {
		super(items);
	}

	// All this casting feels pretty bad in general. Need to find a cleaner way
	// to do this
	private Data evaluate(Expression subExpr, Scope scope) {
		if (subExpr instanceof UnquoteExpression)
			return subExpr.evaluate(scope);

		if (subExpr instanceof ListExpression) {
			List<Data> childExpressions = ((ListExpression) subExpr).getItems();
			List<Data> evaluatedExpressions = childExpressions.stream()
					.map(childExpr -> evaluate((Expression) childExpr, scope))
					.collect(Collectors.toList());

			return ListExpressionFactory
					.createListExpression(evaluatedExpressions);
		}

		return subExpr;
	}

	Data evaluate(Scope scope) {
		assert (items.size() == 2);
		assert (Keywords.QUASIQUOTE.equals(items.get(0)));

		return evaluate((Expression) items.get(1), scope);
	}

}
