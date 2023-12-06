package telran.test;

import java.lang.reflect.Method;
import java.util.*;

import telran.reflect.BeforeEach;
import telran.reflect.Test;

public class TestLibrary {
	public static void launchTest(Object testObj) throws Exception {
		Method[] methods = testObj.getClass().getDeclaredMethods();
		List<Method> beforeEachMethods = getBeforeEachMethods(methods);
		for(Method method : methods) {
			if(method.isAnnotationPresent(Test.class)) {
				beforeEachMethods.forEach(m -> {
					try {
						m.invoke(testObj);
					} catch (Exception e) {
						throw new RuntimeException(e);
					}
				});
				method.setAccessible(true);
				method.invoke(testObj);
			}
		}
	}

	private static List<Method> getBeforeEachMethods(Method[] methods) {
		List<Method> res = new ArrayList<>();
		for(Method method : methods) {
			if (method.isAnnotationPresent(BeforeEach.class)) {
				method.setAccessible(true);
				res.add(method);
			}
		}
		return res;
	}

}
