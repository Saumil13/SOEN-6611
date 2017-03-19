package metrics;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.IVariableBinding;
import org.eclipse.jdt.core.dom.VariableDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.jdt.core.dom.VariableDeclarationStatement;

import ast.Access;
import ast.ClassObject;
import ast.FieldObject;
import ast.MethodInvocationObject;
import ast.MethodObject;
import ast.SystemObject;

public class GlobalVeriable extends ASTVisitor {

	private SystemObject system;
	private Map<String, Integer> classMethodMap;;
	int totalClass = 0;

	public GlobalVeriable(SystemObject system) {

		this.system = system;
		this.classMethodMap = new LinkedHashMap<String, Integer>();

		calculateCoupling();

	}

	private void calculateCoupling() {
		ListIterator<ClassObject> classIterator = system.getClassListIterator();
		while (classIterator.hasNext()) {
			ClassObject classObject = classIterator.next();

			List<FieldObject> fieldObjects = classObject.getFieldList();

			int count = 0;
			for (FieldObject fieldobject : fieldObjects) {

				if (fieldobject != null && fieldobject.getAccess() == Access.PUBLIC) {
					count = count + 1;
				}
			}

			if (count > 0) {
				if (!classMethodMap.containsKey(classObject.getName())) {
					classMethodMap.put(classObject.getName(), count);
				}
			}

		}
		
		for (Map.Entry<String, Integer> entry : classMethodMap.entrySet()) {
			String key = entry.getKey();
			Object value = entry.getValue();
			System.out.println("Key:" + key + ",Value" + value);
		}
	}

}
