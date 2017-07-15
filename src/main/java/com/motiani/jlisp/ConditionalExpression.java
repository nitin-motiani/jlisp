package com.motiani.jlisp;

import java.util.List;

final class ConditionalExpression extends ListExpression {

	ConditionalExpression(List<Data> items) {
		super(items);
	}

	Data evaluate(Scope scope) {
		assert (items.size() == 3 || items.size() == 4);
		assert (Keywords.IF.equals(items.get(0)));

		Expression conditionExpression = (Expression) (items.get(1));
		Expression ifExpression = (Expression) (items.get(2));
		Expression elseExpression = (items.size() == 4) ? (Expression) items
				.get(3) : null;

		// TODO: Are there better ways to figure out the return type of the
		// expression before we evaluate everything.
		Data result = conditionExpression.evaluate(scope);
		if (!(result instanceof BooleanExpression))
			throw new RuntimeException("The condition evaluate to a boolean");

		if (((BooleanExpression) result).getValue())
			return ifExpression.evaluate(scope);
		else
			// Handle possible NPE better
			return elseExpression.evaluate(scope);
	}
}
