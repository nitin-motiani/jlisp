package com.motiani.jlisp;

import java.util.List;

// My current plan is to have a callable interface to provide expressions
// which can be called. Basically functions. It was this or have a separate level of hierarchy call function 
// expression. I went for this. At least for time being. 
interface Callable {
	// Initially plan was to use varargs here to be able to just pass in
	// whatever args are required, without having to worry about list creation.
	// But this will be called when
	// procedure is invoked, and that anyways has list of args which will have
	// to be converted to array, so no good use of it. Other possible benefit
	// was to have native functions with fixed number of args, but can't
	// implement varargs function with const number arguments function, it
	// seems.
	Expression call(List<Expression> args);
}
