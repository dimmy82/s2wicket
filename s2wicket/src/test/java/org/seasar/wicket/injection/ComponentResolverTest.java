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

import junit.framework.TestCase;

import static org.easymock.EasyMock.*;

/**
 * {@link ComponentResolver}�̃e�X�g�P�[�X�N���X�ł��B
 * @author Yoichiro Tanaka
 */
public class ComponentResolverTest extends TestCase {

	/**
	 * �R���|�[�l���g����Seasar�R���|�[�l���g�����b�N�A�b�v���鏈�����e�X�g���܂��B
	 * @throws Exception ���炩�̗�O�����������Ƃ�
	 */
	public void testLookupComponentName() throws Exception {
		String componentName = "testComponent";
		TestComponent testComponent = new TestComponent();
		S2Container container = createMock(S2Container.class);
		expect(container.getComponent(componentName)).andReturn(testComponent);
		replay(container);
		IS2ContainerLocator containerLocator = createMock(IS2ContainerLocator.class);
		expect(containerLocator.get()).andReturn(container);
		replay(containerLocator);
		ComponentResolver target = new ComponentResolver(componentName, TestComponent.class, containerLocator);
		Object result = target.getTargetObject();
		assertSame(testComponent, result);
		verify(container);
		verify(containerLocator);
	}
	
	/**
	 * �R���|�[�l���g�̌^��Seasar�R���|�[�l���g�����b�N�A�b�v���鏈�����e�X�g���܂��B
	 * @throws Exception ���炩�̗�O�����������Ƃ�
	 */
	public void testLookupComponentType() throws Exception {
		String componentName = null;
		TestComponent testComponent = new TestComponent();
		S2Container container = createMock(S2Container.class);
		expect(container.getComponent(TestComponent.class)).andReturn(testComponent);
		expect(container.getComponent(TestComponent.class)).andReturn(testComponent);
		replay(container);
		IS2ContainerLocator containerLocator = createMock(IS2ContainerLocator.class);
		expect(containerLocator.get()).andReturn(container);
		expect(containerLocator.get()).andReturn(container);
		replay(containerLocator);
		ComponentResolver target = new ComponentResolver(componentName, TestComponent.class, containerLocator);
		Object result = target.getTargetObject();
		assertSame(testComponent, result);
		result = target.getTargetObject();
		assertSame(testComponent, result);
		verify(container);
		verify(containerLocator);
	}
	
	/**
	 * �R���|�[�l���g�̌^��null��n�����Ƃ��̃e�X�g���s���܂��B
	 * @throws Exception ���炩�̗�O�����������Ƃ�
	 */
	public void testComponentTypeNull() throws Exception {
		IS2ContainerLocator containerLocator = createMock(IS2ContainerLocator.class);
		try {
			new ComponentResolver("", null, containerLocator);
		} catch(IllegalArgumentException expected) {
			// N/A
		}
	}
	
	/**
	 * �R���|�[�l���g���]���o��null��n�����Ƃ��̃e�X�g���s���܂��B
	 * @throws Exception ���炩�̗�O�����������Ƃ�
	 */
	public void testContainerLocatorNull() throws Exception {
		try {
			new ComponentResolver("", TestComponent.class, null);
		} catch(IllegalArgumentException expected) {
			// N/A
		}
	}
	
	/**
	 * �e�X�g�p�̃R���|�[�l���g�N���X�ł��B
	 */
	private static class TestComponent {
	}

}
