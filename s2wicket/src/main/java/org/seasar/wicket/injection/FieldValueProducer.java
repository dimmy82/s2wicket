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
 * SeasarComponent�A�m�e�[�V�������t�^���ꂽ�t�B�[���h�ɃZ�b�g����l����������N���X�ł��B
 * ���ۂ�Seasar�R���|�[�l���g�̃��\�b�h�̌Ăяo�����s����v���L�V�I�u�W�F�N�g��񋟂��܂��B
 * @author Yoichiro Tanaka
 * @since 1.0.0
 */
class FieldValueProducer {
	
	/** Seasar�R���e�i���P�[�^ */
	private IS2ContainerLocator containerLocator;
	
	/**
	 * ���̃I�u�W�F�N�g�����������Ƃ��ɌĂяo����܂��B
	 * @param containerLocator �R���e�i���P�[�^
	 */
	FieldValueProducer(IS2ContainerLocator containerLocator) {
		super();
		if (containerLocator == null)
			throw new IllegalArgumentException("containerLocator is null.");
		this.containerLocator = containerLocator;
	}
	
	/**
	 * �w�肳�ꂽ�t�B�[���h�ɑΉ�����v���L�V�I�u�W�F�N�g�𐶐����ĕԂ��܂��B
	 * @param field �t�B�[���h�I�u�W�F�N�g
	 * @return �v���L�V�I�u�W�F�N�g
	 */
	Object getValue(Field field) throws AnnotationNotPresentsException {
		// �����`�F�b�N
		if (field == null)
			throw new IllegalArgumentException("field is null.");
		// �t�B�[���h�ɃA�m�e�[�V�������t�^����Ă��邩�`�F�b�N
		if (field.isAnnotationPresent(SeasarComponent.class)) {
			// �A�m�e�[�V�����I�u�W�F�N�g���擾
			SeasarComponent annotation = field.getAnnotation(SeasarComponent.class);
			// �R���|�[�l���g�����擾
			String componentName = annotation.name();
			// Seasar�R���|�[�l���g���]���o�𐶐�
			ComponentResolver resolver = new ComponentResolver(componentName, field.getType(), containerLocator);
			// �v���L�V�𐶐�
			Object proxy = ProxyFactory.create(field.getType(), resolver);
			// �v���L�V��ԋp
			return proxy;
		} else {
			throw new AnnotationNotPresentsException();
		}
	}
	
	/**
	 * �w�肳�ꂽ�t�B�[���h���C���W�F�N�V�����̑ΏۂƂ��ăT�|�[�g����Ă��邩�ǂ�����Ԃ��܂��B
	 * @param field �t�B�[���h
	 * @return �T�|�[�g����Ă���� true
	 */
	boolean isSupported(Field field) {
		return field.isAnnotationPresent(SeasarComponent.class);
	}

}
