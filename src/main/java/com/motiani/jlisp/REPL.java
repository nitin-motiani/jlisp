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

		Scanner sc = new Scanner(System.in);
		// When Ctrl + D is hit, hasNextLine returns false. So we end the loop,
		// and close the scanner
		while (sc.hasNextLine()) {
			try {
				String input = sc.nextLine();
				Expression ex = parser.parse(input);
				Expression result = ex.evaluate(globalScope);
				System.out.println(result.getPrintValue());
			} catch (Exception e) {
				System.out.println(e);
			}
		}

		sc.close();
	}
}
