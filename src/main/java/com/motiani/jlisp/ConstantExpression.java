package com.motiani.jlisp;

abstract class ConstantExpression extends Expression {
	// TODO: Not 100% clear on this idea of having two functions. Maybe there
	// can be separate interface for this stuff.
	abstract Object getValue();
}
