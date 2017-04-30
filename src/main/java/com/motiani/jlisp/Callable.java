package com.motiani.jlisp;

// My current plan is to have a callable interface to provide expressions
// which can be called. Basically functions. It was this or have a separate level of hierarchy call function 
// expression. I went for this. At least for time being. 
interface Callable {
	Expression call(Expression... args);
}
