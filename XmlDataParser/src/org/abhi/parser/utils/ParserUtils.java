package org.abhi.parser.utils;

/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

public abstract class ParserUtils {

	/**
	 * Generates HashMap for the class methods and respective tags
	 * 
	 * @param class
	 * @return
	 */
	public static final HashMap<String, Method> mapXMLElementsToClassMethods(
			Class<?> cls) {
		HashMap<String, Method> retVal = new HashMap<String, Method>();
		for (Method method : cls.getMethods()) {
			MapsToXMLElement mapsTo = method
					.getAnnotation(MapsToXMLElement.class);
			if (mapsTo != null) {
				String value = mapsTo.value();
				retVal.put(value.trim(), method);
			}
		}
		return retVal;
	}

	/**
	 * @author Abhinava Srivastava
	 */
	@Retention(RetentionPolicy.RUNTIME)
	public @interface MapsToXMLElement {
		public String value();
	}

	/**
	 * Generates HashMap for the class methods and respective tags
	 * 
	 * @param class
	 * @return
	 */
	public static final HashMap<String, Method> mapXMLAttributesToClassMethods(
			Class<?> cls) {
		HashMap<String, Method> retVal = new HashMap<String, Method>();
		for (Method method : cls.getMethods()) {
			MapsToXMLAttributes mapsTo = method
					.getAnnotation(MapsToXMLAttributes.class);
			if (mapsTo != null) {
				String value = mapsTo.value();
				retVal.put(value.trim(), method);
			}
		}
		return retVal;
	}

	/**
	 * @author Abhinava Srivastava
	 */
	@Retention(RetentionPolicy.RUNTIME)
	public @interface MapsToXMLAttributes {
		public String value();
	}

	public static HashMap<String, HashMap<String, Method>> mapXMLElementsToClassMethodsInternal(
			Class<?> cls) {
		HashMap<String, Method> retVal = new HashMap<String, Method>();
		for (Method method : cls.getMethods()) {
			MapsToXMLSubMethods mapsTo = method
					.getAnnotation(MapsToXMLSubMethods.class);
			if (mapsTo != null) {
				String value = mapsTo.value();
				retVal.put(value.trim(), method);
			}
		}

		HashMap<String, HashMap<String, Method>> internals = null;
		if (retVal.size() > 0) {
			internals = new HashMap<String, HashMap<String, Method>>();
			Set<String> keys = retVal.keySet();
			Iterator<String> keyIterator = keys.iterator();
			while (keyIterator.hasNext()) {
				String setName = keyIterator.next();
				Class<?> partypes[] = new Class[0];
				try {
					Method meth = cls.getMethod(
					          setName, partypes);
					internals.put(setName,
							mapXMLElementsToClassMethods(meth.getReturnType()));
				} catch (SecurityException e1) {
					e1.printStackTrace();
				} catch (NoSuchMethodException e1) {
					e1.printStackTrace();
				}
			}
		}
		return internals;
	}

	/**
	 * @author Abhinava Srivastava
	 */
	@Retention(RetentionPolicy.RUNTIME)
	public @interface MapsToXMLSubMethods {
		public String value();
	}

}
