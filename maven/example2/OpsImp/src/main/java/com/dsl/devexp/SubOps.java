package com.dsl.devexp;

public class SubOps implements Operation {
	public int op(int a, int b) {
		return a-b;
	}
	public String getDesc() {
		return "minus";
	}
}
