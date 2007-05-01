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

import java.lang.reflect.Method;

import net.sf.cglib.proxy.MethodInterceptor;

/**
 * �ėp�I�ȏ����������[�e�B���e�B�N���X�ł��B
 * @author Yoichiro Tanaka
 * @since 1.0.0
 */
class Gadget {
	
	/**
	 * ���̃I�u�W�F�N�g�����������Ƃ��ɌĂяo����܂��B
	 */
	private Gadget() {
		// N/A
	}
	
	/**
	 * �w�肳�ꂽ���\�b�h��writeReplace()���\�b�h���ǂ�����Ԃ��܂��B
	 * @param method ���\�b�h�I�u�W�F�N�g
	 * @return �Y�����\�b�h�������ꍇ�� true
	 */
	static boolean isWriteReplace(Method method) {
		return isMethod(method, Object.class, new Class[0], "writeReplace");
	}
	
	/**
	 * �w�肳�ꂽ���\�b�h��equals()���\�b�h���ǂ�����Ԃ��܂��B
	 * @param method ���\�b�h�I�u�W�F�N�g
	 * @return �Y�����\�b�h�������ꍇ�� true
	 */
	static boolean isEquals(Method method) {
		return isMethod(method, boolean.class, new Class[] {Object.class}, "equals");
	}
	
	/**
	 * �w�肳�ꂽ���\�b�h��hashCode()���\�b�h���ǂ�����Ԃ��܂��B
	 * @param method ���\�b�h�I�u�W�F�N�g
	 * @return �Y�����\�b�h�������ꍇ�� true
	 */
	static boolean isHashCode(Method method) {
		return isMethod(method, int.class, new Class[0], "hashCode");
	}
	
	/**
	 * �w�肳�ꂽ���\�b�h��toString()���\�b�h���ǂ�����Ԃ��܂��B
	 * @param method ���\�b�h�I�u�W�F�N�g
	 * @return �Y�����\�b�h�������ꍇ�� true
	 */
	static boolean isToString(Method method) {
		return isMethod(method, String.class, new Class[0], "toString");
	}
	
	/**
	 * �w�肳�ꂽ���\�b�h��finalize()���\�b�h���ǂ�����Ԃ��܂��B
	 * @param method ���\�b�h�I�u�W�F�N�g
	 * @return �Y�����\�b�h�������ꍇ�� true
	 */
	static boolean isFinalize(Method method) {
		return isMethod(method, void.class, new Class[0], "finalize");
	}
	
	static boolean isGetMethodInterceptor(Method method) {
		return isMethod(method, MethodInterceptor.class, new Class[0], "getMethodInterceptor");
	}

	/**
	 * �w�肳�ꂽ���\�b�h���C�w�肳�ꂽ�����Ɉ�v���邩�ǂ����Ԃ��܂��B
	 * @param method ���\�b�h�I�u�W�F�N�g
	 * @param type �߂�l�̌^
	 * @param argTypes �����̌^�̔z��
	 * @param name ���\�b�h��
	 * @return ��v�����ꍇ�� true
	 */
	static boolean isMethod(Method method, Class type, Class[] argTypes, String name) {
		Class<?>[] parameterTypes = method.getParameterTypes();
		if ((method.getReturnType() == type)
				&& (parameterTypes.length == argTypes.length)
				&& (method.getName().equals(name))) {
			for (int i = 0; i < argTypes.length; i++) {
				if (parameterTypes[i] != argTypes[i]) {
					return false;
				}
			}
			return true;
		} else {
			return false;
		}
	}
	
}
