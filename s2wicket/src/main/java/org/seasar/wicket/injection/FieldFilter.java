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

import java.io.Serializable;
import java.lang.reflect.Field;

/**
 * �w�肳�ꂽ�t�B�[���h�ɂ��āC���ꂪ�C���W�F�N�V�����Ώۂ��ǂ����𔻒f���C
 * �C���W�F�N�V�����Ώۂ������ꍇ�́C���b�N�A�b�v���邽�߂�Seasar�R���|�[�l���g����񋟂���
 * �������K�肵���C���^�t�F�[�X�ł��B
 * @author Yoichiro Tanaka
 * @since 1.1.0
 */
public interface FieldFilter extends Serializable {

	/**
	 * �w�肳�ꂽ�t�B�[���h���C���W�F�N�V�����̑ΏۂƂ��ăT�|�[�g����Ă��邩�ǂ�����Ԃ��܂��B
	 * @param field �t�B�[���h
	 * @return �T�|�[�g����Ă���� true
	 */
	public boolean isSupported(Field field);
	
	/**
	 * �w�肳�ꂽ�t�B�[���h�ɂ��āC���b�N�A�b�v����Seasar�R���|�[�l���g�̃R���|�[�l���g����Ԃ��܂��B
	 * ���̃��\�b�h�ɓn�����field�I�u�W�F�N�g�́C{{@link #isSupported(Field)}���\�b�h�̌Ăяo�����ʂ�
	 * true �̃I�u�W�F�N�g�݂̂ƂȂ�܂��B
	 * ����Seasar�R���|�[�l���g���ł͂Ȃ��C�t�B�[���h�̌^�Ń��b�N�A�b�v���s���ꍇ�́Cnull��ԋp���Ă��������B
	 * @param field �t�B�[���h
	 * @return ���b�N�A�b�v����Seasar�R���|�[�l���g���B�����t�B�[���h�̌^�Ń��b�N�A�b�v����ꍇ�� null
	 */
	public String getLookupComponentName(Field field);
	
}
