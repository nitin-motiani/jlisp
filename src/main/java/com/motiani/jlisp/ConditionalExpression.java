package com.motiani.jlisp;

final class ConditionalExpression extends Expression {
	private Expression conditionExpression;
	private Expression ifExpression;
	private Expression elseExpression;

	ConditionalExpression(Expression conditionExpression,
			Expression ifExpression, Expression elseExpression) {
		this.conditionExpression = conditionExpression;
		this.ifExpression = ifExpression;
		this.elseExpression = elseExpression;
	}

	// This doesn't make sense right now. Will be revisited when we implement
	// quote
	String getPrintValue() {
		return "conditional";
	}

	Expression evaluate(Scope scope) {
		// TODO: Are there better ways to figure out the return type of the
		// expression before we evaluate everything.
		Expression result = conditionExpression.evaluate(scope);
		if (!(result instanceof BooleanExpression))
			throw new RuntimeException("The condition evaluate to a boolean");

		if (((BooleanExpression) result).getValue())
			return ifExpression.evaluate(scope);
		else
			return elseExpression.evaluate(scope);
	}
}
