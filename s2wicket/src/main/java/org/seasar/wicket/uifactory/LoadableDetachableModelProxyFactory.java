/*
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

package org.seasar.wicket.uifactory;

import static org.seasar.wicket.utils.Gadget.isEquals;
import static org.seasar.wicket.utils.Gadget.isFinalize;
import static org.seasar.wicket.utils.Gadget.isHashCode;
import static org.seasar.wicket.utils.Gadget.isToString;
import static org.seasar.wicket.utils.Gadget.isWriteReplace;

import java.io.ObjectStreamException;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import org.apache.commons.lang.StringUtils;

import wicket.Component;
import wicket.model.LoadableDetachableModel;

/**
 * {@link LoadableDetachableModel}�I�u�W�F�N�g�̓��I�v���L�V�𐶐����鏈�������t�@�N�g���N���X�ł��B
 * @author Yoichiro Tanaka
 * @since 1.3.0
 */
class LoadableDetachableModelProxyFactory {
	
	/**
	 * �v���L�V�I�u�W�F�N�g�𐶐����ĕԂ��܂��B
	 * @param fieldName �����Ώۂ̃t�B�[���h�̖��O
	 * @param target �����Ώۂ̃R���|�[�l���g�I�u�W�F�N�g
	 * @return �v���L�V�I�u�W�F�N�g
	 */
	static LoadableDetachableModel create(String fieldName, Component target) {
		// �C���^�[�Z�v�^�𐶐�
		LoadableDetachableModelMethodInterceptor interceptor =
			new LoadableDetachableModelMethodInterceptor(fieldName, target);
		// �G���n���T�𐶐�
		Enhancer enhancer = new Enhancer();
		// ��������C���^�t�F�[�X���Z�b�g
		enhancer.setInterfaces(new Class[] {
				Serializable.class, WriteReplaceHolder.class, MethodInterceptorHolder.class});
		// �X�[�p�[�N���X���Z�b�g
		enhancer.setSuperclass(LoadableDetachableModel.class);
		// �C���^�[�Z�v�^���Z�b�g
		enhancer.setCallback(interceptor);
		// �v���L�V�I�u�W�F�N�g�𐶐����ĕԋp
		return (LoadableDetachableModel)enhancer.create();
	}
	
	/**
	 * {@link LoadableDetachableModel}�I�u�W�F�N�g�ɑ΂��郁�\�b�h�Ăяo�����C���^�[�Z�v�g���ď������s���N���X�ł��B
	 */
	private static class LoadableDetachableModelMethodInterceptor
			implements MethodInterceptor, Serializable, WriteReplaceHolder, MethodInterceptorHolder {

		/** �Ώۂ̃R���|�[�l���g�I�u�W�F�N�g */
		private Component target;
		
		/** �t�B�[���h�� */
		private String fieldName;
		
		/**
		 * ���̃I�u�W�F�N�g�����������Ƃ��ɌĂяo����܂��B
		 * @param fieldName �����Ώۂ̃t�B�[���h�̖��O
		 * @param target �����Ώۂ̃R���|�[�l���g�I�u�W�F�N�g
		 */
		private LoadableDetachableModelMethodInterceptor(String fieldName, Component target) {
			super();
			this.fieldName = fieldName;
			this.target = target;
		}

		/**
		 * �w�肳�ꂽ�I�u�W�F�N�g�̃��\�b�h�Ăяo�����C���^�[�Z�v�g���܂��B
		 * @param obj �R�[���Ώۂ̃I�u�W�F�N�g
		 * @param method �Ăяo���ꂽ���\�b�h�̏��
		 * @param args ���\�b�h�Ăяo���̍ۂɎw�肳�ꂽ����
		 * @param proxy ���\�b�h�v���L�V
		 * @throws Throwable ���\�b�h�Ăяo�����ɉ��炩�̗�O�����������Ƃ�
		 */
		public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
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
			// �Ăяo���ꂽ���\�b�h��LoadableDetachableModel#load()���\�b�h���`�F�b�N
			if ((method.getName().equals("load"))
					|| (Modifier.isProtected(method.getModifiers()))
					|| (method.getReturnType().equals(Object.class))) {
				// ���\�b�h��������
				String methodName = "load" + StringUtils.capitalize(fieldName) + "Model";
				// �Ώۂ̃��\�b�h���擾
				Method targetMethod = target.getClass().getMethod(methodName, new Class[0]);
				// ���\�b�h�Ăяo��
				Object result = targetMethod.invoke(target, new Object[0]);
				// ���ʂ�ԋp
				return result;
			} else {
				// ���ʂɃ��\�b�h�R�[��
				return proxy.invokeSuper(obj, args);
			}
		}

		/**
		 * ���̃v���L�V�I�u�W�F�N�g���V���A���C�Y�����ۂɌĂяo����܂��B
		 * �����ł́C{@link SerializedProxy}�I�u�W�F�N�g�����̃I�u�W�F�N�g�̑���ɃV���A���C�Y�ΏۂƂ��ĕԋp���܂��B
		 * @return {@link SerializedProxy}�I�u�W�F�N�g
		 * @throws ObjectStreamException ���炩�̗�O�����������Ƃ�
		 */
		public Object writeReplace() throws ObjectStreamException {
			return new SerializedProxy(fieldName, target);
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
		
		/** �Ώۂ̃R���|�[�l���g�I�u�W�F�N�g */
		private Component target;
		
		/** �t�B�[���h�� */
		private String fieldName;
		
		/**
		 * ���̃I�u�W�F�N�g�����������Ƃ��ɌĂяo����܂��B
		 * @param fieldName �����Ώۂ̃t�B�[���h�̖��O
		 * @param fieldTypeName �t�B�[���h�̌^��
		 * @param target �����Ώۂ̃R���|�[�l���g�I�u�W�F�N�g
		 * @param wicketId wicket:id
		 * @param model ���f���I�u�W�F�N�g
		 */
		private SerializedProxy(String fieldName, Component target) {
			super();
			this.fieldName = fieldName;
			this.target = target;
		}
		
		/**
		 * ���̃I�u�W�F�N�g���i������Ԃ��畜�������ۂɌĂяo����܂��B
		 * �����ł́C�V�K�ɓ��I�v���L�V�𐶐����ĕԂ��Ă��܂��B
		 * @return �V�K�ɐ������ꂽ�v���L�V�I�u�W�F�N�g
		 * @throws ObjectStreamException ���炩�̗�O�����������Ƃ�
		 */
		private Object readResolve() throws ObjectStreamException {
			Object proxy = LoadableDetachableModelProxyFactory.create(fieldName, target);
			return proxy;
		}
		
	}

	/**
	 * writeReplace()���\�b�h�������Ƃ��K�肷��C���^�t�F�[�X�ł��B
	 */
	public static interface WriteReplaceHolder {
		
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
	public static interface MethodInterceptorHolder {

		/**
		 * ���\�b�h�C���^�[�Z�v�^��Ԃ��܂��B
		 * @return ���\�b�h�C���^�[�Z�v�^
		 */
		public MethodInterceptor getMethodInterceptor();
		
	}

}
