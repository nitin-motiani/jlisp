package com.motiani.jlisp;

final class VariableExpression extends Expression {
	private String symbol;

	VariableExpression(String symbol) {
		this.symbol = symbol;
	}

	Expression evaluate(Scope scope) {
		return scope.get(symbol);
	}
}
