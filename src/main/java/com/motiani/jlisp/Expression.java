package com.motiani.jlisp;

abstract class Expression extends Type {
	abstract Type evaluate(Scope scope);
}
