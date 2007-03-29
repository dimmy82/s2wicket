package org.seasar.wicket.uifactory;

import wicket.Component;

/**
 * Wicket�R���|�[�l���g�̍\�z���ɉ��炩�̃G���[�������������Ƃ��������s����O�N���X�ł��B
 * @author Yoichiro Tanaka
 * @since 1.3.0
 */
public class WicketUIFactoryException extends RuntimeException {
	
	/** �����Ώۂ������R���|�[�l���g */
	private Component target;

	/**
	 * ���̃I�u�W�F�N�g�����������Ƃ��ɌĂяo����܂��B
	 * @param target �����Ώۂ������R���|�[�l���g�I�u�W�F�N�g
	 * @param message �ڍׂȃ��b�Z�[�W
	 * @param cause �����ƂȂ�����O
	 */
	public WicketUIFactoryException(Component target, String message, Throwable cause) {
		super(message, cause);
		this.target = target;
	}

	/**
	 * ���̃I�u�W�F�N�g�����������Ƃ��ɌĂяo����܂��B
	 * @param target �����Ώۂ������R���|�[�l���g�I�u�W�F�N�g
	 * @param message �ڍׂȃ��b�Z�[�W
	 */
	public WicketUIFactoryException(Component target, String message) {
		super(message);
		this.target = target;
	}

	/**
	 * �ڍׂȃ��b�Z�[�W��Ԃ��܂��B
	 * @return �ڍׂȃ��b�Z�[�W
	 */
	@Override
	public String getMessage() {
		String message = super.getMessage();
		message += "[target=" + target.toString(true) + "]";
		return message;
	}
	
}
