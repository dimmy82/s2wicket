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

import java.lang.reflect.Field;

import wicket.model.BoundCompoundPropertyModel;

/**
 * �R���|�[�l���g��{@link BoundCompoundPropertyModel}�ƃo�C���h����K�v�����邱�Ƃ�\����O�N���X�ł��B
 * @author Yoichiro Tanaka
 * @since 1.3.0
 */
class NecessaryToBindException extends Exception {
	
	/** �o�C���h���郂�f�����֘A�Â���ꂽ�e�R���|�[�l���g�̃t�B�[���h�I�u�W�F�N�g */
	private Field parentField;

	/**
	 * ���̃I�u�W�F�N�g�����������Ƃ��ɌĂяo����܂��B
	 * @param parentField �o�C���h���郂�f�����֘A�Â���ꂽ�e�R���|�[�l���g�̃t�B�[���h�I�u�W�F�N�g
	 */
	NecessaryToBindException(Field parentField) {
		super();
		this.parentField = parentField;
	}
	
	/**
	 * �o�C���h���郂�f�����֘A�Â���ꂽ�e�R���|�[�l���g�̃t�B�[���h�I�u�W�F�N�g��Ԃ��܂��B
	 * @return �o�C���h���郂�f�����֘A�Â���ꂽ�e�R���|�[�l���g�̃t�B�[���h�I�u�W�F�N�g
	 */
	Field getParentField() {
		return parentField;
	}

}
