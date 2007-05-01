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

import static org.seasar.wicket.utils.Gadget.isWicketClass;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.seasar.framework.container.S2Container;
import org.seasar.wicket.injection.fieldfilters.AnnotationFieldFilter;

/**
 * SeasarComponent�A�m�e�[�V���������t�B�[���h�ɃC���W�F�N�V�������s�����������N���X�ł��B
 * @author Yoichiro Tanaka
 * @since 1.0.0
 */
class InjectionProcessor {
	
	/** �t�B�[���h�l�񋟃I�u�W�F�N�g */
	private FieldValueProducer fieldValueProducer;
	
	/**
	 * �t�B�[���h�l�񋟃I�u�W�F�N�g��Ԃ��܂��B
	 * @return �t�B�[���h�l�񋟃I�u�W�F�N�g
	 */
	FieldValueProducer getFieldValueProducer() {
		return fieldValueProducer;
	}
	
	/**
	 * ���̃I�u�W�F�N�g�����������Ƃ��ɌĂяo����܂��B
	 * ���̃R���X�g���N�^�ł́C�t�B�[���h�t�B���^�Ƃ���{@link AnnotationFieldFilter}�I�u�W�F�N�g���K�p����܂��B
	 * ����ɁCSeasar�R���e�i��{@link FieldFilter}�C���^�t�F�[�X�̎����I�u�W�F�N�g���o�^����Ă���ꍇ�́C
	 * ���̃I�u�W�F�N�g���K�p����܂��B
	 * @param containerLocator Seasar�R���e�i���P�[�^
	 */
	InjectionProcessor(IS2ContainerLocator containerLocator) {
		super();
		// �����`�F�b�N
		if (containerLocator == null)
			throw new IllegalArgumentException("containerLocator is null.");
		// �f�t�H���g�̃t�B�[���h�t�B���^�����𐶐�
		List<FieldFilter> fieldFilters = createDefaultFieldFilters(containerLocator);
		// �t�B�[���h�l�����I�u�W�F�N�g�𐶐�
		fieldValueProducer = new FieldValueProducer(containerLocator, fieldFilters);
	}
	
	/**
	 * ���̃I�u�W�F�N�g�����������Ƃ��ɌĂяo����܂��B
	 * @param containerLocator Seasar�R���e�i���P�[�^
	 * @param fieldFilters �t�B�[���h�t�B���^���i�[���ꂽ�R���N�V����
	 */
	InjectionProcessor(IS2ContainerLocator containerLocator, List<FieldFilter> fieldFilters) {
		super();
		// �����`�F�b�N
		if (containerLocator == null)
			throw new IllegalArgumentException("containerLocator is null.");
		if (fieldFilters == null)
			throw new IllegalArgumentException("fieldFilters is null.");
		if (fieldFilters.isEmpty())
			throw new IllegalArgumentException("fieldFilters is empty.");
		// �t�B�[���h�l�����I�u�W�F�N�g�𐶐�
		fieldValueProducer = new FieldValueProducer(containerLocator, fieldFilters);
	}
	
	/**
	 * �w�肳�ꂽ�I�u�W�F�N�g�����t�B�[���h�ɑ΂��āC�C���W�F�N�V�������s���܂��B
	 * @param target �����Ώۂ̃I�u�W�F�N�g
	 */
	void inject(Object target) {
		// �C���W�F�N�V���������s
		inject(target, fieldValueProducer);
	}
	
