package metrics;

import java.util.LinkedHashMap;
import java.util.ListIterator;
import java.util.Map;

import ast.Access;
import ast.ClassObject;
import ast.FieldObject;
import ast.SystemObject;
import ast.TypeObject;

public class AIF {

	private SystemObject system;
	private Map<ClassObject, Integer> inheritedFieldMap;
	private Map<String, Integer> classFieldMap;
	private Map<String, Integer> allclassFieldMap;

	public AIF(SystemObject system) {

		this.system = system;
		this.inheritedFieldMap = new LinkedHashMap<ClassObject, Integer>();
		this.classFieldMap = new LinkedHashMap<String, Integer>();
		this.allclassFieldMap = new LinkedHashMap<String, Integer>();

		ListIterator<ClassObject> classIterator = system.getClassListIterator();

		while (classIterator.hasNext()) {
			ClassObject classObject = classIterator.next();
			int inheritedFieldCount = 0;

			allclassFieldMap.put(classObject.getName(), classObject.getFieldList().toArray().length);
			ListIterator<TypeObject> interfaceIterator = classObject.getInterfaceIterator();

			if (interfaceIterator.hasNext()) {
				ListIterator<FieldObject> fieldIerator = classObject.getFieldIterator();

				while (fieldIerator.hasNext()) {
					FieldObject fieldObject = fieldIerator.next();
					Access access = fieldObject.getAccess();

					if (access == Access.PUBLIC) {
						inheritedFieldCount = inheritedFieldCount + 1;
					}

				}

				inheritedFieldMap.put(classObject, inheritedFieldCount);

			}

		}

		Compute();

	}

	private void Compute() {

		double totalInheritancecount = 0;
		double totalField = 0;
		double classInheritanceFactor = 0;

		for (Map.Entry<ClassObject, Integer> entry : inheritedFieldMap.entrySet()) {
			double inheritanceFactor = entry.getValue();
			ClassObject classObj = entry.getKey();

			if (!Double.isNaN(inheritanceFactor))
				classInheritanceFactor = (inheritanceFactor / classObj.getFieldList().size());

			// System.out.println("ClassName: " + classObj.getName() +
			// ",classInheritanceFactor" + classInheritanceFactor
			// + ",ClassMethodCount: " + classObj.getFieldList().size() +
			// ",classInheritanceFactor"
			// + classInheritanceFactor);

			if (!Double.isNaN(classInheritanceFactor))
				totalInheritancecount = totalInheritancecount + classInheritanceFactor;

		}

		System.out.println("AIF: " + totalInheritancecount / allclassFieldMap.size());
	}
}
