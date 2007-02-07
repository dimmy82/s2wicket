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

import org.apache.commons.lang.StringUtils;

/**
 * SeasarComponent�A�m�e�[�V�������t�^���ꂽ�t�B�[���h���C���W�F�N�V�����ΏۂƂ��鏈�������t�B���^�����N���X�ł��B
 * @author Yoichiro Tanaka
 * @since 1.1.0
 */
public class AnnotationFieldFilter implements FieldFilter {

	/**
	 * �w�肳�ꂽ�t�B�[���h���C���W�F�N�V�����̑ΏۂƂ��ăT�|�[�g����Ă��邩�ǂ�����Ԃ��܂��B
	 * ���̎����ł́C�w�肳�ꂽ�t�B�[���h��SeasarComponent�A�m�e�[�V�������t�^����Ă��邩�ǂ������`�F�b�N���C
	 * �t�^����Ă���ꍇ�̓C���W�F�N�V�����ΏۂƂ��� true ��Ԃ��܂��B
	 * @param field �t�B�[���h
	 * @return SeasarComponent�A�m�e�[�V�������t�^����Ă���t�B�[���h�������ꍇ�� true
	 * @see org.seasar.wicket.injection.FieldFilter#isSupported(java.lang.reflect.Field)
	 */
	public boolean isSupported(Field field) {
		// �����`�F�b�N
		if (field == null)
			throw new IllegalArgumentException("field is null.");
		// SeasarComponent�A�m�e�[�V�������t�^����Ă��邩�ǂ�����ԋp
		return field.isAnnotationPresent(SeasarComponent.class);
	}

	/**
	 * �w�肳�ꂽ�t�B�[���h�ɂ��āC���b�N�A�b�v����Seasar�R���|�[�l���g�̃R���|�[�l���g����Ԃ��܂��B
	 * ���̎����ł́C�t�B�[���h�ɕt�^���ꂽSeasarComponent�A�m�e�[�V������name�����l��
	 * �R���|�[�l���g���Ƃ��ĕԂ��܂��B����name�������Ȃ������ꍇ�́Cnull��Ԃ��܂��B
	 * @param field �t�B�[���h
	 * @return SeasarComponent�A�m�e�[�V������name�����l
	 * @throws IllegalArgumentException {{@link #isSupported(Field)}���\�b�h�̌Ăяo�����ʂ�false�̃t�B�[���h���^����ꂽ�Ƃ�
	 * @see org.seasar.wicket.injection.FieldFilter#getLookupComponentName(java.lang.reflect.Field)
	 */
	public String getLookupComponentName(Field field) {
		// �����`�F�b�N
		if (!isSupported(field)) {
			throw new IllegalArgumentException("field is not supported.");
		}
		// SeasarComponent�A�m�e�[�V�������擾
		SeasarComponent annotation = field.getAnnotation(SeasarComponent.class);
		// name�����l���擾
		String name = annotation.name();
		// ���ʂ�ԋp
		return (StringUtils.isEmpty(name)) ? null : name;
	}

}
