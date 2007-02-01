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

import static org.seasar.wicket.injection.Gadget.isEquals;
import static org.seasar.wicket.injection.Gadget.isFinalize;
import static org.seasar.wicket.injection.Gadget.isGetMethodInterceptor;
import static org.seasar.wicket.injection.Gadget.isHashCode;
import static org.seasar.wicket.injection.Gadget.isToString;
import static org.seasar.wicket.injection.Gadget.isWriteReplace;

import java.io.ObjectStreamException;
import java.io.Serializable;
import java.lang.reflect.Method;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import org.apache.commons.lang.builder.EqualsBuilder;

/**
 * Seasar�R���|�[�l���g�̃��\�b�h�Ăяo����㗝����v���L�V�I�u�W�F�N�g�𐶐����鏈�������N���X�ł��B
 * @author Yoichiro Tanaka
 */
class ProxyFactory {
	
	/**
	 * �v���L�V�I�u�W�F�N�g�𐶐����ĕԂ��܂��B
	 * @param componentType �R���|�[�l���g�̌^
	 * @param resolver Seasar�R���|�[�l���g���]���o
	 * @return �v���L�V�I�u�W�F�N�g
	 */
	static Object create(Class componentType, ComponentResolver resolver) {
		// �C���^�[�Z�v�^�𐶐�
		ComponentMethodInterceptor interceptor = new ComponentMethodInterceptor(componentType, resolver);
		// �G���n���T�𐶐�
		Enhancer enhancer = new Enhancer();
		// �R���|�[�l���g�̌^���C���^�t�F�[�X���ǂ����`�F�b�N
		if (componentType.isInterface()) {
			// ��������C���^�t�F�[�X���Z�b�g
			enhancer.setInterfaces(new Class[] {
					componentType, Serializable.class, WriteReplaceHolder.class, MethodInterceptorHolder.class});
		} else {
			// ��������C���^�t�F�[�X���Z�b�g
			enhancer.setInterfaces(new Class[] {
					Serializable.class, WriteReplaceHolder.class, MethodInterceptorHolder.class});
			// �X�[�p�[�N���X���w��
			enhancer.setSuperclass(componentType);
		}
		// �C���^�[�Z�v�^���Z�b�g
		enhancer.setCallback(interceptor);
		// �v���L�V�I�u�W�F�N�g�𐶐����ĕԋp
		return enhancer.create();
	}
	
	/**
	 * Seasar�R���|�[�l���g�̃��\�b�h�Ăяo�����C���^�[�Z�v�g�����ۂ̏������s���N���X�ł��B
	 */
	private static class ComponentMethodInterceptor
			implements MethodInterceptor, Serializable, WriteReplaceHolder, MethodInterceptorHolder {
		
		/** �R���|�[�l���g�̌^�� */
		private String componentTypeName;
		
		/** �R���|�[�l���g���]���o */
		private ComponentResolver componentResolver;

		/**
		 * ���̃I�u�W�F�N�g�����������Ƃ��ɌĂяo����܂��B
		 * @param componentType �R���|�[�l���g�̌^
		 * @param componentResolver �R���|�[�l���g���]���o
		 */
		private ComponentMethodInterceptor(Class componentType, ComponentResolver componentResolver) {
			super();
			// �������t�B�[���h�ɕێ�
			componentTypeName = componentType.getName();
			this.componentResolver = componentResolver;
		}

		/**
		 * �w�肳�ꂽ�I�u�W�F�N�g�̃��\�b�h�Ăяo�����C���^�[�Z�v�g���܂��B
		 * �����ł́C�w�肳�ꂽ�I�u�W�F�N�g�̑���ɁC�R���|�[�l���g���]���o�ɂ���ē���ꂽSeasar�R���|�[�l���g�̃��\�b�h���Ăяo���܂��B
		 * @param object �R�[���Ώۂ̃I�u�W�F�N�g
		 * @param method �Ăяo���ꂽ���\�b�h�̏��
		 * @param args ���\�b�h�Ăяo���̍ۂɎw�肳�ꂽ����
		 * @param methodProxy ���\�b�h�v���L�V
		 * @throws Throwable ���\�b�h�Ăяo�����ɉ��炩�̗�O�����������Ƃ�
		 */
		public Object intercept(Object object, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
			// toString()�Ăяo�����`�F�b�N
			if (isToString(method)) {
				// ���̃v���L�V�I�u�W�F�N�g�̃��\�b�h���Ăяo��
				return toString();
			}
			// hashCode()�Ăяo�����`�F�b�N
			if (isHashCode(method)) {
				// ���̃v���L�V�I�u�W�F�N�g�̃��\�b�h���Ăяo��
				return hashCode();
			}
			// equals()�Ăяo�����`�F�b�N
			if (isEquals(method)) {
				// ���̃v���L�V�I�u�W�F�N�g�̃��\�b�h���Ăяo��
				return equals(args[0]);
			}
			// finalize()�Ăяo�����`�F�b�N
			if (isFinalize(method)) {
				// �������Ȃ�
				return null;
			}
			// writeReplace()�Ăяo�����`�F�b�N
			if (isWriteReplace(method)) {
				// ���̃v���L�V���i���������Ƀ_�~�[�̃I�u�W�F�N�g���i����
				return writeReplace();
			}
			// getMethodInterceptor()�Ăяo�����`�F�b�N
			if (isGetMethodInterceptor(method)) {
				// ���̃v���L�V�̃��\�b�h���Ăяo��
				return getMethodInterceptor();
			}
			// Seasar�R���|�[�l���g���擾
			Object target = componentResolver.getTargetObject();
			// Seasar�R���|�[�l���g�̃��\�b�h���Ăяo���C���̌��ʂ�ԋp
			return methodProxy.invoke(target, args);
		}

		/**
		 * ���̃v���L�V�I�u�W�F�N�g���V���A���C�Y�����ۂɌĂяo����܂��B
		 * �����ł́C{@link SerializedProxy}�I�u�W�F�N�g�����̃I�u�W�F�N�g�̑���ɃV���A���C�Y�ΏۂƂ��ĕԋp���܂��B
		 * @return {@link SerializedProxy}�I�u�W�F�N�g
		 * @throws ObjectStreamException ���炩�̗�O�����������Ƃ�
		 */
		public Object writeReplace() throws ObjectStreamException {
			return new SerializedProxy(componentTypeName, componentResolver);
		}
		
		/**
		 * �w�肳�ꂽ�I�u�W�F�N�g�Ƃ��̃I�u�W�F�N�g�̓��e�̈�v����Ԃ��܂��B
		 * @param obj ��r�Ώۂ̃I�u�W�F�N�g
		 * @return ���e����v����ꍇ�� true
		 */
		public boolean equals(Object obj) {
			if (!(obj instanceof ComponentMethodInterceptor)) {
				return false;
			} else {
				ComponentMethodInterceptor target = (ComponentMethodInterceptor)obj;
				return new EqualsBuilder()
					.append(componentTypeName, target.componentTypeName)
					.append(componentResolver, target.componentResolver)
					.isEquals();
			}
		}
		
		/**
		 * ���̃I�u�W�F�N�g�̃n�b�V���l��Ԃ��܂��B
		 * @return �n�b�V���l
		 */
		public int hashCode() {
			int hashCode = componentTypeName.hashCode();
			hashCode += componentResolver.hashCode() * 127;
			return hashCode;
		}
		
		/**
		 * ���̃I�u�W�F�N�g��Ԃ��܂��B
		 * @return ���̃I�u�W�F�N�g
		 */
		public MethodInterceptor getMethodInterceptor() {
			return this;
		}
		
	}
	
