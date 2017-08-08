package com.motiani.jlisp;

import java.util.List;

final class Macro extends Data {

	// TODO: To avoid code duplication, for now I have decided to implement
	// macros as having a function internally which takes the macro args
	// (the lisp expressions) and return new expressions. This may change later.
	// Making macro extend function seemed wrong though as the is-a relationship
	// is not the correct semantic here. So better to have composition
	private UserFunction function;

	String getDisplayValue() {
		// TODO : Probably throw.
		return "macro";
	}

	Data expand(List<Data> args) {
		return function.call(args);

	}

	private Macro(UserFunction userFunction) {
		this.function = userFunction;
	}

	static Macro createWithConstArgs(List<Expression> body,
			List<SymbolExpression> argNames, Scope parentScope) {
		UserFunction function = UserFunction.createWithConstArgs(body,
				argNames, parentScope);

		return new Macro(function);
	}

	static Macro createWithVarArgs(List<Expression> body,
			SymbolExpression argName, Scope parentScope) {
		UserFunction function = UserFunction.createWithVarArgs(body, argName,
				parentScope);
		return new Macro(function);
	}

}
