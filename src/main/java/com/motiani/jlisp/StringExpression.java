package com.motiani.jlisp;

final class StringExpression extends ConstantExpression {
	private String value;

	StringExpression(String value) {
		this.value = value;
	}

	String getValue() {
		return value;
	}

	Expression evaluate(Scope scope) {
		return this;
	}
}
