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
 * {@link SeasarComponent}�A�m�e�[�V�������t�^���ꂽ�t�B�[���h���C���W�F�N�V�����ΏۂƂ��鏈�������t�B���^�����N���X�ł��B<br />
 * <p>���̃t�B�[���h�t�B���^�́C{@link SeasarComponent}�A�m�e�[�V�������}�[�J�[�Ƃ��āC
 * ���ꂪ�t�^���ꂽ�t�B�[���h�ɂ��āCSeasar�R���e�i�Ǘ����̃R���|�[�l���g�I�u�W�F�N�g���C���W�F�N�V�����ΏۂƔ��f���܂��B</p>
 * <p>{@link SeasarComponentInjectionListener}�N���X�̃C���X�^���X�𐶐�����ۂɁC�R���X�g���N�^��
 * {@link FieldFilter}�C���^�t�F�[�X�̎����I�u�W�F�N�g�̃R���N�V�������w�肵�Ȃ������ꍇ�́C
 * ���̃N���X�̎����I�u�W�F�N�g�������I�Ɏg�p�����悤�ɂȂ�܂��B�܂��C�Ǝ���{@link FieldFilter}�C���^�t�F�[�X��
 * �����N���X���쐬���āC���̃N���X�ƕ��p�������ꍇ�́C�ȉ��̂悤��{@link SeasarComponentInjectionListener}�I�u�W�F�N�g
 * �ɓo�^���܂��B</p>
 * <pre>
 * MyFieldFilter myFieldFilter = ...;
 * AnnotationFieldFilter annotFieldFilter = new AnnotationFieldFilter();
 * List<FieldFilter> filters = new ArrayList<FieldFilter>(2);
 * filters.add(myFieldFilter);
 * filters.add(annotFieldFilter);
 * addComponentInstantiationListener(
 *     new SeasarComponentInjectionListener(this, filters));
 * </pre>
 * <p>���̃t�B�[���h�t�B���^�����ł́C{@link SeasarComponent}�A�m�e�[�V������{@link SeasarComponent#name()}�v���p�e�B��
 * �w�肳�ꂽ���O���CSeasar�R���e�i����̃��b�N�A�b�v���̃R���|�[�l���g���Ƃ��č̗p���܂��B����{@link SeasarComponent#name()}
 * �v���p�e�B���L�q����Ă��Ȃ������ꍇ�́C�Ώۃt�B�[���h�̌^�����b�N�A�b�v���̃L�[�ɂȂ�܂��B</p>
 * 
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
