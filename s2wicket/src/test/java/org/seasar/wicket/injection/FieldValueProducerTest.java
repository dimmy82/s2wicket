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

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.seasar.framework.container.S2Container;

import junit.framework.TestCase;

import static org.easymock.EasyMock.*;

/**
 * {@link FieldValueProducer}�̃e�X�g�P�[�X�N���X�ł��B
 * @author Yoichiro Tanaka
 * @since 1.0.0
 */
public class FieldValueProducerTest extends TestCase {
	
	/**
	 * �R���X�g���N�^�ɕs���Ȓl��n�����Ƃ��̃e�X�g���s���܂��B
	 * @throws Exception ���炩�̗�O�����������Ƃ�
	 */
	public void testConstructorContainerLocatorInvalidArg() throws Exception {
		try {
			new FieldValueProducer(null, null);
			fail("IllegalArgumentException not thrown.");
		} catch(IllegalArgumentException expected) {
			assertTrue(expected.getMessage().startsWith("containerLocator"));
		}
		try {
			IS2ContainerLocator containerLocator = createMock(IS2ContainerLocator.class);
			new FieldValueProducer(containerLocator, null);
			fail("IllegalArgumentException not thrown.");
		} catch(IllegalArgumentException expected) {
			assertTrue(expected.getMessage().equals("fieldFilterList is null."));
		}
		try {
			IS2ContainerLocator containerLocator = createMock(IS2ContainerLocator.class);
			List<FieldFilter> fieldFilters = new ArrayList<FieldFilter>();
			new FieldValueProducer(containerLocator, fieldFilters);
			fail("IllegalArgumentException not thrown.");
		} catch(IllegalArgumentException expected) {
			assertTrue(expected.getMessage().equals("fieldFilterList is empty."));
		}
	}
	
	/**
	 * {@link FieldValueProducer#isSupported(Field)}�̃e�X�g���s���܂��B
	 * @throws Exception ���炩�̗�O�����������Ƃ�
	 */
	public void testIsSupported() throws Exception {
		Field field = Component.class.getDeclaredField("fieldTest");
		IS2ContainerLocator containerLocator = createMock(IS2ContainerLocator.class);
		// �t�B���^��true��Ԃ��ꍇ
		FieldFilter fieldFilter = createMock(FieldFilter.class);
		expect(fieldFilter.isSupported(field)).andReturn(true);
		replay(fieldFilter);
		List<FieldFilter> filters = new ArrayList<FieldFilter>();
		filters.add(fieldFilter);
		FieldValueProducer target = new FieldValueProducer(containerLocator, filters);
		FieldFilter result = target.isSupported(field);
		assertNotNull(result);
		assertSame(fieldFilter, result);
		verify(fieldFilter);
		reset(fieldFilter);
		// �t�B���^��false��Ԃ��ꍇ
		expect(fieldFilter.isSupported(field)).andReturn(false);
		replay(fieldFilter);
		filters = new ArrayList<FieldFilter>();
		filters.add(fieldFilter);
		target = new FieldValueProducer(containerLocator, filters);
		result = target.isSupported(field);
		assertNull(result);
		verify(fieldFilter);
	}
	
	/**
	 * {@link FieldValueProducer#getValue(Field)}�̃e�X�g���s���܂��B
	 * ������null��n�����Ƃ��̃e�X�g���s���܂��B
	 * @throws Exception ���炩�̗�O�����������Ƃ�
	 */
	public void testGetValueNull() throws Exception {
		IS2ContainerLocator containerLocator = createMock(IS2ContainerLocator.class);
		FieldFilter fieldFilter = createMock(FieldFilter.class);
		List<FieldFilter> filters = new ArrayList<FieldFilter>();
		filters.add(fieldFilter);
		FieldValueProducer target = new FieldValueProducer(containerLocator, filters);
		try {
			target.getValue(null);
			fail("IllegalArgumentException not thrown.");
		} catch(IllegalArgumentException expected) {
			// N/A
		}
	}
	
	/**
	 * {@link FieldValueProducer#getValue(SupportedField)}�̃e�X�g���s���܂��B
	 * �^�Ń��b�N�A�b�v����P�[�X���e�X�g���܂��B
	 * @throws Exception ���炩�̗�O�����������Ƃ�
	 */
	public void testGetValueByType() throws Exception {
		Field field = Component.class.getDeclaredField("fieldTest");
		Service service = createMock(Service.class);
		service.foo();
		S2Container container = createMock(S2Container.class);
		expect(container.getComponent(Service.class)).andReturn(service);
		IS2ContainerLocator containerLocator = createMock(IS2ContainerLocator.class);
		expect(containerLocator.get()).andReturn(container);
		FieldFilter fieldFilter = createMock(FieldFilter.class);
		expect(fieldFilter.getLookupComponentName(field)).andReturn(null);
		replay(service);
		replay(container);
		replay(containerLocator);
		replay(fieldFilter);
		List<FieldFilter> filters = new ArrayList<FieldFilter>();
		filters.add(fieldFilter);
		FieldValueProducer target = new FieldValueProducer(containerLocator, filters);
		SupportedField supportedField = new SupportedField(field, fieldFilter);
		Object result = target.getValue(supportedField);
		assertNotNull(result);
		((Service)result).foo();
		verify(containerLocator);
		verify(container);
		verify(service);
		verify(fieldFilter);
	}
	
	/**
	 * {@link FieldValueProducer#getValue(Field)}�̃e�X�g���s���܂��B
	 * ���O�Ń��b�N�A�b�v����P�[�X���e�X�g���܂��B
	 * @throws Exception ���炩�̗�O�����������Ƃ�
	 */
	public void testGetValueByName() throws Exception {
		Field field = Component.class.getDeclaredField("fieldTest");
		Service service = createMock(Service.class);
		service.foo();
		S2Container container = createMock(S2Container.class);
		expect(container.getComponent("component1")).andReturn(service);
		IS2ContainerLocator containerLocator = createMock(IS2ContainerLocator.class);
		expect(containerLocator.get()).andReturn(container);
		FieldFilter fieldFilter = createMock(FieldFilter.class);
		expect(fieldFilter.getLookupComponentName(field)).andReturn("component1");
		replay(service);
		replay(container);
		replay(containerLocator);
		replay(fieldFilter);
		List<FieldFilter> filters = new ArrayList<FieldFilter>();
		filters.add(fieldFilter);
		FieldValueProducer target = new FieldValueProducer(containerLocator, filters);
		SupportedField supportedField = new SupportedField(field, fieldFilter);
		Object result = target.getValue(supportedField);
		assertNotNull(result);
		((Service)result).foo();
		verify(containerLocator);
		verify(container);
		verify(service);
		verify(fieldFilter);
	}
	
	/**
	 * �e�X�g�p�̃N���X�ł��B
	 */
	private static class Component {
		
		/** �e�X�g�p�̃t�B�[���h */
		private Service fieldTest;
		
	}
	
	/**
	 * �C���W�F�N�V���������I�u�W�F�N�g�̃C���^�t�F�[�X�ł��B
	 */
	private static interface Service  {
		
		/**
		 * �e�X�g�p�̃��\�b�h�ł��B
		 */
		public void foo();
		
	}

}
