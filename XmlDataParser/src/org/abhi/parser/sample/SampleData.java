package org.abhi.parser.sample;

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

import org.abhi.parser.utils.ParserUtils.MapsToXMLElement;

public class SampleData {

	private String title;
	private String description;
	private String link;
	private String media_text;

	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title
	 *            the title to set
	 */
	@MapsToXMLElement("title")
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description
	 *            the description to set
	 */
	@MapsToXMLElement("description")
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the link
	 */
	public String getLink() {
		return link;
	}

	/**
	 * @param link
	 *            the link to set
	 */
	@MapsToXMLElement("link")
	public void setLink(String link) {
		this.link = link;
	}

	/**
	 * @return the media_text
	 */
	public String getMedia_text() {
		return media_text;
	}

	/**
	 * @param media_text
	 *            the media_text to set
	 */
	@MapsToXMLElement("media:text")
	public void setMedia_text(String media_text) {
		this.media_text = media_text;
	}

}
