package org.seasar.wicket.uifactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import wicket.Component;
import wicket.MarkupContainer;
import wicket.model.IModel;
import wicket.model.PropertyModel;

/**
 * �R���|�[�l���g�I�u�W�F�N�g���\�z���鏈�������r���_�[�N���X�ł��B
 * @author Yoichiro Tanaka
 * @since 1.3.0
 */
class ComponentBuilder {

	/**
	 * �w�肳�ꂽ�t�B�[���h���R���|�[�l���g�t�B�[���h���ǂ�����Ԃ��܂��B
	 * @param target �`�F�b�N�Ώۂ̃t�B�[���h�I�u�W�F�N�g
	 * @return �R���|�[�l���g�t�B�[���h�Ɣ��f���ꂽ�ꍇ��true�C�����łȂ������ꍇ��false
	 */
	boolean isSupported(Field target) {
		// @WicketComponent�A�m�e�[�V�������t�^����Ă��邩�ǂ������m�F
		return target.isAnnotationPresent(WicketComponent.class);
	}

	/**
	 * �w�肳�ꂽ�t�B�[���h�ɑΉ�����R���|�[�l���g�I�u�W�F�N�g�𐶐����C���̌��ʂ�Ԃ��܂��B
	 * ���̃��\�b�h�ɓn�����t�B�[���h�́C{@link #isSupported(Field)}���\�b�h�̌Ăяo�����ʂ�true�̃t�B�[���h�݂̂ł��B
	 * @param targetField �����Ώۂ̃t�B�[���h�I�u�W�F�N�g
	 * @param target �����Ώۂ̃R���|�[�l���g�I�u�W�F�N�g
	 * @param modelMap ���f���I�u�W�F�N�g���i�[���ꂽ�R���N�V����
	 * @return �������ꂽ�R���|�[�l���g�I�u�W�F�N�g
	 */
	Object build(Field field, Component target, Map<String, Object> modelMap) {
		// ���������R���|�[�l���g�̕ϐ�
		Component result;
		// �e�R���|�[�l���g���擾
		MarkupContainer parent = getParentContainer(field, target);
		// create[�t�B�[���h��]Component()���\�b�h���擾
		Method createMethod = getCreateMethod(field, target);
		// create���\�b�h�����݂������`�F�b�N
		if (createMethod != null) {
			// create���\�b�h���Ăяo���ăR���|�[�l���g�I�u�W�F�N�g�𐶐�
			result = createComponentByCreateMethod(createMethod, target, field);
		} else {
			// �t�B�[���h�̌^���g���ăC���X�^���X�𐶐�
			result = createComponentByFieldType(field, target, modelMap);
		}
		// �e�R���e�i�ɒǉ�
		parent.add(result);
		// ���ʂ�ԋp
		return result;
	}
	
	/**
	 * �w�肳�ꂽ�t�B�[���h�̌^���g�p���Ă��̃C���X�^���X�𐶐����C������R���|�[�l���g�I�u�W�F�N�g�Ƃ��ĕԂ��܂��B
	 * @param field �����Ώۂ̃t�B�[���h�I�u�W�F�N�g
	 * @param target �����Ώۂ̃t�B�[���h�����R���|�[�l���g�I�u�W�F�N�g
	 * @param modelMap ���f���I�u�W�F�N�g���i�[���ꂽ�R���N�V����
	 * @return �������ꂽ�R���|�[�l���g�I�u�W�F�N�g
	 */
	private Component createComponentByFieldType(Field field, Component target, Map<String, Object> modelMap) {
		// ���������R���|�[�l���g�I�u�W�F�N�g�̕ϐ�
		Component result;
		// ���f���I�u�W�F�N�g���擾
		Object model = getModel(field, modelMap);
		// ���f���̃v���p�e�B��������
		String propertyName = getPropertyName(field);
		// �v���p�e�B���f���𐶐�
		PropertyModel propertyModel = new PropertyModel(model, propertyName);
		// wicket:id������
		String wicketId = getWicketId(field);
		// �t�B�[���h�̌^���擾
		Class<?> clazz = field.getType();
		// �t�B�[���h�̌^�����ۃN���X���`�F�b�N
		if (Modifier.isAbstract(clazz.getModifiers())) {
			// ���I�v���L�V�𐶐�
			result = ComponentProxyFactory.create(field.getName(), clazz, target, wicketId, propertyModel);
		} else {
			// �f���ɃC���X�^���X����
			result = createNewComponentInstance(field, wicketId, propertyModel);
		}
		// ���������R���|�[�l���g�I�u�W�F�N�g��ԋp
		return result;
	}
	
