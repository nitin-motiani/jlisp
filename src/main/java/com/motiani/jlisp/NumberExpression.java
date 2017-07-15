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

	String getDisplayValue() {
		return this.value.toString();
	}

}
