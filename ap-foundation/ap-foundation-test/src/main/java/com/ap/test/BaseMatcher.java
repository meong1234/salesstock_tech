package com.ap.test;

import java.util.Arrays;
import java.util.List;

import org.axonframework.domain.Message;
import org.axonframework.test.matchers.Matchers;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;

public class BaseMatcher {

	@Factory
	public static Matcher<List<? extends Message<?>>> aSequenceOf(Matcher<?>... matchers) {
		Matcher<?>[] terminatedListOfMatchers = Arrays.copyOf(matchers, matchers.length + 1);
		terminatedListOfMatchers[matchers.length] = Matchers.andNoMore();
		return Matchers.payloadsMatching(Matchers.exactSequenceOf(terminatedListOfMatchers));
	}
}
