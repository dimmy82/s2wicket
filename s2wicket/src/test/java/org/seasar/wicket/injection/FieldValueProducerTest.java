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

import org.seasar.framework.container.S2Container;

import junit.framework.TestCase;

import static org.easymock.EasyMock.*;

/**
 * {@link FieldValueProducer}�̃e�X�g�P�[�X�N���X�ł��B
 * @author Yoichiro Tanaka
 */
public class FieldValueProducerTest extends TestCase {
	
	/**
	 * �R���X�g���N�^��null��n�����Ƃ��̃e�X�g���s���܂��B
	 * @throws Exception ���炩�̗�O�����������Ƃ�
	 */
	public void testConstructorContainerLocatorNull() throws Exception {
		try {
			new FieldValueProducer(null);
			fail("IllegalArgumentException not thrown.");
		} catch(IllegalArgumentException expected) {
			// N/A
		}
	}
	
	/**
	 * {@link FieldValueProducer#isSupported(Field)}�̃e�X�g���s���܂��B
	 * @throws Exception ���炩�̗�O�����������Ƃ�
	 */
	public void testIsSupported() throws Exception {
		IS2ContainerLocator containerLocator = createMock(IS2ContainerLocator.class);
		FieldValueProducer target = new FieldValueProducer(containerLocator);
		Field field = Component.class.getDeclaredField("annotationFieldTest");
		assertTrue(target.isSupported(field));
		field = Component.class.getDeclaredField("fieldTest");
		assertFalse(target.isSupported(field));
	}
	
	/**
	 * {@link FieldValueProducer#getValue(Field)}�̃e�X�g���s���܂��B
	 * ������null��n�����Ƃ��̃e�X�g���s���܂��B
	 * @throws Exception ���炩�̗�O�����������Ƃ�
	 */
	public void testGetValueNull() throws Exception {
		IS2ContainerLocator containerLocator = createMock(IS2ContainerLocator.class);
		FieldValueProducer target = new FieldValueProducer(containerLocator);
		try {
			target.getValue(null);
			fail("IllegalArgumentException not thrown.");
		} catch(IllegalArgumentException expected) {
			// N/A
		}
	}
	
	/**
	 * {@link FieldValueProducer#getValue(Field)}�̃e�X�g���s���܂��B
	 * @throws Exception ���炩�̗�O�����������Ƃ�
	 */
	public void testGetValue() throws Exception {
		Service service = createMock(Service.class);
		service.foo();
		S2Container container = createMock(S2Container.class);
		expect(container.getComponent(Service.class)).andReturn(service);
		IS2ContainerLocator containerLocator = createMock(IS2ContainerLocator.class);
		expect(containerLocator.get()).andReturn(container);
		replay(service);
		replay(container);
		replay(containerLocator);
		FieldValueProducer target = new FieldValueProducer(containerLocator);
		Field field = Component.class.getDeclaredField("annotationFieldTest");
		Object result = target.getValue(field);
		assertNotNull(result);
		((Service)result).foo();
		verify(containerLocator);
		verify(container);
		verify(service);
	}
	
	/**
	 * {@link FieldValueProducer#getValue(Field)}�̃e�X�g���s���܂��B
	 * �A�m�e�[�V�������t�^����Ă��Ȃ��t�B�[���h��n�����Ƃ��̃e�X�g���s���܂��B
	 * @throws Exception ���炩�̗�O�����������Ƃ�
	 */
	public void testGetValueNotAnnotation() throws Exception {
		IS2ContainerLocator containerLocator = createMock(IS2ContainerLocator.class);
		FieldValueProducer target = new FieldValueProducer(containerLocator);
		try {
			Field field = Component.class.getDeclaredField("fieldTest");
			target.getValue(field);
			fail("AnnotationNotPresentsException not thrown.");
		} catch(AnnotationNotPresentsException expected) {
			// N/A
		}
	}
	
	/**
	 * �A�m�e�[�V�������t�^���ꂽ�t�B�[���h�ƕt�^����Ă��Ȃ��t�B�[���h�����e�X�g�p�̃N���X�ł��B
	 */
	private static class Component {
		
		/** �A�m�e�[�V�������t�^���ꂽ�t�B�[���h */
		@SeasarComponent
		private Service annotationFieldTest;
		
		/** �A�m�e�[�V�������t�^����Ă��Ȃ��t�B�[���h */
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
