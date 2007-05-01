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

import java.util.List;

import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.factory.SingletonS2ContainerFactory;
import org.seasar.wicket.injection.fieldfilters.AnnotationFieldFilter;

import wicket.Component;
import wicket.application.IComponentInstantiationListener;
import wicket.protocol.http.WebApplication;

/**
 * Seasar�Ǘ����ɂ���R���|�[�l���g��Wicket�R���|�[�l���g�����t�B�[���h�ɃC���W�F�N�V�������鏈�����s���N���X�ł��B
 * <p>���̃N���X�����C���W�F�N�V���������́CWicket�R���|�[�l���g���C���X�^���X�����ꂽ�ۂɍs���܂��B
 * �܂�C{@link IComponentInstantiationListener}�̎����N���X�Ƃ��Ē񋟂��܂��B</p>
 * <p>���̃N���X�̃I�u�W�F�N�g�̓o�^�́CApplication�N���X�̏����������̈�Ƃ��Ď��s���܂��B</p>
 * <pre>
 * public class OrderApplication extends WebApplication {
 * 
 *     public OrderApplication() {
 *         ...
 *     }
 *     
 *     &#064;Override
 *     protected void init() {
 *         super.init();
 *         ...
 *         addComponentInstantiationListener(
 *             new SeasarComponentInjectionListener(this));
 *         ...
 *     }
 * 
 * }
 * </pre>
 * <p>��L�̗�ł́CSeasar�R���e�i���K�؂ɑ��݂��邱�Ƃ��O������ƂȂ�܂��B
 * �܂�CS2ContainerServlet�Ȃǂ�Seasar�R���e�i����������C
 * SingletonS2ContainerFactory.getContainer()���\�b�h�ɂ����Seasar�R���e�i���擾�ł����Ԃ�
 * �Ȃ��Ă���K�v������܂��B</p>
 * <p>�C���W�F�N�V�����ΏۂƂ���t�B�[���h�̔��f������삵�����ꍇ�́C{@link FieldFilter}�C���^�t�F�[�X�̎����N���X��
 * �쐬���āCS2Wicket�ɓo�^����K�v������܂��B���̕��@�ɂ��ẮC{@link FieldFilter}�C���^�t�F�[�X�̐������������������B</p>
 * <p>{@link SeasarComponentInjectionListener}�I�u�W�F�N�g��Wicket�ւ̓o�^�́CApplication�N���X�i�̃T�u�N���X�j
 * �̃R���X�g���N�^���ł͋L�q���Ȃ��ł��������BApplication�N���X�̃R���X�g���N�^���œo�^���s���ƁC�X���b�h��Application�I�u�W�F�N�g��
 * �A�^�b�`����Ă��Ȃ����߁CS2Wicket���ŗ�O���������܂��Binit()���\�b�h���I�[�o�[���C�h���āC
 * ���̒��œo�^�������L�q���Ă��������B</p>
 * 
 * @see FieldFilter
 * @author Yoichiro Tanaka
 * @since 1.0.0
 */
public class SeasarComponentInjectionListener implements IComponentInstantiationListener {
	
	/** Seasar�R���|�[�l���g�C���W�F�N�V�����v���Z�b�T */
	private InjectionProcessor injectionProcessor;
	
	/**
	 * ���̃I�u�W�F�N�g�����������Ƃ��ɌĂяo����܂��B<br />
	 * <p>���̃R���X�g���N�^�ł́CSingletonS2ContainerFactory.getContainer()���\�b�h�ɂ����Seasar�R���e�i���擾�ł����Ԃ�
	 * �Ȃ��Ă���K�v������܂��B</p>
	 * <p>���̃R���X�g���N�^�𗘗p�����ꍇ�́C{@link AnnotationFieldFilter}�t�B�[���h�t�B���^�ƁCSeasar�R���e�i��
	 * �o�^����Ă���{@link FieldFilter}�C���^�t�F�[�X�̎����I�u�W�F�N�g���C�t�B�[���h�t�B���^�Ƃ��ēK�p����܂��B</p>
	 * @param application �A�v���P�[�V�����I�u�W�F�N�g
	 */
	public SeasarComponentInjectionListener(WebApplication application) {
		this(application, SingletonS2ContainerFactory.getContainer(), null);
	}
	
