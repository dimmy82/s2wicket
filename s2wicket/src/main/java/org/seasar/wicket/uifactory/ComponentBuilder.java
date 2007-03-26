package org.seasar.wicket.uifactory;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;

import wicket.Component;
import wicket.MarkupContainer;
import wicket.model.BoundCompoundPropertyModel;
import wicket.model.CompoundPropertyModel;
import wicket.model.IModel;
import wicket.model.Model;
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
	Object build(Field field, Component target, Map<Field, Object> modelMap) {
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
	private Component createComponentByFieldType(Field field, Component target, Map<Field, Object> modelMap) {
		// ���������R���|�[�l���g�I�u�W�F�N�g�̕ϐ�
		Component result;
		// ���f�����擾
		IModel model;
		IModel parentModel = null;
		try {
			model = createIModel(field, target, modelMap);
		} catch(NecessaryToBindException e) {
			// �e�̃R���|�[�l���g�̃��f���ƃo�C���h����K�v����
			model = null;
			try {
				Component parentComponent = (Component)field.get(target);
				parentModel = parentComponent.getModel();
			} catch (IllegalArgumentException e1) {
				// TODO ��O����
				throw new IllegalStateException(e1);
			} catch (IllegalAccessException e1) {
				// TODO ��O����
				throw new IllegalStateException(e1);
			}
		}
		// wicket:id������
		String wicketId = getWicketId(field);
		// �t�B�[���h�̌^���擾
		Class<?> clazz = field.getType();
		// �t�B�[���h�̌^�����ۃN���X���`�F�b�N
		if (Modifier.isAbstract(clazz.getModifiers())) {
			// ���I�v���L�V�𐶐�
			result = ComponentProxyFactory.create(field.getName(), clazz, target, wicketId, model);
		} else {
			// �f���ɃC���X�^���X����
			result = createNewComponentInstance(field, target, wicketId, model);
		}
		// �e�̃R���|�[�l���g�̃��f���ƃo�C���h����K�v�����`�F�b�N
		if (parentModel != null) {
			// �v���p�e�B�����擾
			String propertyName = getPropertyName(field);
			// ���f���ƃo�C���h
			((BoundCompoundPropertyModel)parentModel).bind(result, propertyName);
		}
		// ���������R���|�[�l���g�I�u�W�F�N�g��ԋp
		return result;
	}
	
	private IModel createIModel(Field field, Component target, Map<Field, Object> modelMap) throws NecessaryToBindException {
		// WicketComponent�A�m�e�[�V�������擾
		WicketComponent targetAnnotation = field.getAnnotation(WicketComponent.class);
		// ���f�����������擾
		String modelName = targetAnnotation.modelName();
		// ���f�������w�肳��Ă������`�F�b�N
		if (StringUtils.isNotEmpty(modelName)) {
			// ���f�������w�肳�ꂽ�ꍇ�̏������R�[��
			return createIModelForSpecifiedModelName(field, modelMap, modelName);
		} else {
			// ���f�������w�肳��Ȃ������ꍇ�̏������R�[��
			return createIModelForNotSpecifiedModelName(field, target, modelMap);
		}
	}
	
	private IModel createIModelForNotSpecifiedModelName(Field field, Component target, Map<Field, Object> modelMap) throws NecessaryToBindException {
		// WicketComponent�A�m�e�[�V�������擾
		WicketComponent targetAnnotation = field.getAnnotation(WicketComponent.class);
		// �e�̃R���e�i�̃t�B�[���h�����擾
		String parentName = targetAnnotation.parent();
		// �e�̃R���e�i���w�肳��Ă������`�F�b�N
		if (StringUtils.isNotEmpty(parentName)) {
			try {
				// �e�̃R���e�i�̃t�B�[���h���擾
				Class<? extends Component> clazz = target.getClass();
				Field parentField = clazz.getDeclaredField(parentName);
				// �e�̃R���e�i��WicketComponent�A�m�e�[�V�������擾
				WicketComponent parentAnnotation = parentField.getAnnotation(WicketComponent.class);
				// �e�̃R���e�i��WicketComponent�A�m�e�[�V�����̃��f�����������擾
				String parentModelName = parentAnnotation.modelName();
				// ���f�����������w�肳��Ă������`�F�b�N
				if (StringUtils.isNotEmpty(parentModelName)) {
					// ���f���I�u�W�F�N�g�̃t�B�[���h���擾
					Entry<Field, Object> modelMapEntry = getModelMapEntry(parentModelName, modelMap);
					Field modelField = modelMapEntry.getKey();
					// ���f���t�B�[���h��WicketModel�A�m�e�[�V�������擾
					WicketModel modelAnnotation = modelField.getAnnotation(WicketModel.class);
					// ���f���t�B�[���h�̃��f����ʑ������擾
					ModelType modelType = modelAnnotation.type();
					// ���f����ʖ��ɏ���
					if (modelType.equals(ModelType.BASIC)) { // Model
						// ���Ή�
						throw new UnsupportedOperationException("ModelType.MODEL for parent container not supported.");
					} else if (modelType.equals(ModelType.PROPERTY)) { // PropertyModel
						// ���Ή�
						throw new UnsupportedOperationException("ModelType.PROPERTY for parent container not supported.");
					} else if (modelType.equals(ModelType.COMPOUND_PROPERTY)) { // CompoundPropertyModel
						// �R���|�[�l���g�Ƀ��f���͕K�v�Ȃ�
						return null;
					} else if (modelType.equals(ModelType.BOUND_COMPOUND_PROPERTY)) { // BoundCompoundPropertyModel
						// �o�C���h����K�v�����邱�Ƃ��O���X���[���邱�Ƃŕԋp
						throw new NecessaryToBindException(parentField);
					} else {
						// ���f����ʖ��w��͂��蓾�Ȃ�
						throw new IllegalStateException("Unknown ModelType.(2)");
					}
				} else {
					// �f�t�H���g�̃v���p�e�B���f���𐶐����ԋp
					return createDefaultPropertyModel(field, modelMap);
				}
			} catch(NoSuchFieldException e) {
				// TODO ��O����
				throw new IllegalStateException("Parent component field[" + parentName + "] not found.", e);
			}
		} else {
			// �f�t�H���g�̃v���p�e�B���f���𐶐����ԋp
			return createDefaultPropertyModel(field, modelMap);				
		}
	}
	
	private IModel createIModelForSpecifiedModelName(Field field, Map<Field, Object> modelMap, String modelName) {
		// ���f���I�u�W�F�N�g�̃t�B�[���h�ƃI�u�W�F�N�g���擾
		Entry<Field, Object> modelMapEntry = getModelMapEntry(modelName, modelMap);
		Field modelField = modelMapEntry.getKey();
		Object modelObj = modelMapEntry.getValue();
		// ���f���t�B�[���h��WicketModel�A�m�e�[�V�������擾
		WicketModel modelAnnotation = modelField.getAnnotation(WicketModel.class);
		// ���f���t�B�[���h�̃��f����ʑ������擾
		ModelType modelType = modelAnnotation.type();
		// ���f����ʖ��ɏ���
		if (modelType.equals(ModelType.BASIC)) { // Model
			// ��{���f���𐶐�
			Model model = new Model((Serializable)modelObj);
			// ��{���f����ԋp
			return model;
		} else if (modelType.equals(ModelType.PROPERTY)) { // PropertyModel
			// �֘A�t���郂�f���̃v���p�e�B�����擾
			String propertyName = getPropertyName(field);
			// �v���p�e�B���f���𐶐�
			PropertyModel propertyModel = new PropertyModel(modelObj, propertyName);
			// �v���p�e�B���f����ԋp
			return propertyModel;
		} else if (modelType.equals(ModelType.COMPOUND_PROPERTY)) { // CompoundPropertyModel
			// �����v���p�e�B���f���𐶐�
			CompoundPropertyModel compoundPropertyModel = new CompoundPropertyModel(modelObj);
			// �����v���p�e�B���f����ԋp
			return compoundPropertyModel;
		} else if (modelType.equals(ModelType.BOUND_COMPOUND_PROPERTY)) { // BoundCompoundPropertyModel
			// �����o�C���h�v���p�e�B���f���𐶐�
			BoundCompoundPropertyModel boundCompoundPropertyModel = new BoundCompoundPropertyModel(modelObj);
			// �����o�C���h�v���p�e�B���f����ԋp
			return boundCompoundPropertyModel;
		} else {
			// ���f����ʖ��w��͂��蓾�Ȃ�
			throw new IllegalStateException("Unknown ModelType.(1)");
		}
	}
	
	private IModel createDefaultPropertyModel(Field field, Map<Field, Object> modelMap) {
		// ���f���̌����`�F�b�N
		if (modelMap.size() == 1) {
			// ���f���I�u�W�F�N�g�̃t�B�[���h�ƃI�u�W�F�N�g���擾
			Entry<Field, Object> modelMapEntry = modelMap.entrySet().iterator().next();
			Field modelField = modelMapEntry.getKey();
			Object modelObj = modelMapEntry.getValue();
			// ���f���t�B�[���h��WicketModel�A�m�e�[�V�������擾
			WicketModel modelAnnotation = modelField.getAnnotation(WicketModel.class);
			// ���f���t�B�[���h�̃��f����ʑ������擾
			ModelType modelType = modelAnnotation.type();
			// ���f����ʖ��ɏ���
			if (modelType.equals(ModelType.BASIC)) { // Model
				// ���f���Ɗ֘A�t�����s��Ȃ�
				return null;
			} else if (modelType.equals(ModelType.PROPERTY)) { // PropertyModel
				// �֘A�t���郂�f���̃v���p�e�B�����擾
				String propertyName = getPropertyName(field);
				// �v���p�e�B���f���𐶐�
				PropertyModel propertyModel = new PropertyModel(modelObj, propertyName);
				// �v���p�e�B���f����ԋp
				return propertyModel;
			} else if (modelType.equals(ModelType.COMPOUND_PROPERTY)) { // CompoundPropertyModel
				// ���Ή�
				throw new UnsupportedOperationException("ModelType.COMPOUND_PROPERTY for parent container not supported.");
			} else if (modelType.equals(ModelType.BOUND_COMPOUND_PROPERTY)) { // BoundCompoundPropertyModel
				// ���Ή�
				throw new UnsupportedOperationException("ModelType.BOUND_COMPOUND_PROPERTY for parent container not supported.");
			} else {
				// ���f����ʖ��w��͂��蓾�Ȃ�
				throw new IllegalStateException("Unknown ModelType.(3)");
			} 
		} else {
			// �֘A�t���郂�f�������ł��Ȃ�
			return null;
		}
	}
	
	/**
	 * �w�肳�ꂽ�t�B�[���h���Ɉ�v���郂�f���I�u�W�F�N�g�̃}�b�v�G���g����Ԃ��܂��B
	 * @param fieldName �t�B�[���h��
	 * @param modelMap ���f���t�B�[���h�ƃ��f���I�u�W�F�N�g���i�[���ꂽ�R���N�V����
	 * @return ���f���I�u�W�F�N�g�̃}�b�v�G���g��
	 */
	private Map.Entry<Field, Object> getModelMapEntry(String fieldName, Map<Field, Object> modelMap) {
		for(Map.Entry<Field, Object> entry : modelMap.entrySet()) {
			if (entry.getKey().getName().equals(fieldName)) {
				return entry;
			}
		}
		throw new IllegalArgumentException("Model object entry not found. fieldName = " + fieldName);
	}

	/**
	 * �w�肳�ꂽ�t�B�[���h�ɑΉ�����R���|�[�l���g�I�u�W�F�N�g�𐶐����ĕԂ��܂��B
	 * @param field �����Ώۂ̃t�B�[���h�I�u�W�F�N�g
	 * @param component �����Ώۂ̃R���|�[�l���g�I�u�W�F�N�g
	 * @param wicketId wicket:id
	 * @param model ���f���I�u�W�F�N�g
	 * @return �������ꂽ�R���|�[�l���g�I�u�W�F�N�g
	 */
	private Component createNewComponentInstance(Field field, Component target, String wicketId, IModel model) {
		try {
			// �t�B�[���h�̌^���擾
			Class<?> clazz = field.getType();
			// �R���X�g���N�^�̈����̌^�̃R���N�V�����ƁC�����̃I�u�W�F�N�g�̃R���N�V�����𐶐�
			List<Class> argTypes = new ArrayList<Class>();
			List<Object> argObjs = new ArrayList<Object>();
			// �t�B�[���h�̌^���C���i�[�N���X���`�F�b�N
			if (clazz.isMemberClass()) {
				// �R���X�g���N�^�̑�P�����ɃR���|�[�l���g�I�u�W�F�N�g���Z�b�g
				argTypes.add(target.getClass());
				argObjs.add(target);
			}
			// wicket:id���Q�����Ƃ��ăZ�b�g
			argTypes.add(String.class);
			argObjs.add(wicketId);
			// ���f�������݂��邩�`�F�b�N
			if (model != null) {
				argTypes.add(IModel.class);
				argObjs.add(model);
			}
			// �R���X�g���N�^���擾
			Constructor<?> constructor = clazz.getConstructor((Class[])argTypes.toArray(new Class[0]));
			// �R���|�[�l���g�𐶐�
			Component component = (Component)constructor.newInstance(argObjs.toArray());
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
		String propertyName = annotation.modelProperty();
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
