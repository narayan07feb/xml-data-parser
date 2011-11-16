package org.abhi.parser;

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
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;

import org.abhi.parser.interfaces.ParserInterface;
import org.abhi.parser.utils.ParserUtils;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import android.os.Handler;
import android.view.View;

public class XmlParser extends DefaultHandler {
	protected HashMap<String, Method> mXMLToMethodMap = null;
	protected HashMap<String, Method> mXMLToMethodMapAttributes = null;

	protected HashMap<String, HashMap<String, Method>> internalMethods = null;

	protected StringBuilder currentValue;
	protected String mElementName = "";
	private View mView = null;
	private Handler mHandler = null;
	private boolean stop = false;
	private String methodReference = "";
	private int methodCount = -1;

	protected ParserInterface mIReceiver;
	protected Object currentObj = null;

	public XmlParser(String element, ParserInterface receiver, View view,
			Handler handler) {
		super();
		this.mElementName = element;
		this.mIReceiver = receiver;
		this.mView = view;
		this.mHandler = handler;
		this.mXMLToMethodMap = ParserUtils
				.mapXMLElementsToClassMethods(receiver.getClass(element));
		this.mXMLToMethodMapAttributes = ParserUtils
				.mapXMLAttributesToClassMethods(receiver.getClass(element));
		this.internalMethods = ParserUtils
				.mapXMLElementsToClassMethodsInternal(receiver
						.getClass(element));
	}

	/**
	 * Pass true if want to stop receiving the response from this parser. The
	 * parser will continue parsing but will stop sending the call backs
	 * 
	 * @param stop
	 */
	public void stopExecution(boolean stop) {
		this.stop = stop;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.xml.sax.helpers.DefaultHandler#startElement(java.lang.String,
	 * java.lang.String, java.lang.String, org.xml.sax.Attributes)
	 */
	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		if (stop)
			return;
		if (localName.equalsIgnoreCase(mElementName) && currentObj == null) {
			currentObj = mIReceiver.getNewInstance(mElementName);
		}
		if (currentObj != null && internalMethods != null) {
			if (internalMethods.get(localName) != null) {
				methodReference = localName;
				methodCount = internalMethods.get(localName).size();
			}
			for (int iTemp = 0; iTemp < attributes.getLength(); iTemp++) {
				try {
					Method setMethod = mXMLToMethodMapAttributes.get(attributes
							.getLocalName(iTemp));
					if (setMethod != null
							&& "".equalsIgnoreCase(methodReference)) {
						setMethod.invoke(currentObj, currentValue.toString());
					}
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (NullPointerException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				} catch (SecurityException e) {
					e.printStackTrace();
				}
			}
		}
		currentValue = new StringBuilder();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.xml.sax.helpers.DefaultHandler#endElement(java.lang.String,
	 * java.lang.String, java.lang.String)
	 */
	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		if (stop)
			return;
		Method setMethod = mXMLToMethodMap.get(localName);
		try {
			if (setMethod != null && currentObj != null)
				setMethod.invoke(currentObj, currentValue.toString());
			if (internalMethods != null && !"".equalsIgnoreCase(methodReference)) {
				if (methodCount > -1) {
					methodCount--;
					Method method = internalMethods
							.get(methodReference).get(localName);
					if (method != null){	
						Class<?> partypes[] = new Class[0];
						Object internalObject = null;
			            Method meth = mIReceiver.getClass(mElementName).getMethod(
			              methodReference, partypes);
			           internalObject = meth.invoke(currentObj, null);
						method.invoke(internalObject,
								currentValue.toString());
					}
				}
				if (methodCount == -1) {
					methodReference = "";
				}
			}
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (NullPointerException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} finally {
			if (localName.equalsIgnoreCase(mElementName)) {
				mIReceiver.onReceive(currentObj, mElementName, mHandler, mView);
				currentObj = null;
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.xml.sax.helpers.DefaultHandler#characters(char[], int, int)
	 */
	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		super.characters(ch, start, length);
		if (stop)
			return;
		currentValue.append(new String(ch, start, length));
	}
}