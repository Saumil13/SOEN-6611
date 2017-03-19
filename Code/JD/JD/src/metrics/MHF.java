package metrics;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;

import ast.ClassObject;
import ast.FieldInstructionObject;
import ast.MethodInvocationObject;
import ast.MethodObject;
import ast.SystemObject;

public class MHF {

	private SystemObject system;
	private Map<String, LinkedHashMap<String, Integer>> importCouplingMap;
	private Map<String, Integer> classMethodMap;

	public MHF(SystemObject system) {

		this.system = system;
		this.importCouplingMap = new LinkedHashMap<String, LinkedHashMap<String, Integer>>();
		this.classMethodMap = new LinkedHashMap<String, Integer>();

		List<String> classNames = system.getClassNames();
		for (String className : classNames) {
			LinkedHashMap<String, Integer> map = new LinkedHashMap<String, Integer>();
			for (String className2 : classNames) {
				map.put(className2, 0);
			}
			importCouplingMap.put(className, map);
		}

		calculateCoupling();
		calculateMHF();
	}

	private void calculateCoupling() {
		ListIterator<ClassObject> classIterator = system.getClassListIterator();
		while (classIterator.hasNext()) {
			ClassObject classObject = classIterator.next();
			LinkedHashMap<String, Integer> map = importCouplingMap.get(classObject.getName());
			ListIterator<MethodObject> methodIterator = classObject.getMethodIterator();

			if (!classMethodMap.containsKey(classObject.getName())) {

				if (classObject.getMethodList() != null) {
					classMethodMap.put(classObject.getName(), classObject.getMethodList().toArray().length);
				} else {
					classMethodMap.put(classObject.getName(), 0);
				}
			}

			while (methodIterator.hasNext()) {
				MethodObject method = methodIterator.next();
				if (method.getMethodBody() != null) {
					List<MethodInvocationObject> methodInvocations = method.getMethodInvocations();
					for (MethodInvocationObject methodInvocation : methodInvocations) {
						String methodInvocationOrigin = methodInvocation.getOriginClassName();
						if (map.keySet().contains(methodInvocationOrigin)) {
							ClassObject originClass = system.getClassObject(methodInvocationOrigin);
							MethodObject originMethod = originClass.getMethod(methodInvocation);

							if (originMethod != null && !originMethod.isStatic())
								map.put(methodInvocationOrigin, map.get(methodInvocationOrigin) + 1);
						}
					}
				}
			}
		}
	}

	private double getClassAverageCoupling(String className) {
		LinkedHashMap<String, Integer> map = importCouplingMap.get(className);
		int sum = 0;
		Set<String> keySet = map.keySet();
		for (String key : keySet) {
			if (!key.equals(className))
				sum += map.get(key);
		}

		return (1 -(double) sum / (double) (keySet.size() - 1));
	}

	private double getSystemCoupling() {
		Set<String> keySet = importCouplingMap.keySet();
		double sum = 0;
		for (String key : keySet) {
			sum += getClassAverageCoupling(key);
			// System.out.println(key + " " + getClassAverageCoupling(key));
		}
		return sum;
	}

	private double getSystemMethodCount() {
		double sum = 0;

		for (Map.Entry<String, Integer> entry : classMethodMap.entrySet()) {
			sum += entry.getValue();
		}
		return sum;
	}

	private void calculateMHF() {

		// System.out.println("getSystemCoupling: " + getSystemCoupling());
		// System.out.println("getSystemMethodCount: " +
		// getSystemMethodCount());
		double methodVisible = ((getSystemCoupling()) / getSystemMethodCount());
		double MHF =  methodVisible;
		System.out.println("MHF: " + MHF);
	}

}
