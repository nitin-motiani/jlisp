package com.motiani.jlisp;

abstract class Type {

	// Since we need some way to show the result on REPL, it makes sense for
	// every type of expression to implement this. But when we introduce symbols
	// and quotes, this is going to get complicated. At that time, I'll revisit
	// this construct
	abstract String getDisplayValue();
}
