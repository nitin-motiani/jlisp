package com.motiani.jlisp;

final class AssignmentExpression extends Expression {

	private String variable;
	private Expression expression;

	public AssignmentExpression(String variable, Expression expression) {
		this.variable = variable;
		this.expression = expression;
	}

	// This doesn't make sense right now. Will be revisited when we implement
	// quote
	String getPrintValue() {
		return "assignment";
	}

	public Type evaluate(Scope scope) {
		Type previousValue = scope.get(variable);
		scope.modify(variable, expression.evaluate(scope));
		return previousValue;
	}
}
