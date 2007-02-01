/*
 * $Id$
 * 
 * ==============================================================================
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package org.seasar.wicket.injection;

import java.io.Serializable;

import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.impl.S2ContainerImpl;

import wicket.protocol.http.MockWebApplication;

import junit.framework.TestCase;

/**
 * {@link S2ContainerHolder}�̃e�X�g�P�[�X�N���X�ł��B
 * @author Yoichiro Tanaka
 */
public class S2ContainerHolderTest extends TestCase {

	/**
	 * {@link S2ContainerHolder#store(wicket.Application, S2Container)}�̃e�X�g���s���܂��B
	 * @throws Exception ���炩�̗�O�����������Ƃ�
	 */
	public void testStore() throws Exception {
		MockWebApplication application = new MockWebApplication(null);
		S2Container expected = new S2ContainerImpl();
		S2ContainerHolder.store(application, expected);
		Serializable actual = application.getMetaData(S2ContainerHolder.META_DATA_KEY);
		assertTrue(actual instanceof S2ContainerHolder);
		assertSame(expected, ((S2ContainerHolder)actual).getContainer());
	}
	
	/**
	 * {@link S2ContainerHolder#store(wicket.Application, S2Container)}�̃e�X�g���s���܂��B
	 * Seasar�R���e�i��null�̏ꍇ�ɗ�O���������邱�Ƃ��m�F���܂��B
	 * @throws Exception ���炩�̗�O�����������Ƃ�
	 */
	public void testStoreContainerNull() throws Exception {
		MockWebApplication application = new MockWebApplication(null);
		S2Container container = null;
		try {
			S2ContainerHolder.store(application, container);
			fail("IllegalArgumentException not thrown");
		} catch(IllegalArgumentException expected) {
			// N/A
		}
	}

}
