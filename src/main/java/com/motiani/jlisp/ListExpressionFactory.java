package com.motiani.jlisp;

import java.util.ArrayList;
import java.util.List;

// Purpose of this class is to create the correct concrete implementation of ListExpression 
// based on what items there are are in the list
class ListExpressionFactory {

	// TODO: I am not 100% sure of this approach. But it seemed a clean way to
	// have one singleton empty list which just returns itself on evaluation.
	// This is basically the nil of the language
	private static ListExpression emptyList = new ListExpression(
			new ArrayList<Data>()) {

		@Override
		public Data evaluate(Scope scope) {
			return this;
		}
	};

	// This looked like a good idea at the time. Now I am not so sure
	static ListExpression createListExpression(List<Data> items) {
		if (items.size() == 0)
			return emptyList;

		Data symbol = items.get(0);

		if (Keywords.IF.equals(symbol)) {
			return new ConditionalExpression(items);
		} else if (Keywords.DEFINE.equals(symbol)) {
			return new DefinitionExpression(items);
		} else if (Keywords.ASSIGN.equals(symbol)) {
			return new AssignmentExpression(items);
		} else if (Keywords.LAMBDA.equals(symbol)) {
			return new LambdaExpression(items);
		} else if (Keywords.QUOTE.equals(symbol)) {
			return new QuoteExpression(items);
		} else if (Keywords.LET.equals(symbol)) {
			return new LetExpression(items);
		} else if (Keywords.UNQUOTE.equals(symbol)) {
			return new UnquoteExpression(items);
		} else if (Keywords.QUASIQUOTE.equals(symbol)) {
			return new QuasiquoteExpression(items);
		} else if (Keywords.DEFMACRO.equals(symbol)) {
			return new MacroDefinitionExpression(items);
		} else {
			return new ProcedureExpression(items);
		}
	}
}
