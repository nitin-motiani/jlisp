package com.motiani.jlisp;

final class AssignmentExpression extends Expression {

	private String variable;
	private Expression expression;

	public AssignmentExpression(String variable, Expression expression) {
		this.variable = variable;
		this.expression = expression;
	}

	Expression evaluate(Scope scope) {
		scope.modify(variable, expression.evaluate(scope));
		// TODO: Change this to make it consistent with the scheme REPL. Though
		// it maybe useless anyway
		return new VariableExpression(variable);
	}
}
