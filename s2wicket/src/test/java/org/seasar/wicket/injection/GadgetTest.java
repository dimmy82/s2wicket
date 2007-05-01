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

import java.io.ObjectStreamException;
import java.lang.reflect.Method;

import net.sf.cglib.proxy.MethodInterceptor;

import junit.framework.TestCase;

import static org.easymock.EasyMock.*;

/**
 * {@link Gadget}�̃e�X�g�P�[�X�N���X�ł��B
 * @author Yoichiro Tanaka
 * @since 1.0.0
 */
public class GadgetTest extends TestCase {
	
	/**
	 * {@link Gadget#isMethod(java.lang.reflect.Method, Class, Class[], String)}�̃e�X�g���s���܂��B
	 * @throws Exception ���炩�̗�O�����������Ƃ�
	 */
	public void testIsMethodNormal() throws Exception {
		Method method = Object.class.getMethod("equals", new Class[] {Object.class});
		boolean result = Gadget.isMethod(method, boolean.class, new Class[] {Object.class}, "equals");
		assertTrue(result);
	}
	
	/**
	 * {@link Gadget#isMethod(Method, Class, Class[], String)}�̃e�X�g���s���܂��B
	 * �߂�l�̌^����v���Ă��Ȃ������ꍇ���e�X�g���܂��B
	 * @throws Exception ���炩�̗�O�����������Ƃ�
	 */
	public void testIsMethodNotSameType() throws Exception {
		Method method = Object.class.getMethod("equals", new Class[] {Object.class});
		boolean result = Gadget.isMethod(method, int.class, new Class[] {Object.class}, "equals");
		assertFalse(result);
	}
	
	/**
	 * {@link Gadget#isMethod(Method, Class, Class[], String)}�̃e�X�g���s���܂��B
	 * �����̌^����v���Ă��Ȃ������ꍇ���e�X�g���܂��B
	 * @throws Exception ���炩�̗�O�����������Ƃ�
	 */
	public void testIsMethodNotSameArgType() throws Exception {
		Method method = Object.class.getMethod("equals", new Class[] {Object.class});
		boolean result = Gadget.isMethod(method, boolean.class, new Class[] {int.class}, "equals");
		assertFalse(result);
	}

	/**
	 * {@link Gadget#isMethod(Method, Class, Class[], String)}�̃e�X�g���s���܂��B
	 * �����̐�����v���Ă��Ȃ������ꍇ���e�X�g���܂��B
	 * @throws Exception ���炩�̗�O�����������Ƃ�
	 */
	public void testIsMethodNotSameArgCount() throws Exception {
		Method method = Object.class.getMethod("equals", new Class[] {Object.class});
		boolean result = Gadget.isMethod(method, boolean.class, new Class[] {Object.class, Object.class}, "equals");
		assertFalse(result);
	}
	
	/**
	 * {@link Gadget#isMethod(Method, Class, Class[], String)}�̃e�X�g���s���܂��B
	 * ���\�b�h������v���Ă��Ȃ������ꍇ���e�X�g���܂��B
	 * @throws Exception ���炩�̗�O�����������Ƃ�
	 */
	public void testIsMethodNotSameMethodName() throws Exception {
		Method method = Object.class.getMethod("equals", new Class[] {Object.class});
		boolean result = Gadget.isMethod(method, boolean.class, new Class[] {Object.class}, "equal");
		assertFalse(result);
	}

	/**
	 * {@link Gadget#isWriteReplace(Method)}�̃e�X�g���s���܂��B
	 * @throws Exception ���炩�̗�O�����������Ƃ�
	 */
	public void testIsWriteReplace() throws Exception {
		Test test = createMock(Test.class);
		Method method = test.getClass().getMethod("writeReplace", new Class[0]);
		boolean result = Gadget.isWriteReplace(method);
		assertTrue(result);
		method = test.getClass().getMethod("dummy", new Class[0]);
		result = Gadget.isWriteReplace(method);
		assertFalse(result);
	}
	
	/**
	 * {@link Gadget#isEquals(Method)}�̃e�X�g���s���܂��B
	 * @throws Exception ���炩�̗�O�����������Ƃ�
	 */
	public void testIsEquals() throws Exception {
		Test test = createMock(Test.class);
		Method method = test.getClass().getMethod("equals", new Class[] {Object.class});
		boolean result = Gadget.isEquals(method);
		assertTrue(result);
		method = test.getClass().getMethod("dummy", new Class[0]);
		result = Gadget.isEquals(method);
		assertFalse(result);
	}
	
	/**
	 * {@link Gadget#isHashCode(Method)}�̃e�X�g���s���܂��B
	 * @throws Exception ���炩�̗�O�����������Ƃ�
	 */
	public void testIsHashCode() throws Exception {
		Test test = createMock(Test.class);
		Method method = test.getClass().getMethod("hashCode", new Class[0]);
		boolean result = Gadget.isHashCode(method);
		assertTrue(result);
		method = test.getClass().getMethod("dummy", new Class[0]);
		result = Gadget.isHashCode(method);
		assertFalse(result);
	}
	
	/**
	 * {@link Gadget#isToString(Method)}�̃e�X�g���s���܂��B
	 * @throws Exception ���炩�̗�O�����������Ƃ�
	 */
	public void testIsToString() throws Exception {
		Test test = createMock(Test.class);
		Method method = test.getClass().getMethod("toString", new Class[0]);
		boolean result = Gadget.isToString(method);
		assertTrue(result);
		method = test.getClass().getMethod("dummy", new Class[0]);
		result = Gadget.isToString(method);
		assertFalse(result);
	}
	
	/**
	 * {@link Gadget#isFinalize(Method)}�̃e�X�g���s���܂��B
	 * @throws Exception ���炩�̗�O�����������Ƃ�
	 */
	public void testIsFinalize() throws Exception {
		Test test = createMock(Test.class);
		Method method = test.getClass().getMethod("finalize", new Class[0]);
		boolean result = Gadget.isFinalize(method);
		assertTrue(result);
		method = test.getClass().getMethod("dummy", new Class[0]);
		result = Gadget.isFinalize(method);
		assertFalse(result);
	}
	
	/**
	 * {@link Gadget#isGetMethodInterceptor(Method)}�̃e�X�g���s���܂��B
	 * @throws Exception ���炩�̗�O�����������Ƃ�
	 */
	public void testIsGetMethodInterceptor() throws Exception {
		Test test = createMock(Test.class);
		Method method = test.getClass().getMethod("getMethodInterceptor", new Class[0]);
		boolean result = Gadget.isGetMethodInterceptor(method);
		assertTrue(result);
		method = test.getClass().getMethod("dummy", new Class[0]);
		result = Gadget.isGetMethodInterceptor(method);
		assertFalse(result);
	}
	
	/**
	 * �e�X�g�p�̃C���^�t�F�[�X�ł��B
	 */
	private static interface Test {
		public Object writeReplace() throws ObjectStreamException;
		public void finalize();
		public void dummy();
		public MethodInterceptor getMethodInterceptor();
	}

}
