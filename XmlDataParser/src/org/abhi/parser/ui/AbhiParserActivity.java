package org.abhi.parser.ui;

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

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.abhi.parser.XmlParser;
import org.abhi.parser.interfaces.ParserInterface;
import org.abhi.parser.sample.MultiData;
import org.abhi.parser.sample.SampleData;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;


public class AbhiParserActivity extends Activity implements ParserInterface {
	/** Called when the activity is first created. */

	private ListView listView;
	private Handler handler;

	private ArrayList<SampleData> dataTypeOne;
	private ArrayList<MultiData> dataTypeTwo;
	private boolean fromOne = true;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		listView = (ListView) findViewById(R.id.listing);
		handler = new Handler();
	}

	Thread thread = null;

	public void listOne(View v) {
		listView.setAdapter(null);
		fromOne = true;
		showDialog(0);
		thread = new Thread() {
			XmlParser parsing = new XmlParser("item", AbhiParserActivity.this,
					listView, handler);

			public void run() {
				try {
					HttpURLConnection connection = (HttpURLConnection) (new URL(
							"http://sg.news.yahoo.com/rss/world")
							.openConnection());
					connection.connect();
					SAXParserFactory spf = SAXParserFactory.newInstance();
					SAXParser sp = spf.newSAXParser();
					XMLReader xr = sp.getXMLReader();
					xr.setContentHandler(parsing);
					xr.parse(new InputSource(connection.getInputStream()));
					handler.post(new Runnable() {
						public void run() {
							listView.setAdapter(new SampleAdapter());
							dismissDialog(0);
						}
					});

				} catch (MalformedURLException e) {
				} catch (IOException e) {
					e.printStackTrace();
				} catch (ParserConfigurationException e) {
					e.printStackTrace();
				} catch (SAXException e) {
					e.printStackTrace();
				}
			};

			@Override
			public void interrupt() {
				parsing.stopExecution(true);
				super.interrupt();
			}
		};
		thread.start();
	}

	public void listTwo(View v) {
		listView.setAdapter(null);
		fromOne = false;
		showDialog(0);
		thread = new Thread() {
			XmlParser parsing = new XmlParser("item", AbhiParserActivity.this,
					listView, handler);

			public void run() {
				try {
					HttpURLConnection connection = (HttpURLConnection) (new URL(
							"http://labs.adobe.com/technologies/spry/data/donuts.xml")
							.openConnection());
					connection.connect();
					SAXParserFactory spf = SAXParserFactory.newInstance();
					SAXParser sp = spf.newSAXParser();
					XMLReader xr = sp.getXMLReader();
					xr.setContentHandler(parsing);
					xr.parse(new InputSource(connection.getInputStream()));
					handler.post(new Runnable() {
						public void run() {
							
							listView.setAdapter(new SampleAdapter2());
							dismissDialog(0);
						}
					});

				} catch (MalformedURLException e) {
				} catch (IOException e) {
					e.printStackTrace();
				} catch (ParserConfigurationException e) {
					e.printStackTrace();
				} catch (SAXException e) {
					e.printStackTrace();
				}
			};

			@Override
			public void interrupt() {
				parsing.stopExecution(true);
				super.interrupt();
			}
		};
		thread.start();
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		ProgressDialog dlg = new ProgressDialog(this);
		dlg.setCancelable(true);
		dlg.setOnCancelListener(new DialogInterface.OnCancelListener() {
			public void onCancel(DialogInterface dialog) {
				thread.interrupt();
			}
		});
		dlg.setMessage("Loading content from server");
		return dlg;
	}

	public Class<?> getClass(String element) {
		if (fromOne) {
			return SampleData.class;
		}
		return MultiData.class;
	}

	public Object getNewInstance(String element) {
		if (fromOne) {
			return new SampleData();
		}
		return new MultiData();
	}

	public void onReceive(Object currentObj, String element, Handler handler,
			View view) {
		if (currentObj instanceof SampleData) {
			if (dataTypeOne == null)
				dataTypeOne = new ArrayList<SampleData>();
			dataTypeOne.add((SampleData) currentObj);
		}else{
			if(dataTypeTwo == null)
				dataTypeTwo = new ArrayList<MultiData>();
			dataTypeTwo.add((MultiData)currentObj);
		}
	}

	// //////////////////////////////////////////////////////////////////////////////
	class SampleAdapter extends BaseAdapter {

		public int getCount() {
			return dataTypeOne.size();
		}

		public Object getItem(int arg0) {
			return dataTypeOne.get(arg0);
		}

		public long getItemId(int arg0) {
			return arg0;
		}

		public View getView(int arg0, View arg1, ViewGroup arg2) {
			if (arg1 == null) {
				arg1 = View.inflate(AbhiParserActivity.this,
						R.layout.text_element, null);
			}
			((TextView) arg1.findViewById(R.id.text1)).setText(dataTypeOne.get(
					arg0).getTitle());
			((TextView) arg1.findViewById(R.id.text2)).setText(Html
					.fromHtml(dataTypeOne.get(arg0).getDescription()));
			return arg1;
		}
	}

	// //////////////////////////////////////////////////////////////////////////////
	class SampleAdapter2 extends BaseAdapter {

		public int getCount() {
			return dataTypeTwo.size();
		}

		public Object getItem(int arg0) {
			return dataTypeTwo.get(arg0);
		}

		public long getItemId(int arg0) {
			return arg0;
		}

		public View getView(int arg0, View arg1, ViewGroup arg2) {
			if (arg1 == null) {
				arg1 = View.inflate(AbhiParserActivity.this,
						R.layout.text_element, null);
			}
			((TextView) arg1.findViewById(R.id.text1)).setText(dataTypeTwo.get(
					arg0).getName());
			((TextView) arg1.findViewById(R.id.text2)).setText(Html
					.fromHtml(dataTypeTwo.get(arg0).batters().getBatter()));
			return arg1;
		}
	}

}