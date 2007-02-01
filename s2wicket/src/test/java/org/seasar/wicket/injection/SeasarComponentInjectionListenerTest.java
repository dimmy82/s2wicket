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

import wicket.Component;
import wicket.markup.MarkupStream;
import wicket.protocol.http.MockWebApplication;

import junit.framework.TestCase;

import static org.easymock.EasyMock.*;

/**
 * {@link SeasarComponentInjectionListener}�̃e�X�g�P�[�X�N���X�ł��B
 * @author Yoichiro Tanaka
 */
public class SeasarComponentInjectionListenerTest extends TestCase {

	/**
	 * {@link SeasarComponentInjectionListener#SeasarComponentInjectionListener(wicket.protocol.http.WebApplication, S2Container)}
	 * �̈�����null��n�����Ƃ��̃e�X�g���s���܂��B
	 * @throws Exception ���炩�̗�O�����������Ƃ�
	 */
	public void testConstructorNull() throws Exception {
		try {
			S2Container container = createMock(S2Container.class);
			new SeasarComponentInjectionListener(null, container);
			fail("IllegalArgumentException not thrown.");
		} catch(IllegalArgumentException expected) {
			// N/A
		}
		try {
			new SeasarComponentInjectionListener(new MockWebApplication(null), null);
			fail("IllegalArgumentException not thrown.");
		} catch(IllegalArgumentException expected) {
			// N/A
		}
	}
	
	/**
	 * {@link SeasarComponentInjectionListener#onInstantiation(Component)}�̃e�X�g���s���܂��B
	 * @throws Exception ���炩�̗�O�����������Ƃ�
	 */
	public void testOnInstantiation() throws Exception {
		Service service = createMock(Service.class);
		service.foo();
		S2Container container = createMock(S2Container.class);
		expect(container.getComponent(Service.class)).andReturn(service);
		replay(service);
		replay(container);
		SeasarComponentInjectionListener target =
			new SeasarComponentInjectionListener(new MockWebApplication(null), container);
		TestComponent component = new TestComponent();
		target.onInstantiation(component);
		component.service.foo();
		verify(service);
		verify(container);
	}
	
	/**
	 * �C���W�F�N�V�����Ώۂ̃t�B�[���h�����N���X�ł��B
	 */
	private static class TestComponent extends Component {
		
		@SeasarComponent
		Service service;

		public TestComponent() {
			super("test1");
		}

		@Override
		protected void onRender(MarkupStream markupStream) {
		}
		
	}

	/**
	 * �C���W�F�N�V���������I�u�W�F�N�g�̃C���^�t�F�[�X�ł��B
	 */
	private static interface Service {
		
		public void foo();
		
	}
	
}
