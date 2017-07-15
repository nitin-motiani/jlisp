package com.motiani.jlisp;

abstract class Function extends Type implements Callable {
	String getDisplayValue() {
		return "function";
	}
}
