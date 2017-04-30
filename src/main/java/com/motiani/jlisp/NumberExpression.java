package com.motiani.jlisp;

final class NumberExpression extends ConstantExpression {
	private Number value;

	NumberExpression(Number num) {
		this.value = num;
	}

	Number getValue() {
		return this.value;
	}

	Expression evaluate(Scope scope) {
		return this;
	}
}
