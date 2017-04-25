package com.motiani.jlisp;

class VariableExpression extends AtomicExpression {
	private String symbol;

	// TODO: Include scope here too

	VariableExpression(String symbol) {
		this.symbol = symbol;
	}

	Expression evaluate() {
		return this;
	}
}
