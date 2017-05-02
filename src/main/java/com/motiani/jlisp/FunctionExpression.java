package com.motiani.jlisp;

abstract class FunctionExpression extends Expression implements Callable {
	// TODO: I may have to change the final design of this stuff. I am not 100%
	// comfortable with the idea of an evaluate with a function. But for time
	// being I'll make do.

	// Overall I want to have idea of type and callable, data, and evaluable
	// interfaces.
	// But it's not concrete currently, and implementing quote can change it
	// later. So not committing to it
	Expression evaluate(Scope scope) {
		return this;
	}
}
