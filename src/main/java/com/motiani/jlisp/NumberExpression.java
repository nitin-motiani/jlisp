package com.motiani.jlisp;

import java.math.BigDecimal;

final class NumberExpression extends ConstantExpression {
	private BigDecimal value;

	NumberExpression(BigDecimal num) {
		this.value = num;
	}

	BigDecimal getValue() {
		return this.value;
	}

	public Type evaluate(Scope scope) {
		return this;
	}

	String getPrintValue() {
		return this.value.toString();
	}
}
