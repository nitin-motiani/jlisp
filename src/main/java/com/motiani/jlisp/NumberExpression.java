package com.motiani.jlisp;

class NumberExpression extends ConstantExpression {
	Number value;

	NumberExpression(Number num) {
		this.value = num;
	}

	Number getValue() {
		return this.value;
	}

	Expression evaluate() {
		return this;
	}
}
