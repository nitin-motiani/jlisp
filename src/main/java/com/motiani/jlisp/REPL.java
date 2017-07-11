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
		scope.create("list", NativeFunctions.list());
		scope.create("map", NativeFunctions.map());
		scope.create("car", NativeFunctions.car());
		scope.create("cdr", NativeFunctions.cdr());

		return scope;
	}

	public static void main(String[] args) {
		Parser parser = new Parser();
		Scope globalScope = createGlobalScope();
		// TODO: Use ctrl + D at some point for the closing of the REPL, and
		// close scanner
		Scanner sc = new Scanner(System.in);
		while (true) {
			try {
				String input = sc.nextLine();
				Expression ex = parser.parse(input);
				Expression result = ex.evaluate(globalScope);
				if (result instanceof ConstantExpression)
					System.out
							.println(((ConstantExpression) result).getValue());
				else if (result instanceof ListExpression) {
					System.out.print("(");
					for (Expression expr : ((ListExpression) result)
							.getExpressions()) {
						if (expr instanceof ConstantExpression)
							System.out.print(((ConstantExpression) expr)
									.getValue() + " ");
						else {
							System.out.println("Some-element");
						}
					}
					System.out.println(")");
				} else
					System.out.println("Will do something");
			} catch (Exception e) {
				System.out.println(e);
			}
		}
	}
}