	/**
	 * �w�肳�ꂽ�I�u�W�F�N�g�����t�B�[���h�ɑ΂��āC�w�肳�ꂽ
	 * �t�B�[���h�l�����I�u�W�F�N�g����Seasar�R���|�[�l���g���擾���āC������Ăяo�����I�v���L�V��
	 * �C���W�F�N�V�������܂��B
	 * @param target �ΏۃI�u�W�F�N�g
	 * @param fieldValueProducer �t�B�[���h�l�����I�u�W�F�N�g
	 */
	private void inject(Object target, FieldValueProducer fieldValueProducer) {
		// �ΏۃI�u�W�F�N�g�̃N���X�I�u�W�F�N�g���擾
		Class<? extends Object> clazz = target.getClass();
		// �C���W�F�N�V���������Ώۂ̃t�B�[���h���擾
		SupportedField[] targetFields = getTargetFields(clazz, fieldValueProducer);
		// �t�B�[���h���ɏ���
		for (int i = 0; i < targetFields.length; i++) {
			Field targetField = targetFields[i].getField();
			// �t�B�[���h�ɃA�N�Z�X�ł���悤�ɂ���
			if (!targetField.isAccessible()) {
				targetField.setAccessible(true);
			}
			try {
				// �ΏۃI�u�W�F�N�g�̃t�B�[���h�l��null���`�F�b�N
				if (targetField.get(target) == null) {
					// �t�B�[���h�l�Ƃ���v���L�V�I�u�W�F�N�g���擾
					Object fieldValue = fieldValueProducer.getValue(targetFields[i]);
					// �t�B�[���h�ɃC���W�F�N�g
					targetField.set(target, fieldValue);
				}
			} catch (IllegalArgumentException e) {
				throw new IllegalArgumentException("Field injection failed.", e);
			} catch (IllegalAccessException e) {
				throw new IllegalStateException("Field injection failed.", e);
			}
		}
	}

	/**
	 * �����Ώۂ̃t�B�[���h��Ԃ��܂��B
	 * @param clazz �ΏۃN���X�I�u�W�F�N�g
	 * @param fieldValueProducer �t�B�[���h�l�����I�u�W�F�N�g
	 * @return �����Ώۂ̃t�B�[���h�̔z��
	 */
	private SupportedField[] getTargetFields(Class<? extends Object> clazz, FieldValueProducer fieldValueProducer) {
		// ���ʂ��i�[����R���N�V�����𐶐�
		List<SupportedField> resultList = new ArrayList<SupportedField>();
		// �����Ώۂ��Ȃ��Ȃ邩�CWicket�񋟃N���X�ɂȂ�܂ŌJ��Ԃ�
		while((clazz != null) && (!(isWicketClass(clazz)))) {
			// ��`����Ă���t�B�[���h���擾
			Field[] fields = clazz.getDeclaredFields();
			// �t�B�[���h���ɏ���
			for (int i = 0; i < fields.length; i++) {
				// �T�|�[�g����Ă���t�B�[���h���`�F�b�N
				FieldFilter fieldFilter = fieldValueProducer.isSupported(fields[i]);
				if (fieldFilter != null) {
					// ���ʂ̃R���N�V�����ɒǉ�
					resultList.add(new SupportedField(fields[i], fieldFilter));
				}
			}
			// �X�[�p�[�N���X���擾�����l�̌������s��
			clazz = clazz.getSuperclass();
		}
		// ���ʂ�ԋp
		return resultList.toArray(new SupportedField[0]);
	}
	
	/**
	 * �f�t�H���g�̃t�B�[���h�t�B���^���i�[���ꂽ�R���N�V������Ԃ��܂��B
	 * ���̃��\�b�h�ł́C{@link AnnotationFieldFilter}�I�u�W�F�N�g�ƁC
	 * Seasar�R���e�i�ɓo�^���ꂽ{@link FieldFilter}�C���^�t�F�[�X����������Seasar�R���|�[�l���g��
	 * �R���N�V�����Ɋi�[���ĕԂ��܂��B
	 * @param containerLocator Seasar�R���e�i���P�[�^
	 * @return {@link AnnotationFieldFilter}�I�u�W�F�N�g�����{@link FieldFilter}�C���^�t�F�[�X������������Seasar�R���e�i�ɓo�^���ꂽ�I�u�W�F�N�g�����R���N�V����
	 */
	private List<FieldFilter> createDefaultFieldFilters(IS2ContainerLocator containerLocator) {
		List<FieldFilter> fieldFilters = new ArrayList<FieldFilter>();
		S2Container container = containerLocator.get();
		FieldFilter[] filters = (FieldFilter[])container.findComponents(FieldFilter.class);
		if (filters != null) {
			fieldFilters.addAll(Arrays.asList(filters));
		}
		fieldFilters.add(new AnnotationFieldFilter());
		return fieldFilters;
	}

}