	/**
	 * ���̃I�u�W�F�N�g�����������Ƃ��ɌĂяo����܂��B<br />
	 * <p>���̃R���X�g���N�^���g�p���邱�Ƃɂ��C�茳�ɂ���Seasar�R���e�i�I�u�W�F�N�g���C���b�N�A�b�v�̑ΏۂƂ��邱�Ƃ��ł��܂��B</p>
	 * <p>���̃R���X�g���N�^�𗘗p�����ꍇ�́C{@link AnnotationFieldFilter}�t�B�[���h�t�B���^�ƁCSeasar�R���e�i��
	 * �o�^����Ă���{@link FieldFilter}�C���^�t�F�[�X�̎����I�u�W�F�N�g���C�t�B�[���h�t�B���^�Ƃ��ēK�p����܂��B</p>
	 * @param application �A�v���P�[�V�����I�u�W�F�N�g
	 * @param container Seasar�R���e�i�I�u�W�F�N�g
	 */
	public SeasarComponentInjectionListener(WebApplication application, S2Container container) {
		this(application, container, null);
	}
	
	/**
	 * ���̃I�u�W�F�N�g�����������Ƃ��ɌĂяo����܂��B<br />
	 * <p>���̃R���X�g���N�^�ł́CSingletonS2ContainerFactory.getContainer()���\�b�h�ɂ����Seasar�R���e�i���擾�ł����Ԃ�
	 * �Ȃ��Ă���K�v������܂��B</p>
	 * <p>���̃R���X�g���N�^���g�p����ꍇ�́C�����Ɏw�肳�ꂽ�t�B�[���h�t�B���^���K�p����܂��B�܂�C
	 * {@link AnnotationFieldFilter}�I�u�W�F�N�g��Seasar�R���e�i�ɓo�^���ꂽ{@link FieldFilter}�I�u�W�F�N�g��
	 * �ÖٓI�Ɏg�p����邱�Ƃ͂���܂���B</p>
	 * @param application �A�v���P�[�V�����I�u�W�F�N�g
	 * @param fieldFilters �t�B�[���h�t�B���^���i�[���ꂽ�R���N�V����
	 */
	public SeasarComponentInjectionListener(WebApplication application, List<FieldFilter> fieldFilters) {
		this(application, SingletonS2ContainerFactory.getContainer(), fieldFilters);
	}
	
	/**
	 * ���̃I�u�W�F�N�g�����������Ƃ��ɌĂяo����܂��B<br />
	 * <p>���̃R���X�g���N�^���g�p���邱�Ƃɂ��C�茳�ɂ���Seasar�R���e�i�I�u�W�F�N�g���C���b�N�A�b�v�̑ΏۂƂ��邱�Ƃ��ł��܂��B</p>
	 * <p>���̃R���X�g���N�^���g�p����ꍇ�́C�����Ɏw�肳�ꂽ�t�B�[���h�t�B���^���K�p����܂��B�܂�C
	 * {@link AnnotationFieldFilter}�I�u�W�F�N�g��Seasar�R���e�i�ɓo�^���ꂽ{@link FieldFilter}�I�u�W�F�N�g��
	 * �ÖٓI�Ɏg�p����邱�Ƃ͂���܂���B</p>
	 * @param application �A�v���P�[�V�����I�u�W�F�N�g
	 * @param container Seasar�R���e�i�I�u�W�F�N�g
	 * @param fieldFilters �t�B�[���h�t�B���^���i�[���ꂽ�R���N�V����
	 */
	public SeasarComponentInjectionListener(
			WebApplication application, S2Container container, List<FieldFilter> fieldFilters) {
		super();
		// �����`�F�b�N
		if (application == null)
			throw new IllegalArgumentException("application is null.");
		if (container == null)
			throw new IllegalArgumentException("container is null.");
		if ((fieldFilters != null) && (fieldFilters.size() == 0))
			throw new IllegalArgumentException("fieldFilters is empty.");
		// Seasar�R���e�i�I�u�W�F�N�g��Wicket���^�f�[�^�Ƃ��Ċi�[
		S2ContainerHolder.store(application, container);
		// �C���W�F�N�V�����v���Z�b�T�𐶐�
		if (fieldFilters == null) {
			injectionProcessor = new InjectionProcessor(new S2ContainerLocator());
		} else {
			injectionProcessor = new InjectionProcessor(new S2ContainerLocator(), fieldFilters);
		}
	}

	/**
	 * �R���|�[�l���g���C���X�^���X�����ꂽ�Ƃ��ɌĂяo����܂��B<br />
	 * <p>�����ł́CSeasar�R���|�[�l���g�̌Ăяo�����s�����I�v���L�V�̃C���W�F�N�V�����������s���܂��B</p>
	 * @param component �����Ώۂ̃R���|�[�l���g�I�u�W�F�N�g
	 */
	public void onInstantiation(Component component) {
		// �C���W�F�N�V���������s
		injectionProcessor.inject(component);
	}

}
