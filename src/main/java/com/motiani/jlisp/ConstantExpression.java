package com.motiani.jlisp;

// These are the Self evaluation expressions in lisp. 
abstract class ConstantExpression extends Expression {
	public Type evaluate(Scope scope) {
		return this;
	}
}
