package com.motiani.jlisp;

import java.util.List;

final class UnquoteExpression extends ListExpression {

	UnquoteExpression(List<Data> items) {
		super(items);
	}

	Data evaluate(Scope scope) {
		assert (items.size() == 2);
		assert (Keywords.UNQUOTE.equals(items.get(0)));

		return ((Expression) items.get(1)).evaluate(scope);
	}
}
