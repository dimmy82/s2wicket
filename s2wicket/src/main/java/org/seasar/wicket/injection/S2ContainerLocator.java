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

/**
 * Seasar�R���e�i�I�u�W�F�N�g���擾���邽�߂̏��������N���X�ł��B
 * @author Yoichiro Tanaka
 */
class S2ContainerLocator implements Serializable, IS2ContainerLocator {
	
	/**
	 * ���̃I�u�W�F�N�g�����������Ƃ��ɌĂяo����܂��B
	 */
	S2ContainerLocator() {
		super();
	}
	
	/**
	 * Wicket���^�f�[�^����Seasar�R���e�i�I�u�W�F�N�g�����o���܂��B
	 * @return Seasar�R���e�i�I�u�W�F�N�g
	 */
	public S2Container get() {
		Application application = Application.get();
		return ((S2ContainerHolder)application.getMetaData(S2ContainerHolder.META_DATA_KEY)).getContainer();
	}

}
