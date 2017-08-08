package com.motiani.jlisp;

import java.math.BigDecimal;

// TODO: Either subclass this for float, int etc types or at least write constructors to 
// take different type of numbers
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