	/**
	 * ���I�v���L�V�I�u�W�F�N�g�̑���ɃV���A���C�Y�����I�u�W�F�N�g�̃N���X�ł��B
	 */
	private static class SerializedProxy implements Serializable {
		
		/** �R���|�[�l���g�̌^�̖��O */
		private String componentTypeName;
		
		/** �R���|�[�l���g���]���o */
		private ComponentResolver componentResolver;

		/**
		 * ���̃I�u�W�F�N�g�����������Ƃ��ɌĂяo����܂��B
		 * @param componentTypeName �R���|�[�l���g�̌^�̖��O
		 * @param componentResolver �R���|�[�l���g���]���o
		 */
		private SerializedProxy(String componentTypeName, ComponentResolver componentResolver) {
			super();
			this.componentTypeName = componentTypeName;
			this.componentResolver = componentResolver;
		}
		
		/**
		 * ���̃I�u�W�F�N�g���i������Ԃ��畜�������ۂɌĂяo����܂��B
		 * �����ł́C�V�K�ɓ��I�v���L�V�𐶐����ĕԂ��Ă��܂��B
		 * @return �V�K�ɐ������ꂽ�v���L�V�I�u�W�F�N�g
		 * @throws ObjectStreamException ���炩�̗�O�����������Ƃ�
		 */
		private Object readResolve() throws ObjectStreamException {
			try {
				Class componentType = Class.forName(componentTypeName);
				Object proxy = ProxyFactory.create(componentType, componentResolver);
				return proxy;
			} catch(ClassNotFoundException e) {
				throw new IllegalStateException("ComponentType[" + componentTypeName + "] class not found.", e);
			}
		}
		
	}
	
	/**
	 * writeReplace()���\�b�h�������Ƃ��K�肷��C���^�t�F�[�X�ł��B
	 */
	private static interface WriteReplaceHolder {
		
		/**
		 * �I�u�W�F�N�g���V���A���C�Y�����ۂɌĂяo����܂��B
		 * �Ώۂ̃I�u�W�F�N�g�ł͂Ȃ��C�ʂ̃I�u�W�F�N�g���V���A���C�Y�������ꍇ�Ɏg�p���܂��B
		 * @return ���ۂɃV���A���C�Y�����I�u�W�F�N�g
		 * @throws ObjectStreamException ���炩�̗�O�����������Ƃ�
		 */
		public Object writeReplace() throws ObjectStreamException;
		
	}
	
	/**
	 * ���\�b�h�C���^�[�Z�v�^��Ԃ��������K�肵���C���^�t�F�[�X�ł��B
	 */
	static interface MethodInterceptorHolder {

		/**
		 * ���\�b�h�C���^�[�Z�v�^��Ԃ��܂��B
		 * @return ���\�b�h�C���^�[�Z�v�^
		 */
		public MethodInterceptor getMethodInterceptor();
		
	}

}
