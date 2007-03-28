package org.seasar.wicket.uifactory;

import static org.seasar.wicket.utils.Gadget.isEquals;
import static org.seasar.wicket.utils.Gadget.isFinalize;
import static org.seasar.wicket.utils.Gadget.isHashCode;
import static org.seasar.wicket.utils.Gadget.isToString;
import static org.seasar.wicket.utils.Gadget.isWriteReplace;

import java.io.ObjectStreamException;
import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import ognl.MethodFailedException;
import ognl.Ognl;
import ognl.OgnlException;

import org.apache.commons.lang.StringUtils;
import org.seasar.wicket.utils.Gadget;

import wicket.Component;
import wicket.model.IModel;

/**
 * �R���|�[�l���g�ɑ΂��铮�I�v���L�V�𐶐����鏈�������t�@�N�g���N���X�ł��B
 * @author Yoichiro Tanaka
 * @since 1.3.0
 */
class ComponentProxyFactory {
	
	/**
	 * �v���L�V�I�u�W�F�N�g�𐶐����ĕԂ��܂��B
	 * @param fieldName �����Ώۂ̃t�B�[���h�̖��O
	 * @param fieldType �t�B�[���h�̌^
	 * @param target �����Ώۂ̃R���|�[�l���g�I�u�W�F�N�g
	 * @param wicketId wicket:id
	 * @param model ���f���I�u�W�F�N�g
	 * @return �v���L�V�I�u�W�F�N�g
	 */
	static Component create(String fieldName, Class fieldType, Component target, String wicketId, Object model) {
		// �C���^�[�Z�v�^�𐶐�
		WicketComponentMethodInterceptor interceptor =
			new WicketComponentMethodInterceptor(fieldName, fieldType, target, wicketId, model);
		// �G���n���T�𐶐�
		Enhancer enhancer = new Enhancer();
		// ��������C���^�t�F�[�X���Z�b�g
		enhancer.setInterfaces(new Class[] {
				Serializable.class, WriteReplaceHolder.class, MethodInterceptorHolder.class});
		// �X�[�p�[�N���X���Z�b�g
		enhancer.setSuperclass(fieldType);
		// �C���^�[�Z�v�^���Z�b�g
		enhancer.setCallback(interceptor);
		// �v���L�V�I�u�W�F�N�g�𐶐����ĕԋp
		if (model != null) {
			if (model instanceof IModel) {
				return (Component)enhancer.create(new Class[] {String.class, IModel.class}, new Object[] {wicketId, model});
			} else {
				Constructor constructor = Gadget.getConstructorMatchLastArgType(fieldType, 2, model.getClass());
				return (Component)enhancer.create(constructor.getParameterTypes(), new Object[] {wicketId, model});
			}
		} else {
			return (Component)enhancer.create(new Class[] {String.class}, new Object[] {wicketId});
		}
	}
	
