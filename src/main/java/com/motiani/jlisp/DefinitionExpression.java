package com.motiani.jlisp;

final class DefinitionExpression extends Expression {
	private String variable;
	private Expression expression;

	public DefinitionExpression(String variable, Expression expression) {
		this.variable = variable;
		this.expression = expression;
	}

	// This doesn't make sense right now. Will be revisited when we implement
	// quote
	String getPrintValue() {
		return "definition";
	}

	public Type evaluate(Scope scope) {
		scope.create(variable, expression.evaluate(scope));
		return new VariableExpression(variable);
	}
}