	/**
	 * �w�肳�ꂽ�t�B�[���h�ɑΉ�����R���|�[�l���g�I�u�W�F�N�g�𐶐����ĕԂ��܂��B
	 * @param field �����Ώۂ̃t�B�[���h�I�u�W�F�N�g
	 * @param wicketId wicket:id
	 * @param model ���f���I�u�W�F�N�g
	 * @return �������ꂽ�R���|�[�l���g�I�u�W�F�N�g
	 */
	private Component createNewComponentInstance(Field field, String wicketId, IModel model) {
		try {
			// �R���X�g���N�^���擾
			Class<?> clazz = field.getType();
			Constructor<?> constructor = clazz.getConstructor(String.class, IModel.class);
			// �C���X�^���X�𐶐�
			Component component = (Component)constructor.newInstance(wicketId, model);
			// ���ʂ�ԋp
			return component;
		} catch(NoSuchMethodException e) {
			// TODO ��O����
			throw new IllegalStateException("Create new component instance for " + field.getName() + " failed.", e);
		} catch (IllegalArgumentException e) {
			// TODO ��O����
			throw new IllegalStateException("Create new component instance for " + field.getName() + " failed.", e);
		} catch (InstantiationException e) {
			// TODO ��O����
			throw new IllegalStateException("Create new component instance for " + field.getName() + " failed.", e);
		} catch (IllegalAccessException e) {
			// TODO ��O����
			throw new IllegalStateException("Create new component instance for " + field.getName() + " failed.", e);
		} catch (InvocationTargetException e) {
			// TODO ��O����
			throw new IllegalStateException("Create new component instance for " + field.getName() + " failed.", e);
		}
	}
	
	/**
	 * �w�肳�ꂽ�t�B�[���h��wicket:id��Ԃ��܂��B
	 * @param field �����Ώۂ̃t�B�[���h�I�u�W�F�N�g
	 * @return wicket:id
	 */
	private String getWicketId(Field field) {
		// �A�m�e�[�V�������擾
		WicketComponent annotation = field.getAnnotation(WicketComponent.class);
		// wicketId�����l���擾
		String wicketId = annotation.wicketId();
		// wicketId�������w�肳�ꂽ���`�F�b�N
		if (StringUtils.isNotEmpty(wicketId)) {
			// wicketId�����l�����̂܂ܕԋp
			return wicketId;
		} else {
			// �t�B�[���h����wicket:id�Ƃ��ĕԋp
			return field.getName();
		}
	}
	
	/**
	 * �w�肳�ꂽ�t�B�[���h�ƃ��f�����֘A�t����ۂ̃v���p�e�B����Ԃ��܂��B
	 * @param field �����Ώۂ̃t�B�[���h�I�u�W�F�N�g
	 * @return ���f�����֘A�t����ۂ̃v���p�e�B��
	 */
	private String getPropertyName(Field field) {
		// �A�m�e�[�V�������擾
		WicketComponent annotation = field.getAnnotation(WicketComponent.class);
		// property�����l���擾
		String propertyName = annotation.property();
		// property�����l���w�肳�ꂽ���`�F�b�N
		if (StringUtils.isNotEmpty(propertyName)) {
			// property�����l�����̂܂ܕԋp
			return propertyName;
		} else {
			// �t�B�[���h�����v���p�e�B���Ƃ��ĕԋp
			return field.getName();
		}
	}
	
	/**
	 * �w�肳�ꂽ�t�B�[���h�ɓK�p���郂�f���I�u�W�F�N�g��Ԃ��܂��B
	 * @param field �����Ώۂ̃t�B�[���h
	 * @param modelMap ���f���I�u�W�F�N�g���i�[���ꂽ�R���N�V����
	 * @return ���f���I�u�W�F�N�g
	 */
	private Object getModel(Field field, Map<String, Object> modelMap) {
		// �A�m�e�[�V�������擾
		WicketComponent annotation = field.getAnnotation(WicketComponent.class);
		// model�����l���擾
		String modelName = annotation.model();
		// model�����l���w�肳�ꂽ���`�F�b�N
		if (StringUtils.isNotEmpty(modelName)) {
			// ���f���I�u�W�F�N�g�̃R���N�V��������擾
			Object result = modelMap.get(modelName);
			// ���ʂ�ԋp
			return result;
		} else {
			// ���f���̌���1���ǂ����`�F�b�N
			if (modelMap.size() == 1) {
				// �R���N�V��������擾���ĕԋp
				return modelMap.values().iterator().next();
			} else {
				// ���f�������ł��Ȃ�
				throw new IllegalStateException("Attribute[model] not found. Field name is " + field.getName());
			}
		}
	}

