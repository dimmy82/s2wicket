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

import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.impl.S2ContainerImpl;

import wicket.protocol.http.MockWebApplication;
import junit.framework.TestCase;

/**
 * {@link S2ContainerLocator}�̃e�X�g�P�[�X�N���X�ł��B
 * @author Yoichiro Tanaka
 * @since 1.0.0
 */
public class S2ContainerLocatorTest extends TestCase {

	/**
	 * {@link S2ContainerLocator#get()}���\�b�h�̃e�X�g���s���܂��B
	 * @throws Exception ���炩�̗�O�����������Ƃ�
	 */
	public void testGet() throws Exception {
		// ���҂���I�u�W�F�N�g
		S2Container expected = new S2ContainerImpl();
		// �Z�b�g�A�b�v
		MockWebApplication application = new MockWebApplication(null);
		S2ContainerHolder.store(application, expected);
		// �Ώۃ��\�b�h�Ăяo��
		S2ContainerLocator target = new S2ContainerLocator();
		S2Container actual = target.get();
		// ����
		assertSame(expected, actual);
	}

}
