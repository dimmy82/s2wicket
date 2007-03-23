package org.seasar.wicket.uifactory;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.apache.commons.lang.StringUtils;

import wicket.Component;

/**
 * ���f���I�u�W�F�N�g���\�z���鏈�������r���_�[�N���X�ł��B
 * @author Yoichiro Tanaka
 * @since 1.3.0
 */
class ModelBuilder {

	/**
	 * �w�肳�ꂽ�t�B�[���h�����f���t�B�[���h���ǂ�����Ԃ��܂��B
	 * @param target �`�F�b�N�Ώۂ̃t�B�[���h�I�u�W�F�N�g
	 * @return ���f���t�B�[���h�Ɣ��f���ꂽ�ꍇ��true�C�����łȂ������ꍇ��false
	 */
	boolean isSupported(Field target) {
		// @WicketModel�A�m�e�[�V�������t�^����Ă��邩�ǂ������m�F
		return target.isAnnotationPresent(WicketModel.class);
	}

	/**
	 * �w�肳�ꂽ�t�B�[���h�ɑΉ����郂�f���I�u�W�F�N�g�𐶐����C���̌��ʂ�Ԃ��܂��B
	 * ���̃��\�b�h�ɓn�����t�B�[���h�́C{@link #isSupported(Field)}���\�b�h�̌Ăяo�����ʂ�true�̃t�B�[���h�݂̂ł��B
	 * @param targetField �����Ώۂ̃t�B�[���h�I�u�W�F�N�g
	 * @param target �����Ώۂ̃R���|�[�l���g�I�u�W�F�N�g
	 * @return �������ꂽ���f���I�u�W�F�N�g
	 */
	Object build(Field field, Component target) {
		// �������ʂ̃��f����ێ�����ϐ�
		Object model;
		// create[�t�B�[���h��]()���\�b�h���擾
		Method createMethod = getCreateMethod(field, target);
		// create���\�b�h�����݂������`�F�b�N
		if (createMethod != null) {
			// create���\�b�h���Ăяo���ă��f���I�u�W�F�N�g�𐶐�
			model = createModelByCreateMethod(createMethod, target);
		} else {
			// �t�B�[���h�̌^���g���ăC���X�^���X�𐶐�
			model = createModelByFieldType(field);
		}
		// ���ʂ�ԋp
		return model;
	}

	/**
	 * �w�肳�ꂽ�t�B�[���h�̌^���g�p���Ă��̃C���X�^���X�𐶐����C��������f���I�u�W�F�N�g�Ƃ��ĕԂ��܂��B
	 * @param field �����Ώۂ̃t�B�[���h�I�u�W�F�N�g
	 * @return �������ꂽ���f���I�u�W�F�N�g
	 */
	private Object createModelByFieldType(Field field) {
		try {
			// �t�B�[���h�̌^�𐶐�
			Class<?> type = field.getType();
			// �^�����ɃC���X�^���X�𐶐�
			Object model = type.newInstance();
			// ���ʂ�ԋp
			return model;
		} catch (InstantiationException e) {
			// TODO ��O����
			throw new IllegalStateException("Create model object for " + field.getName() + " failed.");
		} catch (IllegalAccessException e) {
			// TODO ��O����
			throw new IllegalStateException("Create model object for " + field.getName() + " failed.");
		}
	}

	/**
	 * �w�肳�ꂽcreate[�t�B�[���h��]()���\�b�h���Ăяo���C���̌��ʂ̃I�u�W�F�N�g�����f���I�u�W�F�N�g�Ƃ��ĕԂ��܂��B
	 * @param createMethod create[�t�B�[���h��]()���\�b�h�I�u�W�F�N�g
	 * @param target �Ăяo���Ώۂ�create���\�b�h�����R���|�[�l���g�I�u�W�F�N�g
	 * @return �������ꂽ���f���I�u�W�F�N�g
	 */
	private Object createModelByCreateMethod(Method createMethod, Component target) {
		try {
			// create���\�b�h�Ăяo��
			Object model = createMethod.invoke(target, new Object[0]);
			// ���ʂ�ԋp
			return model;
		} catch (IllegalArgumentException e) {
			// TODO ��O����
			throw new IllegalStateException("Invoke " + target.getClass().getName() + "#" + createMethod.getName() + " failed.");
		} catch (IllegalAccessException e) {
			// TODO ��O����
			throw new IllegalStateException("Invoke " + target.getClass().getName() + "#" + createMethod.getName() + " failed.");
		} catch (InvocationTargetException e) {
			// TODO ��O����
			throw new IllegalStateException("Invoke " + target.getClass().getName() + "#" + createMethod.getName() + " failed.");
		}
	}

	/**
	 * �w�肳�ꂽ�t�B�[���h�ɑΉ�����create[�t�B�[���h��]()���\�b�h��Ԃ��܂��B
	 * @param field �����Ώۂ̃t�B�[���h�I�u�W�F�N�g
	 * @param target ���\�b�h�������ǂ�������������R���|�[�l���g�I�u�W�F�N�g
	 * @return create[�t�B�[���h��]()���\�b�h�I�u�W�F�N�g �������݂��Ȃ��ꍇ��null
	 */
	private Method getCreateMethod(Field field, Component target) {
		// ���\�b�h��������
		String methodName = "create" + StringUtils.capitalize(field.getName());
		try {
			// ���\�b�h���擾
			Class<? extends Component> clazz = target.getClass();
			Method method = clazz.getDeclaredMethod(methodName, new Class[0]);
			// ���ʂ�ԋp
			return method;
		} catch(NoSuchMethodException e) {
			// ���\�b�h�����݂��Ȃ��̂�null��ԋp
			return null;
		}
	}

}
