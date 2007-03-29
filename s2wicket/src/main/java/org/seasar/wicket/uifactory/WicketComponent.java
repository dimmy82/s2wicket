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

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Wicket�R���|�[�l���g�̐�����S2Wicket�Ɏw�����邽�߂̃A�m�e�[�V�����ł��B<br />
 * <p>Wicket���g�p�����J���ł́C�y�[�W���\�����镡���̃R���|�[�l���g�𐶐����C�����e�R���|�[�l���g�ɓo�^����Ƃ����C
 * �R���|�[�l���g�c���[�̍\�z�������L�q����K�v������܂��B����́CSwing��SWT�ɑ�\�����GUI�A�v���P�[�V�����ɂ�����
 * ��ʂ̍\�z�����̋L�q�Ɣ��ɋ߂����̂ł��B
 * 
 * 
 * @see WicketAction
 * @see WicketModel
 * @author Yoichiro Tanaka
 * @since 1.3.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Documented
public @interface WicketComponent {
	
	/**
	 * ��������R���|�[�l���g��wicket:id���w�肷�邽�߂̑����i�C�Ӂj�ł��B
	 * ���̑������ȗ������ꍇ�C���̃A�m�e�[�V�������t�^���ꂽ�t�B�[���h�̖��O��wicket:id�Ƃ��č̗p����܂��B
	 * @return wicket:id�Ƃ��镶����
	 */
	public String wicketId() default "";
	
	/**
	 * ��������R���|�[�l���g��o�^����e�̃R���e�i�R���|�[�l���g�̃t�B�[���h�����w�肷�邽�߂̑����i�C�Ӂj�ł��B
	 * ���̑������ȗ������ꍇ�C�܂���"this"���w�肵���ꍇ�́C
	 * ���̃A�m�e�[�V�������t�^���ꂽ�t�B�[���h����������R���|�[�l���g���e�R���e�i�R���|�[�l���g�Ƃ��č̗p����܂��B
	 * @return �e�̃R���e�i�R���|�[�l���g�̃t�B�[���h��
	 */
	public String parent() default "";
	
	/**
	 * ��������R���|�[�l���g�Ɗ֘A�t�����s�����f���I�u�W�F�N�g�̃t�B�[���h�����w�肷�邽�߂̑����i�C�Ӂj�ł��B
	 * 
	 * @return
	 */
	public String modelName() default "";
	
	public String modelProperty() default "";
	
	public WicketAction[] actions() default {};
	
}
