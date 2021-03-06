package com.motiani.jlisp;

class Keywords {
	static SymbolExpression IF = new SymbolExpression("if");
	static SymbolExpression DEFINE = new SymbolExpression("define");
	static SymbolExpression ASSIGN = new SymbolExpression("set!");
	static SymbolExpression LAMBDA = new SymbolExpression("lambda");
	static SymbolExpression QUOTE = new SymbolExpression("quote");
	static SymbolExpression LET = new SymbolExpression("let");
	static SymbolExpression UNQUOTE = new SymbolExpression("unquote");
	static SymbolExpression QUASIQUOTE = new SymbolExpression("quasiquote");
	static SymbolExpression DEFMACRO = new SymbolExpression("defmacro");
}
