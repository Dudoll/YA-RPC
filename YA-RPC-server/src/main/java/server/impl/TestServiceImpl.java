package server.impl;

import facade.TestService;

public class TestServiceImpl implements TestService {

	public Float sum(Float a, Float b) {
		return a + b;
	}

	public String uppercase(String str) {
		return str.toUpperCase();
	}

}
