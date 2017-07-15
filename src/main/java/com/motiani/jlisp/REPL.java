package com.motiani.jlisp;

import java.util.Scanner;

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

		return scope;
	}

	public static void main(String[] args) {
		Parser parser = new Parser();
		Scope globalScope = createGlobalScope();

		Scanner sc = new Scanner(System.in);
		// TODO: For some weird reason this doesn't always work. Will have to
		// figure out.
		// When Ctrl + D is hit, hasNextLine returns false. So we end the
		// loop, and close the scanner
		while (sc.hasNextLine()) {
			try {
				String input = sc.nextLine();
				Expression ex = parser.parse(input);
				Type result = ex.evaluate(globalScope);
				System.out.println(result.getDisplayValue());
			} catch (Exception e) {
				System.out.println(e);
			}
		}

		sc.close();
	}
}
