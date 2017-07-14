package com.motiani.jlisp;

final class BooleanExpression extends ConstantExpression {

	Boolean value;

	public BooleanExpression(boolean value) {
		this.value = value;
	}

	Boolean getValue() {
		return this.value;
	}

	String getPrintValue() {
		return String.valueOf(this.value);
	}

	public Type evaluate(Scope scope) {
		return this;
	}
}
