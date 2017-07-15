package com.motiani.jlisp;

interface Evaluable {
	abstract Type evaluate(Scope scope);
}
