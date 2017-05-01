package com.motiani.jlisp;

// My current plan is to have a callable interface to provide expressions
// which can be called. Basically functions. It was this or have a separate level of hierarchy call function 
// expression. I went for this. At least for time being. 
interface Callable {
	// The reason for giving this varargs instead of list is that for methods
	// like add etc, just want to pass in args without having to worry about
	// creating list or some such shit. Better semantics
	Expression call(Expression... args);
}
