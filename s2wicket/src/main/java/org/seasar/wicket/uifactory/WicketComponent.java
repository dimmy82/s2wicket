package org.seasar.wicket.uifactory;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Wicket�R���|�[�l���g�̐�����S2Wicket�Ɏw�����邱�Ƃ������A�m�e�[�V�����ł��B
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
