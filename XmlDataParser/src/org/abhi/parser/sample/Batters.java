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

public class Batters {
	
	private String batter = "";

	/**
	 * @return the batter
	 */
	public String getBatter() {
		return batter;
	}

	/**
	 * @param batter the batter to set
	 */
	@MapsToXMLElement("batter")
	public void setBatter(String batter) {
		this.batter = batter;
	}

}
