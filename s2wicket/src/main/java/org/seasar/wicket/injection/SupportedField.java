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

/**
 * �t�B�[���h�t�B���^�ɂ��C���W�F�N�V�����ΏۂƔ��f���ꂽ�t�B�[���h��\���N���X�ł��B
 * ���̃N���X�́C�T�|�[�g����t�B�[���h�ƁC�T�|�[�g�̋��𔻒f�����t�B�[���h�t�B���^���֘A�t���邽�߂ɍ쐬����܂����B
 * @see FieldFilter
 * @author yoichiro
 * @since 1.1.0
 */
class SupportedField {
	
	/** �C���W�F�N�V�����ΏۂƂ��ăT�|�[�g�����t�B�[���h */
	private Field field;
	
	/** �T�|�[�g�𔻒f�����t�B�[���h�t�B���^ */
	private FieldFilter fieldFilter;

	/**
	 * ���̃I�u�W�F�N�g�����������Ƃ��ɌĂяo����܂��B
	 * @param field �C���W�F�N�V�����ΏۂƂ��ăT�|�[�g�����t�B�[���h
	 * @param fieldFilter �T�|�[�g�𔻒f�����t�B�[���h�t�B���^
	 */
	SupportedField(Field field, FieldFilter fieldFilter) {
		super();
		this.field = field;
		this.fieldFilter = fieldFilter;
	}

	/**
	 * �C���W�F�N�V�����ΏۂƂ��ăT�|�[�g�����t�B�[���h��Ԃ��܂��B
	 * @return �C���W�F�N�V�����ΏۂƂ��ăT�|�[�g�����t�B�[���h
	 */
	Field getField() {
		return field;
	}

	/**
	 * �T�|�[�g�𔻒f�����t�B�[���h�t�B���^��Ԃ��܂��B
	 * @return �T�|�[�g�𔻒f�����t�B�[���h�t�B���^
	 */
	FieldFilter getFieldFilter() {
		return fieldFilter;
	}
	
	/**
	 * �w�肳�ꂽ�t�B�[���h�̂��߂�Seasar�R���e�i����R���|�[�l���g�I�u�W�F�N�g���b�N�A�b�v�ۂɎg�p����R���|�[�l���g����Ԃ��܂��B
	 * @return �R���|�[�l���g��
	 */
	String getLookupComponentName() {
		return fieldFilter.getLookupComponentName(field);
	}

}
