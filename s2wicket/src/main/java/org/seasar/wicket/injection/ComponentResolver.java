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

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.seasar.framework.container.S2Container;

/**
 * Seasar�R���|�[�l���g��Seasar�R���e�i���烋�b�N�A�b�v���鏈�������R���|�[�l���g���]���o�N���X�ł��B
 * @author Yoichiro Tanaka
 * @since 1.0.0
 */
class ComponentResolver implements Serializable {
	
	/** �R���|�[�l���g�� */
	private String componentName;
	/** �R���|�[�l���g�̌^�̖��O */
	private String componentTypeName;
	/** �R���|�[�l���g�̌^�̃L���b�V�� */
	private transient Class componentTypeCache;
	/** Seasar�R���e�i���P�[�^ */
	private IS2ContainerLocator containerLocator;
	
	/**
	 * ���̃I�u�W�F�N�g�����������Ƃ��ɌĂяo����܂��B
	 * @param componentName �R���|�[�l���g�� 
	 * @param componentType �R���|�[�l���g�̌^
	 * @param containerLocator Seasar�R���e�i���P�[�^
	 */
	ComponentResolver(String componentName, Class componentType, IS2ContainerLocator containerLocator) {
		super();
		// �����`�F�b�N
		if (componentType == null)
			throw new IllegalArgumentException("componentType is null.");
		if (containerLocator == null)
			throw new IllegalArgumentException("containerLocator is null.");
		// �������t�B�[���h�ɕێ�
		this.componentName = componentName;
		this.componentTypeName = componentType.getName();
		this.containerLocator = containerLocator;
	}
	
	/**
	 * �R���|�[�l���g�̌^�I�u�W�F�N�g��Ԃ��܂��B
	 * �R���|�[�l���g�̌^�̓L���b�V������Ă��܂��B�����Z�b�V������Passivate���ꂽ�ۂȂǂɂ́C�L���b�V���͏�������܂��B
	 * ���̍ۂɂ́C�ēx�^�I�u�W�F�N�g���N���X���[�_�ɂ��ǂݍ��܂�܂��B
	 * @return �R���|�[�l���g�̌^�I�u�W�F�N�g
	 */
	private Class getComponentType() {
		if (componentTypeCache == null) {
			try {
				componentTypeCache = Class.forName(componentTypeName, true, Thread.currentThread().getContextClassLoader());
				return componentTypeCache;
			} catch (ClassNotFoundException e) {
				throw new IllegalStateException("Class[" + componentTypeName + "] loading failed.", e);
			}
		} else {
			return componentTypeCache;
		}
	}
	
	/**
	 * �C���W�F�N�V�����Ώۂ̃I�u�W�F�N�g��Seasar�R���e�i��胋�b�N�A�b�v���C���̌��ʂ�Ԃ��܂��B
	 * @return �C���W�F�N�V�����Ώۂ̃I�u�W�F�N�g
	 */
	Object getTargetObject() {
		// �R���e�i���擾
		S2Container container = containerLocator.get();
		// �R���|�[�l���g�����w�肳�ꂽ���`�F�b�N
		if (StringUtils.isEmpty(componentName)) {
			// �^���g���ăR���|�[�l���g�I�u�W�F�N�g�����b�N�A�b�v
			return lookupSeasarComponentByType(container);
		} else {
			// ���O���g���ăR���|�[�l���g�I�u�W�F�N�g�����b�N�A�b�v
			return lookupSeasarComponentByName(container);
		}
	}

	/**
	 * �R���|�[�l���g�����g���āCSeasar�R���e�i�ɓo�^���ꂽ�R���|�[�l���g�I�u�W�F�N�g�����b�N�A�b�v���C���̌��ʂ�Ԃ��܂��B
	 * @param container �R���e�i�I�u�W�F�N�g
	 * @return �R���|�[�l���g�I�u�W�F�N�g
	 */
	private Object lookupSeasarComponentByName(S2Container container) {
		// �R���e�i��胋�b�N�A�b�v
		Object component = container.getComponent(componentName);
		// ���ʂ�ԋp
		return component;
	}

	/**
	 * �R���|�[�l���g�̌^���g���āCSeasar�R���e�i�ɓo�^���ꂽ�R���|�[�l���g�I�u�W�F�N�g�����b�N�A�b�v���C���̌��ʂ�Ԃ��܂��B
	 * @param container �R���e�i�I�u�W�F�N�g
	 * @return �R���|�[�l���g�I�u�W�F�N�g
	 */
	private Object lookupSeasarComponentByType(S2Container container) {
		// �R���|�[�l���g�̌^���擾
		Class componentType = getComponentType();
		// �R���e�i��胋�b�N�A�b�v
		Object component = container.getComponent(componentType);
		// ���ʂ�ԋp
		return component;
	}
	
	/**
	 * ���̃I�u�W�F�N�g�ƈ����ŗ^����ꂽ�I�u�W�F�N�g�̓��e�̈�v����Ԃ��܂��B
	 * @param obj ��r�Ώۂ̃I�u�W�F�N�g
	 * @return ���e����v����� true
	 */
	public boolean equals(Object obj) {
		if (!(obj instanceof ComponentResolver)) {
			return false;
		} else {
			ComponentResolver target = (ComponentResolver)obj;
			return new EqualsBuilder()
				.append(componentName, target.componentName)
				.append(componentTypeName, target.componentTypeName)
				.isEquals();
		}
	}
	
	/**
	 * �n�b�V���l��Ԃ��܂��B
	 * @return �n�b�V���l
	 */
	public int hashCode() {
		int result = componentTypeName.hashCode();
		if (componentName != null) {
			result += componentName.hashCode() * 127;
		}
		return result;
	}

}
