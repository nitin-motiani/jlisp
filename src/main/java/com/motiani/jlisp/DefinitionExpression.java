package com.motiani.jlisp;

final class DefinitionExpression extends Expression {
	private String variable;
	private Expression expression;

	public DefinitionExpression(String variable, Expression expression) {
		this.variable = variable;
		this.expression = expression;
	}

	Expression evaluate(Scope scope) {
		scope.create(variable, expression.evaluate(scope));
		// TODO: Figure this out. Currently doing this in accordance with what
		// the scheme REPL shows me
		return new VariableExpression(variable);
	}
}
