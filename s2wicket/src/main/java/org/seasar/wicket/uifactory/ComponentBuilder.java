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

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;

import wicket.Component;
import wicket.MarkupContainer;
import wicket.model.BoundCompoundPropertyModel;
import wicket.model.CompoundPropertyModel;
import wicket.model.IModel;
import wicket.model.LoadableDetachableModel;
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
	 * @param field �����Ώۂ̃t�B�[���h�I�u�W�F�N�g
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
			result = createComponentByFieldType(field, target, modelMap, parent.getId());
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
	 * @param parent �e�̃R���e�i�R���|�[�l���g
	 * @return �������ꂽ�R���|�[�l���g�I�u�W�F�N�g
	 */
	private Component createComponentByFieldType(Field field, Component target, Map<Field, Object> modelMap, String parentId) {
		// ���������R���|�[�l���g�I�u�W�F�N�g�̕ϐ�
		Component result;
		// ���f�����擾
		Object model;
		IModel parentModel = null;
		try {
			model = createModel(field, target, modelMap);
		} catch(NecessaryToBindException e) {
			// �e�̃R���|�[�l���g�̃��f���ƃo�C���h����K�v����
			model = null;
			try {
				Component parentComponent = (Component)e.getParentField().get(target);
				parentModel = parentComponent.getModel();
			} catch (IllegalArgumentException e1) {
				throw new WicketUIFactoryException(target, "Get value of parent field failed.", e1);
			} catch (IllegalAccessException e1) {
				throw new WicketUIFactoryException(target, "Get value of parent field failed.", e1);
			}
		}
		// wicket:id������
		String wicketId = getWicketId(field);
		// �t�B�[���h�̌^���擾
		Class<?> clazz = field.getType();
		// ���I�v���L�V�𐶐�
		result = ComponentProxyFactory.create(field.getName(), clazz, target, wicketId, model, parentId);
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
	
	/**
	 * �w�肳�ꂽ�R���|�[�l���g�Ɋ֘A�t���郂�f���𐶐����܂��B
	 * @param field �����Ώۂ̃R���|�[�l���g�̃t�B�[���h
	 * @param target �����Ώۂ̃R���|�[�l���g�I�u�W�F�N�g
	 * @param modelMap �������ꂽ���f���I�u�W�F�N�g���i�[���ꂽ�R���N�V����
	 * @return �������ꂽ���f��
	 * @throws NecessaryToBindException �֘A�t����BoundCompoundPropertyModel�ɂ��v���p�e�B�̖����I�Ȏw���K�v�Ƃ���Ƃ�
	 */
	private Object createModel(Field field, Component target, Map<Field, Object> modelMap) throws NecessaryToBindException {
		// WicketComponent�A�m�e�[�V�������擾
		WicketComponent targetAnnotation = field.getAnnotation(WicketComponent.class);
		// �ǂ̃��f���I�u�W�F�N�g���g�p���邩�̑������擾
		UseModel useModel = targetAnnotation.useModel();
		// �ǂ̃��f���I�u�W�F�N�g���g�p���邩�ɂ�菈������
		if (useModel.equals(UseModel.FIELD)) { // �t�B�[���h�ɒ�`���ꂽ���f�����g�p
			// ���f�����������擾
			String modelName = targetAnnotation.modelName();
			// ���f�������w�肳��Ă������`�F�b�N
			if (StringUtils.isNotEmpty(modelName)) {
				// ���f�������w�肳�ꂽ�ꍇ�̏������R�[��
				return createModelForSpecifiedModelName(field, modelMap, modelName, target);
			} else {
				// ���f�������w�肳��Ȃ������ꍇ�̏������R�[��
				return createModelForNotSpecifiedModelName(field, target, modelMap);
			}
		} else if (useModel.equals(UseModel.LOADABLE_DETACHABLE)) { // �K�v�ȂƂ��ɓǂݍ��݁C�s�K�v�ɂȂ������菜�����f�����g�p
			// LoadableDetachableModel�̓��I�v���L�V�𐶐�
			LoadableDetachableModel loadableDetachableModel = LoadableDetachableModelProxyFactory.create(field.getName(), target);
			// ���ʂ�ԋp
			return loadableDetachableModel;
		} else {
			throw new IllegalStateException("Unknown UseModel value.");
		}
	}
	
	/**
	 * �w�肳�ꂽ�R���|�[�l���g�t�B�[���h��WicketComponent�A�m�e�[�V�����ɂ����āC
	 * ���f�������w�肳��Ȃ������Ƃ��̃��f�������������s���܂��B
	 * @param field �����Ώۂ̃R���|�[�l���g�̃t�B�[���h
	 * @param target �����Ώۂ̃R���|�[�l���g�I�u�W�F�N�g
	 * @param modelMap �������ꂽ���f���I�u�W�F�N�g���i�[���ꂽ�R���N�V����
	 * @return �������ꂽ���f��
	 * @throws NecessaryToBindException �֘A�t����BoundCompoundPropertyModel�ɂ��v���p�e�B�̖����I�Ȏw���K�v�Ƃ���Ƃ�
	 */
	private Object createModelForNotSpecifiedModelName(Field field, Component target, Map<Field, Object> modelMap) throws NecessaryToBindException {
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
					Entry<Field, Object> modelMapEntry = getModelMapEntry(parentModelName, modelMap, target);
					Field modelField = modelMapEntry.getKey();
					// ���f���t�B�[���h��WicketModel�A�m�e�[�V�������擾
					WicketModel modelAnnotation = modelField.getAnnotation(WicketModel.class);
					// ���f���t�B�[���h�̃��f����ʑ������擾
					ModelType modelType = modelAnnotation.type();
					// ���f����ʖ��ɏ���
					if (modelType.equals(ModelType.RAW)) { // Raw
						// ���Ή�
						throw new UnsupportedOperationException("ModelType.RAW for parent container not supported.");
					} else if (modelType.equals(ModelType.BASIC)) { // Model
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
						throw new IllegalStateException("Unknown ModelType.");
					}
				} else {
					// �f�t�H���g�̃��f���𐶐����ԋp
					return createDefaultModel(field, modelMap);
				}
			} catch(NoSuchFieldException e) {
				throw new WicketUIFactoryException(target, "Parent component field[" + parentName + "] not found.", e);
			}
		} else {
			// �f�t�H���g�̃��f���𐶐����ԋp
			return createDefaultModel(field, modelMap);				
		}
	}
	
	/**
	 * �w�肳�ꂽ�R���|�[�l���g�t�B�[���h��WicketComponent�A�m�e�[�V�����ɂ����āC
	 * ���f�������w�肳�ꂽ�Ƃ��̃��f�������������s���܂��B
	 * @param field �����Ώۂ̃R���|�[�l���g�̃t�B�[���h
	 * @param modelMap �������ꂽ���f���I�u�W�F�N�g���i�[���ꂽ�R���N�V����
	 * @param modelName �w�肳�ꂽ���f���̖��O
	 * @param target �����Ώۂ̃R���|�[�l���g�I�u�W�F�N�g
	 * @return �������ꂽ���f��
	 */
	private Object createModelForSpecifiedModelName(Field field, Map<Field, Object> modelMap, String modelName, Component target) {
		// ���f���I�u�W�F�N�g�̃t�B�[���h�ƃI�u�W�F�N�g���擾
		Entry<Field, Object> modelMapEntry = getModelMapEntry(modelName, modelMap, target);
		Field modelField = modelMapEntry.getKey();
		Object modelObj = modelMapEntry.getValue();
		// ���f���t�B�[���h��WicketModel�A�m�e�[�V�������擾
		WicketModel modelAnnotation = modelField.getAnnotation(WicketModel.class);
		// ���f���t�B�[���h�̃��f����ʑ������擾
		ModelType modelType = modelAnnotation.type();
		// ���f����ʖ��ɏ���
		if (modelType.equals(ModelType.RAW)) { // ���f���I�u�W�F�N�g���̂܂�
			// ���f���I�u�W�F�N�g�����̂܂ܕԋp
			return modelObj;
		} else if (modelType.equals(ModelType.BASIC)) { // Model
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
			throw new IllegalStateException("Unknown ModelType.");
		}
	}
	
	/**
	 * �����I�Ɏg�p���郂�f�����w�肳��Ȃ������Ƃ��Ɋ֘A�t���郂�f���𐶐����܂��B
	 * @param field �����Ώۂ̃R���|�[�l���g�t�B�[���h
	 * @param modelMap �������ꂽ���f���I�u�W�F�N�g���i�[���ꂽ�R���N�V����
	 * @return �������ꂽ���f��
	 */
	private Object createDefaultModel(Field field, Map<Field, Object> modelMap) {
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
			if (modelType.equals(ModelType.RAW)) { // Raw
				// ���f���Ɗ֘A�t�����s��Ȃ�
				return null;
			} else if (modelType.equals(ModelType.BASIC)) { // Model
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
				throw new IllegalStateException("Unknown ModelType.");
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
	 * @param target �����Ώۂ̃R���|�[�l���g�I�u�W�F�N�g
	 * @return ���f���I�u�W�F�N�g�̃}�b�v�G���g��
	 */
	private Map.Entry<Field, Object> getModelMapEntry(String fieldName, Map<Field, Object> modelMap, Component target) {
		for(Map.Entry<Field, Object> entry : modelMap.entrySet()) {
			if (entry.getKey().getName().equals(fieldName)) {
				return entry;
			}
		}
		throw new WicketUIFactoryException(target, "Model object entry not found. fieldName = " + fieldName);
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
			throw new WicketUIFactoryException(target, "Create component object for " + field.getName() + " failed.", e);
		} catch (IllegalAccessException e) {
			throw new WicketUIFactoryException(target, "Create component object for " + field.getName() + " failed.", e);
		} catch (InvocationTargetException e) {
			throw new WicketUIFactoryException(target, "Create component object for " + field.getName() + " failed.", e);
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
				throw new WicketUIFactoryException(target, "Get parent container for " + field.getName() + " failed.", e);
			} catch (NoSuchFieldException e) {
				throw new WicketUIFactoryException(target, "Get parent container for " + field.getName() + " failed.", e);
			} catch (IllegalArgumentException e) {
				throw new WicketUIFactoryException(target, "Get parent container for " + field.getName() + " failed.", e);
			} catch (IllegalAccessException e) {
				throw new WicketUIFactoryException(target, "Get parent container for " + field.getName() + " failed.", e);
			}
		}
		// ���ʂ�ԋp
		return result;
	}

}
