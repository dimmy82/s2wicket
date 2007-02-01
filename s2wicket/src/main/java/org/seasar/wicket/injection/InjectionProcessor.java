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
import java.util.LinkedList;
import java.util.List;

import wicket.Component;
import wicket.MarkupContainer;
import wicket.Page;
import wicket.markup.html.WebPage;
import wicket.markup.html.panel.Panel;

/**
 * SeasarComponent�A�m�e�[�V���������t�B�[���h�ɃC���W�F�N�V�������s�����������N���X�ł��B
 * @author Yoichiro Tanaka
 * @since 1.0.0
 */
class InjectionProcessor {
	
	/** Seasar�R���e�i���P�[�^ */
	private IS2ContainerLocator containerLocator;
	
	/**
	 * ���̃I�u�W�F�N�g�����������Ƃ��ɌĂяo����܂��B
	 * @param containerLocator Seasar�R���e�i���P�[�^
	 */
	InjectionProcessor(IS2ContainerLocator containerLocator) {
		super();
		if (containerLocator == null)
			throw new IllegalArgumentException("containerLocator is null.");
		this.containerLocator = containerLocator;
	}
	
	/**
	 * �w�肳�ꂽ�I�u�W�F�N�g������SeasarComponent�A�m�e�[�V�����ɑ΂��āC�C���W�F�N�V�������s���܂��B
	 * @param target �����Ώۂ̃I�u�W�F�N�g
	 */
	void inject(Object target) {
		// �t�B�[���h�l�����I�u�W�F�N�g�𐶐�
		FieldValueProducer fieldValueProducer = new FieldValueProducer(containerLocator);
		// �C���W�F�N�V���������s
		inject(target, fieldValueProducer);
	}
	
	/**
	 * �w�肳�ꂽ�I�u�W�F�N�g������SeasarComponent�A�m�e�[�V�����ɑ΂��āC�w�肳�ꂽ
	 * �t�B�[���h�l�����I�u�W�F�N�g����Seasar�R���|�[�l���g���擾���āC������Ăяo�����I�v���L�V��
	 * �C���W�F�N�V�������܂��B
	 * @param target �ΏۃI�u�W�F�N�g
	 * @param fieldValueProducer �t�B�[���h�l�����I�u�W�F�N�g
	 */
	private void inject(Object target, FieldValueProducer fieldValueProducer) {
		// �ΏۃI�u�W�F�N�g�̃N���X�I�u�W�F�N�g���擾
		Class<? extends Object> clazz = target.getClass();
		// �C���W�F�N�V���������Ώۂ̃t�B�[���h���擾
		Field[] targetFields = getTargetFields(clazz, fieldValueProducer);
		// �t�B�[���h���ɏ���
		for (int i = 0; i < targetFields.length; i++) {
			// �t�B�[���h�ɃA�N�Z�X�ł���悤�ɂ���
			if (!targetFields[i].isAccessible()) {
				targetFields[i].setAccessible(true);
			}
			try {
				// �ΏۃI�u�W�F�N�g�̃t�B�[���h�l��null���`�F�b�N
				if (targetFields[i].get(target) == null) {
					// �t�B�[���h�l�Ƃ���v���L�V�I�u�W�F�N�g���擾
					Object fieldValue = fieldValueProducer.getValue(targetFields[i]);
					// �t�B�[���h�ɃC���W�F�N�g
					targetFields[i].set(target, fieldValue);
				}
			} catch(AnnotationNotPresentsException e) {
				// N/A
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
	private Field[] getTargetFields(Class<? extends Object> clazz, FieldValueProducer fieldValueProducer) {
		// ���ʂ��i�[����R���N�V�����𐶐�
		List<Field> resultList = new LinkedList<Field>();
		// �����Ώۂ��Ȃ��Ȃ邩�CWicket�񋟃N���X�ɂȂ�܂ŌJ��Ԃ�
		while((clazz != null) && (!(isWicketClass(clazz)))) {
			// ��`����Ă���t�B�[���h���擾
			Field[] fields = clazz.getDeclaredFields();
			// �t�B�[���h���ɏ���
			for (int i = 0; i < fields.length; i++) {
				// �T�|�[�g����Ă���t�B�[���h���`�F�b�N
				if (fieldValueProducer.isSupported(fields[i])) {
					// ���ʂ̃R���N�V�����ɒǉ�
					resultList.add(fields[i]);
				}
			}
			// �X�[�p�[�N���X���擾�����l�̌������s��
			clazz = clazz.getSuperclass();
		}
		// ���ʂ�ԋp
		return resultList.toArray(new Field[0]);
	}
	
	/**
	 * �w�肳�ꂽ�N���X��Wicket�Œ񋟂��ꂽ�N���X���ǂ�����Ԃ��܂��B
	 * @param clazz �N���X�I�u�W�F�N�g
	 * @return WebPage, Page, Panel, MarkupContainer, Component �N���X�������ꍇ�� true
	 */
	private boolean isWicketClass(Class clazz) {
		return (clazz.equals(WebPage.class))
			|| (clazz.equals(Page.class))
			|| (clazz.equals(Panel.class))
			|| (clazz.equals(MarkupContainer.class))
			|| (clazz.equals(Component.class));
	}

}
