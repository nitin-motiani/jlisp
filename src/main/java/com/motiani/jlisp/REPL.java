package com.motiani.jlisp;

import java.util.LinkedList;

/**
 * Hello world!
 *
 */
public class REPL {
	private static Scope createGlobalScope() {
		Scope scope = new Scope(null);
		scope.create(new SymbolExpression("+"), NativeFunctions.addition());
		scope.create(new SymbolExpression("-"), NativeFunctions.subtraction());
		scope.create(new SymbolExpression("*"),
				NativeFunctions.mutliplication());
		scope.create(new SymbolExpression("/"), NativeFunctions.division());
		scope.create(new SymbolExpression("="),
				NativeFunctions.numberEquality());
		scope.create(new SymbolExpression("abs"), NativeFunctions.abs());
		scope.create(new SymbolExpression("list"), NativeFunctions.list());
		scope.create(new SymbolExpression("map"), NativeFunctions.map());
		scope.create(new SymbolExpression("car"), NativeFunctions.car());
		scope.create(new SymbolExpression("cdr"), NativeFunctions.cdr());
		scope.create(new SymbolExpression("len"), NativeFunctions.len());

		return scope;
	}

	public static void main(String[] args) {
		Parser parser = new Parser();
		Scope globalScope = createGlobalScope();

		Reader reader = new Reader(System.in);

		while (!reader.isEof()) {
			try {
				LinkedList<String> tokens = reader.getTokenizedInput();
				Expression ex = parser.parse(tokens);
				Data result = ex.evaluate(globalScope);
				System.out.println(result.getDisplayValue());
			} catch (Exception e) {
				System.out.println(e);
			}
		}

		reader.close();
	}
}
