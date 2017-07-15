package com.motiani.jlisp;

abstract class Expression extends Data {
	abstract Data evaluate(Scope scope);
}
