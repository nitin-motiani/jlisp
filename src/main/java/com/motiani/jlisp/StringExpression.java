package com.motiani.jlisp;

// TODO: This class is currently useless till we implement strings
final class StringExpression extends ConstantExpression {
	private String value;

	StringExpression(String value) {
		this.value = value;
	}

	String getValue() {
		return value;
	}

	String getDisplayValue() {
		return value;
	}

}
