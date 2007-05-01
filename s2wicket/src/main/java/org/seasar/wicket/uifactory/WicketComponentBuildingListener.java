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

import org.seasar.wicket.injection.SeasarComponentInjectionListener;

import wicket.Application;
import wicket.Component;
import wicket.application.IComponentInstantiationListener;
import wicket.markup.html.WebPage;
import wicket.markup.html.form.Form;
import wicket.markup.html.panel.Panel;

/**
 * S2Wicket���񋟂���UI�R���|�[�l���g�\�z�@�\�𗘗p�\�ɂ��邽�߂̃N���X�ł��B<br />
 * <p>���̃N���X�ɂ���ė��p�\�ƂȂ�UI�R���|�[�l���g�̍\�z�����́CWicket�R���|�[�l���g���C���X�^���X�����ꂽ�ۂɍs���܂��B
 * �܂�C{@link IComponentInstantiationListener}�̎����N���X�Ƃ��Ē񋟂��܂��B</p>
 * <p>���̃N���X�̃I�u�W�F�N�g�̓o�^�́C{@link Application}�N���X�̏����������̈�Ƃ��Ď��s���܂��B</p>
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
 *             new WicketComponentBuildingListener(this));
 *         ...
 *     }
 * 
 * }
 * </pre>
 * <p>{@link WicketComponentBuildingListener}�I�u�W�F�N�g��{@link Application}�I�u�W�F�N�g�ɓo�^���ꂽ��ɁC
 * {@link WebPage}�N���X��{@link Panel}�N���X�C{@link Form}�N���X���p�������A�v���P�[�V�����N���X�̂Ȃ��ŁC
 * {@link WicketComponent}�A�m�e�[�V������{@link WicketModel}�A�m�e�[�V�������g�p���邱�Ƃ��ł���悤�ɂȂ�܂��B
 * �eWicket�R���|�[�l���g���C���X�^���X�����ꂽ�ۂɁCS2Wicket�͊e�t�B�[���h�ɕt�^���ꂽ�A�m�e�[�V�����Ɋ�Â��āC
 * �R���|�[�l���g�I�u�W�F�N�g�̐����C���f���I�u�W�F�N�g�̐����C�e�̃R���e�i�R���|�[�l���g�ւ̓o�^�������s���܂��B
 * ���ɃR���|�[�l���g�I�u�W�F�N�g�́C���̃R���|�[�l���g���p���������I�v���L�V��������������C
 * �C�x���g�����Ȃǂ�{@link WicketAction}�A�m�e�[�V�����ɂ��L�q���ꂽOGNL���̕]���Ŏ������邱�Ƃ��\�ƂȂ�܂��B</p>
 * �X�̏����ɂ��ẮC�e�A�m�e�[�V������Javadoc���������������B</p>
 * <p>�\�z�����UI�R���|�[�l���g�I�u�W�F�N�g�̐�����֘A�t�����郂�f���I�u�W�F�N�g�̐��������ɂ��āC
 * Seasar�R���e�i���񋟂���R���|�[�l���g�̏������Ăяo�������ꍇ�́C{@link WicketComponentBuildingListener}�I�u�W�F�N�g��
 * {@link Application}�I�u�W�F�N�g�ւ̓o�^�̑O�ɁC{@link SeasarComponentInjectionListener}�I�u�W�F�N�g��
 * {@link Application}�I�u�W�F�N�g�ɓo�^���Ă����K�v������܂��B</p>
 * <p>{@link WicketComponentBuildingListener}�I�u�W�F�N�g��Wicket�ւ̓o�^�́CApplication�N���X�i�̃T�u�N���X�j
 * �̃R���X�g���N�^���ł͋L�q���Ȃ��ł��������BApplication�N���X�̃R���X�g���N�^���œo�^���s���ƁC�X���b�h��Application�I�u�W�F�N�g��
 * �A�^�b�`����Ă��Ȃ����߁CS2Wicket���ŗ�O���������܂��Binit()���\�b�h���I�[�o�[���C�h���āC
 * ���̒��œo�^�������L�q���Ă��������B</p>
 * 
 * @see WicketComponent
 * @see WicketModel
 * @see WicketAction
 * @see SeasarComponentInjectionListener
 * @author Yoichiro Tanaka
 * @since 1.3.0
 */
public class WicketComponentBuildingListener implements IComponentInstantiationListener {

	/** Wicket�R���|�[�l���g�r���f�B���O�v���Z�b�T */
	private BuildingProcessor buildingProcessor;
	
	/**
	 * ���̃I�u�W�F�N�g�����������Ƃ��ɌĂяo����܂��B
	 */
	public WicketComponentBuildingListener() {
		super();
		// �r���f�B���O�v���Z�b�T�𐶐�
		buildingProcessor = new BuildingProcessor();
	}

	/**
	 * �R���|�[�l���g���C���X�^���X�����ꂽ�Ƃ��ɌĂяo����܂��B<br />
	 * <p>�����ł́CWicket�R���|�[�l���g�̃r���f�B���O�������s���܂��B</p>
	 * @param component �����Ώۂ̃R���|�[�l���g�I�u�W�F�N�g
	 */
	public void onInstantiation(Component component) {
		// �r���f�B���O�v���Z�b�T�ɏ������Ϗ�
		buildingProcessor.build(component);
	}

}
