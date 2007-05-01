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

import org.seasar.framework.container.S2Container;

import wicket.Application;
import wicket.MetaDataKey;

/**
 * Seasar�R���e�i�I�u�W�F�N�g��ێ�����N���X�ł��B
 * @author Yoichiro Tanaka
 * @since 1.0.0
 */
class S2ContainerHolder implements Serializable {
	
	/** ���̃z���_�[��Wicket���^�f�[�^�Ƃ��Ċi�[����ۂ̃L�[ */
	static final MetaDataKey META_DATA_KEY = new MetaDataKey(S2ContainerHolder.class) {};
	
	/** Seasar�R���e�i�I�u�W�F�N�g */
	private S2Container container;
	
	/**
	 * ���̃I�u�W�F�N�g�����������Ƃ��ɌĂяo����܂��B
	 * @param container Seasar�R���e�i�I�u�W�F�N�g
	 */
	private S2ContainerHolder(S2Container container) {
		super();
		// �����`�F�b�N
		if (container == null)
			throw new IllegalArgumentException("container is null.");
		// �������t�B�[���h�ɕێ�
		this.container = container;
	}
	
	/**
	 * Seasar�R���e�i�I�u�W�F�N�g��Ԃ��܂��B
	 * @return Seasar�R���e�i�I�u�W�F�N�g
	 */
	S2Container getContainer() {
		return container;
	}
	
	/**
	 * Seasar�R���e�i�I�u�W�F�N�g��Wicket���^�f�[�^�Ƃ��Ċi�[���܂��B
	 * @param application �A�v���P�[�V�����I�u�W�F�N�g
	 * @param container Seasar�R���e�i�I�u�W�F�N�g
	 */
	static void store(Application application, S2Container container) {
		S2ContainerHolder holder = new S2ContainerHolder(container);
		application.setMetaData(META_DATA_KEY, holder);
	}
	
}
