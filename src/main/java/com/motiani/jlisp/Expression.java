package com.motiani.jlisp;

abstract class Expression {
	abstract Expression evaluate(Scope scope);
}
