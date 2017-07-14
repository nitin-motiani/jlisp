package com.motiani.jlisp;

final class DefinitionExpression extends Expression {
	private SymbolExpression variable;
	private Expression expression;

	public DefinitionExpression(SymbolExpression variable, Expression expression) {
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
		return variable;
	}
}
