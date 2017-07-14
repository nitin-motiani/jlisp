package com.motiani.jlisp;

// TODO: Most likely this will be deleted
abstract class ConstantExpression extends Expression {
	// TODO: Not 100% clear on this idea of having two functions. Maybe there
	// can be separate interface for this stuff.
	abstract Object getValue();
}
