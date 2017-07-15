package com.motiani.jlisp;

import java.util.List;
import java.util.stream.Collectors;

// This class stores a list. But if it's evaluated, it will check that first arg should evaluate 
// to a callable. So it's doing the job of both a proc and a list. Not sure if that's a good way, 
// but that's what I am keeping for time being. 
class ListExpression extends Expression {

	protected List<Type> items;

	// Should we pass List<? extends Type> to make it easier to use
	ListExpression(List<Type> items) {
		this.items = items;
	}

	// TODO: Is this a good function to have really?
	// If I do the refactor of moving car/cdr/map etc to this class, can get
	// rid of this.
	List<Type> getItems() {
		return items;
	}

	public Type evaluate(Scope scope) {
		if (items == null || items.size() == 0)
			throw new RuntimeException("Not a valid expression to run");

		// TODO: Assumption is that the items in the list will always be
		// evaluable if we are evaluating the list. This will need to be changed
		// if I can think of a counter example.
		Type fExp = ((Evaluable) items.get(0)).evaluate(scope);

		if (!(fExp instanceof Callable))
			throw new RuntimeException("Not a callable.");

		List<Type> args = items.stream().skip(1)
				.map(expression -> ((Expression) expression).evaluate(scope))
				.collect(Collectors.toList());

		return ((Callable) fExp).call(args);

	}

	String getDisplayValue() {
		String value = this.items.stream().map(expr -> expr.getDisplayValue())
				.collect(Collectors.joining(" "));
		return "(" + value + ")";
	}
}
