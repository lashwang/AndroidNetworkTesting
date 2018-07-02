/* 
 * Copyright 2015-2025 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package sockslib.common;

/**
 * 
 * The class <code>AuthenticationException</code> represents a
 * authentication exception.
 * 
 * @author Youchao Feng
 * @date Mar 24, 2015 9:11:35 PM
 * @version 1.0
 *
 */
public class AuthenticationException extends SocksException{

	public AuthenticationException(String msg) {
		super(msg);
	}

	private static final long serialVersionUID = 1L;

}