	/**
	 * �R���|�[�l���g�ɑ΂��郁�\�b�h�Ăяo�����C���^�[�Z�v�g���ď������s���N���X�ł��B
	 */
	private static class WicketComponentMethodInterceptor
			implements MethodInterceptor, Serializable, WriteReplaceHolder, MethodInterceptorHolder {
		
		/** �Ώۂ̃R���|�[�l���g�I�u�W�F�N�g */
		private Component target;
		
		/** �t�B�[���h�� */
		private String fieldName;
		
		/** �Ώۂ̃t�B�[���h�̌^�� */
		private String fieldTypeName;
		
		/** wicket:id */
		private String wicketId;
		
		/** ���f���I�u�W�F�N�g */
		private Object model;
		
		/**
		 * ���̃I�u�W�F�N�g�����������Ƃ��ɌĂяo����܂��B
		 * @param fieldName �����Ώۂ̃t�B�[���h�̖��O
		 * @param fieldType �t�B�[���h�̌^
		 * @param target �����Ώۂ̃R���|�[�l���g�I�u�W�F�N�g
		 * @param wicketId wicket:id
		 * @param model ���f���I�u�W�F�N�g
		 */
		private WicketComponentMethodInterceptor(String fieldName, Class fieldType, Component target, String wicketId, Object model) {
			super();
			this.fieldName = fieldName;
			this.fieldTypeName = fieldType.getName();
			this.target = target;
			this.wicketId = wicketId;
			this.model = model;
		}

		/**
		 * �w�肳�ꂽ�I�u�W�F�N�g�̃��\�b�h�Ăяo�����C���^�[�Z�v�g���܂��B
		 * @param object �R�[���Ώۂ̃I�u�W�F�N�g
		 * @param method �Ăяo���ꂽ���\�b�h�̏��
		 * @param args ���\�b�h�Ăяo���̍ۂɎw�肳�ꂽ����
		 * @param methodProxy ���\�b�h�v���L�V
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
			// �Ăяo���Ώۂ̃��\�b�h�����ۃ��\�b�h���`�F�b�N
			if (Modifier.isAbstract(method.getModifiers())) {
				// ���ۂɌĂяo�����\�b�h�̖��O������
				String methodName = method.getName();
				methodName += StringUtils.capitalize(fieldName);
				// �R���|�[�l���g�I�u�W�F�N�g���烁�\�b�h���擾
				Class<? extends Component> clazz = target.getClass();
				Method targetMethod = getMethod(clazz, methodName, method.getParameterTypes());
				// ���\�b�h�����݂������`�F�b�N
				if (targetMethod != null) {
					// ���\�b�h�Ăяo��
					Object result = targetMethod.invoke(target, args);
					// ���ʂ�ԋp
					return result;
				} else {
					// �t�B�[���h�I�u�W�F�N�g���擾
					Field field = target.getClass().getDeclaredField(fieldName);
					// WicketComponent�A�m�e�[�V�������擾
					WicketComponent wicketComponentAnnotation = field.getAnnotation(WicketComponent.class);
					// �Ăяo���ꂽ���\�b�h���Ɉ�v����WicketAction�A�m�e�[�V�������擾
					WicketAction wicketAction = getWicketActionAnnotation(wicketComponentAnnotation, method.getName());
					// �Y������WicketAction�A�m�e�[�V���������݂������`�F�b�N
					if (wicketAction != null) {
						// ���s���鎮���擾
						String exp = wicketAction.exp();
						// �����w�肳�ꂽ���`�F�b�N
						if (StringUtils.isNotEmpty(exp)) {
							try {
								// �����p�[�X
								Object parsedExp = Ognl.parseExpression(exp);
								// ����]�����C�]�����ʂ��擾
								Ognl.getValue(parsedExp, target);
							} catch(MethodFailedException e) {
								// handleException���\�b�h���擾
								Method handleExceptionMethod = getMethod(target.getClass(), "handleException", new Class[] {Object.class, String.class, Exception.class});
								// �擾�ł������`�F�b�N
								if (handleExceptionMethod != null) {
									// handleException���\�b�h�Ăяo��
									handleExceptionMethod.invoke(target, new Object[] {target, method.getName(), e.getReason()});
									// ���ʂ�ԋp
									return null;
								} else {
									// TODO ��O����
									throw e.getReason();
								}
							} catch(OgnlException e) {
								// TODO ��O����
								throw new IllegalStateException(e);
							}
						}
						// responsePage�����l���擾
						String responsePage = wicketAction.responsePage();
						// responsePage�������w�肳�ꂽ���`�F�b�N
						if (StringUtils.isNotEmpty(responsePage)) {
							// �y�[�W�N���X���擾
							Class<?> pageClazz;
							try {
								pageClazz = Class.forName(responsePage);
							} catch(ClassNotFoundException e) {
								// �����Ώۂ̃R���|�[�l���g�̃p�b�P�[�W����y�[�W�N���X���擾
								Package targetPackage = target.getClass().getPackage();
								pageClazz = Class.forName(targetPackage.getName() + "." + responsePage);
							}
							// ���X�|���X�y�[�W���Z�b�g
							target.setResponsePage(pageClazz);
						}
					}
				}
				// ���ʂ�ԋp
				return null;
			} else {
				// ���ʂɃ��\�b�h�R�[��
				return proxy.invokeSuper(obj, args);
			}
		}
		
		private WicketAction getWicketActionAnnotation(WicketComponent wicketComponentAnnotation, String methodName) {
			WicketAction[] wicketActions = wicketComponentAnnotation.actions();
			for (int i = 0; i < wicketActions.length; i++) {
				if (wicketActions[i].method().equals(methodName)) {
					return wicketActions[i];
				}
			}
			return null;
		}
		
		/**
		 * �w�肳�ꂽ�N���X�ɒ�`���ꂽ���\�b�h�̒��ŁC�w�肳�ꂽ���\�b�h���ƈ����̌^�������\�b�h��Ԃ��܂��B
		 * @param clazz �����Ώۂ̃N���X�I�u�W�F�N�g
		 * @param methodName ���\�b�h��
		 * @param parameterTypes �����̌^�̔z��
		 * @return �����Ɉ�v�������\�b�h�̏������I�u�W�F�N�g ������v���郁�\�b�h���Ȃ����null
		 */
		private Method getMethod(Class clazz, String methodName, Class[] parameterTypes) {
			try {
				return clazz.getMethod(methodName, parameterTypes);
			} catch(NoSuchMethodException e) {
				return null;
			}
		}

		/**
		 * ���̃v���L�V�I�u�W�F�N�g���V���A���C�Y�����ۂɌĂяo����܂��B
		 * �����ł́C{@link SerializedProxy}�I�u�W�F�N�g�����̃I�u�W�F�N�g�̑���ɃV���A���C�Y�ΏۂƂ��ĕԋp���܂��B
		 * @return {@link SerializedProxy}�I�u�W�F�N�g
		 * @throws ObjectStreamException ���炩�̗�O�����������Ƃ�
		 */
		public Object writeReplace() throws ObjectStreamException {
			return new SerializedProxy(fieldName, fieldTypeName, target, wicketId, model);
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
		
		/** �Ώۂ̃t�B�[���h�̌^�� */
		private String fieldTypeName;
		
		/** wicket:id */
		private String wicketId;
		
		/** ���f���I�u�W�F�N�g */
		private Object model;

		/**
		 * ���̃I�u�W�F�N�g�����������Ƃ��ɌĂяo����܂��B
		 * @param fieldName �����Ώۂ̃t�B�[���h�̖��O
		 * @param fieldTypeName �t�B�[���h�̌^��
		 * @param target �����Ώۂ̃R���|�[�l���g�I�u�W�F�N�g
		 * @param wicketId wicket:id
		 * @param model ���f���I�u�W�F�N�g
		 */
		private SerializedProxy(String fieldName, String fieldTypeName, Component target, String wicketId, Object model) {
			super();
			this.fieldName = fieldName;
			this.fieldTypeName = fieldTypeName;
			this.target = target;
			this.wicketId = wicketId;
			this.model = model;
		}
		
		/**
		 * ���̃I�u�W�F�N�g���i������Ԃ��畜�������ۂɌĂяo����܂��B
		 * �����ł́C�V�K�ɓ��I�v���L�V�𐶐����ĕԂ��Ă��܂��B
		 * @return �V�K�ɐ������ꂽ�v���L�V�I�u�W�F�N�g
		 * @throws ObjectStreamException ���炩�̗�O�����������Ƃ�
		 */
		private Object readResolve() throws ObjectStreamException {
			try {
				Class fieldType = Class.forName(fieldTypeName);
				Object proxy = ComponentProxyFactory.create(fieldName, fieldType, target, wicketId, model);
				return proxy;
			} catch(ClassNotFoundException e) {
				throw new IllegalStateException("Field type [" + fieldTypeName + "] class not found.", e);
			}
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
