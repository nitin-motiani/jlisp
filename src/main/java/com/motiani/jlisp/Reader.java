package com.motiani.jlisp;

import java.io.InputStream;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;

class Reader {
	private Scanner sc;
	boolean isClosed;

	Reader(InputStream source) {
		this.sc = new Scanner(source);
		this.isClosed = false;
	}

	boolean isEof() {
		return (!sc.hasNextLine());
	}

	void close() {
		if (isClosed)
			return;
		sc.close();
		isClosed = true;
	}

	// TODO: This function became uglier than I expected. Maybe clean up in some
	// way. Also the coupling with the parser is a bit of pain since the
	// function return type LinkedList is based on assumption that it makes the
	// job of Parser simpler

	// The reason for return type linked list is that we can easily peek top
	// elements and pop them from a linked list. The reason behind returning
	// LinkedList instead of List is that the contract is clear that a linked
	// list will be returned and callers know they pop element in O(1)
	LinkedList<String> getTokenizedInput() {
		LinkedList<String> tokens = new LinkedList<String>();
		int paranBalance = 0;
		// TODO: For some weird reason this doesn't always work. Will have to
		// figure out.
		// When Ctrl + D is hit, hasNextLine returns false. So we end the
		// loop, and close the scanner
		while (!isEof()) {
			String exp = sc.nextLine();

			// TODO: Figure this shit out
			if (exp == null)
				throw new IllegalArgumentException(
						"Can't parse null expression");

			// What this complicated regex does
			// Either match an opening or closing paran
			// Or match string which starts with anything other than double
			// quote or opening/closing parans,
			// and after that can have any number of non space characters apart
			// from parans. Or match a string which starts with a double quote
			// and can have any other characters after that and then ends with a
			// closing double quote
			Matcher m = Pattern.compile(
					"([\\(\\)]|[^\"\\(\\)][\\S&&[^\\(\\)]]*|\".*?\")\\s*")
					.matcher(exp);
			while (m.find()) {
				String token = m.group(1).trim();
				if ("(".equals(token)) {
					paranBalance++;
				}

				if (")".equals(token)) {
					paranBalance--;
				}
				tokens.add(token);
				if (paranBalance < 0) {
					throw new IllegalArgumentException("Paran mismatch");
				}
			}

			if (paranBalance == 0)
				break;

		}

		if (paranBalance < 0) {
			throw new IllegalArgumentException("Paran mismatch");
		}
		// I was hoping to get rid of this codes, but it seems empty strings
		// still slip through so filtering those
		return tokens.stream().filter(StringUtils::isNotEmpty)
				.collect(Collectors.toCollection(LinkedList::new));
	}
}
