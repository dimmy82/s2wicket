package org.seasar.wicket.uifactory;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Wicket�R���|�[�l���g�𐶐����邱�Ƃ������A�m�e�[�V�����ł��B
 * @author Yoichiro Tanaka
 * @since 1.3.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Documented
public @interface WicketComponent {
	
//	/**
//	 * Wicket�R���|�[�l���g�𐶐����鏈�����s�����\�b�h�̖��O�ł��B
//	 * ���̑������ȗ������ꍇ�́C"create[�t�B�[���h��]Component()"�Ƃ������O�̃��\�b�h���������C
//	 * ��������΂��̃��\�b�h���R�[���C�����Ȃ����Wicket�R���|�[�l���g�̐�����S2Wicket���g���s���܂��B
//	 * @return Wicket�R���|�[�l���g�𐶐����鏈�����s�����\�b�h�̖��O
//	 */
//	public String factoryMethodName() default "";
	
	public String wicketId() default "";
	
	public String parent() default "";
	
	public String model() default "";
	
	public String property() default "";
	
}
