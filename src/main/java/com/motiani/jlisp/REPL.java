package com.motiani.jlisp;

import java.util.Scanner;

/**
 * Hello world!
 *
 */
public class REPL {
	private static Scope createGlobalScope() {
		Scope scope = new Scope(null);
		scope.create("+", NativeFunctions.addition());
		scope.create("-", NativeFunctions.subtraction());
		scope.create("*", NativeFunctions.mutliplication());
		scope.create("/", NativeFunctions.division());
		scope.create("=", NativeFunctions.numberEquality());
		scope.create("abs", NativeFunctions.abs());

		return scope;
	}

	public static void main(String[] args) {
		Parser parser = new Parser();
		Scope globalScope = createGlobalScope();
		Scanner sc = new Scanner(System.in);
		while (true) {
			String input = sc.nextLine();
			Expression ex = parser.parse(input);
			Expression result = ex.evaluate(globalScope);
		}
	}
}