	/**
	 * �w�肳�ꂽcreate[�t�B�[���h��]Component()���\�b�h���Ăяo���C���̌��ʂ̃I�u�W�F�N�g���R���|�[�l���g�I�u�W�F�N�g�Ƃ��ĕԂ��܂��B
	 * @param createMethod create[�t�B�[���h��]Component()���\�b�h�I�u�W�F�N�g
	 * @param target �Ăяo���Ώۂ�create���\�b�h�����R���|�[�l���g�I�u�W�F�N�g
	 * @param field �����Ώۂ̃t�B�[���h�I�u�W�F�N�g
	 * @return �������ꂽ�R���|�[�l���g�I�u�W�F�N�g
	 */
	private Component createComponentByCreateMethod(Method createMethod, Component target, Field field) {
		try {
			// create���\�b�h���Ăяo���ăR���|�[�l���g�𐶐�
			Component result = (Component)createMethod.invoke(target, new Object[0]);
			// ���ʂ�ԋp
			return result;
		} catch (IllegalArgumentException e) {
			// TODO ��O����
			throw new IllegalStateException("Create component object for " + target.getClass().getName() + "#" + field.getName() + " failed.", e);
		} catch (IllegalAccessException e) {
			// TODO ��O����
			throw new IllegalStateException("Create component object for " + target.getClass().getName() + "#" + field.getName() + " failed.", e);
		} catch (InvocationTargetException e) {
			// TODO ��O����
			throw new IllegalStateException("Create component object for " + target.getClass().getName() + "#" + field.getName() + " failed.", e);
		}
	}
	
	/**
	 * �w�肳�ꂽ�t�B�[���h�ɑΉ�����create[�t�B�[���h��]Component()���\�b�h��Ԃ��܂��B
	 * @param field �����Ώۂ̃t�B�[���h�I�u�W�F�N�g
	 * @param target ���\�b�h�������ǂ�������������R���|�[�l���g�I�u�W�F�N�g
	 * @return create[�t�B�[���h��]Component()���\�b�h�I�u�W�F�N�g �������݂��Ȃ��ꍇ��null
	 */
	private Method getCreateMethod(Field field, Component target) {
		// ���\�b�h��������
		String methodName = "create" + StringUtils.capitalize(field.getName()) + "Component";
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

	/**
	 * �w�肳�ꂽ�t�B�[���h�ɕt�^���ꂽ�A�m�e�[�V�����ɏ]���āC�e�R���e�i�R���|�[�l���g�����肵�C���̌��ʂ�Ԃ��܂��B
	 * @param field �����Ώۂ̃t�B�[���h
	 * @param target �e�R���e�i�̎擾�ΏۂƂȂ�R���|�[�l���g�I�u�W�F�N�g
	 * @return �e�R���e�i�R���|�[�l���g�I�u�W�F�N�g
	 */
	private MarkupContainer getParentContainer(Field field, Component target) {
		// ���ʂ̐e�R���e�i�̕ϐ�
		MarkupContainer result;
		// �A�m�e�[�V�������擾
		WicketComponent annotation = field.getAnnotation(WicketComponent.class);
		// parent�����l���擾
		String parentAttr = annotation.parent();
		// parent�����l���`�F�b�N
		if ((StringUtils.isEmpty(parentAttr)) || (parentAttr.equals("this"))) {
			// �����Ώۂ̃R���|�[�l���g���g��e�R���e�i�Ƃ���
			result = (MarkupContainer)target;
		} else {
			try {
				// parent�����l�Ŏ����ꂽ�t�B�[���h���擾
				Class<? extends Component> clazz = target.getClass();
				Field parentField = clazz.getDeclaredField(parentAttr);
				// �t�B�[���h�̃A�N�Z�X����ύX
				if (!(parentField.isAccessible())) {
					parentField.setAccessible(true);
				}
				// �t�B�[���h�̒l��e�R���e�i�R���|�[�l���g�Ƃ���
				result = (MarkupContainer)(parentField.get(target));
			} catch (SecurityException e) {
				// TODO ��O����
				throw new IllegalStateException("Get parent container for " + target.getClass().getName() + "#" + field.getName() + " failed.", e);
			} catch (NoSuchFieldException e) {
				// TODO ��O����
				throw new IllegalStateException("Get parent container for " + target.getClass().getName() + "#" + field.getName() + " failed.", e);
			} catch (IllegalArgumentException e) {
				// TODO ��O����
				throw new IllegalStateException("Get parent container for " + target.getClass().getName() + "#" + field.getName() + " failed.", e);
			} catch (IllegalAccessException e) {
				// TODO ��O����
				throw new IllegalStateException("Get parent container for " + target.getClass().getName() + "#" + field.getName() + " failed.", e);
			}
		}
		// ���ʂ�ԋp
		return result;
	}

}
