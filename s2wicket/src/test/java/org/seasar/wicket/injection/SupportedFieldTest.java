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

import junit.framework.TestCase;
import static org.easymock.EasyMock.*;

/**
 * {@link SupportedField}�̃e�X�g�P�[�X�N���X�ł��B
 * @author Yoichiro Tanaka
 * @since 1.1.0
 */
public class SupportedFieldTest extends TestCase {

	/**
	 * {@link SupportedField}������field�v���p�e�B��fieldFilter�v���p�e�B�̃e�X�g���s���܂��B
	 * @throws Exception ���炩�̗�O�����������Ƃ�
	 */
	public void testProperties() throws Exception {
		Field field = SupportedField.class.getDeclaredField("field");
		FieldFilter fieldFilter = createMock(FieldFilter.class);
		SupportedField target = new SupportedField(field, fieldFilter);
		assertSame(field, target.getField());
		assertSame(fieldFilter, target.getFieldFilter());
	}
	
	/**
	 * {@link SupportedField#getLookupComponentName()}�̃e�X�g���s���܂��B
	 * @throws Exception ���炩�̗�O�����������Ƃ�
	 */
	public void testGetLookupComponentName() throws Exception {
		Field field = SupportedField.class.getDeclaredField("field");
		FieldFilter fieldFilter = createMock(FieldFilter.class);
		expect(fieldFilter.getLookupComponentName(field)).andReturn("name1");
		replay(fieldFilter);
		SupportedField target = new SupportedField(field, fieldFilter);
		String result = target.getLookupComponentName();
		verify(fieldFilter);
		assertEquals("name1", result);
	}
	
}
