package org.seasar.wicket.uifactory;

/**
 * ���f���̎�ʂ��`�����񋓌^�ł��B
 * @author Yoichiro Tanaka
 * @since 1.3.0
 */
public enum ModelType {

	/**
	 * ���̃��f���I�u�W�F�N�g�����̂܂܃R���|�[�l���g�Ɋ֘A�t���܂��B
	 */
	RAW,
	
	/**
	 * ��{���f���Ƃ��āC���̃��f���I�u�W�F�N�g���R���|�[�l���g�Ɋ֘A�t�����܂��B
	 */
	BASIC,
	
	/**
	 * �v���p�e�B���f���Ƃ��āC���̃��f���I�u�W�F�N�g���R���|�[�l���g�Ɋ֘A�t�����܂��B
	 */
	PROPERTY,
	
	/**
	 * �����v���p�e�B���f���Ƃ��āC���̃��f���I�u�W�F�N�g���R���|�[�l���g�Ɋ֘A�t�����܂��B
	 */
	COMPOUND_PROPERTY,
	
	/**
	 * �����o�C���h�v���p�e�B���f���Ƃ��āC���̃��f���I�u�W�F�N�g���R���|�[�l���g�Ɋ֘A�t�����܂��B
	 */
	BOUND_COMPOUND_PROPERTY
	
}
