package com.motiani.jlisp;

import java.util.List;

final class QuoteExpression extends ListExpression {

	QuoteExpression(List<Data> items) {
		super(items);
	}

	Data evaluate(Scope scope) {
		assert (items.size() == 2);
		assert (Keywords.QUOTE.equals(items.get(0)));

		return items.get(1);
	}

}
