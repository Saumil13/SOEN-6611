package metrics;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;

import ast.Access;
import ast.ClassObject;
import ast.MethodInvocationObject;
import ast.MethodObject;
import ast.SystemObject;
import ast.TypeObject;

public class MIF {

	private SystemObject system;
	private Map<ClassObject, Integer> inheritedMethodMap;
	private Map<String, Integer> allclassMethodMap;

	public MIF(SystemObject system) {

		this.system = system;
		this.inheritedMethodMap = new LinkedHashMap<ClassObject, Integer>();

		this.allclassMethodMap = new LinkedHashMap<String, Integer>();

		ListIterator<ClassObject> classIterator = system.getClassListIterator();

		while (classIterator.hasNext()) {
			ClassObject classObject = classIterator.next();
			int inheritedMethodCount = 0;

			allclassMethodMap.put(classObject.getName(), classObject.getNumberOfMethods());
			ListIterator<TypeObject> interfaceIterator = classObject.getInterfaceIterator();

			if (interfaceIterator.hasNext()) {
				ListIterator<MethodObject> methodIterator = classObject.getMethodIterator();

				while (methodIterator.hasNext()) {
					MethodObject methodObject = methodIterator.next();
					Access access = methodObject.getAccess();

					if (access == Access.PUBLIC) {
						inheritedMethodCount = inheritedMethodCount + 1;
					}

				}
				inheritedMethodMap.put(classObject, inheritedMethodCount);
			}

		}

		Compute();

	}

	private void Compute() {

		double classInheritanceFactor = 0;
		double total = 0;

		for (Map.Entry<ClassObject, Integer> entry : inheritedMethodMap.entrySet()) {

			double inheritanceMethodCount = entry.getValue();
			ClassObject classObj = entry.getKey();

			classInheritanceFactor = (inheritanceMethodCount / classObj.getMethodList().size());
			total = total + classInheritanceFactor;
			// System.out.println("ClassName: "+classObj.getName() +"
			// ,InteritanceMethodCount" +inheritanceMethodCount + "
			// ,ClassMethodCount: "+classObj.getMethodList().size() +"
			// ,classInheritanceFactor"+ classInheritanceFactor);

		}

		System.out.println("MIF: " + classInheritanceFactor / allclassMethodMap.size());
	}
}