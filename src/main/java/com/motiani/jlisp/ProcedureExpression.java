package com.motiani.jlisp;

import java.util.List;
import java.util.stream.Collectors;

//This class stores a list. But if it's evaluated, it will check that first arg should evaluate 
//to a callable. So it's doing the job of both a proc and a list. Not sure if that's a good way, 
//but that's what I am keeping for time being. 
final class ProcedureExpression extends ListExpression {

	ProcedureExpression(List<Type> items) {
		super(items);
	}

	public Type evaluate(Scope scope) {

		if (items == null)
			throw new RuntimeException("Not a valid expression to run");

		// TODO: Assumption is that the items in the list will always be
		// evaluable if we are evaluating the list. This will need to be changed
		// if I can think of a counter example.

		// We have a separate static instance for empty list. So for proc
		// invocation, the list always have at least one item.
		// If something went wrong, it seems better to throw an NPE here.
		Type fExp = ((Evaluable) items.get(0)).evaluate(scope);

		if (!(fExp instanceof Callable))
			throw new RuntimeException("Not a callable.");

		List<Type> args = items.stream().skip(1)
				.map(expression -> ((Expression) expression).evaluate(scope))
				.collect(Collectors.toList());

		return ((Callable) fExp).call(args);

	}

}
