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

import junit.framework.TestCase;

/**
 * {@link AnnotationFieldFilter}�N���X�̃e�X�g�P�[�X�N���X�ł��B
 * @author Yoichiro Tanaka
 * @since 1.1.0
 */
public class AnnotationFieldFilterTest extends TestCase {

	/** �e�X�g�ΏۃI�u�W�F�N�g */
	private AnnotationFieldFilter target;
	
	/**
	 * �O�������s���܂��B
	 * @throws Exception ���炩�̗�O�����������Ƃ�
	 */
	@Override
	protected void setUp() throws Exception {
		target = new AnnotationFieldFilter();
	}
	
	/**
	 * �㏈�����s���܂��B
	 * @throws Exception ���炩�̗�O�����������Ƃ�
	 */
	@Override
	protected void tearDown() throws Exception {
		target = null;
	}
	
	/**
	 * {@link AnnotationFieldFilter#isSupported(java.lang.reflect.Field)}�̃e�X�g���s���܂��B
	 * @throws Exception ���炩�̗�O�����������Ƃ�
	 */
	public void testIsSupported() throws Exception {
		Class clazz = TestField.class;
		assertTrue(target.isSupported(clazz.getDeclaredField("annotFieldWithoutName")));
		assertTrue(target.isSupported(clazz.getDeclaredField("annotFieldWithName")));
		assertFalse(target.isSupported(clazz.getDeclaredField("normalField")));
		try {
			target.isSupported(null);
			fail("IllegalArgumentException not thrown.");
		} catch(IllegalArgumentException expected) {
			// N/A
		}
	}
	
	/**
	 * {@link AnnotationFieldFilter#getLookupComponentName(java.lang.reflect.Field)}�̃e�X�g���s���܂��B
	 * @throws Exception ���炩�̗�O�����������Ƃ�
	 */
	public void testGteLookupComponentName() throws Exception {
		Class clazz = TestField.class;
		String result = target.getLookupComponentName(clazz.getDeclaredField("annotFieldWithName"));
		assertEquals("name1", result);
		result = target.getLookupComponentName(clazz.getDeclaredField("annotFieldWithoutName"));
		assertNull(result);
		try {
			target.getLookupComponentName(clazz.getDeclaredField("normalField"));
			fail("IllegalArgumentException not thrown.");
		} catch(IllegalArgumentException expected) {
			// N/A
		}
		try {
			target.getLookupComponentName(null);
			fail("IllegalArgumentException not thrown.");
		} catch(IllegalArgumentException expected) {
			// N/A
		}
	}
	
	/**
	 * �����p�̃t�B�[���h�����N���X�ł��B
	 */
	private static class TestField {
		
		/** name�����������Ȃ�SeasarComponent�A�m�e�[�V�������t�^���ꂽ�t�B�[���h */
		@SeasarComponent
		private Object annotFieldWithoutName;

		/** name����������SeasarComponent�A�m�e�[�V�������t�^���ꂽ�t�B�[���h */
		@SeasarComponent(name="name1")
		private Object annotFieldWithName;
		
		/** SeasarComponent�A�m�e�[�V�������t�^����Ă��Ȃ��t�B�[���h */
		private Object normalField;
		
	}
	
}
