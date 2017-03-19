package metrics;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;

import ast.ClassObject;
import ast.MethodObject;
import ast.SystemObject;

public class AHF {

	private SystemObject system;
	private Map<String, LinkedHashMap<String, Integer>> importCouplingMap;
	private Map<String, Integer> classFieldMap;

	public AHF(SystemObject system) {

		this.system = system;
		this.importCouplingMap = new LinkedHashMap<String, LinkedHashMap<String, Integer>>();
		this.classFieldMap = new LinkedHashMap<String, Integer>();

		List<String> classNames = system.getClassNames();
		for (String className : classNames) {
			LinkedHashMap<String, Integer> map = new LinkedHashMap<String, Integer>();
			for (String className2 : classNames) {
				map.put(className2, 0);
			}
			importCouplingMap.put(className, map);
		}

		calculateCoupling();
		calculateAHF();

	}

	private void calculateCoupling() {
		ListIterator<ClassObject> classIterator = system.getClassListIterator();
		while (classIterator.hasNext()) {
			ClassObject classObject = classIterator.next();
			LinkedHashMap<String, Integer> map = importCouplingMap.get(classObject.getName());
			ListIterator<MethodObject> methodIterator = classObject.getMethodIterator();

			if (!classFieldMap.containsKey(classObject.getName())) {

				if (classObject.getFieldList() != null) {
					classFieldMap.put(classObject.getName(), classObject.getFieldList().toArray().length);
				} else {
					classFieldMap.put(classObject.getName(), 0);
				}
			}

			while (methodIterator.hasNext()) {
				MethodObject method = methodIterator.next();
				if (method.getMethodBody() != null) {

					List<ast.FieldInstructionObject> fieldInstructions = method.getFieldInstructions();
					for (ast.FieldInstructionObject fieldInstruction : fieldInstructions) {
						String fieldInstructionOrigin = fieldInstruction.getOwnerClass();
						if (map.keySet().contains(fieldInstructionOrigin) && !fieldInstruction.isStatic()) {
							map.put(fieldInstructionOrigin, map.get(fieldInstructionOrigin) + 1);
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

	private double getSystemFieldCount() {
		double sum = 0;

		for (Map.Entry<String, Integer> entry : classFieldMap.entrySet()) {
			sum += entry.getValue();
		}
		return sum;
	}

	private void calculateAHF() {

		// System.out.println("getSystemCoupling: " + getSystemCoupling());
		// System.out.println("getSystemFieldCount: " + getSystemFieldCount());
		double fieldVisible = ((getSystemCoupling()) / getSystemFieldCount());
		double AHF =  fieldVisible;
		System.out.println("AHF: " + AHF);
	}

}
