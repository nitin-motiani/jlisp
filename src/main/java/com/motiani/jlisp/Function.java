package com.motiani.jlisp;

import java.util.List;

abstract class Function extends Data {
	String getDisplayValue() {
		return "function";
	}

	// Initially plan was to use varargs here to be able to just pass in
	// whatever args are required, without having to worry about list creation.
	// But this will be called when
	// procedure is invoked, and that anyways has list of args which will have
	// to be converted to array, so no good use of it. Other possible benefit
	// was to have native functions with fixed number of args, but can't
	// implement varargs function with const number arguments function, it
	// seems.
	abstract Data call(List<Data> args);
}
