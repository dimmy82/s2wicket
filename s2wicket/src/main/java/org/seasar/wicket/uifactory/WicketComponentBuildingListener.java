package org.seasar.wicket.uifactory;

import wicket.Component;
import wicket.application.IComponentInstantiationListener;

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
