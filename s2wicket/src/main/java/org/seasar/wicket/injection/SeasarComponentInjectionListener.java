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

import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.factory.SingletonS2ContainerFactory;

import wicket.Component;
import wicket.application.IComponentInstantiationListener;
import wicket.protocol.http.WebApplication;

/**
 * Seasar�Ǘ����ɂ���R���|�[�l���g��Wicket�R���|�[�l���g������SeasarComponent�A�m�e�[�V������
 * �t�^���ꂽ�t�B�[���h�ɃC���W�F�N�V�������鏈�����s���N���X�ł��B<br />
 * ���̃N���X�����C���W�F�N�V���������́CWicket�R���|�[�l���g���C���X�^���X�����ꂽ�ۂɍs���܂��B
 * �܂�C{@link IComponentInstantiationListener}�̎����N���X�Ƃ��Ē񋟂��܂��B
 * ���̃N���X�̃I�u�W�F�N�g�̓o�^�́CApplication�N���X�̏����������̈�Ƃ��Ď��s���܂��B<br />
 * <pre>
 * public class OrderApplication extends WebApplication {
 * 
 *     public OrderApplication() {
 *         ...
 *         addComponentInstantiationListener(
 *             new SeasarComponentInjectionListener(this));
 *         ...
 *     }
 * 
 * }
 * </pre>
 * ��L�̗�ł́CSeasar�R���e�i���K�؂ɑ��݂��邱�Ƃ��O������ƂȂ�܂��B
 * �܂�CS2ContainerServlet�Ȃǂ�Seasar�R���e�i����������C
 * SingletonS2ContainerFactory.getContainer()���\�b�h�ɂ����Seasar�R���e�i���擾�ł����Ԃ�
 * �Ȃ��Ă���K�v������܂��B
 * 
 * @author Yoichiro Tanaka
 */
public class SeasarComponentInjectionListener implements IComponentInstantiationListener {
	
	/** Seasar�R���|�[�l���g�C���W�F�N�V�����v���Z�b�T */
	private InjectionProcessor injectionProcessor;
	
	/**
	 * ���̃I�u�W�F�N�g�����������Ƃ��ɌĂяo����܂��B<br />
	 * ���̃R���X�g���N�^�ł́CSingletonS2ContainerFactory.getContainer()���\�b�h�ɂ����Seasar�R���e�i���擾�ł����Ԃ�
	 * �Ȃ��Ă���K�v������܂��B
	 * @param application �A�v���P�[�V�����I�u�W�F�N�g
	 */
	public SeasarComponentInjectionListener(WebApplication application) {
		this(application, SingletonS2ContainerFactory.getContainer());
	}
	
	/**
	 * ���̃I�u�W�F�N�g�����������Ƃ��ɌĂяo����܂��B
	 * @param application �A�v���P�[�V�����I�u�W�F�N�g
	 * @param container Seasar�R���e�i�I�u�W�F�N�g
	 */
	public SeasarComponentInjectionListener(WebApplication application, S2Container container) {
		super();
		// �����`�F�b�N
		if (application == null)
			throw new IllegalArgumentException("application is null.");
		if (container == null)
			throw new IllegalArgumentException("container is null.");
		// Seasar�R���e�i�I�u�W�F�N�g��Wicket���^�f�[�^�Ƃ��Ċi�[
		S2ContainerHolder.store(application, container);
		// �C���W�F�N�V�����v���Z�b�T�𐶐� */
		injectionProcessor = new InjectionProcessor(new S2ContainerLocator());
	}

	/**
	 * �R���|�[�l���g���C���X�^���X�����ꂽ�Ƃ��ɌĂяo����܂��B
	 * �����ł́CSeasar�R���|�[�l���g�̌Ăяo�����s�����I�v���L�V�̃C���W�F�N�V�����������s���܂��B
	 * @param component �����Ώۂ̃R���|�[�l���g�I�u�W�F�N�g
	 */
	public void onInstantiation(Component component) {
		// �C���W�F�N�V���������s
		injectionProcessor.inject(component);
	}

}
