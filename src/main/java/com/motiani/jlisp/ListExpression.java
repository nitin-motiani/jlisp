package com.motiani.jlisp;

import java.util.List;
import java.util.stream.Collectors;

abstract class ListExpression extends Expression {

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

	String getDisplayValue() {
		String value = this.items.stream().map(expr -> expr.getDisplayValue())
				.collect(Collectors.joining(" "));
		return "(" + value + ")";
	}
}
