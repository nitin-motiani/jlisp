package com.motiani.jlisp;

abstract class ConstantExpression extends AtomicExpression {
	// TODO: Not 100% clear on this idea of having two functions. Maybe this can
	// be moved to individual
	// List or atomic types.
	abstract Object getValue();
}
