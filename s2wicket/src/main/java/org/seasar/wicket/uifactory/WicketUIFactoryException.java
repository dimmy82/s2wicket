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
