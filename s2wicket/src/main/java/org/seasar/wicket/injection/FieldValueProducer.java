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

import java.lang.reflect.Field;
import java.util.List;

/**
 * SeasarComponent�A�m�e�[�V�������t�^���ꂽ�t�B�[���h�ɃZ�b�g����l����������N���X�ł��B
 * ���ۂ�Seasar�R���|�[�l���g�̃��\�b�h�̌Ăяo�����s����v���L�V�I�u�W�F�N�g��񋟂��܂��B
 * @author Yoichiro Tanaka
 * @since 1.0.0
 */
class FieldValueProducer {
	
	/** Seasar�R���e�i���P�[�^ */
	private IS2ContainerLocator containerLocator;
	
	/** �t�B�[���h�t�B���^�̃R���N�V���� */
	private List<FieldFilter> fieldFilterList;
	
	/**
	 * ���̃I�u�W�F�N�g�����������Ƃ��ɌĂяo����܂��B
	 * @param containerLocator �R���e�i���P�[�^
	 * @param fieldFilterList �t�B�[���h�t�B���^���i�[���ꂽ�R���N�V����
	 */
	FieldValueProducer(IS2ContainerLocator containerLocator, List<FieldFilter> fieldFilterList) {
		super();
		// �����`�F�b�N
		if (containerLocator == null)
			throw new IllegalArgumentException("containerLocator is null.");
		if (fieldFilterList == null)
			throw new IllegalArgumentException("fieldFilterList is null.");
		if (fieldFilterList.isEmpty())
			throw new IllegalArgumentException("fieldFilterList is empty.");
		this.containerLocator = containerLocator;
		this.fieldFilterList = fieldFilterList;
	}
	
	/**
	 * �w�肳�ꂽ�t�B�[���h�ɑΉ�����v���L�V�I�u�W�F�N�g�𐶐����ĕԂ��܂��B
	 * ���̃��\�b�h�ɓn�����t�B�[���h�́C{{@link #isSupported(Field)}���\�b�h�Ăяo���̌��ʂ�true�̂��݂̂̂ł��B
	 * @param supportedField �T�|�[�g���ꂽ�t�B�[���h�I�u�W�F�N�g
	 * @return �v���L�V�I�u�W�F�N�g
	 */
	Object getValue(SupportedField supportedField) {
		// �����`�F�b�N
		if (supportedField == null)
			throw new IllegalArgumentException("field is null.");
		// �R���|�[�l���g�����擾
		String componentName = supportedField.getLookupComponentName();
		// �t�B�[���h�I�u�W�F�N�g���擾
		Field field = supportedField.getField();
		// Seasar�R���|�[�l���g���]���o�𐶐�
		ComponentResolver resolver = new ComponentResolver(componentName, field.getType(), containerLocator);
		// �v���L�V�𐶐�
		Object proxy = ProxyFactory.create(field.getType(), resolver);
		// �v���L�V��ԋp
		return proxy;
	}
	
	/**
	 * �w�肳�ꂽ�t�B�[���h���C���W�F�N�V�����̑ΏۂƂ��ăT�|�[�g����Ă��邩�ǂ������`�F�b�N���܂��B
	 * �`�F�b�N�������ʁC�T�|�[�g����t�B�[���h�t�B���^�����݂����ꍇ�́C���̃t�B�[���h�t�B���^��Ԃ��܂��B
	 * �����T�|�[�g����t�B�[���h�t�B���^�����݂��Ȃ������ꍇ�́Cnull��Ԃ��܂��B
	 * @param field �t�B�[���h
	 * @return �T�|�[�g����Ă���΁C�T�|�[�g����t�B�[���h�t�B���^�I�u�W�F�N�g�B�T�|�[�g����Ȃ��ꍇ�� null�B
	 */
	FieldFilter isSupported(Field field) {
		for (FieldFilter filter : fieldFilterList) {
			if (filter.isSupported(field)) {
				return filter;
			}
		}
		return null;
	}

	/**
	 * �K�p����Ă���t�B�[���h�t�B���^�̃R���N�V������Ԃ��܂��B
	 * @return �t�B�[���h�t�B���^�̃R���N�V����
	 */
	List<FieldFilter> getFieldFilters() {
		return fieldFilterList;
	}
	
}
